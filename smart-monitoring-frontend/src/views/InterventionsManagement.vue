<template>
  <div class="space-y-6">
    <!-- Header -->
    <div class="flex items-center justify-between">
      <div>
        <h1 class="text-3xl font-bold text-gray-900">Gestion des Interventions</h1>
        <p class="text-gray-500 mt-1">Superviser les interventions STEG</p>
      </div>
      <button
        v-if="canCreate"
        @click="showCreateForm = true"
        class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
      >
        ➕ Nouvelle Intervention
      </button>
    </div>

    <!-- Stats Cards -->
    <div v-if="statistics" class="grid grid-cols-1 md:grid-cols-5 gap-4">
      <div class="bg-white p-4 rounded-lg shadow">
        <p class="text-sm text-gray-600">Total</p>
        <p class="text-2xl font-bold text-gray-900">{{ statistics.totalInterventions }}</p>
      </div>
      <div class="bg-white p-4 rounded-lg shadow">
        <p class="text-sm text-gray-600">En Attente</p>
        <p class="text-2xl font-bold text-gray-500">{{ statistics.enAttente }}</p>
      </div>
      <div class="bg-white p-4 rounded-lg shadow">
        <p class="text-sm text-gray-600">En Cours</p>
        <p class="text-2xl font-bold text-yellow-600">{{ statistics.enCours }}</p>
      </div>
      <div class="bg-white p-4 rounded-lg shadow">
        <p class="text-sm text-gray-600">Critiques</p>
        <p class="text-2xl font-bold text-red-600">{{ statistics.critiques }}</p>
      </div>
      <div class="bg-white p-4 rounded-lg shadow">
        <p class="text-sm text-gray-600">Taux Complétion</p>
        <p class="text-2xl font-bold text-green-600">{{ (statistics.completionRate || 0).toFixed(1) }}%</p>
      </div>
    </div>

    <!-- Search & Filters -->
    <div class="bg-white p-4 rounded-lg shadow">
      <h2 class="text-lg font-semibold mb-4">Recherche & Filtres</h2>
      <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Statut</label>
          <select
            v-model="filters.statut"
            @change="performSearch"
            class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value="">Tous</option>
            <option value="EN_ATTENTE">En Attente</option>
            <option value="PLANIFIEE">Planifiée</option>
            <option value="EN_COURS">En Cours</option>
            <option value="TERMINEE">Terminée</option>
            <option value="VALIDEE">Validée</option>
            <option value="ANNULEE">Annulée</option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Priorité</label>
          <select
            v-model="filters.priorite"
            @change="performSearch"
            class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value="">Tous</option>
            <option value="FAIBLE">Faible</option>
            <option value="MOYENNE">Moyenne</option>
            <option value="HAUTE">Haute</option>
            <option value="CRITIQUE">Critique</option>
          </select>
        </div>
        <div class="flex items-end">
          <button
            @click="resetFilters"
            class="px-4 py-2 bg-gray-200 text-gray-700 rounded-lg hover:bg-gray-300 w-full"
          >
            Réinitialiser
          </button>
        </div>
      </div>
    </div>

    <!-- Table -->
    <div class="bg-white rounded-lg shadow">
      <div v-if="loading" class="p-8 text-center">
        <p class="text-gray-500">Chargement...</p>
      </div>
      <div v-else-if="interventions.length === 0" class="p-8 text-center">
        <p class="text-gray-500">Aucune intervention</p>
      </div>
      <InterventionTable
        v-else
        :interventions="interventions"
        :loading="loading"
        :current-page="currentPage"
        :total-pages="totalPages"
        :total-elements="totalElements"
        @prev="previousPage"
        @next="nextPage"
      >
        <template #actions="{ intervention }">
          <div class="flex gap-2">
            <button
              @click="editIntervention(intervention)"
              class="text-blue-600 hover:text-blue-800 text-sm"
            >
              ✏️
            </button>
            <button
              v-if="canDelete"
              @click="deleteIntervention(intervention.id)"
              class="text-red-600 hover:text-red-800 text-sm"
            >
              🗑️
            </button>
          </div>
        </template>
      </InterventionTable>
    </div>

    <!-- Create/Edit Modal -->
    <InterventionFormModal
      v-if="showCreateForm || showEditForm"
      :intervention="editingIntervention"
      :is-edit="showEditForm"
      @save="saveIntervention"
      @close="closeForm"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useInterventionStore } from '@/stores/useInterventionStore'
import { useAuthStore } from '@/stores/authStore'
import InterventionTable from '@/components/intervention/InterventionTable.vue'
import InterventionFormModal from '@/components/intervention/InterventionFormModal.vue'

const interventionStore = useInterventionStore()
const authStore = useAuthStore()

// State
const showCreateForm = ref(false)
const showEditForm = ref(false)
const editingIntervention = ref(null)

// Computed
const interventions = computed(() => interventionStore.interventions)
const loading = computed(() => interventionStore.loading)
const error = computed(() => interventionStore.error)
const statistics = computed(() => interventionStore.statistics)
const currentPage = computed(() => interventionStore.currentPage)
const totalPages = computed(() => interventionStore.totalPages)
const totalElements = computed(() => interventionStore.totalElements)
const pageSize = computed(() => interventionStore.pageSize)

const filters = ref({
  transformateurId: null,
  agentId: null,
  statut: '',
  priorite: '',
  typeIntervention: '',
  startDate: null,
  endDate: null
})

const canCreate = computed(() => ['ADMIN', 'SUPERVISOR', 'AGENT'].includes(authStore.user?.role))
const canDelete = computed(() => ['ADMIN', 'SUPERVISOR'].includes(authStore.user?.role))

// Methods
onMounted(() => {
  loadInterventions()
  loadStatistics()
})

const loadInterventions = async () => {
  await interventionStore.fetchInterventions(0, pageSize.value)
}

const loadStatistics = async () => {
  await interventionStore.fetchStatistics()
}

const performSearch = async () => {
  interventionStore.searchFilters = { ...filters.value }
  await interventionStore.searchInterventions(0)
}

const resetFilters = () => {
  filters.value = {
    transformateurId: null,
    agentId: null,
    statut: '',
    priorite: '',
    typeIntervention: '',
    startDate: null,
    endDate: null
  }
  loadInterventions()
}

const previousPage = async () => {
  if (currentPage.value > 0) {
    await interventionStore.fetchInterventions(currentPage.value - 1, pageSize.value)
  }
}

const nextPage = async () => {
  if (currentPage.value < totalPages.value - 1) {
    await interventionStore.fetchInterventions(currentPage.value + 1, pageSize.value)
  }
}

const editIntervention = (intervention) => {
  editingIntervention.value = { ...intervention }
  showEditForm.value = true
}

const saveIntervention = async (data) => {
  try {
    if (showEditForm.value) {
      await interventionStore.updateIntervention(editingIntervention.value.id, data)
    } else {
      await interventionStore.createIntervention(data)
    }
    closeForm()
    await loadStatistics()
  } catch (err) {
    console.error('Erreur lors de la sauvegarde:', err)
  }
}

const deleteIntervention = async (id) => {
  if (confirm('Êtes-vous sûr de vouloir supprimer cette intervention?')) {
    try {
      await interventionStore.deleteIntervention(id)
      await loadStatistics()
    } catch (err) {
      console.error('Erreur lors de la suppression:', err)
    }
  }
}

const closeForm = () => {
  showCreateForm.value = false
  showEditForm.value = false
  editingIntervention.value = null
}
</script>
