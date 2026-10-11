<template>
  <div v-if="isOpen" class="fixed inset-0 bg-black bg-opacity-50 z-50 flex items-center justify-center">
    <div class="bg-white rounded-lg shadow-xl w-full max-w-2xl max-h-[90vh] overflow-y-auto">
      <!-- Header -->
      <div class="sticky top-0 bg-gradient-to-r from-blue-600 to-blue-700 text-white px-6 py-4 flex justify-between items-center">
        <h2 class="text-lg font-semibold">{{ mode === 'create' ? 'Créer une alerte' : 'Modifier l\'alerte' }}</h2>
        <button @click="close" class="p-1 hover:bg-blue-500 rounded transition">
          <X :size="20" />
        </button>
      </div>

      <!-- Content -->
      <div class="p-6 space-y-4">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <!-- Titre -->
          <div class="md:col-span-2">
            <label class="block text-sm font-medium text-gray-700 mb-1">Titre</label>
            <input
              v-model="form.titre"
              type="text"
              placeholder="Titre de l'alerte"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            />
            <p v-if="errors.titre" class="text-red-500 text-xs mt-1">{{ errors.titre }}</p>
          </div>

          <!-- Description -->
          <div class="md:col-span-2">
            <label class="block text-sm font-medium text-gray-700 mb-1">Description</label>
            <textarea
              v-model="form.description"
              placeholder="Description détaillée"
              rows="3"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            ></textarea>
            <p v-if="errors.description" class="text-red-500 text-xs mt-1">{{ errors.description }}</p>
          </div>

          <!-- Type -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Type</label>
            <select
              v-model="form.type"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
            >
              <option value="">Sélectionner un type</option>
              <option value="TEMPERATURE">Température</option>
              <option value="SURCHARGE">Surcharge</option>
              <option value="SURCONSOMMATION">Surconsommation</option>
              <option value="PANNE">Panne</option>
              <option value="CHUTE_TENSION">Chute de tension</option>
              <option value="COURT_CIRCUIT">Court-circuit</option>
              <option value="INCENDIE">Incendie</option>
              <option value="COMMUNICATION">Communication</option>
              <option value="AUTRE">Autre</option>
            </select>
          </div>

          <!-- Niveau -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Niveau</label>
            <select
              v-model="form.niveau"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
            >
              <option value="">Sélectionner un niveau</option>
              <option value="INFO">Info</option>
              <option value="WARNING">Avertissement</option>
              <option value="HIGH">Haut</option>
              <option value="CRITICAL">Critique</option>
            </select>
          </div>

          <!-- Transformateur -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Transformateur</label>
            <select
              v-model="form.transformateurId"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
            >
              <option value="">Sélectionner un transformateur</option>
              <option v-for="t in transformateurs" :key="t.id" :value="t.id">
                {{ t.reference }}
              </option>
            </select>
          </div>

          <!-- Poste -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Poste (optionnel)</label>
            <select
              v-model="form.posteId"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
            >
              <option value="">Sélectionner un poste</option>
              <option v-for="p in postes" :key="p.id" :value="p.id">
                {{ p.nom }}
              </option>
            </select>
          </div>
        </div>
      </div>

      <!-- Footer -->
      <div class="bg-gray-50 px-6 py-4 flex justify-end gap-3 border-t">
        <button
          @click="close"
          class="px-4 py-2 bg-gray-200 text-gray-800 rounded-lg hover:bg-gray-300 transition font-medium"
        >
          Annuler
        </button>
        <button
          @click="submit"
          :disabled="loading"
          class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition font-medium disabled:opacity-50"
        >
          {{ loading ? 'Enregistrement...' : 'Enregistrer' }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, watch } from 'vue';
import { X } from 'lucide-vue-next';
import { useAlertStore } from '@/stores/useAlertStore';
import { useTransformateurStore } from '@/stores/transformateurStore';
import { usePosteStore } from '@/stores/posteStore';

const props = defineProps({
  isOpen: Boolean,
  alert: Object,
  mode: {
    type: String,
    default: 'create',
  },
});

const emit = defineEmits(['close', 'saved']);

const alertStore = useAlertStore();
const transformateurStore = useTransformateurStore();
const posteStore = usePosteStore();

const loading = ref(false);
const errors = ref({});

const form = reactive({
  titre: '',
  description: '',
  type: '',
  niveau: '',
  transformateurId: '',
  posteId: '',
  measurementId: '',
});

const transformateurs = ref([]);
const postes = ref([]);

watch(
  () => props.isOpen,
  async (newVal) => {
    if (newVal) {
      if (props.mode === 'edit' && props.alert) {
        form.titre = props.alert.titre;
        form.description = props.alert.description;
        form.type = props.alert.type;
        form.niveau = props.alert.niveau;
        form.transformateurId = props.alert.transformateurId;
        form.posteId = props.alert.posteId;
      } else {
        resetForm();
      }
      await loadData();
    }
  }
);

const loadData = async () => {
  try {
    const transformateurResponse = await transformateurStore.fetchTransformateurs();
    const posteResponse = await posteStore.fetchPostes();
    transformateurs.value = transformateurResponse.data || [];
    postes.value = posteResponse.data || [];
  } catch (e) {
    console.error('Error loading data:', e);
  }
};

const resetForm = () => {
  form.titre = '';
  form.description = '';
  form.type = '';
  form.niveau = '';
  form.transformateurId = '';
  form.posteId = '';
  form.measurementId = '';
  errors.value = {};
};

const submit = async () => {
  loading.value = true;
  errors.value = {};

  try {
    if (props.mode === 'create') {
      await alertStore.createAlert(form);
    } else {
      await alertStore.updateAlert(props.alert.id, form);
    }
    emit('saved');
    close();
  } catch (e) {
    if (e.response?.data?.errors) {
      errors.value = e.response.data.errors;
    } else {
      errors.value.general = e.message;
    }
  } finally {
    loading.value = false;
  }
};

const close = () => {
  resetForm();
  emit('close');
};
</script>
