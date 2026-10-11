<template>
  <div class="flex flex-col items-center justify-center py-16 px-4">
    <!-- Icon -->
    <div v-if="icon" class="mb-4">
      <component :is="icon" class="w-16 h-16 text-gray-300" />
    </div>

    <!-- Title -->
    <h3 class="text-lg font-semibold text-gray-900 mb-2">{{ title }}</h3>

    <!-- Description -->
    <p v-if="description" class="text-gray-500 text-center max-w-md mb-6">
      {{ description }}
    </p>

    <!-- Action Button -->
    <button
      v-if="actionLabel"
      @click="$emit('action')"
      class="px-4 py-2 bg-primary-600 hover:bg-primary-700 text-white rounded-lg transition-colors"
    >
      {{ actionLabel }}
    </button>
  </div>
</template>

<script setup>
defineProps({
  title: {
    type: String,
    required: true,
  },
  description: {
    type: String,
    default: null,
  },
  icon: {
    type: String,
    default: null,
  },
  actionLabel: {
    type: String,
    default: null,
  },
})

defineEmits(['action'])
</script>

<style scoped></style>
