<template>
  <div class="space-y-6">
    <!-- Header -->
    <div class="flex items-center justify-between">
      <div>
        <h1 class="text-3xl font-bold text-gray-900">Gestion des Alertes</h1>
        <p class="text-gray-600 mt-1">Suivi et gestion de toutes les alertes techniques</p>
      </div>
      <button
        v-if="canCreate"
        @click="openCreateDialog"
        class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition flex items-center gap-2"
      >
        <Plus :size="20" />
        Nouvelle alerte
      </button>
    </div>

    <!-- KPI Cards -->
    <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
      <div class="bg-white rounded-lg shadow p-6">
        <p class="text-sm text-gray-600 mb-2">Total</p>
        <p class="text-3xl font-bold text-gray-900">{{ statistics?.totalAlerts || 0 }}</p>
      </div>
      <div class="bg-white rounded-lg shadow p-6">
        <p class="text-sm text-gray-600 mb-2">Actives</p>
        <p class="text-3xl font-bold text-red-600">{{ statistics?.activeAlerts || 0 }}</p>
      </div>
      <div class="bg-white rounded-lg shadow p-6">
        <p class="text-sm text-gray-600 mb-2">Critiques</p>
        <p class="text-3xl font-bold text-orange-600">{{ statistics?.criticalAlerts || 0 }}</p>
      </div>
      <div class="bg-white rounded-lg shadow p-6">
        <p class="text-sm text-gray-600 mb-2">Résolues</p>
        <p class="text-3xl font-bold text-green-600">{{ statistics?.resolvedAlerts || 0 }}</p>
      </div>
    </div>

    <!-- Filters and Search -->
    <div class="bg-white rounded-lg shadow p-4">
      <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
        <input
          v-model="searchTerm"
          type="text"
          placeholder="Rechercher..."
          class="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
        />
        <select v-model="filterStatut" class="px-4 py-2 border border-gray-300 rounded-lg">
          <option value="">Tous les statuts</option>
          <option value="ACTIVE">Actif</option>
          <option value="ACKNOWLEDGED">Reconnu</option>
          <option value="IN_PROGRESS">En cours</option>
          <option value="RESOLVED">Résolu</option>
          <option value="CLOSED">Fermé</option>
        </select>
        <select v-model="filterNiveau" class="px-4 py-2 border border-gray-300 rounded-lg">
          <option value="">Tous les niveaux</option>
          <option value="INFO">Info</option>
          <option value="WARNING">Avertissement</option>
          <option value="HIGH">Haut</option>
          <option value="CRITICAL">Critique</option>
        </select>
        <button
          @click="applyFilters"
          class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition"
        >
          Filtrer
        </button>
      </div>
    </div>

    <!-- Alerts Table -->
    <div class="bg-white rounded-lg shadow">
      <AlertTable
        :alerts="alerts"
        :loading="loading"
        @view="viewAlert"
        @acknowledge="acknowledgeAlert"
        @resolve="resolveAlert"
        @archive="archiveAlert"
      />

      <!-- Pagination -->
      <div v-if="totalElements > 0" class="px-6 py-4 border-t border-gray-200 flex items-center justify-between">
        <p class="text-sm text-gray-600">
          Affichage {{ currentPage * pageSize + 1 }} à {{ Math.min((currentPage + 1) * pageSize, totalElements) }}
          sur {{ totalElements }}
        </p>
        <div class="flex gap-2">
          <button
            @click="previousPage"
            :disabled="currentPage === 0"
            class="px-4 py-2 bg-gray-200 text-gray-800 rounded-lg hover:bg-gray-300 transition disabled:opacity-50"
          >
            Précédent
          </button>
          <button
            @click="nextPage"
            :disabled="(currentPage + 1) * pageSize >= totalElements"
            class="px-4 py-2 bg-gray-200 text-gray-800 rounded-lg hover:bg-gray-300 transition disabled:opacity-50"
          >
            Suivant
          </button>
        </div>
      </div>
    </div>

    <!-- Create/Edit Modal -->
    <AlertFormModal
      :is-open="isFormOpen"
      :alert="selectedAlert"
      :mode="formMode"
      @close="isFormOpen = false"
      @saved="loadData"
    />

    <!-- Details Modal -->
    <AlertDetailsModal
      :is-open="isDetailsOpen"
      :alert="selectedAlert"
      @close="isDetailsOpen = false"
      @updated="loadData"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { Plus } from 'lucide-vue-next';
import { useAlertStore } from '@/stores/useAlertStore';
import { useAuthStore } from '@/stores/authStore';
import AlertTable from '@/components/alert/AlertTable.vue';
import AlertFormModal from '@/components/alert/AlertFormModal.vue';
import AlertDetailsModal from '@/components/alert/AlertDetailsModal.vue';

const alertStore = useAlertStore();
const authStore = useAuthStore();

const alerts = ref([]);
const loading = ref(false);
const statistics = ref(null);
const selectedAlert = ref(null);
const isFormOpen = ref(false);
const isDetailsOpen = ref(false);
const formMode = ref('create');

const searchTerm = ref('');
const filterStatut = ref('');
const filterNiveau = ref('');
const currentPage = ref(0);
const pageSize = ref(10);
const totalElements = ref(0);

const canCreate = ref(false);
const canDelete = ref(false);

onMounted(() => {
  const userRole = authStore.userRole;
  canCreate.value = ['ADMIN', 'SUPERVISOR'].includes(userRole);
  canDelete.value = userRole === 'ADMIN';
  loadData();
});

const loadData = async () => {
  loading.value = true;
  try {
    await alertStore.fetchAlerts(currentPage.value, pageSize.value);
    await alertStore.fetchStatistics();
    alerts.value = alertStore.alerts;
    statistics.value = alertStore.statistics;
    totalElements.value = alertStore.totalElements;
  } catch (e) {
    console.error('Error loading alerts:', e);
  } finally {
    loading.value = false;
  }
};

const applyFilters = async () => {
  currentPage.value = 0;
  const filters = {};
  if (searchTerm.value) filters.titre = searchTerm.value;
  if (filterStatut.value) filters.statut = filterStatut.value;
  if (filterNiveau.value) filters.niveau = filterNiveau.value;
  await alertStore.searchAdvanced(filters);
  alerts.value = alertStore.alerts;
};

const viewAlert = async (id) => {
  selectedAlert.value = await alertStore.getAlert(id);
  isDetailsOpen.value = true;
};

const openCreateDialog = () => {
  selectedAlert.value = null;
  formMode.value = 'create';
  isFormOpen.value = true;
};

const acknowledgeAlert = async (id) => {
  try {
    await alertStore.acknowledgeAlert(id);
    await loadData();
  } catch (e) {
    console.error('Error acknowledging alert:', e);
  }
};

const resolveAlert = async (id) => {
  try {
    const userId = authStore.user?.id || 1;
    await alertStore.resolveAlert(id, userId);
    await loadData();
  } catch (e) {
    console.error('Error resolving alert:', e);
  }
};

const archiveAlert = async (id) => {
  try {
    await alertStore.archiveAlert(id);
    await loadData();
  } catch (e) {
    console.error('Error archiving alert:', e);
  }
};

const previousPage = () => {
  if (currentPage.value > 0) {
    currentPage.value--;
    loadData();
  }
};

const nextPage = () => {
  if ((currentPage.value + 1) * pageSize.value < totalElements.value) {
    currentPage.value++;
    loadData();
  }
};
</script>
