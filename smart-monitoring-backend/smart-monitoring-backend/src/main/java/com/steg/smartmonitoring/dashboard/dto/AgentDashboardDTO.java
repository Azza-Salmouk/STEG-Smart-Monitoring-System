package com.steg.smartmonitoring.dashboard.dto;

import java.util.List;
import java.util.Map;

/**
 * DTO pour le dashboard AGENT
 * Vue terrain et opérationnelle
 */
public class AgentDashboardDTO {

    // KPIs personnels de l'agent
    private Long myInterventionsTotal;
    private Long myInterventionsEnCours;
    private Long myInterventionsTerminees;
    private Long myInterventionsCritiques;
    private Long accessibleTransformateurs;
    private Long recentMeasurements;
    private Long accessibleAlerts;

    // Aliases pour le frontend (myInterventionsEnAttente au lieu de myInterventionsPending)
    private Long myInterventionsEnAttente;
    private Long totalInterventions;
    private Long interventionsEnCours;
    private Long interventionsCompleted;
    private Long totalAlerts;
    private Long criticalAlerts;

    // Statistics by priority (for charts)
    private Map<String, Long> byPriority;

    // Listes
    private List<MyInterventionDTO> myInterventionsList;
    private List<AccessibleAlertDTO> accessibleAlertsList;
    private List<RecentMeasurementDTO> recentMeasurementsList;
    
    // RecentItems wrapper for frontend
    private RecentItemsWrapper recentItems;

    public AgentDashboardDTO() {
    }

    public AgentDashboardDTO(
            Long myInterventionsTotal,
            Long myInterventionsEnCours,
            Long myInterventionsTerminees,
            Long myInterventionsCritiques,
            Long accessibleTransformateurs,
            Long recentMeasurements,
            Long accessibleAlerts,
            List<MyInterventionDTO> myInterventionsList,
            List<AccessibleAlertDTO> accessibleAlertsList,
            List<RecentMeasurementDTO> recentMeasurementsList) {
        this.myInterventionsTotal = myInterventionsTotal;
        this.myInterventionsEnCours = myInterventionsEnCours;
        this.myInterventionsTerminees = myInterventionsTerminees;
        this.myInterventionsCritiques = myInterventionsCritiques;
        this.accessibleTransformateurs = accessibleTransformateurs;
        this.recentMeasurements = recentMeasurements;
        this.accessibleAlerts = accessibleAlerts;
        this.myInterventionsList = myInterventionsList;
        this.accessibleAlertsList = accessibleAlertsList;
        this.recentMeasurementsList = recentMeasurementsList;
        
        // Set frontend aliases
        this.myInterventionsEnAttente = 0L; // Will be calculated by frontend
        this.totalInterventions = myInterventionsTotal;
        this.interventionsEnCours = myInterventionsEnCours;
        this.interventionsCompleted = myInterventionsTerminees;
        this.totalAlerts = accessibleAlerts;
        this.criticalAlerts = 0L;
        
        // Initialize recentItems wrapper
        this.recentItems = new RecentItemsWrapper(myInterventionsList, accessibleAlertsList);
    }

    // Getters & Setters
    public Long getMyInterventionsTotal() { return myInterventionsTotal; }
    public void setMyInterventionsTotal(Long myInterventionsTotal) { this.myInterventionsTotal = myInterventionsTotal; }

    public Long getMyInterventionsEnCours() { return myInterventionsEnCours; }
    public void setMyInterventionsEnCours(Long myInterventionsEnCours) { this.myInterventionsEnCours = myInterventionsEnCours; }

    public Long getMyInterventionsTerminees() { return myInterventionsTerminees; }
    public void setMyInterventionsTerminees(Long myInterventionsTerminees) { this.myInterventionsTerminees = myInterventionsTerminees; }

    public Long getMyInterventionsCritiques() { return myInterventionsCritiques; }
    public void setMyInterventionsCritiques(Long myInterventionsCritiques) { this.myInterventionsCritiques = myInterventionsCritiques; }

    public Long getAccessibleTransformateurs() { return accessibleTransformateurs; }
    public void setAccessibleTransformateurs(Long accessibleTransformateurs) { this.accessibleTransformateurs = accessibleTransformateurs; }

    public Long getRecentMeasurements() { return recentMeasurements; }
    public void setRecentMeasurements(Long recentMeasurements) { this.recentMeasurements = recentMeasurements; }

    public Long getAccessibleAlerts() { return accessibleAlerts; }
    public void setAccessibleAlerts(Long accessibleAlerts) { this.accessibleAlerts = accessibleAlerts; }

    public List<MyInterventionDTO> getMyInterventionsList() { return myInterventionsList; }
    public void setMyInterventionsList(List<MyInterventionDTO> myInterventionsList) { this.myInterventionsList = myInterventionsList; }

    public List<AccessibleAlertDTO> getAccessibleAlertsList() { return accessibleAlertsList; }
    public void setAccessibleAlertsList(List<AccessibleAlertDTO> accessibleAlertsList) { this.accessibleAlertsList = accessibleAlertsList; }

    public List<RecentMeasurementDTO> getRecentMeasurementsList() { return recentMeasurementsList; }
    public void setRecentMeasurementsList(List<RecentMeasurementDTO> recentMeasurementsList) { this.recentMeasurementsList = recentMeasurementsList; }

    public RecentItemsWrapper getRecentItems() { return recentItems; }
    public void setRecentItems(RecentItemsWrapper recentItems) { this.recentItems = recentItems; }

    public Long getMyInterventionsEnAttente() { return myInterventionsEnAttente; }
    public void setMyInterventionsEnAttente(Long myInterventionsEnAttente) { this.myInterventionsEnAttente = myInterventionsEnAttente; }

    public Long getTotalInterventions() { return totalInterventions; }
    public void setTotalInterventions(Long totalInterventions) { this.totalInterventions = totalInterventions; }

    public Long getInterventionsEnCours() { return interventionsEnCours; }
    public void setInterventionsEnCours(Long interventionsEnCours) { this.interventionsEnCours = interventionsEnCours; }

    public Long getInterventionsCompleted() { return interventionsCompleted; }
    public void setInterventionsCompleted(Long interventionsCompleted) { this.interventionsCompleted = interventionsCompleted; }

    public Long getTotalAlerts() { return totalAlerts; }
    public void setTotalAlerts(Long totalAlerts) { this.totalAlerts = totalAlerts; }

    public Long getCriticalAlerts() { return criticalAlerts; }
    public void setCriticalAlerts(Long criticalAlerts) { this.criticalAlerts = criticalAlerts; }

    public Map<String, Long> getByPriority() { return byPriority; }
    public void setByPriority(Map<String, Long> byPriority) { this.byPriority = byPriority; }

    // RecentItemsWrapper to wrap interventions and alerts for frontend
    public static class RecentItemsWrapper {
        private List<MyInterventionDTO> recentInterventions;
        private List<AccessibleAlertDTO> recentAlerts;

        public RecentItemsWrapper() {}
        public RecentItemsWrapper(List<MyInterventionDTO> recentInterventions, List<AccessibleAlertDTO> recentAlerts) {
            this.recentInterventions = recentInterventions;
            this.recentAlerts = recentAlerts;
        }

        public List<MyInterventionDTO> getRecentInterventions() { return recentInterventions; }
        public void setRecentInterventions(List<MyInterventionDTO> recentInterventions) { this.recentInterventions = recentInterventions; }

        public List<AccessibleAlertDTO> getRecentAlerts() { return recentAlerts; }
        public void setRecentAlerts(List<AccessibleAlertDTO> recentAlerts) { this.recentAlerts = recentAlerts; }
    }

    // Inner DTOs
    public static class MyInterventionDTO {
        private Long id;
        private String reference;
        private String transformateurReference;
        private String posteCode;
        private String priorite;
        private String statut;
        private String datePlanifiee;
        private String createdAt;
        private String description;

        public MyInterventionDTO() {}
        public MyInterventionDTO(Long id, String reference, String transformateurReference, String posteCode, String priorite, String statut, String datePlanifiee, String description) {
            this.id = id;
            this.reference = reference;
            this.transformateurReference = transformateurReference;
            this.posteCode = posteCode;
            this.priorite = priorite;
            this.statut = statut;
            this.datePlanifiee = datePlanifiee;
            this.description = description;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getReference() { return reference; }
        public void setReference(String reference) { this.reference = reference; }
        public String getTransformateurReference() { return transformateurReference; }
        public void setTransformateurReference(String transformateurReference) { this.transformateurReference = transformateurReference; }
        public String getPosteCode() { return posteCode; }
        public void setPosteCode(String posteCode) { this.posteCode = posteCode; }
        public String getPriorite() { return priorite; }
        public void setPriorite(String priorite) { this.priorite = priorite; }
        public String getStatut() { return statut; }
        public void setStatut(String statut) { this.statut = statut; }
        public String getDatePlanifiee() { return datePlanifiee; }
        public void setDatePlanifiee(String datePlanifiee) { this.datePlanifiee = datePlanifiee; }
        public String getCreatedAt() { return createdAt; }
        public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    public static class AccessibleAlertDTO {
        private Long id;
        private String title;
        private String level;
        private String status;
        private String transformateurReference;
        private String createdAt;

        public AccessibleAlertDTO() {}
        public AccessibleAlertDTO(Long id, String title, String level, String status, String transformateurReference, String createdAt) {
            this.id = id;
            this.title = title;
            this.level = level;
            this.status = status;
            this.transformateurReference = transformateurReference;
            this.createdAt = createdAt;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getLevel() { return level; }
        public void setLevel(String level) { this.level = level; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getTransformateurReference() { return transformateurReference; }
        public void setTransformateurReference(String transformateurReference) { this.transformateurReference = transformateurReference; }
        public String getCreatedAt() { return createdAt; }
        public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    }

    public static class RecentMeasurementDTO {
        private Long id;
        private String transformateurReference;
        private Double temperature;
        private Double voltage;
        private Double current;
        private Double power;
        private Integer healthScore;
        private String status;
        private String measurementDate;

        public RecentMeasurementDTO() {}
        public RecentMeasurementDTO(Long id, String transformateurReference, Double temperature, Double voltage, Double current, Double power, Integer healthScore, String status, String measurementDate) {
            this.id = id;
            this.transformateurReference = transformateurReference;
            this.temperature = temperature;
            this.voltage = voltage;
            this.current = current;
            this.power = power;
            this.healthScore = healthScore;
            this.status = status;
            this.measurementDate = measurementDate;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getTransformateurReference() { return transformateurReference; }
        public void setTransformateurReference(String transformateurReference) { this.transformateurReference = transformateurReference; }
        public Double getTemperature() { return temperature; }
        public void setTemperature(Double temperature) { this.temperature = temperature; }
        public Double getVoltage() { return voltage; }
        public void setVoltage(Double voltage) { this.voltage = voltage; }
        public Double getCurrent() { return current; }
        public void setCurrent(Double current) { this.current = current; }
        public Double getPower() { return power; }
        public void setPower(Double power) { this.power = power; }
        public Integer getHealthScore() { return healthScore; }
        public void setHealthScore(Integer healthScore) { this.healthScore = healthScore; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getMeasurementDate() { return measurementDate; }
        public void setMeasurementDate(String measurementDate) { this.measurementDate = measurementDate; }
    }
}
