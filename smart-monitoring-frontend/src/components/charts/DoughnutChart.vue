<template>
  <div class="w-full h-full">
    <Doughnut :data="computedData" :options="computedOptions" />
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { Doughnut } from 'vue-chartjs'
import {
  Chart as ChartJS,
  ArcElement,
  Tooltip,
  Legend,
} from 'chart.js'

ChartJS.register(ArcElement, Tooltip, Legend)

const props = defineProps({
  labels: {
    type: Array,
    required: true,
  },
  datasets: {
    type: Array,
    required: true,
  },
  title: {
    type: String,
    default: 'Doughnut Chart',
  },
  options: {
    type: Object,
    default: () => ({}),
  },
})

const computedData = computed(() => ({
  labels: props.labels,
  datasets: props.datasets,
}))

const computedOptions = computed(() => ({
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: {
      position: 'right',
    },
    title: {
      display: false,
    },
  },
  ...props.options,
}))
</script>
