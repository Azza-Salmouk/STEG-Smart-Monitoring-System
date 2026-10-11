/**
 * Utilitaires pour la gestion des JWT tokens
 */

/**
 * Décoder un JWT token
 * @param {string} token - Le JWT token à décoder
 * @returns {Object} Les données décodées du token
 */
export function decodeToken(token) {
  if (!token) return null

  try {
    const parts = token.split('.')
    if (parts.length !== 3) {
      throw new Error('Invalid token format')
    }

    const decoded = JSON.parse(atob(parts[1]))
    return decoded
  } catch (error) {
    console.error('Erreur de décodage du token:', error)
    return null
  }
}

/**
 * Vérifier si un token est expiré
 * @param {string} token - Le JWT token
 * @returns {boolean}
 */
export function isTokenExpired(token) {
  const decoded = decodeToken(token)
  if (!decoded || !decoded.exp) return true

  const expirationTime = decoded.exp * 1000 // Convertir en millisecondes
  return Date.now() >= expirationTime
}

/**
 * Obtenir le temps restant avant expiration (en minutes)
 * @param {string} token - Le JWT token
 * @returns {number} Temps restant en minutes
 */
export function getTokenExpirationTime(token) {
  const decoded = decodeToken(token)
  if (!decoded || !decoded.exp) return 0

  const expirationTime = decoded.exp * 1000
  const remainingTime = expirationTime - Date.now()
  return Math.floor(remainingTime / 60000) // Convertir en minutes
}

export default {
  decodeToken,
  isTokenExpired,
  getTokenExpirationTime,
}
