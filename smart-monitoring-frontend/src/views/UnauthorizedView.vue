<template>
  <div class="min-h-screen bg-gradient-to-br from-gray-900 via-gray-800 to-gray-900 flex items-center justify-center px-4">
    <div class="text-center max-w-md">
      <div class="text-6xl font-bold text-red-500 mb-4">403</div>
      <h1 class="text-3xl font-bold text-white mb-2">Accès refusé</h1>
      <p class="text-gray-400 mb-8">Vous n'avez pas les permissions nécessaires pour accéder à cette page.</p>
      
      <div class="space-y-4">
        <RouterLink
          to="/"
          class="inline-block bg-primary-600 hover:bg-primary-700 text-white font-bold py-2 px-6 rounded-lg transition-colors"
        >
          Retourner à l'accueil
        </RouterLink>
        <button
          @click="handleLogout"
          class="block w-full text-gray-400 hover:text-gray-200 transition-colors"
        >
          Se déconnecter
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { RouterLink, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'

const router = useRouter()
const authStore = useAuthStore()

const handleLogout = () => {
  authStore.logout()
  router.push('/login')
}
</script>

<style scoped>
</style>
