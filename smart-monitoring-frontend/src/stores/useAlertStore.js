import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import * as api from '@/services/api';

export const useAlertStore = defineStore('alert', () => {
  // State
  const alerts = ref([]);
  const loading = ref(false);
  const error = ref(null);
  const totalElements = ref(0);
  const currentPage = ref(0);
  const pageSize = ref(10);
  const statistics = ref(null);
  const selectedAlert = ref(null);

  // Filters
  const filters = ref({
    statut: null,
    niveau: null,
    type: null,
    searchTerm: ''
  });

  // Computed
  const alertCount = computed(() => alerts.value.length);
  const criticalCount = computed(() => 
    alerts.value.filter(a => a.niveau === 'CRITICAL').length
  );
  const activeCount = computed(() => 
    alerts.value.filter(a => a.statut === 'ACTIVE').length
  );

  // Actions
  async function fetchAlerts(page = 0, size = 10, filters_ = null) {
    loading.value = true;
    error.value = null;
    try {
      const params = {
        page,
        size,
        ...filters_
      };
      const response = await api.alertsAPI.getAll(params);
      alerts.value = response.data.content || [];
      totalElements.value = response.data.totalElements || 0;
      currentPage.value = page;
    } catch (e) {
      error.value = e.message;
      console.error('Error fetching alerts:', e);
    } finally {
      loading.value = false;
    }
  }

  async function fetchStatistics() {
    try {
      const response = await api.alertsAPI.getStatistics();
      statistics.value = response.data;
    } catch (e) {
      console.error('Error fetching statistics:', e);
    }
  }

  async function getAlert(id) {
    loading.value = true;
    error.value = null;
    try {
      const response = await api.alertsAPI.getById(id);
      selectedAlert.value = response.data;
      return response.data;
    } catch (e) {
      error.value = e.message;
      console.error('Error fetching alert:', e);
    } finally {
      loading.value = false;
    }
  }

  async function createAlert(data) {
    loading.value = true;
    error.value = null;
    try {
      const response = await api.alertsAPI.create(data);
      await fetchAlerts(currentPage.value, pageSize.value);
      await fetchStatistics();
      return response.data;
    } catch (e) {
      error.value = e.message;
      console.error('Error creating alert:', e);
      throw e;
    } finally {
      loading.value = false;
    }
  }

  async function updateAlert(id, data) {
    loading.value = true;
    error.value = null;
    try {
      const response = await api.alertsAPI.update(id, data);
      await fetchAlerts(currentPage.value, pageSize.value);
      await fetchStatistics();
      return response.data;
    } catch (e) {
      error.value = e.message;
      console.error('Error updating alert:', e);
      throw e;
    } finally {
      loading.value = false;
    }
  }

  async function deleteAlert(id) {
    loading.value = true;
    error.value = null;
    try {
      await api.alertsAPI.delete(id);
      await fetchAlerts(currentPage.value, pageSize.value);
      await fetchStatistics();
    } catch (e) {
      error.value = e.message;
      console.error('Error deleting alert:', e);
      throw e;
    } finally {
      loading.value = false;
    }
  }

  async function acknowledgeAlert(id) {
    loading.value = true;
    error.value = null;
    try {
      const response = await api.alertsAPI.acknowledge(id);
      await fetchAlerts(currentPage.value, pageSize.value);
      return response.data;
    } catch (e) {
      error.value = e.message;
      console.error('Error acknowledging alert:', e);
      throw e;
    } finally {
      loading.value = false;
    }
  }

  async function resolveAlert(id, userId) {
    loading.value = true;
    error.value = null;
    try {
      const response = await api.alertsAPI.resolve(id, userId);
      await fetchAlerts(currentPage.value, pageSize.value);
      await fetchStatistics();
      return response.data;
    } catch (e) {
      error.value = e.message;
      console.error('Error resolving alert:', e);
      throw e;
    } finally {
      loading.value = false;
    }
  }

  async function archiveAlert(id) {
    loading.value = true;
    error.value = null;
    try {
      const response = await api.alertsAPI.archive(id);
      await fetchAlerts(currentPage.value, pageSize.value);
      return response.data;
    } catch (e) {
      error.value = e.message;
      console.error('Error archiving alert:', e);
      throw e;
    } finally {
      loading.value = false;
    }
  }

  async function searchAdvanced(searchParams) {
    loading.value = true;
    error.value = null;
    try {
      const response = await api.alertsAPI.searchAdvanced(searchParams);
      alerts.value = response.data.content || [];
      totalElements.value = response.data.totalElements || 0;
    } catch (e) {
      error.value = e.message;
      console.error('Error searching alerts:', e);
    } finally {
      loading.value = false;
    }
  }

  return {
    // State
    alerts,
    loading,
    error,
    totalElements,
    currentPage,
    pageSize,
    statistics,
    selectedAlert,
    filters,

    // Computed
    alertCount,
    criticalCount,
    activeCount,

    // Actions
    fetchAlerts,
    fetchStatistics,
    getAlert,
    createAlert,
    updateAlert,
    deleteAlert,
    acknowledgeAlert,
    resolveAlert,
    archiveAlert,
    searchAdvanced
  };
});
