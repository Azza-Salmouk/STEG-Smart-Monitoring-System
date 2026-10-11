<template>
  <div class="space-y-6">
    <!-- En-tête -->
    <div class="bg-white rounded-lg shadow p-6">
      <h1 class="text-3xl font-bold text-gray-900">Bienvenue, {{ authStore.username }}</h1>
      <p class="text-gray-600 mt-2">Tableau de bord Administrateur</p>
    </div>

    <!-- Grille de stats -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
      <StatCard
        title="Utilisateurs"
        :value="String(statistics.totalUsers)"
        icon="users"
        color="blue"
      />
      <StatCard
        title="Postes"
        :value="String(statistics.totalPostes)"
        icon="chart"
        color="green"
      />
      <StatCard
        title="Transformateurs"
        :value="String(statistics.totalTransformateurs)"
        icon="database"
        color="purple"
      />
    </div>

    <!-- Infos utilisateur -->
    <div class="bg-white rounded-lg shadow p-6">
      <h2 class="text-xl font-bold text-gray-900 mb-4">Informations de connexion</h2>
      <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
        <InfoItem label="ID" :value="authStore.user" />
        <InfoItem label="Nom" :value="authStore.username" />
        <InfoItem label="Email" :value="authStore.email" />
        <InfoItem label="Rôle" :value="getRoleName(authStore.role)" />
      </div>
    </div>

    <!-- Token décodé (dev) -->
    <div v-if="decodedToken" class="bg-yellow-50 border border-yellow-200 rounded-lg p-6">
      <h2 class="text-xl font-bold text-gray-900 mb-4">Token décodé (Développement)</h2>
      <pre class="bg-gray-900 text-gray-100 p-4 rounded overflow-auto text-xs">{{ JSON.stringify(decodedToken, null, 2) }}</pre>
    </div>
  </div>
</template>

<script setup>
import { computed, ref, onMounted } from 'vue'
import { useAuthStore } from '@/stores/authStore'
import { decodeToken } from '@/utils/token'
import StatCard from '@/components/StatCard.vue'
import InfoItem from '@/components/InfoItem.vue'
import { usersAPI, postesAPI, transformateursAPI } from '@/services/api'

const authStore = useAuthStore()

const statistics = ref({
  totalUsers: 0,
  totalPostes: 0,
  totalTransformateurs: 0,
})

const decodedToken = computed(() => {
  return authStore.token ? decodeToken(authStore.token) : null
})

onMounted(async () => {
  await loadStatistics()
})

async function loadStatistics() {
  try {
    const [usersRes, postesRes, transformateursRes] = await Promise.all([
      usersAPI.getAll(0, 1),
      postesAPI.getAll(0, 1),
      transformateursAPI.getAll(0, 1),
    ])
    
    statistics.value = {
      totalUsers: usersRes.data?.totalElements || 0,
      totalPostes: postesRes.data?.totalElements || 0,
      totalTransformateurs: transformateursRes.data?.totalElements || 0,
    }
  } catch (err) {
    console.error('Error loading dashboard statistics', err)
  }
}

function getRoleName(role) {
  const roles = {
    'ADMIN': 'Administrateur',
    'SUPERVISOR': 'Superviseur',
    'AGENT': 'Agent'
  }
  return roles[role] || role
}
</script>

<style scoped>
</style>
