<template>
  <div class="flex flex-col items-center justify-center p-6 bg-gradient-to-br from-blue-50 to-indigo-50 rounded-lg border border-blue-100">
    <!-- Gauge SVG -->
    <svg viewBox="0 0 200 120" class="w-32 h-20 mb-4">
      <!-- Background arc -->
      <path
        d="M 30 100 A 70 70 0 0 1 170 100"
        stroke="#e5e7eb"
        stroke-width="8"
        fill="none"
        stroke-linecap="round"
      />

      <!-- Colored arcs -->
      <!-- Red (0-20) -->
      <path
        d="M 30 100 A 70 70 0 0 1 47 46.5"
        :stroke="`rgba(239, 68, 68, ${healthScore < 20 ? 1 : 0.3})`"
        stroke-width="8"
        fill="none"
        stroke-linecap="round"
      />
      <!-- Orange (20-50) -->
      <path
        d="M 47 46.5 A 70 70 0 0 1 100 30"
        :stroke="`rgba(249, 115, 22, ${healthScore >= 20 && healthScore < 50 ? 1 : 0.3})`"
        stroke-width="8"
        fill="none"
        stroke-linecap="round"
      />
      <!-- Yellow (50-75) -->
      <path
        d="M 100 30 A 70 70 0 0 1 153 46.5"
        :stroke="`rgba(234, 179, 8, ${healthScore >= 50 && healthScore < 75 ? 1 : 0.3})`"
        stroke-width="8"
        fill="none"
        stroke-linecap="round"
      />
      <!-- Green (75-100) -->
      <path
        d="M 153 46.5 A 70 70 0 0 1 170 100"
        :stroke="`rgba(34, 197, 94, ${healthScore >= 75 ? 1 : 0.3})`"
        stroke-width="8"
        fill="none"
        stroke-linecap="round"
      />

      <!-- Needle -->
      <line
        x1="100"
        y1="100"
        :x2="`${100 + 70 * Math.cos((needleAngle - 90) * Math.PI / 180)}`"
        :y2="`${100 + 70 * Math.sin((needleAngle - 90) * Math.PI / 180)}`"
        stroke="#1f2937"
        stroke-width="3"
        stroke-linecap="round"
      />

      <!-- Center circle -->
      <circle cx="100" cy="100" r="6" fill="#1f2937" />
    </svg>

    <!-- Score value -->
    <div class="text-center">
      <div class="text-4xl font-bold" :class="scoreColor">
        {{ healthScore }}
      </div>
      <div class="text-sm text-gray-600 mt-1">Health Score</div>
      <div class="text-xs text-gray-500 mt-2">
        {{ scoreLabel }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  healthScore: {
    type: Number,
    required: true,
    validator: (v) => v >= 0 && v <= 100,
  },
})

const needleAngle = computed(() => {
  // Map 0-100 to 0-180 degrees (left to right in gauge)
  return (props.healthScore / 100) * 180
})

const scoreColor = computed(() => {
  if (props.healthScore >= 75) return 'text-green-600'
  if (props.healthScore >= 50) return 'text-yellow-600'
  if (props.healthScore >= 20) return 'text-orange-600'
  return 'text-red-600'
})

const scoreLabel = computed(() => {
  if (props.healthScore >= 75) return 'Excellent'
  if (props.healthScore >= 50) return 'Bon'
  if (props.healthScore >= 20) return 'Dégradé'
  return 'Critique'
})
</script>
