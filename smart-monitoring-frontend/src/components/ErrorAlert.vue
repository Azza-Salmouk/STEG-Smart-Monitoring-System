<template>
  <div
    v-if="show"
    class="bg-red-50 border border-red-200 rounded-lg p-4 flex items-start gap-3 animate-fade-in"
  >
    <div class="flex-shrink-0">
      <svg class="w-5 h-5 text-red-600" fill="currentColor" viewBox="0 0 20 20">
        <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clip-rule="evenodd"/>
      </svg>
    </div>
    <div class="flex-1">
      <p class="text-sm font-medium text-red-800">{{ title }}</p>
      <p class="text-sm text-red-700 mt-1">{{ message }}</p>
    </div>
    <button
      @click="close"
      class="flex-shrink-0 text-red-400 hover:text-red-600"
    >
      <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
        <path fill-rule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clip-rule="evenodd"/>
      </svg>
    </button>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'

const props = defineProps({
  show: {
    type: Boolean,
    default: false
  },
  title: {
    type: String,
    default: 'Erreur'
  },
  message: {
    type: String,
    default: ''
  },
  duration: {
    type: Number,
    default: 0 // 0 = pas de fermeture automatique
  }
})

const emit = defineEmits(['close'])
const isVisible = ref(props.show)

const close = () => {
  isVisible.value = false
  emit('close')
}

watch(() => props.show, (newVal) => {
  isVisible.value = newVal

  if (newVal && props.duration > 0) {
    setTimeout(() => {
      close()
    }, props.duration)
  }
})

// Permettre au composant parent de contrôler le show
watch(isVisible, (newVal) => {
  if (!newVal) {
    emit('close')
  }
})
</script>

<style scoped>
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.animate-fade-in {
  animation: fadeIn 0.2s ease-out;
}
</style>
