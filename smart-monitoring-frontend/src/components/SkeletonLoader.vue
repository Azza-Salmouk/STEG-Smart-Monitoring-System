<template>
  <div :class="['bg-gray-200 animate-pulse rounded', `${width} ${height}`]"></div>
</template>

<script setup>
defineProps({
  width: {
    type: String,
    default: 'w-full',
  },
  height: {
    type: String,
    default: 'h-4',
  },
})
</script>

<style scoped>
@keyframes pulse {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: 0.5;
  }
}

.animate-pulse {
  animation: pulse 2s cubic-bezier(0.4, 0, 0.6, 1) infinite;
}
</style>
