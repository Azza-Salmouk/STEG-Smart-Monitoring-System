<template>
  <div class="flex gap-2">
    <!-- Statut Badge -->
    <span :class="['inline-flex items-center px-3 py-1 rounded-full text-xs font-medium gap-1', statutBadgeClass]">
      {{ statutLabel }}
    </span>

    <!-- Priorité Badge -->
    <span :class="['inline-flex items-center px-3 py-1 rounded-full text-xs font-medium gap-1', prioriteBadgeClass]">
      {{ prioriteLabel }}
    </span>

    <!-- Catégorie Badge -->
    <span v-if="categorie" :class="['inline-flex items-center px-3 py-1 rounded-full text-xs font-medium gap-1', categorieBadgeClass]">
      {{ categorieLabel }}
    </span>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  statut: {
    type: String,
    required: true,
    validator: (v) => ['NOUVELLE', 'EN_ANALYSE', 'ASSIGNEE', 'EN_TRAITEMENT', 'RESOLUE', 'FERMEE'].includes(v),
  },
  priorite: {
    type: String,
    required: true,
    validator: (v) => ['LOW', 'MEDIUM', 'HIGH', 'CRITICAL'].includes(v),
  },
  categorie: {
    type: String,
    validator: (v) => !v || ['COUPURE', 'SURCHARGE', 'PANNE', 'FACTURATION', 'BRUIT', 'ODEUR', 'AUTRE'].includes(v),
  },
})

// Statut Badge
const statutBadgeClass = computed(() => {
  switch (props.statut) {
    case 'NOUVELLE':
      return 'bg-blue-100 text-blue-800'
    case 'EN_ANALYSE':
      return 'bg-purple-100 text-purple-800'
    case 'ASSIGNEE':
      return 'bg-cyan-100 text-cyan-800'
    case 'EN_TRAITEMENT':
      return 'bg-yellow-100 text-yellow-800'
    case 'RESOLUE':
      return 'bg-green-100 text-green-800'
    case 'FERMEE':
      return 'bg-gray-100 text-gray-800'
    default:
      return 'bg-gray-100 text-gray-800'
  }
})

const statutLabel = computed(() => {
  switch (props.statut) {
    case 'NOUVELLE':
      return 'Nouvelle'
    case 'EN_ANALYSE':
      return 'En analyse'
    case 'ASSIGNEE':
      return 'Assignée'
    case 'EN_TRAITEMENT':
      return 'En traitement'
    case 'RESOLUE':
      return 'Résolue'
    case 'FERMEE':
      return 'Fermée'
    default:
      return props.statut
  }
})

// Priorité Badge
const prioriteBadgeClass = computed(() => {
  switch (props.priorite) {
    case 'LOW':
      return 'bg-gray-100 text-gray-800'
    case 'MEDIUM':
      return 'bg-blue-100 text-blue-800'
    case 'HIGH':
      return 'bg-orange-100 text-orange-800'
    case 'CRITICAL':
      return 'bg-red-100 text-red-800'
    default:
      return 'bg-gray-100 text-gray-800'
  }
})

const prioriteLabel = computed(() => {
  switch (props.priorite) {
    case 'LOW':
      return 'Faible'
    case 'MEDIUM':
      return 'Moyenne'
    case 'HIGH':
      return 'Haute'
    case 'CRITICAL':
      return 'Critique'
    default:
      return props.priorite
  }
})

// Catégorie Badge
const categorieBadgeClass = computed(() => {
  switch (props.categorie) {
    case 'COUPURE':
      return 'bg-red-100 text-red-800'
    case 'SURCHARGE':
      return 'bg-orange-100 text-orange-800'
    case 'PANNE':
      return 'bg-red-100 text-red-800'
    case 'FACTURATION':
      return 'bg-green-100 text-green-800'
    case 'BRUIT':
      return 'bg-yellow-100 text-yellow-800'
    case 'ODEUR':
      return 'bg-purple-100 text-purple-800'
    case 'AUTRE':
      return 'bg-gray-100 text-gray-800'
    default:
      return 'bg-gray-100 text-gray-800'
  }
})

const categorieLabel = computed(() => {
  const categories = {
    COUPURE: 'Coupure',
    SURCHARGE: 'Surcharge',
    PANNE: 'Panne',
    FACTURATION: 'Facturation',
    BRUIT: 'Bruit',
    ODEUR: 'Odeur',
    AUTRE: 'Autre'
  }
  return categories[props.categorie] || props.categorie
})
</script>
