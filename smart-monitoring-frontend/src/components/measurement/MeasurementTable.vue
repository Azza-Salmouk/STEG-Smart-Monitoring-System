<template>
  <div class="bg-white rounded-lg border border-gray-200 overflow-hidden">
    <!-- Table -->
    <div class="overflow-x-auto">
      <table class="w-full">
        <thead class="bg-gray-50 border-b border-gray-200">
          <tr>
            <th class="px-6 py-3 text-left text-sm font-semibold text-gray-900">
              Transformateur
            </th>
            <th class="px-6 py-3 text-left text-sm font-semibold text-gray-900">
              Date
            </th>
            <th class="px-6 py-3 text-left text-sm font-semibold text-gray-900">
              Température
            </th>
            <th class="px-6 py-3 text-left text-sm font-semibold text-gray-900">
              Tension
            </th>
            <th class="px-6 py-3 text-left text-sm font-semibold text-gray-900">
              Courant
            </th>
            <th class="px-6 py-3 text-left text-sm font-semibold text-gray-900">
              Health Score
            </th>
            <th class="px-6 py-3 text-left text-sm font-semibold text-gray-900">
              Statut
            </th>
            <th class="px-6 py-3 text-center text-sm font-semibold text-gray-900">
              Actions
            </th>
          </tr>
        </thead>
        <tbody class="divide-y divide-gray-200">
          <tr v-for="measurement in measurements" :key="measurement.id" class="hover:bg-gray-50">
            <td class="px-6 py-4 text-sm font-medium text-gray-900">
              {{ measurement.transformateurReference }}
            </td>
            <td class="px-6 py-4 text-sm text-gray-600">
              {{ formatDate(measurement.measurementDate) }}
            </td>
            <td class="px-6 py-4 text-sm text-gray-900">
              <span :class="getTemperatureClass(measurement.temperature)">
                {{ measurement.temperature }}°C
              </span>
            </td>
            <td class="px-6 py-4 text-sm text-gray-900">
              {{ (measurement.voltage / 1000).toFixed(1) }}kV
            </td>
            <td class="px-6 py-4 text-sm text-gray-900">
              {{ measurement.current.toFixed(1) }}A
            </td>
            <td class="px-6 py-4">
              <div class="flex items-center gap-2">
                <div class="w-16 bg-gray-200 rounded-full h-2">
                  <div
                    :style="{ width: `${measurement.healthScore}%` }"
                    :class="getHealthScoreBar(measurement.healthScore)"
                    class="h-2 rounded-full"
                  ></div>
                </div>
                <span class="text-sm font-medium text-gray-900">
                  {{ measurement.healthScore }}%
                </span>
              </div>
            </td>
            <td class="px-6 py-4">
              <MeasurementStatusBadge :status="measurement.status" />
            </td>
            <td class="px-6 py-4 text-center">
              <div class="flex justify-center gap-2">
                <button
                  @click="$emit('view', measurement.id)"
                  class="text-blue-600 hover:text-blue-800 text-sm font-medium"
                >
                  Voir
                </button>
                <button
                  v-if="canEdit"
                  @click="$emit('edit', measurement.id)"
                  class="text-gray-600 hover:text-gray-800 text-sm font-medium"
                >
                  Modifier
                </button>
                <button
                  v-if="canDelete"
                  @click="$emit('delete', measurement.id)"
                  class="text-red-600 hover:text-red-800 text-sm font-medium"
                >
                  Supprimer
                </button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Empty state -->
    <div v-if="measurements.length === 0" class="text-center py-12">
      <p class="text-gray-500">Aucune mesure trouvée</p>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useAuthStore } from '@/stores/authStore'
import MeasurementStatusBadge from './MeasurementStatusBadge.vue'

const authStore = useAuthStore()

defineProps({
  measurements: {
    type: Array,
    required: true,
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
  return new Date(date).toLocaleString('fr-FR', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
  })
}

const getTemperatureClass = (temp) => {
  if (temp >= 85) return 'text-red-600 font-semibold'
  if (temp >= 70) return 'text-orange-600 font-semibold'
  return 'text-gray-900'
}

const getHealthScoreBar = (score) => {
  if (score >= 75) return 'bg-green-500'
  if (score >= 50) return 'bg-yellow-500'
  if (score >= 20) return 'bg-orange-500'
  return 'bg-red-500'
}
</script>
