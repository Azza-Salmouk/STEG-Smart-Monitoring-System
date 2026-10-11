package com.steg.smartmonitoring.measurement.provider;

import com.steg.smartmonitoring.measurement.dto.CreateMeasurementRequest;

/**
 * Interface pour les fournisseurs de mesures IoT
 * 
 * Cette interface permet de découpler la source de données des mesures.
 * Implémentations possibles :
 * - SimulationMeasurementProvider : Génère des données simulées (simulation)
 * - ESP32MeasurementProvider : Reçoit les données d'un ESP32 réel (futur)
 * - MQTTMeasurementProvider : Reçoit les données via MQTT (futur)
 * 
 * Cette approche garantit que la logique métier ne dépend pas
 * de la source de données spécifique.
 */
public interface MeasurementProvider {

    /**
     * Génère une nouvelle mesure
     * 
     * @param transformateurId ID du transformateur pour lequel générer une mesure
     * @return Mesure générée prête à être persistée
     */
    CreateMeasurementRequest generateMeasurement(Long transformateurId);

    /**
     * Retourne le nom du fournisseur
     * 
     * @return Nom du fournisseur (ex: "SIMULATION", "ESP32", "MQTT")
     */
    String getProviderName();

    /**
     * Indique si ce fournisseur est disponible et prêt à générer des mesures
     * 
     * @return true si le fournisseur est actif
     */
    boolean isAvailable();
}
