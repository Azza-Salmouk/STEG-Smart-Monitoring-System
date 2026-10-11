<template>
  <div class="flex gap-2">
    <!-- Statut Badge -->
    <span
      :class="[
        'inline-flex items-center px-3 py-1 rounded-full text-xs font-medium gap-1',
        statutBadgeClass,
      ]"
      :title="`Statut: ${statutLabel}`"
    >
      <component :is="statutIcon" :size="14" />
      {{ statutLabel }}
    </span>

    <!-- Priorité Badge -->
    <span
      :class="[
        'inline-flex items-center px-3 py-1 rounded-full text-xs font-medium gap-1',
        prioriteBadgeClass,
      ]"
      :title="`Priorité: ${prioriteLabel}`"
    >
      <component :is="prioriteIcon" :size="14" />
      {{ prioriteLabel }}
    </span>

    <!-- Type Badge -->
    <span
      v-if="type"
      :class="[
        'inline-flex items-center px-3 py-1 rounded-full text-xs font-medium gap-1',
        typeBadgeClass,
      ]"
      :title="`Type: ${typeLabel}`"
    >
      {{ typeLabel }}
    </span>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { CheckCircle, Clock, Zap, AlertCircle, AlertTriangle, Hammer, Shield, Microscope } from 'lucide-vue-next'

const props = defineProps({
  statut: {
    type: String,
    required: true,
    validator: (v) => ['EN_ATTENTE', 'PLANIFIEE', 'EN_COURS', 'TERMINEE', 'VALIDEE', 'ANNULEE'].includes(v),
  },
  priorite: {
    type: String,
    required: true,
    validator: (v) => ['FAIBLE', 'MOYENNE', 'HAUTE', 'CRITIQUE'].includes(v),
  },
  type: {
    type: String,
    validator: (v) => !v || ['PREVENTIVE', 'CORRECTIVE', 'URGENTE', 'INSPECTION'].includes(v),
  },
})

// Statut Badge
const statutBadgeClass = computed(() => {
  switch (props.statut) {
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
})

const statutLabel = computed(() => {
  switch (props.statut) {
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
    case 'ANNULEE':
      return 'Annulée'
    default:
      return props.statut
  }
})

const statutIcon = computed(() => {
  switch (props.statut) {
    case 'EN_ATTENTE':
      return Clock
    case 'PLANIFIEE':
      return Clock
    case 'EN_COURS':
      return Zap
    case 'TERMINEE':
      return CheckCircle
    case 'VALIDEE':
      return CheckCircle
    case 'ANNULEE':
      return AlertCircle
    default:
      return Clock
  }
})

// Priorité Badge
const prioriteBadgeClass = computed(() => {
  switch (props.priorite) {
    case 'FAIBLE':
      return 'bg-gray-100 text-gray-800'
    case 'MOYENNE':
      return 'bg-blue-100 text-blue-800'
    case 'HAUTE':
      return 'bg-orange-100 text-orange-800'
    case 'CRITIQUE':
      return 'bg-red-100 text-red-800'
    default:
      return 'bg-gray-100 text-gray-800'
  }
})

const prioriteLabel = computed(() => {
  switch (props.priorite) {
    case 'FAIBLE':
      return 'Faible'
    case 'MOYENNE':
      return 'Moyenne'
    case 'HAUTE':
      return 'Haute'
    case 'CRITIQUE':
      return 'Critique'
    default:
      return props.priorite
  }
})

const prioriteIcon = computed(() => {
  switch (props.priorite) {
    case 'FAIBLE':
      return AlertTriangle
    case 'MOYENNE':
      return AlertTriangle
    case 'HAUTE':
      return AlertTriangle
    case 'CRITIQUE':
      return AlertCircle
    default:
      return AlertTriangle
  }
})

// Type Badge
const typeBadgeClass = computed(() => {
  switch (props.type) {
    case 'PREVENTIVE':
      return 'bg-purple-100 text-purple-800'
    case 'CORRECTIVE':
      return 'bg-blue-100 text-blue-800'
    case 'URGENTE':
      return 'bg-red-100 text-red-800'
    case 'INSPECTION':
      return 'bg-green-100 text-green-800'
    default:
      return 'bg-gray-100 text-gray-800'
  }
})

const typeLabel = computed(() => {
  switch (props.type) {
    case 'PREVENTIVE':
      return 'Préventive'
    case 'CORRECTIVE':
      return 'Corrective'
    case 'URGENTE':
      return 'Urgente'
    case 'INSPECTION':
      return 'Inspection'
    default:
      return props.type
  }
})
</script>
