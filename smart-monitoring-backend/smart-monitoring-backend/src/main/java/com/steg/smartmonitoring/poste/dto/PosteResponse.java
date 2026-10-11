package com.steg.smartmonitoring.poste.dto;

import com.steg.smartmonitoring.poste.entity.EtatPoste;
import com.steg.smartmonitoring.poste.entity.TypePoste;

import java.time.LocalDateTime;

/**
 * DTO pour retourner les informations d'un poste
 */
public class PosteResponse {

    private Long id;
    private String nom;
    private String code;
    private String region;
    private String adresse;
    private Double latitude;
    private Double longitude;
    private TypePoste typePoste;
    private EtatPoste etat;
    private String description;
    private Integer nombreTransformateurs;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PosteResponse() {
    }

    public PosteResponse(Long id, String nom, String code, String region, String adresse, Double latitude, Double longitude, TypePoste typePoste, EtatPoste etat, String description, Integer nombreTransformateurs, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.nom = nom;
        this.code = code;
        this.region = region;
        this.adresse = adresse;
        this.latitude = latitude;
        this.longitude = longitude;
        this.typePoste = typePoste;
        this.etat = etat;
        this.description = description;
        this.nombreTransformateurs = nombreTransformateurs;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getNombreTransformateurs() {
        return nombreTransformateurs;
    }

    public void setNombreTransformateurs(Integer nombreTransformateurs) {
        this.nombreTransformateurs = nombreTransformateurs;
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
        private String nom;
        private String code;
        private String region;
        private String adresse;
        private Double latitude;
        private Double longitude;
        private TypePoste typePoste;
        private EtatPoste etat;
        private String description;
        private Integer nombreTransformateurs;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

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

        public Builder etat(EtatPoste etat) {
            this.etat = etat;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder nombreTransformateurs(Integer nombreTransformateurs) {
            this.nombreTransformateurs = nombreTransformateurs;
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

        public PosteResponse build() {
            return new PosteResponse(id, nom, code, region, adresse, latitude, longitude, typePoste, etat, description, nombreTransformateurs, createdAt, updatedAt);
        }
    }
}
