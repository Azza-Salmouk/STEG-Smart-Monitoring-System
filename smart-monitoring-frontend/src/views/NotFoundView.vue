<template>
  <div class="min-h-screen bg-gradient-to-br from-gray-900 via-gray-800 to-gray-900 flex items-center justify-center px-4">
    <div class="text-center max-w-md">
      <div class="text-6xl font-bold text-blue-500 mb-4">404</div>
      <h1 class="text-3xl font-bold text-white mb-2">Page non trouvée</h1>
      <p class="text-gray-400 mb-8">La page que vous recherchez n'existe pas ou a été supprimée.</p>
      
      <RouterLink
        to="/"
        class="inline-block bg-primary-600 hover:bg-primary-700 text-white font-bold py-2 px-6 rounded-lg transition-colors"
      >
        Retourner à l'accueil
      </RouterLink>
    </div>
  </div>
</template>

<script setup>
import { RouterLink } from 'vue-router'
</script>

<style scoped>
</style>
