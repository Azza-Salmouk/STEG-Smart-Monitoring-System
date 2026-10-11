package com.steg.smartmonitoring.measurement.provider;

import com.steg.smartmonitoring.measurement.dto.CreateMeasurementRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Fournisseur de mesures simulées
 * 
 * Génère des données réalistes qui évoluent progressivement dans le temps.
 * Les valeurs ne sont pas complètement aléatoires mais suivent des tendances.
 * 
 * Cela permet de :
 * - Tester le système sans ESP32 réel
 * - Simuler des scénarios normaux et critiques
 * - Avoir des données cohérentes et reproductibles
 */
@Component
public class SimulationMeasurementProvider implements MeasurementProvider {

    private static final Logger log = LoggerFactory.getLogger(SimulationMeasurementProvider.class);

    private static final String PROVIDER_NAME = "SIMULATION";
    private static final Random RANDOM = new Random();

    // État de chaque transformateur pour la simulation
    private final Map<Long, TransformatorState> transformatorStates = new HashMap<>();

    // Valeurs de base pour la simulation
    private static final double BASE_TEMP = 55.0;
    private static final double BASE_VOLTAGE = 90000.0;
    private static final double BASE_CURRENT = 150.0;
    private static final double BASE_POWER = 50.0;
    private static final double BASE_FREQUENCY = 50.0;

    /**
     * État d'un transformateur pour la simulation
     */
    private static class TransformatorState {
        double temperature = BASE_TEMP;
        double voltage = BASE_VOLTAGE;
        double current = BASE_CURRENT;
        double power = BASE_POWER;
        double frequency = BASE_FREQUENCY;
        double energyConsumption = 0;
        int healthScore = 100;
        long measurementCount = 0;

        /**
         * Évolue les valeurs progressivement vers une anomalie
         */
        void evolve() {
            measurementCount++;

            // Température: Tend légèrement à augmenter (usure)
            temperature += (Math.random() - 0.3) * 2;
            temperature = Math.max(40, Math.min(95, temperature));

            // Tension: Fluctuations légères autour de la base
            voltage += (Math.random() - 0.5) * 1000;
            voltage = Math.max(81000, Math.min(99000, voltage));

            // Courant: Charges variables
            current += (Math.random() - 0.5) * 30;
            current = Math.max(50, Math.min(350, current));

            // Puissance: Proportionnelle au courant
            power = (voltage * current) / 1000000;

            // Fréquence: Très stable
            frequency = 50.0 + (Math.random() - 0.5) * 0.1;

            // Consommation: Accumule
            energyConsumption += power * 0.016; // ~1 minute

            // Health Score: Décline très lentement avec la température
            if (temperature > 70 && measurementCount % 5 == 0) {
                healthScore = Math.max(20, healthScore - 1);
            } else if (temperature < 60 && measurementCount % 10 == 0) {
                healthScore = Math.min(100, healthScore + 1);
            }
        }
    }

    @Override
    public CreateMeasurementRequest generateMeasurement(Long transformateurId) {
        // Récupérer ou créer l'état du transformateur
        TransformatorState state = transformatorStates.computeIfAbsent(
            transformateurId,
            k -> new TransformatorState()
        );

        // Évoluer l'état
        state.evolve();

        // Créer la requête
        CreateMeasurementRequest request = CreateMeasurementRequest.builder()
            .transformateurId(transformateurId)
            .measurementDate(LocalDateTime.now())
            .temperature(roundTo2Decimals(state.temperature))
            .voltage(roundTo2Decimals(state.voltage))
            .current(roundTo2Decimals(state.current))
            .power(roundTo2Decimals(state.power))
            .energyConsumption(roundTo2Decimals(state.energyConsumption))
            .frequency(roundTo2Decimals(state.frequency))
            .humidity(roundTo2Decimals(50.0 + Math.random() * 20)) // 50-70%
            .build();

        log.debug("Mesure simulée générée: transformateur={}, temp={}, volt={}, curr={}",
            transformateurId, state.temperature, state.voltage, state.current);

        return request;
    }

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }

    @Override
    public boolean isAvailable() {
        return true; // La simulation est toujours disponible
    }

    /**
     * Génère un scénario critique pour tester les alertes
     */
    public CreateMeasurementRequest generateCriticalMeasurement(Long transformateurId) {
        return CreateMeasurementRequest.builder()
            .transformateurId(transformateurId)
            .measurementDate(LocalDateTime.now())
            .temperature(88.0) // Critique
            .voltage(75000.0) // En dessous des limites
            .current(380.0) // Très élevé
            .power(90.0)
            .energyConsumption(0.5)
            .frequency(50.2)
            .humidity(75.0)
            .build();
    }

    /**
     * Génère un scénario normal
     */
    public CreateMeasurementRequest generateNormalMeasurement(Long transformateurId) {
        return CreateMeasurementRequest.builder()
            .transformateurId(transformateurId)
            .measurementDate(LocalDateTime.now())
            .temperature(55.0)
            .voltage(90000.0)
            .current(150.0)
            .power(50.0)
            .energyConsumption(0.1)
            .frequency(50.0)
            .humidity(55.0)
            .build();
    }

    /**
     * Réinitialise l'état d'un transformateur
     */
    public void resetTransformatorState(Long transformateurId) {
        transformatorStates.remove(transformateurId);
        log.debug("État réinitialisé pour le transformateur: {}", transformateurId);
    }

    /**
     * Réinitialise tous les états
     */
    public void resetAllStates() {
        transformatorStates.clear();
        log.info("Tous les états simulés réinitialisés");
    }

    private double roundTo2Decimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
