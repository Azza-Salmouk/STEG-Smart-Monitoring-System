<template>
  <div class="flex flex-col h-screen bg-gray-50">
    <!-- Header -->
    <div class="bg-white border-b border-gray-200 shadow-sm p-6">
      <div class="flex items-center justify-between">
        <div>
          <h1 class="text-3xl font-bold text-gray-900">Assistant Intelligent</h1>
          <p class="text-gray-600 mt-1">Posez vos questions sur le système de monitoring</p>
        </div>
        <div class="flex items-center gap-3">
          <!-- Service Status Indicator -->
          <div class="flex items-center gap-2 px-3 py-2 rounded-lg bg-gray-100">
            <div
              :class="[
                'w-2 h-2 rounded-full',
                serviceHealth.healthy ? 'bg-green-500' : 'bg-red-500',
              ]"
            ></div>
            <span class="text-sm text-gray-700">
              {{ serviceHealth.healthy ? 'Service actif' : 'Service indisponible' }}
            </span>
          </div>
        </div>
      </div>
    </div>

    <!-- Conversation Area -->
    <div
      ref="conversationContainer"
      class="flex-1 overflow-y-auto p-6 space-y-6"
    >
      <!-- Welcome Message -->
      <div v-if="messages.length === 0" class="flex items-center justify-center h-full">
        <div class="text-center">
          <div class="text-6xl mb-4">🤖</div>
          <h2 class="text-2xl font-bold text-gray-900 mb-2">Bienvenue dans l'Assistant Intelligent</h2>
          <p class="text-gray-600 max-w-md">
            Je suis ici pour vous aider à comprendre votre système de monitoring.
            Posez-moi vos questions sur les alertes, les mesures, les interventions, ou tout autre aspect du système.
          </p>
        </div>
      </div>

      <!-- Messages -->
      <div v-for="msg in messages" :key="msg.id" class="flex gap-4 animate-fade-in">
        <!-- User Message -->
        <div v-if="msg.role === 'user'" class="flex justify-end w-full">
          <div class="bg-blue-600 text-white rounded-lg px-4 py-3 max-w-2xl shadow-md">
            <p class="text-sm">{{ msg.content }}</p>
            <p class="text-xs opacity-75 mt-1">{{ formatTime(msg.timestamp) }}</p>
          </div>
        </div>

        <!-- Assistant Message -->
        <div v-else class="flex gap-4 w-full">
          <div class="w-8 h-8 rounded-full bg-gradient-to-br from-blue-500 to-purple-500 flex items-center justify-center flex-shrink-0 shadow-md">
            <span class="text-white text-lg">🤖</span>
          </div>
          <div class="flex-1">
            <div class="bg-white text-gray-900 rounded-lg px-4 py-3 max-w-2xl shadow-md border border-gray-200">
              <p class="text-sm leading-relaxed">{{ msg.content }}</p>
              <p class="text-xs text-gray-500 mt-2">{{ formatTime(msg.timestamp) }}</p>
            </div>

            <!-- Error Message -->
            <div v-if="msg.error" class="mt-2 bg-red-50 border border-red-200 text-red-700 text-xs rounded px-3 py-2">
              ⚠️ {{ msg.error }}
            </div>
          </div>
        </div>
      </div>

      <!-- Loading Indicator -->
      <div v-if="state === 'LOADING'" class="flex gap-4">
        <div class="w-8 h-8 rounded-full bg-gradient-to-br from-blue-500 to-purple-500 flex items-center justify-center flex-shrink-0 shadow-md">
          <span class="text-white text-lg">🤖</span>
        </div>
        <div class="flex-1">
          <div class="bg-white text-gray-900 rounded-lg px-4 py-3 shadow-md border border-gray-200">
            <div class="flex items-center gap-2">
              <div class="flex gap-1">
                <div class="w-2 h-2 bg-gray-400 rounded-full animate-bounce" style="animation-delay: 0ms"></div>
                <div class="w-2 h-2 bg-gray-400 rounded-full animate-bounce" style="animation-delay: 150ms"></div>
                <div class="w-2 h-2 bg-gray-400 rounded-full animate-bounce" style="animation-delay: 300ms"></div>
              </div>
              <span class="text-sm text-gray-600">L'assistant réfléchit...</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Input Area -->
    <div class="bg-white border-t border-gray-200 p-6 shadow-lg">
      <!-- Error Message -->
      <div v-if="state === 'ERROR'" class="mb-4 bg-red-50 border border-red-200 rounded-lg px-4 py-3">
        <p class="text-red-700 text-sm">
          <span class="font-semibold">Erreur:</span> {{ errorMessage }}
        </p>
      </div>

      <!-- Input Form -->
      <form @submit.prevent="sendMessage" class="flex gap-3">
        <textarea
          v-model="inputMessage"
          :disabled="state === 'LOADING' || !serviceHealth.healthy"
          placeholder="Votre question ici... (Appuyez sur Ctrl+Entrée pour envoyer)"
          class="flex-1 px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 resize-none disabled:bg-gray-100 disabled:cursor-not-allowed"
          rows="3"
          @keydown.ctrl.enter="sendMessage"
          @keydown.meta.enter="sendMessage"
        ></textarea>

        <div class="flex flex-col gap-2">
          <button
            type="submit"
            :disabled="
              state === 'LOADING' ||
              !serviceHealth.healthy ||
              inputMessage.trim().length === 0
            "
            class="px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 disabled:bg-gray-400 disabled:cursor-not-allowed transition-colors font-semibold"
          >
            <span v-if="state === 'LOADING'">
              ⏳ Envoi...
            </span>
            <span v-else>
              Envoyer
            </span>
          </button>

          <button
            type="button"
            @click="clearConversation"
            :disabled="messages.length === 0"
            class="px-4 py-2 bg-gray-200 text-gray-700 rounded-lg hover:bg-gray-300 disabled:opacity-50 disabled:cursor-not-allowed transition-colors text-sm"
          >
            Effacer
          </button>
        </div>
      </form>

      <!-- Service Unavailable Note -->
      <div v-if="!serviceHealth.healthy" class="mt-4 bg-yellow-50 border border-yellow-200 rounded-lg px-4 py-3">
        <p class="text-yellow-700 text-sm">
          <span class="font-semibold">Note:</span> Le service d'intelligence artificielle est actuellement indisponible.
          Les réponses ne peuvent pas être traitées pour le moment. Veuillez réessayer plus tard.
        </p>
      </div>

      <!-- Help Text -->
      <div class="mt-4 text-xs text-gray-500 space-y-1">
        <p>💡 Conseil: Soyez spécifique dans vos questions pour obtenir les meilleures réponses.</p>
        <p>🔐 Vos messages sont protégés par le système d'authentification de l'application.</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick, onMounted } from 'vue'
import { useToast } from '@/composables/useToast'
import { sendChatbotMessage, checkChatbotHealth } from '@/services/chatbotService'
import { useAuthStore } from '@/stores/authStore'

// Constants
const STATES = {
  IDLE: 'IDLE',
  LOADING: 'LOADING',
  SUCCESS: 'SUCCESS',
  ERROR: 'ERROR',
}

// Refs
const state = ref(STATES.IDLE)
const inputMessage = ref('')
const messages = ref([])
const errorMessage = ref('')
const serviceHealth = ref({ healthy: false, timestamp: null })
const conversationContainer = ref(null)

// Composables
const { success: showSuccess, error: showError } = useToast()
const authStore = useAuthStore()

// Message ID counter
let messageIdCounter = 0

/**
 * Generates unique message ID
 */
function generateMessageId() {
  return `msg-${messageIdCounter++}-${Date.now()}`
}

/**
 * Formats timestamp to readable time
 */
function formatTime(timestamp) {
  if (!timestamp) return ''
  try {
    const date = new Date(timestamp)
    return date.toLocaleTimeString('fr-FR', {
      hour: '2-digit',
      minute: '2-digit',
    })
  } catch {
    return ''
  }
}

/**
 * Checks chatbot service health
 */
async function checkServiceHealth() {
  try {
    const health = await checkChatbotHealth()
    serviceHealth.value = health
  } catch (error) {
    serviceHealth.value = {
      healthy: false,
      timestamp: new Date().toISOString(),
      error: error.message,
    }
  }
}

/**
 * Scrolls conversation to bottom
 */
async function scrollToBottom() {
  await nextTick()
  if (conversationContainer.value) {
    conversationContainer.value.scrollTop = conversationContainer.value.scrollHeight
  }
}

/**
 * Sends message to chatbot
 */
async function sendMessage() {
  const message = inputMessage.value.trim()

  if (!message) {
    showError('Veuillez entrer un message.')
    return
  }

  // Check service health
  await checkServiceHealth()
  if (!serviceHealth.value.healthy) {
    showError('Le service d\'IA est actuellement indisponible.')
    return
  }

  // Add user message to conversation
  const userMessageId = generateMessageId()
  messages.value.push({
    id: userMessageId,
    role: 'user',
    content: message,
    timestamp: new Date().toISOString(),
  })

  // Clear input
  inputMessage.value = ''
  state.value = STATES.LOADING
  errorMessage.value = ''

  // Scroll to bottom
  await scrollToBottom()

  try {
    // Send message to backend
    const response = await sendChatbotMessage(message)

    // Add assistant message to conversation
    const assistantMessageId = generateMessageId()
    messages.value.push({
      id: assistantMessageId,
      role: 'assistant',
      content: response.response,
      timestamp: response.timestamp,
    })

    state.value = STATES.SUCCESS
    showSuccess('Réponse reçue avec succès.')

    // Scroll to bottom
    await scrollToBottom()

    // Reset state after 2 seconds
    setTimeout(() => {
      if (state.value === STATES.SUCCESS) {
        state.value = STATES.IDLE
      }
    }, 2000)
  } catch (error) {
    // Add error message to conversation
    const errorMessageId = generateMessageId()
    messages.value.push({
      id: errorMessageId,
      role: 'assistant',
      content: 'Désolé, je n\'ai pas pu traiter votre message.',
      timestamp: new Date().toISOString(),
      error: error.message,
    })

    state.value = STATES.ERROR
    errorMessage.value = error.message
    showError(error.message)

    // Scroll to bottom
    await scrollToBottom()
  }
}

/**
 * Clears the conversation
 */
function clearConversation() {
  if (confirm('Êtes-vous sûr de vouloir effacer la conversation ?')) {
    messages.value = []
    inputMessage.value = ''
    state.value = STATES.IDLE
    errorMessage.value = ''
    messageIdCounter = 0
    showSuccess('Conversation effacée.')
  }
}

// Lifecycle
onMounted(async () => {
  // Check service health on mount
  await checkServiceHealth()

  // Check again every 30 seconds
  setInterval(checkServiceHealth, 30000)

  // Focus input on mount
  nextTick(() => {
    const textarea = document.querySelector('textarea')
    if (textarea) {
      textarea.focus()
    }
  })
})
</script>

<style scoped>
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.animate-fade-in {
  animation: fadeIn 0.3s ease-out;
}

@keyframes bounce {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-8px);
  }
}

.animate-bounce {
  animation: bounce 1.4s infinite;
}
</style>
