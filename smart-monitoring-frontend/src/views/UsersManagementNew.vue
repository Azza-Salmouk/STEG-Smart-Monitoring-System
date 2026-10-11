<template>
  <div class="space-y-6">
    <!-- Header -->
    <div class="flex items-center justify-between">
      <div>
        <h1 class="text-3xl font-bold text-gray-900">Gestion des Utilisateurs</h1>
        <p class="text-gray-500 mt-1">Gérer les utilisateurs du système</p>
      </div>
      <Button
        v-if="canCreate"
        variant="primary"
        size="lg"
        @click="showCreateForm = true"
      >
        ➕ Ajouter Utilisateur
      </Button>
    </div>

    <!-- Data Loader Wrapper -->
    <DataLoader
      :loading="loading"
      :error="error"
      error-title="Impossible de charger les utilisateurs"
      @retry="loadUsers"
    >
      <!-- Stats Cards -->
      <div class="grid grid-cols-1 md:grid-cols-4 gap-4 mb-6">
        <KPICard
          title="Total Utilisateurs"
          :value="stats.total"
          color="bg-blue-500"
        />
        <KPICard
          title="Administrateurs"
          :value="stats.admins"
          color="bg-red-500"
        />
        <KPICard
          title="Superviseurs"
          :value="stats.supervisors"
          color="bg-yellow-500"
        />
        <KPICard
          title="Agents"
          :value="stats.agents"
          color="bg-green-500"
        />
      </div>

      <!-- Search & Filters -->
      <Card class="mb-6">
        <template #header>
          <h2 class="text-lg font-semibold">Recherche & Filtres</h2>
        </template>
        <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Recherche</label>
            <input
              v-model="filters.username"
              @input="handleSearch"
              type="text"
              placeholder="Nom..."
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Email</label>
            <input
              v-model="filters.email"
              @input="handleSearch"
              type="email"
              placeholder="Email..."
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Rôle</label>
            <select
              v-model="filters.role"
              @change="handleSearch"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="">Tous les rôles</option>
              <option value="ADMIN">Administrateur</option>
              <option value="SUPERVISOR">Superviseur</option>
              <option value="AGENT">Agent</option>
              <option value="SERVICE_CLIENT">Service Client</option>
            </select>
          </div>
          <div class="flex items-end">
            <Button
              variant="secondary"
              size="md"
              @click="resetFilters"
              class="w-full"
            >
              Réinitialiser
            </Button>
          </div>
        </div>
      </Card>

      <!-- Table -->
      <Card>
        <template #header>
          <h2 class="text-lg font-semibold">Liste des Utilisateurs</h2>
        </template>
        
        <div v-if="users.length === 0" class="text-center py-12">
          <p class="text-gray-500 text-lg">Aucun utilisateur trouvé</p>
        </div>

        <div v-else class="overflow-x-auto">
          <table class="min-w-full">
            <thead class="bg-gray-50 border-b">
              <tr>
                <th class="px-6 py-3 text-left text-sm font-semibold text-gray-700">Nom</th>
                <th class="px-6 py-3 text-left text-sm font-semibold text-gray-700">Email</th>
                <th class="px-6 py-3 text-left text-sm font-semibold text-gray-700">Rôle</th>
                <th class="px-6 py-3 text-left text-sm font-semibold text-gray-700">Statut</th>
                <th class="px-6 py-3 text-left text-sm font-semibold text-gray-700">Date Création</th>
                <th class="px-6 py-3 text-right text-sm font-semibold text-gray-700">Actions</th>
              </tr>
            </thead>
            <tbody class="divide-y">
              <tr v-for="user in users" :key="user.id" class="hover:bg-gray-50">
                <td class="px-6 py-4 text-gray-900 font-medium">{{ user.username }}</td>
                <td class="px-6 py-4 text-gray-600">{{ user.email }}</td>
                <td class="px-6 py-4">
                  <span
                    :class="[
                      'px-3 py-1 rounded-full text-sm font-medium',
                      user.role === 'ADMIN' ? 'bg-red-100 text-red-800' :
                      user.role === 'SUPERVISOR' ? 'bg-yellow-100 text-yellow-800' :
                      user.role === 'AGENT' ? 'bg-green-100 text-green-800' :
                      'bg-blue-100 text-blue-800'
                    ]"
                  >
                    {{ getRoleLabel(user.role) }}
                  </span>
                </td>
                <td class="px-6 py-4">
                  <span
                    :class="[
                      'px-3 py-1 rounded-full text-sm font-medium',
                      user.isActive ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'
                    ]"
                  >
                    {{ user.isActive ? 'Actif' : 'Inactif' }}
                  </span>
                </td>
                <td class="px-6 py-4 text-gray-600">{{ formatDate(user.createdAt) }}</td>
                <td class="px-6 py-4 text-right space-x-2">
                  <Button
                    v-if="canEdit"
                    variant="secondary"
                    size="sm"
                    @click="editUser(user)"
                  >
                    ✏️ Modifier
                  </Button>
                  <Button
                    v-if="canDelete"
                    variant="danger"
                    size="sm"
                    @click="deleteUser(user)"
                  >
                    🗑️ Supprimer
                  </Button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- Pagination -->
        <template #footer v-if="totalPages > 1">
          <div class="flex items-center justify-between">
            <p class="text-sm text-gray-600">
              Page {{ currentPage + 1 }} sur {{ totalPages }} ({{ totalElements }} total)
            </p>
            <div class="flex gap-2">
              <Button
                variant="secondary"
                size="sm"
                :disabled="currentPage === 0"
                @click="previousPage"
              >
                ← Précédent
              </Button>
              <Button
                variant="secondary"
                size="sm"
                :disabled="currentPage >= totalPages - 1"
                @click="nextPage"
              >
                Suivant →
              </Button>
            </div>
          </div>
        </template>
      </Card>
    </DataLoader>

    <!-- Create/Edit Modal -->
    <div v-if="showCreateForm" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <Card class="max-w-md w-full mx-4">
        <template #header>
          <div class="flex items-center justify-between">
            <h2 class="text-xl font-bold">{{ editingUser ? 'Modifier Utilisateur' : 'Ajouter Utilisateur' }}</h2>
            <button @click="closeForm" class="text-gray-500 hover:text-gray-700">✕</button>
          </div>
        </template>

        <form @submit.prevent="saveUser" class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Nom *</label>
            <input
              v-model="formData.username"
              required
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Prénom *</label>
            <input
              v-model="formData.prenom"
              required
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Email *</label>
            <input
              v-model="formData.email"
              type="email"
              required
              :disabled="!!editingUser"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:bg-gray-100"
            />
          </div>
          <div v-if="!editingUser">
            <label class="block text-sm font-medium text-gray-700 mb-2">Mot de passe *</label>
            <input
              v-model="formData.password"
              type="password"
              required
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Rôle *</label>
            <select
              v-model="formData.role"
              required
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="">Sélectionner...</option>
              <option value="ADMIN">Administrateur</option>
              <option value="SUPERVISOR">Superviseur</option>
              <option value="AGENT">Agent</option>
            </select>
          </div>

          <div class="flex gap-2 pt-4">
            <Button
              type="submit"
              variant="primary"
              size="md"
              :disabled="savingUser"
              class="flex-1"
            >
              {{ savingUser ? '⏳ Enregistrement...' : '✅ Enregistrer' }}
            </Button>
            <Button
              type="button"
              variant="secondary"
              size="md"
              @click="closeForm"
              class="flex-1"
            >
              Annuler
            </Button>
          </div>
        </form>
      </Card>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useAuthStore } from '@/stores/authStore'
import { usersAPI } from '@/services/api'
import { formatError } from '@/services/error-handler'
import Card from '@/components/Card.vue'
import Button from '@/components/Button.vue'
import DataLoader from '@/components/DataLoader.vue'
import KPICard from '@/components/KPICard.vue'

const authStore = useAuthStore()

const loading = ref(false)
const error = ref(null)
const users = ref([])
const currentPage = ref(0)
const totalPages = ref(0)
const totalElements = ref(0)

const showCreateForm = ref(false)
const editingUser = ref(null)
const savingUser = ref(false)

const filters = ref({
  username: '',
  email: '',
  role: '',
})

const formData = ref({
  username: '',
  email: '',
  password: '',
  role: 'AGENT',
})

const stats = ref({
  total: 0,
  admins: 0,
  supervisors: 0,
  agents: 0,
})

const canCreate = computed(() => authStore.role === 'ADMIN')
const canEdit = computed(() => authStore.role === 'ADMIN')
const canDelete = computed(() => authStore.role === 'ADMIN')

let searchTimeout

function getRoleLabel(role) {
  const labels = {
    ADMIN: 'Administrateur',
    SUPERVISOR: 'Superviseur',
    AGENT: 'Agent',
    SERVICE_CLIENT: 'Service Client'
  }
  return labels[role] || role
}

function formatDate(dateString) {
  if (!dateString) return '-'
  return new Date(dateString).toLocaleDateString('fr-FR')
}

async function loadUsers() {
  loading.value = true
  error.value = null

  try {
    const hasFilters = Object.values(filters.value).some(Boolean)
    const response = hasFilters
      ? await usersAPI.search(filters.value, currentPage.value, 10)
      : await usersAPI.getAll(currentPage.value, 10)
    users.value = response.data.content
    totalPages.value = response.data.totalPages
    totalElements.value = response.data.totalElements
    currentPage.value = response.data.number || 0

    // Calculate stats
    stats.value = {
      total: users.value.length,
      admins: users.value.filter(u => u.role === 'ADMIN').length,
      supervisors: users.value.filter(u => u.role === 'SUPERVISOR').length,
      agents: users.value.filter(u => u.role === 'AGENT').length,
    }
  } catch (err) {
    error.value = formatError(err).message
    console.error('Erreur chargement utilisateurs:', err)
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  clearTimeout(searchTimeout)
  searchTimeout = setTimeout(() => {
    currentPage.value = 0
    loadUsers()
  }, 300)
}

function resetFilters() {
  filters.value = { username: '', email: '', role: '' }
  currentPage.value = 0
  loadUsers()
}

function nextPage() {
  if (currentPage.value < totalPages.value - 1) {
    currentPage.value++
    loadUsers()
  }
}

function previousPage() {
  if (currentPage.value > 0) {
    currentPage.value--
    loadUsers()
  }
}

function editUser(user) {
  editingUser.value = user
  formData.value = {
    username: user.username,
    email: user.email,
    password: '',
    role: user.role,
  }
  showCreateForm.value = true
}

async function saveUser() {
  savingUser.value = true
  try {
    const data = { ...formData.value }
    delete data.prenom
    if (editingUser.value && !data.password) {
      delete data.password
    }
    
    if (editingUser.value) {
      await usersAPI.update(editingUser.value.id, data)
    } else {
      await usersAPI.create(data)
    }
    closeForm()
    await loadUsers()
  } catch (err) {
    error.value = formatError(err).message
    console.error('Erreur sauvegarde utilisateur:', err)
  } finally {
    savingUser.value = false
  }
}

async function deleteUser(user) {
  if (!confirm(`Êtes-vous sûr de vouloir supprimer "${user.nom} ${user.prenom}" ?`)) return

  try {
    await usersAPI.delete(user.id)
    await loadUsers()
  } catch (err) {
    error.value = formatError(err).message
    console.error('Erreur suppression utilisateur:', err)
  }
}

function closeForm() {
  showCreateForm.value = false
  editingUser.value = null
  formData.value = {
    username: '',
    email: '',
    password: '',
    role: 'AGENT',
  }
}

onMounted(() => {
  loadUsers()
})
</script>

<style scoped></style>
