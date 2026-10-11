<template>
  <div class="space-y-6">
    <!-- Header -->
    <div class="flex items-center justify-between">
      <div>
        <h1 class="text-3xl font-bold text-gray-900">Gestion des Postes</h1>
        <p class="text-gray-500 mt-1">Superviser tous les postes électriques STEG</p>
      </div>
      <div class="flex gap-3">
        <Button
          v-if="canCreate"
          variant="secondary"
          size="lg"
          @click="showImportModal = true"
        >
          📥 Importer Excel
        </Button>
        <Button
          v-if="canCreate"
          variant="primary"
          size="lg"
          @click="showCreateForm = true"
        >
          ➕ Ajouter Poste
        </Button>
      </div>
    </div>

    <!-- Data Loader Wrapper -->
    <DataLoader
      :loading="loading"
      :error="error"
      error-title="Impossible de charger les postes"
      @retry="loadPostes"
    >
      <!-- Stats Cards -->
      <div class="grid grid-cols-1 md:grid-cols-4 gap-4 mb-6">
        <KPICard
          title="Total Postes"
          :value="stats.total"
          color="bg-blue-500"
        />
        <KPICard
          title="Actifs"
          :value="stats.actifs"
          color="bg-green-500"
        />
        <KPICard
          title="Inactifs"
          :value="stats.inactifs"
          color="bg-yellow-500"
        />
        <KPICard
          title="Maintenance"
          :value="stats.maintenance"
          color="bg-red-500"
        />
      </div>

      <!-- Search & Filters -->
      <Card class="mb-6">
        <template #header>
          <h2 class="text-lg font-semibold">Recherche & Filtres</h2>
        </template>
        <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Recherche</label>
            <input
              v-model="filters.nom"
              @input="handleSearch"
              type="text"
              placeholder="Nom..."
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Région</label>
            <input
              v-model="filters.region"
              @input="handleSearch"
              type="text"
              placeholder="Région..."
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">État</label>
            <select
              v-model="filters.etat"
              @change="handleSearch"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="">Tous les états</option>
              <option value="ACTIF">Actif</option>
              <option value="INACTIF">Inactif</option>
              <option value="MAINTENANCE">Maintenance</option>
            </select>
          </div>
          <div class="flex items-end">
            <Button
              variant="secondary"
              size="md"
              @click="resetFilters"
              class="w-full"
            >
              Réinitialiser
            </Button>
          </div>
        </div>
      </Card>

      <!-- Table -->
      <Card>
        <template #header>
          <h2 class="text-lg font-semibold">Liste des Postes</h2>
        </template>
        
        <div v-if="postes.length === 0" class="text-center py-12">
          <p class="text-gray-500 text-lg">Aucun poste trouvé</p>
        </div>

        <div v-else class="overflow-x-auto">
          <table class="min-w-full">
            <thead class="bg-gray-50 border-b">
              <tr>
                <th class="px-6 py-3 text-left text-sm font-semibold text-gray-700">Nom</th>
                <th class="px-6 py-3 text-left text-sm font-semibold text-gray-700">Code</th>
                <th class="px-6 py-3 text-left text-sm font-semibold text-gray-700">Région</th>
                <th class="px-6 py-3 text-left text-sm font-semibold text-gray-700">État</th>
                <th class="px-6 py-3 text-left text-sm font-semibold text-gray-700">Transformateurs</th>
                <th class="px-6 py-3 text-right text-sm font-semibold text-gray-700">Actions</th>
              </tr>
            </thead>
            <tbody class="divide-y">
              <tr v-for="poste in postes" :key="poste.id" class="hover:bg-gray-50">
                <td class="px-6 py-4 text-gray-900 font-medium">{{ poste.nom }}</td>
                <td class="px-6 py-4 text-gray-600">{{ poste.code }}</td>
                <td class="px-6 py-4 text-gray-600">{{ poste.region }}</td>
                <td class="px-6 py-4">
                  <span
                    :class="[
                      'px-3 py-1 rounded-full text-sm font-medium',
                      poste.etat === 'ACTIF' ? 'bg-green-100 text-green-800' :
                      poste.etat === 'INACTIF' ? 'bg-yellow-100 text-yellow-800' :
                      'bg-red-100 text-red-800'
                    ]"
                  >
                    {{ poste.etat }}
                  </span>
                </td>
                <td class="px-6 py-4 text-gray-600">{{ poste.nombreTransformateurs || 0 }}</td>
                <td class="px-6 py-4 text-right space-x-2">
                  <Button
                    v-if="canEdit"
                    variant="secondary"
                    size="sm"
                    @click="editPoste(poste)"
                  >
                    ✏️ Modifier
                  </Button>
                  <Button
                    v-if="canDelete"
                    variant="danger"
                    size="sm"
                    @click="deletePoste(poste)"
                  >
                    🗑️ Supprimer
                  </Button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- Pagination -->
        <template #footer v-if="totalPages > 1">
          <div class="flex items-center justify-between">
            <p class="text-sm text-gray-600">
              Page {{ currentPage + 1 }} sur {{ totalPages }} ({{ totalElements }} total)
            </p>
            <div class="flex gap-2">
              <Button
                variant="secondary"
                size="sm"
                :disabled="currentPage === 0"
                @click="previousPage"
              >
                ← Précédent
              </Button>
              <Button
                variant="secondary"
                size="sm"
                :disabled="currentPage >= totalPages - 1"
                @click="nextPage"
              >
                Suivant →
              </Button>
            </div>
          </div>
        </template>
      </Card>
    </DataLoader>

    <!-- Create/Edit Modal -->
    <div v-if="showCreateForm" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <Card class="max-w-md w-full mx-4">
        <template #header>
          <div class="flex items-center justify-between">
            <h2 class="text-xl font-bold">{{ editingPoste ? 'Modifier Poste' : 'Ajouter Poste' }}</h2>
            <button @click="closeForm" class="text-gray-500 hover:text-gray-700">✕</button>
          </div>
        </template>

        <form @submit.prevent="savePoste" class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Nom *</label>
            <input
              v-model="formData.nom"
              required
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Code *</label>
            <input
              v-model="formData.code"
              required
              :disabled="!!editingPoste"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:bg-gray-100"
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Région *</label>
            <input
              v-model="formData.region"
              required
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">État *</label>
            <select
              v-model="formData.etat"
              required
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="">Sélectionner...</option>
              <option value="ACTIF">Actif</option>
              <option value="INACTIF">Inactif</option>
              <option value="MAINTENANCE">Maintenance</option>
            </select>
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Adresse *</label>
            <input v-model="formData.adresse" required class="w-full px-4 py-2 border border-gray-300 rounded-lg" />
          </div>
          <div class="grid grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Latitude *</label>
              <input v-model.number="formData.latitude" type="number" step="any" required class="w-full px-4 py-2 border border-gray-300 rounded-lg" />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Longitude *</label>
              <input v-model.number="formData.longitude" type="number" step="any" required class="w-full px-4 py-2 border border-gray-300 rounded-lg" />
            </div>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Type *</label>
            <select v-model="formData.typePoste" required class="w-full px-4 py-2 border border-gray-300 rounded-lg">
              <option value="PRIMAIRE">Primaire</option>
              <option value="SECONDAIRE">Secondaire</option>
              <option value="DISTRIBUTION">Distribution</option>
              <option value="TRANSPORT">Transport</option>
            </select>
          </div>
          <div class="flex gap-2 pt-4">
            <Button
              type="submit"
              variant="primary"
              size="md"
              :disabled="savingPoste"
              class="flex-1"
            >
              {{ savingPoste ? '⏳ Enregistrement...' : '✅ Enregistrer' }}
            </Button>
            <Button
              type="button"
              variant="secondary"
              size="md"
              @click="closeForm"
              class="flex-1"
            >
              Annuler
            </Button>
          </div>
        </form>
      </Card>
    </div>

    <!-- Excel Import Modal -->
    <ExcelImportModal
      :is-open="showImportModal"
      title="Importer des Postes"
      description="Importez plusieurs postes depuis un fichier Excel"
      :template-data="posteTemplateColumns"
      :template-file-name="`template-postes-${new Date().toISOString().split('T')[0]}`"
      :on-import="handlePosteImport"
      @close="showImportModal = false"
      @import-result="handleImportResult"
    />

    <!-- Import Result Display -->
    <ImportResultDisplay
      :result="importResult"
      @close="importResult = null"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useAuthStore } from '@/stores/authStore'
import { postesAPI } from '@/services/api'
import { formatError } from '@/services/error-handler'
import Card from '@/components/Card.vue'
import Button from '@/components/Button.vue'
import DataLoader from '@/components/DataLoader.vue'
import KPICard from '@/components/KPICard.vue'
import ExcelImportModal from '@/components/ExcelImportModal.vue'
import ImportResultDisplay from '@/components/ImportResultDisplay.vue'

const authStore = useAuthStore()

const loading = ref(false)
const error = ref(null)
const postes = ref([])
const currentPage = ref(0)
const totalPages = ref(0)
const totalElements = ref(0)

const showCreateForm = ref(false)
const showImportModal = ref(false)
const editingPoste = ref(null)
const savingPoste = ref(false)
const importResult = ref(null)

const filters = ref({
  nom: '',
  region: '',
  etat: '',
})

const formData = ref({
  nom: '',
  code: '',
  region: '',
  adresse: '',
  latitude: 0,
  longitude: 0,
  typePoste: 'PRIMAIRE',
  etat: 'ACTIF',
  description: '',
})

const stats = ref({
  total: 0,
  actifs: 0,
  inactifs: 0,
  maintenance: 0,
})

const posteTemplateColumns = [
  { name: 'code', example: 'POSTE-001' },
  { name: 'nom', example: 'Poste Tunis Centre' },
  { name: 'region', example: 'Tunis' },
  { name: 'latitude', example: '36.8065' },
  { name: 'longitude', example: '10.1956' },
  { name: 'etat', example: 'ACTIF' },
  { name: 'typePoste', example: 'PRIMAIRE' },
]

const canCreate = computed(() => authStore.role === 'ADMIN')
const canEdit = computed(() => ['ADMIN', 'SUPERVISOR'].includes(authStore.role))
const canDelete = computed(() => authStore.role === 'ADMIN')

let searchTimeout

async function loadPostes() {
  loading.value = true
  error.value = null

  try {
    const hasFilters = Object.values(filters.value).some(Boolean)
    const response = hasFilters
      ? await postesAPI.search(filters.value, currentPage.value, 10)
      : await postesAPI.getAll(currentPage.value, 10)
    postes.value = response.data.content
    totalPages.value = response.data.totalPages
    totalElements.value = response.data.totalElements
    currentPage.value = response.data.number || 0

    // Load stats
    const statsResponse = await postesAPI.getStatistics()
    stats.value = {
      total: statsResponse.data.totalPostes,
      actifs: statsResponse.data.posteActifs,
      inactifs: statsResponse.data.posteInactifs,
      maintenance: statsResponse.data.posteMaintenance,
    }
  } catch (err) {
    error.value = formatError(err).message
    console.error('Erreur chargement postes:', err)
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  clearTimeout(searchTimeout)
  searchTimeout = setTimeout(() => {
    currentPage.value = 0
    loadPostes()
  }, 300)
}

function resetFilters() {
  filters.value = { nom: '', region: '', etat: '' }
  currentPage.value = 0
  loadPostes()
}

function nextPage() {
  if (currentPage.value < totalPages.value - 1) {
    currentPage.value++
    loadPostes()
  }
}

function previousPage() {
  if (currentPage.value > 0) {
    currentPage.value--
    loadPostes()
  }
}

function editPoste(poste) {
  editingPoste.value = poste
  formData.value = { ...poste }
  showCreateForm.value = true
}

async function savePoste() {
  savingPoste.value = true
  try {
    if (editingPoste.value) {
      await postesAPI.update(editingPoste.value.id, formData.value)
    } else {
      await postesAPI.create(formData.value)
    }
    closeForm()
    await loadPostes()
  } catch (err) {
    error.value = formatError(err).message
    console.error('Erreur sauvegarde poste:', err)
  } finally {
    savingPoste.value = false
  }
}

async function deletePoste(poste) {
  if (!confirm(`Êtes-vous sûr de vouloir supprimer "${poste.nom}" ?`)) return

  try {
    await postesAPI.delete(poste.id)
    await loadPostes()
  } catch (err) {
    error.value = formatError(err).message
    console.error('Erreur suppression poste:', err)
  }
}

async function handlePosteImport(file) {
  try {
    const response = await postesAPI.importFromExcel(file)
    importResult.value = response.data
    showImportModal.value = false
    // Reload postes after import
    setTimeout(() => {
      loadPostes()
    }, 500)
    return response.data
  } catch (err) {
    const errorMsg = err.response?.data?.message || err.message || 'Erreur lors de l\'import'
    throw new Error(errorMsg)
  }
}

function handleImportResult(result) {
  importResult.value = result
}

function closeForm() {
  showCreateForm.value = false
  editingPoste.value = null
  formData.value = {
    nom: '',
    code: '',
    region: '',
    adresse: '',
    latitude: 0,
    longitude: 0,
    typePoste: 'PRIMAIRE',
    etat: 'ACTIF',
    description: '',
  }
}

onMounted(() => {
  loadPostes()
})
</script>

<style scoped></style>
