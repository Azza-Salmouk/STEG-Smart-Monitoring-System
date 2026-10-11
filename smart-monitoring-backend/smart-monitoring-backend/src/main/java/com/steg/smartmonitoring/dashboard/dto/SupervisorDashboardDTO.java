package com.steg.smartmonitoring.dashboard.dto;

import com.steg.smartmonitoring.alert.dto.AlertStatisticsResponse;
import com.steg.smartmonitoring.complaint.dto.ComplaintStatisticsResponse;
import com.steg.smartmonitoring.intervention.dto.InterventionStatisticsResponse;
import com.steg.smartmonitoring.poste.service.PosteStatisticsResponse;
import com.steg.smartmonitoring.transformateur.service.TransformateurStatisticsResponse;
import java.util.List;

/**
 * DTO pour le dashboard SUPERVISOR
 * Vision opérationnelle et tactique
 */
public class SupervisorDashboardDTO {

    // KPIs principaux
    private Long totalPostes;
    private Long totalTransformateurs;
    private Double averageHealthScore;
    private Long activeAlerts;
    private Long criticalAlerts;
    private Long interventionsEnCours;
    private Long interventionsTerminees;
    private Long complaintsOpen;
    private Long transformateursCritiques;

    // Statistiques détaillées
    private AlertStatisticsResponse alertStats;
    private InterventionStatisticsResponse interventionStats;
    private PosteStatisticsResponse posteStats;
    private TransformateurStatisticsResponse transformateurStats;
    private ComplaintStatisticsResponse complaintStats;

    // Listes critiques
    private List<CriticalAlertDTO> criticalAlertsList;
    private List<LowHealthScoreTransformateurDTO> lowHealthTransformateurs;
    private List<UrgentInterventionDTO> urgentInterventions;
    private List<ImportantComplaintDTO> importantComplaints;

    public SupervisorDashboardDTO() {
    }

    public SupervisorDashboardDTO(
            Long totalPostes,
            Long totalTransformateurs,
            Double averageHealthScore,
            Long activeAlerts,
            Long criticalAlerts,
            Long interventionsEnCours,
            Long interventionsTerminees,
            Long complaintsOpen,
            Long transformateursCritiques,
            AlertStatisticsResponse alertStats,
            InterventionStatisticsResponse interventionStats,
            PosteStatisticsResponse posteStats,
            TransformateurStatisticsResponse transformateurStats,
            ComplaintStatisticsResponse complaintStats,
            List<CriticalAlertDTO> criticalAlertsList,
            List<LowHealthScoreTransformateurDTO> lowHealthTransformateurs,
            List<UrgentInterventionDTO> urgentInterventions,
            List<ImportantComplaintDTO> importantComplaints) {
        this.totalPostes = totalPostes;
        this.totalTransformateurs = totalTransformateurs;
        this.averageHealthScore = averageHealthScore;
        this.activeAlerts = activeAlerts;
        this.criticalAlerts = criticalAlerts;
        this.interventionsEnCours = interventionsEnCours;
        this.interventionsTerminees = interventionsTerminees;
        this.complaintsOpen = complaintsOpen;
        this.transformateursCritiques = transformateursCritiques;
        this.alertStats = alertStats;
        this.interventionStats = interventionStats;
        this.posteStats = posteStats;
        this.transformateurStats = transformateurStats;
        this.complaintStats = complaintStats;
        this.criticalAlertsList = criticalAlertsList;
        this.lowHealthTransformateurs = lowHealthTransformateurs;
        this.urgentInterventions = urgentInterventions;
        this.importantComplaints = importantComplaints;
    }

    // Getters & Setters
    public Long getTotalPostes() { return totalPostes; }
    public void setTotalPostes(Long totalPostes) { this.totalPostes = totalPostes; }

    public Long getTotalTransformateurs() { return totalTransformateurs; }
    public void setTotalTransformateurs(Long totalTransformateurs) { this.totalTransformateurs = totalTransformateurs; }

    public Double getAverageHealthScore() { return averageHealthScore; }
    public void setAverageHealthScore(Double averageHealthScore) { this.averageHealthScore = averageHealthScore; }

    public Long getActiveAlerts() { return activeAlerts; }
    public void setActiveAlerts(Long activeAlerts) { this.activeAlerts = activeAlerts; }

    public Long getCriticalAlerts() { return criticalAlerts; }
    public void setCriticalAlerts(Long criticalAlerts) { this.criticalAlerts = criticalAlerts; }

    public Long getInterventionsEnCours() { return interventionsEnCours; }
    public void setInterventionsEnCours(Long interventionsEnCours) { this.interventionsEnCours = interventionsEnCours; }

    public Long getInterventionsTerminees() { return interventionsTerminees; }
    public void setInterventionsTerminees(Long interventionsTerminees) { this.interventionsTerminees = interventionsTerminees; }

    public Long getComplaintsOpen() { return complaintsOpen; }
    public void setComplaintsOpen(Long complaintsOpen) { this.complaintsOpen = complaintsOpen; }

    public Long getTransformateursCritiques() { return transformateursCritiques; }
    public void setTransformateursCritiques(Long transformateursCritiques) { this.transformateursCritiques = transformateursCritiques; }

    public AlertStatisticsResponse getAlertStats() { return alertStats; }
    public void setAlertStats(AlertStatisticsResponse alertStats) { this.alertStats = alertStats; }

    public InterventionStatisticsResponse getInterventionStats() { return interventionStats; }
    public void setInterventionStats(InterventionStatisticsResponse interventionStats) { this.interventionStats = interventionStats; }

    public PosteStatisticsResponse getPosteStats() { return posteStats; }
    public void setPosteStats(PosteStatisticsResponse posteStats) { this.posteStats = posteStats; }

    public TransformateurStatisticsResponse getTransformateurStats() { return transformateurStats; }
    public void setTransformateurStats(TransformateurStatisticsResponse transformateurStats) { this.transformateurStats = transformateurStats; }

    public ComplaintStatisticsResponse getComplaintStats() { return complaintStats; }
    public void setComplaintStats(ComplaintStatisticsResponse complaintStats) { this.complaintStats = complaintStats; }

    public List<CriticalAlertDTO> getCriticalAlertsList() { return criticalAlertsList; }
    public void setCriticalAlertsList(List<CriticalAlertDTO> criticalAlertsList) { this.criticalAlertsList = criticalAlertsList; }

    public List<LowHealthScoreTransformateurDTO> getLowHealthTransformateurs() { return lowHealthTransformateurs; }
    public void setLowHealthTransformateurs(List<LowHealthScoreTransformateurDTO> lowHealthTransformateurs) { this.lowHealthTransformateurs = lowHealthTransformateurs; }

    public List<UrgentInterventionDTO> getUrgentInterventions() { return urgentInterventions; }
    public void setUrgentInterventions(List<UrgentInterventionDTO> urgentInterventions) { this.urgentInterventions = urgentInterventions; }

    public List<ImportantComplaintDTO> getImportantComplaints() { return importantComplaints; }
    public void setImportantComplaints(List<ImportantComplaintDTO> importantComplaints) { this.importantComplaints = importantComplaints; }

    // Inner DTOs
    public static class CriticalAlertDTO {
        private Long id;
        private String title;
        private String level;
        private String transformateurReference;
        private String createdAt;

        public CriticalAlertDTO() {}
        public CriticalAlertDTO(Long id, String title, String level, String transformateurReference, String createdAt) {
            this.id = id;
            this.title = title;
            this.level = level;
            this.transformateurReference = transformateurReference;
            this.createdAt = createdAt;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getLevel() { return level; }
        public void setLevel(String level) { this.level = level; }
        public String getTransformateurReference() { return transformateurReference; }
        public void setTransformateurReference(String transformateurReference) { this.transformateurReference = transformateurReference; }
        public String getCreatedAt() { return createdAt; }
        public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    }

    public static class LowHealthScoreTransformateurDTO {
        private Long id;
        private String reference;
        private Integer healthScore;
        private String status;

        public LowHealthScoreTransformateurDTO() {}
        public LowHealthScoreTransformateurDTO(Long id, String reference, Integer healthScore, String status) {
            this.id = id;
            this.reference = reference;
            this.healthScore = healthScore;
            this.status = status;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getReference() { return reference; }
        public void setReference(String reference) { this.reference = reference; }
        public Integer getHealthScore() { return healthScore; }
        public void setHealthScore(Integer healthScore) { this.healthScore = healthScore; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    public static class UrgentInterventionDTO {
        private Long id;
        private String reference;
        private String priorite;
        private String transformateurReference;
        private String datePlanifiee;

        public UrgentInterventionDTO() {}
        public UrgentInterventionDTO(Long id, String reference, String priorite, String transformateurReference, String datePlanifiee) {
            this.id = id;
            this.reference = reference;
            this.priorite = priorite;
            this.transformateurReference = transformateurReference;
            this.datePlanifiee = datePlanifiee;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getReference() { return reference; }
        public void setReference(String reference) { this.reference = reference; }
        public String getPriorite() { return priorite; }
        public void setPriorite(String priorite) { this.priorite = priorite; }
        public String getTransformateurReference() { return transformateurReference; }
        public void setTransformateurReference(String transformateurReference) { this.transformateurReference = transformateurReference; }
        public String getDatePlanifiee() { return datePlanifiee; }
        public void setDatePlanifiee(String datePlanifiee) { this.datePlanifiee = datePlanifiee; }
    }

    public static class ImportantComplaintDTO {
        private Long id;
        private String reference;
        private String priorite;
        private String statut;

        public ImportantComplaintDTO() {}
        public ImportantComplaintDTO(Long id, String reference, String priorite, String statut) {
            this.id = id;
            this.reference = reference;
            this.priorite = priorite;
            this.statut = statut;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getReference() { return reference; }
        public void setReference(String reference) { this.reference = reference; }
        public String getPriorite() { return priorite; }
        public void setPriorite(String priorite) { this.priorite = priorite; }
        public String getStatut() { return statut; }
        public void setStatut(String statut) { this.statut = statut; }
    }
}
