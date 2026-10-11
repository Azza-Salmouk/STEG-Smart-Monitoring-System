import axios from 'axios'
import { useAuthStore } from '@/stores/authStore'

// Configuration de base d'Axios
const axiosInstance = axios.create({
  baseURL: 'http://localhost:8081/api',
  timeout: 150000, // 150 secondes (2.5 min) pour accommoder le timeout backend de 120s (Ollama inference 60-120s + processing) + marge de sécurité
  headers: {
    'Content-Type': 'application/json',
  },
})

// Intercepteur pour ajouter le token Bearer à chaque requête
axiosInstance.interceptors.request.use(
  (config) => {
    const authStore = useAuthStore()
    if (authStore.token) {
      config.headers.Authorization = `Bearer ${authStore.token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// Intercepteur pour gérer les erreurs de réponse
axiosInstance.interceptors.response.use(
  (response) => response,
  (error) => {
    const authStore = useAuthStore()
    
    // Si token expiré ou non autorisé, déconnecter l'utilisateur
    if (error.response?.status === 401) {
      authStore.logout()
      window.location.href = '/login'
    }
    
    return Promise.reject(error)
  }
)

export default axiosInstance
