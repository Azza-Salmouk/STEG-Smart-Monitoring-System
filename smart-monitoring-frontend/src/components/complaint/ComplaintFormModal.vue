<template>
  <div class="fixed inset-0 bg-black bg-opacity-50 z-50 flex items-center justify-center p-4">
    <div class="bg-white rounded-lg shadow-lg w-full max-w-2xl max-h-[90vh] overflow-y-auto">
      <div class="flex items-center justify-between p-6 border-b">
        <h2 class="text-xl font-bold text-gray-900">
          {{ isEdit ? 'Modifier Réclamation' : 'Nouvelle Réclamation' }}
        </h2>
        <button @click="$emit('close')" class="text-gray-500 hover:text-gray-700">✕</button>
      </div>

      <form @submit.prevent="handleSubmit" class="p-6 space-y-4">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Nom Client *</label>
            <input v-model="form.nomClient" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" required />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Téléphone *</label>
            <input v-model="form.telephone" type="tel" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" required />
          </div>
          <div class="md:col-span-2">
            <label class="block text-sm font-medium text-gray-700 mb-1">Adresse *</label>
            <input v-model="form.adresse" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" required />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Catégorie *</label>
            <select v-model="form.categorie" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" required>
              <option value="">Sélectionner...</option>
              <option value="COUPURE">Coupure</option>
              <option value="SURCHARGE">Surcharge</option>
              <option value="PANNE">Panne</option>
              <option value="FACTURATION">Facturation</option>
              <option value="BRUIT">Bruit</option>
              <option value="ODEUR">Odeur</option>
              <option value="AUTRE">Autre</option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Priorité *</label>
            <select v-model="form.priorite" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" required>
              <option value="">Sélectionner...</option>
              <option value="LOW">Faible</option>
              <option value="MEDIUM">Moyenne</option>
              <option value="HIGH">Haute</option>
              <option value="CRITICAL">Critique</option>
            </select>
          </div>
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Description *</label>
          <textarea v-model="form.description" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" rows="3" required></textarea>
        </div>

        <div v-if="isEdit" class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Statut</label>
            <select v-model="form.statut" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
              <option value="">Sélectionner...</option>
              <option value="NOUVELLE">Nouvelle</option>
              <option value="EN_ANALYSE">En analyse</option>
              <option value="ASSIGNEE">Assignée</option>
              <option value="EN_TRAITEMENT">En traitement</option>
              <option value="RESOLUE">Résolue</option>
              <option value="FERMEE">Fermée</option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Commentaires</label>
            <textarea v-model="form.commentaires" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" rows="2"></textarea>
          </div>
        </div>

        <div class="flex gap-3 justify-end pt-4 border-t">
          <button type="button" @click="$emit('close')" class="px-4 py-2 bg-gray-200 text-gray-700 rounded-lg hover:bg-gray-300">Annuler</button>
          <button type="submit" class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">{{ isEdit ? 'Mettre à jour' : 'Créer' }}</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps({
  complaint: Object,
  isEdit: Boolean,
})

const emit = defineEmits(['save', 'close'])

const form = ref({
  nomClient: '',
  telephone: '',
  adresse: '',
  categorie: '',
  priorite: '',
  description: '',
  statut: '',
  commentaires: '',
})

if (props.isEdit && props.complaint) {
  form.value = {
    nomClient: props.complaint.nomClient,
    telephone: props.complaint.telephone,
    adresse: props.complaint.adresse,
    categorie: props.complaint.categorie,
    priorite: props.complaint.priorite,
    description: props.complaint.description,
    statut: props.complaint.statut,
    commentaires: props.complaint.commentaires,
  }
}

const handleSubmit = () => {
  emit('save', form.value)
}
</script>
