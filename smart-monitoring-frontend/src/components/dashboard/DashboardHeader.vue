<template>
  <div class="flex items-center justify-between">
    <div>
      <h1 class="text-3xl font-bold text-gray-900">{{ title }}</h1>
      <p class="text-sm text-gray-500 mt-1" v-if="subtitle">{{ subtitle }}</p>
      <p class="text-xs text-gray-400 mt-2" v-if="showLastUpdate && lastUpdate">
        Dernière actualisation : {{ formatTimeAgo(lastUpdate) }}
      </p>
    </div>
    <button
      @click="$emit('refresh')"
      :disabled="loading"
      class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 disabled:bg-gray-400 transition font-medium"
    >
      {{ loading ? 'Chargement...' : '🔄 Actualiser' }}
    </button>
  </div>
</template>

<script setup>
defineProps({
  title: {
    type: String,
    required: true,
  },
  subtitle: {
    type: String,
    default: null,
  },
  loading: {
    type: Boolean,
    default: false,
  },
  lastUpdate: {
    type: [Date, String],
    default: null,
  },
  showLastUpdate: {
    type: Boolean,
    default: true,
  },
})

defineEmits(['refresh'])

function formatTimeAgo(date) {
  if (!date) return ''
  const now = new Date()
  const diffMs = now - new Date(date)
  const diffMins = Math.floor(diffMs / 60000)

  if (diffMins < 1) return 'À l\'instant'
  if (diffMins < 60) return `Il y a ${diffMins}m`

  const diffHours = Math.floor(diffMins / 60)
  if (diffHours < 24) return `Il y a ${diffHours}h`

  const diffDays = Math.floor(diffHours / 24)
  return `Il y a ${diffDays}j`
}
</script>
