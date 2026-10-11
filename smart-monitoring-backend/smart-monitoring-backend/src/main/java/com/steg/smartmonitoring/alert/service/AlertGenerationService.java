package com.steg.smartmonitoring.alert.service;

import com.steg.smartmonitoring.measurement.entity.Measurement;

/**
 * Service pour la génération automatique des alertes
 * À partir des mesures IoT
 */
public interface AlertGenerationService {

    /**
     * Analyser une mesure et générer les alertes appropriées
     * @param measurement la mesure à analyser
     */
    void generateAlertsFromMeasurement(Measurement measurement);

    /**
     * Vérifier si la température dépasse le seuil
     */
    boolean isTemperatureAnomalous(Measurement measurement);

    /**
     * Vérifier si le score de santé est faible
     */
    boolean isHealthScoreLow(Measurement measurement);

    /**
     * Vérifier s'il y a une anomalie de consommation
     */
    boolean isConsumptionAnomaly(Measurement measurement);

    /**
     * Vérifier s'il y a une surcharge
     */
    boolean isOverload(Measurement measurement);

    /**
     * Vérifier s'il y a une chute de tension
     */
    boolean isVoltageDip(Measurement measurement);
}
