<template>
  <div class="bg-white rounded-lg shadow">
    <div class="overflow-x-auto">
      <table class="w-full text-sm">
        <thead class="bg-gray-50 border-b">
          <tr>
            <th class="px-6 py-3 text-left font-semibold text-gray-700">Référence</th>
            <th class="px-6 py-3 text-left font-semibold text-gray-700">Client</th>
            <th class="px-6 py-3 text-left font-semibold text-gray-700">Catégorie</th>
            <th class="px-6 py-3 text-left font-semibold text-gray-700">Badges</th>
            <th class="px-6 py-3 text-left font-semibold text-gray-700">Superviseur</th>
            <th class="px-6 py-3 text-left font-semibold text-gray-700">Créée</th>
            <th class="px-6 py-3 text-right font-semibold text-gray-700">Actions</th>
          </tr>
        </thead>
        <tbody class="divide-y">
          <tr v-if="loading" v-for="i in 5" :key="i" class="hover:bg-gray-50">
            <td v-for="j in 7" :key="j" class="px-6 py-4">
              <div class="h-4 bg-gray-200 rounded animate-pulse"></div>
            </td>
          </tr>

          <tr v-else-if="complaints.length === 0">
            <td colspan="7" class="px-6 py-12 text-center">
              <p class="text-gray-500">Aucune réclamation</p>
            </td>
          </tr>

          <tr v-for="complaint in complaints" v-else :key="complaint.id" class="hover:bg-gray-50 transition-colors">
            <td class="px-6 py-4 text-gray-900 font-medium">{{ complaint.reference }}</td>
            <td class="px-6 py-4 text-gray-900">{{ complaint.nomClient }}</td>
            <td class="px-6 py-4 text-gray-900">{{ formatCategory(complaint.categorie) }}</td>
            <td class="px-6 py-4">
              <ComplaintBadge
                :statut="complaint.statut"
                :priorite="complaint.priorite"
                :categorie="complaint.categorie"
              />
            </td>
            <td class="px-6 py-4 text-gray-900">
              {{ complaint.assignedSupervisorName || '-' }}
            </td>
            <td class="px-6 py-4 text-gray-900">{{ formatDate(complaint.createdAt) }}</td>
            <td class="px-6 py-4 text-right">
              <slot name="actions" :complaint="complaint"></slot>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

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
import ComplaintBadge from './ComplaintBadge.vue'

defineProps({
  complaints: {
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

function formatCategory(categorie) {
  const categories = {
    COUPURE: 'Coupure',
    SURCHARGE: 'Surcharge',
    PANNE: 'Panne',
    FACTURATION: 'Facturation',
    BRUIT: 'Bruit',
    ODEUR: 'Odeur',
    AUTRE: 'Autre'
  }
  return categories[categorie] || categorie
}

function formatDate(dateString) {
  if (!dateString) return '-'
  return new Date(dateString).toLocaleDateString('fr-FR')
}
</script>
