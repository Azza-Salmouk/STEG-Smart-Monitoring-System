package com.steg.smartmonitoring.transformateur.entity;

import com.steg.smartmonitoring.poste.entity.Poste;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entité représentant un transformateur électrique
 * Un transformateur appartient à un seul poste
 */
@Entity
@Table(name = "transformateurs", uniqueConstraints = {
    @UniqueConstraint(columnNames = "reference", name = "uk_transformateur_reference")
})
public class Transformateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "La référence est requise")
    @Column(nullable = false, unique = true, length = 50)
    private String reference;

    @NotBlank(message = "Le numéro de série est requis")
    @Column(nullable = false, length = 50)
    private String serialNumber;

    @NotBlank(message = "Le fabricant est requis")
    @Column(nullable = false, length = 100)
    private String manufacturer;

    @NotBlank(message = "Le modèle est requis")
    @Column(nullable = false, length = 100)
    private String model;

    @NotNull(message = "La puissance est requise")
    @Column(nullable = false)
    private Double power; // en MVA

    @NotNull(message = "La date d'installation est requise")
    @Column(nullable = false)
    private LocalDate installationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusTransformateur status = StatusTransformateur.ACTIF;

    @NotNull(message = "Le score de santé est requis")
    @DecimalMin(value = "0", message = "Le score de santé doit être entre 0 et 100")
    @DecimalMax(value = "100", message = "Le score de santé doit être entre 0 et 100")
    @Column(nullable = false)
    private Integer healthScore;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RiskLevel riskLevel = RiskLevel.LOW;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Relation Many-to-One avec Poste
     * Un transformateur appartient obligatoirement à un seul poste
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
        name = "poste_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_transformateur_poste")
    )
    @NotNull(message = "Un transformateur doit être assigné à un poste")
    private Poste poste;

    // No-arg constructor
    public Transformateur() {
        this.status = StatusTransformateur.ACTIF;
        this.riskLevel = RiskLevel.LOW;
    }

    // All-args constructor
    public Transformateur(Long id, String reference, String serialNumber, String manufacturer, String model, Double power, LocalDate installationDate, StatusTransformateur status, Integer healthScore, RiskLevel riskLevel, String description, LocalDateTime createdAt, LocalDateTime updatedAt, Poste poste) {
        this.id = id;
        this.reference = reference;
        this.serialNumber = serialNumber;
        this.manufacturer = manufacturer;
        this.model = model;
        this.power = power;
        this.installationDate = installationDate;
        this.status = status != null ? status : StatusTransformateur.ACTIF;
        this.healthScore = healthScore;
        this.riskLevel = riskLevel != null ? riskLevel : RiskLevel.LOW;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.poste = poste;
    }

    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String reference;
        private String serialNumber;
        private String manufacturer;
        private String model;
        private Double power;
        private LocalDate installationDate;
        private StatusTransformateur status = StatusTransformateur.ACTIF;
        private Integer healthScore;
        private RiskLevel riskLevel = RiskLevel.LOW;
        private String description;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Poste poste;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder reference(String reference) {
            this.reference = reference;
            return this;
        }

        public Builder serialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
            return this;
        }

        public Builder manufacturer(String manufacturer) {
            this.manufacturer = manufacturer;
            return this;
        }

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public Builder power(Double power) {
            this.power = power;
            return this;
        }

        public Builder installationDate(LocalDate installationDate) {
            this.installationDate = installationDate;
            return this;
        }

        public Builder status(StatusTransformateur status) {
            this.status = status;
            return this;
        }

        public Builder healthScore(Integer healthScore) {
            this.healthScore = healthScore;
            return this;
        }

        public Builder riskLevel(RiskLevel riskLevel) {
            this.riskLevel = riskLevel;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
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

        public Builder poste(Poste poste) {
            this.poste = poste;
            return this;
        }

        public Transformateur build() {
            return new Transformateur(id, reference, serialNumber, manufacturer, model, power, installationDate, status, healthScore, riskLevel, description, createdAt, updatedAt, poste);
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Double getPower() {
        return power;
    }

    public void setPower(Double power) {
        this.power = power;
    }

    public LocalDate getInstallationDate() {
        return installationDate;
    }

    public void setInstallationDate(LocalDate installationDate) {
        this.installationDate = installationDate;
    }

    public StatusTransformateur getStatus() {
        return status;
    }

    public void setStatus(StatusTransformateur status) {
        this.status = status;
    }

    public Integer getHealthScore() {
        return healthScore;
    }

    public void setHealthScore(Integer healthScore) {
        this.healthScore = healthScore;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Poste getPoste() {
        return poste;
    }

    public void setPoste(Poste poste) {
        this.poste = poste;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
