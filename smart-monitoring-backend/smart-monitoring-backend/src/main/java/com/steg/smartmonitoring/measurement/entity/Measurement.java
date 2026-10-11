package com.steg.smartmonitoring.measurement.entity;

import com.steg.smartmonitoring.transformateur.entity.Transformateur;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * Entité représentant une mesure IoT d'un transformateur
 * 
 * Une mesure contient :
 * - Données de température, tension, courant
 * - Puissance et énergie consommée
 * - Fréquence et humidité
 * - Health score calculé automatiquement
 * - Statut déterminé automatiquement
 */
@Entity
@Table(name = "measurements", indexes = {
    @Index(name = "idx_transformateur_id", columnList = "transformateur_id"),
    @Index(name = "idx_measurement_date", columnList = "measurement_date"),
    @Index(name = "idx_status", columnList = "status"),
    @Index(name = "idx_health_score", columnList = "health_score"),
    @Index(name = "idx_temperature", columnList = "temperature")
})
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Relation Many-to-One avec Transformateur
     * Une mesure appartient toujours à un transformateur
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
        name = "transformateur_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_measurement_transformateur")
    )
    @NotNull(message = "Un transformateur est requis")
    private Transformateur transformateur;

    /**
     * Date et heure de la mesure
     * Timestamp de quand la mesure a été prise
     */
    @Column(nullable = false)
    @NotNull(message = "La date de mesure est requise")
    private LocalDateTime measurementDate;

    /**
     * Température en degrés Celsius
     * Plage normale: -50°C à 100°C
     */
    @Column(nullable = false)
    @NotNull(message = "La température est requise")
    private Double temperature;

    /**
     * Tension en volts (V)
     * Valeur RMS pour AC
     * Plage normale: 0V à 500kV
     */
    @Column(nullable = false)
    @NotNull(message = "La tension est requise")
    private Double voltage;

    /**
     * Courant en ampères (A)
     * Valeur RMS pour AC
     * Plage normale: 0A à 10000A
     */
    @Column(nullable = false)
    @NotNull(message = "Le courant est requis")
    private Double current;

    /**
     * Puissance en kilowatts (kW)
     * Puissance active consommée
     */
    @Column(nullable = false)
    @NotNull(message = "La puissance est requise")
    private Double power;

    /**
     * Énergie consommée en kilowatt-heures (kWh)
     * Total cumulatif
     */
    @Column(nullable = false)
    @NotNull(message = "La consommation d'énergie est requise")
    private Double energyConsumption;

    /**
     * Fréquence en hertz (Hz)
     * Fréquence du réseau électrique
     * Plage normale: 48Hz à 52Hz (pour 50Hz nominal)
     */
    @Column(nullable = false)
    @NotNull(message = "La fréquence est requise")
    private Double frequency;

    /**
     * Humidité relative en pourcentage (%)
     * Optionnelle - peut être NULL
     * Plage: 0% à 100%
     */
    @Column
    private Double humidity;

    /**
     * Score de santé du transformateur (0-100)
     * Calculé automatiquement par HealthScoreCalculator
     * 100 = Excellent
     * 75+ = Bon
     * 50-75 = Moyen
     * < 50 = Dégradé
     */
    @Column(nullable = false)
    @NotNull(message = "Le score de santé est requis")
    private Integer healthScore;

    /**
     * Statut déterminé automatiquement
     * NORMAL, WARNING, CRITICAL, OFFLINE
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Le statut est requis")
    private MeasurementStatus status;

    /**
     * Timestamp de création
     * Immutable après création
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp de dernière modification
     */
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // No-arg constructor
    public Measurement() {
    }

    // All-args constructor
    public Measurement(Long id, Transformateur transformateur, LocalDateTime measurementDate, Double temperature, Double voltage, Double current, Double power, Double energyConsumption, Double frequency, Double humidity, Integer healthScore, MeasurementStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.transformateur = transformateur;
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

    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private Transformateur transformateur;
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

        public Builder transformateur(Transformateur transformateur) {
            this.transformateur = transformateur;
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

        public Measurement build() {
            return new Measurement(id, transformateur, measurementDate, temperature, voltage, current, power, energyConsumption, frequency, humidity, healthScore, status, createdAt, updatedAt);
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Transformateur getTransformateur() {
        return transformateur;
    }

    public void setTransformateur(Transformateur transformateur) {
        this.transformateur = transformateur;
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

    /**
     * Hook JPA appelé avant la création
     * Initialise les timestamps
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * Hook JPA appelé avant une mise à jour
     * Met à jour le timestamp de modification
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
