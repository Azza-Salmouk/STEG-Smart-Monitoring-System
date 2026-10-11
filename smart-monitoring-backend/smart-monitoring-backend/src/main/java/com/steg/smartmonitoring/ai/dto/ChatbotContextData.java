package com.steg.smartmonitoring.ai.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Structured context data to send to Ollama
 * Contains role-filtered data for the LLM to use
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatbotContextData {
    
    // User information
    private String userRole;
    private String userEmail;
    
    // Statistics (generic container for all role types)
    private StatisticsContext statistics;
    
    // Recent items
    private RecentItemsContext recentItems;
    
    // Context metadata
    private String contextSummary;
    private Integer alertsCount;
    private Integer interventionsCount;
    private Integer complaintsCount;
    private Integer measurementsCount;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class StatisticsContext {
        // Alerts
        private Integer totalAlerts;
        private Integer activeAlerts;
        private Integer criticalAlerts;
        private Integer warningAlerts;
        private Integer infoAlerts;
        
        // Interventions
        private Integer totalInterventions;
        private Integer ongoingInterventions;
        private Integer pendingInterventions;
        private Integer completedInterventions;
        private Integer urgentInterventions;
        
        // Complaints
        private Integer totalComplaints;
        private Integer newComplaints;
        private Integer assignedComplaints;
        private Integer inProcessComplaints;
        private Integer resolvedComplaints;
        
        // Measurements
        private Integer totalMeasurements;
        private Integer criticalMeasurements;
        private Integer warningMeasurements;
        private Double averageHealthScore;
        private Integer offlineTransformateurs;
        
        // Infrastructure
        private Integer totalPostes;
        private Integer totalTransformateurs;
        private Integer activeTransformateurs;
        private Integer alertedTransformateurs;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class RecentItemsContext {
        // Recent alerts summary
        private String recentAlertsSummary;
        private Integer recentAlertsCount;
        
        // Recent interventions summary
        private String recentInterventionsSummary;
        private Integer recentInterventionsCount;
        
        // Recent complaints summary
        private String recentComplaintsSummary;
        private Integer recentComplaintsCount;
        
        // Critical situations
        private String criticalSituations;
        private Boolean hasCriticalAlerts;
        private Boolean hasUrgentInterventions;
    }
}
