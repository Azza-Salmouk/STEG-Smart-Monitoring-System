package com.steg.smartmonitoring.poste.dto;

import com.steg.smartmonitoring.poste.entity.EtatPoste;
import com.steg.smartmonitoring.poste.entity.TypePoste;
import jakarta.validation.constraints.*;

/**
 * DTO pour mettre à jour un poste
 */
public class UpdatePosteRequest {

    @Size(min = 3, max = 100, message = "Le nom doit avoir entre 3 et 100 caractères")
    private String nom;

    @Size(max = 50, message = "La région ne doit pas dépasser 50 caractères")
    private String region;

    @Size(min = 5, max = 255, message = "L'adresse doit avoir entre 5 et 255 caractères")
    private String adresse;

    @DecimalMin(value = "-90", message = "La latitude doit être entre -90 et 90")
    @DecimalMax(value = "90", message = "La latitude doit être entre -90 et 90")
    private Double latitude;

    @DecimalMin(value = "-180", message = "La longitude doit être entre -180 et 180")
    @DecimalMax(value = "180", message = "La longitude doit être entre -180 et 180")
    private Double longitude;

    private TypePoste typePoste;

    private EtatPoste etat;

    @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères")
    private String description;

    public UpdatePosteRequest() {
    }

    public UpdatePosteRequest(String nom, String region, String adresse, Double latitude, Double longitude, TypePoste typePoste, EtatPoste etat, String description) {
        this.nom = nom;
        this.region = region;
        this.adresse = adresse;
        this.latitude = latitude;
        this.longitude = longitude;
        this.typePoste = typePoste;
        this.etat = etat;
        this.description = description;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public TypePoste getTypePoste() {
        return typePoste;
    }

    public void setTypePoste(TypePoste typePoste) {
        this.typePoste = typePoste;
    }

    public EtatPoste getEtat() {
        return etat;
    }

    public void setEtat(EtatPoste etat) {
        this.etat = etat;
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
        private String nom;
        private String region;
        private String adresse;
        private Double latitude;
        private Double longitude;
        private TypePoste typePoste;
        private EtatPoste etat;
        private String description;

        public Builder nom(String nom) {
            this.nom = nom;
            return this;
        }

        public Builder region(String region) {
            this.region = region;
            return this;
        }

        public Builder adresse(String adresse) {
            this.adresse = adresse;
            return this;
        }

        public Builder latitude(Double latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder longitude(Double longitude) {
            this.longitude = longitude;
            return this;
        }

        public Builder typePoste(TypePoste typePoste) {
            this.typePoste = typePoste;
            return this;
        }

        public Builder etat(EtatPoste etat) {
            this.etat = etat;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public UpdatePosteRequest build() {
            return new UpdatePosteRequest(nom, region, adresse, latitude, longitude, typePoste, etat, description);
        }
    }
}
