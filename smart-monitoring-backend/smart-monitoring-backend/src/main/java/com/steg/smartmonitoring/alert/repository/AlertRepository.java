package com.steg.smartmonitoring.alert.repository;

import com.steg.smartmonitoring.alert.entity.Alert;
import com.steg.smartmonitoring.alert.entity.AlertLevel;
import com.steg.smartmonitoring.alert.entity.AlertStatus;
import com.steg.smartmonitoring.alert.entity.AlertType;
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
 * Repository pour la gestion des alertes
 */
@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {

    // Recherche par référence
    Optional<Alert> findByReference(String reference);

    // Recherche par statut
    Page<Alert> findByStatut(AlertStatus statut, Pageable pageable);

    // Recherche par niveau
    Page<Alert> findByNiveau(AlertLevel niveau, Pageable pageable);

    // Recherche par type
    Page<Alert> findByType(AlertType type, Pageable pageable);

    // Recherche par transformateur
    Page<Alert> findByTransformateurId(Long transformateurId, Pageable pageable);

    // Recherche par poste
    Page<Alert> findByPosteId(Long posteId, Pageable pageable);

    // Recherche par mesure
    Page<Alert> findByMeasurementId(Long measurementId, Pageable pageable);

    // Recherche par titre
    Page<Alert> findByTitreContainsIgnoreCase(String titre, Pageable pageable);

    // Recherche par période
    Page<Alert> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    // Alertes reconnues
    Page<Alert> findByIsAcknowledged(Boolean isAcknowledged, Pageable pageable);

    // Alertes archivées
    Page<Alert> findByIsArchived(Boolean isArchived, Pageable pageable);

    // Alertes actives
    Page<Alert> findByStatutAndIsArchived(AlertStatus statut, Boolean isArchived, Pageable pageable);

    // Alertes critiques
    Page<Alert> findByNiveauAndIsArchived(AlertLevel niveau, Boolean isArchived, Pageable pageable);

    // Statistiques - Comptages
    @Query("SELECT COUNT(a) FROM Alert a WHERE a.statut = :statut")
    Long countByStatut(@Param("statut") AlertStatus statut);

    @Query("SELECT COUNT(a) FROM Alert a WHERE a.niveau = :niveau")
    Long countByNiveau(@Param("niveau") AlertLevel niveau);

    @Query("SELECT COUNT(a) FROM Alert a WHERE a.type = :type")
    Long countByType(@Param("type") AlertType type);

    @Query("SELECT COUNT(a) FROM Alert a WHERE a.isAcknowledged = :acknowledged")
    Long countByIsAcknowledged(@Param("acknowledged") Boolean acknowledged);

    @Query("SELECT COUNT(a) FROM Alert a WHERE a.isArchived = :archived")
    Long countByIsArchived(@Param("archived") Boolean archived);

    //@Query("SELECT COUNT(a) FROM Alert a WHERE a.statut = com.steg.smartmonitoring.alert.entity.AlertStatus.ACTIVE")
    //Long countActiveAlerts();

    //@Query("SELECT COUNT(a) FROM Alert a WHERE a.niveau = com.steg.smartmonitoring.alert.entity.AlertLevel.CRITICAL")
    //Long countCriticalAlerts();

    //@Query("SELECT COUNT(a) FROM Alert a WHERE a.statut = com.steg.smartmonitoring.alert.entity.AlertStatus.RESOLVED OR a.statut = com.steg.smartmonitoring.alert.entity.AlertStatus.CLOSED")
    //Long countResolvedAlerts();

    // Temps moyen de résolution (returns null if no resolved alerts)
    //@Query(value = "SELECT AVG(EXTRACT(EPOCH FROM (resolved_at - created_at))) FROM alerts WHERE resolved_at IS NOT NULL", nativeQuery = true)
    //Double getAverageResolutionTimeHours();

    // Taux de résolution
    //@Query("SELECT CASE WHEN COUNT(a) = 0 THEN 0 ELSE (COUNT(CASE WHEN a.statut = com.steg.smartmonitoring.alert.entity.AlertStatus.RESOLVED OR a.statut = com.steg.smartmonitoring.alert.entity.AlertStatus.CLOSED THEN 1 END) * 100.0 / COUNT(a)) END FROM Alert a")
    //Double getResolutionRate();

    // Taux de reconnaissance
    //@Query("SELECT CASE WHEN COUNT(a) = 0 THEN 0 ELSE (COUNT(CASE WHEN a.isAcknowledged = true THEN 1 END) * 100.0 / COUNT(a)) END FROM Alert a WHERE a.statut = com.steg.smartmonitoring.alert.entity.AlertStatus.ACTIVE")
    //Double getAcknowledgmentRate();

    // Recherche avancée
    @Query("SELECT a FROM Alert a WHERE " +
            "(:titre IS NULL OR a.titre LIKE %:titre%) AND " +
            "(:statut IS NULL OR a.statut = :statut) AND " +
            "(:niveau IS NULL OR a.niveau = :niveau) AND " +
            "(:type IS NULL OR a.type = :type) AND " +
            "(:transformateurId IS NULL OR a.transformateur.id = :transformateurId) AND " +
            "(:posteId IS NULL OR a.poste.id = :posteId) AND " +
            "(:startDate IS NULL OR a.createdAt >= :startDate) AND " +
            "(:endDate IS NULL OR a.createdAt <= :endDate)")
    Page<Alert> searchAdvanced(
            @Param("titre") String titre,
            @Param("statut") AlertStatus statut,
            @Param("niveau") AlertLevel niveau,
            @Param("type") AlertType type,
            @Param("transformateurId") Long transformateurId,
            @Param("posteId") Long posteId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );

    // Alertes récentes non archivées
    @Query("SELECT a FROM Alert a WHERE a.isArchived = false ORDER BY a.createdAt DESC")
    Page<Alert> findRecentActiveAlerts(Pageable pageable);

    // Alertes par transformateur et statut
    @Query("SELECT a FROM Alert a WHERE a.transformateur.id = :transformateurId AND a.statut = :statut")
    Page<Alert> findByTransformateurAndStatut(
            @Param("transformateurId") Long transformateurId,
            @Param("statut") AlertStatus statut,
            Pageable pageable
    );

    /**
     * Compte les alertes pour une liste de transformateurs
     */
    @Query("SELECT COUNT(a) FROM Alert a WHERE a.transformateur.id IN :transformateurIds")
    Long countByTransformateurIdIn(@Param("transformateurIds") List<Long> transformateurIds);

    /**
     * Récupère les alertes actives d'un transformateur
     * 
     * @param transformateurId ID du transformateur
     * @return Liste des alertes actives
     */
    @Query("SELECT a FROM Alert a WHERE a.transformateur.id = :transformateurId AND a.statut = :statut ORDER BY a.createdAt DESC")
    List<Alert> findActiveAlertsByTransformateurId(@Param("transformateurId") Long transformateurId, @Param("statut") AlertStatus statut);

    /**
     * Compte les alertes actives d'un transformateur
     * 
     * @param transformateurId ID du transformateur
     * @return Nombre d'alertes actives
     */
    @Query("SELECT COUNT(a) FROM Alert a WHERE a.transformateur.id = :transformateurId AND a.statut = :statut")
    Long countActiveAlertsByTransformateurId(@Param("transformateurId") Long transformateurId, @Param("statut") AlertStatus statut);

    /**
     * Compte les alertes critiques actives d'un transformateur
     * 
     * @param transformateurId ID du transformateur
     * @return Nombre d'alertes critiques
     */
    @Query("SELECT COUNT(a) FROM Alert a WHERE a.transformateur.id = :transformateurId AND a.statut = :statut AND a.niveau = :niveau")
    Long countCriticalAlertsByTransformateurId(@Param("transformateurId") Long transformateurId, @Param("statut") AlertStatus statut, @Param("niveau") AlertLevel niveau);

    /**
     * Récupère les alertes d'un transformateur après une date donnée
     * 
     * @param transformateurId ID du transformateur
     * @param date Date de début
     * @return Liste des alertes
     */
    @Query("SELECT a FROM Alert a WHERE a.transformateur.id = :transformateurId AND a.createdAt > :date ORDER BY a.createdAt DESC")
    List<Alert> findAlertsByTransformateurIdAfterDate(@Param("transformateurId") Long transformateurId, @Param("date") LocalDateTime date);
}
