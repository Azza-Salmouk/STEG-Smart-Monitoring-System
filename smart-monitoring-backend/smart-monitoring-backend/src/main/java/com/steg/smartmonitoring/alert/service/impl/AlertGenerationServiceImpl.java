package com.steg.smartmonitoring.alert.service.impl;

import com.steg.smartmonitoring.alert.entity.Alert;
import com.steg.smartmonitoring.alert.entity.AlertLevel;
import com.steg.smartmonitoring.alert.entity.AlertStatus;
import com.steg.smartmonitoring.alert.entity.AlertType;
import com.steg.smartmonitoring.alert.repository.AlertRepository;
import com.steg.smartmonitoring.alert.service.AlertDeduplicationService;
import com.steg.smartmonitoring.alert.service.AlertGenerationService;
import com.steg.smartmonitoring.measurement.entity.Measurement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class AlertGenerationServiceImpl implements AlertGenerationService {

    private static final Logger logger = LoggerFactory.getLogger(AlertGenerationServiceImpl.class);
    private final AlertRepository alertRepository;
    private final AlertDeduplicationService deduplicationService;

    public AlertGenerationServiceImpl(AlertRepository alertRepository, AlertDeduplicationService deduplicationService) {
        this.alertRepository = alertRepository;
        this.deduplicationService = deduplicationService;
    }

    @Override
    @Transactional
    public void generateAlertsFromMeasurement(Measurement measurement) {
        if (measurement == null || measurement.getTransformateur() == null) {
            return;
        }

        // Check all anomaly conditions and create alerts (with deduplication)
        if (isTemperatureAnomalous(measurement)) {
            if (!deduplicationService.shouldSkipAlert(measurement.getTransformateur(), AlertType.TEMPERATURE)) {
                createAndSaveAlert(measurement, AlertLevel.HIGH, "Temperature High", "Temperature is above threshold");
            } else {
                logger.debug("Skipping duplicate TEMPERATURE alert for transformateur {}", measurement.getTransformateur().getId());
            }
        }

        if (isVoltageDip(measurement)) {
            if (!deduplicationService.shouldSkipAlert(measurement.getTransformateur(), AlertType.CHUTE_TENSION)) {
                createAndSaveAlert(measurement, AlertLevel.HIGH, "Voltage Dip", "Voltage is below acceptable level");
            }
        }

        if (isOverload(measurement)) {
            if (!deduplicationService.shouldSkipAlert(measurement.getTransformateur(), AlertType.SURCHARGE)) {
                createAndSaveAlert(measurement, AlertLevel.HIGH, "Overload", "Current overload detected");
            }
        }

        if (isHealthScoreLow(measurement)) {
            if (!deduplicationService.shouldSkipAlert(measurement.getTransformateur(), AlertType.PANNE)) {
                createAndSaveAlert(measurement, AlertLevel.WARNING, "Health Score Low", "Transformer health score is low");
            }
        }

        if (isConsumptionAnomaly(measurement)) {
            if (!deduplicationService.shouldSkipAlert(measurement.getTransformateur(), AlertType.SURCONSOMMATION)) {
                createAndSaveAlert(measurement, AlertLevel.WARNING, "Consumption Anomaly", "Power consumption is anomalous");
            }
        }
    }

    @Override
    public boolean isTemperatureAnomalous(Measurement measurement) {
        return measurement.getTemperature() != null && measurement.getTemperature() > 80.0;
    }

    @Override
    public boolean isHealthScoreLow(Measurement measurement) {
        return measurement.getHealthScore() != null && measurement.getHealthScore() < 60;
    }

    @Override
    public boolean isConsumptionAnomaly(Measurement measurement) {
        return measurement.getPower() != null && measurement.getPower() > 1500.0;
    }

    @Override
    public boolean isOverload(Measurement measurement) {
        return measurement.getCurrent() != null && measurement.getCurrent() > 500.0;
    }

    @Override
    public boolean isVoltageDip(Measurement measurement) {
        return measurement.getVoltage() != null && measurement.getVoltage() < 380.0;
    }

    private void createAndSaveAlert(Measurement measurement, AlertLevel level, String titre, String description) {
        try {
            Alert alert = new Alert();
            alert.setReference("ALERT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            alert.setTitre(titre);
            alert.setDescription(description);
            alert.setType(getAlertType(titre));
            alert.setNiveau(level);
            alert.setStatut(AlertStatus.ACTIVE);
            alert.setTransformateur(measurement.getTransformateur());
            alert.setMeasurement(measurement);
            alert.setCreatedAt(LocalDateTime.now());
            alert.setUpdatedAt(LocalDateTime.now());
            alertRepository.save(alert);
            logger.info("Alert created: {}", titre);
        } catch (Exception e) {
            logger.error("Error creating alert", e);
        }
    }

    private AlertType getAlertType(String titre) {
        if (titre.contains("Temperature")) {
            return AlertType.TEMPERATURE;
        } else if (titre.contains("Voltage")) {
            return AlertType.CHUTE_TENSION;
        } else if (titre.contains("Overload")) {
            return AlertType.SURCHARGE;
        } else if (titre.contains("Consumption")) {
            return AlertType.SURCONSOMMATION;
        } else if (titre.contains("Health")) {
            return AlertType.PANNE;
        }
        return AlertType.AUTRE;
    }
}
