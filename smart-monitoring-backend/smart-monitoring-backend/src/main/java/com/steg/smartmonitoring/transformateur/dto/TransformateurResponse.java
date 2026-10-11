package com.steg.smartmonitoring.transformateur.dto;

import com.steg.smartmonitoring.transformateur.entity.RiskLevel;
import com.steg.smartmonitoring.transformateur.entity.StatusTransformateur;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO pour retourner les informations d'un transformateur
 */
public class TransformateurResponse {

    private Long id;
    private String reference;
    private String serialNumber;
    private String manufacturer;
    private String model;
    private Double power;
    private LocalDate installationDate;
    private StatusTransformateur status;
    private Integer healthScore;
    private RiskLevel riskLevel;
    private String description;
    private Long posteId;
    private String posteNom;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public TransformateurResponse() {
    }

    public TransformateurResponse(Long id, String reference, String serialNumber, String manufacturer, String model, Double power, LocalDate installationDate, StatusTransformateur status, Integer healthScore, RiskLevel riskLevel, String description, Long posteId, String posteNom, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.reference = reference;
        this.serialNumber = serialNumber;
        this.manufacturer = manufacturer;
        this.model = model;
        this.power = power;
        this.installationDate = installationDate;
        this.status = status;
        this.healthScore = healthScore;
        this.riskLevel = riskLevel;
        this.description = description;
        this.posteId = posteId;
        this.posteNom = posteNom;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

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

    public Long getPosteId() {
        return posteId;
    }

    public void setPosteId(Long posteId) {
        this.posteId = posteId;
    }

    public String getPosteNom() {
        return posteNom;
    }

    public void setPosteNom(String posteNom) {
        this.posteNom = posteNom;
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
        private String reference;
        private String serialNumber;
        private String manufacturer;
        private String model;
        private Double power;
        private LocalDate installationDate;
        private StatusTransformateur status;
        private Integer healthScore;
        private RiskLevel riskLevel;
        private String description;
        private Long posteId;
        private String posteNom;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

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

        public Builder posteId(Long posteId) {
            this.posteId = posteId;
            return this;
        }

        public Builder posteNom(String posteNom) {
            this.posteNom = posteNom;
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

        public TransformateurResponse build() {
            return new TransformateurResponse(id, reference, serialNumber, manufacturer, model, power, installationDate, status, healthScore, riskLevel, description, posteId, posteNom, createdAt, updatedAt);
        }
    }
}
