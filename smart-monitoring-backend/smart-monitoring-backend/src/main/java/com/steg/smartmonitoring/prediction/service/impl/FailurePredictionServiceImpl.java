package com.steg.smartmonitoring.prediction.service.impl;

import com.steg.smartmonitoring.alert.entity.Alert;
import com.steg.smartmonitoring.alert.entity.AlertLevel;
import com.steg.smartmonitoring.alert.entity.AlertStatus;
import com.steg.smartmonitoring.alert.repository.AlertRepository;
import com.steg.smartmonitoring.measurement.entity.Measurement;
import com.steg.smartmonitoring.measurement.repository.MeasurementRepository;
import com.steg.smartmonitoring.prediction.dto.FailurePredictionResult;
import com.steg.smartmonitoring.prediction.service.FailurePredictionService;
import com.steg.smartmonitoring.prediction.service.MaintenanceRecommendation;
import com.steg.smartmonitoring.transformateur.entity.Transformateur;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Failure prediction service implementation using data-driven analysis
 */
@Service
@Transactional(readOnly = true)
public class FailurePredictionServiceImpl implements FailurePredictionService {

    private static final Logger logger = LoggerFactory.getLogger(FailurePredictionServiceImpl.class);

    private final MeasurementRepository measurementRepository;
    private final AlertRepository alertRepository;

    public FailurePredictionServiceImpl(MeasurementRepository measurementRepository, AlertRepository alertRepository) {
        this.measurementRepository = measurementRepository;
        this.alertRepository = alertRepository;
    }

    @Override
    public FailurePredictionResult predictFailure(Transformateur transformateur) {
        if (transformateur == null) {
            return null;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime sevenDaysAgo = now.minusDays(7);
        LocalDateTime thirtyDaysAgo = now.minusDays(30);

        // Get latest measurement using optimized query
        List<Measurement> latestMeasurements = measurementRepository.findLatestMeasurementsOrderedByTransformateurId(transformateur.getId());
        Measurement latestMeasurement = latestMeasurements.isEmpty() ? null : latestMeasurements.get(0);

        if (latestMeasurement == null) {
            return buildDefaultPrediction(transformateur);
        }

        // Get measurements for trend analysis using optimized queries
        List<Measurement> last7Days = measurementRepository.findByTransformateurIdAfterDate(transformateur.getId(), sevenDaysAgo);
        List<Measurement> last30Days = measurementRepository.findByTransformateurIdAfterDate(transformateur.getId(), thirtyDaysAgo);

        // Get active alerts using optimized queries
        List<Alert> activeAlerts = alertRepository.findActiveAlertsByTransformateurId(transformateur.getId(), AlertStatus.ACTIVE);
        
        // Count critical alerts efficiently
        Long criticalAlertCount = alertRepository.countCriticalAlertsByTransformateurId(transformateur.getId(), AlertStatus.ACTIVE, AlertLevel.CRITICAL);

        // Calculate health score trend
        double healthScoreTrend = calculateHealthScoreTrend(last7Days);

        // Calculate temperature trend
        double temperatureTrend = calculateTemperatureTrend(last7Days);

        // Calculate failure risk score
        int failureRiskScore = calculateFailureRiskScore(
            latestMeasurement,
            healthScoreTrend,
            temperatureTrend,
            activeAlerts.size(),
            criticalAlertCount.intValue()
        );

        // Determine risk level
        FailurePredictionResult.RiskLevel riskLevel = determineRiskLevel(failureRiskScore);

        // Estimate failure date
        LocalDateTime estimatedFailureDate = estimateFailureDate(failureRiskScore, last7Days);

        // Build explanation
        String explanation = buildExplanation(latestMeasurement, healthScoreTrend, temperatureTrend, activeAlerts.size());

        // Get recommendations
        MaintenanceRecommendation recommendation = getRecommendations(transformateur);

        return FailurePredictionResult.builder()
            .transformateurId(transformateur.getId())
            .failureRiskScore(failureRiskScore)
            .riskLevel(riskLevel)
            .estimatedFailureDate(estimatedFailureDate)
            .explanation(explanation)
            .currentHealthScore(latestMeasurement.getHealthScore())
            .healthScoreTrend(healthScoreTrend)
            .activeCriticalAlerts(criticalAlertCount.intValue())
            .totalAlertsLast7Days(alertRepository.findAlertsByTransformateurIdAfterDate(transformateur.getId(), sevenDaysAgo).size())
            .currentTemperature(latestMeasurement.getTemperature())
            .averageTemperatureLast7Days(calculateAverageTemperature(last7Days))
            .temperatureTrend(temperatureTrend)
            .hasVoltageInstability(hasVoltageInstability(last7Days))
            .primaryRecommendation(recommendation.getRecommendation())
            .secondaryRecommendation(recommendation.getCause())
            .priorityLevel(recommendation.getPriorityLevel())
            .build();
    }

    @Override
    public MaintenanceRecommendation getRecommendations(Transformateur transformateur) {
        if (transformateur == null) {
            return null;
        }

        List<Measurement> latestMeasurements = measurementRepository.findLatestMeasurementsOrderedByTransformateurId(transformateur.getId());
        Measurement latest = latestMeasurements.isEmpty() ? null : latestMeasurements.get(0);

        if (latest == null) {
            return buildDefaultRecommendation(transformateur);
        }

        // Analyze conditions and recommend
        if (latest.getTemperature() != null && latest.getTemperature() > 80) {
            return MaintenanceRecommendation.builder()
                .transformateurId(transformateur.getId())
                .maintenanceType(MaintenanceRecommendation.MaintenanceType.COOLING_SYSTEM)
                .cause("Temperature exceeds safe operating level (80°C)")
                .recommendation("Inspect and clean cooling system, verify ventilation")
                .priorityLevel(1) // CRITICAL
                .estimatedDuration("2-4 hours")
                .timeline("Within 24-48 hours")
                .build();
        }

        if (latest.getCurrent() != null && latest.getCurrent() > 500) {
            return MaintenanceRecommendation.builder()
                .transformateurId(transformateur.getId())
                .maintenanceType(MaintenanceRecommendation.MaintenanceType.LOAD_REDISTRIBUTION)
                .cause("Current exceeds rated capacity (500A)")
                .recommendation("Redistribute load to other transformers or upgrade capacity")
                .priorityLevel(1)
                .estimatedDuration("4-8 hours")
                .timeline("Within 48-72 hours")
                .build();
        }

        if (latest.getVoltage() != null && latest.getVoltage() < 380) {
            return MaintenanceRecommendation.builder()
                .transformateurId(transformateur.getId())
                .maintenanceType(MaintenanceRecommendation.MaintenanceType.ELECTRICAL_CONNECTION)
                .cause("Voltage below minimum threshold (380V)")
                .recommendation("Check electrical connections, verify supply voltage, clean terminals")
                .priorityLevel(2) // HIGH
                .estimatedDuration("1-2 hours")
                .timeline("Within 24 hours")
                .build();
        }

        if (latest.getHealthScore() != null && latest.getHealthScore() < 60) {
            return MaintenanceRecommendation.builder()
                .transformateurId(transformateur.getId())
                .maintenanceType(MaintenanceRecommendation.MaintenanceType.PREVENTIVE_MAINTENANCE)
                .cause("Overall health score deteriorating")
                .recommendation("Schedule comprehensive preventive maintenance inspection")
                .priorityLevel(2)
                .estimatedDuration("3-6 hours")
                .timeline("Within 1 week")
                .build();
        }

        // Default recommendation - increase monitoring
        return MaintenanceRecommendation.builder()
            .transformateurId(transformateur.getId())
            .maintenanceType(MaintenanceRecommendation.MaintenanceType.MONITORING_UPGRADE)
            .cause("Routine maintenance schedule")
            .recommendation("Increase monitoring frequency and conduct routine inspections")
            .priorityLevel(4) // LOW
            .estimatedDuration("30 minutes")
            .timeline("Routine")
            .build();
    }

    private int calculateFailureRiskScore(Measurement latest, double healthScoreTrend, double tempTrend, int alertCount, int criticalAlertCount) {
        int score = 0;

        // Health score impact (0-30 points)
        if (latest.getHealthScore() != null) {
            score += Math.max(0, 30 - (latest.getHealthScore() / 3));
        }

        // Health score trend (0-20 points)
        if (healthScoreTrend < -10) {
            score += 20; // Rapidly declining
        } else if (healthScoreTrend < -5) {
            score += 15;
        } else if (healthScoreTrend < 0) {
            score += 10;
        }

        // Temperature impact (0-20 points)
        if (latest.getTemperature() != null) {
            if (latest.getTemperature() > 90) {
                score += 20;
            } else if (latest.getTemperature() > 80) {
                score += 15;
            } else if (latest.getTemperature() > 70) {
                score += 5;
            }
        }

        // Temperature trend (0-15 points)
        if (tempTrend > 15) {
            score += 15; // Rapidly rising
        } else if (tempTrend > 5) {
            score += 10;
        }

        // Alert count impact (0-15 points)
        score += Math.min(15, criticalAlertCount * 5 + (alertCount / 10));

        return Math.min(100, score);
    }

    private FailurePredictionResult.RiskLevel determineRiskLevel(int score) {
        if (score >= 80) return FailurePredictionResult.RiskLevel.CRITICAL;
        if (score >= 60) return FailurePredictionResult.RiskLevel.HIGH;
        if (score >= 40) return FailurePredictionResult.RiskLevel.MEDIUM;
        return FailurePredictionResult.RiskLevel.LOW;
    }

    private LocalDateTime estimateFailureDate(int riskScore, List<Measurement> last7Days) {
        if (riskScore < 40) return null;

        int daysUntilFailure;
        if (riskScore >= 80) {
            daysUntilFailure = 1;
        } else if (riskScore >= 60) {
            daysUntilFailure = 7;
        } else if (riskScore >= 40) {
            daysUntilFailure = 30;
        } else {
            return null;
        }

        return LocalDateTime.now().plusDays(daysUntilFailure);
    }

    private double calculateHealthScoreTrend(List<Measurement> measurements) {
        if (measurements.size() < 2) return 0.0;

        double first = measurements.stream()
            .filter(m -> m.getHealthScore() != null)
            .mapToDouble(Measurement::getHealthScore)
            .average()
            .orElse(0.0);

        if (first == 0) return 0.0;

        List<Measurement> recent = measurements.stream()
            .sorted((m1, m2) -> m2.getMeasurementDate().compareTo(m1.getMeasurementDate()))
            .limit(measurements.size() / 2)
            .collect(Collectors.toList());

        double second = recent.stream()
            .filter(m -> m.getHealthScore() != null)
            .mapToDouble(Measurement::getHealthScore)
            .average()
            .orElse(first);

        return ((second - first) / first) * 100;
    }

    private double calculateTemperatureTrend(List<Measurement> measurements) {
        if (measurements.size() < 2) return 0.0;

        double first = measurements.stream()
            .filter(m -> m.getTemperature() != null)
            .mapToDouble(Measurement::getTemperature)
            .average()
            .orElse(0.0);

        if (first == 0) return 0.0;

        List<Measurement> recent = measurements.stream()
            .sorted((m1, m2) -> m2.getMeasurementDate().compareTo(m1.getMeasurementDate()))
            .limit(measurements.size() / 2)
            .collect(Collectors.toList());

        double second = recent.stream()
            .filter(m -> m.getTemperature() != null)
            .mapToDouble(Measurement::getTemperature)
            .average()
            .orElse(first);

        return ((second - first) / first) * 100;
    }

    private double calculateAverageTemperature(List<Measurement> measurements) {
        return measurements.stream()
            .filter(m -> m.getTemperature() != null)
            .mapToDouble(Measurement::getTemperature)
            .average()
            .orElse(0.0);
    }

    private boolean hasVoltageInstability(List<Measurement> measurements) {
        if (measurements.isEmpty()) return false;

        double avg = measurements.stream()
            .filter(m -> m.getVoltage() != null)
            .mapToDouble(Measurement::getVoltage)
            .average()
            .orElse(400.0);

        return measurements.stream()
            .filter(m -> m.getVoltage() != null)
            .anyMatch(m -> Math.abs(m.getVoltage() - avg) > 50);
    }

    private String buildExplanation(Measurement latest, double healthTrend, double tempTrend, int alertCount) {
        StringBuilder sb = new StringBuilder();

        if (latest.getTemperature() != null && latest.getTemperature() > 80) {
            sb.append(String.format("Temperature is elevated at %.1f°C. ", latest.getTemperature()));
        }

        if (tempTrend > 10) {
            sb.append(String.format("Temperature is rising (+%.1f%% over 7 days). ", tempTrend));
        }

        if (healthTrend < -10) {
            sb.append(String.format("Health score is declining (-%.1f%% over 7 days). ", Math.abs(healthTrend)));
        }

        if (alertCount > 5) {
            sb.append(String.format("%d active alerts detected. ", alertCount));
        }

        if (sb.length() == 0) {
            sb.append("Transformer is operating normally with no significant anomalies detected.");
        }

        return sb.toString();
    }

    private FailurePredictionResult buildDefaultPrediction(Transformateur transformateur) {
        return FailurePredictionResult.builder()
            .transformateurId(transformateur.getId())
            .failureRiskScore(0)
            .riskLevel(FailurePredictionResult.RiskLevel.LOW)
            .explanation("Insufficient data for prediction")
            .build();
    }

    private MaintenanceRecommendation buildDefaultRecommendation(Transformateur transformateur) {
        return MaintenanceRecommendation.builder()
            .transformateurId(transformateur.getId())
            .maintenanceType(MaintenanceRecommendation.MaintenanceType.MONITORING_UPGRADE)
            .cause("Routine schedule")
            .recommendation("Continue regular monitoring")
            .priorityLevel(4)
            .timeline("Routine")
            .build();
    }
}
