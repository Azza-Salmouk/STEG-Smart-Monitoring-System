package com.steg.smartmonitoring.complaint.dto;

import com.steg.smartmonitoring.complaint.entity.ComplaintCategory;
import com.steg.smartmonitoring.complaint.entity.ComplaintPriority;
import com.steg.smartmonitoring.complaint.entity.ComplaintStatus;

import java.time.LocalDateTime;

/**
 * DTO pour la réponse d'une réclamation
 */
public class ComplaintResponse {

    private Long id;

    private String reference;

    private String nomClient;

    private String telephone;

    private String adresse;

    private ComplaintCategory categorie;

    private String description;

    private ComplaintPriority priorite;

    private ComplaintStatus statut;

    private Long assignedSupervisorId;

    private String assignedSupervisorName;

    private Long posteId;

    private String posteNom;

    private Long transformateurId;

    private String transformateurReference;

    private String commentaires;

    private LocalDateTime createdAt;

    private LocalDateTime dateResolution;

    private LocalDateTime updatedAt;

    private Long resolutionTimeHours;

    public ComplaintResponse() {
    }

    public ComplaintResponse(Long id, String reference, String nomClient, String telephone, String adresse, ComplaintCategory categorie, String description, ComplaintPriority priorite, ComplaintStatus statut, Long assignedSupervisorId, String assignedSupervisorName, Long posteId, String posteNom, Long transformateurId, String transformateurReference, String commentaires, LocalDateTime createdAt, LocalDateTime dateResolution, LocalDateTime updatedAt, Long resolutionTimeHours) {
        this.id = id;
        this.reference = reference;
        this.nomClient = nomClient;
        this.telephone = telephone;
        this.adresse = adresse;
        this.categorie = categorie;
        this.description = description;
        this.priorite = priorite;
        this.statut = statut;
        this.assignedSupervisorId = assignedSupervisorId;
        this.assignedSupervisorName = assignedSupervisorName;
        this.posteId = posteId;
        this.posteNom = posteNom;
        this.transformateurId = transformateurId;
        this.transformateurReference = transformateurReference;
        this.commentaires = commentaires;
        this.createdAt = createdAt;
        this.dateResolution = dateResolution;
        this.updatedAt = updatedAt;
        this.resolutionTimeHours = resolutionTimeHours;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String reference;
        private String nomClient;
        private String telephone;
        private String adresse;
        private ComplaintCategory categorie;
        private String description;
        private ComplaintPriority priorite;
        private ComplaintStatus statut;
        private Long assignedSupervisorId;
        private String assignedSupervisorName;
        private Long posteId;
        private String posteNom;
        private Long transformateurId;
        private String transformateurReference;
        private String commentaires;
        private LocalDateTime createdAt;
        private LocalDateTime dateResolution;
        private LocalDateTime updatedAt;
        private Long resolutionTimeHours;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder reference(String reference) {
            this.reference = reference;
            return this;
        }

        public Builder nomClient(String nomClient) {
            this.nomClient = nomClient;
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

        public Builder categorie(ComplaintCategory categorie) {
            this.categorie = categorie;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder priorite(ComplaintPriority priorite) {
            this.priorite = priorite;
            return this;
        }

        public Builder statut(ComplaintStatus statut) {
            this.statut = statut;
            return this;
        }

        public Builder assignedSupervisorId(Long assignedSupervisorId) {
            this.assignedSupervisorId = assignedSupervisorId;
            return this;
        }

        public Builder assignedSupervisorName(String assignedSupervisorName) {
            this.assignedSupervisorName = assignedSupervisorName;
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

        public Builder transformateurId(Long transformateurId) {
            this.transformateurId = transformateurId;
            return this;
        }

        public Builder transformateurReference(String transformateurReference) {
            this.transformateurReference = transformateurReference;
            return this;
        }

        public Builder commentaires(String commentaires) {
            this.commentaires = commentaires;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder dateResolution(LocalDateTime dateResolution) {
            this.dateResolution = dateResolution;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder resolutionTimeHours(Long resolutionTimeHours) {
            this.resolutionTimeHours = resolutionTimeHours;
            return this;
        }

        public ComplaintResponse build() {
            return new ComplaintResponse(id, reference, nomClient, telephone, adresse, categorie, description, priorite, statut, assignedSupervisorId, assignedSupervisorName, posteId, posteNom, transformateurId, transformateurReference, commentaires, createdAt, dateResolution, updatedAt, resolutionTimeHours);
        }
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

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
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

    public ComplaintCategory getCategorie() {
        return categorie;
    }

    public void setCategorie(ComplaintCategory categorie) {
        this.categorie = categorie;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ComplaintPriority getPriorite() {
        return priorite;
    }

    public void setPriorite(ComplaintPriority priorite) {
        this.priorite = priorite;
    }

    public ComplaintStatus getStatut() {
        return statut;
    }

    public void setStatut(ComplaintStatus statut) {
        this.statut = statut;
    }

    public Long getAssignedSupervisorId() {
        return assignedSupervisorId;
    }

    public void setAssignedSupervisorId(Long assignedSupervisorId) {
        this.assignedSupervisorId = assignedSupervisorId;
    }

    public String getAssignedSupervisorName() {
        return assignedSupervisorName;
    }

    public void setAssignedSupervisorName(String assignedSupervisorName) {
        this.assignedSupervisorName = assignedSupervisorName;
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

    public Long getTransformateurId() {
        return transformateurId;
    }

    public void setTransformateurId(Long transformateurId) {
        this.transformateurId = transformateurId;
    }

    public String getTransformateurReference() {
        return transformateurReference;
    }

    public void setTransformateurReference(String transformateurReference) {
        this.transformateurReference = transformateurReference;
    }

    public String getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(String commentaires) {
        this.commentaires = commentaires;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getDateResolution() {
        return dateResolution;
    }

    public void setDateResolution(LocalDateTime dateResolution) {
        this.dateResolution = dateResolution;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getResolutionTimeHours() {
        return resolutionTimeHours;
    }

    public void setResolutionTimeHours(Long resolutionTimeHours) {
        this.resolutionTimeHours = resolutionTimeHours;
    }
}
