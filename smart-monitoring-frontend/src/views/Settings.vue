<template>
  <div class="space-y-6">
    <!-- En-tête -->
    <div>
      <h1 class="text-3xl font-bold text-gray-900">Paramètres</h1>
      <p class="text-gray-600 mt-2">Gérez vos paramètres personnels</p>
    </div>

    <!-- Messages d'alerte -->
    <ErrorAlert
      :show="!!error"
      title="Erreur"
      :message="error"
      :duration="5000"
      @close="error = null"
    />
    <SuccessAlert
      :show="!!success"
      title="Succès"
      :message="success"
      @close="success = null"
    />

    <!-- Profil utilisateur -->
    <div class="bg-white rounded-lg shadow-lg p-6">
      <h2 class="text-2xl font-bold text-gray-900 mb-6">Mon Profil</h2>

      <div class="space-y-6">
        <!-- Avatar et Info -->
        <div class="flex items-center gap-6">
          <div class="w-24 h-24 bg-primary-500 rounded-full flex items-center justify-center text-white text-3xl font-bold">
            {{ authStore.username?.charAt(0).toUpperCase() }}
          </div>
          <div>
            <h3 class="text-xl font-bold text-gray-900">{{ authStore.username }}</h3>
            <p class="text-gray-600">{{ authStore.email }}</p>
            <span
              :class="[
                'inline-block mt-2 px-3 py-1 rounded-full text-sm font-medium',
                authStore.role === 'ADMIN'
                  ? 'bg-red-100 text-red-800'
                  : authStore.role === 'SUPERVISOR'
                  ? 'bg-blue-100 text-blue-800'
                  : 'bg-green-100 text-green-800'
              ]"
            >
              {{ authStore.role }}
            </span>
          </div>
        </div>

        <hr class="my-6" />

        <!-- Informations personnelles -->
        <div>
          <h3 class="text-lg font-bold text-gray-900 mb-4">Informations Personnelles</h3>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Nom d'utilisateur</label>
              <input
                v-model="profileForm.username"
                type="text"
                :disabled="!isEditingProfile"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 disabled:bg-gray-100"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Email</label>
              <input
                v-model="profileForm.email"
                type="email"
                :disabled="!isEditingProfile"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 disabled:bg-gray-100"
              />
            </div>
          </div>

          <div class="mt-4 flex gap-2">
            <button
              v-if="!isEditingProfile"
              @click="isEditingProfile = true"
              class="bg-primary-600 hover:bg-primary-700 text-white font-bold py-2 px-4 rounded-lg"
            >
              Modifier
            </button>
            <div v-else class="flex gap-2">
              <button
                @click="handleUpdateProfile"
                :disabled="loading"
                class="bg-primary-600 hover:bg-primary-700 disabled:bg-primary-400 text-white font-bold py-2 px-4 rounded-lg"
              >
                Enregistrer
              </button>
              <button
                @click="isEditingProfile = false"
                class="bg-gray-300 hover:bg-gray-400 text-gray-900 font-bold py-2 px-4 rounded-lg"
              >
                Annuler
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Sécurité -->
    <div class="bg-white rounded-lg shadow-lg p-6">
      <h2 class="text-2xl font-bold text-gray-900 mb-6">Sécurité</h2>

      <div class="space-y-6">
        <!-- Changement de mot de passe -->
        <div v-if="!showPasswordForm">
          <p class="text-gray-600 mb-4">Changez votre mot de passe régulièrement pour sécuriser votre compte</p>
          <button
            @click="showPasswordForm = true"
            class="bg-yellow-600 hover:bg-yellow-700 text-white font-bold py-2 px-4 rounded-lg"
          >
            Changer le Mot de Passe
          </button>
        </div>

        <form v-else @submit.prevent="handleChangePassword" class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Mot de passe actuel</label>
            <input
              v-model="passwordForm.currentPassword"
              type="password"
              required
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500"
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Nouveau mot de passe</label>
            <input
              v-model="passwordForm.newPassword"
              type="password"
              required
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500"
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Confirmer le nouveau mot de passe</label>
            <input
              v-model="passwordForm.confirmPassword"
              type="password"
              required
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500"
            />
          </div>
          <div class="flex gap-2">
            <button
              type="submit"
              :disabled="loading"
              class="bg-yellow-600 hover:bg-yellow-700 disabled:bg-yellow-400 text-white font-bold py-2 px-4 rounded-lg"
            >
              Changer le Mot de Passe
            </button>
            <button
              type="button"
              @click="showPasswordForm = false"
              class="bg-gray-300 hover:bg-gray-400 text-gray-900 font-bold py-2 px-4 rounded-lg"
            >
              Annuler
            </button>
          </div>
        </form>
      </div>
    </div>


  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'
import axiosInstance from '@/api/axios'
import ErrorAlert from '@/components/ErrorAlert.vue'
import SuccessAlert from '@/components/SuccessAlert.vue'

const router = useRouter()
const authStore = useAuthStore()

const loading = ref(false)
const error = ref(null)
const success = ref(null)
const isEditingProfile = ref(false)
const showPasswordForm = ref(false)

const profileForm = reactive({
  username: authStore.username,
  email: authStore.email
})

const passwordForm = reactive({
  currentPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// Mettre à jour le profil
const handleUpdateProfile = async () => {
  loading.value = true
  try {
    await axiosInstance.put(`/users/${authStore.user}`, {
      username: profileForm.username,
      email: profileForm.email,
      role: authStore.role
    })
    success.value = 'Profil mis à jour avec succès'
    isEditingProfile.value = false
    // Mettre à jour le store
    authStore.username = profileForm.username
    authStore.email = profileForm.email
  } catch (err) {
    error.value = err.response?.data?.message || 'Erreur lors de la mise à jour'
  } finally {
    loading.value = false
  }
}

// Changer le mot de passe
const handleChangePassword = async () => {
  if (passwordForm.newPassword !== passwordForm.confirmPassword) {
    error.value = 'Les mots de passe ne correspondent pas'
    return
  }

  loading.value = true
  try {
    // Vérifier le mot de passe actuel en essayant de se reconnecter
    await axiosInstance.post('/auth/login', {
      email: authStore.email,
      password: passwordForm.currentPassword
    })
    
    // Mot de passe actuel correct, mais nous n'avons pas d'endpoint pour changer le mot de passe
    // Pour l'instant, afficher un message
    success.value = 'Fonctionnalité disponible prochainement'
    showPasswordForm.value = false
    passwordForm.currentPassword = ''
    passwordForm.newPassword = ''
    passwordForm.confirmPassword = ''
  } catch (err) {
    error.value = 'Le mot de passe actuel est incorrect'
  } finally {
    loading.value = false
  }
}

// Déconnexion
const handleLogout = () => {
  authStore.logout()
  router.push('/login')
}
</script>

<style scoped></style>
