package com.steg.smartmonitoring.intervention.mapper;

import com.steg.smartmonitoring.intervention.dto.CreateInterventionRequest;
import com.steg.smartmonitoring.intervention.dto.InterventionResponse;
import com.steg.smartmonitoring.intervention.dto.UpdateInterventionRequest;
import com.steg.smartmonitoring.intervention.entity.Intervention;
import com.steg.smartmonitoring.transformateur.entity.Transformateur;
import com.steg.smartmonitoring.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir entre Intervention et ses DTOs
 */
@Component
@RequiredArgsConstructor
public class InterventionMapper {

    /**
     * Convertir CreateInterventionRequest en Intervention
     */
    public Intervention toEntity(CreateInterventionRequest request, Transformateur transformateur, User agent, String reference) {
        return Intervention.builder()
                .reference(reference)
                .transformateur(transformateur)
                .agent(agent)
                .datePlanifiee(request.getDatePlanifiee())
                .typeIntervention(request.getTypeIntervention())
                .priorite(request.getPriorite())
                .description(request.getDescription())
                .observations(request.getObservations())
                .cout(request.getCout())
                .build();
    }

    /**
     * Mettre à jour une Intervention avec UpdateInterventionRequest
     */
    public void updateEntity(UpdateInterventionRequest request, Intervention intervention) {
        intervention.setDatePlanifiee(request.getDatePlanifiee());
        intervention.setDateDebut(request.getDateDebut());
        intervention.setDateFin(request.getDateFin());
        intervention.setTypeIntervention(request.getTypeIntervention());
        intervention.setPriorite(request.getPriorite());
        intervention.setStatut(request.getStatut());
        intervention.setDescription(request.getDescription());
        intervention.setResultat(request.getResultat());
        intervention.setObservations(request.getObservations());
        intervention.setDureeMinutes(request.getDureeMinutes());
        intervention.setCout(request.getCout());
    }

    /**
     * Convertir Intervention en InterventionResponse
     */
    public InterventionResponse toResponse(Intervention intervention) {
        return InterventionResponse.builder()
                .id(intervention.getId())
                .reference(intervention.getReference())
                .transformateurId(intervention.getTransformateur().getId())
                .transformateurNom(intervention.getTransformateur().getReference())
                .agentId(intervention.getAgent().getId())
                .agentNom(intervention.getAgent().getUsername())
                .dateCreation(intervention.getDateCreation())
                .datePlanifiee(intervention.getDatePlanifiee())
                .dateDebut(intervention.getDateDebut())
                .dateFin(intervention.getDateFin())
                .typeIntervention(intervention.getTypeIntervention())
                .priorite(intervention.getPriorite())
                .statut(intervention.getStatut())
                .description(intervention.getDescription())
                .resultat(intervention.getResultat())
                .observations(intervention.getObservations())
                .dureeMinutes(intervention.getDureeMinutes())
                .cout(intervention.getCout())
                .createdAt(intervention.getCreatedAt())
                .updatedAt(intervention.getUpdatedAt())
                .build();
    }
}
