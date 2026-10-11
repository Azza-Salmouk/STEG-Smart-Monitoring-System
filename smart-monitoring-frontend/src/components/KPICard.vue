<template>
  <Card hoverable>
    <div class="flex items-start justify-between">
      <div>
        <p class="text-sm text-gray-600 mb-2">{{ title }}</p>
        <div class="flex items-baseline gap-2">
          <h3 class="text-3xl font-bold text-gray-900">{{ value }}</h3>
          <span v-if="trend" :class="['text-sm font-medium', trend.startsWith('+') ? 'text-green-600' : 'text-red-600']">
            {{ trend }}
          </span>
        </div>
      </div>
      <div :class="[color, 'w-12 h-12 rounded-lg flex items-center justify-center']">
        <svg class="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z" />
        </svg>
      </div>
    </div>
  </Card>
</template>

<script setup>
import Card from './Card.vue'

defineProps({
  title: {
    type: String,
    required: true,
  },
  value: {
    type: [String, Number],
    required: true,
  },
  color: {
    type: String,
    default: 'bg-primary-600',
  },
  icon: {
    type: String,
    default: null,
  },
  trend: {
    type: String,
    default: null,
  },
})
</script>

<style scoped></style>
