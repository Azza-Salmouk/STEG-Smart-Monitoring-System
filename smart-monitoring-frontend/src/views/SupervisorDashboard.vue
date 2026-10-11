<template>
  <div class="space-y-6">
    <!-- Loading State -->
    <div v-if="loading" class="flex items-center justify-center h-64">
      <div class="text-gray-500">
        <div class="animate-spin inline-block w-8 h-8 border-4 border-blue-500 border-r-transparent rounded-full"></div>
        <p class="mt-2">Chargement des données...</p>
      </div>
    </div>

    <!-- Error State -->
    <div v-if="error" class="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded">
      {{ error }}
    </div>

    <!-- Header -->
    <DashboardHeader 
      v-if="!loading && !error"
      title="Tableau de bord de supervision"
      subtitle="Vue opérationnelle du système STEG"
      :loading="loading"
      @refresh="loadDashboard"
    />

    <!-- KPI Cards -->
    <div v-if="!loading && !error" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
      <DashboardKpiCard
        label="Postes Supervisés"
        :value="dashboardData.totalPostes"
        icon="🏢"
        type="primary"
      />
      <DashboardKpiCard
        label="Transformateurs"
        :value="dashboardData.totalTransformateurs"
        icon="⚡"
        type="warning"
      />
      <DashboardKpiCard
        label="Health Score Moyen"
        :value="Math.round(dashboardData.averageHealthScore || 0) + '%'"
        icon="📊"
        type="success"
      />
      <DashboardKpiCard
        label="Alertes Actives"
        :value="dashboardData.activeAlerts"
        icon="🚨"
        type="danger"
      />
    </div>

    <!-- Global State Section -->
    <DashboardState
      v-if="!loading && !error"
      :total-items="dashboardData.totalPostes + dashboardData.totalTransformateurs"
      :active-alerts="dashboardData.activeAlerts"
      :critical-count="dashboardData.criticalAlerts"
      :health-score="dashboardData.averageHealthScore"
    />

    <!-- Charts Row -->
    <div v-if="!loading && !error" class="grid grid-cols-1 lg:grid-cols-2 gap-6">
      <!-- Interventions by Status -->
      <DashboardCard title="Interventions par Statut">
        <div class="h-80">
          <Bar
            :data="chartData.interventions"
            :options="chartOptions"
          />
        </div>
      </DashboardCard>

      <!-- Complaints Distribution -->
      <DashboardCard title="Réclamations par Priorité">
        <div class="h-80">
          <Doughnut
            :data="chartData.complaints"
            :options="chartOptions"
          />
        </div>
      </DashboardCard>
    </div>

    <!-- Critical Indicators -->
    <CriticalIndicators
      v-if="!loading && !error"
      :critical-count="dashboardData.transformateursCritiques"
      :active-alerts="dashboardData.activeAlerts"
      :critical-alerts="dashboardData.criticalAlerts"
    />

    <!-- Status Summary Cards -->
    <div v-if="!loading && !error" class="grid grid-cols-1 md:grid-cols-3 gap-6">
      <!-- Interventions Status -->
      <DashboardCard title="Interventions">
        <div class="space-y-3">
          <div class="flex items-center justify-between p-2 bg-blue-50 rounded">
            <span class="text-sm font-medium text-blue-900">En Attente</span>
            <span class="text-lg font-bold text-blue-600">0</span>
          </div>
          <div class="flex items-center justify-between p-2 bg-orange-50 rounded">
            <span class="text-sm font-medium text-orange-900">En Cours</span>
            <span class="text-lg font-bold text-orange-600">{{ dashboardData.interventionsEnCours || 0 }}</span>
          </div>
          <div class="flex items-center justify-between p-2 bg-green-50 rounded">
            <span class="text-sm font-medium text-green-900">Terminées</span>
            <span class="text-lg font-bold text-green-600">{{ dashboardData.interventionsTerminees || 0 }}</span>
          </div>
        </div>
      </DashboardCard>

      <!-- Complaints Status -->
      <DashboardCard title="Réclamations">
        <div class="space-y-3">
          <div class="flex items-center justify-between p-2 bg-blue-50 rounded">
            <span class="text-sm font-medium text-blue-900">Ouvertes</span>
            <span class="text-lg font-bold text-blue-600">{{ dashboardData.openComplaints || 0 }}</span>
          </div>
          <div class="flex items-center justify-between p-2 bg-gray-50 rounded">
            <span class="text-sm font-medium text-gray-900">Critiques</span>
            <span class="text-lg font-bold text-red-600">{{ dashboardData.complaintStats?.critiques || 0 }}</span>
          </div>
          <div class="flex items-center justify-between p-2 bg-green-50 rounded">
            <span class="text-sm font-medium text-green-900">Résolues</span>
            <span class="text-lg font-bold text-green-600">{{ dashboardData.complaintStats?.resolues || 0 }}</span>
          </div>
        </div>
      </DashboardCard>

      <!-- Alerts Status -->
      <DashboardCard title="Alertes">
        <div class="space-y-3">
          <div class="flex items-center justify-between p-2 bg-red-50 rounded">
            <span class="text-sm font-medium text-red-900">Critiques</span>
            <span class="text-lg font-bold text-red-600">{{ dashboardData.criticalAlerts || 0 }}</span>
          </div>
          <div class="flex items-center justify-between p-2 bg-orange-50 rounded">
            <span class="text-sm font-medium text-orange-900">Actives</span>
            <span class="text-lg font-bold text-orange-600">{{ dashboardData.activeAlerts || 0 }}</span>
          </div>
          <div class="flex items-center justify-between p-2 bg-blue-50 rounded">
            <span class="text-sm font-medium text-blue-900">Total</span>
            <span class="text-lg font-bold text-blue-600">{{ dashboardData.totalAlerts || 0 }}</span>
          </div>
        </div>
      </DashboardCard>
    </div>

    <!-- Recent Activity -->
    <div v-if="!loading && !error && dashboardData.recentItems" class="grid grid-cols-1 lg:grid-cols-3 gap-6">
      <!-- Recent Alerts -->
      <DashboardCard title="Alertes Récentes">
        <div v-if="dashboardData.recentItems.recentAlerts.length === 0" class="text-center py-6 text-gray-500">
          Aucune alerte
        </div>
        <div v-else class="space-y-3">
          <div v-for="alert in dashboardData.recentItems.recentAlerts.slice(0, 5)" :key="alert.id" class="flex items-start gap-3 p-2 hover:bg-gray-50 rounded">
            <div class="text-lg">{{ getRiskIcon(alert.niveau) }}</div>
            <div class="flex-1 min-w-0">
              <p class="text-sm font-medium text-gray-900 truncate">{{ alert.titre }}</p>
              <p class="text-xs text-gray-500">{{ formatDate(alert.createdAt) }}</p>
            </div>
          </div>
        </div>
      </DashboardCard>

      <!-- Recent Interventions -->
      <DashboardCard title="Interventions Récentes">
        <div v-if="dashboardData.recentItems.recentInterventions.length === 0" class="text-center py-6 text-gray-500">
          Aucune intervention
        </div>
        <div v-else class="space-y-3">
          <div v-for="intervention in dashboardData.recentItems.recentInterventions.slice(0, 5)" :key="intervention.id" class="flex items-center justify-between p-2 hover:bg-gray-50 rounded">
            <div>
              <p class="text-sm font-medium text-gray-900">{{ intervention.reference }}</p>
              <p class="text-xs text-gray-500">{{ formatDate(intervention.createdAt) }}</p>
            </div>
            <span class="inline-block px-2 py-1 text-xs font-medium rounded" :class="getStatutClass(intervention.statut)">
              {{ intervention.statut }}
            </span>
          </div>
        </div>
      </DashboardCard>

      <!-- Recent Complaints -->
      <DashboardCard title="Réclamations Récentes">
        <div v-if="dashboardData.recentItems.recentComplaints.length === 0" class="text-center py-6 text-gray-500">
          Aucune réclamation
        </div>
        <div v-else class="space-y-3">
          <div v-for="complaint in dashboardData.recentItems.recentComplaints.slice(0, 5)" :key="complaint.id" class="flex items-start gap-3 p-2 hover:bg-gray-50 rounded">
            <div class="text-lg">📋</div>
            <div class="flex-1 min-w-0">
              <p class="text-sm font-medium text-gray-900 truncate">{{ complaint.description }}</p>
              <p class="text-xs text-gray-500">{{ formatDate(complaint.createdAt) }}</p>
            </div>
          </div>
        </div>
      </DashboardCard>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
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
import { dashboardAPI } from '@/services/api'
import DashboardHeader from '@/components/dashboard/DashboardHeader.vue'
import DashboardState from '@/components/dashboard/DashboardState.vue'
import DashboardCard from '@/components/dashboard/DashboardCard.vue'
import DashboardKpiCard from '@/components/dashboard/DashboardKpiCard.vue'
import CriticalIndicators from '@/components/dashboard/CriticalIndicators.vue'

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

const dashboardData = ref({
  totalPostes: 0,
  totalTransformateurs: 0,
  averageHealthScore: 0,
  activeAlerts: 0,
  criticalAlerts: 0,
  interventionsEnCours: 0,
  interventionsTerminees: 0,
  openComplaints: 0,
  transformateursCritiques: 0,
  totalAlerts: 0,
  recentItems: {
    recentAlerts: [],
    recentInterventions: [],
    recentComplaints: [],
  },
  alertStats: {},
  interventionStats: {},
  complaintStats: {},
  transformateurStats: {},
  posteStats: {},
})

const chartData = ref({
  interventions: {
    labels: ['En Attente', 'En Cours', 'Terminées', 'Annulées'],
    datasets: [
      {
        label: 'Interventions',
        data: [0, 0, 0, 0],
        backgroundColor: [
          'rgba(59, 130, 246, 0.8)',
          'rgba(251, 146, 60, 0.8)',
          'rgba(34, 197, 94, 0.8)',
          'rgba(239, 68, 68, 0.8)',
        ],
        borderRadius: 8,
      },
    ],
  },
  complaints: {
    labels: ['Critique', 'Haute', 'Moyenne', 'Basse'],
    datasets: [
      {
        label: 'Réclamations',
        data: [0, 0, 0, 0],
        backgroundColor: [
          'rgba(239, 68, 68, 0.8)',
          'rgba(251, 146, 60, 0.8)',
          'rgba(234, 179, 8, 0.8)',
          'rgba(34, 197, 94, 0.8)',
        ],
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
    const response = await dashboardAPI.getSupervisorDashboard()
    const data = response.data

    dashboardData.value = {
      totalPostes: data.totalPostes || 0,
      totalTransformateurs: data.totalTransformateurs || 0,
      averageHealthScore: data.averageHealthScore || 0,
      activeAlerts: data.activeAlerts || 0,
      criticalAlerts: data.criticalAlerts || 0,
      interventionsEnCours: data.interventionsEnCours || 0,
      interventionsTerminees: data.interventionsTerminees || 0,
      openComplaints: data.openComplaints || 0,
      transformateursCritiques: data.transformateursCritiques || 0,
      totalAlerts: data.totalAlerts || 0,
      recentItems: {
        recentAlerts: data.recentItems?.recentAlerts || [],
        recentInterventions: data.recentItems?.recentInterventions || [],
        recentComplaints: data.recentItems?.recentComplaints || [],
      },
      alertStats: data.alertStats || {},
      interventionStats: data.interventionStats || {},
      complaintStats: data.complaintStats || {},
      transformateurStats: data.transformateurStats || {},
      posteStats: data.posteStats || {},
    }

    updateChartData(data)
  } catch (err) {
    console.error('Erreur chargement supervisor dashboard:', err)
    error.value = 'Impossible de charger les données du dashboard. Veuillez réessayer.'
  } finally {
    loading.value = false
  }
}

function updateChartData(data) {
  const interventionStats = data.interventionStats || {}
  chartData.value.interventions.datasets[0].data = [
    interventionStats.enAttente || 0,
    interventionStats.enCours || 0,
    interventionStats.terminees || 0,
    interventionStats.annulees || 0,
  ]

  const complaintStats = data.complaintStats || {}
  chartData.value.complaints.datasets[0].data = [
    complaintStats.critiques || 0,
    complaintStats.haute || 0,
    complaintStats.moyenne || 0,
    complaintStats.basse || 0,
  ]
}

function getRiskIcon(niveau) {
  const map = {
    'CRITIQUE': '🔴',
    'HAUTE': '🟠',
    'MOYENNE': '🟡',
    'BASSE': '🟢',
  }
  return map[niveau] || '⚪'
}

function getStatutClass(statut) {
  const map = {
    'EN_ATTENTE': 'bg-blue-100 text-blue-800',
    'EN_COURS': 'bg-orange-100 text-orange-800',
    'TERMINEE': 'bg-green-100 text-green-800',
    'ANNULEE': 'bg-red-100 text-red-800',
  }
  return map[statut] || 'bg-gray-100 text-gray-800'
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

