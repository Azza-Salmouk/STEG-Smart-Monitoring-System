<template>
  <aside class="w-64 bg-gradient-to-b from-blue-900 to-blue-800 text-white shadow-xl flex flex-col">
    <!-- Logo Section -->
    <div class="p-6 border-b border-blue-700">
      <div class="flex items-center gap-3">
        <div class="w-10 h-10 bg-cyan-400 rounded-lg flex items-center justify-center font-bold text-blue-900">
          S
        </div>
        <div>
          <h1 class="font-bold text-lg">STEG</h1>
          <p class="text-xs text-blue-200">Monitoring</p>
        </div>
      </div>
    </div>

    <!-- Navigation Menu -->
    <nav class="flex-1 px-4 py-6 overflow-y-auto">
      <div v-for="section in menuSections" :key="section.label" class="mb-8">
        <p class="text-xs font-semibold text-blue-200 uppercase mb-3 px-3">{{ section.label }}</p>
        <div class="space-y-1">
          <button
            v-for="item in section.items"
            :key="item.path"
            @click="navigate(item.path)"
            :class="[
              'w-full flex items-center gap-3 px-4 py-3 rounded-lg text-sm font-medium transition-all',
              isActive(item.path)
                ? 'bg-cyan-500 text-white'
                : 'text-blue-100 hover:bg-blue-700'
            ]"
          >
            <component :is="item.icon" class="w-5 h-5" />
            <span>{{ item.label }}</span>
          </button>
        </div>
      </div>
    </nav>

    <!-- User Section -->
    <div class="p-4 border-t border-blue-700">
      <div class="flex items-center gap-3 mb-4">
        <div class="w-10 h-10 bg-cyan-400 rounded-full flex items-center justify-center text-blue-900 font-bold">
          {{ userInitial }}
        </div>
        <div class="flex-1 min-w-0">
          <p class="text-sm font-medium truncate">{{ userName }}</p>
          <p class="text-xs text-blue-200 truncate">{{ userRole }}</p>
        </div>
      </div>
      <button
        @click="logout"
        class="w-full flex items-center justify-center gap-2 px-4 py-2 bg-red-600 hover:bg-red-700 rounded-lg text-sm font-medium transition-colors"
      >
        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
        </svg>
        Déconnexion
      </button>
    </div>

    <!-- Version -->
    <div class="px-4 py-3 border-t border-blue-700 text-center text-xs text-blue-300">
      v1.0.0 - 2026
    </div>
  </aside>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'
import {
  HomeIcon,
  LayoutGridIcon,
  ZapIcon,
  UsersIcon,
  WrenchIcon,
  AlertCircleIcon,
  SettingsIcon,
  TrendingUpIcon,
  MessageSquareIcon
} from 'lucide-vue-next'

const router = useRouter()
const authStore = useAuthStore()

const emit = defineEmits(['navigate'])

const menuSections = computed(() => {
  const role = authStore.role

  if (role === 'ADMIN') {
    return [
      {
        label: 'Principal',
        items: [
          { label: 'Dashboard', path: '/admin/dashboard', icon: HomeIcon }
        ]
      },
      {
        label: 'Gestion',
        items: [
          { label: 'Utilisateurs', path: '/users', icon: UsersIcon },
          { label: 'Transformateurs', path: '/transformateurs', icon: LayoutGridIcon },
          { label: 'Postes', path: '/postes', icon: ZapIcon },
          { label: 'Mesures', path: '/measurements', icon: TrendingUpIcon },
          { label: 'Interventions', path: '/interventions', icon: WrenchIcon },
          { label: 'Réclamations', path: '/complaints', icon: AlertCircleIcon },
          { label: 'Alertes', path: '/alerts', icon: AlertCircleIcon }
        ]
      },
      {
        label: 'Intelligence',
        items: [
          { label: 'Assistant IA', path: '/chatbot', icon: MessageSquareIcon }
        ]
      },
      {
        label: 'Système',
        items: [
          { label: 'Paramètres', path: '/settings', icon: SettingsIcon }
        ]
      }
    ]
  }

  if (role === 'SUPERVISOR') {
    return [
      {
        label: 'Principal',
        items: [
          { label: 'Dashboard', path: '/supervisor/dashboard', icon: HomeIcon }
        ]
      },
      {
        label: 'Gestion',
        items: [
          { label: 'Transformateurs', path: '/transformateurs', icon: LayoutGridIcon },
          { label: 'Postes', path: '/postes', icon: ZapIcon },
          { label: 'Mesures', path: '/measurements', icon: TrendingUpIcon },
          { label: 'Interventions', path: '/interventions', icon: WrenchIcon },
          { label: 'Réclamations', path: '/complaints', icon: AlertCircleIcon },
          { label: 'Alertes', path: '/alerts', icon: AlertCircleIcon }
        ]
      },
      {
        label: 'Intelligence',
        items: [
          { label: 'Assistant IA', path: '/chatbot', icon: MessageSquareIcon }
        ]
      },
      {
        label: 'Système',
        items: [
          { label: 'Paramètres', path: '/settings', icon: SettingsIcon }
        ]
      }
    ]
  }

  if (role === 'AGENT') {
    return [
      {
        label: 'Principal',
        items: [
          { label: 'Dashboard', path: '/agent/dashboard', icon: HomeIcon }
        ]
      },
      {
        label: 'Opérations',
        items: [
          { label: 'Transformateurs', path: '/transformateurs', icon: LayoutGridIcon },
          { label: 'Mesures', path: '/measurements', icon: TrendingUpIcon },
          { label: 'Interventions', path: '/interventions', icon: WrenchIcon },
          { label: 'Réclamations', path: '/complaints', icon: AlertCircleIcon },
          { label: 'Alertes', path: '/alerts', icon: AlertCircleIcon }
        ]
      },
      {
        label: 'Intelligence',
        items: [
          { label: 'Assistant IA', path: '/chatbot', icon: MessageSquareIcon }
        ]
      },
      {
        label: 'Système',
        items: [
          { label: 'Paramètres', path: '/settings', icon: SettingsIcon }
        ]
      }
    ]
  }

  if (role === 'SERVICE_CLIENT') {
    return [
      {
        label: 'Principal',
        items: [
          { label: 'Dashboard', path: '/serviceclient/dashboard', icon: HomeIcon }
        ]
      },
      {
        label: 'Gestion',
        items: [
          { label: 'Réclamations', path: '/complaints', icon: AlertCircleIcon }
        ]
      },
      {
        label: 'Intelligence',
        items: [
          { label: 'Assistant IA', path: '/chatbot', icon: MessageSquareIcon }
        ]
      },
      {
        label: 'Système',
        items: [
          { label: 'Paramètres', path: '/settings', icon: SettingsIcon }
        ]
      }
    ]
  }

  return []
})

const userName = computed(() => authStore.user?.username || 'User')
const userRole = computed(() => {
  const roles = {
    ADMIN: 'Administrateur',
    SUPERVISOR: 'Superviseur',
    AGENT: 'Agent',
    SERVICE_CLIENT: 'Service Client'
  }
  return roles[authStore.role] || authStore.role
})

const userInitial = computed(() => userName.value.charAt(0).toUpperCase())

function isActive(path) {
  return router.currentRoute.value.path === path
}

function navigate(path) {
  router.push(path)
  emit('navigate', path)
}

async function logout() {
  await authStore.logout()
  router.push('/login')
}
</script>

<style scoped>
aside::-webkit-scrollbar {
  width: 6px;
}

aside::-webkit-scrollbar-track {
  background: transparent;
}

aside::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255, 0.2);
  border-radius: 3px;
}

aside::-webkit-scrollbar-thumb:hover {
  background: rgba(255, 255, 255, 0.3);
}
</style>
