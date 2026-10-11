<template>
  <div>
    <p class="text-sm text-gray-600 font-medium">{{ label }}</p>
    <p class="text-lg text-gray-900 font-semibold mt-1">{{ value }}</p>
  </div>
</template>

<script setup>
defineProps({
  label: {
    type: String,
    required: true
  },
  value: {
    type: [String, Number],
    required: true
  }
})
</script>

<style scoped>
</style>
