<template>
  <div class="bg-white rounded-lg shadow">
    <!-- Table -->
    <div class="overflow-x-auto">
      <table class="w-full text-sm">
        <thead class="bg-gray-50 border-b">
          <tr>
            <th class="px-6 py-3 text-left font-semibold text-gray-700">Référence</th>
            <th class="px-6 py-3 text-left font-semibold text-gray-700">Transformateur</th>
            <th class="px-6 py-3 text-left font-semibold text-gray-700">Agent</th>
            <th class="px-6 py-3 text-left font-semibold text-gray-700">Priorité</th>
            <th class="px-6 py-3 text-left font-semibold text-gray-700">Statut</th>
            <th class="px-6 py-3 text-left font-semibold text-gray-700">Date Planifiée</th>
            <th class="px-6 py-3 text-right font-semibold text-gray-700">Actions</th>
          </tr>
        </thead>
        <tbody class="divide-y">
          <!-- Loading state -->
          <tr v-if="loading" v-for="i in 5" :key="i" class="hover:bg-gray-50">
            <td v-for="j in 7" :key="j" class="px-6 py-4">
              <div class="h-4 bg-gray-200 rounded animate-pulse"></div>
            </td>
          </tr>

          <!-- Empty state -->
          <tr v-else-if="interventions.length === 0">
            <td colspan="7" class="px-6 py-12 text-center">
              <p class="text-gray-500">Aucune intervention</p>
            </td>
          </tr>

          <!-- Data rows -->
          <tr v-for="intervention in interventions" v-else :key="intervention.id" class="hover:bg-gray-50 transition-colors">
            <td class="px-6 py-4 text-gray-900 font-medium">{{ intervention.reference }}</td>
            <td class="px-6 py-4 text-gray-900">{{ intervention.transformateurNom }}</td>
            <td class="px-6 py-4 text-gray-900">{{ intervention.agentNom }}</td>
            <td class="px-6 py-4">
              <InterventionBadge
                :statut="intervention.statut"
                :priorite="intervention.priorite"
                :type="intervention.typeIntervention"
              />
            </td>
            <td class="px-6 py-4">
              <span
                :class="[
                  'inline-flex items-center px-2 py-1 rounded-full text-xs font-medium',
                  getStatusClass(intervention.statut),
                ]"
              >
                {{ formatStatus(intervention.statut) }}
              </span>
            </td>
            <td class="px-6 py-4 text-gray-900">{{ formatDate(intervention.datePlanifiee) }}</td>
            <td class="px-6 py-4 text-right">
              <slot name="actions" :intervention="intervention"></slot>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Pagination -->
    <div v-if="totalPages > 1" class="flex items-center justify-between p-4 border-t bg-gray-50">
      <p class="text-sm text-gray-600">
        Page {{ currentPage + 1 }} sur {{ totalPages }} ({{ totalElements }} total)
      </p>
      <div class="flex gap-2">
        <button
          :disabled="currentPage === 0"
          @click="$emit('prev')"
          class="px-4 py-2 bg-white border rounded hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
        >
          Précédent
        </button>
        <button
          :disabled="currentPage >= totalPages - 1"
          @click="$emit('next')"
          class="px-4 py-2 bg-white border rounded hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
        >
          Suivant
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import InterventionBadge from './InterventionBadge.vue'

defineProps({
  interventions: {
    type: Array,
    default: () => [],
  },
  loading: {
    type: Boolean,
    default: false,
  },
  currentPage: {
    type: Number,
    default: 0,
  },
  totalPages: {
    type: Number,
    default: 0,
  },
  totalElements: {
    type: Number,
    default: 0,
  },
})

defineEmits(['prev', 'next', 'edit', 'delete'])

function getStatusClass(statut) {
  switch (statut) {
    case 'EN_ATTENTE':
      return 'bg-gray-100 text-gray-800'
    case 'PLANIFIEE':
      return 'bg-blue-100 text-blue-800'
    case 'EN_COURS':
      return 'bg-yellow-100 text-yellow-800'
    case 'TERMINEE':
      return 'bg-green-100 text-green-800'
    case 'VALIDEE':
      return 'bg-green-200 text-green-900'
    case 'ANNULEE':
      return 'bg-red-100 text-red-800'
    default:
      return 'bg-gray-100 text-gray-800'
  }
}

function formatStatus(statut) {
  switch (statut) {
    case 'EN_ATTENTE':
      return 'En attente'
    case 'PLANIFIEE':
      return 'Planifiée'
    case 'EN_COURS':
      return 'En cours'
    case 'TERMINEE':
      return 'Terminée'
    case 'VALIDEE':
      return 'Validée'
    case 'ANNULEA':
      return 'Annulée'
    default:
      return statut
  }
}

function formatDate(dateString) {
  if (!dateString) return '-'
  return new Date(dateString).toLocaleDateString('fr-FR')
}
</script>
