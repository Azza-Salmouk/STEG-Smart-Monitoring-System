package com.steg.smartmonitoring.dashboard.dto;

import java.util.List;

/**
 * DTO contenant les éléments récents pour les dashboards
 */
public class RecentItemsDTO {

    private List<RecentAlertDTO> recentAlerts;
    private List<RecentInterventionDTO> recentInterventions;
    private List<RecentComplaintDTO> recentComplaints;
    private List<RiskTransformateurDTO> riskTransformateurs;

    public RecentItemsDTO() {
    }

    public RecentItemsDTO(
            List<RecentAlertDTO> recentAlerts,
            List<RecentInterventionDTO> recentInterventions,
            List<RecentComplaintDTO> recentComplaints,
            List<RiskTransformateurDTO> riskTransformateurs) {
        this.recentAlerts = recentAlerts;
        this.recentInterventions = recentInterventions;
        this.recentComplaints = recentComplaints;
        this.riskTransformateurs = riskTransformateurs;
    }

    public List<RecentAlertDTO> getRecentAlerts() { return recentAlerts; }
    public void setRecentAlerts(List<RecentAlertDTO> recentAlerts) { this.recentAlerts = recentAlerts; }

    public List<RecentInterventionDTO> getRecentInterventions() { return recentInterventions; }
    public void setRecentInterventions(List<RecentInterventionDTO> recentInterventions) { this.recentInterventions = recentInterventions; }

    public List<RecentComplaintDTO> getRecentComplaints() { return recentComplaints; }
    public void setRecentComplaints(List<RecentComplaintDTO> recentComplaints) { this.recentComplaints = recentComplaints; }

    public List<RiskTransformateurDTO> getRiskTransformateurs() { return riskTransformateurs; }
    public void setRiskTransformateurs(List<RiskTransformateurDTO> riskTransformateurs) { this.riskTransformateurs = riskTransformateurs; }

    // Inner classes for recent items
    public static class RecentAlertDTO {
        private Long id;
        private String title;
        private String level;
        private String status;
        private Long transformateurId;
        private String transformateurReference;
        private String createdAt;

        public RecentAlertDTO() {}
        public RecentAlertDTO(Long id, String title, String level, String status, Long transformateurId, String transformateurReference, String createdAt) {
            this.id = id;
            this.title = title;
            this.level = level;
            this.status = status;
            this.transformateurId = transformateurId;
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
        public Long getTransformateurId() { return transformateurId; }
        public void setTransformateurId(Long transformateurId) { this.transformateurId = transformateurId; }
        public String getTransformateurReference() { return transformateurReference; }
        public void setTransformateurReference(String transformateurReference) { this.transformateurReference = transformateurReference; }
        public String getCreatedAt() { return createdAt; }
        public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    }

    public static class RecentInterventionDTO {
        private Long id;
        private String reference;
        private String statut;
        private String priorite;
        private Long transformateurId;
        private String transformateurReference;
        private String datePlanifiee;

        public RecentInterventionDTO() {}
        public RecentInterventionDTO(Long id, String reference, String statut, String priorite, Long transformateurId, String transformateurReference, String datePlanifiee) {
            this.id = id;
            this.reference = reference;
            this.statut = statut;
            this.priorite = priorite;
            this.transformateurId = transformateurId;
            this.transformateurReference = transformateurReference;
            this.datePlanifiee = datePlanifiee;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getReference() { return reference; }
        public void setReference(String reference) { this.reference = reference; }
        public String getStatut() { return statut; }
        public void setStatut(String statut) { this.statut = statut; }
        public String getPriorite() { return priorite; }
        public void setPriorite(String priorite) { this.priorite = priorite; }
        public Long getTransformateurId() { return transformateurId; }
        public void setTransformateurId(Long transformateurId) { this.transformateurId = transformateurId; }
        public String getTransformateurReference() { return transformateurReference; }
        public void setTransformateurReference(String transformateurReference) { this.transformateurReference = transformateurReference; }
        public String getDatePlanifiee() { return datePlanifiee; }
        public void setDatePlanifiee(String datePlanifiee) { this.datePlanifiee = datePlanifiee; }
    }

    public static class RecentComplaintDTO {
        private Long id;
        private String reference;
        private String categorie;
        private String priorite;
        private String statut;
        private String createdAt;

        public RecentComplaintDTO() {}
        public RecentComplaintDTO(Long id, String reference, String categorie, String priorite, String statut, String createdAt) {
            this.id = id;
            this.reference = reference;
            this.categorie = categorie;
            this.priorite = priorite;
            this.statut = statut;
            this.createdAt = createdAt;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getReference() { return reference; }
        public void setReference(String reference) { this.reference = reference; }
        public String getCategorie() { return categorie; }
        public void setCategorie(String categorie) { this.categorie = categorie; }
        public String getPriorite() { return priorite; }
        public void setPriorite(String priorite) { this.priorite = priorite; }
        public String getStatut() { return statut; }
        public void setStatut(String statut) { this.statut = statut; }
        public String getCreatedAt() { return createdAt; }
        public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    }

    public static class RiskTransformateurDTO {
        private Long id;
        private String reference;
        private String serialNumber;
        private Integer healthScore;
        private String riskLevel;
        private String status;
        private String posteCode;

        public RiskTransformateurDTO() {}
        public RiskTransformateurDTO(Long id, String reference, String serialNumber, Integer healthScore, String riskLevel, String status, String posteCode) {
            this.id = id;
            this.reference = reference;
            this.serialNumber = serialNumber;
            this.healthScore = healthScore;
            this.riskLevel = riskLevel;
            this.status = status;
            this.posteCode = posteCode;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getReference() { return reference; }
        public void setReference(String reference) { this.reference = reference; }
        public String getSerialNumber() { return serialNumber; }
        public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
        public Integer getHealthScore() { return healthScore; }
        public void setHealthScore(Integer healthScore) { this.healthScore = healthScore; }
        public String getRiskLevel() { return riskLevel; }
        public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getPosteCode() { return posteCode; }
        public void setPosteCode(String posteCode) { this.posteCode = posteCode; }
    }
}
