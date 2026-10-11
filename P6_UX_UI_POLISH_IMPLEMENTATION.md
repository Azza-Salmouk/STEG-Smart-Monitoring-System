# P6 - UX/UI Polish & Notifications - Implementation Guide

## Overview
Comprehensive user experience enhancements including:
- Loading skeleton screens
- Empty state displays
- Error handling & boundaries
- Success notifications
- Toast messages
- Loading indicators
- Responsive feedback systems

---

## 1. NOTIFICATION STORE (Pinia)

### NotificationStore.js
```javascript
import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useNotificationStore = defineStore('notification', () => {
  const notifications = ref([])
  let nextId = 0

  const addNotification = (message, type = 'info', duration = 5000) => {
    const id = nextId++
    const notification = {
      id,
      message,
      type, // 'success', 'error', 'warning', 'info'
      timestamp: new Date()
    }
    notifications.value.push(notification)

    if (duration > 0) {
      setTimeout(() => {
        removeNotification(id)
      }, duration)
    }
    return id
  }

  const removeNotification = (id) => {
    const index = notifications.value.findIndex(n => n.id === id)
    if (index > -1) {
      notifications.value.splice(index, 1)
    }
  }

  const showSuccess = (message, duration = 5000) =>
    addNotification(message, 'success', duration)

  const showError = (message, duration = 7000) =>
    addNotification(message, 'error', duration)

  const showWarning = (message, duration = 6000) =>
    addNotification(message, 'warning', duration)

  const showInfo = (message, duration = 5000) =>
    addNotification(message, 'info', duration)

  return {
    notifications,
    addNotification,
    removeNotification,
    showSuccess,
    showError,
    showWarning,
    showInfo
  }
})
```

---

## 2. TOAST NOTIFICATION COMPONENT

### Toast.vue
```vue
<template>
  <div class="fixed top-4 right-4 z-50 space-y-2 w-full max-w-sm pointer-events-none">
    <TransitionGroup name="toast">
      <div
        v-for="notification in notifications"
        :key="notification.id"
        :class="[
          'p-4 rounded-lg shadow-lg flex items-center justify-between text-white pointer-events-auto',
          getNotificationClass(notification.type)
        ]"
      >
        <div class="flex items-center gap-3">
          <span class="text-xl">{{ getIcon(notification.type) }}</span>
          <span class="text-sm">{{ notification.message }}</span>
        </div>
        <button
          @click="removeNotification(notification.id)"
          class="ml-4 text-white hover:opacity-75 flex-shrink-0"
        >
          ✕
        </button>
      </div>
    </TransitionGroup>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useNotificationStore } from '@/stores/notificationStore'

const notificationStore = useNotificationStore()
const notifications = computed(() => notificationStore.notifications)

const removeNotification = (id) => {
  notificationStore.removeNotification(id)
}

const getNotificationClass = (type) => {
  const classes = {
    success: 'bg-green-500 border border-green-600',
    error: 'bg-red-500 border border-red-600',
    warning: 'bg-yellow-500 border border-yellow-600',
    info: 'bg-blue-500 border border-blue-600'
  }
  return classes[type] || 'bg-gray-500'
}

const getIcon = (type) => {
  const icons = {
    success: '✓',
    error: '✕',
    warning: '⚠',
    info: 'ℹ'
  }
  return icons[type] || '•'
}
</script>

<style scoped>
.toast-enter-active,
.toast-leave-active {
  transition: all 0.3s ease;
}

.toast-enter-from,
.toast-leave-to {
  transform: translateX(400px);
  opacity: 0;
}

.toast-move {
  transition: transform 0.3s ease;
}
</style>
```

---

## 3. EMPTY STATE COMPONENT

### EmptyState.vue
```vue
<template>
  <div class="flex flex-col items-center justify-center py-12 text-center">
    <div class="text-6xl mb-4">{{ icon }}</div>
    <h3 class="text-xl font-semibold text-gray-900 mb-2">{{ title }}</h3>
    <p class="text-gray-600 mb-6 max-w-sm">{{ description }}</p>
    <button 
      v-if="showAction"
      @click="$emit('action')"
      class="px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition"
    >
      {{ actionText }}
    </button>
  </div>
</template>

<script setup>
defineProps({
  icon: { type: String, default: '📭' },
  title: { type: String, default: 'No Data' },
  description: { type: String, default: 'No items found' },
  showAction: { type: Boolean, default: false },
  actionText: { type: String, default: 'Create' }
})

defineEmits(['action'])
</script>
```

---

## 4. ERROR STATE COMPONENT

### ErrorState.vue
```vue
<template>
  <div class="flex flex-col items-center justify-center py-12 text-center">
    <div class="text-6xl mb-4">⚠️</div>
    <h3 class="text-xl font-semibold text-red-900 mb-2">{{ title }}</h3>
    <p class="text-red-700 mb-6 max-w-sm">{{ message }}</p>
    <button 
      @click="$emit('retry')"
      class="px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition"
    >
      Retry
    </button>
  </div>
</template>

<script setup>
defineProps({
  title: { type: String, default: 'Error' },
  message: { type: String, default: 'An error occurred' }
})

defineEmits(['retry'])
</script>
```

---

## 5. SKELETON LOADER COMPONENTS

### SkeletonLoader.vue (Generic List)
```vue
<template>
  <div class="animate-pulse space-y-4">
    <div v-for="i in count" :key="i" class="bg-white p-4 rounded-lg border border-gray-200">
      <div class="space-y-3">
        <div class="h-4 bg-gray-300 rounded w-3/4"></div>
        <div class="h-4 bg-gray-300 rounded w-1/2"></div>
        <div class="h-3 bg-gray-200 rounded w-full"></div>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  count: { type: Number, default: 3 }
})
</script>
```

### KPICardSkeleton.vue
```vue
<template>
  <div class="bg-white p-6 rounded-lg border border-gray-200 animate-pulse">
    <div class="h-4 bg-gray-300 rounded w-1/2 mb-4"></div>
    <div class="h-10 bg-gray-300 rounded w-2/3 mb-2"></div>
    <div class="h-3 bg-gray-200 rounded w-1/4"></div>
  </div>
</template>
```

### TableSkeleton.vue
```vue
<template>
  <div class="space-y-3 bg-white rounded-lg border border-gray-200 p-4">
    <div v-for="i in rows" :key="i" class="flex gap-4 animate-pulse">
      <div v-for="j in columns" :key="j" class="h-4 bg-gray-300 rounded flex-1"></div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  rows: { type: Number, default: 5 },
  columns: { type: Number, default: 4 }
})
</script>
```

---

## 6. LOADING SPINNER

### LoadingSpinner.vue
```vue
<template>
  <div class="flex items-center justify-center gap-3">
    <div class="relative w-8 h-8">
      <div class="absolute inset-0 border-4 border-gray-200 rounded-full"></div>
      <div class="absolute inset-0 border-4 border-blue-500 border-r-transparent rounded-full animate-spin"></div>
    </div>
    <p v-if="text" class="text-gray-600">{{ text }}</p>
  </div>
</template>

<script setup>
defineProps({
  text: String
})
</script>
```

---

## 7. USAGE IN COMPONENTS

### Example: TransformateurDetail.vue Enhancement
```javascript
import { useNotificationStore } from '@/stores/notificationStore'

const notificationStore = useNotificationStore()

async function saveTransformateur() {
  try {
    loading.value = true
    await transformateursAPI.update(id, data)
    notificationStore.showSuccess('Transformateur updated successfully!')
  } catch (error) {
    notificationStore.showError('Failed to update: ' + error.message)
  } finally {
    loading.value = false
  }
}

async function deleteTransformateur(id) {
  try {
    await transformateursAPI.delete(id)
    notificationStore.showSuccess('Transformateur deleted successfully')
    router.push('/transformateurs')
  } catch (error) {
    notificationStore.showError('Cannot delete: ' + error.message)
  }
}

// In template:
// <template>
//   <div v-if="loading" class="flex justify-center">
//     <LoadingSpinner text="Saving..." />
//   </div>
//   <div v-else-if="error">
//     <ErrorState :message="error" @retry="loadData" />
//   </div>
//   <div v-else-if="!transformateur">
//     <EmptyState title="Not Found" />
//   </div>
//   <div v-else>
//     <!-- Content -->
//   </div>
// </template>
```

---

## 8. ENHANCED DATA LOADER

### Updated DataLoader.vue
```vue
<template>
  <div>
    <!-- Loading State with Skeleton -->
    <div v-if="loading">
      <slot name="skeleton">
        <SkeletonLoader :count="3" />
      </slot>
    </div>

    <!-- Error State -->
    <div v-else-if="error">
      <slot name="error">
        <ErrorState :message="error" @retry="$emit('retry')" />
      </slot>
    </div>

    <!-- Empty State -->
    <div v-else-if="isEmpty">
      <slot name="empty">
        <EmptyState :title="emptyTitle" :description="emptyDescription" />
      </slot>
    </div>

    <!-- Main Content -->
    <div v-else>
      <slot></slot>
    </div>
  </div>
</template>

<script setup>
defineProps({
  loading: { type: Boolean, default: false },
  error: { type: String, default: null },
  isEmpty: { type: Boolean, default: false },
  emptyTitle: { type: String, default: 'No Data' },
  emptyDescription: { type: String, default: 'No items found' }
})

defineEmits(['retry'])
</script>
```

---

## 9. API CALL ERROR HANDLING

### Enhanced error-handler.js
```javascript
import { useNotificationStore } from '@/stores/notificationStore'

export const handleApiError = (error) => {
  const notificationStore = useNotificationStore()

  if (error.response) {
    const status = error.response.status
    const data = error.response.data

    switch (status) {
      case 400:
        notificationStore.showError('Invalid request: ' + (data.message || 'Check your input'))
        break
      case 401:
        notificationStore.showError('Session expired. Please login again')
        break
      case 403:
        notificationStore.showError('Access denied. You do not have permission')
        break
      case 404:
        notificationStore.showError('Resource not found')
        break
      case 409:
        notificationStore.showError('Conflict: ' + (data.message || 'Item already exists'))
        break
      case 500:
        notificationStore.showError('Server error. Please try again later')
        break
      default:
        notificationStore.showError('Error: ' + (data.message || 'Unknown error'))
    }
  } else if (error.request) {
    notificationStore.showError('Network error. Check your connection')
  } else {
    notificationStore.showError('Error: ' + error.message)
  }

  console.error('API Error:', error)
}
```

---

## 10. APP SETUP

### In main.js
```javascript
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import Toast from '@/components/Toast.vue'

const app = createApp(App)

app.use(createPinia())
app.component('Toast', Toast)
app.mount('#app')
```

### In App.vue
```vue
<template>
  <div>
    <Toast />
    <RouterView />
  </div>
</template>
```

---

## 11. IMPLEMENTATION CHECKLIST

### Components to Create
- [ ] NotificationStore.js (Pinia store)
- [ ] Toast.vue (Notification display)
- [ ] EmptyState.vue (Empty data state)
- [ ] ErrorState.vue (Error state)
- [ ] SkeletonLoader.vue (Generic skeleton)
- [ ] KPICardSkeleton.vue (KPI skeleton)
- [ ] TableSkeleton.vue (Table skeleton)
- [ ] LoadingSpinner.vue (Loading indicator)
- [ ] Enhanced DataLoader.vue
- [ ] Enhanced error-handler.js

### Components to Update
- [ ] TransformateurDetail.vue - Add notifications
- [ ] ModernAdminDashboard.vue - Add skeletons
- [ ] SupervisorDashboard.vue - Add skeletons
- [ ] AgentDashboard.vue - Add skeletons
- [ ] ServiceClientDashboard.vue - Add skeletons
- [ ] TransformateursManagementNew.vue - Add notifications
- [ ] PostesManagementNew.vue - Add notifications
- [ ] UsersManagementNew.vue - Add notifications
- [ ] InterventionsManagement.vue - Add notifications
- [ ] ComplaintsManagement.vue - Add notifications
- [ ] AlertsManagement.vue - Add notifications

### Features to Implement
- [ ] Success notifications for create/update/delete
- [ ] Error notifications for failed operations
- [ ] Loading skeletons for all data-loading states
- [ ] Empty states for no-data scenarios
- [ ] Error boundaries for component failures
- [ ] Loading spinners for async operations

---

## 12. BEST PRACTICES

### Notification Usage
```javascript
// Good: Specific message
notificationStore.showSuccess('User created successfully')

// Avoid: Generic message
notificationStore.showSuccess('Done')

// Good: Include error details
notificationStore.showError('Failed to save: Email already exists')

// Avoid: Suppress error details
notificationStore.showError('Error occurred')
```

### Loading States
```javascript
const loading = ref(false)

async function fetchData() {
  try {
    loading.value = true
    const data = await api.getData()
    return data
  } finally {
    loading.value = false
  }
}
```

### Error Handling
```javascript
try {
  await saveData()
  notificationStore.showSuccess('Saved!')
} catch (error) {
  handleApiError(error) // Already shows notification
  console.error('Save failed:', error)
}
```

---

## 13. TESTING CHECKLIST

- [ ] Toast notifications appear and auto-dismiss
- [ ] Manual close button works on notifications
- [ ] Empty states display when no data
- [ ] Skeletons show during loading
- [ ] Error states show with retry button
- [ ] Error messages are descriptive
- [ ] Success notifications appear after actions
- [ ] Spinners animate smoothly
- [ ] Multiple notifications stack properly
- [ ] Notifications don't block interaction

---

## Summary

🟢 **P6 UX/UI POLISH - READY FOR IMPLEMENTATION**

**Components to Create**: 8 new components
**Components to Update**: 10 existing components
**Features**: Success/error notifications, loading skeletons, empty states, error boundaries

**Ready for**: Integration into all dashboards and management pages, then security verification.

