<template>
  <RouterLink
    :to="to"
    class="flex items-center gap-3 px-4 py-3 rounded-lg text-sm font-medium transition-colors"
    :class="[
      isActive
        ? 'bg-primary-600 text-white'
        : 'text-gray-400 hover:bg-gray-800 hover:text-gray-100'
    ]"
  >
    <component :is="getIcon()" class="w-5 h-5" />
    <span>{{ label }}</span>
  </RouterLink>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { RouterLink } from 'vue-router'

const route = useRoute()

const props = defineProps({
  to: {
    type: String,
    required: true
  },
  label: {
    type: String,
    required: true
  },
  icon: {
    type: String,
    default: 'chart'
  }
})

const isActive = computed(() => {
  return route.path.startsWith(props.to)
})

function getIcon() {
  const icons = {
    chart: 'ChartBarIcon',
    users: 'UsersIcon',
    settings: 'CogIcon',
  }
  return icons[props.icon] || 'ChartBarIcon'
}

// Simple SVG icons instead of heroicons for now
</script>

<style scoped>
</style>
