package com.steg.smartmonitoring.dashboard.dto;

import com.steg.smartmonitoring.alert.dto.AlertStatisticsResponse;
import com.steg.smartmonitoring.complaint.dto.ComplaintStatisticsResponse;
import com.steg.smartmonitoring.intervention.dto.InterventionStatisticsResponse;
import com.steg.smartmonitoring.measurement.service.MeasurementStatisticsResponse;
import com.steg.smartmonitoring.poste.service.PosteStatisticsResponse;
import com.steg.smartmonitoring.transformateur.service.TransformateurStatisticsResponse;

/**
 * DTO pour le dashboard ADMIN
 * Agrège toutes les statistiques du système
 */
public class AdminDashboardDTO {

    // Comptages simples
    private Long totalUsers;
    private Long totalPostes;
    private Long totalTransformateurs;
    private Long totalMeasurements;
    private Long totalInterventions;
    private Long totalComplaints;
    private Long totalAlerts;

    // KPIs critiques
    private Long criticalAlerts;
    private Long transformateursCritiques;
    private Long interventionsEnCours;
    private Long complaintsUnresolved;
    private Double averageHealthScore;

    // Statistiques détaillées
    private PosteStatisticsResponse posteStats;
    private TransformateurStatisticsResponse transformateurStats;
    private InterventionStatisticsResponse interventionStats;
    private AlertStatisticsResponse alertStats;
    private MeasurementStatisticsResponse measurementStats;

    // Récents
    private RecentItemsDTO recentItems;

    public AdminDashboardDTO() {
    }

    public AdminDashboardDTO(
            Long totalUsers,
            Long totalPostes,
            Long totalTransformateurs,
            Long totalMeasurements,
            Long totalInterventions,
            Long totalComplaints,
            Long totalAlerts,
            Long criticalAlerts,
            Long transformateursCritiques,
            Long interventionsEnCours,
            Long complaintsUnresolved,
            Double averageHealthScore,
            PosteStatisticsResponse posteStats,
            TransformateurStatisticsResponse transformateurStats,
            InterventionStatisticsResponse interventionStats,
            AlertStatisticsResponse alertStats,
            MeasurementStatisticsResponse measurementStats,
            RecentItemsDTO recentItems) {
        this.totalUsers = totalUsers;
        this.totalPostes = totalPostes;
        this.totalTransformateurs = totalTransformateurs;
        this.totalMeasurements = totalMeasurements;
        this.totalInterventions = totalInterventions;
        this.totalComplaints = totalComplaints;
        this.totalAlerts = totalAlerts;
        this.criticalAlerts = criticalAlerts;
        this.transformateursCritiques = transformateursCritiques;
        this.interventionsEnCours = interventionsEnCours;
        this.complaintsUnresolved = complaintsUnresolved;
        this.averageHealthScore = averageHealthScore;
        this.posteStats = posteStats;
        this.transformateurStats = transformateurStats;
        this.interventionStats = interventionStats;
        this.alertStats = alertStats;
        this.measurementStats = measurementStats;
        this.recentItems = recentItems;
    }

    // Getters & Setters
    public Long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(Long totalUsers) { this.totalUsers = totalUsers; }

    public Long getTotalPostes() { return totalPostes; }
    public void setTotalPostes(Long totalPostes) { this.totalPostes = totalPostes; }

    public Long getTotalTransformateurs() { return totalTransformateurs; }
    public void setTotalTransformateurs(Long totalTransformateurs) { this.totalTransformateurs = totalTransformateurs; }

    public Long getTotalMeasurements() { return totalMeasurements; }
    public void setTotalMeasurements(Long totalMeasurements) { this.totalMeasurements = totalMeasurements; }

    public Long getTotalInterventions() { return totalInterventions; }
    public void setTotalInterventions(Long totalInterventions) { this.totalInterventions = totalInterventions; }

    public Long getTotalComplaints() { return totalComplaints; }
    public void setTotalComplaints(Long totalComplaints) { this.totalComplaints = totalComplaints; }

    public Long getTotalAlerts() { return totalAlerts; }
    public void setTotalAlerts(Long totalAlerts) { this.totalAlerts = totalAlerts; }

    public Long getCriticalAlerts() { return criticalAlerts; }
    public void setCriticalAlerts(Long criticalAlerts) { this.criticalAlerts = criticalAlerts; }

    public Long getTransformateursCritiques() { return transformateursCritiques; }
    public void setTransformateursCritiques(Long transformateursCritiques) { this.transformateursCritiques = transformateursCritiques; }

    public Long getInterventionsEnCours() { return interventionsEnCours; }
    public void setInterventionsEnCours(Long interventionsEnCours) { this.interventionsEnCours = interventionsEnCours; }

    public Long getComplaintsUnresolved() { return complaintsUnresolved; }
    public void setComplaintsUnresolved(Long complaintsUnresolved) { this.complaintsUnresolved = complaintsUnresolved; }

    public Double getAverageHealthScore() { return averageHealthScore; }
    public void setAverageHealthScore(Double averageHealthScore) { this.averageHealthScore = averageHealthScore; }

    public PosteStatisticsResponse getPosteStats() { return posteStats; }
    public void setPosteStats(PosteStatisticsResponse posteStats) { this.posteStats = posteStats; }

    public TransformateurStatisticsResponse getTransformateurStats() { return transformateurStats; }
    public void setTransformateurStats(TransformateurStatisticsResponse transformateurStats) { this.transformateurStats = transformateurStats; }

    public InterventionStatisticsResponse getInterventionStats() { return interventionStats; }
    public void setInterventionStats(InterventionStatisticsResponse interventionStats) { this.interventionStats = interventionStats; }

    public AlertStatisticsResponse getAlertStats() { return alertStats; }
    public void setAlertStats(AlertStatisticsResponse alertStats) { this.alertStats = alertStats; }

    public MeasurementStatisticsResponse getMeasurementStats() { return measurementStats; }
    public void setMeasurementStats(MeasurementStatisticsResponse measurementStats) { this.measurementStats = measurementStats; }

    public RecentItemsDTO getRecentItems() { return recentItems; }
    public void setRecentItems(RecentItemsDTO recentItems) { this.recentItems = recentItems; }
}
