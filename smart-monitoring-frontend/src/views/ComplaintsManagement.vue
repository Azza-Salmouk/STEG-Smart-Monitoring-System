<template>
  <div class="space-y-6">
    <!-- Header -->
    <div class="flex items-center justify-between">
      <div>
        <h1 class="text-3xl font-bold text-gray-900">Gestion des Réclamations</h1>
        <p class="text-gray-500 mt-1">Superviser les réclamations clients STEG</p>
      </div>
      <button
        v-if="canCreate"
        @click="showCreateForm = true"
        class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
      >
        ➕ Nouvelle Réclamation
      </button>
    </div>

    <!-- Stats Cards -->
    <div v-if="statistics" class="grid grid-cols-1 md:grid-cols-5 gap-4">
      <div class="bg-white p-4 rounded-lg shadow">
        <p class="text-sm text-gray-600">Total</p>
        <p class="text-2xl font-bold text-gray-900">{{ statistics.totalComplaints }}</p>
      </div>
      <div class="bg-white p-4 rounded-lg shadow">
        <p class="text-sm text-gray-600">Nouvelles</p>
        <p class="text-2xl font-bold text-blue-600">{{ statistics.nouvelles }}</p>
      </div>
      <div class="bg-white p-4 rounded-lg shadow">
        <p class="text-sm text-gray-600">En Traitement</p>
        <p class="text-2xl font-bold text-yellow-600">{{ statistics.enTraitement }}</p>
      </div>
      <div class="bg-white p-4 rounded-lg shadow">
        <p class="text-sm text-gray-600">Critiques</p>
        <p class="text-2xl font-bold text-red-600">{{ statistics.critiques }}</p>
      </div>
      <div class="bg-white p-4 rounded-lg shadow">
        <p class="text-sm text-gray-600">Résolution</p>
        <p class="text-2xl font-bold text-green-600">{{ (statistics.resolutionRate || 0).toFixed(1) }}%</p>
      </div>
    </div>

    <!-- Search & Filters -->
    <div class="bg-white p-4 rounded-lg shadow">
      <h2 class="text-lg font-semibold mb-4">Recherche & Filtres</h2>
      <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Statut</label>
          <select
            v-model="filters.statut"
            @change="performSearch"
            class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value="">Tous</option>
            <option value="NOUVELLE">Nouvelle</option>
            <option value="EN_ANALYSE">En analyse</option>
            <option value="ASSIGNEE">Assignée</option>
            <option value="EN_TRAITEMENT">En traitement</option>
            <option value="RESOLUE">Résolue</option>
            <option value="FERMEE">Fermée</option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Priorité</label>
          <select
            v-model="filters.priorite"
            @change="performSearch"
            class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value="">Tous</option>
            <option value="LOW">Faible</option>
            <option value="MEDIUM">Moyenne</option>
            <option value="HIGH">Haute</option>
            <option value="CRITICAL">Critique</option>
          </select>
        </div>
        <div class="flex items-end">
          <button
            @click="resetFilters"
            class="px-4 py-2 bg-gray-200 text-gray-700 rounded-lg hover:bg-gray-300 w-full"
          >
            Réinitialiser
          </button>
        </div>
      </div>
    </div>

    <!-- Table -->
    <div class="bg-white rounded-lg shadow">
      <div v-if="loading" class="p-8 text-center">
        <p class="text-gray-500">Chargement...</p>
      </div>
      <ComplaintTable
        v-else
        :complaints="complaints"
        :loading="loading"
        :current-page="currentPage"
        :total-pages="totalPages"
        :total-elements="totalElements"
        @prev="previousPage"
        @next="nextPage"
      >
        <template #actions="{ complaint }">
          <div class="flex gap-2">
            <button
              @click="editComplaint(complaint)"
              class="text-blue-600 hover:text-blue-800 text-sm"
            >
              ✏️
            </button>
            <button
              v-if="canDelete"
              @click="deleteComplaint(complaint.id)"
              class="text-red-600 hover:text-red-800 text-sm"
            >
              🗑️
            </button>
          </div>
        </template>
      </ComplaintTable>
    </div>

    <!-- Create/Edit Modal -->
    <ComplaintFormModal
      v-if="showCreateForm || showEditForm"
      :complaint="editingComplaint"
      :is-edit="showEditForm"
      @save="saveComplaint"
      @close="closeForm"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useComplaintStore } from '@/stores/useComplaintStore'
import { useAuthStore } from '@/stores/authStore'
import ComplaintTable from '@/components/complaint/ComplaintTable.vue'
import ComplaintFormModal from '@/components/complaint/ComplaintFormModal.vue'

const complaintStore = useComplaintStore()
const authStore = useAuthStore()

// State
const showCreateForm = ref(false)
const showEditForm = ref(false)
const editingComplaint = ref(null)

// Computed
const complaints = computed(() => complaintStore.complaints)
const loading = computed(() => complaintStore.loading)
const statistics = computed(() => complaintStore.statistics)
const currentPage = computed(() => complaintStore.currentPage)
const totalPages = computed(() => complaintStore.totalPages)
const totalElements = computed(() => complaintStore.totalElements)
const pageSize = computed(() => complaintStore.pageSize)

const filters = ref({
  nomClient: '',
  statut: '',
  priorite: '',
  categorie: '',
})

const canCreate = computed(() => ['ADMIN', 'SUPERVISOR', 'SERVICE_CLIENT'].includes(authStore.user?.role))
const canDelete = computed(() => ['ADMIN', 'SUPERVISOR'].includes(authStore.user?.role))

// Methods
onMounted(() => {
  loadComplaints()
  loadStatistics()
})

const loadComplaints = async () => {
  await complaintStore.fetchComplaints(0, pageSize.value)
}

const loadStatistics = async () => {
  await complaintStore.fetchStatistics()
}

const performSearch = async () => {
  complaintStore.searchFilters = { ...filters.value }
  await complaintStore.searchComplaints(0)
}

const resetFilters = () => {
  filters.value = {
    nomClient: '',
    statut: '',
    priorite: '',
    categorie: '',
  }
  loadComplaints()
}

const previousPage = async () => {
  if (currentPage.value > 0) {
    await complaintStore.fetchComplaints(currentPage.value - 1, pageSize.value)
  }
}

const nextPage = async () => {
  if (currentPage.value < totalPages.value - 1) {
    await complaintStore.fetchComplaints(currentPage.value + 1, pageSize.value)
  }
}

const editComplaint = (complaint) => {
  editingComplaint.value = { ...complaint }
  showEditForm.value = true
}

const saveComplaint = async (data) => {
  try {
    if (showEditForm.value) {
      await complaintStore.updateComplaint(editingComplaint.value.id, data)
    } else {
      await complaintStore.createComplaint(data)
    }
    closeForm()
    await loadStatistics()
  } catch (err) {
    console.error('Erreur:', err)
  }
}

const deleteComplaint = async (id) => {
  if (confirm('Êtes-vous sûr ?')) {
    try {
      await complaintStore.deleteComplaint(id)
      await loadStatistics()
    } catch (err) {
      console.error('Erreur:', err)
    }
  }
}

const closeForm = () => {
  showCreateForm.value = false
  showEditForm.value = false
  editingComplaint.value = null
}
</script>
