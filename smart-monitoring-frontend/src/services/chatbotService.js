/**
 * Service Chatbot pour l'intégration avec le backend IA
 * Gère les appels à l'endpoint POST /api/ai/chatbot/message
 */

import axiosInstance from '@/api/axios'
import { getErrorMessage } from './error-handler'

/**
 * Envoie un message au chatbot et reçoit une réponse IA
 * @param {string} message - Le message utilisateur
 * @returns {Promise<Object>} - Réponse du chatbot { response, timestamp }
 * @throws {Error} - Erreur réseau, validation ou serveur
 */
export const sendChatbotMessage = async (message) => {
  if (!message || message.trim().length === 0) {
    throw new Error('Le message ne peut pas être vide.')
  }

  try {
    const response = await axiosInstance.post('/ai/chatbot/message', {
      message: message.trim(),
    })

    // Le backend retourne { response, timestamp }
    return {
      response: response.data.response,
      timestamp: response.data.timestamp || new Date().toISOString(),
    }
  } catch (error) {
    // Gérer les erreurs spécifiques
    if (error.response?.status === 503) {
      throw new Error(
        'Le service d\'intelligence artificielle est actuellement indisponible. Veuillez réessayer plus tard.'
      )
    }

    if (error.response?.status === 400) {
      throw new Error(
        error.response.data?.message || 'Message invalide. Veuillez vérifier votre entrée.'
      )
    }

    if (error.response?.status === 401) {
      throw new Error('Session expirée. Veuillez vous reconnecter.')
    }

    if (error.response?.status === 403) {
      throw new Error('Vous n\'avez pas accès au chatbot.')
    }

    // Vérifier si c'est un timeout
    if (error.code === 'ECONNABORTED' || error.message?.includes('timeout')) {
      throw new Error(
        'La requête a dépassé le délai d\'attente. Le modèle d\'IA prend du temps à traiter votre question. Veuillez réessayer.'
      )
    }

    // Erreur réseau ou autre
    throw new Error(getErrorMessage(error))
  }
}

/**
 * Vérifie la santé du service chatbot
 * @returns {Promise<Object>} - Statut du service { healthy, timestamp }
 */
export const checkChatbotHealth = async () => {
  try {
    const response = await axiosInstance.get('/ai/chatbot/health')
    return {
      healthy: response.status === 200,
      timestamp: new Date().toISOString(),
    }
  } catch (error) {
    return {
      healthy: false,
      timestamp: new Date().toISOString(),
      error: getErrorMessage(error),
    }
  }
}

export default {
  sendChatbotMessage,
  checkChatbotHealth,
}
