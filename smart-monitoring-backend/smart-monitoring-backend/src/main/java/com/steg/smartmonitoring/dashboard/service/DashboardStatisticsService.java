package com.steg.smartmonitoring.dashboard.service;

import com.steg.smartmonitoring.dashboard.dto.AdminDashboardDTO;
import com.steg.smartmonitoring.dashboard.dto.SupervisorDashboardDTO;
import com.steg.smartmonitoring.dashboard.dto.AgentDashboardDTO;
import com.steg.smartmonitoring.dashboard.dto.ServiceClientDashboardDTO;

/**
 * Service pour agréger les statistiques des dashboards
 * Combine les données de plusieurs services pour créer des vues complètes
 */
public interface DashboardStatisticsService {

    /**
     * Récupère les statistiques pour le dashboard ADMIN
     * @return AdminDashboardDTO avec toutes les statistiques du système
     */
    AdminDashboardDTO getAdminDashboard();

    /**
     * Récupère les statistiques pour le dashboard SUPERVISOR
     * @return SupervisorDashboardDTO avec vue opérationnelle
     */
    SupervisorDashboardDTO getSupervisorDashboard();

    /**
     * Récupère les statistiques pour le dashboard AGENT
     * @param agentId ID de l'agent (user ID)
     * @return AgentDashboardDTO avec interventions et alertes de l'agent
     */
    AgentDashboardDTO getAgentDashboard(Long agentId);

    /**
     * Récupère les statistiques pour le dashboard SERVICE_CLIENT
     * @return ServiceClientDashboardDTO avec vue réclamations
     */
    ServiceClientDashboardDTO getServiceClientDashboard();
}
