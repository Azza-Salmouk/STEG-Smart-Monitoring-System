<template>
  <div v-if="show" class="flex items-center justify-center p-4">
    <div class="relative w-12 h-12">
      <!-- Spinner animé -->
      <div class="absolute inset-0 border-4 border-gray-200 rounded-full"></div>
      <div class="absolute inset-0 border-4 border-transparent border-t-primary-600 rounded-full animate-spin"></div>
    </div>
    <span v-if="text" class="ml-3 text-gray-600 font-medium">{{ text }}</span>
  </div>
</template>

<script setup>
defineProps({
  show: {
    type: Boolean,
    default: true
  },
  text: {
    type: String,
    default: 'Chargement...'
  }
})
</script>

<style scoped>
@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.animate-spin {
  animation: spin 1s linear infinite;
}
</style>
