package com.steg.smartmonitoring.measurement.repository;

import com.steg.smartmonitoring.measurement.entity.Measurement;
import com.steg.smartmonitoring.measurement.entity.MeasurementStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository JPA pour l'entité Measurement
 * 
 * Contient les requêtes optimisées pour :
 * - Récupérer la dernière mesure d'un transformateur
 * - Récupérer l'historique des mesures
 * - Rechercher les mesures critiques
 * - Filtrer par période, température, tension, courant, health score
 * - Recherche avancée avec pagination
 */
@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {

    /**
     * Récupère la dernière mesure d'un transformateur
     * 
     * @param transformateurId ID du transformateur
     * @return Dernière mesure ou vide
     */
    @Query(value = "SELECT * FROM measurements WHERE transformateur_id = :id ORDER BY measurement_date DESC LIMIT 1",
           nativeQuery = true)
    Optional<Measurement> findLatestByTransformateurId(@Param("id") Long transformateurId);

    /**
     * Récupère l'historique des mesures d'un transformateur
     * 
     * @param transformateurId ID du transformateur
     * @param pageable Pagination (last 100, ordered by date)
     * @return Page de mesures
     */
    Page<Measurement> findByTransformateurIdOrderByMeasurementDateDesc(Long transformateurId, Pageable pageable);

    /**
     * Récupère les mesures d'un transformateur pour une période donnée
     * 
     * @param transformateurId ID du transformateur
     * @param startDate Date de début
     * @param endDate Date de fin
     * @param pageable Pagination
     * @return Page de mesures
     */
    Page<Measurement> findByTransformateurIdAndMeasurementDateBetween(
        Long transformateurId,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Pageable pageable
    );

    /**
     * Récupère les mesures d'un transformateur (dernière mesure d'abord)
     * 
     * @param transformateurId ID du transformateur
     * @return Liste des mesures du dernier au plus ancien
     */
    List<Measurement> findByTransformateurIdOrderByMeasurementDateDesc(Long transformateurId);

    /**
     * Récupère les mesures par statut (dernière d'abord)
     * 
     * @param status Statut à rechercher
     * @param pageable Pagination
     * @return Page de mesures
     */
    Page<Measurement> findByStatusOrderByMeasurementDateDesc(MeasurementStatus status, Pageable pageable);

    /**
     * Recherche par température (> seuil)
     * 
     * @param minTemperature Température minimale
     * @param pageable Pagination
     * @return Page de mesures
     */
    Page<Measurement> findByTemperatureGreaterThanOrderByMeasurementDateDesc(
        Double minTemperature,
        Pageable pageable
    );

    /**
     * Recherche par température (plage)
     * 
     * @param minTemp Température minimale
     * @param maxTemp Température maximale
     * @param pageable Pagination
     * @return Page de mesures
     */
    @Query("""
        SELECT m FROM Measurement m
        WHERE m.temperature BETWEEN :minTemp AND :maxTemp
        ORDER BY m.measurementDate DESC
        """)
    Page<Measurement> findByTemperatureRange(
        @Param("minTemp") Double minTemp,
        @Param("maxTemp") Double maxTemp,
        Pageable pageable
    );

    /**
     * Recherche par tension (hors limites)
     * 
     * @param minVoltage Tension minimale acceptable
     * @param maxVoltage Tension maximale acceptable
     * @param pageable Pagination
     * @return Page de mesures
     */
    @Query("""
        SELECT m FROM Measurement m
        WHERE m.voltage < :minVoltage OR m.voltage > :maxVoltage
        ORDER BY m.measurementDate DESC
        """)
    Page<Measurement> findByVoltageOutOfRange(
        @Param("minVoltage") Double minVoltage,
        @Param("maxVoltage") Double maxVoltage,
        Pageable pageable
    );

    /**
     * Recherche par courant (> seuil)
     * 
     * @param minCurrent Courant minimum
     * @param pageable Pagination
     * @return Page de mesures
     */
    Page<Measurement> findByCurrentGreaterThanOrderByMeasurementDateDesc(
        Double minCurrent,
        Pageable pageable
    );

    /**
     * Recherche par Health Score (< seuil)
     * 
     * @param maxHealthScore Health Score maximum
     * @param pageable Pagination
     * @return Page de mesures
     */
    Page<Measurement> findByHealthScoreLessThanOrderByMeasurementDateDesc(
        Integer maxHealthScore,
        Pageable pageable
    );

    /**
     * Recherche avancée combinée avec tous les filtres optionnels
     * 
     * @param transformateurId ID du transformateur (optionnel)
     * @param status Statut de la mesure (optionnel)
     * @param minTemp Température minimale (optionnel)
     * @param maxTemp Température maximale (optionnel)
     * @param minVolt Tension minimale (optionnel)
     * @param maxVolt Tension maximale (optionnel)
     * @param minCurr Courant minimum (optionnel)
     * @param maxVolt Health Score maximum (optionnel)
     * @param startDate Date de début (optionnel)
     * @param endDate Date de fin (optionnel)
     * @param pageable Pagination
     * @return Page de mesures
     */
    @Query("""
        SELECT m FROM Measurement m
        WHERE (:transformateurId IS NULL OR m.transformateur.id = :transformateurId)
        AND (:status IS NULL OR m.status = :status)
        AND (:minTemp IS NULL OR m.temperature >= :minTemp)
        AND (:maxTemp IS NULL OR m.temperature <= :maxTemp)
        AND (:minVolt IS NULL OR m.voltage >= :minVolt)
        AND (:maxVolt IS NULL OR m.voltage <= :maxVolt)
        AND (:minCurr IS NULL OR m.current >= :minCurr)
        AND (:maxHealthScore IS NULL OR m.healthScore <= :maxHealthScore)
        AND (:startDate IS NULL OR m.measurementDate >= :startDate)
        AND (:endDate IS NULL OR m.measurementDate <= :endDate)
        ORDER BY m.measurementDate DESC
        """)
    Page<Measurement> searchAdvanced(
        @Param("transformateurId") Long transformateurId,
        @Param("status") MeasurementStatus status,
        @Param("minTemp") Double minTemp,
        @Param("maxTemp") Double maxTemp,
        @Param("minVolt") Double minVolt,
        @Param("maxVolt") Double maxVolt,
        @Param("minCurr") Double minCurr,
        @Param("maxHealthScore") Integer maxHealthScore,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        Pageable pageable
    );

    /**
     * Récupère les mesures d'aujourd'hui pour tous les transformateurs
     * 
     * @param startOfDay Début du jour
     * @param endOfDay Fin du jour
     * @param pageable Pagination
     * @return Page de mesures
     */
    Page<Measurement> findByMeasurementDateBetween(
        LocalDateTime startOfDay,
        LocalDateTime endOfDay,
        Pageable pageable
    );

    /**
     * Compte le nombre de mesures critiques
     * 
     * @return Nombre de mesures critiques
     */
    long countByStatus(MeasurementStatus status);

    /**
     * Compte le nombre de mesures pour un transformateur
     * 
     * @param transformateurId ID du transformateur
     * @return Nombre de mesures
     */
    long countByTransformateurId(Long transformateurId);

    /**
     * Compte les mesures critiques pour un transformateur
     * 
     * @param transformateurId ID du transformateur
     * @param status Statut à compter
     * @return Nombre de mesures
     */
    long countByTransformateurIdAndStatus(Long transformateurId, MeasurementStatus status);

    /**
     * Récupère tous les transformateurs qui ont des mesures critiques
     * 
     * @return Liste des IDs des transformateurs critiques
     */
    @Query("""
        SELECT DISTINCT m.transformateur.id FROM Measurement m
        WHERE m.status = 'CRITICAL'
        """)
    List<Long> findDistinctTransformateurIdByStatus(MeasurementStatus status);

    /**
     * Calcule le health score moyen de tous les transformateurs
     * 
     * @return Health score moyen ou NULL si aucune mesure
     */
    @Query("SELECT AVG(m.healthScore) FROM Measurement m")
    Double getAverageHealthScore();

    /**
     * Calcule le health score moyen d'un transformateur
     * 
     * @param transformateurId ID du transformateur
     * @return Health score moyen
     */
    @Query("SELECT AVG(m.healthScore) FROM Measurement m WHERE m.transformateur.id = :transformateurId")
    Double getAverageHealthScoreByTransformateur(@Param("transformateurId") Long transformateurId);

    /**
     * Récupère les mesures d'un transformateur après une date donnée
     * 
     * @param transformateurId ID du transformateur
     * @param date Date de début
     * @return Liste des mesures
     */
    @Query("SELECT m FROM Measurement m WHERE m.transformateur.id = :transformateurId AND m.measurementDate > :date ORDER BY m.measurementDate DESC")
    List<Measurement> findByTransformateurIdAfterDate(@Param("transformateurId") Long transformateurId, @Param("date") LocalDateTime date);

    /**
     * Récupère la dernière mesure d'un transformateur
     * 
     * @param transformateurId ID du transformateur
     * @return Dernière mesure
     */
    @Query(value = "SELECT m FROM Measurement m WHERE m.transformateur.id = :transformateurId ORDER BY m.measurementDate DESC")
    List<Measurement> findLatestMeasurementsOrderedByTransformateurId(@Param("transformateurId") Long transformateurId);

    /**
     * Calcule la température moyenne de tous les transformateurs
     * 
     * @return Température moyenne ou NULL si aucune mesure
     */
    @Query("SELECT AVG(m.temperature) FROM Measurement m")
    Double getAverageTemperature();

    /**
     * Calcule la température minimale
     * 
     * @return Température minimale ou NULL
     */
    @Query("SELECT MIN(m.temperature) FROM Measurement m")
    Double getMinTemperature();

    /**
     * Calcule la température maximale
     * 
     * @return Température maximale ou NULL
     */
    @Query("SELECT MAX(m.temperature) FROM Measurement m")
    Double getMaxTemperature();

    /**
     * Calcule la tension moyenne
     * 
     * @return Tension moyenne ou NULL
     */
    @Query("SELECT AVG(m.voltage) FROM Measurement m")
    Double getAverageVoltage();

    /**
     * Calcule le courant moyen
     * 
     * @return Courant moyen ou NULL
     */
    @Query("SELECT AVG(m.current) FROM Measurement m")
    Double getAverageCurrent();

    /**
     * Calcule la puissance moyenne
     * 
     * @return Puissance moyenne ou NULL
     */
    @Query("SELECT AVG(m.power) FROM Measurement m")
    Double getAveragePower();

    /**
     * Calcule le health score minimum
     * 
     * @return Health score minimum ou NULL
     */
    @Query("SELECT MIN(m.healthScore) FROM Measurement m")
    Integer getMinHealthScore();

    /**
     * Calcule le health score maximum
     * 
     * @return Health score maximum ou NULL
     */
    @Query("SELECT MAX(m.healthScore) FROM Measurement m")
    Integer getMaxHealthScore();

    /**
     * Récupère la date de la première mesure
     * 
     * @return Date de la première mesure ou NULL
     */
    @Query("SELECT MIN(m.measurementDate) FROM Measurement m")
    LocalDateTime getFirstMeasurementDate();

    /**
     * Récupère la date de la dernière mesure
     * 
     * @return Date de la dernière mesure ou NULL
     */
    @Query("SELECT MAX(m.measurementDate) FROM Measurement m")
    LocalDateTime getLastMeasurementDate();

    /**
     * Compte le nombre de transformateurs qui ont des mesures
     * 
     * @return Nombre de transformateurs distincts
     */
    @Query("SELECT COUNT(DISTINCT m.transformateur.id) FROM Measurement m")
    Long countDistinctTransformateurs();
}
