package com.steg.smartmonitoring.complaint.dto;

import com.steg.smartmonitoring.complaint.entity.ComplaintPriority;
import com.steg.smartmonitoring.complaint.entity.ComplaintStatus;
import jakarta.validation.constraints.Size;

/**
 * DTO pour la mise à jour d'une réclamation
 */
public class UpdateComplaintRequest {

    private ComplaintStatus statut;

    private ComplaintPriority priorite;

    private Long assignedSupervisorId;

    @Size(max = 2000, message = "Les commentaires ne doivent pas dépasser 2000 caractères")
    private String commentaires;

    private String telephone;

    private String adresse;

    private String description;

    public UpdateComplaintRequest() {
    }

    public UpdateComplaintRequest(ComplaintStatus statut, ComplaintPriority priorite, Long assignedSupervisorId, String commentaires, String telephone, String adresse, String description) {
        this.statut = statut;
        this.priorite = priorite;
        this.assignedSupervisorId = assignedSupervisorId;
        this.commentaires = commentaires;
        this.telephone = telephone;
        this.adresse = adresse;
        this.description = description;
    }

    public ComplaintStatus getStatut() {
        return statut;
    }

    public void setStatut(ComplaintStatus statut) {
        this.statut = statut;
    }

    public ComplaintPriority getPriorite() {
        return priorite;
    }

    public void setPriorite(ComplaintPriority priorite) {
        this.priorite = priorite;
    }

    public Long getAssignedSupervisorId() {
        return assignedSupervisorId;
    }

    public void setAssignedSupervisorId(Long assignedSupervisorId) {
        this.assignedSupervisorId = assignedSupervisorId;
    }

    public String getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(String commentaires) {
        this.commentaires = commentaires;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
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
        private ComplaintStatus statut;
        private ComplaintPriority priorite;
        private Long assignedSupervisorId;
        private String commentaires;
        private String telephone;
        private String adresse;
        private String description;

        public Builder statut(ComplaintStatus statut) {
            this.statut = statut;
            return this;
        }

        public Builder priorite(ComplaintPriority priorite) {
            this.priorite = priorite;
            return this;
        }

        public Builder assignedSupervisorId(Long assignedSupervisorId) {
            this.assignedSupervisorId = assignedSupervisorId;
            return this;
        }

        public Builder commentaires(String commentaires) {
            this.commentaires = commentaires;
            return this;
        }

        public Builder telephone(String telephone) {
            this.telephone = telephone;
            return this;
        }

        public Builder adresse(String adresse) {
            this.adresse = adresse;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public UpdateComplaintRequest build() {
            return new UpdateComplaintRequest(statut, priorite, assignedSupervisorId, commentaires, telephone, adresse, description);
        }
    }
}
