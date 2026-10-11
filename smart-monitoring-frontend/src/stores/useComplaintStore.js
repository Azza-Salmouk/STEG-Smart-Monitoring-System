import { defineStore } from 'pinia'
import { ref } from 'vue'
import { complaintsAPI } from '@/services/api'

/**
 * Store Pinia pour la gestion des réclamations
 */
export const useComplaintStore = defineStore('complaint', () => {
  // État
  const complaints = ref([])
  const totalElements = ref(0)
  const totalPages = ref(0)
  const currentPage = ref(0)
  const pageSize = ref(10)
  const loading = ref(false)
  const error = ref(null)
  const statistics = ref(null)

  // Filtres de recherche
  const searchFilters = ref({
    nomClient: '',
    statut: '',
    priorite: '',
    categorie: '',
    posteId: null,
    transformateurId: null,
    startDate: null,
    endDate: null
  })

  // Récupérer toutes les réclamations avec pagination
  const fetchComplaints = async (page = 0, size = 10) => {
    loading.value = true
    error.value = null

    try {
      const response = await complaintsAPI.getAll(page, size)
      complaints.value = response.data.content
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
  const searchComplaints = async (page = 0) => {
    loading.value = true
    error.value = null

    try {
      const response = await complaintsAPI.search(searchFilters.value, page, pageSize.value)
      complaints.value = response.data.content
      totalElements.value = response.data.totalElements
      totalPages.value = response.data.totalPages
      currentPage.value = page
    } catch (err) {
      error.value = err.response?.data?.message || 'Erreur lors de la recherche'
    } finally {
      loading.value = false
    }
  }

  // Créer une réclamation
  const createComplaint = async (data) => {
    loading.value = true
    error.value = null

    try {
      const response = await complaintsAPI.create(data)
      complaints.value.unshift(response.data)
      totalElements.value += 1
      return response.data
    } catch (err) {
      error.value = err.response?.data?.message || 'Erreur lors de la création'
      throw err
    } finally {
      loading.value = false
    }
  }

  // Mettre à jour une réclamation
  const updateComplaint = async (id, data) => {
    loading.value = true
    error.value = null

    try {
      const response = await complaintsAPI.update(id, data)
      const index = complaints.value.findIndex(c => c.id === id)
      if (index !== -1) {
        complaints.value[index] = response.data
      }
      return response.data
    } catch (err) {
      error.value = err.response?.data?.message || 'Erreur lors de la modification'
      throw err
    } finally {
      loading.value = false
    }
  }

  // Supprimer une réclamation
  const deleteComplaint = async (id) => {
    loading.value = true
    error.value = null

    try {
      await complaintsAPI.delete(id)
      complaints.value = complaints.value.filter(c => c.id !== id)
      totalElements.value -= 1
    } catch (err) {
      error.value = err.response?.data?.message || 'Erreur lors de la suppression'
      throw err
    } finally {
      loading.value = false
    }
  }

  // Récupérer les réclamations critiques
  const fetchCritical = async (page = 0) => {
    loading.value = true
    error.value = null

    try {
      const response = await complaintsAPI.getCritical(page, pageSize.value)
      complaints.value = response.data.content
      totalElements.value = response.data.totalElements
      totalPages.value = response.data.totalPages
      currentPage.value = page
    } catch (err) {
      error.value = err.response?.data?.message || 'Erreur lors du chargement'
      throw err
    } finally {
      loading.value = false
    }
  }

  // Récupérer les réclamations d'un superviseur
  const fetchBySupervisor = async (supervisorId, page = 0) => {
    loading.value = true
    error.value = null

    try {
      const response = await complaintsAPI.getBySupervisor(supervisorId, page, pageSize.value)
      complaints.value = response.data.content
      totalElements.value = response.data.totalElements
      totalPages.value = response.data.totalPages
      currentPage.value = page
    } catch (err) {
      error.value = err.response?.data?.message || 'Erreur lors du chargement'
      throw err
    } finally {
      loading.value = false
    }
  }

  // Assigner une réclamation
  const assignComplaint = async (complaintId, supervisorId) => {
    loading.value = true
    error.value = null

    try {
      const response = await complaintsAPI.assign(complaintId, supervisorId)
      const index = complaints.value.findIndex(c => c.id === complaintId)
      if (index !== -1) {
        complaints.value[index] = response.data
      }
      return response.data
    } catch (err) {
      error.value = err.response?.data?.message || 'Erreur lors de l\'assignation'
      throw err
    } finally {
      loading.value = false
    }
  }

  // Récupérer les statistiques
  const fetchStatistics = async () => {
    try {
      const response = await complaintsAPI.getStatistics()
      statistics.value = response.data
      return response.data
    } catch (err) {
      console.error('Erreur lors de la récupération des statistiques:', err)
      throw err
    }
  }

  // Réinitialiser les filtres
  const resetFilters = () => {
    searchFilters.value = {
      nomClient: '',
      statut: '',
      priorite: '',
      categorie: '',
      posteId: null,
      transformateurId: null,
      startDate: null,
      endDate: null
    }
  }

  // Getters calculés
  const criticalCount = () => {
    return complaints.value.filter(c => c.priorite === 'CRITICAL').length
  }

  const nouvelleCount = () => {
    return complaints.value.filter(c => c.statut === 'NOUVELLE').length
  }

  const enTraitementCount = () => {
    return complaints.value.filter(c => c.statut === 'EN_TRAITEMENT').length
  }

  return {
    // État
    complaints,
    totalElements,
    totalPages,
    currentPage,
    pageSize,
    loading,
    error,
    statistics,
    searchFilters,

    // Actions
    fetchComplaints,
    searchComplaints,
    createComplaint,
    updateComplaint,
    deleteComplaint,
    fetchCritical,
    fetchBySupervisor,
    assignComplaint,
    fetchStatistics,
    resetFilters,

    // Getters
    criticalCount,
    nouvelleCount,
    enTraitementCount
  }
})
