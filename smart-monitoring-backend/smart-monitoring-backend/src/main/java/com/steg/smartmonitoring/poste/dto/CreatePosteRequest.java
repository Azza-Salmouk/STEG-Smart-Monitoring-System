package com.steg.smartmonitoring.poste.dto;

import com.steg.smartmonitoring.poste.entity.TypePoste;
import jakarta.validation.constraints.*;

/**
 * DTO pour créer un nouveau poste
 */
public class CreatePosteRequest {

    @NotBlank(message = "Le nom du poste est requis")
    @Size(min = 3, max = 100, message = "Le nom doit avoir entre 3 et 100 caractères")
    private String nom;

    @NotBlank(message = "Le code du poste est requis")
    @Size(min = 2, max = 50, message = "Le code doit avoir entre 2 et 50 caractères")
    private String code;

    @NotBlank(message = "La région est requise")
    @Size(max = 50, message = "La région ne doit pas dépasser 50 caractères")
    private String region;

    @NotBlank(message = "L'adresse est requise")
    @Size(min = 5, max = 255, message = "L'adresse doit avoir entre 5 et 255 caractères")
    private String adresse;

    @NotNull(message = "La latitude est requise")
    @DecimalMin(value = "-90", message = "La latitude doit être entre -90 et 90")
    @DecimalMax(value = "90", message = "La latitude doit être entre -90 et 90")
    private Double latitude;

    @NotNull(message = "La longitude est requise")
    @DecimalMin(value = "-180", message = "La longitude doit être entre -180 et 180")
    @DecimalMax(value = "180", message = "La longitude doit être entre -180 et 180")
    private Double longitude;

    @NotNull(message = "Le type de poste est requis")
    private TypePoste typePoste;

    @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères")
    private String description;

    public CreatePosteRequest() {
    }

    public CreatePosteRequest(String nom, String code, String region, String adresse, Double latitude, Double longitude, TypePoste typePoste, String description) {
        this.nom = nom;
        this.code = code;
        this.region = region;
        this.adresse = adresse;
        this.latitude = latitude;
        this.longitude = longitude;
        this.typePoste = typePoste;
        this.description = description;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
        private String code;
        private String region;
        private String adresse;
        private Double latitude;
        private Double longitude;
        private TypePoste typePoste;
        private String description;

        public Builder nom(String nom) {
            this.nom = nom;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
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

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public CreatePosteRequest build() {
            return new CreatePosteRequest(nom, code, region, adresse, latitude, longitude, typePoste, description);
        }
    }
}
