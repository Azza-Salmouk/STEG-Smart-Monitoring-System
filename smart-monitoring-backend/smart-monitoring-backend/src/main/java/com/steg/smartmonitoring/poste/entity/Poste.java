package com.steg.smartmonitoring.poste.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant un poste électrique
 * Un poste peut contenir plusieurs transformateurs
 */
@Entity
@Table(name = "postes", uniqueConstraints = {
    @UniqueConstraint(columnNames = "code", name = "uk_poste_code")
})
public class Poste {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom du poste est requis")
    @Column(nullable = false, length = 100)
    private String nom;

    @NotBlank(message = "Le code du poste est requis")
    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, length = 50)
    private String region;

    @Column(nullable = false, length = 255)
    private String adresse;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypePoste typePoste;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EtatPoste etat = EtatPoste.ACTIF;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Relation One-to-Many avec Transformateur
     * Un poste possède plusieurs transformateurs
     */
    @OneToMany(
        mappedBy = "poste",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    private List<com.steg.smartmonitoring.transformateur.entity.Transformateur> transformateurs = new ArrayList<>();

    // No-arg constructor
    public Poste() {
        this.etat = EtatPoste.ACTIF;
        this.transformateurs = new ArrayList<>();
    }

    // All-args constructor
    public Poste(Long id, String nom, String code, String region, String adresse, Double latitude, Double longitude, TypePoste typePoste, EtatPoste etat, String description, LocalDateTime createdAt, LocalDateTime updatedAt, List<com.steg.smartmonitoring.transformateur.entity.Transformateur> transformateurs) {
        this.id = id;
        this.nom = nom;
        this.code = code;
        this.region = region;
        this.adresse = adresse;
        this.latitude = latitude;
        this.longitude = longitude;
        this.typePoste = typePoste;
        this.etat = etat != null ? etat : EtatPoste.ACTIF;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.transformateurs = transformateurs != null ? transformateurs : new ArrayList<>();
    }

    // Builder pattern
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
        private EtatPoste etat = EtatPoste.ACTIF;
        private String description;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private List<com.steg.smartmonitoring.transformateur.entity.Transformateur> transformateurs = new ArrayList<>();

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

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder transformateurs(List<com.steg.smartmonitoring.transformateur.entity.Transformateur> transformateurs) {
            this.transformateurs = transformateurs;
            return this;
        }

        public Poste build() {
            return new Poste(id, nom, code, region, adresse, latitude, longitude, typePoste, etat, description, createdAt, updatedAt, transformateurs);
        }
    }

    // Getters and Setters
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

    public List<com.steg.smartmonitoring.transformateur.entity.Transformateur> getTransformateurs() {
        return transformateurs;
    }

    public void setTransformateurs(List<com.steg.smartmonitoring.transformateur.entity.Transformateur> transformateurs) {
        this.transformateurs = transformateurs;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Ajouter un transformateur à ce poste
     */
    public void addTransformateur(com.steg.smartmonitoring.transformateur.entity.Transformateur transformateur) {
        if (transformateurs == null) {
            transformateurs = new ArrayList<>();
        }
        transformateurs.add(transformateur);
        transformateur.setPoste(this);
    }

    /**
     * Retirer un transformateur de ce poste
     */
    public void removeTransformateur(com.steg.smartmonitoring.transformateur.entity.Transformateur transformateur) {
        if (transformateurs != null) {
            transformateurs.remove(transformateur);
            transformateur.setPoste(null);
        }
    }
}
