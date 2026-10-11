package com.steg.smartmonitoring.dashboard.service.impl;

import com.steg.smartmonitoring.alert.dto.AlertStatisticsResponse;
import com.steg.smartmonitoring.alert.repository.AlertRepository;
import com.steg.smartmonitoring.alert.service.AlertService;
import com.steg.smartmonitoring.alert.entity.AlertStatus;
import com.steg.smartmonitoring.alert.entity.AlertLevel;
import com.steg.smartmonitoring.complaint.dto.ComplaintStatisticsResponse;
import com.steg.smartmonitoring.complaint.entity.ComplaintPriority;
import com.steg.smartmonitoring.complaint.entity.ComplaintStatus;
import com.steg.smartmonitoring.complaint.entity.Complaint;
import com.steg.smartmonitoring.complaint.repository.ComplaintRepository;
import com.steg.smartmonitoring.complaint.service.ComplaintService;
import com.steg.smartmonitoring.dashboard.dto.*;
import com.steg.smartmonitoring.intervention.dto.InterventionStatisticsResponse;
import com.steg.smartmonitoring.intervention.entity.Priorite;
import com.steg.smartmonitoring.intervention.entity.StatutIntervention;
import com.steg.smartmonitoring.intervention.entity.Intervention;
import com.steg.smartmonitoring.intervention.repository.InterventionRepository;
import com.steg.smartmonitoring.intervention.service.InterventionService;
import com.steg.smartmonitoring.measurement.service.MeasurementService;
import com.steg.smartmonitoring.measurement.service.MeasurementStatisticsResponse;
import com.steg.smartmonitoring.poste.service.PosteService;
import com.steg.smartmonitoring.poste.service.PosteStatisticsResponse;
import com.steg.smartmonitoring.transformateur.repository.TransformateurRepository;
import com.steg.smartmonitoring.transformateur.service.TransformateurService;
import com.steg.smartmonitoring.transformateur.service.TransformateurStatisticsResponse;
import com.steg.smartmonitoring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service d'agrégation des statistiques des dashboards
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardStatisticsServiceImpl implements com.steg.smartmonitoring.dashboard.service.DashboardStatisticsService {

    private final UserRepository userRepository;
    private final PosteService posteService;
    private final TransformateurService transformateurService;
    private final MeasurementService measurementService;
    private final InterventionService interventionService;
    private final ComplaintService complaintService;
    private final AlertService alertService;
    private final TransformateurRepository transformateurRepository;
    private final InterventionRepository interventionRepository;
    private final ComplaintRepository complaintRepository;
    private final AlertRepository alertRepository;

    @Override
    public AdminDashboardDTO getAdminDashboard() {
        log.info("Generating admin dashboard");
        PosteStatisticsResponse posteStats = posteService.getStatistics();
        TransformateurStatisticsResponse transformateurStats = transformateurService.getStatistics();
        MeasurementStatisticsResponse measurementStats = measurementService.getStatistics();
        InterventionStatisticsResponse interventionStats = interventionService.getStatistics();
        ComplaintStatisticsResponse complaintStats = complaintService.getStatistics();
        AlertStatisticsResponse alertStats = alertService.getStatistics();

        // Calculate non-resolved complaints (total - resolved)
        long nonResolvedComplaints = complaintStats.getTotalComplaints() - (complaintStats.getResolues() != null ? complaintStats.getResolues() : 0);

        return new AdminDashboardDTO(
                userRepository.count(),
                posteStats.getTotalPostes(),
                transformateurStats.getTotalTransformateurs(),
                measurementStats.getTotalMeasurements(),
                interventionStats.getTotalInterventions(),
                complaintStats.getTotalComplaints(),
                alertStats.getTotalAlerts(),
                alertStats.getCriticalAlerts(),
                transformateurStats.getTransformateursCritiques(),
                interventionStats.getEnCours(),
                nonResolvedComplaints,
                transformateurStats.getAverageHealthScore(),
                posteStats,
                transformateurStats,
                interventionStats,
                alertStats,
                measurementStats,
                buildRecentItems()
        );
    }

    @Override
    public SupervisorDashboardDTO getSupervisorDashboard() {
        log.info("Generating supervisor dashboard");
        PosteStatisticsResponse posteStats = posteService.getStatistics();
        TransformateurStatisticsResponse transformateurStats = transformateurService.getStatistics();
        InterventionStatisticsResponse interventionStats = interventionService.getStatistics();
        AlertStatisticsResponse alertStats = alertService.getStatistics();
        ComplaintStatisticsResponse complaintStats = complaintService.getStatistics();

        // Calculate open complaints (nouvelle + en_analyse + assignee + en_traitement)
        long openComplaints = (complaintStats.getNouveilles() != null ? complaintStats.getNouveilles() : 0) +
                              (complaintStats.getEnAnalyse() != null ? complaintStats.getEnAnalyse() : 0) +
                              (complaintStats.getAssignees() != null ? complaintStats.getAssignees() : 0) +
                              (complaintStats.getEnTraitement() != null ? complaintStats.getEnTraitement() : 0);

        return new SupervisorDashboardDTO(
                posteStats.getTotalPostes(),
                transformateurStats.getTotalTransformateurs(),
                transformateurStats.getAverageHealthScore(),
                alertStats.getActiveAlerts(),
                alertStats.getCriticalAlerts(),
                interventionStats.getEnCours(),
                interventionStats.getTerminees(),
                openComplaints,
                transformateurStats.getTransformateursCritiques(),
                alertStats,
                interventionStats,
                posteStats,
                transformateurStats,
                complaintStats,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

    @Override
    public AgentDashboardDTO getAgentDashboard(Long agentId) {
        log.info("Generating agent dashboard for: {}", agentId);
        
        // Get all interventions for this agent using pagination to get all records
        Pageable unlimitedPageable = PageRequest.of(0, Integer.MAX_VALUE);
        List<Intervention> agentInterventions = interventionRepository.findByAgentId(agentId, unlimitedPageable)
                .getContent();
        
        // Calculate intervention stats by status
        long totalInterventions = agentInterventions.size();
        long interventionsEnCours = agentInterventions.stream()
                .filter(i -> i.getStatut() != null && i.getStatut().equals(StatutIntervention.EN_COURS))
                .count();
        long interventionsTerminees = agentInterventions.stream()
                .filter(i -> i.getStatut() != null && i.getStatut().equals(StatutIntervention.TERMINEE))
                .count();
        long interventionsCritiques = agentInterventions.stream()
                .filter(i -> i.getPriorite() != null && i.getPriorite().equals(Priorite.CRITIQUE))
                .count();
        
        // Calculate myInterventionsEnAttente (interventions with EN_ATTENTE status)
        long myInterventionsEnAttente = agentInterventions.stream()
                .filter(i -> i.getStatut() != null && i.getStatut().equals(StatutIntervention.EN_ATTENTE))
                .count();
        
        // Create byPriority breakdown with lowercase keys for frontend compatibility
        Map<String, Long> byPriority = new HashMap<>();
        byPriority.put("critique", agentInterventions.stream()
                .filter(i -> i.getPriorite() != null && i.getPriorite().equals(Priorite.CRITIQUE))
                .count());
        byPriority.put("haute", agentInterventions.stream()
                .filter(i -> i.getPriorite() != null && i.getPriorite().equals(Priorite.HAUTE))
                .count());
        byPriority.put("moyenne", agentInterventions.stream()
                .filter(i -> i.getPriorite() != null && i.getPriorite().equals(Priorite.MOYENNE))
                .count());
        byPriority.put("basse", agentInterventions.stream()
                .filter(i -> i.getPriorite() != null && i.getPriorite().equals(Priorite.FAIBLE))
                .count());
        
        // Get accessible transformateurs (those with assigned interventions)
        Set<Long> accessibleTransformateurIds = agentInterventions.stream()
                .map(i -> i.getTransformateur().getId())
                .collect(Collectors.toSet());
        long accessibleTransformateurs = accessibleTransformateurIds.size();
        
        // Get measurement count for accessible transformateurs
        long totalAccessibleMeasurements = 0;
        
        // Get alerts count for accessible transformateurs
        long accessibleAlerts = 0;
        long criticalAlerts = 0;
        if (!accessibleTransformateurIds.isEmpty()) {
            accessibleAlerts = alertRepository.countByTransformateurIdIn(new ArrayList<>(accessibleTransformateurIds));
            // Count critical alerts from accessible transformateurs by querying each one
            for (Long transformateurId : accessibleTransformateurIds) {
                criticalAlerts += alertRepository.countCriticalAlertsByTransformateurId(transformateurId, AlertStatus.ACTIVE, AlertLevel.CRITICAL);
            }
        }
        
        // Build recent interventions list (5 most recent)
        List<AgentDashboardDTO.MyInterventionDTO> recentInterventions = new ArrayList<>();
        Pageable recentLimit = PageRequest.of(0, 5, org.springframework.data.domain.Sort.by("datePlanifiee").descending());
        agentInterventions.stream()
                .limit(5)
                .forEach(intervention -> {
                    recentInterventions.add(new AgentDashboardDTO.MyInterventionDTO(
                            intervention.getId(),
                            intervention.getReference(),
                            intervention.getTransformateur() != null ? intervention.getTransformateur().getReference() : "N/A",
                            intervention.getTransformateur() != null && intervention.getTransformateur().getPoste() != null ? intervention.getTransformateur().getPoste().getCode() : "N/A",
                            intervention.getPriorite() != null ? intervention.getPriorite().toString() : "NORMALE",
                            intervention.getStatut() != null ? intervention.getStatut().toString() : "EN_ATTENTE",
                            intervention.getDatePlanifiee() != null ? intervention.getDatePlanifiee().toString() : null,
                            intervention.getDescription()
                    ));
                    recentInterventions.get(recentInterventions.size() - 1).setCreatedAt(
                            intervention.getCreatedAt() != null ? intervention.getCreatedAt().toString() : null
                    );
                });
        
        // Build recent alerts list (5 most recent accessible alerts)
        List<AgentDashboardDTO.AccessibleAlertDTO> recentAlerts = new ArrayList<>();
        if (!accessibleTransformateurIds.isEmpty()) {
            // Get up to 5 recent alerts for each accessible transformateur
            Pageable alertLimit = PageRequest.of(0, 5, org.springframework.data.domain.Sort.by("createdAt").descending());
            for (Long transformateurId : accessibleTransformateurIds) {
                if (recentAlerts.size() >= 5) break;
                alertRepository.findByTransformateurId(transformateurId, alertLimit).forEach(alert -> {
                    if (recentAlerts.size() < 5) {
                        recentAlerts.add(new AgentDashboardDTO.AccessibleAlertDTO(
                                alert.getId(),
                                alert.getTitre(),
                                alert.getNiveau() != null ? alert.getNiveau().toString() : "INFO",
                                alert.getStatut() != null ? alert.getStatut().toString() : "ACTIVE",
                                alert.getTransformateur() != null ? alert.getTransformateur().getReference() : "N/A",
                                alert.getCreatedAt() != null ? alert.getCreatedAt().toString() : null
                        ));
                    }
                });
            }
        }
        
        AgentDashboardDTO dashboard = new AgentDashboardDTO(
                totalInterventions,
                interventionsEnCours,
                interventionsTerminees,
                interventionsCritiques,
                accessibleTransformateurs,
                totalAccessibleMeasurements,
                accessibleAlerts,
                recentInterventions,
                recentAlerts,
                new ArrayList<>()
        );
        
        // Set calculated fields
        dashboard.setMyInterventionsEnAttente(myInterventionsEnAttente);
        dashboard.setCriticalAlerts(criticalAlerts);
        dashboard.setByPriority(byPriority);
        
        return dashboard;
    }

    @Override
    public ServiceClientDashboardDTO getServiceClientDashboard() {
        log.info("Generating service client dashboard");
        ComplaintStatisticsResponse complaintStats = complaintService.getStatistics();
        
        // Get counts for each status
        long nouvelles = complaintRepository.findByStatut(ComplaintStatus.NOUVELLE, PageRequest.of(0, 1)).getTotalElements();
        long enAnalyse = complaintRepository.findByStatut(ComplaintStatus.EN_ANALYSE, PageRequest.of(0, 1)).getTotalElements();
        long assignees = complaintRepository.findByStatut(ComplaintStatus.ASSIGNEE, PageRequest.of(0, 1)).getTotalElements();
        long enTraitement = complaintRepository.findByStatut(ComplaintStatus.EN_TRAITEMENT, PageRequest.of(0, 1)).getTotalElements();
        long resolues = complaintRepository.findByStatut(ComplaintStatus.RESOLUE, PageRequest.of(0, 1)).getTotalElements();
        long criticalCount = complaintRepository.findByPriorite(ComplaintPriority.CRITICAL, PageRequest.of(0, 1)).getTotalElements();
        
        // Fetch recent complaints (5 most recent)
        Pageable recentLimit = PageRequest.of(0, 5, org.springframework.data.domain.Sort.by("createdAt").descending());
        List<ServiceClientDashboardDTO.ComplaintSummaryDTO> recentComplaints = new ArrayList<>();
        complaintRepository.findAll(recentLimit).forEach(complaint -> {
            recentComplaints.add(new ServiceClientDashboardDTO.ComplaintSummaryDTO(
                    complaint.getId(),
                    complaint.getReference(),
                    complaint.getCategorie() != null ? complaint.getCategorie().toString() : "AUTRE",
                    complaint.getPriorite() != null ? complaint.getPriorite().toString() : "MEDIUM",
                    complaint.getStatut() != null ? complaint.getStatut().toString() : "NOUVELLE",
                    complaint.getDescription(),
                    complaint.getCreatedAt() != null ? complaint.getCreatedAt().toString() : null
            ));
        });
        
        // Fetch critical complaints
        List<ServiceClientDashboardDTO.ComplaintSummaryDTO> criticalComplaintsList = new ArrayList<>();
        complaintRepository.findByPriorite(ComplaintPriority.CRITICAL, recentLimit).forEach(complaint -> {
            criticalComplaintsList.add(new ServiceClientDashboardDTO.ComplaintSummaryDTO(
                    complaint.getId(),
                    complaint.getReference(),
                    complaint.getCategorie() != null ? complaint.getCategorie().toString() : "AUTRE",
                    complaint.getPriorite() != null ? complaint.getPriorite().toString() : "CRITICAL",
                    complaint.getStatut() != null ? complaint.getStatut().toString() : "NOUVELLE",
                    complaint.getDescription(),
                    complaint.getCreatedAt() != null ? complaint.getCreatedAt().toString() : null
            ));
        });
        
        // Build byPriority breakdown with lowercase keys for frontend compatibility
        Map<String, Long> byPriority = new HashMap<>();
        List<Complaint> allComplaints = complaintRepository.findAll();
        byPriority.put("critique", allComplaints.stream()
                .filter(c -> c.getPriorite() != null && c.getPriorite().equals(ComplaintPriority.CRITICAL))
                .count());
        byPriority.put("haute", allComplaints.stream()
                .filter(c -> c.getPriorite() != null && c.getPriorite().equals(ComplaintPriority.HIGH))
                .count());
        byPriority.put("moyenne", allComplaints.stream()
                .filter(c -> c.getPriorite() != null && c.getPriorite().equals(ComplaintPriority.MEDIUM))
                .count());
        byPriority.put("basse", allComplaints.stream()
                .filter(c -> c.getPriorite() != null && c.getPriorite().equals(ComplaintPriority.LOW))
                .count());
        
        // Fetch unresolved complaints (those not in RESOLUE or FERMEE status)
        List<ServiceClientDashboardDTO.ComplaintSummaryDTO> unresolvedComplaints = new ArrayList<>();
        List<ComplaintStatus> unresolvedStatuses = Arrays.asList(
                ComplaintStatus.NOUVELLE,
                ComplaintStatus.EN_ANALYSE,
                ComplaintStatus.ASSIGNEE,
                ComplaintStatus.EN_TRAITEMENT
        );
        for (ComplaintStatus status : unresolvedStatuses) {
            complaintRepository.findByStatut(status, recentLimit).forEach(complaint -> {
                if (unresolvedComplaints.size() < 10) { // Keep limit to 10 for dashboard
                    unresolvedComplaints.add(new ServiceClientDashboardDTO.ComplaintSummaryDTO(
                            complaint.getId(),
                            complaint.getReference(),
                            complaint.getCategorie() != null ? complaint.getCategorie().toString() : "AUTRE",
                            complaint.getPriorite() != null ? complaint.getPriorite().toString() : "MEDIUM",
                            complaint.getStatut() != null ? complaint.getStatut().toString() : "NOUVELLE",
                            complaint.getDescription(),
                            complaint.getCreatedAt() != null ? complaint.getCreatedAt().toString() : null
                    ));
                }
            });
        }
        
        ServiceClientDashboardDTO dashboard = new ServiceClientDashboardDTO(
                complaintStats.getTotalComplaints(),
                nouvelles,
                enAnalyse,
                assignees,
                enTraitement,
                resolues,
                criticalCount,
                complaintStats,
                recentComplaints,
                criticalComplaintsList,
                unresolvedComplaints
        );
        dashboard.setByPriority(byPriority);
        return dashboard;
    }

    private RecentItemsDTO buildRecentItems() {
        // Fetch recent alerts (last 5, non-archived, most recent first)
        Pageable recentLimit = PageRequest.of(0, 5, org.springframework.data.domain.Sort.by("createdAt").descending());
        List<RecentItemsDTO.RecentAlertDTO> recentAlerts = new ArrayList<>();
        try {
            alertRepository.findRecentActiveAlerts(recentLimit).forEach(alert -> {
                recentAlerts.add(new RecentItemsDTO.RecentAlertDTO(
                        alert.getId(),
                        alert.getTitre(),
                        alert.getNiveau() != null ? alert.getNiveau().toString() : "INFO",
                        alert.getStatut() != null ? alert.getStatut().toString() : "ACTIVE",
                        alert.getTransformateur() != null ? alert.getTransformateur().getId() : null,
                        alert.getTransformateur() != null ? alert.getTransformateur().getReference() : "N/A",
                        alert.getCreatedAt() != null ? alert.getCreatedAt().toString() : null
                ));
            });
        } catch (Exception e) {
            log.warn("Error fetching recent alerts: {}", e.getMessage());
        }

        // Fetch recent interventions (last 5, most recent first)
        List<RecentItemsDTO.RecentInterventionDTO> recentInterventions = new ArrayList<>();
        try {
            interventionRepository.findAll(recentLimit).forEach(intervention -> {
                recentInterventions.add(new RecentItemsDTO.RecentInterventionDTO(
                        intervention.getId(),
                        intervention.getReference(),
                        intervention.getStatut() != null ? intervention.getStatut().toString() : "EN_ATTENTE",
                        intervention.getPriorite() != null ? intervention.getPriorite().toString() : "NORMALE",
                        intervention.getTransformateur() != null ? intervention.getTransformateur().getId() : null,
                        intervention.getTransformateur() != null ? intervention.getTransformateur().getReference() : "N/A",
                        intervention.getDatePlanifiee() != null ? intervention.getDatePlanifiee().toString() : null
                ));
            });
        } catch (Exception e) {
            log.warn("Error fetching recent interventions: {}", e.getMessage());
        }

        // Fetch recent complaints (last 5, most recent first)
        List<RecentItemsDTO.RecentComplaintDTO> recentComplaints = new ArrayList<>();
        try {
            complaintRepository.findAll(recentLimit).forEach(complaint -> {
                recentComplaints.add(new RecentItemsDTO.RecentComplaintDTO(
                        complaint.getId(),
                        complaint.getReference(),
                        complaint.getCategorie() != null ? complaint.getCategorie().toString() : "AUTRE",
                        complaint.getPriorite() != null ? complaint.getPriorite().toString() : "NORMAL",
                        complaint.getStatut() != null ? complaint.getStatut().toString() : "NOUVELLE",
                        complaint.getCreatedAt() != null ? complaint.getCreatedAt().toString() : null
                ));
            });
        } catch (Exception e) {
            log.warn("Error fetching recent complaints: {}", e.getMessage());
        }

        // Fetch critical/at-risk transformateurs using available repository methods
        List<RecentItemsDTO.RiskTransformateurDTO> riskTransformateurs = new ArrayList<>();
        try {
            Pageable riskLimit = PageRequest.of(0, 5);
            
            // Strategy 1: Get transformateurs with low health score (health <= 60)
            transformateurRepository.findByHealthScoreLessThanEqual(60, riskLimit).forEach(transf -> {
                riskTransformateurs.add(new RecentItemsDTO.RiskTransformateurDTO(
                        transf.getId(),
                        transf.getReference(),
                        transf.getSerialNumber(),
                        transf.getHealthScore() != null ? transf.getHealthScore().intValue() : 0,
                        transf.getRiskLevel() != null ? transf.getRiskLevel().toString() : "LOW",
                        transf.getStatus() != null ? transf.getStatus().toString() : "ACTIF",
                        transf.getPoste() != null ? transf.getPoste().getCode() : "N/A"
                ));
            });
            
            // Strategy 2: If we need more, add transformateurs with alert/fault status
            if (riskTransformateurs.size() < 5) {
                int remaining = 5 - riskTransformateurs.size();
                transformateurRepository.findAllAlerting().stream()
                        .limit(remaining)
                        .forEach(transf -> {
                            // Only add if not already in list
                            if (riskTransformateurs.stream().noneMatch(r -> r.getId().equals(transf.getId()))) {
                                riskTransformateurs.add(new RecentItemsDTO.RiskTransformateurDTO(
                                        transf.getId(),
                                        transf.getReference(),
                                        transf.getSerialNumber(),
                                        transf.getHealthScore() != null ? transf.getHealthScore().intValue() : 0,
                                        transf.getRiskLevel() != null ? transf.getRiskLevel().toString() : "MEDIUM",
                                        transf.getStatus() != null ? transf.getStatus().toString() : "ALERTE",
                                        transf.getPoste() != null ? transf.getPoste().getCode() : "N/A"
                                ));
                            }
                        });
            }
        } catch (Exception e) {
            log.warn("Error fetching risk transformateurs: {}", e.getMessage());
        }

        return new RecentItemsDTO(recentAlerts, recentInterventions, recentComplaints, riskTransformateurs);
    }
}
