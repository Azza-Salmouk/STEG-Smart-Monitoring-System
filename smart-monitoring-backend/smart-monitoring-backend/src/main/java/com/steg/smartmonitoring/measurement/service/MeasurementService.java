package com.steg.smartmonitoring.measurement.service;

import com.steg.smartmonitoring.measurement.dto.CreateMeasurementRequest;
import com.steg.smartmonitoring.measurement.dto.MeasurementResponse;
import com.steg.smartmonitoring.measurement.dto.UpdateMeasurementRequest;
import com.steg.smartmonitoring.measurement.entity.MeasurementStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service métier pour la gestion des mesures IoT
 * 
 * Responsabilités :
 * - CRUD des mesures
 * - Calcul automatique du Health Score et du statut
 * - Recherche et filtrage avancé
 * - Récupération de l'historique
 * - Statistiques et rapports
 */
public interface MeasurementService {

    /**
     * Crée une nouvelle mesure IoT
     * 
     * Effectue automatiquement :
     * - Validation des données
     * - Calcul du Health Score
     * - Détermination du statut
     * - Vérification du transformateur
     * 
     * @param request Données de la mesure
     * @return Mesure créée avec health score et statut calculés
     */
    MeasurementResponse create(CreateMeasurementRequest request);

    /**
     * Récupère une mesure par son ID
     * 
     * @param id ID de la mesure
     * @return Données de la mesure
     */
    MeasurementResponse getById(Long id);

    /**
     * Récupère toutes les mesures avec pagination
     * 
     * @param pageable Pagination (page, size, sort)
     * @return Page de mesures
     */
    Page<MeasurementResponse> getAll(Pageable pageable);

    /**
     * Met à jour une mesure existante
     * 
     * Recalcule automatiquement le Health Score et le statut
     * après la mise à jour des données.
     * 
     * @param id ID de la mesure à mettre à jour
     * @param request Données à mettre à jour
     * @return Mesure mise à jour
     */
    MeasurementResponse update(Long id, UpdateMeasurementRequest request);

    /**
     * Supprime une mesure
     * 
     * @param id ID de la mesure à supprimer
     */
    void delete(Long id);

    /**
     * Récupère l'historique des mesures d'un transformateur
     * 
     * @param transformateurId ID du transformateur
     * @param pageable Pagination
     * @return Page d'historique
     */
    Page<MeasurementResponse> getHistoryByTransformateur(Long transformateurId, Pageable pageable);

    /**
     * Récupère la dernière mesure d'un transformateur
     * 
     * @param transformateurId ID du transformateur
     * @return Dernière mesure
     */
    MeasurementResponse getLatestByTransformateur(Long transformateurId);

    /**
     * Récupère les mesures d'un transformateur pour une période
     * 
     * @param transformateurId ID du transformateur
     * @param startDate Date de début
     * @param endDate Date de fin
     * @param pageable Pagination
     * @return Page de mesures
     */
    Page<MeasurementResponse> getMeasurementsByDateRange(
        Long transformateurId,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Pageable pageable
    );

    /**
     * Récupère les mesures critiques
     * 
     * @param pageable Pagination
     * @return Page de mesures critiques
     */
    Page<MeasurementResponse> getCriticalMeasurements(Pageable pageable);

    /**
     * Récupère les mesures en avertissement
     * 
     * @param pageable Pagination
     * @return Page de mesures en avertissement
     */
    Page<MeasurementResponse> getWarningMeasurements(Pageable pageable);

    /**
     * Recherche avancée avec filtres multiples
     * 
     * @param transformateurId Filtre par transformateur (optionnel)
     * @param status Filtre par statut (optionnel)
     * @param minTemperature Température minimale (optionnel)
     * @param maxTemperature Température maximale (optionnel)
     * @param minVoltage Tension minimale (optionnel)
     * @param maxVoltage Tension maximale (optionnel)
     * @param minCurrent Courant minimum (optionnel)
     * @param maxHealthScore Health Score maximum (optionnel)
     * @param startDate Date de début (optionnel)
     * @param endDate Date de fin (optionnel)
     * @param pageable Pagination
     * @return Page de résultats
     */
    Page<MeasurementResponse> searchAdvanced(
        Long transformateurId,
        MeasurementStatus status,
        Double minTemperature,
        Double maxTemperature,
        Double minVoltage,
        Double maxVoltage,
        Double minCurrent,
        Integer maxHealthScore,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Pageable pageable
    );

    /**
     * Récupère les mesures d'aujourd'hui
     * 
     * @param pageable Pagination
     * @return Page de mesures du jour
     */
    Page<MeasurementResponse> getTodayMeasurements(Pageable pageable);

    /**
     * Récupère les statistiques globales des mesures
     * 
     * @return Objet contenant les statistiques
     */
    MeasurementStatisticsResponse getStatistics();

    /**
     * Récupère les statistiques des mesures d'un transformateur
     * 
     * @param transformateurId ID du transformateur
     * @return Objet contenant les statistiques
     */
    MeasurementStatisticsResponse getStatisticsByTransformateur(Long transformateurId);

    /**
     * Récupère le nombre de mesures critiques
     * 
     * @return Nombre de mesures critiques
     */
    long countCriticalMeasurements();

    /**
     * Récupère le nombre de mesures en avertissement
     * 
     * @return Nombre de mesures en avertissement
     */
    long countWarningMeasurements();

    /**
     * Récupère les transformateurs avec mesures critiques
     * 
     * @return Liste des transformateurs critiques
     */
    List<Long> getTransformateursWithCriticalMeasurements();

    /**
     * Calcule le health score moyen global
     * 
     * @return Health score moyen
     */
    Double getAverageHealthScore();

    /**
     * Calcule le health score moyen d'un transformateur
     * 
     * @param transformateurId ID du transformateur
     * @return Health score moyen
     */
    Double getAverageHealthScoreByTransformateur(Long transformateurId);
}
