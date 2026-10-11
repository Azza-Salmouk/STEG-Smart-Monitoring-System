package com.steg.smartmonitoring.dashboard.controller;

import com.steg.smartmonitoring.dashboard.dto.AdminDashboardDTO;
import com.steg.smartmonitoring.dashboard.dto.AgentDashboardDTO;
import com.steg.smartmonitoring.dashboard.dto.ServiceClientDashboardDTO;
import com.steg.smartmonitoring.dashboard.dto.SupervisorDashboardDTO;
import com.steg.smartmonitoring.dashboard.service.DashboardStatisticsService;
import com.steg.smartmonitoring.user.entity.User;
import com.steg.smartmonitoring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour les dashboards
 * Fournit les endpoints d'agrégation de statistiques pour chaque rôle
 * 
 * SÉCURITÉ:
 * - Tous les endpoints nécessitent une authentification JWT
 * - @PreAuthorize vérifie le rôle spécifique
 * - Agent dashboard vérifie que l'utilisateur accède uniquement à ses propres données
 */
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardStatisticsService dashboardService;
    private final UserRepository userRepository;

    /**
     * GET /api/dashboard/admin - Dashboard pour ADMIN
     * Affiche toutes les statistiques du système
     * Accès: ADMIN uniquement
     */
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminDashboardDTO> getAdminDashboard() {
        AdminDashboardDTO dashboard = dashboardService.getAdminDashboard();
        return ResponseEntity.ok(dashboard);
    }

    /**
     * GET /api/dashboard/supervisor - Dashboard pour SUPERVISOR
     * Affiche vue opérationnelle et tactique
     * Accès: SUPERVISOR uniquement
     */
    @GetMapping("/supervisor")
    @PreAuthorize("hasRole('SUPERVISOR')")
    public ResponseEntity<SupervisorDashboardDTO> getSupervisorDashboard() {
        SupervisorDashboardDTO dashboard = dashboardService.getSupervisorDashboard();
        return ResponseEntity.ok(dashboard);
    }

    /**
     * GET /api/dashboard/agent/{id} - Dashboard pour AGENT
     * Affiche interventions et alertes de l'agent
     * Accès: AGENT uniquement
     * Sécurité: Un agent ne peut accéder qu'à ses propres données
     */
    @GetMapping("/agent/{id}")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<AgentDashboardDTO> getAgentDashboard(@PathVariable Long id) {
        // Récupérer l'utilisateur actuel depuis le JWT
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        
        User currentUser = userRepository.findByEmail(currentUserEmail)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Vérifier que l'agent n'accède qu'à ses propres données
        if (!currentUser.getId().equals(id)) {
            throw new org.springframework.security.access.AccessDeniedException(
                "Agent cannot access other agent's dashboard"
            );
        }
        
        AgentDashboardDTO dashboard = dashboardService.getAgentDashboard(id);
        return ResponseEntity.ok(dashboard);
    }

    /**
     * GET /api/dashboard/service-client - Dashboard pour SERVICE_CLIENT
     * Affiche statistiques des réclamations
     * Accès: SERVICE_CLIENT uniquement
     */
    @GetMapping("/service-client")
    @PreAuthorize("hasRole('SERVICE_CLIENT')")
    public ResponseEntity<ServiceClientDashboardDTO> getServiceClientDashboard() {
        ServiceClientDashboardDTO dashboard = dashboardService.getServiceClientDashboard();
        return ResponseEntity.ok(dashboard);
    }
}
