import { defineStore } from 'pinia'
import { ref } from 'vue'
import axiosInstance from '@/api/axios'

/**
 * Store Pinia pour la gestion des transformateurs
 */
export const useTransformateurStore = defineStore('transformateur', () => {
  // État
  const transformateurs = ref([])
  const totalElements = ref(0)
  const totalPages = ref(0)
  const currentPage = ref(0)
  const pageSize = ref(10)
  const loading = ref(false)
  const error = ref(null)
  const statistics = ref(null)

  // Filtres de recherche
  const searchFilters = ref({
    reference: '',
    manufacturer: '',
    status: '',
    riskLevel: '',
    healthScoreMax: null,
    posteId: null
  })

  // Récupérer tous les transformateurs avec pagination
  const fetchTransformateurs = async (page = 0, size = 10) => {
    loading.value = true
    error.value = null

    try {
      const params = new URLSearchParams()
      params.append('page', page)
      params.append('size', size)
      params.append('sort', 'createdAt,desc')

      const response = await axiosInstance.get('/transformateurs', { params })

      transformateurs.value = response.data.content
      totalElements.value = response.data.totalElements
      totalPages.value = response.data.totalPages
      currentPage.value = page
    } catch (err) {
      error.value = err.response?.data?.message || 'Erreur lors du chargement'
    } finally {
      loading.value = false
    }
  }

  // Recherche avancée avec filtres
  const searchTransformateurs = async (page = 0) => {
    loading.value = true
    error.value = null

    try {
      const params = new URLSearchParams()
      params.append('page', page)
      params.append('size', pageSize.value)
      params.append('sort', 'createdAt,desc')

      if (searchFilters.value.reference) params.append('reference', searchFilters.value.reference)
      if (searchFilters.value.manufacturer) params.append('manufacturer', searchFilters.value.manufacturer)
      if (searchFilters.value.status) params.append('status', searchFilters.value.status)
      if (searchFilters.value.riskLevel) params.append('riskLevel', searchFilters.value.riskLevel)
      if (searchFilters.value.healthScoreMax) params.append('healthScoreMax', searchFilters.value.healthScoreMax)
      if (searchFilters.value.posteId) params.append('posteId', searchFilters.value.posteId)

      const response = await axiosInstance.get('/transformateurs/search/advanced', { params })

      transformateurs.value = response.data.content
      totalElements.value = response.data.totalElements
      totalPages.value = response.data.totalPages
      currentPage.value = page
    } catch (err) {
      error.value = err.response?.data?.message || 'Erreur lors de la recherche'
    } finally {
      loading.value = false
    }
  }

  // Créer un transformateur
  const createTransformateur = async (data) => {
    loading.value = true
    error.value = null

    try {
      const response = await axiosInstance.post('/transformateurs', data)
      transformateurs.value.unshift(response.data)
      totalElements.value += 1
      return response.data
    } catch (err) {
      error.value = err.response?.data?.message || 'Erreur lors de la création'
      throw err
    } finally {
      loading.value = false
    }
  }

  // Mettre à jour un transformateur
  const updateTransformateur = async (id, data) => {
    loading.value = true
    error.value = null

    try {
      const response = await axiosInstance.put(`/transformateurs/${id}`, data)
      const index = transformateurs.value.findIndex(t => t.id === id)
      if (index !== -1) {
        transformateurs.value[index] = response.data
      }
      return response.data
    } catch (err) {
      error.value = err.response?.data?.message || 'Erreur lors de la modification'
      throw err
    } finally {
      loading.value = false
    }
  }

  // Supprimer un transformateur
  const deleteTransformateur = async (id) => {
    loading.value = true
    error.value = null

    try {
      await axiosInstance.delete(`/transformateurs/${id}`)
      transformateurs.value = transformateurs.value.filter(t => t.id !== id)
      totalElements.value -= 1
    } catch (err) {
      error.value = err.response?.data?.message || 'Erreur lors de la suppression'
      throw err
    } finally {
      loading.value = false
    }
  }

  // Récupérer les transformateurs d'un poste
  const fetchByPosteId = async (posteId) => {
    loading.value = true
    error.value = null

    try {
      const response = await axiosInstance.get(`/transformateurs/by-poste/${posteId}`)
      return response.data
    } catch (err) {
      error.value = err.response?.data?.message || 'Erreur lors du chargement'
      throw err
    } finally {
      loading.value = false
    }
  }

  // Récupérer les statistiques
  const fetchStatistics = async () => {
    try {
      const response = await axiosInstance.get('/transformateurs/statistics')
      statistics.value = response.data
    } catch (err) {
      console.error('Erreur lors de la récupération des statistiques:', err)
    }
  }

  // Réinitialiser les filtres
  const resetFilters = () => {
    searchFilters.value = {
      reference: '',
      manufacturer: '',
      status: '',
      riskLevel: '',
      healthScoreMax: null,
      posteId: null
    }
  }

  return {
    // État
    transformateurs,
    totalElements,
    totalPages,
    currentPage,
    pageSize,
    loading,
    error,
    statistics,
    searchFilters,

    // Actions
    fetchTransformateurs,
    searchTransformateurs,
    createTransformateur,
    updateTransformateur,
    deleteTransformateur,
    fetchByPosteId,
    fetchStatistics,
    resetFilters
  }
})
