<template>
  <div class="space-y-6 bg-gradient-to-b from-gray-50 to-white min-h-screen p-6">
    <!-- Header with Reusable Component -->
    <DashboardHeader
      title="Vue globale de la supervision"
      subtitle="Dashboard administrateur - Supervision dynamique et en temps réel du système STEG"
      :loading="loading"
      :lastUpdate="lastUpdate"
      @refresh="loadDashboard"
    />

    <!-- State Management Component -->
    <DashboardState
      :loading="loading"
      :error="error"
      loadingMessage="Chargement des données de supervision..."
      errorTitle="Erreur de chargement des données"
      :empty="false"
    >
      <!-- Main Content -->
      <!-- KPI Cards Section - 8 Cards Grid -->
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mb-6">
        <!-- KPI 1: Postes -->
        <DashboardKpiCard
          label="Postes"
          :value="dashboardData.totalPostes"
          icon="📍"
          type="primary"
          :description="`${dashboardData.posteStats?.posteActifs || 0} actifs`"
          @click="navigateToPostes"
        />
        <!-- KPI 2: Transformateurs -->
        <DashboardKpiCard
          label="Transformateurs"
          :value="dashboardData.totalTransformateurs"
          icon="⚡"
          type="info"
          :description="`${dashboardData.transformateurStats?.transformateursActifs || 0} actifs`"
          @click="navigateToTransformateurs"
        />
        <!-- KPI 3: À Surveiller -->
        <DashboardKpiCard
          label="À Surveiller"
          :value="dashboardData.recentItems?.riskTransformateurs?.length || 0"
          icon="⚠️"
          type="warning"
          :description="dashboardData.recentItems?.riskTransformateurs?.length > 0 ? 'Attention requise' : 'Tous en bon état'"
          @click="navigateToRiskTransformateurs"
        />
        <!-- KPI 4: Alertes Actives -->
        <DashboardKpiCard
          label="Alertes Actives"
          :value="dashboardData.alertStats?.activeAlerts || 0"
          icon="🔔"
          type="warning"
          :description="`${dashboardData.alertStats?.totalAlerts || 0} total`"
          @click="navigateToAlerts"
        />
      </div>

      <!-- Second Row: 4 more KPI Cards -->
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mb-6">
        <!-- KPI 5: Alertes Critiques -->
        <DashboardKpiCard
          label="Critiques"
          :value="dashboardData.alertStats?.criticalCount || 0"
          icon="🔴"
          type="danger"
          :description="(dashboardData.alertStats?.criticalCount || 0) > 0 ? 'Action immédiate' : 'Aucune'"
          @click="navigateToAlerts"
        />
        <!-- KPI 6: Interventions En Cours -->
        <DashboardKpiCard
          label="Interventions"
          :value="dashboardData.interventionStats?.enCours || 0"
          icon="🔧"
          type="info"
          :description="`${dashboardData.interventionStats?.totalInterventions || 0} total`"
          @click="navigateToInterventions"
        />
        <!-- KPI 7: Réclamations En Attente -->
        <DashboardKpiCard
          label="Réclamations"
          :value="dashboardData.complaintsUnresolved || 0"
          icon="📋"
          type="warning"
          :description="`${dashboardData.totalComplaints || 0} total`"
          @click="navigateToComplaints"
        />
        <!-- KPI 8: Health Score Moyen -->
        <DashboardKpiCard
          label="Health Score"
          :value="`${Math.round(dashboardData.averageHealthScore || 0)}%`"
          icon="❤️"
          type="success"
          showProgress
          :description="dashboardData.averageHealthScore >= 80 ? 'Excellent' : dashboardData.averageHealthScore >= 60 ? 'Bon' : 'À surveiller'"
        />
      </div>

      <!-- Section 1: État du Parc (Left) + Indicateurs Critiques (Right) -->
      <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <!-- État Global du Parc -->
        <DashboardCard title="État global du parc" class="shadow-md">
          <div class="h-80">
            <Doughnut
              :data="chartData.transformateurStatus"
              :options="chartOptions"
            />
          </div>
        </DashboardCard>

        <!-- Indicateurs Critiques -->
        <DashboardCard title="Indicateurs en temps réel" class="shadow-md">
          <CriticalIndicators
            criticalLabel="Transformateurs Critiques"
            criticalDescription="Nécessitent attention immédiate"
            :criticalValue="dashboardData.transformateurStats?.transformateursCritiques || 0"
            warningLabel="Alertes Non Résolues"
            warningDescription="En cours de traitement"
            :warningValue="(dashboardData.alertStats?.activeAlerts || 0)"
            infoLabel="Interventions En Cours"
            infoDescription="À suivre et compléter"
            :infoValue="dashboardData.interventionStats?.enCours || 0"
            pendingLabel="Réclamations Ouvertes"
            pendingDescription="À traiter"
            :pendingValue="dashboardData.complaintsUnresolved || 0"
          />
        </DashboardCard>
      </div>

      <!-- Section 2: Alertes Intelligentes -->
      <DashboardCard title="Alertes Intelligentes - 5 Plus Récentes" class="shadow-md">
        <div v-if="dashboardData.recentItems?.recentAlerts && dashboardData.recentItems.recentAlerts.length > 0" class="space-y-2">
          <div
            v-for="alert in dashboardData.recentItems.recentAlerts.slice(0, 5)"
            :key="alert.id"
            class="flex items-center gap-3 p-4 border border-gray-200 rounded-lg hover:bg-gray-50 cursor-pointer transition"
            @click="navigateToAlerts"
          >
            <div class="w-1 h-10 rounded flex-shrink-0" :class="getAlertColor(alert.level)"></div>
            <div class="flex-1 min-w-0">
              <p class="text-sm font-semibold text-gray-900">{{ alert.title }}</p>
              <p class="text-xs text-gray-600">
                <strong>{{ alert.transformateurReference }}</strong> • {{ formatDate(alert.createdAt) }}
              </p>
            </div>
            <span class="text-xs font-semibold px-3 py-1 rounded-full whitespace-nowrap flex-shrink-0" :class="getAlertBadgeClass(alert.level)">
              {{ alert.level }}
            </span>
          </div>
          <button
            @click="navigateToAlerts"
            class="mt-4 w-full py-2 text-center text-sm text-blue-600 hover:text-blue-700 font-medium border border-blue-200 rounded hover:bg-blue-50 transition"
          >
            Voir toutes les alertes →
          </button>
        </div>
        <div v-else class="text-center py-8 text-gray-500">
          ✅ Aucune alerte pour le moment - Système en bon état
        </div>
      </DashboardCard>

      <!-- Section 3: Transformateurs à Surveiller -->
      <DashboardCard title="Transformateurs à Surveiller" class="shadow-md">
        <div v-if="dashboardData.recentItems?.riskTransformateurs && dashboardData.recentItems.riskTransformateurs.length > 0" class="space-y-2">
          <p class="text-sm text-gray-600 mb-4">
            <strong>{{ dashboardData.recentItems.riskTransformateurs.length }}</strong> transformateur(s) nécessitant attention
          </p>
          <div class="space-y-2">
            <div
              v-for="transf in dashboardData.recentItems.riskTransformateurs.slice(0, 5)"
              :key="transf.id"
              class="flex items-center justify-between p-4 border border-orange-200 bg-orange-50 rounded-lg hover:bg-orange-100 cursor-pointer transition"
              @click="navigateToTransformateurDetail(transf.id)"
            >
              <div class="flex-1">
                <p class="text-sm font-semibold text-gray-900">{{ transf.reference }}</p>
                <p class="text-xs text-gray-600">
                  📍 Poste: <strong>{{ transf.posteCode || 'N/A' }}</strong> • ❤️ Health: <strong>{{ transf.healthScore }}%</strong>
                </p>
              </div>
              <div class="text-right flex-shrink-0">
                <span class="text-xs font-semibold px-3 py-1 rounded-full" :class="getHealthBadgeClass(transf.healthScore)">
                  {{ transf.riskLevel || 'MEDIUM' }}
                </span>
              </div>
            </div>
          </div>
        </div>
        <div v-else class="text-center py-8 text-gray-500">
          ✅ Tous les transformateurs sont en bon état
        </div>
      </DashboardCard>

      <!-- Section 4: Interventions & Réclamations -->
      <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <!-- Recent Interventions -->
        <DashboardCard title="Interventions Récentes" class="shadow-md">
          <div v-if="dashboardData.recentItems?.recentInterventions && dashboardData.recentItems.recentInterventions.length > 0">
            <div class="space-y-2">
              <div
                v-for="intervention in dashboardData.recentItems.recentInterventions.slice(0, 5)"
                :key="intervention.id"
                class="p-3 border border-gray-200 rounded hover:bg-gray-50 cursor-pointer transition"
                @click="navigateToInterventions"
              >
                <div class="flex items-center justify-between">
                  <div>
                    <p class="text-sm font-semibold text-gray-900">{{ intervention.reference }}</p>
                    <p class="text-xs text-gray-600">{{ intervention.transformateurReference }}</p>
                  </div>
                  <span :class="getInterventionBadgeClass(intervention.statut)" class="text-xs font-semibold px-2 py-1 rounded">
                    {{ intervention.statut }}
                  </span>
                </div>
              </div>
            </div>
            <button
              @click="navigateToInterventions"
              class="mt-4 w-full py-2 text-center text-sm text-blue-600 hover:text-blue-700 font-medium border border-blue-200 rounded hover:bg-blue-50 transition"
            >
              Voir les interventions →
            </button>
          </div>
          <div v-else class="text-center py-6 text-gray-500">
            ✅ Aucune intervention récente
          </div>
        </DashboardCard>

        <!-- Recent Complaints -->
        <DashboardCard title="Réclamations Récentes" class="shadow-md">
          <div v-if="dashboardData.recentItems?.recentComplaints && dashboardData.recentItems.recentComplaints.length > 0">
            <div class="space-y-2">
              <div
                v-for="complaint in dashboardData.recentItems.recentComplaints.slice(0, 5)"
                :key="complaint.id"
                class="p-3 border border-gray-200 rounded hover:bg-gray-50 cursor-pointer transition"
                @click="navigateToComplaints"
              >
                <div class="flex items-center justify-between">
                  <div class="flex-1">
                    <p class="text-sm font-semibold text-gray-900">{{ complaint.reference }}</p>
                    <p class="text-xs text-gray-600">{{ complaint.categorie }}</p>
                  </div>
                  <span :class="getComplaintBadgeClass(complaint.statut)" class="text-xs font-semibold px-2 py-1 rounded flex-shrink-0">
                    {{ complaint.statut }}
                  </span>
                </div>
              </div>
            </div>
            <button
              @click="navigateToComplaints"
              class="mt-4 w-full py-2 text-center text-sm text-blue-600 hover:text-blue-700 font-medium border border-blue-200 rounded hover:bg-blue-50 transition"
            >
              Voir les réclamations →
            </button>
          </div>
          <div v-else class="text-center py-6 text-gray-500">
            ✅ Aucune réclamation récente
          </div>
        </DashboardCard>
      </div>

      <!-- Section 5: IoT Measurements - Tendance Réseau avec Filtrage Temporel -->
      <DashboardCard title="Tendance Réseau IoT - Mesures en Temps Réel" class="shadow-md">
        <div class="space-y-4">
          <!-- Temporal Filter Buttons -->
          <div class="flex flex-wrap gap-2 mb-6">
            <button
              v-for="period in ['24h', '7j', '30j']"
              :key="period"
              @click="selectTemporalPeriod(period)"
              :class="[
                'px-4 py-2 rounded-lg font-medium text-sm transition',
                selectedPeriod === period
                  ? 'bg-blue-600 text-white shadow-md'
                  : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
              ]"
            >
              {{ period }}
            </button>
          </div>

          <!-- Measurement Statistics Display -->
          <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mb-6">
            <!-- Temperature -->
            <div class="p-4 bg-gradient-to-br from-orange-50 to-orange-100 rounded-lg border border-orange-200">
              <p class="text-xs text-orange-700 font-semibold mb-2">TEMPÉRATURE</p>
              <p class="text-2xl font-bold text-orange-900">{{ Math.round(dashboardData.measurementStatistics?.averageTemperature || 0) }}°C</p>
              <p class="text-xs text-orange-600 mt-1">
                Min: {{ Math.round(dashboardData.measurementStatistics?.minTemperature || 0) }}° | 
                Max: {{ Math.round(dashboardData.measurementStatistics?.maxTemperature || 0) }}°
              </p>
            </div>

            <!-- Voltage -->
            <div class="p-4 bg-gradient-to-br from-blue-50 to-blue-100 rounded-lg border border-blue-200">
              <p class="text-xs text-blue-700 font-semibold mb-2">TENSION</p>
              <p class="text-2xl font-bold text-blue-900">{{ Math.round(dashboardData.measurementStatistics?.averageVoltage || 0) }}V</p>
              <p class="text-xs text-blue-600 mt-1">Moyenne: {{ selectedPeriod }}</p>
            </div>

            <!-- Current -->
            <div class="p-4 bg-gradient-to-br from-purple-50 to-purple-100 rounded-lg border border-purple-200">
              <p class="text-xs text-purple-700 font-semibold mb-2">COURANT</p>
              <p class="text-2xl font-bold text-purple-900">{{ Math.round(dashboardData.measurementStatistics?.averageCurrent || 0) }}A</p>
              <p class="text-xs text-purple-600 mt-1">Moyenne: {{ selectedPeriod }}</p>
            </div>

            <!-- Power -->
            <div class="p-4 bg-gradient-to-br from-green-50 to-green-100 rounded-lg border border-green-200">
              <p class="text-xs text-green-700 font-semibold mb-2">PUISSANCE</p>
              <p class="text-2xl font-bold text-green-900">{{ (dashboardData.measurementStatistics?.averagePower || 0 / 1000).toFixed(1) }}kW</p>
              <p class="text-xs text-green-600 mt-1">Moyenne: {{ selectedPeriod }}</p>
            </div>
          </div>

          <!-- Trend Indicators -->
          <div class="p-4 bg-gray-50 rounded-lg border border-gray-200">
            <p class="text-sm font-semibold text-gray-900 mb-3">Indicateurs de Qualité - {{ selectedPeriod }}</p>
            <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
              <div class="flex items-center space-x-3">
                <div class="w-12 h-12 rounded-full bg-green-100 flex items-center justify-center">
                  <span class="text-lg">📊</span>
                </div>
                <div>
                  <p class="text-sm font-medium text-gray-700">Mesures Collectées</p>
                  <p class="text-lg font-bold text-green-600">{{ dashboardData.totalMeasurements }}</p>
                </div>
              </div>
              <div class="flex items-center space-x-3">
                <div class="w-12 h-12 rounded-full bg-yellow-100 flex items-center justify-center">
                  <span class="text-lg">⚠️</span>
                </div>
                <div>
                  <p class="text-sm font-medium text-gray-700">Mesures en Alerte</p>
                  <p class="text-lg font-bold text-yellow-600">{{ dashboardData.measurementStatistics?.warningMeasurements || 0 }}</p>
                </div>
              </div>
              <div class="flex items-center space-x-3">
                <div class="w-12 h-12 rounded-full bg-red-100 flex items-center justify-center">
                  <span class="text-lg">🔴</span>
                </div>
                <div>
                  <p class="text-sm font-medium text-gray-700">Mesures Critiques</p>
                  <p class="text-lg font-bold text-red-600">{{ dashboardData.measurementStatistics?.criticalMeasurements || 0 }}</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </DashboardCard>

      <!-- Section 5: Distribution Charts -->
      <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <!-- Interventions Distribution -->
        <DashboardCard title="Interventions par Statut" class="shadow-md">
          <div class="h-80">
            <Bar
              :data="chartData.interventions"
              :options="chartOptions"
            />
          </div>
        </DashboardCard>

        <!-- Alerts Distribution -->
        <DashboardCard title="Alertes par Niveau de Sévérité" class="shadow-md">
          <div class="h-80">
            <Doughnut
              :data="chartData.alertsDistribution"
              :options="chartOptions"
            />
          </div>
        </DashboardCard>
      </div>
    </DashboardState>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
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

const router = useRouter()
const loading = ref(true)
const error = ref(null)
const lastUpdate = ref(null)
const selectedPeriod = ref('24h')

const dashboardData = ref({
  totalPostes: 0,
  totalTransformateurs: 0,
  totalMeasurements: 0,
  totalInterventions: 0,
  totalComplaints: 0,
  totalAlerts: 0,
  criticalAlerts: 0,
  transformateursCritiques: 0,
  interventionsEnCours: 0,
  averageHealthScore: 0,
  recentItems: {
    recentAlerts: [],
    recentInterventions: [],
    recentComplaints: [],
    riskTransformateurs: [],
  },
  posteStats: {},
  transformateurStats: {},
  interventionStats: {},
  alertStats: {},
  measurementStatistics: {},
  complaintStatistics: {},
})

const chartData = ref({
  transformateurStatus: {
    labels: ['Normal', 'Avertissement', 'Critique'],
    datasets: [
      {
        label: 'État des transformateurs',
        data: [0, 0, 0],
        backgroundColor: [
          'rgba(34, 197, 94, 0.8)',
          'rgba(251, 146, 60, 0.8)',
          'rgba(239, 68, 68, 0.8)',
        ],
        borderColor: [
          'rgba(34, 197, 94, 1)',
          'rgba(251, 146, 60, 1)',
          'rgba(239, 68, 68, 1)',
        ],
        borderWidth: 2,
      },
    ],
  },
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
  alertsDistribution: {
    labels: ['Critique', 'Haute', 'Moyenne', 'Basse'],
    datasets: [
      {
        label: 'Alertes',
        data: [0, 0, 0, 0],
        backgroundColor: [
          'rgba(239, 68, 68, 0.8)',
          'rgba(251, 146, 60, 0.8)',
          'rgba(234, 179, 8, 0.8)',
          'rgba(34, 197, 94, 0.8)',
        ],
        borderColor: [
          'rgba(239, 68, 68, 1)',
          'rgba(251, 146, 60, 1)',
          'rgba(234, 179, 8, 1)',
          'rgba(34, 197, 94, 1)',
        ],
        borderWidth: 2,
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
    tooltip: {
      callbacks: {
        label: function(context) {
          return context.label + ': ' + context.parsed.y
        },
      },
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
    const response = await dashboardAPI.getAdminDashboard()
    const data = response.data

    dashboardData.value = {
      totalPostes: data.totalPostes || 0,
      totalTransformateurs: data.totalTransformateurs || 0,
      totalMeasurements: data.totalMeasurements || 0,
      totalInterventions: data.totalInterventions || 0,
      totalComplaints: data.totalComplaints || 0,
      totalAlerts: data.totalAlerts || 0,
      criticalAlerts: data.criticalAlerts || 0,
      transformateursCritiques: data.transformateursCritiques || 0,
      interventionsEnCours: data.interventionsEnCours || 0,
      complaintsUnresolved: data.complaintsUnresolved || 0,
      averageHealthScore: data.averageHealthScore || 0,
      recentItems: {
        recentAlerts: data.recentItems?.recentAlerts || [],
        recentInterventions: data.recentItems?.recentInterventions || [],
        recentComplaints: data.recentItems?.recentComplaints || [],
        riskTransformateurs: data.recentItems?.riskTransformateurs || [],
      },
      posteStats: data.posteStats || {},
      transformateurStats: data.transformateurStats || {},
      interventionStats: data.interventionStats || {},
      alertStats: data.alertStats || {},
      measurementStatistics: data.measurementStatistics || {},
      complaintStatistics: data.complaintStatistics || {},
    }

    lastUpdate.value = new Date()
    updateChartData(data)
  } catch (err) {
    console.error('Erreur chargement dashboard:', err)
    error.value = 'Impossible de charger les données du dashboard. Veuillez réessayer.'
  } finally {
    loading.value = false
  }
}

function updateChartData(data) {
  // Transformateur status distribution
  const transfStats = data.transformateurStats || {}
  chartData.value.transformateurStatus.datasets[0].data = [
    transfStats.transformateursActifs || 0,
    transfStats.transformateursAlertes || 0,
    transfStats.transformateursCritiques || 0,
  ]

  // Interventions distribution
  const interventionStats = data.interventionStats || {}
  chartData.value.interventions.datasets[0].data = [
    interventionStats.enAttente || 0,
    interventionStats.enCours || 0,
    interventionStats.terminees || 0,
    interventionStats.annulees || 0,
  ]

  // Alerts distribution
  const alertStats = data.alertStats || {}
  chartData.value.alertsDistribution.datasets[0].data = [
    alertStats.criticalCount || 0,
    alertStats.highCount || 0,
    alertStats.warningCount || 0,
    alertStats.infoCount || 0,
  ]
}

// Color and style helper methods
function getAlertColor(niveau) {
  const map = {
    'CRITICAL': 'bg-red-500',
    'HIGH': 'bg-orange-500',
    'WARNING': 'bg-yellow-500',
    'INFO': 'bg-blue-500',
  }
  return map[niveau] || 'bg-gray-500'
}

function getAlertBadgeClass(niveau) {
  const map = {
    'CRITICAL': 'bg-red-100 text-red-800',
    'HIGH': 'bg-orange-100 text-orange-800',
    'WARNING': 'bg-yellow-100 text-yellow-800',
    'INFO': 'bg-blue-100 text-blue-800',
  }
  return map[niveau] || 'bg-gray-100 text-gray-800'
}

function getHealthBadgeClass(score) {
  if (score >= 80) return 'bg-green-100 text-green-800'
  if (score >= 60) return 'bg-yellow-100 text-yellow-800'
  if (score >= 40) return 'bg-orange-100 text-orange-800'
  return 'bg-red-100 text-red-800'
}

function getInterventionBadgeClass(statut) {
  const map = {
    'EN_ATTENTE': 'bg-blue-100 text-blue-800',
    'PLANIFIEE': 'bg-purple-100 text-purple-800',
    'EN_COURS': 'bg-orange-100 text-orange-800',
    'TERMINEE': 'bg-green-100 text-green-800',
    'VALIDEE': 'bg-green-100 text-green-800',
    'ANNULEE': 'bg-red-100 text-red-800',
  }
  return map[statut] || 'bg-gray-100 text-gray-800'
}

function getComplaintBadgeClass(statut) {
  const map = {
    'NOUVELLE': 'bg-blue-100 text-blue-800',
    'EN_ANALYSE': 'bg-purple-100 text-purple-800',
    'ASSIGNEE': 'bg-orange-100 text-orange-800',
    'EN_TRAITEMENT': 'bg-yellow-100 text-yellow-800',
    'RESOLUE': 'bg-green-100 text-green-800',
    'FERMEE': 'bg-green-100 text-green-800',
  }
  return map[statut] || 'bg-gray-100 text-gray-800'
}

function formatDate(date) {
  if (!date) return ''
  return new Date(date).toLocaleDateString('fr-FR', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  })
}

// Navigation methods
function navigateToPostes() {
  router.push('/postes')
}

function navigateToTransformateurs() {
  router.push('/transformateurs')
}

function navigateToRiskTransformateurs() {
  router.push('/transformateurs?risk=true')
}

function navigateToTransformateurDetail(id) {
  router.push(`/transformateurs/${id}`)
}

function navigateToAlerts() {
  router.push('/alerts')
}

function navigateToInterventions() {
  router.push('/interventions')
}

function navigateToComplaints() {
  router.push('/complaints')
}

// Temporal filtering method
function selectTemporalPeriod(period) {
  selectedPeriod.value = period
  // Reload dashboard data for the selected period
  loadDashboard()
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

