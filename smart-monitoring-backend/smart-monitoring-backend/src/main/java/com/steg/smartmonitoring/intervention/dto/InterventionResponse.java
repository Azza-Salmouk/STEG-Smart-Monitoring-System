package com.steg.smartmonitoring.intervention.dto;

import com.steg.smartmonitoring.intervention.entity.Priorite;
import com.steg.smartmonitoring.intervention.entity.StatutIntervention;
import com.steg.smartmonitoring.intervention.entity.TypeIntervention;

import java.time.LocalDateTime;

/**
 * DTO pour la réponse d'une intervention
 */
public class InterventionResponse {

    private Long id;
    private String reference;
    private Long transformateurId;
    private String transformateurNom;
    private Long agentId;
    private String agentNom;
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

    public InterventionResponse() {
    }

    public InterventionResponse(Long id, String reference, Long transformateurId, String transformateurNom, Long agentId, String agentNom, LocalDateTime dateCreation, LocalDateTime datePlanifiee, LocalDateTime dateDebut, LocalDateTime dateFin, TypeIntervention typeIntervention, Priorite priorite, StatutIntervention statut, String description, String resultat, String observations, Long dureeMinutes, Double cout, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.reference = reference;
        this.transformateurId = transformateurId;
        this.transformateurNom = transformateurNom;
        this.agentId = agentId;
        this.agentNom = agentNom;
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

    public Long getTransformateurId() {
        return transformateurId;
    }

    public void setTransformateurId(Long transformateurId) {
        this.transformateurId = transformateurId;
    }

    public String getTransformateurNom() {
        return transformateurNom;
    }

    public void setTransformateurNom(String transformateurNom) {
        this.transformateurNom = transformateurNom;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public String getAgentNom() {
        return agentNom;
    }

    public void setAgentNom(String agentNom) {
        this.agentNom = agentNom;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDateTime getDatePlanifiee() {
        return datePlanifiee;
    }

    public void setDatePlanifiee(LocalDateTime datePlanifiee) {
        this.datePlanifiee = datePlanifiee;
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public TypeIntervention getTypeIntervention() {
        return typeIntervention;
    }

    public void setTypeIntervention(TypeIntervention typeIntervention) {
        this.typeIntervention = typeIntervention;
    }

    public Priorite getPriorite() {
        return priorite;
    }

    public void setPriorite(Priorite priorite) {
        this.priorite = priorite;
    }

    public StatutIntervention getStatut() {
        return statut;
    }

    public void setStatut(StatutIntervention statut) {
        this.statut = statut;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResultat() {
        return resultat;
    }

    public void setResultat(String resultat) {
        this.resultat = resultat;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public Long getDureeMinutes() {
        return dureeMinutes;
    }

    public void setDureeMinutes(Long dureeMinutes) {
        this.dureeMinutes = dureeMinutes;
    }

    public Double getCout() {
        return cout;
    }

    public void setCout(Double cout) {
        this.cout = cout;
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
        private String reference;
        private Long transformateurId;
        private String transformateurNom;
        private Long agentId;
        private String agentNom;
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

        public Builder transformateurId(Long transformateurId) {
            this.transformateurId = transformateurId;
            return this;
        }

        public Builder transformateurNom(String transformateurNom) {
            this.transformateurNom = transformateurNom;
            return this;
        }

        public Builder agentId(Long agentId) {
            this.agentId = agentId;
            return this;
        }

        public Builder agentNom(String agentNom) {
            this.agentNom = agentNom;
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

        public InterventionResponse build() {
            return new InterventionResponse(id, reference, transformateurId, transformateurNom, agentId, agentNom, dateCreation, datePlanifiee, dateDebut, dateFin, typeIntervention, priorite, statut, description, resultat, observations, dureeMinutes, cout, createdAt, updatedAt);
        }
    }
}
