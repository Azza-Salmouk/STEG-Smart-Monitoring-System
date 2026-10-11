<template>
  <div class="bg-white rounded-lg border border-gray-200 p-4">
    <!-- Header -->
    <div class="flex items-center gap-2 mb-3">
      <component :is="icon" :size="20" :class="iconColor" />
      <h3 class="font-semibold text-gray-900">{{ title }}</h3>
    </div>

    <!-- Value -->
    <div class="mb-3">
      <div class="text-3xl font-bold text-gray-900">
        {{ value }}
      </div>
      <div class="text-xs text-gray-500 mt-1">{{ unit }}</div>
    </div>

    <!-- Progress bar -->
    <div v-if="showProgress" class="mb-3">
      <div class="w-full bg-gray-200 rounded-full h-2">
        <div
          :style="{ width: `${progressPercent}%` }"
          :class="progressColor"
          class="h-2 rounded-full transition-all duration-300"
        ></div>
      </div>
    </div>

    <!-- Status -->
    <div :class="statusClass" class="text-xs font-medium py-1 px-2 rounded text-center">
      {{ statusLabel }}
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { Thermometer, Zap, Wind, Lightbulb } from 'lucide-vue-next'

const props = defineProps({
  title: {
    type: String,
    required: true,
  },
  value: {
    type: [Number, String],
    required: true,
  },
  unit: {
    type: String,
    required: true,
  },
  type: {
    type: String,
    required: true,
    validator: (v) => ['temperature', 'voltage', 'current', 'power'].includes(v),
  },
  min: {
    type: Number,
    default: 0,
  },
  max: {
    type: Number,
    default: 100,
  },
  warningThreshold: {
    type: Number,
    default: 75,
  },
  criticalThreshold: {
    type: Number,
    default: 90,
  },
  showProgress: {
    type: Boolean,
    default: true,
  },
})

const icon = computed(() => {
  switch (props.type) {
    case 'temperature':
      return Thermometer
    case 'voltage':
      return Zap
    case 'current':
      return Wind
    case 'power':
      return Lightbulb
    default:
      return Lightbulb
  }
})

const iconColor = computed(() => {
  switch (props.type) {
    case 'temperature':
      return 'text-red-500'
    case 'voltage':
      return 'text-yellow-500'
    case 'current':
      return 'text-blue-500'
    case 'power':
      return 'text-orange-500'
    default:
      return 'text-gray-500'
  }
})

const progressPercent = computed(() => {
  if (props.max === props.min) return 0
  const percent = ((props.value - props.min) / (props.max - props.min)) * 100
  return Math.min(100, Math.max(0, percent))
})

const progressColor = computed(() => {
  if (props.value >= props.criticalThreshold) return 'bg-red-500'
  if (props.value >= props.warningThreshold) return 'bg-yellow-500'
  return 'bg-green-500'
})

const statusLabel = computed(() => {
  if (props.value >= props.criticalThreshold) return 'Critique'
  if (props.value >= props.warningThreshold) return 'Avertissement'
  return 'Normale'
})

const statusClass = computed(() => {
  if (props.value >= props.criticalThreshold) return 'bg-red-100 text-red-800'
  if (props.value >= props.warningThreshold) return 'bg-yellow-100 text-yellow-800'
  return 'bg-green-100 text-green-800'
})
</script>
