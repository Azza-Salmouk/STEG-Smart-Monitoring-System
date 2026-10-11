package com.steg.smartmonitoring.alert.entity;

import com.steg.smartmonitoring.measurement.entity.Measurement;
import com.steg.smartmonitoring.poste.entity.Poste;
import com.steg.smartmonitoring.transformateur.entity.Transformateur;
import com.steg.smartmonitoring.user.entity.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Entité Alert pour la gestion des alertes techniques
 */
@Entity
@Table(name = "alerts")
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String reference;

    @Column(nullable = false, length = 255)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "niveau", nullable = false)
    private AlertLevel niveau;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private AlertStatus statut;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "measurement_id")
    private Measurement measurement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transformateur_id", nullable = false)
    private Transformateur transformateur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poste_id")
    private Poste poste;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resolved_by_id")
    private User resolvedBy;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime resolvedAt;

    @Column(columnDefinition = "boolean default false")
    private Boolean isAcknowledged = false;

    @Column(columnDefinition = "boolean default false")
    private Boolean isArchived = false;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // No-arg constructor
    public Alert() {
        this.isAcknowledged = false;
        this.isArchived = false;
    }

    // All-args constructor
    public Alert(Long id, String reference, String titre, String description, AlertType type, AlertLevel niveau, AlertStatus statut, Measurement measurement, Transformateur transformateur, Poste poste, User resolvedBy, LocalDateTime createdAt, LocalDateTime resolvedAt, Boolean isAcknowledged, Boolean isArchived, LocalDateTime updatedAt) {
        this.id = id;
        this.reference = reference;
        this.titre = titre;
        this.description = description;
        this.type = type;
        this.niveau = niveau;
        this.statut = statut;
        this.measurement = measurement;
        this.transformateur = transformateur;
        this.poste = poste;
        this.resolvedBy = resolvedBy;
        this.createdAt = createdAt;
        this.resolvedAt = resolvedAt;
        this.isAcknowledged = isAcknowledged != null ? isAcknowledged : false;
        this.isArchived = isArchived != null ? isArchived : false;
        this.updatedAt = updatedAt;
    }

    // Builder pattern
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
        private Measurement measurement;
        private Transformateur transformateur;
        private Poste poste;
        private User resolvedBy;
        private LocalDateTime createdAt;
        private LocalDateTime resolvedAt;
        private Boolean isAcknowledged = false;
        private Boolean isArchived = false;
        private LocalDateTime updatedAt;

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

        public Builder measurement(Measurement measurement) {
            this.measurement = measurement;
            return this;
        }

        public Builder transformateur(Transformateur transformateur) {
            this.transformateur = transformateur;
            return this;
        }

        public Builder poste(Poste poste) {
            this.poste = poste;
            return this;
        }

        public Builder resolvedBy(User resolvedBy) {
            this.resolvedBy = resolvedBy;
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

        public Alert build() {
            return new Alert(id, reference, titre, description, type, niveau, statut, measurement, transformateur, poste, resolvedBy, createdAt, resolvedAt, isAcknowledged, isArchived, updatedAt);
        }
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.statut == null) {
            this.statut = AlertStatus.ACTIVE;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Explicit getters and setters
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

    public Measurement getMeasurement() {
        return measurement;
    }

    public void setMeasurement(Measurement measurement) {
        this.measurement = measurement;
    }

    public Transformateur getTransformateur() {
        return transformateur;
    }

    public void setTransformateur(Transformateur transformateur) {
        this.transformateur = transformateur;
    }

    public Poste getPoste() {
        return poste;
    }

    public void setPoste(Poste poste) {
        this.poste = poste;
    }

    public User getResolvedBy() {
        return resolvedBy;
    }

    public void setResolvedBy(User resolvedBy) {
        this.resolvedBy = resolvedBy;
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
}
