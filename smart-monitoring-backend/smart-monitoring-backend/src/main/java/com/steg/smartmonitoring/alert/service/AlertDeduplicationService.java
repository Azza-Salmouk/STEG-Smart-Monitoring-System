package com.steg.smartmonitoring.alert.service;

import com.steg.smartmonitoring.alert.entity.Alert;
import com.steg.smartmonitoring.alert.entity.AlertStatus;
import com.steg.smartmonitoring.alert.entity.AlertType;
import com.steg.smartmonitoring.alert.repository.AlertRepository;
import com.steg.smartmonitoring.transformateur.entity.Transformateur;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Service for deduplicating alerts to prevent spam
 * If the same type of alert exists for a transformateur within a cooldown period,
 * skip creating a new alert
 */
@Service
@Transactional
public class AlertDeduplicationService {

    private final AlertRepository alertRepository;

    // Cooldown period in minutes - prevents duplicate alerts within this window
    private static final int ALERT_COOLDOWN_MINUTES = 5;

    public AlertDeduplicationService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    /**
     * Check if a similar alert already exists for this transformateur within the cooldown period
     *
     * @param transformateur The transformateur
     * @param alertType The alert type to check
     * @return true if alert should be skipped (similar one exists), false if new alert should be created
     */
    public boolean shouldSkipAlert(Transformateur transformateur, AlertType alertType) {
        if (transformateur == null || alertType == null) {
            return false;
        }

        LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(ALERT_COOLDOWN_MINUTES);

        // Check if an active alert of this type exists for this transformateur
        // created within the last ALERT_COOLDOWN_MINUTES
        Optional<Alert> recentAlert = alertRepository.findAll().stream()
            .filter(a -> a.getTransformateur().getId().equals(transformateur.getId()))
            .filter(a -> a.getType() == alertType)
            .filter(a -> a.getStatut() == AlertStatus.ACTIVE)
            .filter(a -> a.getCreatedAt().isAfter(cutoffTime))
            .findFirst();

        return recentAlert.isPresent();
    }

    /**
     * Get the most recent alert of a specific type for a transformateur
     */
    public Optional<Alert> getMostRecentAlert(Transformateur transformateur, AlertType alertType) {
        if (transformateur == null || alertType == null) {
            return Optional.empty();
        }

        return alertRepository.findAll().stream()
            .filter(a -> a.getTransformateur().getId().equals(transformateur.getId()))
            .filter(a -> a.getType() == alertType)
            .max((a1, a2) -> a1.getCreatedAt().compareTo(a2.getCreatedAt()));
    }

    /**
     * Check if all active alerts for a transformateur have been resolved
     * This allows new alerts of the same type to be created after resolution
     */
    public boolean hasActiveAlerts(Transformateur transformateur) {
        if (transformateur == null) {
            return false;
        }

        return alertRepository.findAll().stream()
            .anyMatch(a -> a.getTransformateur().getId().equals(transformateur.getId())
                && a.getStatut() == AlertStatus.ACTIVE);
    }
}
