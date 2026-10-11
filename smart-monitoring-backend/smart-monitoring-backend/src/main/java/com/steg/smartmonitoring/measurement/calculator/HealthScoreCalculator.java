package com.steg.smartmonitoring.measurement.calculator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Composant responsable du calcul du Health Score d'un transformateur
 * en fonction des paramètres de mesure.
 * 
 * Le Health Score évolue de 0 à 100 en fonction de :
 * - Température
 * - Tension
 * - Courant
 * - Fréquence
 * - Consommation d'énergie
 * 
 * Cette classe est conçue pour être facilement modifiable
 * sans impacter le reste du système.
 */
@Component
public class HealthScoreCalculator {

    private static final Logger log = LoggerFactory.getLogger(HealthScoreCalculator.class);

    // Seuils de température (en °C)
    private static final double TEMP_OPTIMAL = 50;
    private static final double TEMP_WARNING = 70;
    private static final double TEMP_CRITICAL = 85;

    // Seuils de tension (en volts)
    // Tolérance nominale: ±10%
    private static final double VOLTAGE_OPTIMAL_MIN = 81000;  // -10% de 90kV
    private static final double VOLTAGE_OPTIMAL_MAX = 99000;  // +10% de 90kV
    private static final double VOLTAGE_WARNING_MIN = 72000;  // -20%
    private static final double VOLTAGE_WARNING_MAX = 108000; // +20%

    // Seuils de courant (en ampères)
    // Basé sur la puissance nominale
    private static final double CURRENT_OPTIMAL = 200;  // 50% de la capacité
    private static final double CURRENT_WARNING = 350;  // 87.5% de capacité
    private static final double CURRENT_CRITICAL = 400; // Au-delà de la capacité

    // Seuils de fréquence (en Hz)
    // Nominale: 50Hz ±1Hz
    private static final double FREQUENCY_OPTIMAL_MIN = 49.5;
    private static final double FREQUENCY_OPTIMAL_MAX = 50.5;
    private static final double FREQUENCY_WARNING_MIN = 48.5;
    private static final double FREQUENCY_WARNING_MAX = 51.5;

    // Pondération des facteurs
    private static final double WEIGHT_TEMPERATURE = 0.30;   // 30%
    private static final double WEIGHT_VOLTAGE = 0.25;       // 25%
    private static final double WEIGHT_CURRENT = 0.25;       // 25%
    private static final double WEIGHT_FREQUENCY = 0.20;     // 20%

    /**
     * Calcule le Health Score en fonction des paramètres de mesure
     * 
     * @param temperature Température en °C
     * @param voltage Tension en volts
     * @param current Courant en ampères
     * @param frequency Fréquence en Hz
     * @return Health Score entre 0 et 100
     */
    public Integer calculateHealthScore(
            Double temperature,
            Double voltage,
            Double current,
            Double frequency) {

        // Scores partiels pour chaque paramètre (0-100)
        double temperatureScore = calculateTemperatureScore(temperature);
        double voltageScore = calculateVoltageScore(voltage);
        double currentScore = calculateCurrentScore(current);
        double frequencyScore = calculateFrequencyScore(frequency);

        // Score pondéré global
        double healthScore = 
            (temperatureScore * WEIGHT_TEMPERATURE) +
            (voltageScore * WEIGHT_VOLTAGE) +
            (currentScore * WEIGHT_CURRENT) +
            (frequencyScore * WEIGHT_FREQUENCY);

        // Arrondir et limiter entre 0 et 100
        int finalScore = Math.max(0, Math.min(100, (int) Math.round(healthScore)));

        log.debug("Health Score calculé: {} (Temp: {}, Volt: {}, Curr: {}, Freq: {})",
            finalScore, temperatureScore, voltageScore, currentScore, frequencyScore);

        return finalScore;
    }

    /**
     * Calcule le score de température (0-100)
     * 
     * 100 = température optimale (~50°C)
     * 75 = température acceptable (~70°C)
     * 25 = température critique (~85°C)
     * 0 = hors limites
     */
    private double calculateTemperatureScore(Double temperature) {
        if (temperature == null) return 50; // Score par défaut

        if (temperature <= TEMP_OPTIMAL) {
            // Optimal ou froid: score complet
            return 100;
        } else if (temperature <= TEMP_WARNING) {
            // Entre optimal et warning: dégradation progressive
            double ratio = (temperature - TEMP_OPTIMAL) / (TEMP_WARNING - TEMP_OPTIMAL);
            return 100 - (ratio * 25); // De 100 à 75
        } else if (temperature <= TEMP_CRITICAL) {
            // Entre warning et critique: forte dégradation
            double ratio = (temperature - TEMP_WARNING) / (TEMP_CRITICAL - TEMP_WARNING);
            return 75 - (ratio * 50); // De 75 à 25
        } else {
            // Au-delà du critique: très critique
            return Math.max(0, 25 - (temperature - TEMP_CRITICAL));
        }
    }

    /**
     * Calcule le score de tension (0-100)
     * 
     * 100 = tension dans les limites normales
     * 50 = tension en zone d'avertissement
     * 0 = tension hors limites
     */
    private double calculateVoltageScore(Double voltage) {
        if (voltage == null) return 50;

        // Vérifier si dans la zone optimale
        if (voltage >= VOLTAGE_OPTIMAL_MIN && voltage <= VOLTAGE_OPTIMAL_MAX) {
            return 100;
        }

        // Vérifier si dans la zone d'avertissement
        if (voltage >= VOLTAGE_WARNING_MIN && voltage <= VOLTAGE_WARNING_MAX) {
            return 50;
        }

        // Hors limites
        return 0;
    }

    /**
     * Calcule le score de courant (0-100)
     * 
     * 100 = courant optimal (< 50% capacité)
     * 75 = courant bon (50-75% capacité)
     * 50 = courant acceptable (75-87.5% capacité)
     * 25 = courant critique (87.5-100% capacité)
     * 0 = surcharge
     */
    private double calculateCurrentScore(Double current) {
        if (current == null) return 100;

        if (current <= CURRENT_OPTIMAL) {
            return 100;
        } else if (current <= (CURRENT_OPTIMAL + CURRENT_WARNING) / 2) {
            // 200-275A: bon
            double ratio = (current - CURRENT_OPTIMAL) / ((CURRENT_WARNING + CURRENT_OPTIMAL) / 2 - CURRENT_OPTIMAL);
            return 100 - (ratio * 25); // De 100 à 75
        } else if (current <= CURRENT_WARNING) {
            // 275-350A: acceptable
            double ratio = (current - (CURRENT_OPTIMAL + CURRENT_WARNING) / 2) / (CURRENT_WARNING - (CURRENT_OPTIMAL + CURRENT_WARNING) / 2);
            return 75 - (ratio * 25); // De 75 à 50
        } else if (current <= CURRENT_CRITICAL) {
            // 350-400A: critique
            double ratio = (current - CURRENT_WARNING) / (CURRENT_CRITICAL - CURRENT_WARNING);
            return 50 - (ratio * 25); // De 50 à 25
        } else {
            // > 400A: surcharge
            return Math.max(0, 25 - (current - CURRENT_CRITICAL) * 5);
        }
    }

    /**
     * Calcule le score de fréquence (0-100)
     * 
     * 100 = fréquence optimale (49.5-50.5 Hz)
     * 50 = fréquence acceptable (48.5-51.5 Hz)
     * 0 = fréquence hors limites
     */
    private double calculateFrequencyScore(Double frequency) {
        if (frequency == null) return 100;

        // Zone optimale: ±0.5Hz autour de 50Hz
        if (frequency >= FREQUENCY_OPTIMAL_MIN && frequency <= FREQUENCY_OPTIMAL_MAX) {
            return 100;
        }

        // Zone acceptable: ±1.5Hz
        if (frequency >= FREQUENCY_WARNING_MIN && frequency <= FREQUENCY_WARNING_MAX) {
            return 50;
        }

        // Hors limites
        return 0;
    }

    /**
     * Simule l'évolution du Health Score avec le temps et les conditions
     * Utile pour le système de simulation
     * 
     * @param currentScore Score actuel
     * @param temperature Température actuelle
     * @return Nouveau score après évolution
     */
    public Integer simulateHealthScoreDegradation(Integer currentScore, Double temperature) {
        if (currentScore == null || temperature == null) {
            return currentScore;
        }

        // Le Health Score se dégrade lentement si température élevée
        if (temperature > TEMP_WARNING) {
            double degradationRate = (temperature - TEMP_WARNING) / (TEMP_CRITICAL - TEMP_WARNING) * 2;
            int newScore = currentScore - (int) degradationRate;
            return Math.max(0, newScore);
        }

        // Le Health Score s'améliore lentement si température basse
        if (temperature < TEMP_OPTIMAL) {
            int newScore = currentScore + 1;
            return Math.min(100, newScore);
        }

        // Pas de changement si dans la zone optimale
        return currentScore;
    }
}
