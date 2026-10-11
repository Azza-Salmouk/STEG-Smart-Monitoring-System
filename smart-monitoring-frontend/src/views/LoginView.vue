<template>
  <div class="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center p-4">
    <div class="w-full max-w-md">
      <!-- Carte de connexion -->
      <div class="bg-white rounded-2xl shadow-2xl p-8 space-y-6">
        <!-- Logo -->
        <div class="text-center">
          <AppLogo />
        </div>

        <!-- Titre -->
        <div class="text-center">
          <h1 class="text-2xl font-bold text-gray-900">Connexion</h1>
          <p class="text-gray-600 mt-2">Accédez à la plateforme de supervision</p>
        </div>

        <!-- Messages d'erreur -->
        <ErrorAlert
          :show="!!authStore.error"
          title="Erreur de connexion"
          :message="authStore.error || ''"
          :duration="0"
          @close="authStore.clearError"
        />

        <!-- Formulaire -->
        <form @submit.prevent="handleLogin" class="space-y-4">
          <!-- Email -->
          <div>
            <label for="email" class="block text-sm font-medium text-gray-700 mb-2">
              Email
            </label>
            <input
              id="email"
              v-model="form.email"
              type="email"
              placeholder="admin@steg.tn"
              required
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent transition-colors"
              :disabled="authStore.loading"
            />
          </div>

          <!-- Password -->
          <div>
            <label for="password" class="block text-sm font-medium text-gray-700 mb-2">
              Mot de passe
            </label>
            <div class="relative">
              <input
                id="password"
                v-model="form.password"
                :type="showPassword ? 'text' : 'password'"
                placeholder="Votre mot de passe"
                required
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent transition-colors"
                :disabled="authStore.loading"
              />
              <button
                type="button"
                @click="showPassword = !showPassword"
                class="absolute right-3 top-1/2 transform -translate-y-1/2 text-gray-500 hover:text-gray-700"
                :disabled="authStore.loading"
              >
                <svg v-if="showPassword" class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
                  <path d="M10 12a2 2 0 100-4 2 2 0 000 4z"/>
                  <path fill-rule="evenodd" d="M.458 10C1.732 5.943 5.522 3 10 3s8.268 2.943 9.542 7c-1.274 4.057-5.064 7-9.542 7S1.732 14.057.458 10zM14 10a4 4 0 11-8 0 4 4 0 018 0z" clip-rule="evenodd"/>
                </svg>
                <svg v-else class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
                  <path fill-rule="evenodd" d="M3.707 2.293a1 1 0 00-1.414 1.414l14 14a1 1 0 001.414-1.414l-1.473-1.473A10.014 10.014 0 0019.542 10C18.268 5.943 14.478 3 10 3a9.958 9.958 0 00-4.512 1.074l-1.78-1.781zm4.261 4.26l1.514 1.515a2.003 2.003 0 012.45 2.45l1.514 1.514a4 4 0 00-5.478-5.478z" clip-rule="evenodd"/>
                  <path d="M15.171 13.576l1.473 1.473A10.014 10.014 0 0019.542 10c-1.274-4.057-5.064-7-9.542 7a9.958 9.958 0 00-4.512 1.074l1.781 1.781A4 4 0 0110 6c2.054 0 3.868.996 5.022 2.53l-4.293 4.293a4 4 0 00-5.022-5.022L3.89 5.574A9.961 9.961 0 0110 3z"/>
                </svg>
              </button>
            </div>
          </div>

          <!-- Remember me -->
          <div class="flex items-center">
            <input
              id="remember"
              v-model="form.rememberMe"
              type="checkbox"
              class="w-4 h-4 text-primary-600 rounded border-gray-300 focus:ring-primary-500"
              :disabled="authStore.loading"
            />
            <label for="remember" class="ml-2 text-sm text-gray-600">
              Se souvenir de moi
            </label>
          </div>

          <!-- Bouton de connexion -->
          <button
            type="submit"
            :disabled="authStore.loading"
            class="w-full bg-primary-600 hover:bg-primary-700 disabled:bg-primary-400 text-white font-bold py-2 px-4 rounded-lg transition-colors flex items-center justify-center gap-2"
          >
            <span v-if="authStore.loading">
              <LoadingSpinner :show="true" text="" />
            </span>
            <span v-else>Connexion</span>
          </button>
        </form>

        <!-- Info développement -->
        <div v-if="showDevInfo" class="bg-blue-50 border border-blue-200 rounded-lg p-3 text-xs text-blue-700">
          <p><strong>Compte de test:</strong></p>
          <p>Email: admin@steg.tn</p>
          <p>Password: Admin@123</p>
        </div>
      </div>

      <!-- Footer -->
      <div class="text-center mt-6">
        <p class="text-gray-400 text-sm">
          Smart Monitoring - Plateforme STEG v0.1.0
        </p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'
import AppLogo from '@/components/AppLogo.vue'
import ErrorAlert from '@/components/ErrorAlert.vue'
import LoadingSpinner from '@/components/LoadingSpinner.vue'

const router = useRouter()
const authStore = useAuthStore()

const showPassword = ref(false)
const showDevInfo = ref(true)

const form = reactive({
  email: '',
  password: '',
  rememberMe: false
})

const handleLogin = async () => {
  try {
    authStore.clearError()
    
    await authStore.login({
      email: form.email,
      password: form.password
    })

    const redirects = {
      'ADMIN': '/admin/dashboard',
      'SUPERVISOR': '/supervisor/dashboard',
      'AGENT': '/agent/dashboard',
      'SERVICE_CLIENT': '/serviceclient/dashboard'
    }

    const redirectPath = redirects[authStore.role] || '/'
    router.push(redirectPath)
  } catch (error) {
    console.error('Erreur de connexion:', error)
  }
}
</script>

<style scoped>
</style>
