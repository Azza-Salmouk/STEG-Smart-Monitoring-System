import { defineStore } from 'pinia'
import { ref } from 'vue'
import axiosInstance from '@/api/axios'

/**
 * Store Pinia pour la gestion des postes
 */
export const usePosteStore = defineStore('poste', () => {
  // État
  const postes = ref([])
  const totalElements = ref(0)
  const totalPages = ref(0)
  const currentPage = ref(0)
  const pageSize = ref(10)
  const loading = ref(false)
  const error = ref(null)
  const statistics = ref(null)

  // Filtres de recherche
  const searchFilters = ref({
    nom: '',
    region: '',
    etat: '',
    typePoste: ''
  })

  // Récupérer tous les postes avec pagination et filtres
  const fetchPostes = async (page = 0, size = 10) => {
    loading.value = true
    error.value = null

    try {
      // Construire les paramètres de recherche
      const params = new URLSearchParams()
      params.append('page', page)
      params.append('size', size)
      params.append('sort', 'createdAt,desc')

      // Appeler l'endpoint
      const response = await axiosInstance.get('/postes', { params })

      postes.value = response.data.content
      totalElements.value = response.data.totalElements
      totalPages.value = response.data.totalPages
      currentPage.value = page
    } catch (err) {
      error.value = err.response?.data?.message || 'Erreur lors du chargement des postes'
    } finally {
      loading.value = false
    }
  }

  // Recherche avancée avec filtres
  const searchPostes = async (page = 0) => {
    loading.value = true
    error.value = null

    try {
      const params = new URLSearchParams()
      params.append('page', page)
      params.append('size', pageSize.value)
      params.append('sort', 'createdAt,desc')

      if (searchFilters.value.nom) params.append('nom', searchFilters.value.nom)
      if (searchFilters.value.region) params.append('region', searchFilters.value.region)
      if (searchFilters.value.etat) params.append('etat', searchFilters.value.etat)
      if (searchFilters.value.typePoste) params.append('typePoste', searchFilters.value.typePoste)

      const response = await axiosInstance.get('/postes/search/advanced', { params })

      postes.value = response.data.content
      totalElements.value = response.data.totalElements
      totalPages.value = response.data.totalPages
      currentPage.value = page
    } catch (err) {
      error.value = err.response?.data?.message || 'Erreur lors de la recherche'
    } finally {
      loading.value = false
    }
  }

  // Créer un poste
  const createPoste = async (data) => {
    loading.value = true
    error.value = null

    try {
      const response = await axiosInstance.post('/postes', data)
      postes.value.unshift(response.data)
      totalElements.value += 1
      return response.data
    } catch (err) {
      error.value = err.response?.data?.message || 'Erreur lors de la création'
      throw err
    } finally {
      loading.value = false
    }
  }

  // Mettre à jour un poste
  const updatePoste = async (id, data) => {
    loading.value = true
    error.value = null

    try {
      const response = await axiosInstance.put(`/postes/${id}`, data)
      const index = postes.value.findIndex(p => p.id === id)
      if (index !== -1) {
        postes.value[index] = response.data
      }
      return response.data
    } catch (err) {
      error.value = err.response?.data?.message || 'Erreur lors de la modification'
      throw err
    } finally {
      loading.value = false
    }
  }

  // Supprimer un poste
  const deletePoste = async (id) => {
    loading.value = true
    error.value = null

    try {
      await axiosInstance.delete(`/postes/${id}`)
      postes.value = postes.value.filter(p => p.id !== id)
      totalElements.value -= 1
    } catch (err) {
      error.value = err.response?.data?.message || 'Erreur lors de la suppression'
      throw err
    } finally {
      loading.value = false
    }
  }

  // Récupérer les statistiques
  const fetchStatistics = async () => {
    try {
      const response = await axiosInstance.get('/postes/statistics')
      statistics.value = response.data
    } catch (err) {
      console.error('Erreur lors de la récupération des statistiques:', err)
    }
  }

  // Réinitialiser les filtres
  const resetFilters = () => {
    searchFilters.value = {
      nom: '',
      region: '',
      etat: '',
      typePoste: ''
    }
  }

  return {
    // État
    postes,
    totalElements,
    totalPages,
    currentPage,
    pageSize,
    loading,
    error,
    statistics,
    searchFilters,

    // Actions
    fetchPostes,
    searchPostes,
    createPoste,
    updatePoste,
    deletePoste,
    fetchStatistics,
    resetFilters
  }
})
