<template>
  <div class="fixed inset-0 bg-black bg-opacity-50 z-50 flex items-center justify-center p-4">
    <div class="bg-white rounded-lg shadow-xl max-w-md w-full">
      <!-- Icon -->
      <div class="flex justify-center pt-6">
        <div class="flex-shrink-0">
          <AlertTriangleIcon class="h-12 w-12 text-red-600" />
        </div>
      </div>

      <!-- Content -->
      <div class="text-center px-6 py-4">
        <h3 class="text-lg font-medium text-gray-900">
          Supprimer {{ itemName }}
        </h3>
        <p class="text-gray-500 text-sm mt-2">
          Cette action ne peut pas être annulée. Êtes-vous sûr ?
        </p>
      </div>

      <!-- Actions -->
      <div class="px-6 py-4 bg-gray-50 flex gap-3 justify-end rounded-b-lg">
        <button
          @click="$emit('cancel')"
          class="px-4 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-lg hover:bg-gray-50"
        >
          Annuler
        </button>
        <button
          @click="$emit('confirm')"
          class="px-4 py-2 text-sm font-medium text-white bg-red-600 rounded-lg hover:bg-red-700"
        >
          Supprimer
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { AlertTriangleIcon } from 'lucide-vue-next'

defineProps({
  itemName: {
    type: String,
    required: true
  }
})

defineEmits(['confirm', 'cancel'])
</script>
