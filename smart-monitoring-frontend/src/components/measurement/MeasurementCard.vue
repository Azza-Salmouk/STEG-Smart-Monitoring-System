<template>
  <div class="bg-white rounded-lg border border-gray-200 p-6 hover:shadow-lg transition-shadow">
    <!-- Header -->
    <div class="flex items-start justify-between mb-4">
      <div>
        <h3 class="text-lg font-semibold text-gray-900">
          {{ measurement.transformateurReference }}
        </h3>
        <p class="text-sm text-gray-500 mt-1">
          {{ formatDate(measurement.measurementDate) }}
        </p>
      </div>
      <MeasurementStatusBadge :status="measurement.status" />
    </div>

    <!-- Grid de paramètres -->
    <div class="grid grid-cols-2 gap-4 mb-4">
      <div class="flex items-center gap-2">
        <span class="text-sm text-gray-600">Température:</span>
        <span class="font-semibold text-gray-900">{{ measurement.temperature }}°C</span>
      </div>
      <div class="flex items-center gap-2">
        <span class="text-sm text-gray-600">Tension:</span>
        <span class="font-semibold text-gray-900">{{ (measurement.voltage / 1000).toFixed(1) }}kV</span>
      </div>
      <div class="flex items-center gap-2">
        <span class="text-sm text-gray-600">Courant:</span>
        <span class="font-semibold text-gray-900">{{ measurement.current.toFixed(1) }}A</span>
      </div>
      <div class="flex items-center gap-2">
        <span class="text-sm text-gray-600">Puissance:</span>
        <span class="font-semibold text-gray-900">{{ measurement.power.toFixed(1) }}kW</span>
      </div>
    </div>

    <!-- Health Score bar -->
    <div class="mb-4">
      <div class="flex items-center justify-between mb-2">
        <span class="text-sm font-medium text-gray-700">Health Score</span>
        <span :class="getHealthScoreColor(measurement.healthScore)" class="font-semibold">
          {{ measurement.healthScore }}%
        </span>
      </div>
      <div class="w-full bg-gray-200 rounded-full h-2">
        <div
          :style="{ width: `${measurement.healthScore}%` }"
          :class="getHealthScoreBar(measurement.healthScore)"
          class="h-2 rounded-full transition-all duration-300"
        ></div>
      </div>
    </div>

    <!-- Actions -->
    <div v-if="showActions" class="flex gap-2 pt-4 border-t border-gray-100">
      <button
        @click="$emit('view')"
        class="flex-1 px-3 py-2 text-sm font-medium text-blue-600 bg-blue-50 rounded hover:bg-blue-100 transition-colors"
      >
        Détails
      </button>
      <button
        v-if="canEdit"
        @click="$emit('edit')"
        class="flex-1 px-3 py-2 text-sm font-medium text-gray-600 bg-gray-100 rounded hover:bg-gray-200 transition-colors"
      >
        Modifier
      </button>
      <button
        v-if="canDelete"
        @click="$emit('delete')"
        class="flex-1 px-3 py-2 text-sm font-medium text-red-600 bg-red-50 rounded hover:bg-red-100 transition-colors"
      >
        Supprimer
      </button>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useAuthStore } from '@/stores/authStore'
import MeasurementStatusBadge from './MeasurementStatusBadge.vue'

const authStore = useAuthStore()

const props = defineProps({
  measurement: {
    type: Object,
    required: true,
  },
  showActions: {
    type: Boolean,
    default: true,
  },
})

defineEmits(['view', 'edit', 'delete'])

const canEdit = computed(() => {
  return ['ADMIN', 'SUPERVISOR'].includes(authStore.role)
})

const canDelete = computed(() => {
  return authStore.role === 'ADMIN'
})

const formatDate = (date) => {
  return new Date(date).toLocaleString('fr-FR')
}

const getHealthScoreColor = (score) => {
  if (score >= 75) return 'text-green-600'
  if (score >= 50) return 'text-yellow-600'
  if (score >= 20) return 'text-orange-600'
  return 'text-red-600'
}

const getHealthScoreBar = (score) => {
  if (score >= 75) return 'bg-green-500'
  if (score >= 50) return 'bg-yellow-500'
  if (score >= 20) return 'bg-orange-500'
  return 'bg-red-500'
}
</script>
