<template>
  <div class="flex items-center gap-2">
    <div class="flex-1">
      <div class="w-full bg-gray-200 rounded-full h-2">
        <div
          :style="{ width: score + '%' }"
          :class="[
            'h-2 rounded-full transition-all duration-300',
            score >= 80 ? 'bg-green-500' :
            score >= 60 ? 'bg-yellow-500' :
            score >= 40 ? 'bg-orange-500' :
            'bg-red-500'
          ]"
        ></div>
      </div>
    </div>
    <span
      :class="[
        'text-sm font-bold min-w-12 text-right',
        score >= 80 ? 'text-green-600' :
        score >= 60 ? 'text-yellow-600' :
        score >= 40 ? 'text-orange-600' :
        'text-red-600'
      ]"
    >
      {{ score }}%
    </span>
  </div>
</template>

<script setup>
defineProps({
  score: {
    type: Number,
    required: true,
    validator: (value) => value >= 0 && value <= 100
  }
})
</script>

<style scoped></style>
