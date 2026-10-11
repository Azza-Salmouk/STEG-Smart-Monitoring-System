package com.steg.smartmonitoring.prediction.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Maintenance recommendation for a transformateur
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceRecommendation {

    private Long transformateurId;

    // Type of maintenance needed
    private MaintenanceType maintenanceType;

    // Cause of recommendation
    private String cause;

    // Specific recommendation
    private String recommendation;

    // Priority level (1=CRITICAL, 2=HIGH, 3=MEDIUM, 4=LOW)
    private Integer priorityLevel;

    // Estimated time to complete maintenance
    private String estimatedDuration;

    // When it should be done
    private String timeline;

    public enum MaintenanceType {
        COOLING_SYSTEM("Cooling system inspection"),
        ELECTRICAL_CONNECTION("Electrical connection check"),
        LOAD_REDISTRIBUTION("Load redistribution"),
        PREVENTIVE_MAINTENANCE("Preventive maintenance"),
        EMERGENCY_REPAIR("Emergency repair"),
        MONITORING_UPGRADE("Increase monitoring frequency"),
        CLEANING("Equipment cleaning");

        public final String description;

        MaintenanceType(String description) {
            this.description = description;
        }
    }
}
