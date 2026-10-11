package com.steg.smartmonitoring.measurement.entity;

/**
 * Enum représentant le statut d'une mesure IoT
 * Basé sur les valeurs mesurées et les seuils définis
 */
public enum MeasurementStatus {
    /**
     * Tous les paramètres sont dans les limites normales
     */
    NORMAL,

    /**
     * Au moins un paramètre est dans la zone d'avertissement
     * Aucun paramètre critique
     */
    WARNING,

    /**
     * Au moins un paramètre est critique
     * Nécessite une attention immédiate
     */
    CRITICAL,

    /**
     * Le capteur/transformateur est hors ligne
     * Pas de données disponibles
     */
    OFFLINE
}
