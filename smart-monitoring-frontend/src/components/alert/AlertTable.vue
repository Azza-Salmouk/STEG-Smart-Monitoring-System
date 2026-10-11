<template>
  <div class="bg-white rounded-lg shadow">
    <!-- Loading state -->
    <div v-if="loading" class="p-8 flex justify-center items-center">
      <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-500"></div>
    </div>

    <!-- Table -->
    <div v-else class="overflow-x-auto">
      <table class="w-full text-sm">
        <thead class="bg-gray-50 border-b">
          <tr>
            <th class="px-6 py-3 text-left font-semibold text-gray-700">Référence</th>
            <th class="px-6 py-3 text-left font-semibold text-gray-700">Titre</th>
            <th class="px-6 py-3 text-left font-semibold text-gray-700">Transformateur</th>
            <th class="px-6 py-3 text-left font-semibold text-gray-700">Poste</th>
            <th class="px-6 py-3 text-left font-semibold text-gray-700">Type</th>
            <th class="px-6 py-3 text-left font-semibold text-gray-700">Niveau / Statut</th>
            <th class="px-6 py-3 text-left font-semibold text-gray-700">Date</th>
            <th class="px-6 py-3 text-center font-semibold text-gray-700">Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="alert in alerts" :key="alert.id" class="border-b hover:bg-gray-50 transition">
            <td class="px-6 py-4 font-mono text-blue-600">{{ alert.reference }}</td>
            <td class="px-6 py-4">{{ alert.titre }}</td>
            <td class="px-6 py-4">{{ alert.transformateurName }}</td>
            <td class="px-6 py-4">{{ alert.posteName || '-' }}</td>
            <td class="px-6 py-4 text-xs">{{ alert.type }}</td>
            <td class="px-6 py-4">
              <AlertBadge :alert="alert" />
            </td>
            <td class="px-6 py-4 text-xs text-gray-500">{{ formatDate(alert.createdAt) }}</td>
            <td class="px-6 py-4">
              <div class="flex justify-center gap-2">
                <button
                  @click="$emit('view', alert.id)"
                  title="Voir détails"
                  class="p-1 text-blue-600 hover:bg-blue-50 rounded transition"
                >
                  <Eye :size="18" />
                </button>
                <button
                  v-if="alert.statut === 'ACTIVE' && !alert.isAcknowledged"
                  @click="$emit('acknowledge', alert.id)"
                  title="Reconnaître"
                  class="p-1 text-green-600 hover:bg-green-50 rounded transition"
                >
                  <Check :size="18" />
                </button>
                <button
                  v-if="['ACTIVE', 'ACKNOWLEDGED'].includes(alert.statut)"
                  @click="$emit('resolve', alert.id)"
                  title="Résoudre"
                  class="p-1 text-yellow-600 hover:bg-yellow-50 rounded transition"
                >
                  <CheckCircle2 :size="18" />
                </button>
                <button
                  v-if="!alert.isArchived"
                  @click="$emit('archive', alert.id)"
                  title="Archiver"
                  class="p-1 text-purple-600 hover:bg-purple-50 rounded transition"
                >
                  <Archive :size="18" />
                </button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>

      <!-- Empty state -->
      <div v-if="alerts.length === 0" class="p-8 text-center text-gray-500">
        Aucune alerte trouvée
      </div>
    </div>
  </div>
</template>

<script setup>
import { Eye, Check, CheckCircle2, Archive } from 'lucide-vue-next';
import AlertBadge from './AlertBadge.vue';

defineProps({
  alerts: {
    type: Array,
    required: true,
  },
  loading: {
    type: Boolean,
    default: false,
  },
});

defineEmits(['view', 'acknowledge', 'resolve', 'archive', 'delete']);

const formatDate = (date) => {
  if (!date) return '-';
  return new Date(date).toLocaleDateString('fr-FR', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
  });
};
</script>
