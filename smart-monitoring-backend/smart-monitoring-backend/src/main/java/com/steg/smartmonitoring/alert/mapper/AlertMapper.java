package com.steg.smartmonitoring.alert.mapper;

import com.steg.smartmonitoring.alert.dto.AlertResponse;
import com.steg.smartmonitoring.alert.entity.Alert;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Mapper pour les alertes
 */
@Component
public class AlertMapper {

    /**
     * Convertit une entité Alert en AlertResponse
     */
    public AlertResponse toResponse(Alert alert) {
        if (alert == null) {
            return null;
        }

        Long resolutionTimeMinutes = null;
        if (alert.getResolvedAt() != null && alert.getCreatedAt() != null) {
            resolutionTimeMinutes = ChronoUnit.MINUTES.between(alert.getCreatedAt(), alert.getResolvedAt());
        }

        return AlertResponse.builder()
                .id(alert.getId())
                .reference(alert.getReference())
                .titre(alert.getTitre())
                .description(alert.getDescription())
                .type(alert.getType())
                .niveau(alert.getNiveau())
                .statut(alert.getStatut())
                .measurementId(alert.getMeasurement() != null ? alert.getMeasurement().getId() : null)
                .transformateurId(alert.getTransformateur() != null ? alert.getTransformateur().getId() : null)
                .transformateurName(alert.getTransformateur() != null ? alert.getTransformateur().getReference() : null)
                .posteId(alert.getPoste() != null ? alert.getPoste().getId() : null)
                .posteName(alert.getPoste() != null ? alert.getPoste().getNom() : null)
                .resolvedByUserId(alert.getResolvedBy() != null ? alert.getResolvedBy().getId() : null)
                .resolvedByUsername(alert.getResolvedBy() != null ? alert.getResolvedBy().getUsername() : null)
                .createdAt(alert.getCreatedAt())
                .resolvedAt(alert.getResolvedAt())
                .isAcknowledged(alert.getIsAcknowledged())
                .isArchived(alert.getIsArchived())
                .updatedAt(alert.getUpdatedAt())
                .resolutionTimeMinutes(resolutionTimeMinutes)
                .build();
    }
}
