<template>
  <header class="bg-white border-b border-gray-200 shadow-sm">
    <div class="px-6 py-4 flex items-center justify-between">
      <!-- Left: Title -->
      <div>
        <h2 class="text-2xl font-bold text-gray-900">{{ pageTitle }}</h2>
        <p class="text-sm text-gray-500 mt-1">{{ pageDescription }}</p>
      </div>

      <!-- Right: Actions -->
      <div class="flex items-center gap-4">
        <!-- Notifications -->
        <div class="relative">
          <button 
            @click="notificationsOpen = !notificationsOpen"
            class="relative p-2 text-gray-600 hover:bg-gray-100 rounded-lg transition-colors"
            :class="{ 'bg-gray-100': notificationsOpen }"
          >
            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9" />
            </svg>
            <span v-if="unreadCount > 0" class="absolute top-1 right-1 w-2 h-2 bg-red-500 rounded-full"></span>
          </button>

          <!-- Notifications Dropdown -->
          <div
            v-if="notificationsOpen"
            class="absolute right-0 mt-2 w-80 bg-white rounded-lg shadow-lg border border-gray-200 z-50 max-h-96 overflow-y-auto"
            @click.away="notificationsOpen = false"
          >
            <div class="px-4 py-3 border-b border-gray-200">
              <h3 class="font-semibold text-gray-900">Notifications</h3>
            </div>
            
            <div v-if="notifications.length === 0" class="px-4 py-8 text-center">
              <p class="text-gray-500">Aucune notification</p>
            </div>
            
            <div v-else class="divide-y divide-gray-200">
              <div 
                v-for="notif in notifications"
                :key="notif.id"
                class="px-4 py-3 hover:bg-gray-50 cursor-pointer transition-colors"
                @click="handleNotificationClick(notif)"
              >
                <p class="text-sm font-medium text-gray-900">{{ notif.title }}</p>
                <p class="text-sm text-gray-600 mt-1">{{ notif.message }}</p>
                <p class="text-xs text-gray-400 mt-1">{{ formatTime(notif.timestamp) }}</p>
              </div>
            </div>
            
            <div v-if="notifications.length > 0" class="px-4 py-3 border-t border-gray-200">
              <button 
                @click="clearNotifications"
                class="w-full text-sm text-primary-600 hover:text-primary-700 font-medium"
              >
                Effacer tout
              </button>
            </div>
          </div>
        </div>

        <!-- Profile Dropdown -->
        <div class="relative">
          <button
            @click="profileOpen = !profileOpen"
            class="flex items-center gap-2 px-4 py-2 text-gray-700 hover:bg-gray-100 rounded-lg transition-colors"
          >
            <div class="w-8 h-8 bg-primary-600 rounded-full flex items-center justify-center text-white text-sm font-bold">
              {{ userInitial }}
            </div>
            <span class="text-sm font-medium">{{ userName }}</span>
            <svg
              :class="['w-4 h-4 transition-transform', profileOpen && 'rotate-180']"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 14l-7 7m0 0l-7-7m7 7V3" />
            </svg>
          </button>

          <!-- Dropdown Menu -->
          <div
            v-if="profileOpen"
            class="absolute right-0 mt-2 w-48 bg-white rounded-lg shadow-lg border border-gray-200 z-50"
            @click.away="profileOpen = false"
          >
            <button
              @click="goToProfile"
              class="w-full text-left px-4 py-2 text-gray-700 hover:bg-gray-100 rounded-t-lg"
            >
              Mon Profil
            </button>
            <button
              @click="goToSettings"
              class="w-full text-left px-4 py-2 text-gray-700 hover:bg-gray-100"
            >
              Paramètres
            </button>
            <div class="border-t border-gray-200"></div>
            <button
              @click="logout"
              class="w-full text-left px-4 py-2 text-red-600 hover:bg-red-50 rounded-b-lg font-medium"
            >
              Déconnexion
            </button>
          </div>
        </div>
      </div>
    </div>
  </header>
</template>

<script setup>
import { computed, ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()
const profileOpen = ref(false)
const notificationsOpen = ref(false)
const notifications = ref([])

// Load notifications on mount
onMounted(() => {
  loadNotifications()
})

const pageTitle = computed(() => {
  const titles = {
    'AdminDashboard': 'Dashboard',
    'SupervisorDashboard': 'Dashboard',
    'AgentDashboard': 'Dashboard',
    'ServiceClientDashboard': 'Dashboard',
    'PostesManagement': 'Gestion des Postes',
    'TransformateursManagement': 'Gestion des Transformateurs',
    'UsersManagement': 'Gestion des Utilisateurs',
    'MeasurementsManagement': 'Gestion des Mesures',
    'InterventionsManagement': 'Gestion des Interventions',
    'ComplaintsManagement': 'Gestion des Réclamations',
    'AlertsManagement': 'Gestion des Alertes',
    'Chatbot': 'Assistant IA',
    'Settings': 'Paramètres',
    'Profile': 'Mon Profil'
  }
  return titles[route.name] || ''
})

const pageDescription = computed(() => {
  const descriptions = {
    'AdminDashboard': 'Bienvenue administrateur',
    'SupervisorDashboard': 'Bienvenue superviseur',
    'AgentDashboard': 'Bienvenue agent',
    'ServiceClientDashboard': 'Bienvenue',
    'PostesManagement': 'Gérez les postes électriques',
    'TransformateursManagement': 'Gérez les transformateurs',
    'UsersManagement': 'Gérez les utilisateurs du système',
    'MeasurementsManagement': 'Consultez les mesures',
    'InterventionsManagement': 'Gérez les interventions',
    'ComplaintsManagement': 'Gérez les réclamations',
    'AlertsManagement': 'Gérez les alertes',
    'Chatbot': 'Posez vos questions à l\'Assistant IA',
    'Settings': 'Gérez vos préférences',
    'Profile': 'Consultez votre profil'
  }
  return descriptions[route.name] || ''
})

const userName = computed(() => authStore.user?.username || 'User')

const userInitial = computed(() => userName.value.charAt(0).toUpperCase())

const unreadCount = computed(() => notifications.value.length)

function loadNotifications() {
  // Mock notifications - in a real app, fetch from backend
  // This gives users demo notifications to see the feature works
  const mockNotifications = [
    {
      id: 1,
      title: 'Nouvelle réclamation',
      message: 'Une nouvelle réclamation a été soumise',
      timestamp: new Date(Date.now() - 5 * 60000),
      type: 'info'
    },
    {
      id: 2,
      title: 'Intervention assignée',
      message: 'Une nouvelle intervention vous a été assignée',
      timestamp: new Date(Date.now() - 15 * 60000),
      type: 'info'
    }
  ]
  notifications.value = mockNotifications
}

function formatTime(date) {
  const now = new Date()
  const diff = now - date
  const minutes = Math.floor(diff / 60000)
  const hours = Math.floor(diff / 3600000)
  const days = Math.floor(diff / 86400000)
  
  if (minutes < 1) return 'À l\'instant'
  if (minutes < 60) return `Il y a ${minutes}m`
  if (hours < 24) return `Il y a ${hours}h`
  if (days < 7) return `Il y a ${days}j`
  return date.toLocaleDateString('fr-FR')
}

function handleNotificationClick(notif) {
  // Route based on notification type
  notificationsOpen.value = false
}

function clearNotifications() {
  notifications.value = []
  notificationsOpen.value = false
}

function goToProfile() {
  profileOpen.value = false
  router.push('/profile')
}

function goToSettings() {
  profileOpen.value = false
  router.push('/settings')
}

async function logout() {
  profileOpen.value = false
  await authStore.logout()
  router.push('/login')
}
</script>

<style scoped></style>
