import { defineStore } from 'pinia'
import { ref } from 'vue'
import { interventionsAPI } from '@/services/api'

/**
 * Store Pinia pour la gestion des interventions
 */
export const useInterventionStore = defineStore('intervention', () => {
  // État
  const interventions = ref([])
  const totalElements = ref(0)
  const totalPages = ref(0)
  const currentPage = ref(0)
  const pageSize = ref(10)
  const loading = ref(false)
  const error = ref(null)
  const statistics = ref(null)

  // Filtres de recherche
  const searchFilters = ref({
    transformateurId: null,
    agentId: null,
    statut: '',
    priorite: '',
    typeIntervention: '',
    startDate: null,
    endDate: null
  })

  // Récupérer toutes les interventions avec pagination
  const fetchInterventions = async (page = 0, size = 10) => {
    loading.value = true
    error.value = null

    try {
      const response = await interventionsAPI.getAll(page, size)
      interventions.value = response.data.content
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
  const searchInterventions = async (page = 0) => {
    loading.value = true
    error.value = null

    try {
      const response = await interventionsAPI.search(searchFilters.value, page, pageSize.value)
      interventions.value = response.data.content
      totalElements.value = response.data.totalElements
      totalPages.value = response.data.totalPages
      currentPage.value = page
    } catch (err) {
      error.value = err.response?.data?.message || 'Erreur lors de la recherche'
    } finally {
      loading.value = false
    }
  }

  // Créer une intervention
  const createIntervention = async (data) => {
    loading.value = true
    error.value = null

    try {
      const response = await interventionsAPI.create(data)
      interventions.value.unshift(response.data)
      totalElements.value += 1
      return response.data
    } catch (err) {
      error.value = err.response?.data?.message || 'Erreur lors de la création'
      throw err
    } finally {
      loading.value = false
    }
  }

  // Mettre à jour une intervention
  const updateIntervention = async (id, data) => {
    loading.value = true
    error.value = null

    try {
      const response = await interventionsAPI.update(id, data)
      const index = interventions.value.findIndex(i => i.id === id)
      if (index !== -1) {
        interventions.value[index] = response.data
      }
      return response.data
    } catch (err) {
      error.value = err.response?.data?.message || 'Erreur lors de la modification'
      throw err
    } finally {
      loading.value = false
    }
  }

  // Supprimer une intervention
  const deleteIntervention = async (id) => {
    loading.value = true
    error.value = null

    try {
      await interventionsAPI.delete(id)
      interventions.value = interventions.value.filter(i => i.id !== id)
      totalElements.value -= 1
    } catch (err) {
      error.value = err.response?.data?.message || 'Erreur lors de la suppression'
      throw err
    } finally {
      loading.value = false
    }
  }

  // Récupérer les interventions d'un agent
  const fetchByAgent = async (agentId, page = 0) => {
    loading.value = true
    error.value = null

    try {
      const response = await interventionsAPI.getByAgent(agentId, page, pageSize.value)
      interventions.value = response.data.content
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

  // Récupérer les interventions d'un transformateur
  const fetchByTransformateur = async (transformateurId, page = 0) => {
    loading.value = true
    error.value = null

    try {
      const response = await interventionsAPI.getByTransformateur(transformateurId, page, pageSize.value)
      interventions.value = response.data.content
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

  // Récupérer les interventions critiques
  const fetchCritical = async (page = 0) => {
    loading.value = true
    error.value = null

    try {
      const response = await interventionsAPI.getCritical(page, pageSize.value)
      interventions.value = response.data.content
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

  // Récupérer les interventions en cours d'un agent
  const fetchOngoingByAgent = async (agentId, page = 0) => {
    loading.value = true
    error.value = null

    try {
      const response = await interventionsAPI.getOngoingByAgent(agentId, page, pageSize.value)
      interventions.value = response.data.content
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

  // Récupérer les statistiques
  const fetchStatistics = async () => {
    try {
      const response = await interventionsAPI.getStatistics()
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
      transformateurId: null,
      agentId: null,
      statut: '',
      priorite: '',
      typeIntervention: '',
      startDate: null,
      endDate: null
    }
  }

  // Getters calculés
  const criticalCount = () => {
    return interventions.value.filter(i => i.priorite === 'CRITIQUE' && i.statut !== 'TERMINEE').length
  }

  const ongoingCount = () => {
    return interventions.value.filter(i => i.statut === 'EN_COURS').length
  }

  const pendingCount = () => {
    return interventions.value.filter(i => i.statut === 'EN_ATTENTE').length
  }

  return {
    // État
    interventions,
    totalElements,
    totalPages,
    currentPage,
    pageSize,
    loading,
    error,
    statistics,
    searchFilters,

    // Actions
    fetchInterventions,
    searchInterventions,
    createIntervention,
    updateIntervention,
    deleteIntervention,
    fetchByAgent,
    fetchByTransformateur,
    fetchCritical,
    fetchOngoingByAgent,
    fetchStatistics,
    resetFilters,

    // Getters
    criticalCount,
    ongoingCount,
    pendingCount
  }
})
