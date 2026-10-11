package com.steg.smartmonitoring.intervention.dto;

import com.steg.smartmonitoring.intervention.entity.Priorite;
import com.steg.smartmonitoring.intervention.entity.StatutIntervention;
import com.steg.smartmonitoring.intervention.entity.TypeIntervention;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

/**
 * DTO pour la mise à jour d'une intervention
 */
public class UpdateInterventionRequest {

    @NotNull(message = "La date planifiée est requise")
    private LocalDateTime datePlanifiee;

    private LocalDateTime dateDebut;

    private LocalDateTime dateFin;

    @NotNull(message = "Le type d'intervention est requis")
    private TypeIntervention typeIntervention;

    @NotNull(message = "La priorité est requise")
    private Priorite priorite;

    @NotNull(message = "Le statut est requis")
    private StatutIntervention statut;

    @NotBlank(message = "La description est requise")
    @Size(min = 10, max = 1000, message = "La description doit faire entre 10 et 1000 caractères")
    private String description;

    @Size(max = 2000, message = "Le résultat ne doit pas dépasser 2000 caractères")
    private String resultat;

    @Size(max = 2000, message = "Les observations ne doivent pas dépasser 2000 caractères")
    private String observations;

    @Min(value = 0, message = "La durée doit être positive")
    private Long dureeMinutes;

    @Min(value = 0, message = "Le coût doit être positif")
    private Double cout;

    public UpdateInterventionRequest() {
    }

    public UpdateInterventionRequest(LocalDateTime datePlanifiee, LocalDateTime dateDebut, LocalDateTime dateFin,
                                    TypeIntervention typeIntervention, Priorite priorite, StatutIntervention statut,
                                    String description, String resultat, String observations,
                                    Long dureeMinutes, Double cout) {
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
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

        public UpdateInterventionRequest build() {
            return new UpdateInterventionRequest(datePlanifiee, dateDebut, dateFin, typeIntervention,
                                                priorite, statut, description, resultat, observations,
                                                dureeMinutes, cout);
        }
    }
}
