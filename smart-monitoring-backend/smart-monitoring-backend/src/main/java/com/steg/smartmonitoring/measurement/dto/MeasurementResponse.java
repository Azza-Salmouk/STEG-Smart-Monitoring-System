package com.steg.smartmonitoring.measurement.dto;

import com.steg.smartmonitoring.measurement.entity.MeasurementStatus;

import java.time.LocalDateTime;

/**
 * DTO pour retourner les informations d'une mesure IoT
 * 
 * Contient toutes les données de la mesure incluant les valeurs
 * calculées automatiquement (healthScore, status).
 */
public class MeasurementResponse {

    /**
     * ID unique de la mesure
     */
    private Long id;

    /**
     * ID du transformateur
     */
    private Long transformateurId;

    /**
     * Référence du transformateur (pour affichage)
     */
    private String transformateurReference;

    /**
     * Date et heure de la mesure
     */
    private LocalDateTime measurementDate;

    /**
     * Température en degrés Celsius
     */
    private Double temperature;

    /**
     * Tension en volts
     */
    private Double voltage;

    /**
     * Courant en ampères
     */
    private Double current;

    /**
     * Puissance en kilowatts
     */
    private Double power;

    /**
     * Énergie consommée en kilowatt-heures
     */
    private Double energyConsumption;

    /**
     * Fréquence en hertz
     */
    private Double frequency;

    /**
     * Humidité relative en pourcentage (optionnel)
     */
    private Double humidity;

    /**
     * Health score calculé automatiquement (0-100)
     */
    private Integer healthScore;

    /**
     * Statut déterminé automatiquement
     */
    private MeasurementStatus status;

    /**
     * Date de création de la mesure
     */
    private LocalDateTime createdAt;

    /**
     * Date de dernière modification
     */
    private LocalDateTime updatedAt;

    public MeasurementResponse() {
    }

    public MeasurementResponse(Long id, Long transformateurId, String transformateurReference, LocalDateTime measurementDate, Double temperature, Double voltage, Double current, Double power, Double energyConsumption, Double frequency, Double humidity, Integer healthScore, MeasurementStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.transformateurId = transformateurId;
        this.transformateurReference = transformateurReference;
        this.measurementDate = measurementDate;
        this.temperature = temperature;
        this.voltage = voltage;
        this.current = current;
        this.power = power;
        this.energyConsumption = energyConsumption;
        this.frequency = frequency;
        this.humidity = humidity;
        this.healthScore = healthScore;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTransformateurId() {
        return transformateurId;
    }

    public void setTransformateurId(Long transformateurId) {
        this.transformateurId = transformateurId;
    }

    public String getTransformateurReference() {
        return transformateurReference;
    }

    public void setTransformateurReference(String transformateurReference) {
        this.transformateurReference = transformateurReference;
    }

    public LocalDateTime getMeasurementDate() {
        return measurementDate;
    }

    public void setMeasurementDate(LocalDateTime measurementDate) {
        this.measurementDate = measurementDate;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getVoltage() {
        return voltage;
    }

    public void setVoltage(Double voltage) {
        this.voltage = voltage;
    }

    public Double getCurrent() {
        return current;
    }

    public void setCurrent(Double current) {
        this.current = current;
    }

    public Double getPower() {
        return power;
    }

    public void setPower(Double power) {
        this.power = power;
    }

    public Double getEnergyConsumption() {
        return energyConsumption;
    }

    public void setEnergyConsumption(Double energyConsumption) {
        this.energyConsumption = energyConsumption;
    }

    public Double getFrequency() {
        return frequency;
    }

    public void setFrequency(Double frequency) {
        this.frequency = frequency;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Integer getHealthScore() {
        return healthScore;
    }

    public void setHealthScore(Integer healthScore) {
        this.healthScore = healthScore;
    }

    public MeasurementStatus getStatus() {
        return status;
    }

    public void setStatus(MeasurementStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private Long transformateurId;
        private String transformateurReference;
        private LocalDateTime measurementDate;
        private Double temperature;
        private Double voltage;
        private Double current;
        private Double power;
        private Double energyConsumption;
        private Double frequency;
        private Double humidity;
        private Integer healthScore;
        private MeasurementStatus status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder transformateurId(Long transformateurId) {
            this.transformateurId = transformateurId;
            return this;
        }

        public Builder transformateurReference(String transformateurReference) {
            this.transformateurReference = transformateurReference;
            return this;
        }

        public Builder measurementDate(LocalDateTime measurementDate) {
            this.measurementDate = measurementDate;
            return this;
        }

        public Builder temperature(Double temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder voltage(Double voltage) {
            this.voltage = voltage;
            return this;
        }

        public Builder current(Double current) {
            this.current = current;
            return this;
        }

        public Builder power(Double power) {
            this.power = power;
            return this;
        }

        public Builder energyConsumption(Double energyConsumption) {
            this.energyConsumption = energyConsumption;
            return this;
        }

        public Builder frequency(Double frequency) {
            this.frequency = frequency;
            return this;
        }

        public Builder humidity(Double humidity) {
            this.humidity = humidity;
            return this;
        }

        public Builder healthScore(Integer healthScore) {
            this.healthScore = healthScore;
            return this;
        }

        public Builder status(MeasurementStatus status) {
            this.status = status;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public MeasurementResponse build() {
            return new MeasurementResponse(id, transformateurId, transformateurReference, measurementDate, temperature, voltage, current, power, energyConsumption, frequency, humidity, healthScore, status, createdAt, updatedAt);
        }
    }
}
