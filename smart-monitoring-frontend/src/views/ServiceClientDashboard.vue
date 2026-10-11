<template>
  <div class="space-y-6">
    <!-- Header -->
    <DashboardHeader
      title="Dashboard Service Client"
      subtitle="Gestion des réclamations et suivi"
      :loading="loading"
      @refresh="loadDashboard"
    />

    <!-- State Wrapper -->
    <DashboardState
      :loading="loading"
      :error="error"
      loading-message="Chargement des données du dashboard..."
      error-title="Impossible de charger le dashboard"
    >
      <!-- KPI Stats -->
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <DashboardKpiCard
          label="Total Réclamations"
          :value="dashboardData.totalComplaints"
          icon="📋"
          type="primary"
          :show-progress="true"
        />
        <DashboardKpiCard
          label="Ouvertes"
          :value="calculateOpenComplaints()"
          icon="⏳"
          type="warning"
          :show-progress="true"
        />
        <DashboardKpiCard
          label="Critiques"
          :value="dashboardData.criticalComplaints || 0"
          icon="🚨"
          type="danger"
          :show-progress="true"
        />
        <DashboardKpiCard
          label="Résolues"
          :value="dashboardData.complaintStats?.resolues || 0"
          icon="✅"
          type="success"
          :show-progress="true"
        />
      </div>

      <!-- Charts Row -->
      <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <!-- Complaints by Status -->
        <DashboardCard title="Réclamations par statut">
          <div class="h-80">
            <Doughnut
              :data="chartData.complaintStatus"
              :options="chartOptions"
            />
          </div>
        </DashboardCard>

        <!-- Complaints by Priority -->
        <DashboardCard title="Réclamations par priorité">
          <div class="h-80">
            <Bar
              :data="chartData.complaintPriority"
              :options="chartOptions"
            />
          </div>
        </DashboardCard>
      </div>

      <!-- Status Summary -->
      <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
        <!-- Status Breakdown -->
        <DashboardCard title="Statuts">
          <div class="space-y-2">
            <div class="flex justify-between items-center p-2 bg-blue-50 rounded">
              <span class="text-sm font-medium">Nouvelle</span>
              <span class="text-lg font-bold text-blue-600">{{ dashboardData.complaintStats?.nouvelles || 0 }}</span>
            </div>
            <div class="flex justify-between items-center p-2 bg-purple-50 rounded">
              <span class="text-sm font-medium">En Analyse</span>
              <span class="text-lg font-bold text-purple-600">{{ dashboardData.complaintStats?.enAnalyse || 0 }}</span>
            </div>
            <div class="flex justify-between items-center p-2 bg-yellow-50 rounded">
              <span class="text-sm font-medium">En Traitement</span>
              <span class="text-lg font-bold text-yellow-600">{{ dashboardData.complaintStats?.enTraitement || 0 }}</span>
            </div>
          </div>
        </DashboardCard>

        <!-- Resolution Stats -->
        <DashboardCard title="Résolution">
          <div class="space-y-2">
            <div class="flex justify-between items-center p-2 bg-green-50 rounded">
              <span class="text-sm font-medium">Résolues</span>
              <span class="text-lg font-bold text-green-600">{{ dashboardData.complaintStats?.resolues || 0 }}</span>
            </div>
            <div class="flex justify-between items-center p-2 bg-gray-50 rounded">
              <span class="text-sm font-medium">Fermées</span>
              <span class="text-lg font-bold text-gray-600">{{ dashboardData.complaintStats?.fermees || 0 }}</span>
            </div>
            <div class="flex justify-between items-center p-2 bg-blue-50 rounded">
              <span class="text-sm font-medium">Taux Résolution</span>
              <span class="text-lg font-bold text-blue-600">{{ calculateResolutionRate() }}%</span>
            </div>
          </div>
        </DashboardCard>

        <!-- Priority Breakdown -->
        <DashboardCard title="Priorités">
          <div class="space-y-2">
            <div class="flex justify-between items-center p-2 bg-red-50 rounded">
              <span class="text-sm font-medium">Critique</span>
              <span class="text-lg font-bold text-red-600">{{ dashboardData.criticalComplaints || 0 }}</span>
            </div>
            <div class="flex justify-between items-center p-2 bg-orange-50 rounded">
              <span class="text-sm font-medium">Haute</span>
              <span class="text-lg font-bold text-orange-600">0</span>
            </div>
            <div class="flex justify-between items-center p-2 bg-blue-50 rounded">
              <span class="text-sm font-medium">Moyenne</span>
              <span class="text-lg font-bold text-blue-600">0</span>
            </div>
          </div>
        </DashboardCard>
      </div>

      <!-- Recent Complaints Table -->
      <DashboardCard title="Réclamations récentes">
        <div class="overflow-x-auto">
          <table class="w-full text-sm">
            <thead class="bg-gray-50 border-b">
              <tr>
                <th class="px-6 py-3 text-left font-semibold text-gray-700">Référence</th>
                <th class="px-6 py-3 text-left font-semibold text-gray-700">Description</th>
                <th class="px-6 py-3 text-left font-semibold text-gray-700">Priorité</th>
                <th class="px-6 py-3 text-left font-semibold text-gray-700">Statut</th>
                <th class="px-6 py-3 text-left font-semibold text-gray-700">Créée</th>
              </tr>
            </thead>
            <tbody class="divide-y">
              <tr v-if="dashboardData.recentItems?.recentComplaints?.length === 0">
                <td colspan="5" class="px-6 py-12 text-center text-gray-500">
                  Aucune réclamation
                </td>
              </tr>

              <tr v-for="complaint in dashboardData.recentItems?.recentComplaints?.slice(0, 10) || []" :key="complaint.id" class="hover:bg-gray-50">
                <td class="px-6 py-4 text-gray-900 font-medium">{{ complaint.id }}</td>
                <td class="px-6 py-4 text-gray-900 truncate">{{ complaint.description }}</td>
                <td class="px-6 py-4">
                  <span :class="getPriorityClass(complaint.priorite)" class="inline-flex items-center px-2 py-1 rounded-full text-xs font-medium">
                    {{ formatPriority(complaint.priorite) }}
                  </span>
                </td>
                <td class="px-6 py-4">
                  <span :class="getStatusClass(complaint.statut)" class="inline-flex items-center px-2 py-1 rounded-full text-xs font-medium">
                    {{ formatStatus(complaint.statut) }}
                  </span>
                </td>
                <td class="px-6 py-4 text-gray-900">{{ formatDate(complaint.createdAt) }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </DashboardCard>
    </DashboardState>

    <!-- Modal pour créer -->
    <ServiceClientComplaintModal
      v-if="showNewComplaint"
      @save="handleNewComplaint"
      @close="showNewComplaint = false"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import DashboardHeader from '@/components/dashboard/DashboardHeader.vue'
import DashboardState from '@/components/dashboard/DashboardState.vue'
import DashboardCard from '@/components/dashboard/DashboardCard.vue'
import DashboardKpiCard from '@/components/dashboard/DashboardKpiCard.vue'
import { Bar, Doughnut } from 'vue-chartjs'
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  BarElement,
  Title,
  Tooltip,
  Legend,
  ArcElement,
} from 'chart.js'
import ServiceClientComplaintModal from '@/components/complaint/ServiceClientComplaintModal.vue'
import { dashboardAPI } from '@/services/api'

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  BarElement,
  Title,
  Tooltip,
  Legend,
  ArcElement,
)

const loading = ref(true)
const error = ref(null)
const showNewComplaint = ref(false)

const dashboardData = ref({
  totalComplaints: 0,
  criticalComplaints: 0,
  recentItems: {
    recentComplaints: [],
  },
  complaintStats: {},
})

const chartData = ref({
  complaintStatus: {
    labels: ['Nouvelle', 'En Analyse', 'Assignée', 'En Traitement', 'Résolue', 'Fermée'],
    datasets: [
      {
        label: 'Réclamations',
        data: [0, 0, 0, 0, 0, 0],
        backgroundColor: [
          'rgba(59, 130, 246, 0.8)',
          'rgba(139, 92, 246, 0.8)',
          'rgba(6, 182, 212, 0.8)',
          'rgba(234, 179, 8, 0.8)',
          'rgba(34, 197, 94, 0.8)',
          'rgba(107, 114, 128, 0.8)',
        ],
      },
    ],
  },
  complaintPriority: {
    labels: ['Critique', 'Haute', 'Moyenne', 'Basse'],
    datasets: [
      {
        label: 'Priorité',
        data: [0, 0, 0, 0],
        backgroundColor: [
          'rgba(239, 68, 68, 0.8)',
          'rgba(251, 146, 60, 0.8)',
          'rgba(234, 179, 8, 0.8)',
          'rgba(34, 197, 94, 0.8)',
        ],
        borderRadius: 8,
      },
    ],
  },
})

const chartOptions = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: {
      position: 'top',
    },
  },
  scales: {
    y: {
      beginAtZero: true,
    },
  },
}

onMounted(async () => {
  await loadDashboard()
})

async function loadDashboard() {
  try {
    loading.value = true
    error.value = null
    const response = await dashboardAPI.getServiceClientDashboard()
    const data = response.data

    dashboardData.value = {
      totalComplaints: data.totalComplaints || 0,
      criticalComplaints: data.complaintStats?.critiques || 0,
      recentItems: {
        recentComplaints: data.recentItems?.recentComplaints || [],
      },
      complaintStats: data.complaintStats || {},
    }

    updateChartData(data)
  } catch (err) {
    console.error('Erreur chargement service client dashboard:', err)
    error.value = 'Impossible de charger les données du dashboard. Veuillez réessayer.'
  } finally {
    loading.value = false
  }
}

function updateChartData(data) {
  const stats = data.complaintStats || {}
  chartData.value.complaintStatus.datasets[0].data = [
    stats.nouvelles || 0,
    stats.enAnalyse || 0,
    stats.assignees || 0,
    stats.enTraitement || 0,
    stats.resolues || 0,
    stats.fermees || 0,
  ]

  // Priority breakdown - if not available in backend, show 0
  const priorityStats = data.byPriority || {}
  chartData.value.complaintPriority.datasets[0].data = [
    priorityStats.critique || 0,
    priorityStats.haute || 0,
    priorityStats.moyenne || 0,
    priorityStats.basse || 0,
  ]
}

function handleNewComplaint() {
  loadDashboard()
  showNewComplaint.value = false
}

function calculateOpenComplaints() {
  const stats = dashboardData.value.complaintStats
  return (stats.nouvelles || 0) + (stats.enAnalyse || 0) + (stats.assignees || 0) + (stats.enTraitement || 0)
}

function calculateResolutionRate() {
  const total = dashboardData.value.totalComplaints
  if (total === 0) return 0
  const resolved = dashboardData.value.complaintStats?.resolues || 0
  return Math.round((resolved / total) * 100)
}

function formatCategory(cat) {
  const cats = { COUPURE: 'Coupure', SURCHARGE: 'Surcharge', PANNE: 'Panne', FACTURATION: 'Facturation', BRUIT: 'Bruit', ODEUR: 'Odeur', AUTRE: 'Autre' }
  return cats[cat] || cat
}

function formatPriority(pri) {
  const pris = { LOW: 'Faible', MEDIUM: 'Moyenne', HIGH: 'Haute', CRITICAL: 'Critique' }
  return pris[pri] || pri
}

function formatStatus(stat) {
  const stats = { NOUVELLE: 'Nouvelle', EN_ANALYSE: 'En analyse', ASSIGNEE: 'Assignée', EN_TRAITEMENT: 'En traitement', RESOLUE: 'Résolue', FERMEE: 'Fermée' }
  return stats[stat] || stat
}

function getPriorityClass(pri) {
  switch (pri) {
    case 'LOW':
      return 'bg-gray-100 text-gray-800'
    case 'MEDIUM':
      return 'bg-blue-100 text-blue-800'
    case 'HIGH':
      return 'bg-orange-100 text-orange-800'
    case 'CRITICAL':
      return 'bg-red-100 text-red-800'
    default:
      return 'bg-gray-100 text-gray-800'
  }
}

function getStatusClass(stat) {
  switch (stat) {
    case 'NOUVELLE':
      return 'bg-blue-100 text-blue-800'
    case 'EN_ANALYSE':
      return 'bg-purple-100 text-purple-800'
    case 'ASSIGNEE':
      return 'bg-cyan-100 text-cyan-800'
    case 'EN_TRAITEMENT':
      return 'bg-yellow-100 text-yellow-800'
    case 'RESOLUE':
      return 'bg-green-100 text-green-800'
    case 'FERMEE':
      return 'bg-gray-100 text-gray-800'
    default:
      return 'bg-gray-100 text-gray-800'
  }
}

function formatDate(date) {
  if (!date) return ''
  return new Date(date).toLocaleDateString('fr-FR', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
  })
}
</script>

<style scoped>
.animate-spin {
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}
</style>

