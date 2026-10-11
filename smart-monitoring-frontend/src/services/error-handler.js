/**
 * Service de gestion centralisée des erreurs
 */

/**
 * Récupère le message d'erreur approprié selon le code HTTP
 */
export function getErrorMessage(error) {
  // Vérifier les erreurs de timeout
  if (error.code === 'ECONNABORTED' || error.message?.includes('timeout')) {
    return 'Délai d\'attente dépassé. La requête a pris trop de temps.'
  }

  // Vérifier les erreurs réseau réelles (pas de réponse du serveur)
  if (!error.response) {
    return error.message || 'Erreur réseau. Vérifiez que le serveur est accessible.'
  }

  const status = error.response.status
  const data = error.response.data

  switch (status) {
    case 400:
      // Bad Request
      return data?.message || 'Requête invalide. Vérifiez vos données.'

    case 401:
      // Unauthorized
      return 'Session expirée. Veuillez vous reconnecter.'

    case 403:
      // Forbidden
      return 'Accès refusé. Vous n\'avez pas les permissions requises.'

    case 404:
      // Not Found
      return 'Ressource non trouvée.'

    case 409:
      // Conflict
      return data?.message || 'Cette ressource existe déjà.'

    case 422:
      // Unprocessable Entity (Validation)
      if (data?.errors) {
        return Object.values(data.errors).flat().join('\n')
      }
      return data?.message || 'Erreur de validation.'

    case 500:
      // Server Error
      return 'Erreur serveur. Veuillez réessayer plus tard.'

    case 503:
      // Service Unavailable
      return 'Serveur indisponible. Veuillez réessayer plus tard.'

    default:
      return data?.message || `Erreur: ${status}`
  }
}

/**
 * Catégorise l'erreur pour styling approprié
 */
export function getErrorCategory(error) {
  if (!error?.response) {
    return 'network'
  }

  const status = error.response.status

  if (status >= 400 && status < 500) {
    return 'client'
  } else if (status >= 500) {
    return 'server'
  }

  return 'unknown'
}

/**
 * Vérifie si c'est une erreur d'authentification
 */
export function isAuthError(error) {
  return error?.response?.status === 401 || error?.response?.status === 403
}

/**
 * Vérifie si c'est une erreur de validation
 */
export function isValidationError(error) {
  return error?.response?.status === 400 || error?.response?.status === 422
}

/**
 * Extrait les erreurs de validation
 */
export function getValidationErrors(error) {
  if (error?.response?.data?.errors) {
    return error.response.data.errors
  }
  return {}
}

/**
 * Format d'erreur pour affichage
 */
export function formatError(error) {
  return {
    message: getErrorMessage(error),
    category: getErrorCategory(error),
    status: error?.response?.status || 0,
    isAuth: isAuthError(error),
    isValidation: isValidationError(error),
  }
}
