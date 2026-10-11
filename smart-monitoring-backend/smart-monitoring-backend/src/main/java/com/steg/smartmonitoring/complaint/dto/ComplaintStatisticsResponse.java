package com.steg.smartmonitoring.complaint.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * DTO pour les statistiques des réclamations
 */
public class ComplaintStatisticsResponse {

    private Long totalComplaints;

    private Long nouvelles;

    private Long enAnalyse;

    private Long assignees;

    private Long enTraitement;

    private Long resolues;

    private Long fermees;

    private Long critiques;

    private Double averageResolutionTime;

    private Double resolutionRate;

    private Map<String, Long> byPriority;

    public ComplaintStatisticsResponse() {
        this.byPriority = new HashMap<>();
    }

    public ComplaintStatisticsResponse(Long totalComplaints, Long nouvelles, Long enAnalyse, Long assignees, Long enTraitement, Long resolues, Long fermees, Long critiques, Double averageResolutionTime, Double resolutionRate, Map<String, Long> byPriority) {
        this.totalComplaints = totalComplaints;
        this.nouvelles = nouvelles;
        this.enAnalyse = enAnalyse;
        this.assignees = assignees;
        this.enTraitement = enTraitement;
        this.resolues = resolues;
        this.fermees = fermees;
        this.critiques = critiques;
        this.averageResolutionTime = averageResolutionTime;
        this.resolutionRate = resolutionRate;
        this.byPriority = byPriority != null ? byPriority : new HashMap<>();
    }

    public Long getTotalComplaints() {
        return totalComplaints;
    }

    public void setTotalComplaints(Long totalComplaints) {
        this.totalComplaints = totalComplaints;
    }

    public Long getNouveilles() {
        return nouvelles;
    }

    public void setNouveilles(Long nouvelles) {
        this.nouvelles = nouvelles;
    }

    public Long getEnAnalyse() {
        return enAnalyse;
    }

    public void setEnAnalyse(Long enAnalyse) {
        this.enAnalyse = enAnalyse;
    }

    public Long getAssignees() {
        return assignees;
    }

    public void setAssignees(Long assignees) {
        this.assignees = assignees;
    }

    public Long getEnTraitement() {
        return enTraitement;
    }

    public void setEnTraitement(Long enTraitement) {
        this.enTraitement = enTraitement;
    }

    public Long getResolues() {
        return resolues;
    }

    public void setResolues(Long resolues) {
        this.resolues = resolues;
    }

    public Long getFermees() {
        return fermees;
    }

    public void setFermees(Long fermees) {
        this.fermees = fermees;
    }

    public Long getCritiques() {
        return critiques;
    }

    public void setCritiques(Long critiques) {
        this.critiques = critiques;
    }

    public Double getAverageResolutionTime() {
        return averageResolutionTime;
    }

    public void setAverageResolutionTime(Double averageResolutionTime) {
        this.averageResolutionTime = averageResolutionTime;
    }

    public Double getResolutionRate() {
        return resolutionRate;
    }

    public void setResolutionRate(Double resolutionRate) {
        this.resolutionRate = resolutionRate;
    }

    public Map<String, Long> getByPriority() {
        return byPriority;
    }

    public void setByPriority(Map<String, Long> byPriority) {
        this.byPriority = byPriority;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long totalComplaints;
        private Long nouvelles;
        private Long enAnalyse;
        private Long assignees;
        private Long enTraitement;
        private Long resolues;
        private Long fermees;
        private Long critiques;
        private Double averageResolutionTime;
        private Double resolutionRate;
        private Map<String, Long> byPriority;

        public Builder totalComplaints(Long totalComplaints) {
            this.totalComplaints = totalComplaints;
            return this;
        }

        public Builder nouvelles(Long nouvelles) {
            this.nouvelles = nouvelles;
            return this;
        }

        public Builder enAnalyse(Long enAnalyse) {
            this.enAnalyse = enAnalyse;
            return this;
        }

        public Builder assignees(Long assignees) {
            this.assignees = assignees;
            return this;
        }

        public Builder enTraitement(Long enTraitement) {
            this.enTraitement = enTraitement;
            return this;
        }

        public Builder resolues(Long resolues) {
            this.resolues = resolues;
            return this;
        }

        public Builder fermees(Long fermees) {
            this.fermees = fermees;
            return this;
        }

        public Builder critiques(Long critiques) {
            this.critiques = critiques;
            return this;
        }

        public Builder averageResolutionTime(Double averageResolutionTime) {
            this.averageResolutionTime = averageResolutionTime;
            return this;
        }

        public Builder resolutionRate(Double resolutionRate) {
            this.resolutionRate = resolutionRate;
            return this;
        }

        public Builder byPriority(Map<String, Long> byPriority) {
            this.byPriority = byPriority;
            return this;
        }

        public ComplaintStatisticsResponse build() {
            return new ComplaintStatisticsResponse(totalComplaints, nouvelles, enAnalyse, assignees, enTraitement, resolues, fermees, critiques, averageResolutionTime, resolutionRate, byPriority);
        }
    }
}
