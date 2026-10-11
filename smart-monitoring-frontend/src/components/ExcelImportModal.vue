<template>
  <div v-if="isOpen" class="fixed inset-0 z-50 overflow-y-auto">
    <!-- Backdrop -->
    <div class="fixed inset-0 bg-black bg-opacity-50" @click="close"></div>

    <!-- Modal -->
    <div class="flex items-center justify-center min-h-screen px-4 pt-4 pb-20 sm:p-0">
      <div class="relative bg-white rounded-lg shadow-xl max-w-md w-full">
        <!-- Header -->
        <div class="bg-gradient-to-r from-blue-600 to-blue-700 px-6 py-4 rounded-t-lg">
          <h3 class="text-lg font-semibold text-white">{{ title }}</h3>
          <p class="text-blue-100 text-sm mt-1">{{ description }}</p>
        </div>

        <!-- Content -->
        <div class="px-6 py-6 space-y-4">
          <!-- Download Template Button -->
          <div class="mb-6">
            <button
              @click="downloadTemplate"
              class="w-full px-4 py-2 bg-green-50 border-2 border-green-300 text-green-700 rounded-lg hover:bg-green-100 transition-colors font-medium text-sm"
            >
              📥 Télécharger le modèle Excel
            </button>
            <p class="text-xs text-gray-500 mt-2 text-center">
              Téléchargez le fichier modèle pour voir les colonnes requises
            </p>
          </div>

          <!-- File Upload Area -->
          <div
            @click="triggerFileInput"
            @dragover.prevent="isDragging = true"
            @dragleave.prevent="isDragging = false"
            @drop.prevent="handleFileDrop"
            :class="[
              'border-2 border-dashed rounded-lg p-6 text-center cursor-pointer transition-colors',
              isDragging ? 'border-blue-500 bg-blue-50' : 'border-gray-300 bg-gray-50 hover:bg-gray-100',
            ]"
          >
            <div class="text-3xl mb-2">📄</div>
            <p class="text-gray-700 font-medium">{{ selectedFile ? selectedFile.name : 'Glisser votre fichier' }}</p>
            <p class="text-gray-500 text-sm">ou cliquer pour choisir</p>
            <p class="text-xs text-gray-400 mt-2">Format: Excel (.xlsx) - Max 10 MB</p>
            <input
              ref="fileInput"
              type="file"
              accept=".xlsx"
              style="display: none"
              @change="handleFileSelect"
            />
          </div>

          <!-- File Info -->
          <div v-if="selectedFile" class="bg-blue-50 border-l-4 border-blue-500 p-3 rounded">
            <p class="text-sm text-blue-900">
              📊 Fichier sélectionné: <strong>{{ selectedFile.name }}</strong> ({{
                formatFileSize(selectedFile.size)
              }})
            </p>
            <button
              @click="clearFile"
              class="text-xs text-blue-700 hover:text-blue-900 mt-2 underline"
            >
              × Supprimer le fichier
            </button>
          </div>

          <!-- Error Messages -->
          <div v-if="error" class="bg-red-50 border-l-4 border-red-500 p-3 rounded">
            <p class="text-sm text-red-700">❌ {{ error }}</p>
          </div>
        </div>

        <!-- Footer -->
        <div class="bg-gray-50 px-6 py-4 rounded-b-lg flex gap-3 justify-end">
          <button
            @click="close"
            :disabled="isLoading"
            class="px-4 py-2 text-gray-700 border border-gray-300 rounded-lg hover:bg-gray-100 disabled:opacity-50 font-medium text-sm"
          >
            Annuler
          </button>
          <button
            @click="submitImport"
            :disabled="!selectedFile || isLoading"
            :class="[
              'px-4 py-2 rounded-lg font-medium text-sm text-white transition-colors',
              !selectedFile || isLoading
                ? 'bg-gray-400 cursor-not-allowed'
                : 'bg-blue-600 hover:bg-blue-700 active:bg-blue-800',
            ]"
          >
            <span v-if="isLoading" class="inline-flex items-center gap-2">
              <span class="animate-spin">⟳</span>
              Import en cours...
            </span>
            <span v-else>✓ Importer</span>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps({
  isOpen: {
    type: Boolean,
    required: true,
  },
  title: {
    type: String,
    required: true,
  },
  description: {
    type: String,
    required: true,
  },
  templateData: {
    type: Array,
    required: true, // Array of column definitions
  },
  onImport: {
    type: Function,
    required: true,
  },
  templateFileName: {
    type: String,
    required: true,
  },
})

const emit = defineEmits(['close', 'import-result'])

const selectedFile = ref(null)
const isDragging = ref(false)
const isLoading = ref(false)
const error = ref('')
const fileInput = ref(null)

const triggerFileInput = () => {
  fileInput.value?.click()
}

const handleFileSelect = (event) => {
  const files = event.target.files
  if (files && files.length > 0) {
    validateAndSelectFile(files[0])
  }
}

const handleFileDrop = (event) => {
  isDragging.value = false
  const files = event.dataTransfer.files
  if (files && files.length > 0) {
    validateAndSelectFile(files[0])
  }
}

const validateAndSelectFile = (file) => {
  error.value = ''

  // Check extension
  if (!file.name.endsWith('.xlsx')) {
    error.value = 'Seuls les fichiers Excel (.xlsx) sont acceptés'
    return
  }

  // Check size (10 MB max)
  const maxSize = 10 * 1024 * 1024
  if (file.size > maxSize) {
    error.value = 'Le fichier est trop volumineux (max 10 MB)'
    return
  }

  // Check if empty
  if (file.size === 0) {
    error.value = 'Le fichier est vide'
    return
  }

  selectedFile.value = file
}

const clearFile = () => {
  selectedFile.value = null
  fileInput.value.value = ''
}

const formatFileSize = (bytes) => {
  if (bytes === 0) return '0 Bytes'
  const k = 1024
  const sizes = ['Bytes', 'KB', 'MB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return Math.round((bytes / Math.pow(k, i)) * 100) / 100 + ' ' + sizes[i]
}

const downloadTemplate = () => {
  // Create Excel file in memory using a simple approach
  const worksheet = []

  // Add header row
  const headers = props.templateData.map((col) => col.name)
  worksheet.push(headers)

  // Add example row if provided
  if (props.templateData.some((col) => col.example)) {
    const exampleRow = props.templateData.map((col) => col.example || '')
    worksheet.push(exampleRow)
  }

  // Generate CSV (most compatible)
  const csv = worksheet.map((row) => row.map((cell) => `"${cell}"`).join(',')).join('\n')

  const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' })
  const link = document.createElement('a')
  const url = URL.createObjectURL(blob)

  link.setAttribute('href', url)
  link.setAttribute('download', `${props.templateFileName}.csv`)
  link.style.visibility = 'hidden'

  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
}

const submitImport = async () => {
  if (!selectedFile.value) {
    error.value = 'Veuillez sélectionner un fichier'
    return
  }

  isLoading.value = true
  error.value = ''

  try {
    const result = await props.onImport(selectedFile.value)
    emit('import-result', result)
    close()
  } catch (err) {
    error.value = err.message || 'Erreur lors de l\'import'
  } finally {
    isLoading.value = false
  }
}

const close = () => {
  if (!isLoading.value) {
    selectedFile.value = null
    error.value = ''
    fileInput.value.value = ''
    emit('close')
  }
}
</script>
