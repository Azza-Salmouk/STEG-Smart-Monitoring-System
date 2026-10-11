<template>
  <!-- Loading State -->
  <div v-if="loading" class="flex items-center justify-center h-64">
    <div class="text-gray-500 text-center">
      <div class="animate-spin inline-block w-8 h-8 border-4 border-blue-500 border-r-transparent rounded-full"></div>
      <p class="mt-2">{{ loadingMessage }}</p>
    </div>
  </div>

  <!-- Error State -->
  <div v-else-if="error" class="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg">
    <p class="font-semibold">{{ errorTitle }}</p>
    <p class="text-sm">{{ error }}</p>
  </div>

  <!-- Empty State -->
  <div v-else-if="empty" class="flex flex-col items-center justify-center h-64">
    <div class="text-6xl mb-4">{{ emptyIcon }}</div>
    <p class="text-gray-500 font-medium">{{ emptyMessage }}</p>
  </div>

  <!-- Content Slot -->
  <template v-else>
    <slot />
  </template>
</template>

<script setup>
defineProps({
  loading: {
    type: Boolean,
    default: false,
  },
  loadingMessage: {
    type: String,
    default: 'Chargement des données...',
  },
  error: {
    type: String,
    default: null,
  },
  errorTitle: {
    type: String,
    default: 'Erreur de chargement',
  },
  empty: {
    type: Boolean,
    default: false,
  },
  emptyMessage: {
    type: String,
    default: 'Aucune donnée disponible',
  },
  emptyIcon: {
    type: String,
    default: '📭',
  },
})
</script>

<style scoped>
.animate-spin {
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}
</style>
