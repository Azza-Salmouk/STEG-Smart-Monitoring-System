<template>
  <div class="space-y-6">
    <!-- Header -->
    <div class="flex justify-between items-center">
      <div>
        <h1 class="text-3xl font-bold text-gray-900">Mesures IoT</h1>
        <p class="text-gray-600 mt-1">Gestion et suivi des mesures des transformateurs</p>
      </div>
      <button
        @click="showCreateForm = true"
        v-if="canCreate"
        class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-lg shadow-sm text-white bg-blue-600 hover:bg-blue-700"
      >
        <PlusIcon class="w-4 h-4 mr-2" />
        Nouvelle mesure
      </button>
    </div>

    <!-- KPI Cards -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
      <div class="bg-white rounded-lg shadow p-6">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-sm text-gray-600">Total mesures</p>
            <p class="text-2xl font-bold text-gray-900 mt-1">{{ totalElements }}</p>
          </div>
          <ActivityIcon class="w-8 h-8 text-blue-500 opacity-20" />
        </div>
      </div>

      <div class="bg-white rounded-lg shadow p-6">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-sm text-gray-600">Temp. moyenne</p>
            <p class="text-2xl font-bold text-gray-900 mt-1">{{ averageTemperature }}°C</p>
          </div>
          <ThermometerIcon class="w-8 h-8 text-orange-500 opacity-20" />
        </div>
      </div>

      <div class="bg-white rounded-lg shadow p-6">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-sm text-gray-600">Courant moyen</p>
            <p class="text-2xl font-bold text-gray-900 mt-1">{{ averageCurrent }}A</p>
          </div>
          <ZapIcon class="w-8 h-8 text-yellow-500 opacity-20" />
        </div>
      </div>

      <div class="bg-white rounded-lg shadow p-6">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-sm text-gray-600">Health Score</p>
            <p class="text-2xl font-bold text-gray-900 mt-1">{{ averageHealthScore }}/100</p>
          </div>
          <HeartIcon class="w-8 h-8 text-red-500 opacity-20" />
        </div>
      </div>
    </div>

    <!-- Stats Row -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
      <div class="bg-white rounded-lg shadow p-6">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-sm text-gray-600">Critiques</p>
            <p class="text-2xl font-bold text-red-600 mt-1">{{ criticalCount }}</p>
          </div>
          <AlertTriangleIcon class="w-8 h-8 text-red-500 opacity-20" />
        </div>
      </div>

      <div class="bg-white rounded-lg shadow p-6">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-sm text-gray-600">Tension moyenne</p>
            <p class="text-2xl font-bold text-gray-900 mt-1">{{ averageVoltage }}V</p>
          </div>
          <BoltIcon class="w-8 h-8 text-purple-500 opacity-20" />
        </div>
      </div>

      <div class="bg-white rounded-lg shadow p-6">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-sm text-gray-600">Alertes</p>
            <p class="text-2xl font-bold text-yellow-600 mt-1">{{ warningCount }}</p>
          </div>
          <AlertCircleIcon class="w-8 h-8 text-yellow-500 opacity-20" />
        </div>
      </div>
    </div>

    <!-- Filters -->
    <MeasurementFilter
      :filters="searchFilters"
      @update:filters="searchFilters = $event"
      @search="handleSearch"
      @reset="handleResetFilters"
    />

    <!-- Data Table -->
    <div class="bg-white rounded-lg shadow">
      <MeasurementTable
        :measurements="measurements"
        :loading="loading"
        :can-edit="canEdit"
        :can-delete="canDelete"
        @edit="handleEdit"
        @delete="handleDelete"
        @view="handleView"
      />

      <!-- Pagination -->
      <div v-if="totalPages > 1" class="px-6 py-4 border-t border-gray-200 flex items-center justify-between">
        <button
          @click="previousPage"
          :disabled="currentPage === 0"
          class="px-3 py-1 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-md hover:bg-gray-50 disabled:opacity-50"
        >
          Précédent
        </button>
        <div class="text-sm text-gray-700">
          Page {{ currentPage + 1 }} sur {{ totalPages }}
        </div>
        <button
          @click="nextPage"
          :disabled="currentPage >= totalPages - 1"
          class="px-3 py-1 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-md hover:bg-gray-50 disabled:opacity-50"
        >
          Suivant
        </button>
      </div>
    </div>

    <!-- Create/Edit Modal -->
    <MeasurementFormModal
      v-if="showCreateForm"
      :measurement="editingMeasurement"
      @save="handleSave"
      @close="showCreateForm = false"
    />

    <!-- Delete Confirmation Modal -->
    <DeleteConfirmationModal
      v-if="showDeleteConfirm"
      :item-name="`la mesure de ${deletingMeasurement?.transformateur?.nom || 'transformateur'}`"
      @confirm="confirmDelete"
      @cancel="showDeleteConfirm = false"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useMeasurementStore } from '@/stores/useMeasurementStore'
import { useAuthStore } from '@/stores/authStore'
import { useToast } from '@/composables/useToast'
import MeasurementFilter from '@/components/measurement/MeasurementFilter.vue'
import MeasurementTable from '@/components/measurement/MeasurementTable.vue'
import MeasurementFormModal from '@/components/measurement/MeasurementFormModal.vue'
import DeleteConfirmationModal from '@/components/DeleteConfirmationModal.vue'
import {
  Plus as PlusIcon,
  Activity as ActivityIcon,
  Thermometer as ThermometerIcon,
  Zap as ZapIcon,
  Heart as HeartIcon,
  AlertTriangle as AlertTriangleIcon,
  Bolt as BoltIcon,
  AlertCircle as AlertCircleIcon,
} from 'lucide-vue-next'

const measurementStore = useMeasurementStore()
const authStore = useAuthStore()
const toast = useToast()

// State
const showCreateForm = ref(false)
const showDeleteConfirm = ref(false)
const editingMeasurement = ref(null)
const deletingMeasurement = ref(null)
const searchFilters = ref({ ...measurementStore.searchFilters })

// Computed
const measurements = computed(() => measurementStore.measurements)
const loading = computed(() => measurementStore.loading)
const totalElements = computed(() => measurementStore.totalElements)
const totalPages = computed(() => measurementStore.totalPages)
const currentPage = computed(() => measurementStore.currentPage)
const pageSize = computed(() => measurementStore.pageSize)
const averageHealthScore = computed(() => measurementStore.averageHealthScore)
const averageTemperature = computed(() => measurementStore.averageTemperature)
const averageCurrent = computed(() => measurementStore.averageCurrent)
const averageVoltage = computed(() => measurementStore.averageVoltage)
const criticalCount = computed(() => measurementStore.criticalCount)
const warningCount = computed(() => measurementStore.warningCount)

// Permissions
const canCreate = computed(() => {
  return ['ADMIN', 'SUPERVISOR'].includes(authStore.role)
})

const canEdit = computed(() => {
  return ['ADMIN', 'SUPERVISOR'].includes(authStore.role)
})

const canDelete = computed(() => {
  return authStore.role === 'ADMIN'
})

// Methods
const handleSearch = async () => {
  Object.assign(measurementStore.searchFilters, searchFilters.value)
  await measurementStore.searchMeasurements(0)
}

const handleResetFilters = () => {
  measurementStore.resetFilters()
  searchFilters.value = { ...measurementStore.searchFilters }
  loadMeasurements()
}

const previousPage = () => {
  if (currentPage.value > 0) {
    loadMeasurements(currentPage.value - 1)
  }
}

const nextPage = () => {
  if (currentPage.value < totalPages.value - 1) {
    loadMeasurements(currentPage.value + 1)
  }
}

const loadMeasurements = async (page = 0) => {
  await measurementStore.fetchMeasurements(page, pageSize.value)
}

const handleEdit = (measurement) => {
  editingMeasurement.value = { ...measurement }
  showCreateForm.value = true
}

const handleView = async (measurement) => {
  await measurementStore.fetchMeasurementById(measurement.id)
  // Could open a detail modal here if needed
}

const handleDelete = (measurement) => {
  deletingMeasurement.value = measurement
  showDeleteConfirm.value = true
}

const confirmDelete = async () => {
  if (!deletingMeasurement.value) return

  try {
    await measurementStore.deleteMeasurement(deletingMeasurement.value.id)
    toast.success('Mesure supprimée avec succès')
    showDeleteConfirm.value = false
    deletingMeasurement.value = null
    await loadMeasurements(currentPage.value)
  } catch (error) {
    toast.error('Erreur lors de la suppression')
    console.error('Delete error:', error)
  }
}

const handleSave = async (formData) => {
  try {
    if (editingMeasurement.value?.id) {
      await measurementStore.updateMeasurement(editingMeasurement.value.id, formData)
      toast.success('Mesure mise à jour avec succès')
    } else {
      await measurementStore.createMeasurement(formData)
      toast.success('Mesure créée avec succès')
    }
    showCreateForm.value = false
    editingMeasurement.value = null
    await loadMeasurements(0)
  } catch (error) {
    toast.error(error.response?.data?.message || 'Erreur lors de la sauvegarde')
    console.error('Save error:', error)
  }
}

// Lifecycle
onMounted(async () => {
  await loadMeasurements()
  await measurementStore.fetchStatistics()
})
</script>
