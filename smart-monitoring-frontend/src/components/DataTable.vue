<template>
  <Card>
    <!-- Toolbar -->
    <template #header>
      <div class="flex items-center justify-between">
        <h3 class="text-lg font-semibold text-gray-900">{{ title }}</h3>
        <slot name="toolbar"></slot>
      </div>
    </template>

    <!-- Table -->
    <div class="overflow-x-auto">
      <table class="w-full text-sm">
        <thead class="bg-gray-50 border-b">
          <tr>
            <th v-for="column in columns" :key="column.key" class="px-6 py-3 text-left font-semibold text-gray-700">
              <div class="flex items-center gap-2">
                {{ column.label }}
                <button
                  v-if="column.sortable"
                  @click="sort(column.key)"
                  class="p-1 hover:bg-gray-100 rounded transition-colors"
                >
                  <svg class="w-4 h-4 text-gray-400" fill="currentColor" viewBox="0 0 20 20">
                    <path d="M3 3a1 1 0 000 2h11a1 1 0 100-2H3zM3 7a1 1 0 000 2h5a1 1 0 000-2H3zM3 11a1 1 0 100 2h4a1 1 0 100-2H3zM13 16a1 1 0 102 0v-5.586l1.293 1.293a1 1 0 001.414-1.414l-3-3a1 1 0 00-1.414 0l-3 3a1 1 0 101.414 1.414L13 10.414V16z" />
                  </svg>
                </button>
              </div>
            </th>
            <th class="px-6 py-3 text-right font-semibold text-gray-700">Actions</th>
          </tr>
        </thead>
        <tbody class="divide-y">
          <!-- Loading state -->
          <tr v-if="loading" v-for="i in 5" :key="i">
            <td v-for="j in columns.length + 1" :key="j" class="px-6 py-4">
              <SkeletonLoader height="h-4" />
            </td>
          </tr>

          <!-- Empty state -->
          <tr v-else-if="rows.length === 0">
            <td :colspan="columns.length + 1" class="px-6 py-12 text-center">
              <EmptyState
                title="Aucune donnée"
                description="Il n'y a aucun enregistrement à afficher"
                @action="$emit('add')"
              />
            </td>
          </tr>

          <!-- Data rows -->
          <tr v-for="(row, idx) in rows" v-else :key="row.id" class="hover:bg-gray-50 transition-colors">
            <td v-for="column in columns" :key="column.key" class="px-6 py-4 text-gray-900">
              <component
                :is="column.component || 'div'"
                v-if="column.component"
                :value="row[column.key]"
              />
              <span v-else>{{ formatCell(row[column.key], column.type) }}</span>
            </td>
            <td class="px-6 py-4 text-right">
              <slot name="actions" :row="row"></slot>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Pagination -->
    <template #footer v-if="totalPages > 1">
      <div class="flex items-center justify-between">
        <p class="text-sm text-gray-600">
          Page {{ currentPage + 1 }} sur {{ totalPages }} ({{ totalElements }} total)
        </p>
        <div class="flex gap-2">
          <Button
            size="sm"
            variant="secondary"
            :disabled="currentPage === 0"
            @click="$emit('prev')"
          >
            Précédent
          </Button>
          <Button
            size="sm"
            variant="secondary"
            :disabled="currentPage >= totalPages - 1"
            @click="$emit('next')"
          >
            Suivant
          </Button>
        </div>
      </div>
    </template>
  </Card>
</template>

<script setup>
import Card from './Card.vue'
import Button from './Button.vue'
import SkeletonLoader from './SkeletonLoader.vue'
import EmptyState from './EmptyState.vue'

defineProps({
  title: {
    type: String,
    required: true,
  },
  columns: {
    type: Array,
    required: true,
  },
  rows: {
    type: Array,
    default: () => [],
  },
  loading: {
    type: Boolean,
    default: false,
  },
  currentPage: {
    type: Number,
    default: 0,
  },
  totalPages: {
    type: Number,
    default: 0,
  },
  totalElements: {
    type: Number,
    default: 0,
  },
})

defineEmits(['sort', 'prev', 'next', 'add'])

function formatCell(value, type) {
  if (value === null || value === undefined) {
    return '-'
  }

  switch (type) {
    case 'date':
      return new Date(value).toLocaleDateString('fr-FR')
    case 'datetime':
      return new Date(value).toLocaleString('fr-FR')
    case 'number':
      return Number(value).toFixed(2)
    case 'boolean':
      return value ? 'Oui' : 'Non'
    default:
      return String(value)
  }
}

function sort(key) {
  emit('sort', key)
}
</script>

<style scoped></style>
