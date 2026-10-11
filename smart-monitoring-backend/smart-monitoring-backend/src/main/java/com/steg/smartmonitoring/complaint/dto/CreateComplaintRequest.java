package com.steg.smartmonitoring.complaint.dto;

import com.steg.smartmonitoring.complaint.entity.ComplaintCategory;
import com.steg.smartmonitoring.complaint.entity.ComplaintPriority;
import jakarta.validation.constraints.*;

/**
 * DTO pour la création d'une réclamation
 */
public class CreateComplaintRequest {

    @NotBlank(message = "Le nom du client est requis")
    @Size(min = 3, max = 100, message = "Le nom doit faire entre 3 et 100 caractères")
    private String nomClient;

    @NotBlank(message = "Le téléphone est requis")
    @Size(min = 8, max = 20, message = "Le téléphone doit faire entre 8 et 20 caractères")
    private String telephone;

    @NotBlank(message = "L'adresse est requise")
    @Size(min = 5, max = 255, message = "L'adresse doit faire entre 5 et 255 caractères")
    private String adresse;

    @NotNull(message = "La catégorie est requise")
    private ComplaintCategory categorie;

    @NotBlank(message = "La description est requise")
    @Size(min = 10, max = 2000, message = "La description doit faire entre 10 et 2000 caractères")
    private String description;

    @NotNull(message = "La priorité est requise")
    private ComplaintPriority priorite;

    private Long posteId;

    private Long transformateurId;

    public CreateComplaintRequest() {
    }

    public CreateComplaintRequest(String nomClient, String telephone, String adresse, ComplaintCategory categorie, String description, ComplaintPriority priorite, Long posteId, Long transformateurId) {
        this.nomClient = nomClient;
        this.telephone = telephone;
        this.adresse = adresse;
        this.categorie = categorie;
        this.description = description;
        this.priorite = priorite;
        this.posteId = posteId;
        this.transformateurId = transformateurId;
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

    public Long getPosteId() {
        return posteId;
    }

    public void setPosteId(Long posteId) {
        this.posteId = posteId;
    }

    public Long getTransformateurId() {
        return transformateurId;
    }

    public void setTransformateurId(Long transformateurId) {
        this.transformateurId = transformateurId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String nomClient;
        private String telephone;
        private String adresse;
        private ComplaintCategory categorie;
        private String description;
        private ComplaintPriority priorite;
        private Long posteId;
        private Long transformateurId;

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

        public Builder posteId(Long posteId) {
            this.posteId = posteId;
            return this;
        }

        public Builder transformateurId(Long transformateurId) {
            this.transformateurId = transformateurId;
            return this;
        }

        public CreateComplaintRequest build() {
            return new CreateComplaintRequest(nomClient, telephone, adresse, categorie, description, priorite, posteId, transformateurId);
        }
    }
}
