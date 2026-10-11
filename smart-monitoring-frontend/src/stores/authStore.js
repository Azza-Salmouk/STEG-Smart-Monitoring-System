import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { authAPI } from '@/services/api'

/**
 * Store Pinia pour la gestion de l'authentification
 */
export const useAuthStore = defineStore('auth', () => {
  // État
  const token = ref(null)
  const user = ref(null)
  const role = ref(null)
  const email = ref(null)
  const username = ref(null)
  const loading = ref(false)
  const error = ref(null)

  // Propriétés calculées
  const isAuthenticated = computed(() => !!token.value)

  /**
   * Connexion utilisateur
   * @param {Object} credentials - {email, password}
   */
  const login = async (credentials) => {
    loading.value = true
    error.value = null

    try {
      const response = await authAPI.login(credentials)
      const { data } = response

      // Sauvegarder les données dans le store
      token.value = data.token
      user.value = {
        id: data.id,
        username: data.username,
        email: data.email,
        role: data.role,
      }
      role.value = data.role
      email.value = data.email
      username.value = data.username

      // Sauvegarder le token dans localStorage
      localStorage.setItem('token', data.token)
      localStorage.setItem('user', JSON.stringify({
        id: data.id,
        username: data.username,
        email: data.email,
        role: data.role,
      }))

      return data
    } catch (err) {
      error.value = err.response?.data?.message || 'Erreur de connexion'
      throw err
    } finally {
      loading.value = false
    }
  }

  /**
   * Déconnexion utilisateur
   */
  const logout = () => {
    token.value = null
    user.value = null
    role.value = null
    email.value = null
    username.value = null
    error.value = null

    // Supprimer les données du localStorage
    localStorage.removeItem('token')
    localStorage.removeItem('user')
  }

  /**
   * Restaurer la session à partir du localStorage
   */
  const restoreSession = () => {
    const storedToken = localStorage.getItem('token')
    const storedUser = localStorage.getItem('user')

    if (storedToken && storedUser) {
      token.value = storedToken
      const user_data = JSON.parse(storedUser)
      user.value = user_data
      username.value = user_data.username
      email.value = user_data.email
      role.value = user_data.role
    }
  }

  /**
   * Effacer les erreurs
   */
  const clearError = () => {
    error.value = null
  }

  return {
    // État
    token,
    user,
    role,
    email,
    username,
    loading,
    error,

    // Propriétés calculées
    isAuthenticated,

    // Actions
    login,
    logout,
    restoreSession,
    clearError,
  }
})
