<template>
  <div class="bg-white rounded-lg shadow p-6 transition-shadow hover:shadow-lg">
    <h2 v-if="title" class="text-lg font-semibold text-gray-900 mb-4">{{ title }}</h2>
    <slot />
  </div>
</template>

<script setup>
defineProps({
  title: {
    type: String,
    default: null,
  },
})
</script>
