<template>
  <div class="bg-white rounded-lg border border-gray-200 p-4">
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
      <!-- Transformateur -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">
          Transformateur
        </label>
        <input
          v-model="filters.transformateurId"
          type="number"
          placeholder="ID du transformateur"
          class="w-full px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
      </div>

      <!-- Statut -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">
          Statut
        </label>
        <select
          v-model="filters.status"
          class="w-full px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
        >
          <option value="">-- Tous --</option>
          <option value="NORMAL">Normale</option>
          <option value="WARNING">Avertissement</option>
          <option value="CRITICAL">Critique</option>
          <option value="OFFLINE">Hors ligne</option>
        </select>
      </div>

      <!-- Température min -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">
          Temp. min (°C)
        </label>
        <input
          v-model.number="filters.minTemp"
          type="number"
          placeholder="Min"
          class="w-full px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
      </div>

      <!-- Température max -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">
          Temp. max (°C)
        </label>
        <input
          v-model.number="filters.maxTemp"
          type="number"
          placeholder="Max"
          class="w-full px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
      </div>

      <!-- Health Score max -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">
          Health Score max
        </label>
        <input
          v-model.number="filters.maxHealthScore"
          type="number"
          placeholder="Max"
          min="0"
          max="100"
          class="w-full px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
      </div>

      <!-- Courant min -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">
          Courant min (A)
        </label>
        <input
          v-model.number="filters.minCurrent"
          type="number"
          placeholder="Min"
          class="w-full px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
      </div>

      <!-- Date début -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">
          Date début
        </label>
        <input
          v-model="filters.startDate"
          type="datetime-local"
          class="w-full px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
      </div>

      <!-- Date fin -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">
          Date fin
        </label>
        <input
          v-model="filters.endDate"
          type="datetime-local"
          class="w-full px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
      </div>
    </div>

    <!-- Buttons -->
    <div class="flex gap-2 mt-4 justify-end">
      <button
        @click="resetFilters"
        class="px-4 py-2 text-sm font-medium text-gray-700 bg-gray-100 border border-gray-300 rounded-lg hover:bg-gray-200 transition-colors"
      >
        Réinitialiser
      </button>
      <button
        @click="applyFilters"
        class="px-4 py-2 text-sm font-medium text-white bg-blue-600 rounded-lg hover:bg-blue-700 transition-colors"
      >
        Appliquer
      </button>
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue'

const emit = defineEmits(['apply', 'reset'])

const filters = reactive({
  transformateurId: null,
  status: '',
  minTemp: null,
  maxTemp: null,
  minCurrent: null,
  maxHealthScore: null,
  startDate: '',
  endDate: '',
})

const applyFilters = () => {
  const cleanFilters = {}
  Object.entries(filters).forEach(([key, value]) => {
    if (value !== null && value !== '') {
      cleanFilters[key] = value
    }
  })
  emit('apply', cleanFilters)
}

const resetFilters = () => {
  Object.keys(filters).forEach((key) => {
    filters[key] = key.includes('Date') ? '' : null
  })
  emit('reset')
}
</script>
