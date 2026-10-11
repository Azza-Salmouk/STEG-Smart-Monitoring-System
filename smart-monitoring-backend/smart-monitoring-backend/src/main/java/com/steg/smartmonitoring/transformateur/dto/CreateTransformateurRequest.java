package com.steg.smartmonitoring.transformateur.dto;

import com.steg.smartmonitoring.transformateur.entity.RiskLevel;
import com.steg.smartmonitoring.transformateur.entity.StatusTransformateur;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

/**
 * DTO pour créer un nouveau transformateur
 */
public class CreateTransformateurRequest {

    @NotBlank(message = "La référence est requise")
    @Size(min = 3, max = 50, message = "La référence doit avoir entre 3 et 50 caractères")
    private String reference;

    @NotBlank(message = "Le numéro de série est requis")
    @Size(min = 3, max = 50, message = "Le numéro de série doit avoir entre 3 et 50 caractères")
    private String serialNumber;

    @NotBlank(message = "Le fabricant est requis")
    @Size(min = 2, max = 100, message = "Le fabricant doit avoir entre 2 et 100 caractères")
    private String manufacturer;

    @NotBlank(message = "Le modèle est requis")
    @Size(min = 2, max = 100, message = "Le modèle doit avoir entre 2 et 100 caractères")
    private String model;

    @NotNull(message = "La puissance est requise")
    @DecimalMin(value = "0.1", message = "La puissance doit être supérieure à 0.1 MVA")
    @DecimalMax(value = "1000", message = "La puissance ne doit pas dépasser 1000 MVA")
    private Double power;

    @NotNull(message = "La date d'installation est requise")
    @PastOrPresent(message = "La date d'installation ne peut pas être dans le futur")
    private LocalDate installationDate;

    @NotNull(message = "Le score de santé est requis")
    @Min(value = 0, message = "Le score de santé doit être entre 0 et 100")
    @Max(value = 100, message = "Le score de santé doit être entre 0 et 100")
    private Integer healthScore;

    @NotNull(message = "Le niveau de risque est requis")
    private RiskLevel riskLevel;

    @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères")
    private String description;

    private StatusTransformateur status;

    @NotNull(message = "L'ID du poste est requis")
    private Long posteId;

    public CreateTransformateurRequest() {
    }

    public CreateTransformateurRequest(String reference, String serialNumber, String manufacturer, String model, Double power, LocalDate installationDate, Integer healthScore, RiskLevel riskLevel, String description, StatusTransformateur status, Long posteId) {
        this.reference = reference;
        this.serialNumber = serialNumber;
        this.manufacturer = manufacturer;
        this.model = model;
        this.power = power;
        this.installationDate = installationDate;
        this.healthScore = healthScore;
        this.riskLevel = riskLevel;
        this.description = description;
        this.status = status;
        this.posteId = posteId;
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

    public StatusTransformateur getStatus() {
        return status;
    }

    public void setStatus(StatusTransformateur status) {
        this.status = status;
    }

    public Long getPosteId() {
        return posteId;
    }

    public void setPosteId(Long posteId) {
        this.posteId = posteId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String reference;
        private String serialNumber;
        private String manufacturer;
        private String model;
        private Double power;
        private LocalDate installationDate;
        private Integer healthScore;
        private RiskLevel riskLevel;
        private String description;
        private StatusTransformateur status;
        private Long posteId;

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

        public Builder status(StatusTransformateur status) {
            this.status = status;
            return this;
        }

        public Builder posteId(Long posteId) {
            this.posteId = posteId;
            return this;
        }

        public CreateTransformateurRequest build() {
            return new CreateTransformateurRequest(reference, serialNumber, manufacturer, model, power, installationDate, healthScore, riskLevel, description, status, posteId);
        }
    }
}
