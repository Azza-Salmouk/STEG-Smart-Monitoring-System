<template>
  <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
    <div class="flex items-center justify-between p-4 bg-red-50 rounded-lg border border-red-200">
      <div>
        <p class="text-sm font-semibold text-red-900">{{ criticalLabel }}</p>
        <p class="text-xs text-red-600" v-if="criticalDescription">{{ criticalDescription }}</p>
      </div>
      <span class="text-3xl font-bold text-red-600">{{ criticalValue }}</span>
    </div>

    <div class="flex items-center justify-between p-4 bg-orange-50 rounded-lg border border-orange-200">
      <div>
        <p class="text-sm font-semibold text-orange-900">{{ warningLabel }}</p>
        <p class="text-xs text-orange-600" v-if="warningDescription">{{ warningDescription }}</p>
      </div>
      <span class="text-3xl font-bold text-orange-600">{{ warningValue }}</span>
    </div>

    <div class="flex items-center justify-between p-4 bg-blue-50 rounded-lg border border-blue-200">
      <div>
        <p class="text-sm font-semibold text-blue-900">{{ infoLabel }}</p>
        <p class="text-xs text-blue-600" v-if="infoDescription">{{ infoDescription }}</p>
      </div>
      <span class="text-3xl font-bold text-blue-600">{{ infoValue }}</span>
    </div>

    <div class="flex items-center justify-between p-4 bg-purple-50 rounded-lg border border-purple-200">
      <div>
        <p class="text-sm font-semibold text-purple-900">{{ pendingLabel }}</p>
        <p class="text-xs text-purple-600" v-if="pendingDescription">{{ pendingDescription }}</p>
      </div>
      <span class="text-3xl font-bold text-purple-600">{{ pendingValue }}</span>
    </div>
  </div>
</template>

<script setup>
defineProps({
  criticalLabel: {
    type: String,
    default: 'Critiques',
  },
  criticalValue: {
    type: [String, Number],
    required: true,
  },
  criticalDescription: {
    type: String,
    default: null,
  },
  warningLabel: {
    type: String,
    default: 'Avertissements',
  },
  warningValue: {
    type: [String, Number],
    required: true,
  },
  warningDescription: {
    type: String,
    default: null,
  },
  infoLabel: {
    type: String,
    default: 'Actifs',
  },
  infoValue: {
    type: [String, Number],
    required: true,
  },
  infoDescription: {
    type: String,
    default: null,
  },
  pendingLabel: {
    type: String,
    default: 'En attente',
  },
  pendingValue: {
    type: [String, Number],
    required: true,
  },
  pendingDescription: {
    type: String,
    default: null,
  },
})
</script>
