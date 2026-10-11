<template>
  <div class="bg-white rounded-lg shadow p-6 border-l-4 transition-shadow hover:shadow-lg" :class="borderColorClass">
    <div class="flex items-center justify-between">
      <div class="flex-1">
        <p class="text-sm text-gray-600 font-medium">{{ label }}</p>
        <p class="text-3xl font-bold text-gray-900 mt-2">{{ value }}</p>
        <p v-if="description" class="text-xs text-gray-500 mt-2">{{ description }}</p>
      </div>
      <div class="text-4xl ml-4" :class="iconClass">{{ icon }}</div>
    </div>
    <div v-if="showProgress && typeof value === 'number'" class="mt-4 h-2 bg-gray-200 rounded-full overflow-hidden">
      <div
        class="h-full transition-all"
        :class="progressBarClass"
        :style="{ width: Math.min(value, 100) + '%' }"
      ></div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  label: {
    type: String,
    required: true,
  },
  value: {
    type: [String, Number],
    required: true,
  },
  icon: {
    type: String,
    default: '📊',
  },
  type: {
    type: String,
    enum: ['primary', 'success', 'warning', 'danger', 'info'],
    default: 'primary',
  },
  description: {
    type: String,
    default: null,
  },
  showProgress: {
    type: Boolean,
    default: false,
  },
})

const borderColorClass = computed(() => {
  const map = {
    primary: 'border-blue-500',
    success: 'border-green-500',
    warning: 'border-yellow-500',
    danger: 'border-red-500',
    info: 'border-purple-500',
  }
  return map[props.type] || 'border-blue-500'
})

const iconClass = computed(() => {
  const map = {
    primary: 'text-blue-200',
    success: 'text-green-200',
    warning: 'text-yellow-200',
    danger: 'text-red-200',
    info: 'text-purple-200',
  }
  return map[props.type] || 'text-blue-200'
})

const progressBarClass = computed(() => {
  const map = {
    primary: 'bg-blue-500',
    success: 'bg-green-500',
    warning: 'bg-yellow-500',
    danger: 'bg-red-500',
    info: 'bg-purple-500',
  }
  return map[props.type] || 'bg-blue-500'
})
</script>
