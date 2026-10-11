package com.steg.smartmonitoring.intervention.entity;

/**
 * Énumération des types d'intervention
 */
public enum TypeIntervention {
    PREVENTIVE("Préventive"),
    CORRECTIVE("Corrective"),
    URGENTE("Urgente"),
    INSPECTION("Inspection");

    private final String label;

    TypeIntervention(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
