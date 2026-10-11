package com.steg.smartmonitoring.transformateur.dto;

import com.steg.smartmonitoring.transformateur.entity.RiskLevel;
import com.steg.smartmonitoring.transformateur.entity.StatusTransformateur;
import jakarta.validation.constraints.*;

/**
 * DTO pour mettre à jour un transformateur
 */
public class UpdateTransformateurRequest {

    @Size(min = 2, max = 100, message = "Le fabricant doit avoir entre 2 et 100 caractères")
    private String manufacturer;

    @Size(min = 2, max = 100, message = "Le modèle doit avoir entre 2 et 100 caractères")
    private String model;

    @DecimalMin(value = "0.1", message = "La puissance doit être supérieure à 0.1 MVA")
    @DecimalMax(value = "1000", message = "La puissance ne doit pas dépasser 1000 MVA")
    private Double power;

    private StatusTransformateur status;

    @Min(value = 0, message = "Le score de santé doit être entre 0 et 100")
    @Max(value = 100, message = "Le score de santé doit être entre 0 et 100")
    private Integer healthScore;

    private RiskLevel riskLevel;

    @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères")
    private String description;

    public UpdateTransformateurRequest() {
    }

    public UpdateTransformateurRequest(String manufacturer, String model, Double power, StatusTransformateur status, Integer healthScore, RiskLevel riskLevel, String description) {
        this.manufacturer = manufacturer;
        this.model = model;
        this.power = power;
        this.status = status;
        this.healthScore = healthScore;
        this.riskLevel = riskLevel;
        this.description = description;
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String manufacturer;
        private String model;
        private Double power;
        private StatusTransformateur status;
        private Integer healthScore;
        private RiskLevel riskLevel;
        private String description;

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

        public UpdateTransformateurRequest build() {
            return new UpdateTransformateurRequest(manufacturer, model, power, status, healthScore, riskLevel, description);
        }
    }
}
