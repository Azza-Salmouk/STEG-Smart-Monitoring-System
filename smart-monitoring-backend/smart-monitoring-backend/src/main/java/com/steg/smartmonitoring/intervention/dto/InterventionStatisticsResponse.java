package com.steg.smartmonitoring.intervention.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * DTO pour les statistiques des interventions
 */
public class InterventionStatisticsResponse {

    private Long totalInterventions;
    private Long enAttente;
    private Long enCours;
    private Long terminees;
    private Long critiques;
    private Double completionRate;
    private Double averageDuration;
    private Double averageCost;
    private Map<String, Long> byPriority;

    public InterventionStatisticsResponse() {
        this.byPriority = new HashMap<>();
    }

    public InterventionStatisticsResponse(Long totalInterventions, Long enAttente, Long enCours, Long terminees, Long critiques, Double completionRate, Double averageDuration, Double averageCost, Map<String, Long> byPriority) {
        this.totalInterventions = totalInterventions;
        this.enAttente = enAttente;
        this.enCours = enCours;
        this.terminees = terminees;
        this.critiques = critiques;
        this.completionRate = completionRate;
        this.averageDuration = averageDuration;
        this.averageCost = averageCost;
        this.byPriority = byPriority != null ? byPriority : new HashMap<>();
    }

    public Long getTotalInterventions() {
        return totalInterventions;
    }

    public void setTotalInterventions(Long totalInterventions) {
        this.totalInterventions = totalInterventions;
    }

    public Long getEnAttente() {
        return enAttente;
    }

    public void setEnAttente(Long enAttente) {
        this.enAttente = enAttente;
    }

    public Long getEnCours() {
        return enCours;
    }

    public void setEnCours(Long enCours) {
        this.enCours = enCours;
    }

    public Long getTerminees() {
        return terminees;
    }

    public void setTerminees(Long terminees) {
        this.terminees = terminees;
    }

    public Long getCritiques() {
        return critiques;
    }

    public void setCritiques(Long critiques) {
        this.critiques = critiques;
    }

    public Double getCompletionRate() {
        return completionRate;
    }

    public void setCompletionRate(Double completionRate) {
        this.completionRate = completionRate;
    }

    public Double getAverageDuration() {
        return averageDuration;
    }

    public void setAverageDuration(Double averageDuration) {
        this.averageDuration = averageDuration;
    }

    public Double getAverageCost() {
        return averageCost;
    }

    public void setAverageCost(Double averageCost) {
        this.averageCost = averageCost;
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
        private Long totalInterventions;
        private Long enAttente;
        private Long enCours;
        private Long terminees;
        private Long critiques;
        private Double completionRate;
        private Double averageDuration;
        private Double averageCost;
        private Map<String, Long> byPriority;

        public Builder totalInterventions(Long totalInterventions) {
            this.totalInterventions = totalInterventions;
            return this;
        }

        public Builder enAttente(Long enAttente) {
            this.enAttente = enAttente;
            return this;
        }

        public Builder enCours(Long enCours) {
            this.enCours = enCours;
            return this;
        }

        public Builder terminees(Long terminees) {
            this.terminees = terminees;
            return this;
        }

        public Builder critiques(Long critiques) {
            this.critiques = critiques;
            return this;
        }

        public Builder completionRate(Double completionRate) {
            this.completionRate = completionRate;
            return this;
        }

        public Builder averageDuration(Double averageDuration) {
            this.averageDuration = averageDuration;
            return this;
        }

        public Builder averageCost(Double averageCost) {
            this.averageCost = averageCost;
            return this;
        }

        public Builder byPriority(Map<String, Long> byPriority) {
            this.byPriority = byPriority;
            return this;
        }

        public InterventionStatisticsResponse build() {
            return new InterventionStatisticsResponse(totalInterventions, enAttente, enCours, terminees, critiques, completionRate, averageDuration, averageCost, byPriority);
        }
    }
}
