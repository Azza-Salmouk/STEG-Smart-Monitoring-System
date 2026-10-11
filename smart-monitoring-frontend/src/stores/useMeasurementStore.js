import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { measurementsAPI } from '@/services/api'

/**
 * Store Pinia pour la gestion des mesures IoT
 */
export const useMeasurementStore = defineStore('measurement', () => {
  // État
  const measurements = ref([])
  const totalElements = ref(0)
  const totalPages = ref(0)
  const currentPage = ref(0)
  const pageSize = ref(10)
  const loading = ref(false)
  const error = ref(null)
  const statistics = ref(null)
  const selectedMeasurement = ref(null)

  // Filtres de recherche
  const searchFilters = ref({
    transformateurId: null,
    status: '',
    temperatureMin: null,
    temperatureMax: null,
    currentMin: null,
    currentMax: null,
    healthScoreMin: null,
    healthScoreMax: null,
    startDate: null,
    endDate: null,
  })

  // Computed - Mesures critiques
  const criticalCount = computed(() => {
    return measurements.value.filter(m => m.status === 'CRITICAL').length
  })

  // Computed - Mesures en alerte
  const warningCount = computed(() => {
    return measurements.value.filter(m => m.status === 'WARNING').length
  })

  // Computed - Health score moyen
  const averageHealthScore = computed(() => {
    if (measurements.value.length === 0) return 0
    const sum = measurements.value.reduce((acc, m) => acc + (m.healthScore || 0), 0)
    return Math.round(sum / measurements.value.length)
  })

  // Computed - Température moyenne
  const averageTemperature = computed(() => {
    if (measurements.value.length === 0) return 0
    const sum = measurements.value.reduce((acc, m) => acc + (m.temperature || 0), 0)
    return Math.round(sum / measurements.value.length * 10) / 10
  })

  // Computed - Courant moyen
  const averageCurrent = computed(() => {
    if (measurements.value.length === 0) return 0
    const sum = measurements.value.reduce((acc, m) => acc + (m.current || 0), 0)
    return Math.round(sum / measurements.value.length * 10) / 10
  })

  // Computed - Tension moyenne
  const averageVoltage = computed(() => {
    if (measurements.value.length === 0) return 0
    const sum = measurements.value.reduce((acc, m) => acc + (m.voltage || 0), 0)
    return Math.round(sum / measurements.value.length * 10) / 10
  })

  /**
   * Récupérer toutes les mesures avec pagination
   */
  const fetchMeasurements = async (page = 0, size = 10) => {
    loading.value = true
    error.value = null

    try {
      const response = await measurementsAPI.getAll(page, size)
      measurements.value = response.data.content || response.data
      totalElements.value = response.data.totalElements || 0
      totalPages.value = response.data.totalPages || 1
      currentPage.value = page
      pageSize.value = size
    } catch (err) {
      error.value = err.response?.data?.message || 'Erreur lors du chargement des mesures'
      console.error('Fetch measurements error:', err)
    } finally {
      loading.value = false
    }
  }

  /**
   * Recherche avancée avec filtres
   */
  const searchMeasurements = async (page = 0) => {
    loading.value = true
    error.value = null

    try {
      const filters = {}
      
      if (searchFilters.value.transformateurId) {
        filters.transformateurId = searchFilters.value.transformateurId
      }
      if (searchFilters.value.status) {
        filters.status = searchFilters.value.status
      }
      if (searchFilters.value.temperatureMin !== null && searchFilters.value.temperatureMin !== '') {
        filters.temperatureMin = searchFilters.value.temperatureMin
      }
      if (searchFilters.value.temperatureMax !== null && searchFilters.value.temperatureMax !== '') {
        filters.temperatureMax = searchFilters.value.temperatureMax
      }
      if (searchFilters.value.currentMin !== null && searchFilters.value.currentMin !== '') {
        filters.currentMin = searchFilters.value.currentMin
      }
      if (searchFilters.value.currentMax !== null && searchFilters.value.currentMax !== '') {
        filters.currentMax = searchFilters.value.currentMax
      }
      if (searchFilters.value.healthScoreMin !== null && searchFilters.value.healthScoreMin !== '') {
        filters.healthScoreMin = searchFilters.value.healthScoreMin
      }
      if (searchFilters.value.healthScoreMax !== null && searchFilters.value.healthScoreMax !== '') {
        filters.healthScoreMax = searchFilters.value.healthScoreMax
      }
      if (searchFilters.value.startDate) {
        filters.startDate = searchFilters.value.startDate
      }
      if (searchFilters.value.endDate) {
        filters.endDate = searchFilters.value.endDate
      }

      const response = await measurementsAPI.search(filters, page, pageSize.value)
      measurements.value = response.data.content || response.data
      totalElements.value = response.data.totalElements || 0
      totalPages.value = response.data.totalPages || 1
      currentPage.value = page
    } catch (err) {
      error.value = err.response?.data?.message || 'Erreur lors de la recherche'
      console.error('Search measurements error:', err)
    } finally {
      loading.value = false
    }
  }

  /**
   * Récupérer les mesures critiques
   */
  const fetchCriticalMeasurements = async (page = 0) => {
    loading.value = true
    error.value = null

    try {
      const response = await measurementsAPI.getCritical(page, pageSize.value)
      measurements.value = response.data.content || response.data
      totalElements.value = response.data.totalElements || 0
      totalPages.value = response.data.totalPages || 1
      currentPage.value = page
    } catch (err) {
      error.value = err.response?.data?.message || 'Erreur lors du chargement des mesures critiques'
      console.error('Fetch critical measurements error:', err)
    } finally {
      loading.value = false
    }
  }

  /**
   * Récupérer les mesures d'un transformateur
   */
  const fetchByTransformateur = async (transformateurId, page = 0) => {
    loading.value = true
    error.value = null

    try {
      const response = await measurementsAPI.getByTransformateur(transformateurId, page, pageSize.value)
      measurements.value = response.data.content || response.data
      totalElements.value = response.data.totalElements || 0
      totalPages.value = response.data.totalPages || 1
      currentPage.value = page
    } catch (err) {
      error.value = err.response?.data?.message || 'Erreur lors du chargement des mesures'
      console.error('Fetch by transformateur error:', err)
    } finally {
      loading.value = false
    }
  }

  /**
   * Récupérer une mesure par ID
   */
  const fetchMeasurementById = async (id) => {
    loading.value = true
    error.value = null

    try {
      const response = await measurementsAPI.getById(id)
      selectedMeasurement.value = response.data
      return response.data
    } catch (err) {
      error.value = err.response?.data?.message || 'Erreur lors du chargement de la mesure'
      console.error('Fetch measurement by id error:', err)
    } finally {
      loading.value = false
    }
  }

  /**
   * Créer une mesure
   */
  const createMeasurement = async (data) => {
    loading.value = true
    error.value = null

    try {
      const response = await measurementsAPI.create(data)
      measurements.value.unshift(response.data)
      totalElements.value += 1
      return response.data
    } catch (err) {
      error.value = err.response?.data?.message || 'Erreur lors de la création'
      throw err
    } finally {
      loading.value = false
    }
  }

  /**
   * Mettre à jour une mesure
   */
  const updateMeasurement = async (id, data) => {
    loading.value = true
    error.value = null

    try {
      const response = await measurementsAPI.update(id, data)
      const index = measurements.value.findIndex(m => m.id === id)
      if (index !== -1) {
        measurements.value[index] = response.data
      }
      if (selectedMeasurement.value?.id === id) {
        selectedMeasurement.value = response.data
      }
      return response.data
    } catch (err) {
      error.value = err.response?.data?.message || 'Erreur lors de la modification'
      throw err
    } finally {
      loading.value = false
    }
  }

  /**
   * Supprimer une mesure
   */
  const deleteMeasurement = async (id) => {
    loading.value = true
    error.value = null

    try {
      await measurementsAPI.delete(id)
      measurements.value = measurements.value.filter(m => m.id !== id)
      totalElements.value -= 1
      if (selectedMeasurement.value?.id === id) {
        selectedMeasurement.value = null
      }
    } catch (err) {
      error.value = err.response?.data?.message || 'Erreur lors de la suppression'
      throw err
    } finally {
      loading.value = false
    }
  }

  /**
   * Récupérer les statistiques
   */
  const fetchStatistics = async () => {
    try {
      const response = await measurementsAPI.getStatistics()
      statistics.value = response.data
      return response.data
    } catch (err) {
      console.error('Fetch statistics error:', err)
    }
  }

  /**
   * Réinitialiser les filtres
   */
  const resetFilters = () => {
    searchFilters.value = {
      transformateurId: null,
      status: '',
      temperatureMin: null,
      temperatureMax: null,
      currentMin: null,
      currentMax: null,
      healthScoreMin: null,
      healthScoreMax: null,
      startDate: null,
      endDate: null,
    }
  }

  /**
   * Réinitialiser l'état
   */
  const reset = () => {
    measurements.value = []
    totalElements.value = 0
    totalPages.value = 0
    currentPage.value = 0
    loading.value = false
    error.value = null
    selectedMeasurement.value = null
    resetFilters()
  }

  return {
    // État
    measurements,
    totalElements,
    totalPages,
    currentPage,
    pageSize,
    loading,
    error,
    statistics,
    selectedMeasurement,
    searchFilters,

    // Computed
    criticalCount,
    warningCount,
    averageHealthScore,
    averageTemperature,
    averageCurrent,
    averageVoltage,

    // Actions
    fetchMeasurements,
    searchMeasurements,
    fetchCriticalMeasurements,
    fetchByTransformateur,
    fetchMeasurementById,
    createMeasurement,
    updateMeasurement,
    deleteMeasurement,
    fetchStatistics,
    resetFilters,
    reset,
  }
})
