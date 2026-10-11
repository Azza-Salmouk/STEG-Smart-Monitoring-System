<template>
  <div class="inline-flex items-center gap-2">
    <!-- Niveau Badge -->
    <span :class="['px-3 py-1 rounded-full text-xs font-semibold', niveauColor]">
      {{ alert.niveau }}
    </span>

    <!-- Statut Badge -->
    <span :class="['px-3 py-1 rounded-full text-xs font-semibold', statutColor]">
      {{ alert.statut }}
    </span>

    <!-- Acknowledged Indicator -->
    <span v-if="alert.isAcknowledged" class="flex items-center gap-1 text-xs px-2 py-1 bg-blue-100 text-blue-700 rounded">
      <CheckCircle2 :size="14" />
      Reconnue
    </span>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { CheckCircle2 } from 'lucide-vue-next';

const props = defineProps({
  alert: {
    type: Object,
    required: true,
  },
});

const niveauColor = computed(() => {
  const colors = {
    INFO: 'bg-blue-100 text-blue-700',
    WARNING: 'bg-yellow-100 text-yellow-700',
    HIGH: 'bg-orange-100 text-orange-700',
    CRITICAL: 'bg-red-100 text-red-700',
  };
  return colors[props.alert.niveau] || 'bg-gray-100 text-gray-700';
});

const statutColor = computed(() => {
  const colors = {
    ACTIVE: 'bg-red-100 text-red-700',
    ACKNOWLEDGED: 'bg-blue-100 text-blue-700',
    IN_PROGRESS: 'bg-purple-100 text-purple-700',
    RESOLVED: 'bg-green-100 text-green-700',
    CLOSED: 'bg-gray-100 text-gray-700',
  };
  return colors[props.alert.statut] || 'bg-gray-100 text-gray-700';
});
</script>
