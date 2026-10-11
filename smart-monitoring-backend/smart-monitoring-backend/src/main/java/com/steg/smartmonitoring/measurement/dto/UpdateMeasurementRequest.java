package com.steg.smartmonitoring.measurement.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

/**
 * DTO pour mettre à jour une mesure IoT existante
 * 
 * Tous les champs sont optionnels (pour PATCH).
 * Les champs calculés automatiquement ne peuvent pas être modifiés.
 */
public class UpdateMeasurementRequest {

    /**
     * Date et heure de la mesure (optionnel)
     */
    @PastOrPresent(message = "La date de mesure ne peut pas être dans le futur")
    private LocalDateTime measurementDate;

    /**
     * Température en degrés Celsius (optionnel)
     */
    @DecimalMin(value = "-50", message = "La température ne peut pas être inférieure à -50°C")
    @DecimalMax(value = "150", message = "La température ne peut pas dépasser 150°C")
    private Double temperature;

    /**
     * Tension en volts (optionnel)
     */
    @DecimalMin(value = "0", message = "La tension doit être positive")
    @DecimalMax(value = "500000", message = "La tension ne peut pas dépasser 500kV")
    private Double voltage;

    /**
     * Courant en ampères (optionnel)
     */
    @DecimalMin(value = "0", message = "Le courant doit être positif ou nul")
    @DecimalMax(value = "10000", message = "Le courant ne peut pas dépasser 10000A")
    private Double current;

    /**
     * Puissance en kilowatts (optionnel)
     */
    @DecimalMin(value = "0", message = "La puissance doit être positive ou nulle")
    private Double power;

    /**
     * Énergie consommée en kilowatt-heures (optionnel)
     */
    @DecimalMin(value = "0", message = "La consommation d'énergie doit être positive ou nulle")
    private Double energyConsumption;

    /**
     * Fréquence en hertz (optionnel)
     */
    @DecimalMin(value = "40", message = "La fréquence doit être entre 40Hz et 60Hz")
    @DecimalMax(value = "60", message = "La fréquence doit être entre 40Hz et 60Hz")
    private Double frequency;

    /**
     * Humidité relative en pourcentage (optionnel)
     */
    @DecimalMin(value = "0", message = "L'humidité doit être entre 0% et 100%")
    @DecimalMax(value = "100", message = "L'humidité doit être entre 0% et 100%")
    private Double humidity;

    public UpdateMeasurementRequest() {
    }

    public UpdateMeasurementRequest(LocalDateTime measurementDate, Double temperature, Double voltage, Double current, Double power, Double energyConsumption, Double frequency, Double humidity) {
        this.measurementDate = measurementDate;
        this.temperature = temperature;
        this.voltage = voltage;
        this.current = current;
        this.power = power;
        this.energyConsumption = energyConsumption;
        this.frequency = frequency;
        this.humidity = humidity;
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
        private LocalDateTime measurementDate;
        private Double temperature;
        private Double voltage;
        private Double current;
        private Double power;
        private Double energyConsumption;
        private Double frequency;
        private Double humidity;

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

        public UpdateMeasurementRequest build() {
            return new UpdateMeasurementRequest(measurementDate, temperature, voltage, current, power, energyConsumption, frequency, humidity);
        }
    }
}
