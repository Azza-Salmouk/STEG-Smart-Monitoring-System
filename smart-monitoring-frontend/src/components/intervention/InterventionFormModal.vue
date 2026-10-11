<template>
  <div class="fixed inset-0 bg-black bg-opacity-50 z-50 flex items-center justify-center">
    <div class="bg-white rounded-lg shadow-lg w-full max-w-2xl mx-4 max-h-[90vh] overflow-y-auto">
      <!-- Header -->
      <div class="flex items-center justify-between p-6 border-b">
        <h2 class="text-xl font-bold text-gray-900">
          {{ isEdit ? 'Modifier Intervention' : 'Créer Intervention' }}
        </h2>
        <button
          @click="$emit('close')"
          class="text-gray-500 hover:text-gray-700"
        >
          ✕
        </button>
      </div>

      <!-- Form -->
      <form @submit.prevent="handleSubmit" class="p-6 space-y-4">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <!-- Transformateur -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Transformateur *</label>
            <select
              v-model="form.transformateurId"
              class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            >
              <option value="">Sélectionner...</option>
              <option v-for="t in transformateurs" :key="t.id" :value="t.id">
                {{ t.reference }}
              </option>
            </select>
          </div>

          <!-- Agent -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Agent *</label>
            <select
              v-model="form.agentId"
              class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            >
              <option value="">Sélectionner...</option>
              <option v-for="a in agents" :key="a.id" :value="a.id">
                {{ a.username }}
              </option>
            </select>
          </div>

          <!-- Date Planifiée -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Date Planifiée *</label>
            <input
              v-model="form.datePlanifiee"
              type="datetime-local"
              class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            />
          </div>

          <!-- Type -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Type *</label>
            <select
              v-model="form.typeIntervention"
              class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            >
              <option value="">Sélectionner...</option>
              <option value="PREVENTIVE">Préventive</option>
              <option value="CORRECTIVE">Corrective</option>
              <option value="URGENTE">Urgente</option>
              <option value="INSPECTION">Inspection</option>
            </select>
          </div>

          <!-- Priorité -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Priorité *</label>
            <select
              v-model="form.priorite"
              class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            >
              <option value="">Sélectionner...</option>
              <option value="FAIBLE">Faible</option>
              <option value="MOYENNE">Moyenne</option>
              <option value="HAUTE">Haute</option>
              <option value="CRITIQUE">Critique</option>
            </select>
          </div>

          <!-- Coût -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Coût</label>
            <input
              v-model.number="form.cout"
              type="number"
              step="0.01"
              class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
        </div>

        <!-- Description -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Description *</label>
          <textarea
            v-model="form.description"
            class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            rows="3"
            required
          ></textarea>
        </div>

        <!-- Observations -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Observations</label>
          <textarea
            v-model="form.observations"
            class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            rows="2"
          ></textarea>
        </div>

        <!-- Buttons -->
        <div class="flex gap-3 justify-end pt-4 border-t">
          <button
            type="button"
            @click="$emit('close')"
            class="px-4 py-2 bg-gray-200 text-gray-700 rounded-lg hover:bg-gray-300"
          >
            Annuler
          </button>
          <button
            type="submit"
            class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
          >
            {{ isEdit ? 'Mettre à jour' : 'Créer' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { transformateursAPI } from '@/services/api'
import { usersAPI } from '@/services/api'

const props = defineProps({
  intervention: Object,
  isEdit: Boolean,
})

const emit = defineEmits(['save', 'close'])

const transformateurs = ref([])
const agents = ref([])

const form = ref({
  transformateurId: null,
  agentId: null,
  datePlanifiee: null,
  typeIntervention: '',
  priorite: '',
  description: '',
  observations: '',
  cout: null,
})

onMounted(async () => {
  // Load transformateurs
  const tResponse = await transformateursAPI.getAll(0, 100)
  transformateurs.value = tResponse.data.content || []

  // Load agents
  const aResponse = await usersAPI.getAll(0, 100)
  agents.value = (aResponse.data.content || []).filter(u => u.role === 'AGENT')

  // Populate form if editing
  if (props.isEdit && props.intervention) {
    form.value = {
      transformateurId: props.intervention.transformateurId,
      agentId: props.intervention.agentId,
      datePlanifiee: props.intervention.datePlanifiee,
      typeIntervention: props.intervention.typeIntervention,
      priorite: props.intervention.priorite,
      description: props.intervention.description,
      observations: props.intervention.observations,
      cout: props.intervention.cout,
    }
  }
})

const handleSubmit = () => {
  emit('save', form.value)
}
</script>
