<template>
  <div id="app" class="bg-gray-50 min-h-screen">
    <!-- Layout pour pages authentifiées -->
    <template v-if="authStore.isAuthenticated && isMainLayout">
      <div class="flex h-screen bg-gray-100">
        <!-- Sidebar Moderne -->
        <ModernSidebar @navigate="navigateTo" />

        <!-- Contenu Principal -->
        <div class="flex-1 flex flex-col overflow-hidden">
          <!-- Navbar Moderne -->
          <ModernNavbar />

          <!-- Contenu -->
          <main class="flex-1 overflow-auto">
            <div class="p-6">
              <RouterView />
            </div>
          </main>
        </div>
      </div>

      <!-- Toast Container -->
      <Toast ref="toast" />
    </template>

    <!-- Pages sans layout (Login, 404, etc) -->
    <template v-else>
      <RouterView />
    </template>
  </div>
</template>

<script setup>
import { computed, ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'
import ModernSidebar from '@/components/layout/ModernSidebar.vue'
import ModernNavbar from '@/components/layout/ModernNavbar.vue'
import Toast from '@/components/Toast.vue'
import { RouterView } from 'vue-router'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()
const toast = ref(null)

// Pages sans layout
const pagesWithoutLayout = ['Login', 'NotFound', 'Unauthorized']

// Vérifier si page doit avoir layout
const isMainLayout = computed(() => {
  return !pagesWithoutLayout.includes(route.name)
})

// Restaurer session au démarrage
onMounted(() => {
  authStore.restoreSession()
})

function navigateTo(path) {
  router.push(path)
}
</script>

<style scoped>
</style>
