package com.steg.smartmonitoring.measurement.analyzer;

import com.steg.smartmonitoring.measurement.entity.MeasurementStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Composant responsable de l'analyse et de la détermination du statut
 * d'une mesure IoT en fonction de ses paramètres et du Health Score.
 * 
 * Le statut est déterminé selon des règles métier précises :
 * - NORMAL : tous paramètres ok, health score bon
 * - WARNING : au moins un paramètre en zone d'avertissement
 * - CRITICAL : au moins un paramètre critique ou health score bas
 * - OFFLINE : transformateur hors ligne
 * 
 * Cette classe est indépendante et facilement testable.
 */
@Component
public class MeasurementAnalyzer {

    private static final Logger log = LoggerFactory.getLogger(MeasurementAnalyzer.class);

    // Seuils de température (en °C)
    private static final double TEMP_WARNING_THRESHOLD = 70;
    private static final double TEMP_CRITICAL_THRESHOLD = 85;

    // Seuils de tension (en volts)
    private static final double VOLTAGE_MIN_ACCEPTABLE = 72000;   // -20%
    private static final double VOLTAGE_MAX_ACCEPTABLE = 108000;  // +20%
    private static final double VOLTAGE_MIN_WARNING = 81000;      // -10%
    private static final double VOLTAGE_MAX_WARNING = 99000;      // +10%

    // Seuils de courant (en ampères)
    private static final double CURRENT_WARNING = 350;
    private static final double CURRENT_CRITICAL = 400;

    // Seuils de fréquence (en Hz)
    private static final double FREQUENCY_WARNING_MIN = 48.5;
    private static final double FREQUENCY_WARNING_MAX = 51.5;
    private static final double FREQUENCY_CRITICAL_MIN = 47.0;
    private static final double FREQUENCY_CRITICAL_MAX = 53.0;

    // Seuils de Health Score
    private static final int HEALTH_SCORE_WARNING = 50;
    private static final int HEALTH_SCORE_CRITICAL = 30;

    /**
     * Détermine le statut d'une mesure en fonction de ses paramètres
     * et du Health Score.
     * 
     * Règles de priorité :
     * 1. Paramètre OFFLINE → OFFLINE
     * 2. Au moins un paramètre CRITICAL → CRITICAL
     * 3. Health Score CRITICAL → CRITICAL
     * 4. Au moins un paramètre WARNING → WARNING
     * 5. Health Score WARNING → WARNING
     * 6. Sinon → NORMAL
     * 
     * @param temperature Température en °C
     * @param voltage Tension en volts
     * @param current Courant en ampères
     * @param frequency Fréquence en Hz
     * @param healthScore Health Score (0-100)
     * @return MeasurementStatus (NORMAL, WARNING, CRITICAL, OFFLINE)
     */
    public MeasurementStatus determineMeasurementStatus(
            Double temperature,
            Double voltage,
            Double current,
            Double frequency,
            Integer healthScore) {

        log.debug("Détermination du statut: Temp={}, Volt={}, Curr={}, Freq={}, Health={}",
            temperature, voltage, current, frequency, healthScore);

        // Priorité 1: Vérifier les paramètres hors ligne
        if (isOffline(temperature, voltage, current, frequency)) {
            log.debug("Statut déterminé: OFFLINE");
            return MeasurementStatus.OFFLINE;
        }

        // Priorité 2: Vérifier les conditions CRITIQUES
        if (isCriticalCondition(temperature, voltage, current, frequency, healthScore)) {
            log.debug("Statut déterminé: CRITICAL");
            return MeasurementStatus.CRITICAL;
        }

        // Priorité 3: Vérifier les conditions d'AVERTISSEMENT
        if (isWarningCondition(temperature, voltage, current, frequency, healthScore)) {
            log.debug("Statut déterminé: WARNING");
            return MeasurementStatus.WARNING;
        }

        // Sinon: NORMAL
        log.debug("Statut déterminé: NORMAL");
        return MeasurementStatus.NORMAL;
    }

    /**
     * Vérifie si le capteur/transformateur est hors ligne
     * Critères: valeurs NULL ou zéro pendant plusieurs mesures
     * 
     * @return true si hors ligne
     */
    private boolean isOffline(Double temperature, Double voltage, Double current, Double frequency) {
        // Si tous les paramètres sont nuls ou zéro
        boolean allZero = 
            (temperature == null || temperature == 0) &&
            (voltage == null || voltage == 0) &&
            (current == null || current == 0) &&
            (frequency == null || frequency == 0);

        return allZero;
    }

    /**
     * Vérifie si une condition CRITIQUE est présente
     * 
     * @return true si condition critique
     */
    private boolean isCriticalCondition(
            Double temperature,
            Double voltage,
            Double current,
            Double frequency,
            Integer healthScore) {

        // Critère 1: Température critique
        if (temperature != null && temperature > TEMP_CRITICAL_THRESHOLD) {
            return true;
        }

        // Critère 2: Tension hors limites
        if (voltage != null && (voltage < VOLTAGE_MIN_ACCEPTABLE || voltage > VOLTAGE_MAX_ACCEPTABLE)) {
            return true;
        }

        // Critère 3: Courant critique
        if (current != null && current > CURRENT_CRITICAL) {
            return true;
        }

        // Critère 4: Fréquence hors limites
        if (frequency != null && (frequency < FREQUENCY_CRITICAL_MIN || frequency > FREQUENCY_CRITICAL_MAX)) {
            return true;
        }

        // Critère 5: Health Score critique
        if (healthScore != null && healthScore < HEALTH_SCORE_CRITICAL) {
            return true;
        }

        return false;
    }

    /**
     * Vérifie si une condition d'AVERTISSEMENT est présente
     * 
     * @return true si condition d'avertissement
     */
    private boolean isWarningCondition(
            Double temperature,
            Double voltage,
            Double current,
            Double frequency,
            Integer healthScore) {

        // Critère 1: Température en zone d'avertissement
        if (temperature != null && temperature > TEMP_WARNING_THRESHOLD && temperature <= TEMP_CRITICAL_THRESHOLD) {
            return true;
        }

        // Critère 2: Tension en zone d'avertissement
        if (voltage != null && 
            ((voltage < VOLTAGE_MIN_WARNING || voltage > VOLTAGE_MAX_WARNING) &&
             (voltage >= VOLTAGE_MIN_ACCEPTABLE && voltage <= VOLTAGE_MAX_ACCEPTABLE))) {
            return true;
        }

        // Critère 3: Courant en zone d'avertissement
        if (current != null && current > CURRENT_WARNING && current <= CURRENT_CRITICAL) {
            return true;
        }

        // Critère 4: Fréquence en zone d'avertissement
        if (frequency != null && 
            ((frequency < FREQUENCY_WARNING_MIN || frequency > FREQUENCY_WARNING_MAX) &&
             (frequency >= FREQUENCY_CRITICAL_MIN && frequency <= FREQUENCY_CRITICAL_MAX))) {
            return true;
        }

        // Critère 5: Health Score en zone d'avertissement
        if (healthScore != null && healthScore >= HEALTH_SCORE_CRITICAL && healthScore < HEALTH_SCORE_WARNING) {
            return true;
        }

        return false;
    }

    /**
     * Fournit une description détaillée du statut
     * 
     * @return Description lisible du statut et des causes
     */
    public String getStatusExplanation(
            Double temperature,
            Double voltage,
            Double current,
            Double frequency,
            Integer healthScore,
            MeasurementStatus status) {

        StringBuilder explanation = new StringBuilder();
        explanation.append("Statut: ").append(status).append(" - ");

        if (status == MeasurementStatus.OFFLINE) {
            explanation.append("Capteur/transformateur hors ligne");
        } else if (status == MeasurementStatus.CRITICAL) {
            explanation.append("Conditions critiques: ");
            if (temperature != null && temperature > TEMP_CRITICAL_THRESHOLD) {
                explanation.append("Température trop élevée (").append(temperature).append("°C) ");
            }
            if (voltage != null && (voltage < VOLTAGE_MIN_ACCEPTABLE || voltage > VOLTAGE_MAX_ACCEPTABLE)) {
                explanation.append("Tension hors limites (").append(voltage).append("V) ");
            }
            if (current != null && current > CURRENT_CRITICAL) {
                explanation.append("Courant critique (").append(current).append("A) ");
            }
            if (frequency != null && (frequency < FREQUENCY_CRITICAL_MIN || frequency > FREQUENCY_CRITICAL_MAX)) {
                explanation.append("Fréquence critique (").append(frequency).append("Hz) ");
            }
            if (healthScore != null && healthScore < HEALTH_SCORE_CRITICAL) {
                explanation.append("Health Score critique (").append(healthScore).append("%) ");
            }
        } else if (status == MeasurementStatus.WARNING) {
            explanation.append("Avertissement: ");
            if (temperature != null && temperature > TEMP_WARNING_THRESHOLD) {
                explanation.append("Température élevée (").append(temperature).append("°C) ");
            }
            if (current != null && current > CURRENT_WARNING) {
                explanation.append("Courant élevé (").append(current).append("A) ");
            }
            if (healthScore != null && healthScore < HEALTH_SCORE_WARNING) {
                explanation.append("Health Score dégradé (").append(healthScore).append("%) ");
            }
        } else {
            explanation.append("Tous les paramètres sont normaux");
        }

        return explanation.toString();
    }
}
