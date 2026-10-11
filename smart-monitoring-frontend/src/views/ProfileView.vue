<template>
  <div class="min-h-screen bg-gray-50 py-8 px-4 sm:px-6 lg:px-8">
    <div class="max-w-3xl mx-auto">
      <!-- Header -->
      <div class="mb-8">
        <h1 class="text-3xl font-bold text-gray-900">Mon Profil</h1>
        <p class="text-gray-600 mt-2">Gérez vos informations personnelles</p>
      </div>

      <!-- Profile Card -->
      <div class="bg-white rounded-lg shadow-md p-8">
        <!-- User Avatar and Name -->
        <div class="flex items-center gap-4 mb-8 pb-8 border-b border-gray-200">
          <div class="w-20 h-20 bg-primary-600 rounded-full flex items-center justify-center text-white text-3xl font-bold">
            {{ userInitial }}
          </div>
          <div>
            <h2 class="text-2xl font-bold text-gray-900">{{ user.username }}</h2>
            <p class="text-gray-600">{{ user.email }}</p>
            <span class="inline-block mt-2 px-3 py-1 bg-primary-100 text-primary-800 rounded-full text-sm font-medium">
              {{ roleLabel }}
            </span>
          </div>
        </div>

        <!-- Profile Information -->
        <div class="space-y-6">
          <div class="grid grid-cols-1 sm:grid-cols-2 gap-6">
            <!-- Username -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Nom d'utilisateur</label>
              <p class="text-gray-900 bg-gray-50 rounded px-4 py-2">{{ user.username }}</p>
            </div>

            <!-- Email -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Adresse Email</label>
              <p class="text-gray-900 bg-gray-50 rounded px-4 py-2">{{ user.email }}</p>
            </div>

            <!-- Role -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Rôle</label>
              <p class="text-gray-900 bg-gray-50 rounded px-4 py-2">{{ roleLabel }}</p>
            </div>

            <!-- User ID -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">ID Utilisateur</label>
              <p class="text-gray-900 bg-gray-50 rounded px-4 py-2">{{ user.id }}</p>
            </div>
          </div>

          <!-- Account Status -->
          <div class="pt-4 border-t border-gray-200">
            <h3 class="text-lg font-semibold text-gray-900 mb-4">État du compte</h3>
            <div class="flex items-center gap-4">
              <div class="flex-1">
                <p class="text-sm text-gray-600">Compte actif</p>
                <p class="text-gray-900 font-medium">
                  <svg class="inline w-5 h-5 text-green-600 mr-2" fill="currentColor" viewBox="0 0 20 20">
                    <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd" />
                  </svg>
                  Actif
                </p>
              </div>
            </div>
          </div>
        </div>

        <!-- Action Buttons -->
        <div class="mt-8 pt-8 border-t border-gray-200 flex gap-4">
          <button
            @click="goToSettings"
            class="px-6 py-2 bg-primary-600 text-white rounded-lg hover:bg-primary-700 transition-colors font-medium"
          >
            Paramètres du compte
          </button>
          <button
            @click="goBack"
            class="px-6 py-2 bg-gray-200 text-gray-900 rounded-lg hover:bg-gray-300 transition-colors font-medium"
          >
            Retour
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'

const router = useRouter()
const authStore = useAuthStore()

const user = computed(() => authStore.user || {
  id: 'N/A',
  username: 'Utilisateur',
  email: 'user@example.com'
})

const userInitial = computed(() => user.value.username?.charAt(0).toUpperCase() || 'U')

const roleLabel = computed(() => {
  const roles = {
    'ADMIN': 'Administrateur',
    'SUPERVISOR': 'Superviseur',
    'AGENT': 'Agent',
    'SERVICE_CLIENT': 'Service Client'
  }
  return roles[authStore.role] || authStore.role
})

function goToSettings() {
  router.push('/settings')
}

function goBack() {
  router.back()
}
</script>

<style scoped></style>
