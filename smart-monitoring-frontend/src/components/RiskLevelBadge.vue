<template>
  <span
    :class="[
      'px-3 py-1 rounded-full text-sm font-medium whitespace-nowrap',
      riskClasses[riskLevel] || 'bg-gray-100 text-gray-800'
    ]"
  >
    {{ riskLabel[riskLevel] || riskLevel }}
  </span>
</template>

<script setup>
defineProps({
  riskLevel: {
    type: String,
    required: true
  }
})

const riskClasses = {
  'LOW': 'bg-green-100 text-green-800',
  'MEDIUM': 'bg-yellow-100 text-yellow-800',
  'HIGH': 'bg-orange-100 text-orange-800',
  'CRITICAL': 'bg-red-100 text-red-800'
}

const riskLabel = {
  'LOW': 'Bas',
  'MEDIUM': 'Moyen',
  'HIGH': 'Haut',
  'CRITICAL': 'Critique'
}
</script>

<style scoped></style>
