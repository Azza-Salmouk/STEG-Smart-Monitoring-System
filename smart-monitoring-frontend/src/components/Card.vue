<template>
  <div :class="['bg-white rounded-lg shadow-md overflow-hidden transition-all duration-200 hover:shadow-lg', hoverable && 'hover:shadow-xl cursor-pointer']">
    <!-- Header -->
    <div v-if="$slots.header || title" class="px-6 py-4 border-b border-gray-200">
      <slot name="header">
        <div class="flex items-center justify-between">
          <h3 class="text-lg font-semibold text-gray-900">{{ title }}</h3>
          <slot name="header-action"></slot>
        </div>
      </slot>
    </div>

    <!-- Body -->
    <div class="px-6 py-4">
      <slot></slot>
    </div>

    <!-- Footer -->
    <div v-if="$slots.footer" class="px-6 py-4 border-t border-gray-200 bg-gray-50">
      <slot name="footer"></slot>
    </div>
  </div>
</template>

<script setup>
defineProps({
  title: {
    type: String,
    default: null,
  },
  hoverable: {
    type: Boolean,
    default: false,
  },
})
</script>

<style scoped></style>
