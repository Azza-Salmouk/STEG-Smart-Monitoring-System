/**
 * Service API centralisé
 * Gère tous les appels API vers le backend
 */

import axiosInstance from '@/api/axios'

/**
 * Auth API
 */
export const authAPI = {
  login: (credentials) =>
    axiosInstance.post('/auth/login', credentials),

  register: (userData) =>
    axiosInstance.post('/auth/register', userData),

  logout: () =>
    axiosInstance.post('/auth/logout'),
}

/**
 * Users API
 */
export const usersAPI = {
  getAll: (page = 0, size = 10) =>
    axiosInstance.get('/users', { params: { page, size, sort: 'createdAt,desc' } }),

  getById: (id) =>
    axiosInstance.get(`/users/${id}`),

  create: (userData) =>
    axiosInstance.post('/users', userData),

  update: (id, userData) =>
    axiosInstance.put(`/users/${id}`, userData),

  delete: (id) =>
    axiosInstance.delete(`/users/${id}`),

  search: (filters, page = 0, size = 10) =>
    axiosInstance.get('/users/search/advanced', {
      params: { ...filters, page, size, sort: 'createdAt,desc' },
    }),
}

/**
 * Postes API
 */
export const postesAPI = {
  getAll: (page = 0, size = 10) =>
    axiosInstance.get('/postes', {
      params: { page, size, sort: 'createdAt,desc' },
    }),

  getById: (id) =>
    axiosInstance.get(`/postes/${id}`),

  create: (data) =>
    axiosInstance.post('/postes', data),

  update: (id, data) =>
    axiosInstance.put(`/postes/${id}`, data),

  delete: (id) =>
    axiosInstance.delete(`/postes/${id}`),

  search: (filters, page = 0, size = 10) =>
    axiosInstance.get('/postes/search/advanced', {
      params: { ...filters, page, size, sort: 'createdAt,desc' },
    }),

  getStatistics: () =>
    axiosInstance.get('/postes/statistics'),

  importFromExcel: (file) => {
    const formData = new FormData()
    formData.append('file', file)
    return axiosInstance.post('/postes/import', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })
  },
}

/**
 * Transformateurs API
 */
export const transformateursAPI = {
  getAll: (page = 0, size = 10) =>
    axiosInstance.get('/transformateurs', {
      params: { page, size, sort: 'createdAt,desc' },
    }),

  getById: (id) =>
    axiosInstance.get(`/transformateurs/${id}`),

  create: (data) =>
    axiosInstance.post('/transformateurs', data),

  update: (id, data) =>
    axiosInstance.put(`/transformateurs/${id}`, data),

  delete: (id) =>
    axiosInstance.delete(`/transformateurs/${id}`),

  getByPosteId: (posteId) =>
    axiosInstance.get(`/transformateurs/by-poste/${posteId}`),

  search: (filters, page = 0, size = 10) =>
    axiosInstance.get('/transformateurs/search/advanced', {
      params: { ...filters, page, size, sort: 'createdAt,desc' },
    }),

  getStatistics: () =>
    axiosInstance.get('/transformateurs/statistics'),

  getCritical: () =>
    axiosInstance.get('/transformateurs/critical'),

  importFromExcel: (file) => {
    const formData = new FormData()
    formData.append('file', file)
    return axiosInstance.post('/transformateurs/import', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })
  },

  // Get transformateur with related data
  getDetail: (id) =>
    axiosInstance.get(`/transformateurs/${id}`),
}

/**
 * Measurements API
 * API pour les mesures IoT des transformateurs
 */
export const measurementsAPI = {
  // CRUD de base
  getAll: (page = 0, size = 10) =>
    axiosInstance.get('/measurements', {
      params: { page, size, sort: 'measurementDate,desc' },
    }),

  getById: (id) =>
    axiosInstance.get(`/measurements/${id}`),

  create: (data) =>
    axiosInstance.post('/measurements', data),

  update: (id, data) =>
    axiosInstance.put(`/measurements/${id}`, data),

  delete: (id) =>
    axiosInstance.delete(`/measurements/${id}`),

  // Historique et dernière mesure
  getByTransformateur: (transformateurId, page = 0, size = 10) =>
    axiosInstance.get(`/measurements/by-transformateur/${transformateurId}`, {
      params: { page, size, sort: 'measurementDate,desc' },
    }),

  getLatest: (transformateurId) =>
    axiosInstance.get('/measurements/latest', {
      params: { transformateurId },
    }),

  getHistory: (transformateurId, page = 0, size = 10) =>
    axiosInstance.get(`/measurements/history/${transformateurId}`, {
      params: { page, size, sort: 'measurementDate,desc' },
    }),

  // Filtrage par statut
  getCritical: (page = 0, size = 10) =>
    axiosInstance.get('/measurements/critical', {
      params: { page, size },
    }),

  getWarning: (page = 0, size = 10) =>
    axiosInstance.get('/measurements/warning', {
      params: { page, size },
    }),

  getToday: (page = 0, size = 10) =>
    axiosInstance.get('/measurements/today', {
      params: { page, size },
    }),

  // Recherche avancée
  search: (filters, page = 0, size = 10) =>
    axiosInstance.get('/measurements/search', {
      params: { ...filters, page, size, sort: 'measurementDate,desc' },
    }),

  // Statistiques
  getStatistics: () =>
    axiosInstance.get('/measurements/statistics'),

  getStatisticsByTransformateur: (transformateurId) =>
    axiosInstance.get(`/measurements/statistics/${transformateurId}`),

  countCritical: () =>
    axiosInstance.get('/measurements/count/critical'),

  getTransformateursWithCritical: () =>
    axiosInstance.get('/measurements/transformateurs/critical'),

  getAverageHealthScore: () =>
    axiosInstance.get('/measurements/health-score/average'),

  getAverageHealthScoreByTransformateur: (transformateurId) =>
    axiosInstance.get(`/measurements/health-score/average/${transformateurId}`),
}


/**
 * Interventions API
 */
export const interventionsAPI = {
  // CRUD de base
  getAll: (page = 0, size = 10) =>
    axiosInstance.get('/interventions', {
      params: { page, size, sort: 'createdAt,desc' },
    }),

  getById: (id) =>
    axiosInstance.get(`/interventions/${id}`),

  create: (data) =>
    axiosInstance.post('/interventions', data),

  update: (id, data) =>
    axiosInstance.put(`/interventions/${id}`, data),

  delete: (id) =>
    axiosInstance.delete(`/interventions/${id}`),

  // Filtrage par agent et transformateur
  getByAgent: (agentId, page = 0, size = 10) =>
    axiosInstance.get(`/interventions/by-agent/${agentId}`, {
      params: { page, size, sort: 'createdAt,desc' },
    }),

  getByTransformateur: (transformateurId, page = 0, size = 10) =>
    axiosInstance.get(`/interventions/by-transformateur/${transformateurId}`, {
      params: { page, size, sort: 'createdAt,desc' },
    }),

  // Recherche par statut et priorité
  getByStatut: (statut, page = 0, size = 10) =>
    axiosInstance.get('/interventions/search/by-statut', {
      params: { statut, page, size, sort: 'createdAt,desc' },
    }),

  getByPriorite: (priorite, page = 0, size = 10) =>
    axiosInstance.get('/interventions/search/by-priorite', {
      params: { priorite, page, size, sort: 'createdAt,desc' },
    }),

  // Recherche par période
  getByDateRange: (startDate, endDate, page = 0, size = 10) =>
    axiosInstance.get('/interventions/search/by-date-range', {
      params: { startDate, endDate, page, size, sort: 'createdAt,desc' },
    }),

  // Recherche avancée
  search: (filters, page = 0, size = 10) =>
    axiosInstance.get('/interventions/search/advanced', {
      params: { ...filters, page, size, sort: 'createdAt,desc' },
    }),

  // Interventions particulières
  getCritical: (page = 0, size = 10) =>
    axiosInstance.get('/interventions/critical', {
      params: { page, size, sort: 'createdAt,desc' },
    }),

  getOngoingByAgent: (agentId, page = 0, size = 10) =>
    axiosInstance.get(`/interventions/ongoing/by-agent/${agentId}`, {
      params: { page, size, sort: 'createdAt,desc' },
    }),

  // Statistiques
  getStatistics: () =>
    axiosInstance.get('/interventions/statistics'),
}


/**
 * Complaints API
 */
export const complaintsAPI = {
  // CRUD de base
  getAll: (page = 0, size = 10) =>
    axiosInstance.get('/complaints', {
      params: { page, size, sort: 'createdAt,desc' },
    }),

  getById: (id) =>
    axiosInstance.get(`/complaints/${id}`),

  create: (data) =>
    axiosInstance.post('/complaints', data),

  update: (id, data) =>
    axiosInstance.put(`/complaints/${id}`, data),

  delete: (id) =>
    axiosInstance.delete(`/complaints/${id}`),

  // Filtrage par statut, priorité et catégorie
  getByStatut: (statut, page = 0, size = 10) =>
    axiosInstance.get('/complaints/search/by-statut', {
      params: { statut, page, size, sort: 'createdAt,desc' },
    }),

  getByPriorite: (priorite, page = 0, size = 10) =>
    axiosInstance.get('/complaints/search/by-priorite', {
      params: { priorite, page, size, sort: 'createdAt,desc' },
    }),

  getByCategorie: (categorie, page = 0, size = 10) =>
    axiosInstance.get('/complaints/search/by-categorie', {
      params: { categorie, page, size, sort: 'createdAt,desc' },
    }),

  // Recherche par client, superviseur, poste
  searchByClient: (nomClient, page = 0, size = 10) =>
    axiosInstance.get('/complaints/search/by-client', {
      params: { nomClient, page, size, sort: 'createdAt,desc' },
    }),

  getBySupervisor: (supervisorId, page = 0, size = 10) =>
    axiosInstance.get(`/complaints/by-supervisor/${supervisorId}`, {
      params: { page, size, sort: 'createdAt,desc' },
    }),

  getByPoste: (posteId, page = 0, size = 10) =>
    axiosInstance.get(`/complaints/by-poste/${posteId}`, {
      params: { page, size, sort: 'createdAt,desc' },
    }),

  getByTransformateur: (transformateurId, page = 0, size = 10) =>
    axiosInstance.get(`/complaints/by-transformateur/${transformateurId}`, {
      params: { page, size, sort: 'createdAt,desc' },
    }),

  // Recherche par période
  getByDateRange: (startDate, endDate, page = 0, size = 10) =>
    axiosInstance.get('/complaints/search/by-date-range', {
      params: { startDate, endDate, page, size, sort: 'createdAt,desc' },
    }),

  // Recherche avancée
  search: (filters, page = 0, size = 10) =>
    axiosInstance.get('/complaints/search/advanced', {
      params: { ...filters, page, size, sort: 'createdAt,desc' },
    }),

  // Réclamations particulières
  getCritical: (page = 0, size = 10) =>
    axiosInstance.get('/complaints/critical', {
      params: { page, size, sort: 'createdAt,desc' },
    }),

  // Assignation
  assign: (complaintId, supervisorId) =>
    axiosInstance.post(`/complaints/${complaintId}/assign/${supervisorId}`),

  // Statistiques
  getStatistics: () =>
    axiosInstance.get('/complaints/statistics'),
}

/**
 * Dashboard API
 */
export const dashboardAPI = {
  getAdminDashboard: () =>
    axiosInstance.get('/dashboard/admin'),

  getSupervisorDashboard: () =>
    axiosInstance.get('/dashboard/supervisor'),

  getAgentDashboard: (agentId) =>
    axiosInstance.get(`/dashboard/agent/${agentId}`),

  getServiceClientDashboard: () =>
    axiosInstance.get('/dashboard/service-client'),
}

/**
 * Alerts API
 */
export const alertsAPI = {
  // CRUD de base
  getAll: (page = 0, size = 10) =>
    axiosInstance.get('/alerts', {
      params: { page, size, sort: 'createdAt,desc' },
    }),

  getById: (id) =>
    axiosInstance.get(`/alerts/${id}`),

  create: (data) =>
    axiosInstance.post('/alerts', data),

  update: (id, data) =>
    axiosInstance.put(`/alerts/${id}`, data),

  delete: (id) =>
    axiosInstance.delete(`/alerts/${id}`),

  // Filtrage par statut, niveau et type
  getByStatut: (statut, page = 0, size = 10) =>
    axiosInstance.get('/alerts/search/by-statut', {
      params: { statut, page, size, sort: 'createdAt,desc' },
    }),

  getByNiveau: (niveau, page = 0, size = 10) =>
    axiosInstance.get('/alerts/search/by-niveau', {
      params: { niveau, page, size, sort: 'createdAt,desc' },
    }),

  getByType: (type, page = 0, size = 10) =>
    axiosInstance.get('/alerts/search/by-type', {
      params: { type, page, size, sort: 'createdAt,desc' },
    }),

  // Recherche par titre, transformateur, poste
  searchByTitre: (titre, page = 0, size = 10) =>
    axiosInstance.get('/alerts/search/by-titre', {
      params: { titre, page, size, sort: 'createdAt,desc' },
    }),

  getByTransformateur: (transformateurId, page = 0, size = 10) =>
    axiosInstance.get(`/alerts/by-transformateur/${transformateurId}`, {
      params: { page, size, sort: 'createdAt,desc' },
    }),

  getByPoste: (posteId, page = 0, size = 10) =>
    axiosInstance.get(`/alerts/by-poste/${posteId}`, {
      params: { page, size, sort: 'createdAt,desc' },
    }),

  // Recherche par période
  getByDateRange: (startDate, endDate, page = 0, size = 10) =>
    axiosInstance.get('/alerts/search/by-date-range', {
      params: { startDate, endDate, page, size, sort: 'createdAt,desc' },
    }),

  // Recherche avancée
  searchAdvanced: (filters, page = 0, size = 10) =>
    axiosInstance.get('/alerts/search/advanced', {
      params: { ...filters, page, size, sort: 'createdAt,desc' },
    }),

  // Alertes particulières
  getCritical: (page = 0, size = 10) =>
    axiosInstance.get('/alerts/critical', {
      params: { page, size, sort: 'createdAt,desc' },
    }),

  getActive: (page = 0, size = 10) =>
    axiosInstance.get('/alerts/active', {
      params: { page, size, sort: 'createdAt,desc' },
    }),

  getRecent: (page = 0, size = 10) =>
    axiosInstance.get('/alerts/recent', {
      params: { page, size, sort: 'createdAt,desc' },
    }),

  // Actions
  acknowledge: (alertId) =>
    axiosInstance.post(`/alerts/${alertId}/acknowledge`),

  resolve: (alertId, userId) =>
    axiosInstance.post(`/alerts/${alertId}/resolve/${userId}`),

  archive: (alertId) =>
    axiosInstance.post(`/alerts/${alertId}/archive`),

  changeStatus: (alertId, newStatus) =>
    axiosInstance.put(`/alerts/${alertId}/status`, { newStatus }, {
      params: { newStatus },
    }),

  // Statistiques
  getStatistics: () =>
    axiosInstance.get('/alerts/statistics'),
}

/**
 * Predictions API
 */
export const predictionsAPI = {
  // Get prediction for a transformateur
  getPrediction: (transformateurId) =>
    axiosInstance.get(`/prediction/transformateurs/${transformateurId}`),

  // Get recommendations for a transformateur
  getRecommendations: (transformateurId) =>
    axiosInstance.get(`/prediction/transformateurs/${transformateurId}/recommendations`),

  // Get all predictions
  getAllPredictions: () =>
    axiosInstance.get('/prediction/all'),

  // Get critical predictions
  getCriticalPredictions: (page = 0, size = 10) =>
    axiosInstance.get('/prediction/critical', {
      params: { page, size, sort: 'failureRiskScore,desc' },
    }),
}
