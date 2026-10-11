package com.steg.smartmonitoring.transformateur.service;

/**
 * DTO pour retourner les statistiques des transformateurs
 */
public class TransformateurStatisticsResponse {
    private Long totalTransformateurs;
    private Long transformateursActifs;
    private Long transformateursAlertes;
    private Long transformateursDefauts;
    private Long transformateursCritiques;
    private Double averageHealthScore;

    public TransformateurStatisticsResponse() {
    }

    public TransformateurStatisticsResponse(Long totalTransformateurs, Long transformateursActifs, Long transformateursAlertes, Long transformateursDefauts, Long transformateursCritiques, Double averageHealthScore) {
        this.totalTransformateurs = totalTransformateurs;
        this.transformateursActifs = transformateursActifs;
        this.transformateursAlertes = transformateursAlertes;
        this.transformateursDefauts = transformateursDefauts;
        this.transformateursCritiques = transformateursCritiques;
        this.averageHealthScore = averageHealthScore;
    }

    public Long getTotalTransformateurs() { return totalTransformateurs; }
    public void setTotalTransformateurs(Long totalTransformateurs) { this.totalTransformateurs = totalTransformateurs; }
    public Long getTransformateursActifs() { return transformateursActifs; }
    public void setTransformateursActifs(Long transformateursActifs) { this.transformateursActifs = transformateursActifs; }
    public Long getTransformateursAlertes() { return transformateursAlertes; }
    public void setTransformateursAlertes(Long transformateursAlertes) { this.transformateursAlertes = transformateursAlertes; }
    public Long getTransformateursDefauts() { return transformateursDefauts; }
    public void setTransformateursDefauts(Long transformateursDefauts) { this.transformateursDefauts = transformateursDefauts; }
    public Long getTransformateursCritiques() { return transformateursCritiques; }
    public void setTransformateursCritiques(Long transformateursCritiques) { this.transformateursCritiques = transformateursCritiques; }
    public Double getAverageHealthScore() { return averageHealthScore; }
    public void setAverageHealthScore(Double averageHealthScore) { this.averageHealthScore = averageHealthScore; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long totalTransformateurs;
        private Long transformateursActifs;
        private Long transformateursAlertes;
        private Long transformateursDefauts;
        private Long transformateursCritiques;
        private Double averageHealthScore;

        public Builder totalTransformateurs(Long totalTransformateurs) { this.totalTransformateurs = totalTransformateurs; return this; }
        public Builder transformateursActifs(Long transformateursActifs) { this.transformateursActifs = transformateursActifs; return this; }
        public Builder transformateursAlertes(Long transformateursAlertes) { this.transformateursAlertes = transformateursAlertes; return this; }
        public Builder transformateursDefauts(Long transformateursDefauts) { this.transformateursDefauts = transformateursDefauts; return this; }
        public Builder transformateursCritiques(Long transformateursCritiques) { this.transformateursCritiques = transformateursCritiques; return this; }
        public Builder averageHealthScore(Double averageHealthScore) { this.averageHealthScore = averageHealthScore; return this; }

        public TransformateurStatisticsResponse build() {
            return new TransformateurStatisticsResponse(totalTransformateurs, transformateursActifs, transformateursAlertes, transformateursDefauts, transformateursCritiques, averageHealthScore);
        }
    }
}
