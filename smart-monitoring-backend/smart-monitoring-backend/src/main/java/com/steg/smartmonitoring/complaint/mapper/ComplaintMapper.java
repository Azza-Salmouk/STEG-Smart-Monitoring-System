package com.steg.smartmonitoring.complaint.mapper;

import com.steg.smartmonitoring.complaint.dto.CreateComplaintRequest;
import com.steg.smartmonitoring.complaint.dto.ComplaintResponse;
import com.steg.smartmonitoring.complaint.entity.Complaint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Mapper pour convertir entre Complaint et ses DTOs
 */
@Component
@RequiredArgsConstructor
public class ComplaintMapper {

    /**
     * Convertir Complaint en ComplaintResponse
     */
    public ComplaintResponse toResponse(Complaint complaint) {
        Long resolutionTimeHours = null;
        if (complaint.getDateResolution() != null && complaint.getCreatedAt() != null) {
            resolutionTimeHours = ChronoUnit.HOURS.between(complaint.getCreatedAt(), complaint.getDateResolution());
        }

        return ComplaintResponse.builder()
                .id(complaint.getId())
                .reference(complaint.getReference())
                .nomClient(complaint.getNomClient())
                .telephone(complaint.getTelephone())
                .adresse(complaint.getAdresse())
                .categorie(complaint.getCategorie())
                .description(complaint.getDescription())
                .priorite(complaint.getPriorite())
                .statut(complaint.getStatut())
                .assignedSupervisorId(complaint.getAssignedSupervisor() != null ? complaint.getAssignedSupervisor().getId() : null)
                .assignedSupervisorName(complaint.getAssignedSupervisor() != null ? complaint.getAssignedSupervisor().getUsername() : null)
                .posteId(complaint.getPoste() != null ? complaint.getPoste().getId() : null)
                .posteNom(complaint.getPoste() != null ? complaint.getPoste().getNom() : null)
                .transformateurId(complaint.getTransformateur() != null ? complaint.getTransformateur().getId() : null)
                .transformateurReference(complaint.getTransformateur() != null ? complaint.getTransformateur().getReference() : null)
                .commentaires(complaint.getCommentaires())
                .createdAt(complaint.getCreatedAt())
                .dateResolution(complaint.getDateResolution())
                .updatedAt(complaint.getUpdatedAt())
                .resolutionTimeHours(resolutionTimeHours)
                .build();
    }
}
