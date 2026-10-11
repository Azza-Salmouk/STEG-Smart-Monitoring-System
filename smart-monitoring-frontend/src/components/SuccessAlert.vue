<template>
  <div
    v-if="show"
    class="bg-green-50 border border-green-200 rounded-lg p-4 flex items-start gap-3 animate-fade-in"
  >
    <div class="flex-shrink-0">
      <svg class="w-5 h-5 text-green-600" fill="currentColor" viewBox="0 0 20 20">
        <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd"/>
      </svg>
    </div>
    <div class="flex-1">
      <p class="text-sm font-medium text-green-800">{{ title }}</p>
      <p class="text-sm text-green-700 mt-1">{{ message }}</p>
    </div>
    <button
      @click="close"
      class="flex-shrink-0 text-green-400 hover:text-green-600"
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
    default: 'Succès'
  },
  message: {
    type: String,
    default: ''
  },
  duration: {
    type: Number,
    default: 3000 // 3 secondes par défaut
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
