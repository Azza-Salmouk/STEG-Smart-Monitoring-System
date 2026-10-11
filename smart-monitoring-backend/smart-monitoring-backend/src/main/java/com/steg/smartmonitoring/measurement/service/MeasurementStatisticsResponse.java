package com.steg.smartmonitoring.measurement.service;

import java.time.LocalDateTime;

/**
 * DTO contenant les statistiques des mesures IoT
 */
public class MeasurementStatisticsResponse {

    /**
     * Nombre total de mesures
     */
    private Long totalMeasurements;

    /**
     * Nombre de mesures critiques
     */
    private Long criticalMeasurements;

    /**
     * Nombre de mesures en avertissement
     */
    private Long warningMeasurements;

    /**
     * Nombre de mesures normales
     */
    private Long normalMeasurements;

    /**
     * Nombre de mesures hors ligne
     */
    private Long offlineMeasurements;

    /**
     * Température moyenne
     */
    private Double averageTemperature;

    /**
     * Température minimale
     */
    private Double minTemperature;

    /**
     * Température maximale
     */
    private Double maxTemperature;

    /**
     * Tension moyenne
     */
    private Double averageVoltage;

    /**
     * Courant moyen
     */
    private Double averageCurrent;

    /**
     * Puissance moyenne
     */
    private Double averagePower;

    /**
     * Health Score moyen
     */
    private Double averageHealthScore;

    /**
     * Health Score minimal
     */
    private Integer minHealthScore;

    /**
     * Health Score maximal
     */
    private Integer maxHealthScore;

    /**
     * Date et heure de la première mesure
     */
    private LocalDateTime firstMeasurementDate;

    /**
     * Date et heure de la dernière mesure
     */
    private LocalDateTime lastMeasurementDate;

    /**
     * Nombre de transformateurs avec mesures
     */
    private Long transformateurCount;

    public MeasurementStatisticsResponse() {
    }

    public MeasurementStatisticsResponse(Long totalMeasurements, Long criticalMeasurements, Long warningMeasurements, Long normalMeasurements, Long offlineMeasurements, Double averageTemperature, Double minTemperature, Double maxTemperature, Double averageVoltage, Double averageCurrent, Double averagePower, Double averageHealthScore, Integer minHealthScore, Integer maxHealthScore, LocalDateTime firstMeasurementDate, LocalDateTime lastMeasurementDate, Long transformateurCount) {
        this.totalMeasurements = totalMeasurements;
        this.criticalMeasurements = criticalMeasurements;
        this.warningMeasurements = warningMeasurements;
        this.normalMeasurements = normalMeasurements;
        this.offlineMeasurements = offlineMeasurements;
        this.averageTemperature = averageTemperature;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.averageVoltage = averageVoltage;
        this.averageCurrent = averageCurrent;
        this.averagePower = averagePower;
        this.averageHealthScore = averageHealthScore;
        this.minHealthScore = minHealthScore;
        this.maxHealthScore = maxHealthScore;
        this.firstMeasurementDate = firstMeasurementDate;
        this.lastMeasurementDate = lastMeasurementDate;
        this.transformateurCount = transformateurCount;
    }

    public Long getTotalMeasurements() { return totalMeasurements; }
    public void setTotalMeasurements(Long totalMeasurements) { this.totalMeasurements = totalMeasurements; }
    public Long getCriticalMeasurements() { return criticalMeasurements; }
    public void setCriticalMeasurements(Long criticalMeasurements) { this.criticalMeasurements = criticalMeasurements; }
    public Long getWarningMeasurements() { return warningMeasurements; }
    public void setWarningMeasurements(Long warningMeasurements) { this.warningMeasurements = warningMeasurements; }
    public Long getNormalMeasurements() { return normalMeasurements; }
    public void setNormalMeasurements(Long normalMeasurements) { this.normalMeasurements = normalMeasurements; }
    public Long getOfflineMeasurements() { return offlineMeasurements; }
    public void setOfflineMeasurements(Long offlineMeasurements) { this.offlineMeasurements = offlineMeasurements; }
    public Double getAverageTemperature() { return averageTemperature; }
    public void setAverageTemperature(Double averageTemperature) { this.averageTemperature = averageTemperature; }
    public Double getMinTemperature() { return minTemperature; }
    public void setMinTemperature(Double minTemperature) { this.minTemperature = minTemperature; }
    public Double getMaxTemperature() { return maxTemperature; }
    public void setMaxTemperature(Double maxTemperature) { this.maxTemperature = maxTemperature; }
    public Double getAverageVoltage() { return averageVoltage; }
    public void setAverageVoltage(Double averageVoltage) { this.averageVoltage = averageVoltage; }
    public Double getAverageCurrent() { return averageCurrent; }
    public void setAverageCurrent(Double averageCurrent) { this.averageCurrent = averageCurrent; }
    public Double getAveragePower() { return averagePower; }
    public void setAveragePower(Double averagePower) { this.averagePower = averagePower; }
    public Double getAverageHealthScore() { return averageHealthScore; }
    public void setAverageHealthScore(Double averageHealthScore) { this.averageHealthScore = averageHealthScore; }
    public Integer getMinHealthScore() { return minHealthScore; }
    public void setMinHealthScore(Integer minHealthScore) { this.minHealthScore = minHealthScore; }
    public Integer getMaxHealthScore() { return maxHealthScore; }
    public void setMaxHealthScore(Integer maxHealthScore) { this.maxHealthScore = maxHealthScore; }
    public LocalDateTime getFirstMeasurementDate() { return firstMeasurementDate; }
    public void setFirstMeasurementDate(LocalDateTime firstMeasurementDate) { this.firstMeasurementDate = firstMeasurementDate; }
    public LocalDateTime getLastMeasurementDate() { return lastMeasurementDate; }
    public void setLastMeasurementDate(LocalDateTime lastMeasurementDate) { this.lastMeasurementDate = lastMeasurementDate; }
    public Long getTransformateurCount() { return transformateurCount; }
    public void setTransformateurCount(Long transformateurCount) { this.transformateurCount = transformateurCount; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long totalMeasurements;
        private Long criticalMeasurements;
        private Long warningMeasurements;
        private Long normalMeasurements;
        private Long offlineMeasurements;
        private Double averageTemperature;
        private Double minTemperature;
        private Double maxTemperature;
        private Double averageVoltage;
        private Double averageCurrent;
        private Double averagePower;
        private Double averageHealthScore;
        private Integer minHealthScore;
        private Integer maxHealthScore;
        private LocalDateTime firstMeasurementDate;
        private LocalDateTime lastMeasurementDate;
        private Long transformateurCount;

        public Builder totalMeasurements(Long totalMeasurements) { this.totalMeasurements = totalMeasurements; return this; }
        public Builder criticalMeasurements(Long criticalMeasurements) { this.criticalMeasurements = criticalMeasurements; return this; }
        public Builder warningMeasurements(Long warningMeasurements) { this.warningMeasurements = warningMeasurements; return this; }
        public Builder normalMeasurements(Long normalMeasurements) { this.normalMeasurements = normalMeasurements; return this; }
        public Builder offlineMeasurements(Long offlineMeasurements) { this.offlineMeasurements = offlineMeasurements; return this; }
        public Builder averageTemperature(Double averageTemperature) { this.averageTemperature = averageTemperature; return this; }
        public Builder minTemperature(Double minTemperature) { this.minTemperature = minTemperature; return this; }
        public Builder maxTemperature(Double maxTemperature) { this.maxTemperature = maxTemperature; return this; }
        public Builder averageVoltage(Double averageVoltage) { this.averageVoltage = averageVoltage; return this; }
        public Builder averageCurrent(Double averageCurrent) { this.averageCurrent = averageCurrent; return this; }
        public Builder averagePower(Double averagePower) { this.averagePower = averagePower; return this; }
        public Builder averageHealthScore(Double averageHealthScore) { this.averageHealthScore = averageHealthScore; return this; }
        public Builder minHealthScore(Integer minHealthScore) { this.minHealthScore = minHealthScore; return this; }
        public Builder maxHealthScore(Integer maxHealthScore) { this.maxHealthScore = maxHealthScore; return this; }
        public Builder firstMeasurementDate(LocalDateTime firstMeasurementDate) { this.firstMeasurementDate = firstMeasurementDate; return this; }
        public Builder lastMeasurementDate(LocalDateTime lastMeasurementDate) { this.lastMeasurementDate = lastMeasurementDate; return this; }
        public Builder transformateurCount(Long transformateurCount) { this.transformateurCount = transformateurCount; return this; }

        public MeasurementStatisticsResponse build() {
            return new MeasurementStatisticsResponse(totalMeasurements, criticalMeasurements, warningMeasurements, normalMeasurements, offlineMeasurements, averageTemperature, minTemperature, maxTemperature, averageVoltage, averageCurrent, averagePower, averageHealthScore, minHealthScore, maxHealthScore, firstMeasurementDate, lastMeasurementDate, transformateurCount);
        }
    }
}
