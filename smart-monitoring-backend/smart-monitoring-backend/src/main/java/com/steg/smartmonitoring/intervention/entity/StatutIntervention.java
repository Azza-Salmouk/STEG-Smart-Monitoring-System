package com.steg.smartmonitoring.intervention.entity;

/**
 * Énumération des statuts d'intervention
 */
public enum StatutIntervention {
    EN_ATTENTE("En attente"),
    PLANIFIEE("Planifiée"),
    EN_COURS("En cours"),
    TERMINEE("Terminée"),
    VALIDEE("Validée"),
    ANNULEE("Annulée");

    private final String label;

    StatutIntervention(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
