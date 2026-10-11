package com.steg.smartmonitoring.alert.dto;

import com.steg.smartmonitoring.alert.entity.AlertLevel;
import com.steg.smartmonitoring.alert.entity.AlertStatus;
import com.steg.smartmonitoring.alert.entity.AlertType;

import java.time.LocalDateTime;

/**
 * DTO de réponse pour une alerte
 */
public class AlertResponse {

    private Long id;

    private String reference;

    private String titre;

    private String description;

    private AlertType type;

    private AlertLevel niveau;

    private AlertStatus statut;

    private Long measurementId;

    private Long transformateurId;
    private String transformateurName;

    private Long posteId;
    private String posteName;

    private Long resolvedByUserId;
    private String resolvedByUsername;

    private LocalDateTime createdAt;

    private LocalDateTime resolvedAt;

    private Boolean isAcknowledged;

    private Boolean isArchived;

    private LocalDateTime updatedAt;

    // Calculated field
    private Long resolutionTimeMinutes;

    public AlertResponse() {
    }

    public AlertResponse(Long id, String reference, String titre, String description, AlertType type, AlertLevel niveau, AlertStatus statut, Long measurementId, Long transformateurId, String transformateurName, Long posteId, String posteName, Long resolvedByUserId, String resolvedByUsername, LocalDateTime createdAt, LocalDateTime resolvedAt, Boolean isAcknowledged, Boolean isArchived, LocalDateTime updatedAt, Long resolutionTimeMinutes) {
        this.id = id;
        this.reference = reference;
        this.titre = titre;
        this.description = description;
        this.type = type;
        this.niveau = niveau;
        this.statut = statut;
        this.measurementId = measurementId;
        this.transformateurId = transformateurId;
        this.transformateurName = transformateurName;
        this.posteId = posteId;
        this.posteName = posteName;
        this.resolvedByUserId = resolvedByUserId;
        this.resolvedByUsername = resolvedByUsername;
        this.createdAt = createdAt;
        this.resolvedAt = resolvedAt;
        this.isAcknowledged = isAcknowledged;
        this.isArchived = isArchived;
        this.updatedAt = updatedAt;
        this.resolutionTimeMinutes = resolutionTimeMinutes;
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

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AlertType getType() {
        return type;
    }

    public void setType(AlertType type) {
        this.type = type;
    }

    public AlertLevel getNiveau() {
        return niveau;
    }

    public void setNiveau(AlertLevel niveau) {
        this.niveau = niveau;
    }

    public AlertStatus getStatut() {
        return statut;
    }

    public void setStatut(AlertStatus statut) {
        this.statut = statut;
    }

    public Long getMeasurementId() {
        return measurementId;
    }

    public void setMeasurementId(Long measurementId) {
        this.measurementId = measurementId;
    }

    public Long getTransformateurId() {
        return transformateurId;
    }

    public void setTransformateurId(Long transformateurId) {
        this.transformateurId = transformateurId;
    }

    public String getTransformateurName() {
        return transformateurName;
    }

    public void setTransformateurName(String transformateurName) {
        this.transformateurName = transformateurName;
    }

    public Long getPosteId() {
        return posteId;
    }

    public void setPosteId(Long posteId) {
        this.posteId = posteId;
    }

    public String getPosteName() {
        return posteName;
    }

    public void setPosteName(String posteName) {
        this.posteName = posteName;
    }

    public Long getResolvedByUserId() {
        return resolvedByUserId;
    }

    public void setResolvedByUserId(Long resolvedByUserId) {
        this.resolvedByUserId = resolvedByUserId;
    }

    public String getResolvedByUsername() {
        return resolvedByUsername;
    }

    public void setResolvedByUsername(String resolvedByUsername) {
        this.resolvedByUsername = resolvedByUsername;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public Boolean getIsAcknowledged() {
        return isAcknowledged;
    }

    public void setIsAcknowledged(Boolean isAcknowledged) {
        this.isAcknowledged = isAcknowledged;
    }

    public Boolean getIsArchived() {
        return isArchived;
    }

    public void setIsArchived(Boolean isArchived) {
        this.isArchived = isArchived;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getResolutionTimeMinutes() {
        return resolutionTimeMinutes;
    }

    public void setResolutionTimeMinutes(Long resolutionTimeMinutes) {
        this.resolutionTimeMinutes = resolutionTimeMinutes;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String reference;
        private String titre;
        private String description;
        private AlertType type;
        private AlertLevel niveau;
        private AlertStatus statut;
        private Long measurementId;
        private Long transformateurId;
        private String transformateurName;
        private Long posteId;
        private String posteName;
        private Long resolvedByUserId;
        private String resolvedByUsername;
        private LocalDateTime createdAt;
        private LocalDateTime resolvedAt;
        private Boolean isAcknowledged;
        private Boolean isArchived;
        private LocalDateTime updatedAt;
        private Long resolutionTimeMinutes;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder reference(String reference) {
            this.reference = reference;
            return this;
        }

        public Builder titre(String titre) {
            this.titre = titre;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder type(AlertType type) {
            this.type = type;
            return this;
        }

        public Builder niveau(AlertLevel niveau) {
            this.niveau = niveau;
            return this;
        }

        public Builder statut(AlertStatus statut) {
            this.statut = statut;
            return this;
        }

        public Builder measurementId(Long measurementId) {
            this.measurementId = measurementId;
            return this;
        }

        public Builder transformateurId(Long transformateurId) {
            this.transformateurId = transformateurId;
            return this;
        }

        public Builder transformateurName(String transformateurName) {
            this.transformateurName = transformateurName;
            return this;
        }

        public Builder posteId(Long posteId) {
            this.posteId = posteId;
            return this;
        }

        public Builder posteName(String posteName) {
            this.posteName = posteName;
            return this;
        }

        public Builder resolvedByUserId(Long resolvedByUserId) {
            this.resolvedByUserId = resolvedByUserId;
            return this;
        }

        public Builder resolvedByUsername(String resolvedByUsername) {
            this.resolvedByUsername = resolvedByUsername;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder resolvedAt(LocalDateTime resolvedAt) {
            this.resolvedAt = resolvedAt;
            return this;
        }

        public Builder isAcknowledged(Boolean isAcknowledged) {
            this.isAcknowledged = isAcknowledged;
            return this;
        }

        public Builder isArchived(Boolean isArchived) {
            this.isArchived = isArchived;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder resolutionTimeMinutes(Long resolutionTimeMinutes) {
            this.resolutionTimeMinutes = resolutionTimeMinutes;
            return this;
        }

        public AlertResponse build() {
            return new AlertResponse(id, reference, titre, description, type, niveau, statut, measurementId, transformateurId, transformateurName, posteId, posteName, resolvedByUserId, resolvedByUsername, createdAt, resolvedAt, isAcknowledged, isArchived, updatedAt, resolutionTimeMinutes);
        }
    }
}
