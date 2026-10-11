/**
 * Composable pour gérer les notifications Toast
 * À utiliser avec le composant Toast.vue
 */

import { ref } from 'vue'

const notifications = ref([])
let nextId = 0

function add(message, type = 'info', duration = 5000) {
  const id = nextId++
  const notification = { id, message, type }
  notifications.value.push(notification)

  if (duration > 0) {
    setTimeout(() => remove(id), duration)
  }

  return id
}

function remove(id) {
  const index = notifications.value.findIndex(n => n.id === id)
  if (index !== -1) {
    notifications.value.splice(index, 1)
  }
}

function success(message, duration = 5000) {
  return add(message, 'success', duration)
}

function error(message, duration = 5000) {
  return add(message, 'error', duration)
}

function warning(message, duration = 5000) {
  return add(message, 'warning', duration)
}

function info(message, duration = 5000) {
  return add(message, 'info', duration)
}

function clear() {
  notifications.value = []
}

export const useToast = () => ({
  notifications,
  add,
  remove,
  success,
  error,
  warning,
  info,
  clear,
})
