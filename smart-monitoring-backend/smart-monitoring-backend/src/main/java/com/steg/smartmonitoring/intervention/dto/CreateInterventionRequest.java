package com.steg.smartmonitoring.intervention.dto;

import com.steg.smartmonitoring.intervention.entity.Priorite;
import com.steg.smartmonitoring.intervention.entity.TypeIntervention;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

/**
 * DTO pour la création d'une intervention
 */
public class CreateInterventionRequest {

    @NotNull(message = "Le transformateur est requis")
    private Long transformateurId;

    @NotNull(message = "L'agent est requis")
    private Long agentId;

    @NotNull(message = "La date planifiée est requise")
    @FutureOrPresent(message = "La date planifiée doit être dans le futur ou aujourd'hui")
    private LocalDateTime datePlanifiee;

    @NotNull(message = "Le type d'intervention est requis")
    private TypeIntervention typeIntervention;

    @NotNull(message = "La priorité est requise")
    private Priorite priorite;

    private LocalDateTime dateDebut;

    private LocalDateTime dateFin;

    @NotBlank(message = "La description est requise")
    @Size(min = 10, max = 1000, message = "La description doit faire entre 10 et 1000 caractères")
    private String description;

    private String resultat;

    @Size(max = 1000, message = "Les observations ne doivent pas dépasser 1000 caractères")
    private String observations;

    private Long dureeMinutes;

    @Min(value = 0, message = "Le coût doit être positif")
    private Double cout;

    public CreateInterventionRequest() {
    }

    public CreateInterventionRequest(Long transformateurId, Long agentId, LocalDateTime datePlanifiee, 
                                    TypeIntervention typeIntervention, Priorite priorite, LocalDateTime dateDebut, 
                                    LocalDateTime dateFin, String description, String resultat, 
                                    String observations, Long dureeMinutes, Double cout) {
        this.transformateurId = transformateurId;
        this.agentId = agentId;
        this.datePlanifiee = datePlanifiee;
        this.typeIntervention = typeIntervention;
        this.priorite = priorite;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.description = description;
        this.resultat = resultat;
        this.observations = observations;
        this.dureeMinutes = dureeMinutes;
        this.cout = cout;
    }

    public Long getTransformateurId() {
        return transformateurId;
    }

    public void setTransformateurId(Long transformateurId) {
        this.transformateurId = transformateurId;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public LocalDateTime getDatePlanifiee() {
        return datePlanifiee;
    }

    public void setDatePlanifiee(LocalDateTime datePlanifiee) {
        this.datePlanifiee = datePlanifiee;
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
        private Long transformateurId;
        private Long agentId;
        private LocalDateTime datePlanifiee;
        private TypeIntervention typeIntervention;
        private Priorite priorite;
        private LocalDateTime dateDebut;
        private LocalDateTime dateFin;
        private String description;
        private String resultat;
        private String observations;
        private Long dureeMinutes;
        private Double cout;

        public Builder transformateurId(Long transformateurId) {
            this.transformateurId = transformateurId;
            return this;
        }

        public Builder agentId(Long agentId) {
            this.agentId = agentId;
            return this;
        }

        public Builder datePlanifiee(LocalDateTime datePlanifiee) {
            this.datePlanifiee = datePlanifiee;
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

        public Builder dateDebut(LocalDateTime dateDebut) {
            this.dateDebut = dateDebut;
            return this;
        }

        public Builder dateFin(LocalDateTime dateFin) {
            this.dateFin = dateFin;
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

        public CreateInterventionRequest build() {
            return new CreateInterventionRequest(transformateurId, agentId, datePlanifiee, typeIntervention, 
                                                priorite, dateDebut, dateFin, description, resultat, 
                                                observations, dureeMinutes, cout);
        }
    }
}
