<template>
  <span
    :class="[
      'inline-flex items-center px-3 py-1 rounded-full text-sm font-medium gap-1',
      badgeClass,
    ]"
  >
    <component :is="icon" :size="16" />
    {{ statusLabel }}
  </span>
</template>

<script setup>
import { computed } from 'vue'
import { AlertCircle, CheckCircle, AlertTriangle, Wifi } from 'lucide-vue-next'

const props = defineProps({
  status: {
    type: String,
    required: true,
    validator: (v) => ['NORMAL', 'WARNING', 'CRITICAL', 'OFFLINE'].includes(v),
  },
})

const badgeClass = computed(() => {
  switch (props.status) {
    case 'NORMAL':
      return 'bg-green-100 text-green-800'
    case 'WARNING':
      return 'bg-yellow-100 text-yellow-800'
    case 'CRITICAL':
      return 'bg-red-100 text-red-800'
    case 'OFFLINE':
      return 'bg-gray-100 text-gray-800'
    default:
      return 'bg-gray-100 text-gray-800'
  }
})

const statusLabel = computed(() => {
  switch (props.status) {
    case 'NORMAL':
      return 'Normale'
    case 'WARNING':
      return 'Avertissement'
    case 'CRITICAL':
      return 'Critique'
    case 'OFFLINE':
      return 'Hors ligne'
    default:
      return props.status
  }
})

const icon = computed(() => {
  switch (props.status) {
    case 'NORMAL':
      return CheckCircle
    case 'WARNING':
      return AlertTriangle
    case 'CRITICAL':
      return AlertCircle
    case 'OFFLINE':
      return Wifi
    default:
      return AlertCircle
  }
})
</script>
