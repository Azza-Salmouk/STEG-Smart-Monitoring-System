<template>
  <div v-if="isOpen && alert" class="fixed inset-0 bg-black bg-opacity-50 z-50 flex items-center justify-center">
    <div class="bg-white rounded-lg shadow-xl w-full max-w-2xl max-h-[90vh] overflow-y-auto">
      <!-- Header -->
      <div class="sticky top-0 bg-gradient-to-r from-blue-600 to-blue-700 text-white px-6 py-4 flex justify-between items-center">
        <h2 class="text-lg font-semibold">Détails de l'alerte</h2>
        <button @click="close" class="p-1 hover:bg-blue-500 rounded transition">
          <X :size="20" />
        </button>
      </div>

      <!-- Content -->
      <div class="p-6 space-y-6">
        <!-- Reference and Badges -->
        <div>
          <p class="text-sm text-gray-600 mb-2">Référence</p>
          <p class="font-mono text-lg font-semibold text-blue-600 mb-4">{{ alert.reference }}</p>
          <AlertBadge :alert="alert" />
        </div>

        <hr class="border-gray-200" />

        <!-- General Info -->
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div>
            <p class="text-sm text-gray-600 mb-2">Titre</p>
            <p class="font-semibold">{{ alert.titre }}</p>
          </div>
          <div>
            <p class="text-sm text-gray-600 mb-2">Type</p>
            <p class="font-semibold">{{ alert.type }}</p>
          </div>
          <div>
            <p class="text-sm text-gray-600 mb-2">Transformateur</p>
            <p class="font-semibold">{{ alert.transformateurName }}</p>
          </div>
          <div>
            <p class="text-sm text-gray-600 mb-2">Poste</p>
            <p class="font-semibold">{{ alert.posteName || '-' }}</p>
          </div>
          <div>
            <p class="text-sm text-gray-600 mb-2">Créée le</p>
            <p class="font-semibold">{{ formatDate(alert.createdAt) }}</p>
          </div>
          <div v-if="alert.resolvedAt">
            <p class="text-sm text-gray-600 mb-2">Résolue le</p>
            <p class="font-semibold">{{ formatDate(alert.resolvedAt) }}</p>
          </div>
        </div>

        <hr class="border-gray-200" />

        <!-- Description -->
        <div>
          <p class="text-sm text-gray-600 mb-2">Description</p>
          <p class="text-gray-700 leading-relaxed">{{ alert.description }}</p>
        </div>

        <hr class="border-gray-200" />

        <!-- Resolution Info -->
        <div v-if="alert.resolvedByUsername" class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div>
            <p class="text-sm text-gray-600 mb-2">Résolue par</p>
            <p class="font-semibold">{{ alert.resolvedByUsername }}</p>
          </div>
          <div v-if="alert.resolutionTimeMinutes">
            <p class="text-sm text-gray-600 mb-2">Temps de résolution</p>
            <p class="font-semibold">{{ formatResolutionTime(alert.resolutionTimeMinutes) }}</p>
          </div>
        </div>
      </div>

      <!-- Footer -->
      <div class="bg-gray-50 px-6 py-4 flex justify-end gap-3 border-t">
        <button
          v-if="alert.statut === 'ACTIVE' && !alert.isAcknowledged"
          @click="acknowledgeAlert"
          :disabled="actionLoading"
          class="px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 transition font-medium disabled:opacity-50"
        >
          {{ actionLoading ? 'Chargement...' : 'Reconnaître' }}
        </button>
        <button
          v-if="['ACTIVE', 'ACKNOWLEDGED'].includes(alert.statut)"
          @click="resolveAlert"
          :disabled="actionLoading"
          class="px-4 py-2 bg-yellow-600 text-white rounded-lg hover:bg-yellow-700 transition font-medium disabled:opacity-50"
        >
          {{ actionLoading ? 'Chargement...' : 'Résoudre' }}
        </button>
        <button
          @click="close"
          class="px-4 py-2 bg-gray-200 text-gray-800 rounded-lg hover:bg-gray-300 transition font-medium"
        >
          Fermer
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { X } from 'lucide-vue-next';
import { useAlertStore } from '@/stores/useAlertStore';
import AlertBadge from './AlertBadge.vue';

const props = defineProps({
  isOpen: Boolean,
  alert: Object,
});

const emit = defineEmits(['close', 'updated']);

const alertStore = useAlertStore();
const actionLoading = ref(false);

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

const formatResolutionTime = (minutes) => {
  if (!minutes) return '-';
  const hours = Math.floor(minutes / 60);
  const mins = minutes % 60;
  if (hours > 0) return `${hours}h ${mins}min`;
  return `${mins}min`;
};

const acknowledgeAlert = async () => {
  actionLoading.value = true;
  try {
    await alertStore.acknowledgeAlert(props.alert.id);
    emit('updated');
    close();
  } catch (e) {
    console.error('Error acknowledging alert:', e);
  } finally {
    actionLoading.value = false;
  }
};

const resolveAlert = async () => {
  actionLoading.value = true;
  try {
    const userId = 1; // Get from auth
    await alertStore.resolveAlert(props.alert.id, userId);
    emit('updated');
    close();
  } catch (e) {
    console.error('Error resolving alert:', e);
  } finally {
    actionLoading.value = false;
  }
};

const close = () => {
  emit('close');
};
</script>
