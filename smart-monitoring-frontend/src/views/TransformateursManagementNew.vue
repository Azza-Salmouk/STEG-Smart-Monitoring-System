<template>
  <div class="space-y-6">
    <!-- Header -->
    <div class="flex items-center justify-between">
      <div>
        <h1 class="text-3xl font-bold text-gray-900">Gestion des Transformateurs</h1>
        <p class="text-gray-500 mt-1">Superviser tous les transformateurs STEG</p>
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
          ➕ Ajouter Transformateur
        </Button>
      </div>
    </div>

    <!-- Data Loader Wrapper -->
    <DataLoader
      :loading="loading"
      :error="error"
      error-title="Impossible de charger les transformateurs"
      @retry="loadTransformateurs"
    >
      <!-- Stats Cards -->
      <div class="grid grid-cols-1 md:grid-cols-4 gap-4 mb-6">
        <KPICard
          title="Total Transformateurs"
          :value="stats.total"
          color="bg-blue-500"
        />
        <KPICard
          title="Sains"
          :value="stats.sains"
          color="bg-green-500"
        />
        <KPICard
          title="Alertes"
          :value="stats.alertes"
          color="bg-yellow-500"
        />
        <KPICard
          title="Critiques"
          :value="stats.critiques"
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
              v-model="filters.reference"
              @input="handleSearch"
              type="text"
              placeholder="Désignation..."
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Poste</label>
            <input
              v-model="filters.posteId"
              @input="handleSearch"
              type="text"
              placeholder="ID Poste..."
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Statut</label>
            <select
              v-model="filters.status"
              @change="handleSearch"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="">Tous les statuts</option>
              <option value="ACTIF">Actif</option>
              <option value="INACTIF">Inactif</option>
              <option value="EN_MAINTENANCE">En Maintenance</option>
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
          <h2 class="text-lg font-semibold">Liste des Transformateurs</h2>
        </template>
        
        <div v-if="transformateurs.length === 0" class="text-center py-12">
          <p class="text-gray-500 text-lg">Aucun transformateur trouvé</p>
        </div>

        <div v-else class="overflow-x-auto">
          <table class="min-w-full">
            <thead class="bg-gray-50 border-b">
              <tr>
                <th class="px-6 py-3 text-left text-sm font-semibold text-gray-700">Désignation</th>
                <th class="px-6 py-3 text-left text-sm font-semibold text-gray-700">Poste</th>
                <th class="px-6 py-3 text-left text-sm font-semibold text-gray-700">Health Score</th>
                <th class="px-6 py-3 text-left text-sm font-semibold text-gray-700">Risque</th>
                <th class="px-6 py-3 text-left text-sm font-semibold text-gray-700">Statut</th>
                <th class="px-6 py-3 text-left text-sm font-semibold text-gray-700">Puissance</th>
                <th class="px-6 py-3 text-right text-sm font-semibold text-gray-700">Actions</th>
              </tr>
            </thead>
            <tbody class="divide-y">
              <tr v-for="t in transformateurs" :key="t.id" class="hover:bg-gray-50">
                <td class="px-6 py-4 text-gray-900 font-medium">{{ t.reference }}</td>
                <td class="px-6 py-4 text-gray-600">{{ t.posteNom || t.posteId }}</td>
                <td class="px-6 py-4">
                  <div class="flex items-center gap-2">
                    <div class="w-24 bg-gray-200 rounded-full h-2">
                      <div
                        class="h-2 rounded-full"
                        :class="getHealthScoreColor(t.healthScore)"
                        :style="{ width: t.healthScore + '%' }"
                      ></div>
                    </div>
                    <span class="text-sm font-medium">{{ t.healthScore }}%</span>
                  </div>
                </td>
                <td class="px-6 py-4">
                  <span
                    :class="[
                      'px-3 py-1 rounded-full text-sm font-medium',
                      t.riskLevel === 'CRITICAL' ? 'bg-red-100 text-red-800' :
                      t.riskLevel === 'HIGH' ? 'bg-yellow-100 text-yellow-800' :
                      'bg-green-100 text-green-800'
                    ]"
                  >
                    {{ t.riskLevel || '-' }}
                  </span>
                </td>
                <td class="px-6 py-4">
                  <span
                    :class="[
                      'px-3 py-1 rounded-full text-sm font-medium',
                      t.status === 'ACTIF' ? 'bg-green-100 text-green-800' :
                      t.status === 'MAINTENANCE' ? 'bg-yellow-100 text-yellow-800' :
                      'bg-gray-100 text-gray-800'
                    ]"
                  >
                    {{ getStatutLabel(t.status) }}
                  </span>
                </td>
                <td class="px-6 py-4 text-gray-600">{{ t.power || '-' }} MVA</td>
                <td class="px-6 py-4 text-right space-x-2">
                  <Button
                    v-if="canEdit"
                    variant="secondary"
                    size="sm"
                    @click="editTransformateur(t)"
                  >
                    ✏️ Modifier
                  </Button>
                  <Button
                    v-if="canDelete"
                    variant="danger"
                    size="sm"
                    @click="deleteTransformateur(t)"
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
            <h2 class="text-xl font-bold">{{ editingTransformateur ? 'Modifier Transformateur' : 'Ajouter Transformateur' }}</h2>
            <button @click="closeForm" class="text-gray-500 hover:text-gray-700">✕</button>
          </div>
        </template>

        <form @submit.prevent="saveTransformateur" class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Désignation *</label>
            <input
              v-model="formData.reference"
              required
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Poste ID *</label>
            <input
              v-model="formData.posteId"
              required
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Puissance Nominale (MVA) *</label>
            <input
              v-model.number="formData.power"
              type="number"
              step="0.1"
              required
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Statut *</label>
            <select
              v-model="formData.status"
              required
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="">Sélectionner...</option>
              <option value="ACTIF">Actif</option>
              <option value="ALERTE">Alerte</option>
              <option value="DEFAUT">Défaut</option>
              <option value="MAINTENANCE">En Maintenance</option>
              <option value="HORS_SERVICE">Hors service</option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Numéro de série *</label>
            <input
              v-model="formData.serialNumber"
              required
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Fabricant *</label>
            <input v-model="formData.manufacturer" required class="w-full px-4 py-2 border border-gray-300 rounded-lg" />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Modèle *</label>
            <input v-model="formData.model" required class="w-full px-4 py-2 border border-gray-300 rounded-lg" />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Date d'installation *</label>
            <input v-model="formData.installationDate" type="date" required class="w-full px-4 py-2 border border-gray-300 rounded-lg" />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Niveau de risque *</label>
            <select v-model="formData.riskLevel" required class="w-full px-4 py-2 border border-gray-300 rounded-lg">
              <option value="LOW">Bas</option><option value="MEDIUM">Moyen</option><option value="HIGH">Élevé</option><option value="CRITICAL">Critique</option>
            </select>
          </div>

          <div class="flex gap-2 pt-4">
            <Button
              type="submit"
              variant="primary"
              size="md"
              :disabled="savingTransformateur"
              class="flex-1"
            >
              {{ savingTransformateur ? '⏳ Enregistrement...' : '✅ Enregistrer' }}
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
      title="Importer des Transformateurs"
      description="Importez plusieurs transformateurs depuis un fichier Excel"
      :template-data="transformateurTemplateColumns"
      :template-file-name="`template-transformateurs-${new Date().toISOString().split('T')[0]}`"
      :on-import="handleTransformateurImport"
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
import { transformateursAPI } from '@/services/api'
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
const transformateurs = ref([])
const currentPage = ref(0)
const totalPages = ref(0)
const totalElements = ref(0)

const showCreateForm = ref(false)
const showImportModal = ref(false)
const editingTransformateur = ref(null)
const savingTransformateur = ref(false)
const importResult = ref(null)

const filters = ref({
  reference: '',
  posteId: '',
  status: '',
})

const formData = ref({
  reference: '',
  posteId: '',
  serialNumber: '',
  manufacturer: '',
  model: '',
  power: 0,
  installationDate: '',
  status: 'ACTIF',
  healthScore: 100,
  riskLevel: 'LOW',
})

const stats = ref({
  total: 0,
  sains: 0,
  alertes: 0,
  critiques: 0,
})

const transformateurTemplateColumns = [
  { name: 'reference', example: 'TRANSF-001' },
  { name: 'serialNumber', example: 'SN-2024-001' },
  { name: 'manufacturer', example: 'ABB' },
  { name: 'power', example: '25' },
  { name: 'installationDate', example: '2024-01-15' },
  { name: 'posteCode', example: 'POSTE-001' },
  { name: 'status', example: 'ACTIF' },
  { name: 'healthScore', example: '85' },
  { name: 'riskLevel', example: 'LOW' },
]

const canCreate = computed(() => authStore.role === 'ADMIN')
const canEdit = computed(() => ['ADMIN', 'SUPERVISOR'].includes(authStore.role))
const canDelete = computed(() => authStore.role === 'ADMIN')

let searchTimeout

function getHealthScoreColor(score) {
  if (score >= 80) return 'bg-green-500'
  if (score >= 60) return 'bg-yellow-500'
  return 'bg-red-500'
}

function getStatutLabel(statut) {
  const labels = {
    ACTIF: 'Actif',
    INACTIF: 'Inactif',
    EN_MAINTENANCE: 'En Maintenance'
  }
  return labels[statut] || statut
}

async function loadTransformateurs() {
  loading.value = true
  error.value = null

  try {
    const hasFilters = Object.values(filters.value).some(Boolean)
    const response = hasFilters
      ? await transformateursAPI.search(filters.value, currentPage.value, 10)
      : await transformateursAPI.getAll(currentPage.value, 10)
    transformateurs.value = response.data.content || response.data
    totalPages.value = response.data.totalPages || 1
    totalElements.value = response.data.totalElements || transformateurs.value.length
    currentPage.value = response.data.number || 0

    // Calculate stats
    const statsResponse = await transformateursAPI.getStatistics()
    stats.value = {
      total: statsResponse.data.totalTransformateurs,
      sains: statsResponse.data.transformateursActifs,
      alertes: statsResponse.data.transformateursAlertes,
      critiques: statsResponse.data.transformateursCritiques,
    }
  } catch (err) {
    error.value = formatError(err).message
    console.error('Erreur chargement transformateurs:', err)
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  clearTimeout(searchTimeout)
  searchTimeout = setTimeout(() => {
    currentPage.value = 0
    loadTransformateurs()
  }, 300)
}

function resetFilters() {
  filters.value = { reference: '', posteId: '', status: '' }
  currentPage.value = 0
  loadTransformateurs()
}

function nextPage() {
  if (currentPage.value < totalPages.value - 1) {
    currentPage.value++
    loadTransformateurs()
  }
}

function previousPage() {
  if (currentPage.value > 0) {
    currentPage.value--
    loadTransformateurs()
  }
}

function editTransformateur(transformateur) {
  editingTransformateur.value = transformateur
  formData.value = { ...transformateur }
  showCreateForm.value = true
}

async function saveTransformateur() {
  savingTransformateur.value = true
  try {
    if (editingTransformateur.value) {
      await transformateursAPI.update(editingTransformateur.value.id, formData.value)
    } else {
      await transformateursAPI.create(formData.value)
    }
    closeForm()
    await loadTransformateurs()
  } catch (err) {
    error.value = formatError(err).message
    console.error('Erreur sauvegarde transformateur:', err)
  } finally {
    savingTransformateur.value = false
  }
}

async function deleteTransformateur(transformateur) {
  if (!confirm(`Êtes-vous sûr de vouloir supprimer "${transformateur.reference}" ?`)) return

  try {
    await transformateursAPI.delete(transformateur.id)
    await loadTransformateurs()
  } catch (err) {
    error.value = formatError(err).message
    console.error('Erreur suppression transformateur:', err)
  }
}

async function handleTransformateurImport(file) {
  try {
    const response = await transformateursAPI.importFromExcel(file)
    importResult.value = response.data
    showImportModal.value = false
    // Reload transformateurs after import
    setTimeout(() => {
      loadTransformateurs()
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
  editingTransformateur.value = null
  formData.value = {
    reference: '',
    posteId: '',
    serialNumber: '',
    manufacturer: '',
    model: '',
    power: 0,
    installationDate: '',
    status: 'ACTIF',
    healthScore: 100,
    riskLevel: 'LOW',
  }
}

onMounted(() => {
  loadTransformateurs()
})
</script>

<style scoped></style>
