<template>
  <span
    :class="[
      'px-3 py-1 rounded-full text-sm font-medium whitespace-nowrap',
      statusClasses[status] || 'bg-gray-100 text-gray-800'
    ]"
  >
    {{ statusLabel[status] || status }}
  </span>
</template>

<script setup>
defineProps({
  status: {
    type: String,
    required: true
  }
})

const statusClasses = {
  'ACTIF': 'bg-green-100 text-green-800',
  'ALERTE': 'bg-yellow-100 text-yellow-800',
  'DEFAUT': 'bg-orange-100 text-orange-800',
  'MAINTENANCE': 'bg-blue-100 text-blue-800',
  'HORS_SERVICE': 'bg-red-100 text-red-800',
  'ACTIVE': 'bg-green-100 text-green-800',
  'WARNING': 'bg-yellow-100 text-yellow-800',
  'FAULT': 'bg-orange-100 text-orange-800',
  'OUT_OF_SERVICE': 'bg-red-100 text-red-800'
}

const statusLabel = {
  'ACTIF': 'Actif',
  'ALERTE': 'Alerte',
  'DEFAUT': 'Défaut',
  'MAINTENANCE': 'Maintenance',
  'HORS_SERVICE': 'Hors Service',
  'ACTIVE': 'Actif',
  'WARNING': 'Alerte',
  'FAULT': 'Défaut',
  'OUT_OF_SERVICE': 'Hors Service'
}
</script>

<style scoped></style>
