package com.steg.smartmonitoring.intervention.entity;

import com.steg.smartmonitoring.transformateur.entity.Transformateur;
import com.steg.smartmonitoring.user.entity.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Entité Intervention pour la gestion des interventions sur les transformateurs
 */
@Entity
@Table(name = "interventions")
public class Intervention {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String reference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transformateur_id", nullable = false)
    private Transformateur transformateur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id", nullable = false)
    private User agent;

    @Column(nullable = false)
    private LocalDateTime dateCreation;

    @Column(nullable = false)
    private LocalDateTime datePlanifiee;

    private LocalDateTime dateDebut;

    private LocalDateTime dateFin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeIntervention typeIntervention;

    @Enumerated(EnumType.STRING)
    @Column(name = "priorite", nullable = false)
    private Priorite priorite;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private StatutIntervention statut;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String resultat;

    @Column(columnDefinition = "TEXT")
    private String observations;

    @Column(name = "duree_minutes")
    private Long dureeMinutes;

    @Column(name = "cout")
    private Double cout;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // No-arg constructor
    public Intervention() {
    }

    // All-args constructor
    public Intervention(Long id, String reference, Transformateur transformateur, User agent,
                       LocalDateTime dateCreation, LocalDateTime datePlanifiee,
                       LocalDateTime dateDebut, LocalDateTime dateFin,
                       TypeIntervention typeIntervention, Priorite priorite,
                       StatutIntervention statut, String description, String resultat,
                       String observations, Long dureeMinutes, Double cout,
                       LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.reference = reference;
        this.transformateur = transformateur;
        this.agent = agent;
        this.dateCreation = dateCreation;
        this.datePlanifiee = datePlanifiee;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.typeIntervention = typeIntervention;
        this.priorite = priorite;
        this.statut = statut;
        this.description = description;
        this.resultat = resultat;
        this.observations = observations;
        this.dureeMinutes = dureeMinutes;
        this.cout = cout;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String reference;
        private Transformateur transformateur;
        private User agent;
        private LocalDateTime dateCreation;
        private LocalDateTime datePlanifiee;
        private LocalDateTime dateDebut;
        private LocalDateTime dateFin;
        private TypeIntervention typeIntervention;
        private Priorite priorite;
        private StatutIntervention statut;
        private String description;
        private String resultat;
        private String observations;
        private Long dureeMinutes;
        private Double cout;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder reference(String reference) {
            this.reference = reference;
            return this;
        }

        public Builder transformateur(Transformateur transformateur) {
            this.transformateur = transformateur;
            return this;
        }

        public Builder agent(User agent) {
            this.agent = agent;
            return this;
        }

        public Builder dateCreation(LocalDateTime dateCreation) {
            this.dateCreation = dateCreation;
            return this;
        }

        public Builder datePlanifiee(LocalDateTime datePlanifiee) {
            this.datePlanifiee = datePlanifiee;
            return this;
        }

        public Builder dateDebut(LocalDateTime dateDebut) {
            this.dateDebut = dateDebut;
            return this;
        }

        public Builder dateFin(LocalDateTime dateFin) {
            this.dateFin = dateFin;
            return this;
        }

        public Builder typeIntervention(TypeIntervention typeIntervention) {
            this.typeIntervention = typeIntervention;
            return this;
        }

        public Builder priorite(Priorite priorite) {
            this.priorite = priorite;
            return this;
        }

        public Builder statut(StatutIntervention statut) {
            this.statut = statut;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder resultat(String resultat) {
            this.resultat = resultat;
            return this;
        }

        public Builder observations(String observations) {
            this.observations = observations;
            return this;
        }

        public Builder dureeMinutes(Long dureeMinutes) {
            this.dureeMinutes = dureeMinutes;
            return this;
        }

        public Builder cout(Double cout) {
            this.cout = cout;
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

        public Intervention build() {
            return new Intervention(id, reference, transformateur, agent,
                    dateCreation, datePlanifiee, dateDebut, dateFin,
                    typeIntervention, priorite, statut, description, resultat,
                    observations, dureeMinutes, cout, createdAt, updatedAt);
        }
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getReference() {
        return reference;
    }

    public Transformateur getTransformateur() {
        return transformateur;
    }

    public User getAgent() {
        return agent;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public LocalDateTime getDatePlanifiee() {
        return datePlanifiee;
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public LocalDateTime getDateFin() {
        return dateFin;
    }

    public TypeIntervention getTypeIntervention() {
        return typeIntervention;
    }

    public Priorite getPriorite() {
        return priorite;
    }

    public StatutIntervention getStatut() {
        return statut;
    }

    public String getDescription() {
        return description;
    }

    public String getResultat() {
        return resultat;
    }

    public String getObservations() {
        return observations;
    }

    public Long getDureeMinutes() {
        return dureeMinutes;
    }

    public Double getCout() {
        return cout;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setTransformateur(Transformateur transformateur) {
        this.transformateur = transformateur;
    }

    public void setAgent(User agent) {
        this.agent = agent;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public void setDatePlanifiee(LocalDateTime datePlanifiee) {
        this.datePlanifiee = datePlanifiee;
    }

    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(LocalDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public void setTypeIntervention(TypeIntervention typeIntervention) {
        this.typeIntervention = typeIntervention;
    }

    public void setPriorite(Priorite priorite) {
        this.priorite = priorite;
    }

    public void setStatut(StatutIntervention statut) {
        this.statut = statut;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setResultat(String resultat) {
        this.resultat = resultat;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public void setDureeMinutes(Long dureeMinutes) {
        this.dureeMinutes = dureeMinutes;
    }

    public void setCout(Double cout) {
        this.cout = cout;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.dateCreation = LocalDateTime.now();
        if (this.statut == null) {
            this.statut = StatutIntervention.EN_ATTENTE;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
