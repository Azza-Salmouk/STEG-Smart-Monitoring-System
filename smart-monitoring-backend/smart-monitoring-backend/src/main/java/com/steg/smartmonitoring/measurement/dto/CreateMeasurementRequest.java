package com.steg.smartmonitoring.measurement.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

/**
 * DTO pour créer une nouvelle mesure IoT
 * 
 * Les champs calculés automatiquement (healthScore, status)
 * ne doivent pas être fournis par le client.
 * Ils seront calculés par le backend.
 */
public class CreateMeasurementRequest {

    /**
     * ID du transformateur auquel appartient cette mesure
     */
    @NotNull(message = "L'ID du transformateur est requis")
    @Positive(message = "L'ID du transformateur doit être positif")
    private Long transformateurId;

    /**
     * Date et heure de la mesure
     */
    @NotNull(message = "La date de mesure est requise")
    @PastOrPresent(message = "La date de mesure ne peut pas être dans le futur")
    private LocalDateTime measurementDate;

    /**
     * Température en degrés Celsius
     */
    @NotNull(message = "La température est requise")
    @DecimalMin(value = "-50", message = "La température ne peut pas être inférieure à -50°C")
    @DecimalMax(value = "150", message = "La température ne peut pas dépasser 150°C")
    private Double temperature;

    /**
     * Tension en volts
     */
    @NotNull(message = "La tension est requise")
    @DecimalMin(value = "0", message = "La tension doit être positive")
    @DecimalMax(value = "500000", message = "La tension ne peut pas dépasser 500kV")
    private Double voltage;

    /**
     * Courant en ampères
     */
    @NotNull(message = "Le courant est requis")
    @DecimalMin(value = "0", message = "Le courant doit être positif ou nul")
    @DecimalMax(value = "10000", message = "Le courant ne peut pas dépasser 10000A")
    private Double current;

    /**
     * Puissance en kilowatts
     */
    @NotNull(message = "La puissance est requise")
    @DecimalMin(value = "0", message = "La puissance doit être positive ou nulle")
    private Double power;

    /**
     * Énergie consommée en kilowatt-heures
     */
    @NotNull(message = "La consommation d'énergie est requise")
    @DecimalMin(value = "0", message = "La consommation d'énergie doit être positive ou nulle")
    private Double energyConsumption;

    /**
     * Fréquence en hertz
     */
    @NotNull(message = "La fréquence est requise")
    @DecimalMin(value = "40", message = "La fréquence doit être entre 40Hz et 60Hz")
    @DecimalMax(value = "60", message = "La fréquence doit être entre 40Hz et 60Hz")
    private Double frequency;

    /**
     * Humidité relative en pourcentage (optionnel)
     */
    @DecimalMin(value = "0", message = "L'humidité doit être entre 0% et 100%")
    @DecimalMax(value = "100", message = "L'humidité doit être entre 0% et 100%")
    private Double humidity;

    public CreateMeasurementRequest() {
    }

    public CreateMeasurementRequest(Long transformateurId, LocalDateTime measurementDate, Double temperature, Double voltage, Double current, Double power, Double energyConsumption, Double frequency, Double humidity) {
        this.transformateurId = transformateurId;
        this.measurementDate = measurementDate;
        this.temperature = temperature;
        this.voltage = voltage;
        this.current = current;
        this.power = power;
        this.energyConsumption = energyConsumption;
        this.frequency = frequency;
        this.humidity = humidity;
    }

    public Long getTransformateurId() {
        return transformateurId;
    }

    public void setTransformateurId(Long transformateurId) {
        this.transformateurId = transformateurId;
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long transformateurId;
        private LocalDateTime measurementDate;
        private Double temperature;
        private Double voltage;
        private Double current;
        private Double power;
        private Double energyConsumption;
        private Double frequency;
        private Double humidity;

        public Builder transformateurId(Long transformateurId) {
            this.transformateurId = transformateurId;
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

        public CreateMeasurementRequest build() {
            return new CreateMeasurementRequest(transformateurId, measurementDate, temperature, voltage, current, power, energyConsumption, frequency, humidity);
        }
    }
}
