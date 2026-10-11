<template>
  <div class="space-y-6">
    <!-- Header with Back Button -->
    <div class="flex items-center gap-4 mb-6">
      <button
        @click="goBack"
        class="px-4 py-2 text-gray-600 hover:bg-gray-100 rounded-lg flex items-center gap-2"
      >
        ← Retour
      </button>
      <h1 class="text-3xl font-bold text-gray-900">Détail Transformateur</h1>
    </div>

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

    <!-- Main Content -->
    <div v-if="!loading && !error && transformateur" class="space-y-6">
      <!-- Key Information Card -->
      <Card class="border-l-4 border-l-blue-500">
        <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
          <div>
            <p class="text-sm text-gray-600">Référence</p>
            <p class="text-2xl font-bold text-gray-900">{{ transformateur.reference }}</p>
          </div>
          <div>
            <p class="text-sm text-gray-600">Fabricant</p>
            <p class="text-xl font-semibold text-gray-900">{{ transformateur.manufacturer || 'N/A' }}</p>
          </div>
          <div>
            <p class="text-sm text-gray-600">Health Score</p>
            <div class="flex items-center gap-2 mt-1">
              <div class="flex-1 h-2 bg-gray-200 rounded-full overflow-hidden">
                <div
                  class="h-full"
                  :style="{ width: (transformateur.healthScore || 0) + '%', backgroundColor: getHealthColor(transformateur.healthScore || 0) }"
                ></div>
              </div>
              <span class="text-xl font-bold" :class="getHealthClassFull(transformateur.healthScore || 0)">
                {{ Math.round(transformateur.healthScore || 0) }}%
              </span>
            </div>
          </div>
        </div>
      </Card>

      <!-- Status and Risk Indicators -->
      <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
        <Card>
          <div class="text-center">
            <p class="text-sm text-gray-600 mb-2">État</p>
            <div :class="getStatusBadgeClass(transformateur.statut)" class="inline-block px-4 py-2 rounded-lg font-medium">
              {{ transformateur.statut || 'Inconnu' }}
            </div>
          </div>
        </Card>

        <Card>
          <div class="text-center">
            <p class="text-sm text-gray-600 mb-2">Niveau de Risque</p>
            <div :class="getRiskBadgeClass(transformateur.riskLevel)" class="inline-block px-4 py-2 rounded-lg font-medium">
              {{ transformateur.riskLevel || 'N/A' }}
            </div>
          </div>
        </Card>

        <Card>
          <div class="text-center">
            <p class="text-sm text-gray-600 mb-2">Critique</p>
            <div class="text-3xl font-bold">
              <span v-if="transformateur.critique" class="text-red-600">🔴</span>
              <span v-else class="text-green-600">🟢</span>
            </div>
          </div>
        </Card>
      </div>

      <!-- Tabs -->
      <div class="border-b border-gray-200 overflow-x-auto">
        <div class="flex gap-8 min-w-max">
          <button
            v-for="tab in tabs"
            :key="tab"
            @click="activeTab = tab"
            :class="[
              'px-4 py-3 font-medium border-b-2 transition whitespace-nowrap',
              activeTab === tab
                ? 'border-blue-500 text-blue-600'
                : 'border-transparent text-gray-600 hover:text-gray-900'
            ]"
          >
            {{ tab }}
          </button>
        </div>
      </div>

      <!-- Tab Content -->
      <div>
        <!-- A. Information Tab -->
        <div v-if="activeTab === 'Information'" class="space-y-6">
          <Card title="Détails du Transformateur">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div>
                <p class="text-sm text-gray-600">Référence</p>
                <p class="text-lg font-medium text-gray-900">{{ transformateur.reference }}</p>
              </div>
              <div>
                <p class="text-sm text-gray-600">Poste</p>
                <p class="text-lg font-medium text-gray-900">{{ transformateur.posteNom || 'N/A' }}</p>
              </div>
              <div>
                <p class="text-sm text-gray-600">Fabricant</p>
                <p class="text-lg font-medium text-gray-900">{{ transformateur.manufacturer || 'N/A' }}</p>
              </div>
              <div>
                <p class="text-sm text-gray-600">Modèle</p>
                <p class="text-lg font-medium text-gray-900">{{ transformateur.model || 'N/A' }}</p>
              </div>
              <div>
                <p class="text-sm text-gray-600">Puissance (kVA)</p>
                <p class="text-lg font-medium text-gray-900">{{ transformateur.power || 'N/A' }}</p>
              </div>
              <div>
                <p class="text-sm text-gray-600">Tension (V)</p>
                <p class="text-lg font-medium text-gray-900">{{ transformateur.voltage || 'N/A' }}</p>
              </div>
              <div>
                <p class="text-sm text-gray-600">Année de Fabrication</p>
                <p class="text-lg font-medium text-gray-900">{{ transformateur.manufacturingYear || 'N/A' }}</p>
              </div>
              <div>
                <p class="text-sm text-gray-600">Numéro de Série</p>
                <p class="text-lg font-medium text-gray-900">{{ transformateur.serialNumber || 'N/A' }}</p>
              </div>
            </div>
          </Card>

          <!-- Status Information -->
          <Card title="Statut Actuel">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div class="p-4 bg-blue-50 rounded-lg">
                <p class="text-sm text-gray-600">Health Score</p>
                <p class="text-3xl font-bold text-blue-600">{{ Math.round(transformateur.healthScore || 0) }}%</p>
              </div>
              <div class="p-4 bg-orange-50 rounded-lg">
                <p class="text-sm text-gray-600">Niveau de Risque</p>
                <p class="text-2xl font-bold text-orange-600">{{ transformateur.riskLevel || 'N/A' }}</p>
              </div>
              <div class="p-4 bg-gray-50 rounded-lg">
                <p class="text-sm text-gray-600">Créé le</p>
                <p class="text-lg font-medium text-gray-900">{{ formatDate(transformateur.createdAt) }}</p>
              </div>
              <div class="p-4 bg-gray-50 rounded-lg">
                <p class="text-sm text-gray-600">Mis à jour le</p>
                <p class="text-lg font-medium text-gray-900">{{ formatDate(transformateur.updatedAt) }}</p>
              </div>
            </div>
          </Card>
        </div>

        <!-- B. État Actuel Tab -->
        <div v-if="activeTab === 'État Actuel'" class="space-y-6">
          <Card title="Paramètres Actuels">
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
              <div v-if="measurements.length > 0" class="p-4 border-l-4 border-red-500 bg-red-50 rounded">
                <p class="text-sm text-gray-600">Température</p>
                <p class="text-3xl font-bold text-red-600">{{ measurements[0].temperature }}°C</p>
                <p class="text-xs text-gray-500 mt-1">{{ formatDate(measurements[0].measurementDate) }}</p>
              </div>
              <div v-if="measurements.length > 0" class="p-4 border-l-4 border-blue-500 bg-blue-50 rounded">
                <p class="text-sm text-gray-600">Tension</p>
                <p class="text-3xl font-bold text-blue-600">{{ measurements[0].voltage }}V</p>
                <p class="text-xs text-gray-500 mt-1">Nominale: ~400V</p>
              </div>
              <div v-if="measurements.length > 0" class="p-4 border-l-4 border-green-500 bg-green-50 rounded">
                <p class="text-sm text-gray-600">Courant</p>
                <p class="text-3xl font-bold text-green-600">{{ measurements[0].current }}A</p>
                <p class="text-xs text-gray-500 mt-1">Charge: {{ measurements[0].power }}W</p>
              </div>
              <div v-if="measurements.length > 0" class="p-4 border-l-4 border-yellow-500 bg-yellow-50 rounded">
                <p class="text-sm text-gray-600">Statut</p>
                <p :class="getMeasurementStatusClass(measurements[0].status)" class="inline-block px-3 py-1 rounded text-sm font-medium mt-2">
                  {{ measurements[0].status || 'NORMAL' }}
                </p>
              </div>
            </div>
          </Card>
        </div>

        <!-- C. Santé Tab -->
        <div v-if="activeTab === 'Santé'" class="space-y-6">
          <Card title="Score de Santé">
            <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
              <div class="p-6 bg-gradient-to-br from-blue-50 to-blue-100 rounded-lg text-center">
                <p class="text-sm text-gray-600 mb-2">Score Actuel</p>
                <div class="relative w-32 h-32 mx-auto">
                  <svg viewBox="0 0 120 120" class="w-full h-full transform -rotate-90">
                    <circle cx="60" cy="60" r="50" fill="none" stroke="#e5e7eb" stroke-width="10" />
                    <circle
                      cx="60"
                      cy="60"
                      r="50"
                      fill="none"
                      :stroke="getHealthColor(transformateur.healthScore || 0)"
                      stroke-width="10"
                      :stroke-dasharray="`${((transformateur.healthScore || 0) / 100) * 314} 314`"
                      stroke-linecap="round"
                    />
                  </svg>
                  <div class="absolute inset-0 flex items-center justify-center">
                    <span class="text-4xl font-bold" :class="getHealthClassFull(transformateur.healthScore || 0)">
                      {{ Math.round(transformateur.healthScore || 0) }}
                    </span>
                  </div>
                </div>
                <p class="text-sm text-gray-600 mt-4">État: <span :class="getHealthClassFull(transformateur.healthScore || 0)" class="font-semibold">{{ getHealthStatus(transformateur.healthScore || 0) }}</span></p>
              </div>
              <div class="p-6 bg-gray-50 rounded-lg">
                <p class="text-sm text-gray-600 mb-4">Facteurs de Santé</p>
                <div class="space-y-3">
                  <div>
                    <p class="text-xs text-gray-600 mb-1">Température (40%)</p>
                    <div class="h-2 bg-gray-200 rounded-full overflow-hidden">
                      <div class="h-full bg-red-500" style="width: 45%;"></div>
                    </div>
                  </div>
                  <div>
                    <p class="text-xs text-gray-600 mb-1">Tension (30%)</p>
                    <div class="h-2 bg-gray-200 rounded-full overflow-hidden">
                      <div class="h-full bg-blue-500" style="width: 80%;"></div>
                    </div>
                  </div>
                  <div>
                    <p class="text-xs text-gray-600 mb-1">Courant (20%)</p>
                    <div class="h-2 bg-gray-200 rounded-full overflow-hidden">
                      <div class="h-full bg-green-500" style="width: 60%;"></div>
                    </div>
                  </div>
                  <div>
                    <p class="text-xs text-gray-600 mb-1">Puissance (10%)</p>
                    <div class="h-2 bg-gray-200 rounded-full overflow-hidden">
                      <div class="h-full bg-yellow-500" style="width: 75%;"></div>
                    </div>
                  </div>
                </div>
              </div>
              <div class="p-6 bg-gray-50 rounded-lg">
                <p class="text-sm text-gray-600 mb-4">Tendance</p>
                <div v-if="measurements.length > 1" class="space-y-2">
                  <p class="text-lg font-bold text-gray-900">{{ getTrend() }}</p>
                  <p class="text-xs text-gray-600">Comparaison: 7 derniers jours</p>
                  <div class="h-16 mt-2 bg-white rounded border border-gray-200">
                    <!-- Mini trend chart -->
                    <svg class="w-full h-full" viewBox="0 0 100 50" preserveAspectRatio="xMidYMid meet">
                      <polyline points="0,40 10,35 20,38 30,32 40,28 50,30 60,25 70,20 80,22 90,18 100,15" 
                        fill="none" stroke="currentColor" stroke-width="1.5" class="text-blue-500" />
                    </svg>
                  </div>
                </div>
                <div v-else class="text-center py-4">
                  <p class="text-sm text-gray-500">Données insuffisantes</p>
                </div>
              </div>
            </div>
          </Card>
        </div>

        <!-- D. Mesures Tab -->
        <div v-if="activeTab === 'Mesures'" class="space-y-6">
          <Card title="Dernières Mesures (30 derniers jours)">
            <div v-if="measurements.length === 0" class="text-center py-8 text-gray-500">
              Aucune mesure disponible
            </div>
            <div v-else class="space-y-4">
              <div
                v-for="measurement in measurements.slice(0, 10)"
                :key="measurement.id"
                class="flex items-center justify-between p-4 border border-gray-200 rounded-lg hover:bg-gray-50"
              >
                <div>
                  <p class="font-medium text-gray-900">{{ formatDate(measurement.measurementDate) }}</p>
                  <div class="grid grid-cols-4 gap-2 mt-2 text-xs text-gray-600">
                    <span>🌡️ {{ measurement.temperature }}°C</span>
                    <span>⚡ {{ measurement.voltage }}V</span>
                    <span>📊 {{ measurement.current }}A</span>
                    <span>💡 {{ measurement.power }}W</span>
                  </div>
                </div>
                <div>
                  <span :class="getMeasurementStatusClass(measurement.status)" class="inline-block px-3 py-1 rounded text-sm font-medium">
                    {{ measurement.status || 'Normal' }}
                  </span>
                </div>
              </div>
            </div>
          </Card>

          <!-- Temperature Chart (7 days) -->
          <Card title="Évolution Température (7 derniers jours)">
            <div class="h-80">
              <Line
                v-if="chartData.temperature.labels.length > 0"
                :data="chartData.temperature"
                :options="lineChartOptions"
              />
              <div v-else class="flex items-center justify-center h-full text-gray-500">
                Pas de données disponibles
              </div>
            </div>
          </Card>

          <!-- Voltage Chart -->
          <Card title="Évolution Tension (7 derniers jours)">
            <div class="h-80">
              <Line
                v-if="chartData.voltage.labels.length > 0"
                :data="chartData.voltage"
                :options="lineChartOptions"
              />
              <div v-else class="flex items-center justify-center h-full text-gray-500">
                Pas de données disponibles
              </div>
            </div>
          </Card>

          <!-- Current Chart -->
          <Card title="Évolution Courant (7 derniers jours)">
            <div class="h-80">
              <Line
                v-if="chartData.current.labels.length > 0"
                :data="chartData.current"
                :options="lineChartOptions"
              />
              <div v-else class="flex items-center justify-center h-full text-gray-500">
                Pas de données disponibles
              </div>
            </div>
          </Card>

          <!-- Power Chart -->
          <Card title="Évolution Puissance (7 derniers jours)">
            <div class="h-80">
              <Line
                v-if="chartData.power.labels.length > 0"
                :data="chartData.power"
                :options="lineChartOptions"
              />
              <div v-else class="flex items-center justify-center h-full text-gray-500">
                Pas de données disponibles
              </div>
            </div>
          </Card>
        </div>

        <!-- E. Alertes Tab -->
        <div v-if="activeTab === 'Alertes'" class="space-y-6">
          <Card title="Alertes Actives (10 Dernières)">
            <div v-if="alerts.length === 0" class="text-center py-8 text-gray-500">
              Aucune alerte
            </div>
            <div v-else class="space-y-3">
              <div
                v-for="alert in alerts.slice(0, 10)"
                :key="alert.id"
                class="flex items-start gap-4 p-4 border-l-4 rounded"
                :class="getAlertBorderClass(alert.level)"
              >
                <div class="text-2xl">
                  {{ getAlertIcon(alert.level) }}
                </div>
                <div class="flex-1">
                  <p class="font-medium text-gray-900">{{ alert.title }}</p>
                  <p class="text-sm text-gray-600">{{ alert.description || 'N/A' }}</p>
                  <p class="text-xs text-gray-500 mt-1">{{ formatDate(alert.createdAt) }}</p>
                </div>
                <span :class="getAlertBadgeClass(alert.level)" class="inline-block px-3 py-1 rounded text-sm font-medium whitespace-nowrap">
                  {{ alert.level }}
                </span>
              </div>
            </div>
          </Card>

          <!-- Alerts Distribution -->
          <Card title="Distribution des Alertes">
            <div class="h-80">
              <Pie
                v-if="chartData.alerts.labels.length > 0"
                :data="chartData.alerts"
                :options="pieChartOptions"
              />
              <div v-else class="flex items-center justify-center h-full text-gray-500">
                Pas de données disponibles
              </div>
            </div>
          </Card>
        </div>

        <!-- F. Interventions Tab -->
        <div v-if="activeTab === 'Interventions'" class="space-y-6">
          <Card title="Interventions Récentes et En Cours">
            <div v-if="interventions.length === 0" class="text-center py-8 text-gray-500">
              Aucune intervention
            </div>
            <div v-else class="space-y-3">
              <div
                v-for="intervention in interventions.slice(0, 10)"
                :key="intervention.id"
                class="flex items-start justify-between p-4 border border-gray-200 rounded-lg hover:bg-gray-50"
              >
                <div>
                  <p class="font-medium text-gray-900">{{ intervention.reference }}</p>
                  <p class="text-sm text-gray-600">{{ intervention.description || 'N/A' }}</p>
                  <p class="text-xs text-gray-500 mt-1">{{ formatDate(intervention.createdAt) }}</p>
                </div>
                <div class="flex items-center gap-2">
                  <span :class="getPriorityBadgeClass(intervention.priority)" class="inline-block px-3 py-1 rounded text-sm font-medium">
                    {{ intervention.priority }}
                  </span>
                  <span :class="getInterventionStatusClass(intervention.status)" class="inline-block px-3 py-1 rounded text-sm font-medium">
                    {{ intervention.status }}
                  </span>
                </div>
              </div>
            </div>
          </Card>

          <!-- Interventions by Status -->
          <Card title="Interventions par Statut">
            <div class="h-80">
              <Bar
                v-if="chartData.interventions.labels.length > 0"
                :data="chartData.interventions"
                :options="barChartOptions"
              />
              <div v-else class="flex items-center justify-center h-full text-gray-500">
                Pas de données disponibles
              </div>
            </div>
          </Card>
        </div>

        <!-- G. Plaintes Tab -->
        <div v-if="activeTab === 'Plaintes'" class="space-y-6">
          <Card title="Plaintes Liées">
            <div v-if="complaints.length === 0" class="text-center py-8 text-gray-500">
              Aucune plainte
            </div>
            <div v-else class="space-y-3">
              <div
                v-for="complaint in complaints.slice(0, 10)"
                :key="complaint.id"
                class="flex items-start justify-between p-4 border border-gray-200 rounded-lg hover:bg-gray-50"
              >
                <div>
                  <p class="font-medium text-gray-900">{{ complaint.reference }}</p>
                  <p class="text-sm text-gray-600">{{ complaint.description || 'N/A' }}</p>
                  <p class="text-xs text-gray-500 mt-1">Créée: {{ formatDate(complaint.createdAt) }}</p>
                </div>
                <div class="flex items-center gap-2">
                  <span :class="getComplaintPriorityClass(complaint.priority)" class="inline-block px-3 py-1 rounded text-sm font-medium">
                    {{ complaint.priority }}
                  </span>
                  <span :class="getComplaintStatusClass(complaint.status)" class="inline-block px-3 py-1 rounded text-sm font-medium">
                    {{ complaint.status }}
                  </span>
                </div>
              </div>
            </div>
          </Card>
        </div>

        <!-- H. Prédictions Tab -->
        <div v-if="activeTab === 'Prédictions'" class="space-y-6">
          <Card v-if="predictions" title="Prédiction de Défaillance">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div class="p-6 bg-gradient-to-br" :class="getPredictionBgClass(predictions.riskLevel)">
                <p class="text-sm text-gray-600 mb-2">Score de Risque</p>
                <p class="text-4xl font-bold mb-2" :class="getPredictionTextClass(predictions.riskLevel)">
                  {{ predictions.failureRiskScore }}/100
                </p>
                <p class="text-lg font-semibold" :class="getPredictionTextClass(predictions.riskLevel)">
                  {{ predictions.riskLevel }}
                </p>
              </div>
              <div class="p-6 bg-gray-50 rounded-lg">
                <p class="text-sm text-gray-600 mb-4">Détails</p>
                <div class="space-y-3">
                  <div>
                    <p class="text-xs text-gray-600">Date Estimée de Défaillance</p>
                    <p class="text-lg font-semibold text-gray-900">{{ formatDate(predictions.estimatedFailureDate) }}</p>
                  </div>
                  <div>
                    <p class="text-xs text-gray-600">Explication</p>
                    <p class="text-sm text-gray-700 mt-1">{{ predictions.explanation }}</p>
                  </div>
                </div>
              </div>
            </div>
          </Card>
          <Card v-else class="text-center py-8">
            <p class="text-gray-500">Aucune prédiction disponible</p>
          </Card>
        </div>

        <!-- I. Recommandations Tab -->
        <div v-if="activeTab === 'Recommandations'" class="space-y-6">
          <Card title="Maintenance Recommandée">
            <div v-if="recommendations.length === 0" class="text-center py-8 text-gray-500">
              Aucune recommandation
            </div>
            <div v-else class="space-y-4">
              <div
                v-for="(rec, idx) in recommendations"
                :key="idx"
                class="p-4 border-l-4 rounded"
                :class="getRecommendationBorderClass(rec.priorityLevel)"
              >
                <div class="flex items-start justify-between mb-2">
                  <div>
                    <p class="font-semibold text-gray-900">{{ rec.maintenanceType }}</p>
                    <p class="text-sm text-gray-600">{{ rec.cause }}</p>
                  </div>
                  <span :class="getRecommendationBadgeClass(rec.priorityLevel)" class="inline-block px-3 py-1 rounded text-sm font-medium whitespace-nowrap">
                    P{{ rec.priorityLevel }}
                  </span>
                </div>
                <p class="text-sm text-gray-700 mt-2">{{ rec.recommendation }}</p>
                <p class="text-xs text-gray-500 mt-2">Timeline: {{ rec.timeline }}</p>
              </div>
            </div>
          </Card>
        </div>

        <!-- J. Historique Tab -->
        <div v-if="activeTab === 'Historique'" class="space-y-6">
          <Card title="Historique des Événements">
            <div class="space-y-3">
              <div class="flex gap-4">
                <div class="w-1 bg-blue-500 rounded"></div>
                <div class="pb-4">
                  <p class="font-medium text-gray-900">Créé</p>
                  <p class="text-sm text-gray-600">{{ formatDate(transformateur.createdAt) }}</p>
                </div>
              </div>
              <div class="flex gap-4">
                <div class="w-1 bg-gray-500 rounded"></div>
                <div class="pb-4">
                  <p class="font-medium text-gray-900">Dernière mise à jour</p>
                  <p class="text-sm text-gray-600">{{ formatDate(transformateur.updatedAt) }}</p>
                </div>
              </div>
              <div v-if="alerts.length > 0" class="flex gap-4">
                <div class="w-1 bg-red-500 rounded"></div>
                <div class="pb-4">
                  <p class="font-medium text-gray-900">Dernière alerte</p>
                  <p class="text-sm text-gray-600">{{ formatDate(alerts[0].createdAt) }} - {{ alerts[0].title }}</p>
                </div>
              </div>
              <div v-if="measurements.length > 0" class="flex gap-4">
                <div class="w-1 bg-green-500 rounded"></div>
                <div class="pb-4">
                  <p class="font-medium text-gray-900">Dernière mesure</p>
                  <p class="text-sm text-gray-600">{{ formatDate(measurements[0].measurementDate) }}</p>
                </div>
              </div>
            </div>
          </Card>
        </div>

        <!-- K. Analyse Tab -->
        <div v-if="activeTab === 'Analyse'" class="space-y-6">
          <Card title="Analyse Détaillée">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div class="p-4 bg-blue-50 rounded-lg">
                <p class="text-sm font-semibold text-gray-900 mb-2">Nombre total de mesures</p>
                <p class="text-3xl font-bold text-blue-600">{{ measurements.length }}</p>
              </div>
              <div class="p-4 bg-red-50 rounded-lg">
                <p class="text-sm font-semibold text-gray-900 mb-2">Alertes actives</p>
                <p class="text-3xl font-bold text-red-600">{{ alerts.filter(a => a.status === 'ACTIVE').length }}</p>
              </div>
              <div class="p-4 bg-orange-50 rounded-lg">
                <p class="text-sm font-semibold text-gray-900 mb-2">Interventions en cours</p>
                <p class="text-3xl font-bold text-orange-600">{{ interventions.filter(i => i.status === 'IN_PROGRESS').length }}</p>
              </div>
              <div class="p-4 bg-yellow-50 rounded-lg">
                <p class="text-sm font-semibold text-gray-900 mb-2">Plaintes</p>
                <p class="text-3xl font-bold text-yellow-600">{{ complaints.length }}</p>
              </div>
            </div>
          </Card>

          <!-- Statistics -->
          <Card title="Statistiques">
            <div v-if="measurements.length > 0" class="space-y-4">
              <div>
                <p class="text-sm text-gray-600 mb-2">Température</p>
                <div class="grid grid-cols-4 gap-2 text-center">
                  <div class="p-3 bg-gray-50 rounded">
                    <p class="text-xs text-gray-600">Min</p>
                    <p class="text-lg font-bold text-gray-900">{{ getMinMeasurement('temperature') }}°C</p>
                  </div>
                  <div class="p-3 bg-gray-50 rounded">
                    <p class="text-xs text-gray-600">Moy</p>
                    <p class="text-lg font-bold text-gray-900">{{ getAvgMeasurement('temperature') }}°C</p>
                  </div>
                  <div class="p-3 bg-gray-50 rounded">
                    <p class="text-xs text-gray-600">Max</p>
                    <p class="text-lg font-bold text-gray-900">{{ getMaxMeasurement('temperature') }}°C</p>
                  </div>
                  <div class="p-3 bg-gray-50 rounded">
                    <p class="text-xs text-gray-600">Écart-type</p>
                    <p class="text-lg font-bold text-gray-900">{{ getStdDevMeasurement('temperature') }}°C</p>
                  </div>
                </div>
              </div>
            </div>
            <div v-else class="text-center py-8 text-gray-500">
              Pas de données pour l'analyse
            </div>
          </Card>
        </div>

        <!-- L. Logs Tab -->
        <div v-if="activeTab === 'Logs'" class="space-y-6">
          <Card title="Journal d'Audit">
            <div class="space-y-2">
              <div class="p-3 bg-gray-50 rounded text-sm text-gray-600">
                <p class="font-medium text-gray-900">Transformateur créé</p>
                <p>{{ formatDate(transformateur.createdAt) }}</p>
              </div>
              <div class="p-3 bg-gray-50 rounded text-sm text-gray-600">
                <p class="font-medium text-gray-900">Dernière mise à jour</p>
                <p>{{ formatDate(transformateur.updatedAt) }}</p>
              </div>
              <div v-if="measurements.length > 0" class="p-3 bg-gray-50 rounded text-sm text-gray-600">
                <p class="font-medium text-gray-900">Dernière mesure enregistrée</p>
                <p>{{ formatDate(measurements[0].measurementDate) }}</p>
              </div>
              <div v-if="alerts.length > 0" class="p-3 bg-gray-50 rounded text-sm text-gray-600">
                <p class="font-medium text-gray-900">Dernière alerte générée</p>
                <p>{{ formatDate(alerts[0].createdAt) }} - {{ alerts[0].title }}</p>
              </div>
            </div>
          </Card>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import Card from '@/components/Card.vue'
import { Line, Bar, Pie } from 'vue-chartjs'
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
import { transformateursAPI, measurementsAPI, alertsAPI, interventionsAPI, complaintsAPI, predictionsAPI } from '@/services/api'

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

const route = useRoute()
const router = useRouter()

const loading = ref(true)
const error = ref(null)
const activeTab = ref('Information')
const tabs = ['Information', 'État Actuel', 'Santé', 'Mesures', 'Alertes', 'Interventions', 'Plaintes', 'Prédictions', 'Recommandations', 'Historique', 'Analyse', 'Logs']

const transformateur = ref(null)
const measurements = ref([])
const alerts = ref([])
const interventions = ref([])
const complaints = ref([])
const predictions = ref(null)
const recommendations = ref([])

const chartData = ref({
  temperature: {
    labels: [],
    datasets: [
      {
        label: 'Température (°C)',
        data: [],
        borderColor: 'rgba(239, 68, 68, 1)',
        backgroundColor: 'rgba(239, 68, 68, 0.1)',
        tension: 0.4,
        fill: true,
      },
    ],
  },
  voltage: {
    labels: [],
    datasets: [
      {
        label: 'Tension (V)',
        data: [],
        borderColor: 'rgba(59, 130, 246, 1)',
        backgroundColor: 'rgba(59, 130, 246, 0.1)',
        tension: 0.4,
        fill: true,
      },
    ],
  },
  current: {
    labels: [],
    datasets: [
      {
        label: 'Courant (A)',
        data: [],
        borderColor: 'rgba(34, 197, 94, 1)',
        backgroundColor: 'rgba(34, 197, 94, 0.1)',
        tension: 0.4,
        fill: true,
      },
    ],
  },
  power: {
    labels: [],
    datasets: [
      {
        label: 'Puissance (W)',
        data: [],
        borderColor: 'rgba(251, 146, 60, 1)',
        backgroundColor: 'rgba(251, 146, 60, 0.1)',
        tension: 0.4,
        fill: true,
      },
    ],
  },
  alerts: {
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
})

const lineChartOptions = {
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

const pieChartOptions = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: {
      position: 'right',
    },
  },
}

const barChartOptions = {
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
  await loadData()
})

async function loadData() {
  try {
    loading.value = true
    error.value = null

    const transformateurId = route.params.id

    // Fetch transformateur detail
    const transformateurRes = await transformateursAPI.getDetail(transformateurId)
    transformateur.value = transformateurRes.data

    // Fetch related data in parallel
    const [measurementsRes, alertsRes, interventionsRes, complaintsRes, predictionsRes] = await Promise.all([
      measurementsAPI.getByTransformateur(transformateurId, 0, 50),
      alertsAPI.getByTransformateur(transformateurId, 0, 50),
      interventionsAPI.getByTransformateur(transformateurId, 0, 50),
      complaintsAPI.getByTransformateur(transformateurId, 0, 50).catch(() => ({ data: { content: [] } })),
      predictionsAPI.getPrediction(transformateurId).catch(() => ({ data: null })),
    ])

    measurements.value = measurementsRes.data.content || []
    alerts.value = alertsRes.data.content || []
    interventions.value = interventionsRes.data.content || []
    complaints.value = complaintsRes.data.content || []
    predictions.value = predictionsRes.data

    // Fetch recommendations if prediction exists
    if (predictions.value) {
      try {
        const recsRes = await predictionsAPI.getRecommendations(transformateurId)
        recommendations.value = recsRes.data || []
      } catch (e) {
        recommendations.value = []
      }
    }

    updateCharts()
  } catch (err) {
    console.error('Erreur chargement détail transformateur:', err)
    error.value = 'Impossible de charger les données du transformateur.'
  } finally {
    loading.value = false
  }
}

function updateCharts() {
  // Temperature chart (last 7 measurements)
  const sortedMeasurements = [...measurements.value].sort((a, b) => new Date(a.measurementDate) - new Date(b.measurementDate))
  const last7 = sortedMeasurements.slice(-7)

  chartData.value.temperature.labels = last7.map(m => formatDate(m.measurementDate))
  chartData.value.temperature.datasets[0].data = last7.map(m => m.temperature || 0)

  // Voltage chart
  chartData.value.voltage.labels = last7.map(m => formatDate(m.measurementDate))
  chartData.value.voltage.datasets[0].data = last7.map(m => m.voltage || 0)

  // Current chart
  chartData.value.current.labels = last7.map(m => formatDate(m.measurementDate))
  chartData.value.current.datasets[0].data = last7.map(m => m.current || 0)

  // Power chart
  chartData.value.power.labels = last7.map(m => formatDate(m.measurementDate))
  chartData.value.power.datasets[0].data = last7.map(m => m.power || 0)

  // Alerts distribution
  const alertCounts = {
    CRITIQUE: alerts.value.filter(a => a.niveau === 'CRITIQUE').length,
    HAUTE: alerts.value.filter(a => a.niveau === 'HAUTE').length,
    MOYENNE: alerts.value.filter(a => a.niveau === 'MOYENNE').length,
    BASSE: alerts.value.filter(a => a.niveau === 'BASSE').length,
  }
  chartData.value.alerts.datasets[0].data = [alertCounts.CRITIQUE, alertCounts.HAUTE, alertCounts.MOYENNE, alertCounts.BASSE]

  // Interventions distribution
  const interventionCounts = {
    EN_ATTENTE: interventions.value.filter(i => i.statut === 'EN_ATTENTE').length,
    EN_COURS: interventions.value.filter(i => i.statut === 'EN_COURS').length,
    TERMINEE: interventions.value.filter(i => i.statut === 'TERMINEE').length,
    ANNULEE: interventions.value.filter(i => i.statut === 'ANNULEA').length,
  }
  chartData.value.interventions.datasets[0].data = [
    interventionCounts.EN_ATTENTE,
    interventionCounts.EN_COURS,
    interventionCounts.TERMINEE,
    interventionCounts.ANNULEE,
  ]
}

function getHealthColor(score) {
  if (score >= 80) return 'rgba(34, 197, 94, 1)'
  if (score >= 60) return 'rgba(234, 179, 8, 1)'
  if (score >= 40) return 'rgba(251, 146, 60, 1)'
  return 'rgba(239, 68, 68, 1)'
}

function getHealthClassFull(score) {
  if (score >= 80) return 'text-green-600'
  if (score >= 60) return 'text-yellow-600'
  if (score >= 40) return 'text-orange-600'
  return 'text-red-600'
}

function getStatusBadgeClass(status) {
  const classes = {
    ACTIF: 'bg-green-100 text-green-800',
    INACTIF: 'bg-gray-100 text-gray-800',
    MAINTENANCE: 'bg-yellow-100 text-yellow-800',
  }
  return classes[status] || 'bg-gray-100 text-gray-800'
}

function getRiskBadgeClass(risk) {
  const classes = {
    BAS: 'bg-green-100 text-green-800',
    MOYEN: 'bg-yellow-100 text-yellow-800',
    HAUT: 'bg-orange-100 text-orange-800',
    CRITIQUE: 'bg-red-100 text-red-800',
  }
  return classes[risk] || 'bg-gray-100 text-gray-800'
}

function getAlertIcon(niveau) {
  const icons = {
    CRITIQUE: '🔴',
    HAUTE: '🟠',
    MOYENNE: '🟡',
    BASSE: '🟢',
  }
  return icons[niveau] || '⚪'
}

function getAlertBorderClass(niveau) {
  const classes = {
    CRITIQUE: 'border-red-500 bg-red-50',
    HAUTE: 'border-orange-500 bg-orange-50',
    MOYENNE: 'border-yellow-500 bg-yellow-50',
    BASSE: 'border-green-500 bg-green-50',
  }
  return classes[niveau] || 'border-gray-500 bg-gray-50'
}

function getAlertBadgeClass(niveau) {
  const classes = {
    CRITIQUE: 'bg-red-100 text-red-800',
    HAUTE: 'bg-orange-100 text-orange-800',
    MOYENNE: 'bg-yellow-100 text-yellow-800',
    BASSE: 'bg-green-100 text-green-800',
  }
  return classes[niveau] || 'bg-gray-100 text-gray-800'
}

function getPriorityBadgeClass(priorite) {
  const classes = {
    CRITIQUE: 'bg-red-100 text-red-800',
    URGENTE: 'bg-orange-100 text-orange-800',
    NORMALES: 'bg-yellow-100 text-yellow-800',
    BASSE: 'bg-green-100 text-green-800',
  }
  return classes[priorite] || 'bg-gray-100 text-gray-800'
}

function getInterventionStatusClass(statut) {
  const classes = {
    EN_ATTENTE: 'bg-blue-100 text-blue-800',
    EN_COURS: 'bg-orange-100 text-orange-800',
    TERMINEE: 'bg-green-100 text-green-800',
    ANNULEE: 'bg-red-100 text-red-800',
  }
  return classes[statut] || 'bg-gray-100 text-gray-800'
}

function getMeasurementStatusClass(status) {
  const classes = {
    NORMAL: 'bg-green-100 text-green-800',
    WARNING: 'bg-yellow-100 text-yellow-800',
    CRITICAL: 'bg-red-100 text-red-800',
  }
  return classes[status] || 'bg-gray-100 text-gray-800'
}

function formatDate(date) {
  if (!date) return 'N/A'
  return new Date(date).toLocaleDateString('fr-FR', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  })
}

function getHealthStatus(score) {
  if (score >= 80) return 'Excellent'
  if (score >= 60) return 'Bon'
  if (score >= 40) return 'Dégradé'
  return 'Critique'
}

function getTrend() {
  if (measurements.value.length < 2) return 'Données insuffisantes'
  const recent = measurements.value[0]?.healthScore || 0
  const older = measurements.value[Math.min(6, measurements.value.length - 1)]?.healthScore || 0
  const diff = recent - older
  return diff > 0 ? `📈 En amélioration (+${diff.toFixed(1)})` : `📉 En dégradation (${diff.toFixed(1)})`
}

function getPredictionBgClass(riskLevel) {
  const classes = {
    'CRITICAL': 'from-red-50 to-red-100',
    'HIGH': 'from-orange-50 to-orange-100',
    'MEDIUM': 'from-yellow-50 to-yellow-100',
    'LOW': 'from-green-50 to-green-100',
  }
  return classes[riskLevel] || 'from-gray-50 to-gray-100'
}

function getPredictionTextClass(riskLevel) {
  const classes = {
    'CRITICAL': 'text-red-700',
    'HIGH': 'text-orange-700',
    'MEDIUM': 'text-yellow-700',
    'LOW': 'text-green-700',
  }
  return classes[riskLevel] || 'text-gray-700'
}

function getRecommendationBorderClass(priority) {
  const classes = {
    1: 'border-red-500 bg-red-50',
    2: 'border-orange-500 bg-orange-50',
    3: 'border-yellow-500 bg-yellow-50',
    4: 'border-green-500 bg-green-50',
  }
  return classes[priority] || 'border-gray-500 bg-gray-50'
}

function getRecommendationBadgeClass(priority) {
  const classes = {
    1: 'bg-red-100 text-red-800',
    2: 'bg-orange-100 text-orange-800',
    3: 'bg-yellow-100 text-yellow-800',
    4: 'bg-green-100 text-green-800',
  }
  return classes[priority] || 'bg-gray-100 text-gray-800'
}

function getComplaintPriorityClass(priority) {
  const classes = {
    'CRITICAL': 'bg-red-100 text-red-800',
    'HIGH': 'bg-orange-100 text-orange-800',
    'MEDIUM': 'bg-yellow-100 text-yellow-800',
    'LOW': 'bg-green-100 text-green-800',
  }
  return classes[priority] || 'bg-gray-100 text-gray-800'
}

function getComplaintStatusClass(status) {
  const classes = {
    'OPEN': 'bg-blue-100 text-blue-800',
    'IN_PROGRESS': 'bg-orange-100 text-orange-800',
    'RESOLVED': 'bg-green-100 text-green-800',
    'CLOSED': 'bg-gray-100 text-gray-800',
  }
  return classes[status] || 'bg-gray-100 text-gray-800'
}

function getMinMeasurement(field) {
  if (measurements.value.length === 0) return 'N/A'
  return Math.min(...measurements.value.map(m => m[field] || 0)).toFixed(1)
}

function getMaxMeasurement(field) {
  if (measurements.value.length === 0) return 'N/A'
  return Math.max(...measurements.value.map(m => m[field] || 0)).toFixed(1)
}

function getAvgMeasurement(field) {
  if (measurements.value.length === 0) return 'N/A'
  const sum = measurements.value.reduce((a, m) => a + (m[field] || 0), 0)
  return (sum / measurements.value.length).toFixed(1)
}

function getStdDevMeasurement(field) {
  if (measurements.value.length === 0) return 'N/A'
  const avg = parseFloat(getAvgMeasurement(field))
  const variance = measurements.value.reduce((sum, m) => sum + Math.pow((m[field] || 0) - avg, 2), 0) / measurements.value.length
  return Math.sqrt(variance).toFixed(1)
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
