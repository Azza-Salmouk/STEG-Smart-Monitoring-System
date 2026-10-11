<template>
  <div class="bg-white rounded-lg shadow p-6 border-l-4" :class="borderColor">
    <div class="flex items-center justify-between">
      <div>
        <p class="text-gray-600 text-sm font-medium">{{ title }}</p>
        <p class="text-3xl font-bold text-gray-900 mt-2">{{ value }}</p>
      </div>
      <div :class="iconBgColor" class="p-3 rounded-lg">
        <svg class="w-6 h-6 text-white" fill="currentColor" viewBox="0 0 20 20">
          <path v-if="icon === 'users'" d="M9 6a3 3 0 11-6 0 3 3 0 016 0zM9 10a3 3 0 11-6 0 3 3 0 016 0zm6 0a3 3 0 11-6 0 3 3 0 016 0zm3-5a3 3 0 11-6 0 3 3 0 016 0zm0 10a1 1 0 100-2 1 1 0 000 2z"/>
          <path v-else-if="icon === 'chart'" d="M2 11a1 1 0 011-1h2a1 1 0 011 1v5a1 1 0 01-1 1H3a1 1 0 01-1-1v-5zM8 7a1 1 0 011-1h2a1 1 0 011 1v9a1 1 0 01-1 1H9a1 1 0 01-1-1V7zM14 4a1 1 0 011-1h2a1 1 0 011 1v12a1 1 0 01-1 1h-2a1 1 0 01-1-1V4z"/>
          <path v-else-if="icon === 'database'" d="M3 12a9 9 0 0118 0v-6a9 9 0 00-18 0v6z"/>
        </svg>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  title: {
    type: String,
    required: true
  },
  value: {
    type: String,
    required: true
  },
  icon: {
    type: String,
    default: 'chart'
  },
  color: {
    type: String,
    default: 'blue'
  }
})

const borderColor = computed(() => {
  const colors = {
    'blue': 'border-blue-500',
    'green': 'border-green-500',
    'purple': 'border-purple-500',
    'red': 'border-red-500'
  }
  return colors[props.color] || colors.blue
})

const iconBgColor = computed(() => {
  const colors = {
    'blue': 'bg-blue-500',
    'green': 'bg-green-500',
    'purple': 'bg-purple-500',
    'red': 'bg-red-500'
  }
  return colors[props.color] || colors.blue
})
</script>

<style scoped>
</style>
