<template>
  <div class="space-y-6">
    <!-- Header -->
    <DashboardHeader
      title="Dashboard Agent"
      subtitle="Vue d'exécution - Interventions et alertes"
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
      <!-- KPI Cards -->
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <DashboardKpiCard
          label="Interventions totales"
          :value="dashboardData.totalInterventions"
          icon="🔧"
          type="primary"
          :show-progress="true"
        />
        <DashboardKpiCard
          label="En cours"
          :value="dashboardData.interventionsEnCours"
          icon="⏳"
          type="warning"
          :show-progress="true"
        />
        <DashboardKpiCard
          label="Complétées"
          :value="dashboardData.interventionsCompleted"
          icon="✅"
          type="success"
          :show-progress="true"
        />
        <DashboardKpiCard
          label="Alertes actives"
          :value="dashboardData.totalAlerts"
          icon="🚨"
          type="danger"
          :show-progress="true"
        />
      </div>

      <!-- Charts Row -->
      <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <!-- Interventions by Status -->
        <DashboardCard title="Interventions par statut">
          <div class="h-80">
            <Doughnut
              :data="chartData.myInterventions"
              :options="chartOptions"
            />
          </div>
        </DashboardCard>

        <!-- Interventions by Priority -->
        <DashboardCard title="Interventions par priorité">
          <div class="h-80">
            <Bar
              :data="chartData.priorities"
              :options="chartOptions"
            />
          </div>
        </DashboardCard>
      </div>

      <!-- Recent Interventions -->
      <div v-if="dashboardData.recentItems" class="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <!-- My Recent Interventions -->
        <DashboardCard title="Interventions récentes">
          <div v-if="dashboardData.recentItems.recentInterventions.length === 0" class="text-center py-6 text-gray-500">
            Aucune intervention
          </div>
          <div v-else class="space-y-3">
            <div v-for="intervention in dashboardData.recentItems.recentInterventions.slice(0, 5)" :key="intervention.id" class="flex items-start gap-3 p-2 hover:bg-gray-50 rounded">
              <div class="text-lg">⚙️</div>
              <div class="flex-1 min-w-0">
                <p class="text-sm font-medium text-gray-900">{{ intervention.reference }}</p>
                <p class="text-xs text-gray-500">{{ formatDate(intervention.createdAt) }}</p>
                <span class="inline-block mt-1 px-2 py-1 text-xs font-medium rounded" :class="getStatutClass(intervention.statut)">
                  {{ intervention.statut }}
                </span>
              </div>
            </div>
          </div>
        </DashboardCard>

        <!-- Related Alerts -->
        <DashboardCard title="Alertes associées">
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
      </div>
    </DashboardState>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import DashboardHeader from '@/components/dashboard/DashboardHeader.vue'
import DashboardState from '@/components/dashboard/DashboardState.vue'
import DashboardCard from '@/components/dashboard/DashboardCard.vue'
import DashboardKpiCard from '@/components/dashboard/DashboardKpiCard.vue'
import CriticalIndicators from '@/components/dashboard/CriticalIndicators.vue'
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
import { useAuthStore } from '@/stores/authStore'

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

const authStore = useAuthStore()
const loading = ref(true)
const error = ref(null)

const dashboardData = ref({
  totalInterventions: 0,
  interventionsEnCours: 0,
  interventionsCompleted: 0,
  totalAlerts: 0,
  criticalAlerts: 0,
  myInterventionsEnAttente: 0,
  myInterventionsEnCours: 0,
  myInterventionsTerminees: 0,
  myInterventionsCritiques: 0,
  recentItems: {
    recentInterventions: [],
    recentAlerts: [],
  },
  interventionStatistics: {},
})

const chartData = ref({
  myInterventions: {
    labels: ['En Attente', 'En Cours', 'Complétées', 'Annulées'],
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
      },
    ],
  },
  priorities: {
    labels: ['Critique', 'Urgente', 'Normale', 'Basse'],
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
    
    // Obtenir l'ID de l'agent actuellement connecté
    const agentId = authStore.user?.id
    if (!agentId) {
      error.value = 'ID utilisateur non disponible. Veuillez vous reconnecter.'
      return
    }
    
    const response = await dashboardAPI.getAgentDashboard(agentId)
    const data = response.data

    dashboardData.value = {
      totalInterventions: data.totalInterventions || 0,
      interventionsEnCours: data.interventionsEnCours || 0,
      interventionsCompleted: data.interventionsCompleted || 0,
      totalAlerts: data.totalAlerts || 0,
      criticalAlerts: data.criticalAlerts || 0,
      myInterventionsEnAttente: data.myInterventionsEnAttente || 0,
      myInterventionsEnCours: data.myInterventionsEnCours || 0,
      myInterventionsTerminees: data.myInterventionsTerminees || 0,
      myInterventionsCritiques: data.myInterventionsCritiques || 0,
      recentItems: {
        recentInterventions: data.recentItems?.recentInterventions || [],
        recentAlerts: data.recentItems?.recentAlerts || [],
      },
      interventionStatistics: data.interventionStatistics || {},
    }

    updateChartData(data)
  } catch (err) {
    console.error('Erreur chargement agent dashboard:', err)
    error.value = 'Impossible de charger les données du dashboard. Veuillez réessayer.'
  } finally {
    loading.value = false
  }
}

function updateChartData(data) {
  // Backend returns properties directly at the root level
  chartData.value.myInterventions.datasets[0].data = [
    data.myInterventionsEnAttente || 0,
    data.myInterventionsEnCours || 0,
    data.myInterventionsTerminees || 0,
    data.myInterventionsCritiques || 0,
  ]

  // Priority statistics - if not available from backend, use 0
  const priorityStats = data.byPriority || {}
  chartData.value.priorities.datasets[0].data = [
    priorityStats.critique || 0,
    priorityStats.haute || 0,
    priorityStats.moyenne || 0,
    priorityStats.basse || 0,
  ]
}

function getRoleName(role) {
  const roles = {
    'ADMIN': 'Administrateur',
    'SUPERVISOR': 'Superviseur',
    'AGENT': 'Agent',
    'SERVICE_CLIENT': 'Client Service'
  }
  return roles[role] || role
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

function calculateCompletionRate() {
  const total = dashboardData.value.totalInterventions
  if (total === 0) return 0
  return Math.round((dashboardData.value.interventionsCompleted / total) * 100)
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

