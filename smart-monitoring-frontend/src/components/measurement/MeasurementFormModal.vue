<template>
  <div class="fixed inset-0 bg-black bg-opacity-50 z-50 flex items-center justify-center p-4">
    <div class="bg-white rounded-lg shadow-xl max-w-2xl w-full max-h-screen overflow-y-auto">
      <!-- Header -->
      <div class="px-6 py-4 border-b border-gray-200 flex justify-between items-center">
        <h2 class="text-xl font-semibold text-gray-900">
          {{ measurement?.id ? 'Modifier la mesure' : 'Nouvelle mesure' }}
        </h2>
        <button
          @click="$emit('close')"
          class="text-gray-400 hover:text-gray-600"
        >
          <XIcon class="w-6 h-6" />
        </button>
      </div>

      <!-- Form -->
      <div class="px-6 py-4">
        <form @submit.prevent="handleSubmit">
          <!-- Transformateur -->
          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-1">
              Transformateur <span class="text-red-500">*</span>
            </label>
            <select
              v-model="formData.transformateurId"
              class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              required
              @change="validateForm"
            >
              <option value="">Sélectionner un transformateur</option>
              <option v-for="t in transformateurs" :key="t.id" :value="t.id">
                {{ t.nom }} ({{ t.poste?.nom }})
              </option>
            </select>
            <span v-if="errors.transformateurId" class="text-sm text-red-500">{{ errors.transformateurId }}</span>
          </div>

          <!-- Temperature -->
          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-1">
              Température (°C) <span class="text-red-500">*</span>
            </label>
            <input
              v-model.number="formData.temperature"
              type="number"
              step="0.01"
              class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              required
              @blur="validateForm"
            />
            <span v-if="errors.temperature" class="text-sm text-red-500">{{ errors.temperature }}</span>
          </div>

          <!-- Voltage -->
          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-1">
              Tension (V) <span class="text-red-500">*</span>
            </label>
            <input
              v-model.number="formData.voltage"
              type="number"
              step="0.01"
              class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              required
              @blur="validateForm"
            />
            <span v-if="errors.voltage" class="text-sm text-red-500">{{ errors.voltage }}</span>
          </div>

          <!-- Current -->
          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-1">
              Courant (A) <span class="text-red-500">*</span>
            </label>
            <input
              v-model.number="formData.current"
              type="number"
              step="0.01"
              class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              required
              @blur="validateForm"
            />
            <span v-if="errors.current" class="text-sm text-red-500">{{ errors.current }}</span>
          </div>

          <!-- Power -->
          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-1">
              Puissance (kW)
            </label>
            <input
              v-model.number="formData.power"
              type="number"
              step="0.01"
              class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            />
          </div>

          <!-- Energy Consumption -->
          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-1">
              Consommation énergétique (kWh)
            </label>
            <input
              v-model.number="formData.energyConsumption"
              type="number"
              step="0.01"
              class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            />
          </div>

          <!-- Frequency -->
          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-1">
              Fréquence (Hz)
            </label>
            <input
              v-model.number="formData.frequency"
              type="number"
              step="0.01"
              class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            />
          </div>

          <!-- Humidity -->
          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-1">
              Humidité (%)
            </label>
            <input
              v-model.number="formData.humidity"
              type="number"
              step="0.01"
              min="0"
              max="100"
              class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            />
          </div>

          <!-- Measurement Date -->
          <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-1">
              Date/Heure de mesure
            </label>
            <input
              v-model="formData.measurementDate"
              type="datetime-local"
              class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            />
          </div>

          <!-- Actions -->
          <div class="flex justify-end gap-3 pt-4 border-t border-gray-200">
            <button
              type="button"
              @click="$emit('close')"
              class="px-4 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-lg hover:bg-gray-50"
            >
              Annuler
            </button>
            <button
              type="submit"
              :disabled="loading"
              class="px-4 py-2 text-sm font-medium text-white bg-blue-600 rounded-lg hover:bg-blue-700 disabled:opacity-50"
            >
              {{ loading ? 'En cours...' : 'Enregistrer' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { transformateursAPI } from '@/services/api'
import { XIcon } from 'lucide-vue-next'

const props = defineProps({
  measurement: {
    type: Object,
    default: null
  }
})

const emit = defineEmits(['save', 'close'])

const loading = ref(false)
const transformateurs = ref([])
const formData = ref({
  transformateurId: '',
  temperature: 0,
  voltage: 0,
  current: 0,
  power: 0,
  energyConsumption: 0,
  frequency: 0,
  humidity: 0,
  measurementDate: new Date().toISOString().slice(0, 16)
})

const errors = ref({})

const validateForm = () => {
  errors.value = {}
  
  if (!formData.value.transformateurId) {
    errors.value.transformateurId = 'Transformateur requis'
  }
  if (formData.value.temperature === null || formData.value.temperature === '') {
    errors.value.temperature = 'Température requise'
  }
  if (formData.value.voltage === null || formData.value.voltage === '') {
    errors.value.voltage = 'Tension requise'
  }
  if (formData.value.current === null || formData.value.current === '') {
    errors.value.current = 'Courant requis'
  }

  return Object.keys(errors.value).length === 0
}

const handleSubmit = async () => {
  if (!validateForm()) return

  loading.value = true
  try {
    emit('save', { ...formData.value })
  } catch (error) {
    console.error('Form submit error:', error)
  } finally {
    loading.value = false
  }
}

const loadTransformateurs = async () => {
  try {
    const response = await transformateursAPI.getAll(0, 1000)
    transformateurs.value = response.data.content || response.data
  } catch (error) {
    console.error('Load transformateurs error:', error)
  }
}

watch(
  () => props.measurement,
  (newVal) => {
    if (newVal) {
      formData.value = {
        transformateurId: newVal.transformateurId || newVal.transformateur?.id || '',
        temperature: newVal.temperature || 0,
        voltage: newVal.voltage || 0,
        current: newVal.current || 0,
        power: newVal.power || 0,
        energyConsumption: newVal.energyConsumption || 0,
        frequency: newVal.frequency || 0,
        humidity: newVal.humidity || 0,
        measurementDate: newVal.measurementDate 
          ? new Date(newVal.measurementDate).toISOString().slice(0, 16)
          : new Date().toISOString().slice(0, 16)
      }
    } else {
      formData.value = {
        transformateurId: '',
        temperature: 0,
        voltage: 0,
        current: 0,
        power: 0,
        energyConsumption: 0,
        frequency: 0,
        humidity: 0,
        measurementDate: new Date().toISOString().slice(0, 16)
      }
    }
    errors.value = {}
  }
)

onMounted(async () => {
  await loadTransformateurs()
})
</script>
