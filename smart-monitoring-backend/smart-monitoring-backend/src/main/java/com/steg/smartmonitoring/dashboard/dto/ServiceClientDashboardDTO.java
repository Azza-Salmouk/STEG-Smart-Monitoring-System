package com.steg.smartmonitoring.dashboard.dto;

import com.steg.smartmonitoring.complaint.dto.ComplaintStatisticsResponse;
import java.util.List;
import java.util.Map;

/**
 * DTO pour le dashboard SERVICE_CLIENT
 * Axé sur les réclamations et le service à la clientèle
 */
public class ServiceClientDashboardDTO {

    // KPIs de base
    private Long totalComplaints;
    private Long newComplaints;
    private Long analysisComplaints;
    private Long assignedComplaints;
    private Long processingComplaints;
    private Long resolvedComplaints;
    private Long criticalComplaints;

    // Statistiques détaillées
    private ComplaintStatisticsResponse complaintStats;

    // Statistics by priority (for charts)
    private Map<String, Long> byPriority;

    // Listes
    private List<ComplaintSummaryDTO> recentComplaints;
    private List<ComplaintSummaryDTO> criticalComplaintsList;
    private List<ComplaintSummaryDTO> unresolvedComplaints;

    public ServiceClientDashboardDTO() {
    }

    public ServiceClientDashboardDTO(
            Long totalComplaints,
            Long newComplaints,
            Long analysisComplaints,
            Long assignedComplaints,
            Long processingComplaints,
            Long resolvedComplaints,
            Long criticalComplaints,
            ComplaintStatisticsResponse complaintStats,
            List<ComplaintSummaryDTO> recentComplaints,
            List<ComplaintSummaryDTO> criticalComplaintsList,
            List<ComplaintSummaryDTO> unresolvedComplaints) {
        this.totalComplaints = totalComplaints;
        this.newComplaints = newComplaints;
        this.analysisComplaints = analysisComplaints;
        this.assignedComplaints = assignedComplaints;
        this.processingComplaints = processingComplaints;
        this.resolvedComplaints = resolvedComplaints;
        this.criticalComplaints = criticalComplaints;
        this.complaintStats = complaintStats;
        this.recentComplaints = recentComplaints;
        this.criticalComplaintsList = criticalComplaintsList;
        this.unresolvedComplaints = unresolvedComplaints;
    }

    // Getters & Setters
    public Long getTotalComplaints() { return totalComplaints; }
    public void setTotalComplaints(Long totalComplaints) { this.totalComplaints = totalComplaints; }

    public Long getNewComplaints() { return newComplaints; }
    public void setNewComplaints(Long newComplaints) { this.newComplaints = newComplaints; }

    public Long getAnalysisComplaints() { return analysisComplaints; }
    public void setAnalysisComplaints(Long analysisComplaints) { this.analysisComplaints = analysisComplaints; }

    public Long getAssignedComplaints() { return assignedComplaints; }
    public void setAssignedComplaints(Long assignedComplaints) { this.assignedComplaints = assignedComplaints; }

    public Long getProcessingComplaints() { return processingComplaints; }
    public void setProcessingComplaints(Long processingComplaints) { this.processingComplaints = processingComplaints; }

    public Long getResolvedComplaints() { return resolvedComplaints; }
    public void setResolvedComplaints(Long resolvedComplaints) { this.resolvedComplaints = resolvedComplaints; }

    public Long getCriticalComplaints() { return criticalComplaints; }
    public void setCriticalComplaints(Long criticalComplaints) { this.criticalComplaints = criticalComplaints; }

    public ComplaintStatisticsResponse getComplaintStats() { return complaintStats; }
    public void setComplaintStats(ComplaintStatisticsResponse complaintStats) { this.complaintStats = complaintStats; }

    public Map<String, Long> getByPriority() { return byPriority; }
    public void setByPriority(Map<String, Long> byPriority) { this.byPriority = byPriority; }

    public List<ComplaintSummaryDTO> getRecentComplaints() { return recentComplaints; }
    public void setRecentComplaints(List<ComplaintSummaryDTO> recentComplaints) { this.recentComplaints = recentComplaints; }

    public List<ComplaintSummaryDTO> getCriticalComplaintsList() { return criticalComplaintsList; }
    public void setCriticalComplaintsList(List<ComplaintSummaryDTO> criticalComplaintsList) { this.criticalComplaintsList = criticalComplaintsList; }

    public List<ComplaintSummaryDTO> getUnresolvedComplaints() { return unresolvedComplaints; }
    public void setUnresolvedComplaints(List<ComplaintSummaryDTO> unresolvedComplaints) { this.unresolvedComplaints = unresolvedComplaints; }

    // Inner DTO
    public static class ComplaintSummaryDTO {
        private Long id;
        private String reference;
        private String categorie;
        private String priorite;
        private String statut;
        private String description;
        private String createdAt;

        public ComplaintSummaryDTO() {}
        public ComplaintSummaryDTO(Long id, String reference, String categorie, String priorite, String statut, String description, String createdAt) {
            this.id = id;
            this.reference = reference;
            this.categorie = categorie;
            this.priorite = priorite;
            this.statut = statut;
            this.description = description;
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
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getCreatedAt() { return createdAt; }
        public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    }
}
