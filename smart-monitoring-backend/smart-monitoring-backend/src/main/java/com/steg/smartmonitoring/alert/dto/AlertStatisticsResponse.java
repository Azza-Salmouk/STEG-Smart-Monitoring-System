package com.steg.smartmonitoring.alert.dto;

/**
 * DTO pour les statistiques des alertes
 */
public class AlertStatisticsResponse {

    private Long totalAlerts;

    private Long activeAlerts;

    private Long criticalAlerts;

    private Long resolvedAlerts;

    private Long acknowledgedAlerts;

    private Long archivedAlerts;

    // Par niveau
    private Long infoCount;

    private Long warningCount;

    private Long highCount;

    private Long criticalCount;

    // Par type
    private Long temperatureCount;

    private Long overloadCount;

    private Long overconsumptionCount;

    private Long breakdownCount;

    private Long voltageDipCount;

    private Long shortCircuitCount;

    private Long fireCount;

    private Long communicationCount;

    private Long otherCount;

    // Métriques
    private Double averageResolutionTimeHours;

    private Double resolutionRate;

    private Double acknowledgmentRate;

    public AlertStatisticsResponse() {
    }

    public AlertStatisticsResponse(Long totalAlerts, Long activeAlerts, Long criticalAlerts, Long resolvedAlerts, Long acknowledgedAlerts, Long archivedAlerts, Long infoCount, Long warningCount, Long highCount, Long criticalCount, Long temperatureCount, Long overloadCount, Long overconsumptionCount, Long breakdownCount, Long voltageDipCount, Long shortCircuitCount, Long fireCount, Long communicationCount, Long otherCount, Double averageResolutionTimeHours, Double resolutionRate, Double acknowledgmentRate) {
        this.totalAlerts = totalAlerts;
        this.activeAlerts = activeAlerts;
        this.criticalAlerts = criticalAlerts;
        this.resolvedAlerts = resolvedAlerts;
        this.acknowledgedAlerts = acknowledgedAlerts;
        this.archivedAlerts = archivedAlerts;
        this.infoCount = infoCount;
        this.warningCount = warningCount;
        this.highCount = highCount;
        this.criticalCount = criticalCount;
        this.temperatureCount = temperatureCount;
        this.overloadCount = overloadCount;
        this.overconsumptionCount = overconsumptionCount;
        this.breakdownCount = breakdownCount;
        this.voltageDipCount = voltageDipCount;
        this.shortCircuitCount = shortCircuitCount;
        this.fireCount = fireCount;
        this.communicationCount = communicationCount;
        this.otherCount = otherCount;
        this.averageResolutionTimeHours = averageResolutionTimeHours;
        this.resolutionRate = resolutionRate;
        this.acknowledgmentRate = acknowledgmentRate;
    }

    public Long getTotalAlerts() {
        return totalAlerts;
    }

    public void setTotalAlerts(Long totalAlerts) {
        this.totalAlerts = totalAlerts;
    }

    public Long getActiveAlerts() {
        return activeAlerts;
    }

    public void setActiveAlerts(Long activeAlerts) {
        this.activeAlerts = activeAlerts;
    }

    public Long getCriticalAlerts() {
        return criticalAlerts;
    }

    public void setCriticalAlerts(Long criticalAlerts) {
        this.criticalAlerts = criticalAlerts;
    }

    public Long getResolvedAlerts() {
        return resolvedAlerts;
    }

    public void setResolvedAlerts(Long resolvedAlerts) {
        this.resolvedAlerts = resolvedAlerts;
    }

    public Long getAcknowledgedAlerts() {
        return acknowledgedAlerts;
    }

    public void setAcknowledgedAlerts(Long acknowledgedAlerts) {
        this.acknowledgedAlerts = acknowledgedAlerts;
    }

    public Long getArchivedAlerts() {
        return archivedAlerts;
    }

    public void setArchivedAlerts(Long archivedAlerts) {
        this.archivedAlerts = archivedAlerts;
    }

    public Long getInfoCount() {
        return infoCount;
    }

    public void setInfoCount(Long infoCount) {
        this.infoCount = infoCount;
    }

    public Long getWarningCount() {
        return warningCount;
    }

    public void setWarningCount(Long warningCount) {
        this.warningCount = warningCount;
    }

    public Long getHighCount() {
        return highCount;
    }

    public void setHighCount(Long highCount) {
        this.highCount = highCount;
    }

    public Long getCriticalCount() {
        return criticalCount;
    }

    public void setCriticalCount(Long criticalCount) {
        this.criticalCount = criticalCount;
    }

    public Long getTemperatureCount() {
        return temperatureCount;
    }

    public void setTemperatureCount(Long temperatureCount) {
        this.temperatureCount = temperatureCount;
    }

    public Long getOverloadCount() {
        return overloadCount;
    }

    public void setOverloadCount(Long overloadCount) {
        this.overloadCount = overloadCount;
    }

    public Long getOverconsumptionCount() {
        return overconsumptionCount;
    }

    public void setOverconsumptionCount(Long overconsumptionCount) {
        this.overconsumptionCount = overconsumptionCount;
    }

    public Long getBreakdownCount() {
        return breakdownCount;
    }

    public void setBreakdownCount(Long breakdownCount) {
        this.breakdownCount = breakdownCount;
    }

    public Long getVoltageDipCount() {
        return voltageDipCount;
    }

    public void setVoltageDipCount(Long voltageDipCount) {
        this.voltageDipCount = voltageDipCount;
    }

    public Long getShortCircuitCount() {
        return shortCircuitCount;
    }

    public void setShortCircuitCount(Long shortCircuitCount) {
        this.shortCircuitCount = shortCircuitCount;
    }

    public Long getFireCount() {
        return fireCount;
    }

    public void setFireCount(Long fireCount) {
        this.fireCount = fireCount;
    }

    public Long getCommunicationCount() {
        return communicationCount;
    }

    public void setCommunicationCount(Long communicationCount) {
        this.communicationCount = communicationCount;
    }

    public Long getOtherCount() {
        return otherCount;
    }

    public void setOtherCount(Long otherCount) {
        this.otherCount = otherCount;
    }

    public Double getAverageResolutionTimeHours() {
        return averageResolutionTimeHours;
    }

    public void setAverageResolutionTimeHours(Double averageResolutionTimeHours) {
        this.averageResolutionTimeHours = averageResolutionTimeHours;
    }

    public Double getResolutionRate() {
        return resolutionRate;
    }

    public void setResolutionRate(Double resolutionRate) {
        this.resolutionRate = resolutionRate;
    }

    public Double getAcknowledgmentRate() {
        return acknowledgmentRate;
    }

    public void setAcknowledgmentRate(Double acknowledgmentRate) {
        this.acknowledgmentRate = acknowledgmentRate;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long totalAlerts;
        private Long activeAlerts;
        private Long criticalAlerts;
        private Long resolvedAlerts;
        private Long acknowledgedAlerts;
        private Long archivedAlerts;
        private Long infoCount;
        private Long warningCount;
        private Long highCount;
        private Long criticalCount;
        private Long temperatureCount;
        private Long overloadCount;
        private Long overconsumptionCount;
        private Long breakdownCount;
        private Long voltageDipCount;
        private Long shortCircuitCount;
        private Long fireCount;
        private Long communicationCount;
        private Long otherCount;
        private Double averageResolutionTimeHours;
        private Double resolutionRate;
        private Double acknowledgmentRate;

        public Builder totalAlerts(Long totalAlerts) {
            this.totalAlerts = totalAlerts;
            return this;
        }

        public Builder activeAlerts(Long activeAlerts) {
            this.activeAlerts = activeAlerts;
            return this;
        }

        public Builder criticalAlerts(Long criticalAlerts) {
            this.criticalAlerts = criticalAlerts;
            return this;
        }

        public Builder resolvedAlerts(Long resolvedAlerts) {
            this.resolvedAlerts = resolvedAlerts;
            return this;
        }

        public Builder acknowledgedAlerts(Long acknowledgedAlerts) {
            this.acknowledgedAlerts = acknowledgedAlerts;
            return this;
        }

        public Builder archivedAlerts(Long archivedAlerts) {
            this.archivedAlerts = archivedAlerts;
            return this;
        }

        public Builder infoCount(Long infoCount) {
            this.infoCount = infoCount;
            return this;
        }

        public Builder warningCount(Long warningCount) {
            this.warningCount = warningCount;
            return this;
        }

        public Builder highCount(Long highCount) {
            this.highCount = highCount;
            return this;
        }

        public Builder criticalCount(Long criticalCount) {
            this.criticalCount = criticalCount;
            return this;
        }

        public Builder temperatureCount(Long temperatureCount) {
            this.temperatureCount = temperatureCount;
            return this;
        }

        public Builder overloadCount(Long overloadCount) {
            this.overloadCount = overloadCount;
            return this;
        }

        public Builder overconsumptionCount(Long overconsumptionCount) {
            this.overconsumptionCount = overconsumptionCount;
            return this;
        }

        public Builder breakdownCount(Long breakdownCount) {
            this.breakdownCount = breakdownCount;
            return this;
        }

        public Builder voltageDipCount(Long voltageDipCount) {
            this.voltageDipCount = voltageDipCount;
            return this;
        }

        public Builder shortCircuitCount(Long shortCircuitCount) {
            this.shortCircuitCount = shortCircuitCount;
            return this;
        }

        public Builder fireCount(Long fireCount) {
            this.fireCount = fireCount;
            return this;
        }

        public Builder communicationCount(Long communicationCount) {
            this.communicationCount = communicationCount;
            return this;
        }

        public Builder otherCount(Long otherCount) {
            this.otherCount = otherCount;
            return this;
        }

        public Builder averageResolutionTimeHours(Double averageResolutionTimeHours) {
            this.averageResolutionTimeHours = averageResolutionTimeHours;
            return this;
        }

        public Builder resolutionRate(Double resolutionRate) {
            this.resolutionRate = resolutionRate;
            return this;
        }

        public Builder acknowledgmentRate(Double acknowledgmentRate) {
            this.acknowledgmentRate = acknowledgmentRate;
            return this;
        }

        public AlertStatisticsResponse build() {
            return new AlertStatisticsResponse(totalAlerts, activeAlerts, criticalAlerts, resolvedAlerts, acknowledgedAlerts, archivedAlerts, infoCount, warningCount, highCount, criticalCount, temperatureCount, overloadCount, overconsumptionCount, breakdownCount, voltageDipCount, shortCircuitCount, fireCount, communicationCount, otherCount, averageResolutionTimeHours, resolutionRate, acknowledgmentRate);
        }
    }
}
