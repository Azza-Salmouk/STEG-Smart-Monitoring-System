package com.steg.smartmonitoring.alert.dto;

import com.steg.smartmonitoring.alert.entity.AlertLevel;
import com.steg.smartmonitoring.alert.entity.AlertStatus;
import jakarta.validation.constraints.Size;

/**
 * DTO pour la mise à jour d'une alerte
 */
public class UpdateAlertRequest {

    @Size(min = 5, max = 255, message = "Le titre doit faire entre 5 et 255 caractères")
    private String titre;

    @Size(min = 10, max = 2000, message = "La description doit faire entre 10 et 2000 caractères")
    private String description;

    private AlertLevel niveau;

    private AlertStatus statut;

    private Boolean isAcknowledged;

    private Boolean isArchived;

    private Long resolvedByUserId;

    public UpdateAlertRequest() {
    }

    public UpdateAlertRequest(String titre, String description, AlertLevel niveau, AlertStatus statut,
                             Boolean isAcknowledged, Boolean isArchived, Long resolvedByUserId) {
        this.titre = titre;
        this.description = description;
        this.niveau = niveau;
        this.statut = statut;
        this.isAcknowledged = isAcknowledged;
        this.isArchived = isArchived;
        this.resolvedByUserId = resolvedByUserId;
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

    public Long getResolvedByUserId() {
        return resolvedByUserId;
    }

    public void setResolvedByUserId(Long resolvedByUserId) {
        this.resolvedByUserId = resolvedByUserId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String titre;
        private String description;
        private AlertLevel niveau;
        private AlertStatus statut;
        private Boolean isAcknowledged;
        private Boolean isArchived;
        private Long resolvedByUserId;

        public Builder titre(String titre) {
            this.titre = titre;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
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

        public Builder isAcknowledged(Boolean isAcknowledged) {
            this.isAcknowledged = isAcknowledged;
            return this;
        }

        public Builder isArchived(Boolean isArchived) {
            this.isArchived = isArchived;
            return this;
        }

        public Builder resolvedByUserId(Long resolvedByUserId) {
            this.resolvedByUserId = resolvedByUserId;
            return this;
        }

        public UpdateAlertRequest build() {
            return new UpdateAlertRequest(titre, description, niveau, statut, isAcknowledged, 
                                         isArchived, resolvedByUserId);
        }
    }
}
