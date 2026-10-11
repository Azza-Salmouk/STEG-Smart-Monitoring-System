package com.steg.smartmonitoring.prediction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Result of failure prediction analysis
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FailurePredictionResult {

    private Long transformateurId;

    // Risk score 0-100
    private Integer failureRiskScore;

    // Risk level
    private RiskLevel riskLevel;

    // Estimated failure date
    private LocalDateTime estimatedFailureDate;

    // Factors contributing to risk
    private String explanation;

    // Current health metrics
    private Integer currentHealthScore;
    private Double healthScoreTrend; // percentage change over 7 days
    private Integer activeCriticalAlerts;
    private Integer totalAlertsLast7Days;

    // Temperature analysis
    private Double currentTemperature;
    private Double averageTemperatureLast7Days;
    private Double temperatureTrend; // percentage change

    // Voltage analysis
    private Double averageVoltage;
    private Boolean hasVoltageInstability;

    // Recommended actions
    private String primaryRecommendation;
    private String secondaryRecommendation;
    private Integer priorityLevel; // 1=CRITICAL, 2=HIGH, 3=MEDIUM, 4=LOW

    /**
     * Risk levels
     */
    public enum RiskLevel {
        LOW("No immediate action needed"),
        MEDIUM("Monitor closely"),
        HIGH("Schedule maintenance soon"),
        CRITICAL("Immediate intervention required");

        public final String description;

        RiskLevel(String description) {
            this.description = description;
        }
    }
}
