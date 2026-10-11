<template>
  <div v-if="result" class="fixed inset-0 z-50 overflow-y-auto">
    <!-- Backdrop -->
    <div class="fixed inset-0 bg-black bg-opacity-50" @click="close"></div>

    <!-- Modal -->
    <div class="flex items-center justify-center min-h-screen px-4 pt-4 pb-20 sm:p-0">
      <div class="relative bg-white rounded-lg shadow-xl max-w-2xl w-full max-h-[80vh] overflow-y-auto">
        <!-- Header -->
        <div class="bg-gradient-to-r from-blue-600 to-blue-700 px-6 py-4 rounded-t-lg sticky top-0 z-10">
          <h3 class="text-lg font-semibold text-white">Résultat de l'Import</h3>
          <p class="text-blue-100 text-sm mt-1">Détails de l'opération d'importation</p>
        </div>

        <!-- Content -->
        <div class="px-6 py-6 space-y-6">
          <!-- Summary Stats -->
          <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
            <div class="bg-blue-50 border-l-4 border-blue-500 p-4 rounded">
              <p class="text-gray-600 text-sm font-medium">Total</p>
              <p class="text-2xl font-bold text-blue-600 mt-1">
                {{ result.importedRows + result.skippedRows + result.errorRows }}
              </p>
            </div>

            <div class="bg-green-50 border-l-4 border-green-500 p-4 rounded">
              <p class="text-gray-600 text-sm font-medium">✅ Importés</p>
              <p class="text-2xl font-bold text-green-600 mt-1">{{ result.importedRows }}</p>
            </div>

            <div class="bg-yellow-50 border-l-4 border-yellow-500 p-4 rounded">
              <p class="text-gray-600 text-sm font-medium">⚠️ Ignorés</p>
              <p class="text-2xl font-bold text-yellow-600 mt-1">{{ result.skippedRows }}</p>
            </div>

            <div class="bg-red-50 border-l-4 border-red-500 p-4 rounded">
              <p class="text-gray-600 text-sm font-medium">❌ Erreurs</p>
              <p class="text-2xl font-bold text-red-600 mt-1">{{ result.errorRows }}</p>
            </div>
          </div>

          <!-- Message -->
          <div v-if="result.message" class="bg-blue-50 border border-blue-200 rounded-lg p-4">
            <p class="text-blue-800">{{ result.message }}</p>
          </div>

          <!-- Errors List -->
          <div v-if="result.errors && result.errors.length > 0" class="space-y-3">
            <h4 class="text-sm font-semibold text-gray-700">Détails des erreurs et avertissements:</h4>

            <div class="space-y-2 max-h-96 overflow-y-auto">
              <div
                v-for="(error, index) in result.errors"
                :key="index"
                class="bg-red-50 border-l-4 border-red-300 p-3 rounded text-sm"
              >
                <p class="text-red-700">{{ error }}</p>
              </div>
            </div>

            <!-- Export Errors -->
            <button
              @click="exportErrors"
              class="text-xs text-blue-600 hover:text-blue-800 underline mt-2"
            >
              📋 Copier tous les messages
            </button>
          </div>

          <!-- Success Message -->
          <div
            v-else-if="result.importedRows > 0"
            class="bg-green-50 border border-green-200 rounded-lg p-4"
          >
            <p class="text-green-800">
              ✓ Importation réussie! {{ result.importedRows }} éléments ont été ajoutés avec succès.
            </p>
          </div>
        </div>

        <!-- Footer -->
        <div class="bg-gray-50 px-6 py-4 rounded-b-lg flex gap-3 justify-end sticky bottom-0">
          <button
            @click="close"
            class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 font-medium text-sm"
          >
            ✓ Fermer
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps({
  result: {
    type: Object,
    default: null,
  },
})

const emit = defineEmits(['close'])

const close = () => {
  emit('close')
}

const exportErrors = () => {
  if (!props.result?.errors) return

  const text = props.result.errors.join('\n')
  navigator.clipboard
    .writeText(text)
    .then(() => {
      alert('Erreurs copiées dans le presse-papiers')
    })
    .catch(() => {
      alert('Impossible de copier dans le presse-papiers')
    })
}
</script>
