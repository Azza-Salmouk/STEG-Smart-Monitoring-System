package com.steg.smartmonitoring.transformateur.repository;

import com.steg.smartmonitoring.transformateur.entity.RiskLevel;
import com.steg.smartmonitoring.transformateur.entity.StatusTransformateur;
import com.steg.smartmonitoring.transformateur.entity.Transformateur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour l'entité Transformateur
 * Contient les requêtes personnalisées pour la recherche et le filtrage
 */
@Repository
public interface TransformateurRepository extends JpaRepository<Transformateur, Long> {

    /**
     * Vérifier si un transformateur avec cette référence existe
     */
    boolean existsByReference(String reference);

    /**
     * Trouver un transformateur par sa référence
     */
    Optional<Transformateur> findByReference(String reference);

    /**
     * Trouver tous les transformateurs d'un poste
     */
    List<Transformateur> findByPosteId(Long posteId);

    /**
     * Recherche par référence (case-insensitive)
     */
    Page<Transformateur> findByReferenceContainingIgnoreCase(String reference, Pageable pageable);

    /**
     * Recherche par fabricant
     */
    Page<Transformateur> findByManufacturerContainingIgnoreCase(String manufacturer, Pageable pageable);

    /**
     * Recherche par modèle
     */
    Page<Transformateur> findByModelContainingIgnoreCase(String model, Pageable pageable);

    /**
     * Recherche par status
     */
    Page<Transformateur> findByStatus(StatusTransformateur status, Pageable pageable);

    /**
     * Recherche par niveau de risque
     */
    Page<Transformateur> findByRiskLevel(RiskLevel riskLevel, Pageable pageable);

    /**
     * Recherche des transformateurs par plage de health score
     */
    Page<Transformateur> findByHealthScoreLessThanEqual(Integer healthScore, Pageable pageable);

    /**
     * Recherche combinée avancée
     */
    @Query("""
        SELECT t FROM Transformateur t 
        WHERE (:reference IS NULL OR t.reference LIKE %:reference%) 
        AND (:manufacturer IS NULL OR t.manufacturer LIKE %:manufacturer%) 
        AND (:status IS NULL OR t.status = :status)
        AND (:riskLevel IS NULL OR t.riskLevel = :riskLevel)
        AND (:healthScoreMax IS NULL OR t.healthScore <= :healthScoreMax)
        AND (:posteId IS NULL OR t.poste.id = :posteId)
        """)
    Page<Transformateur> search(
        @Param("reference") String reference,
        @Param("manufacturer") String manufacturer,
        @Param("status") StatusTransformateur status,
        @Param("riskLevel") RiskLevel riskLevel,
        @Param("healthScoreMax") Integer healthScoreMax,
        @Param("posteId") Long posteId,
        Pageable pageable
    );

    /**
     * Compter les transformateurs par status
     */
    long countByStatus(StatusTransformateur status);

    /**
     * Compter les transformateurs par niveau de risque
     */
    long countByRiskLevel(RiskLevel riskLevel);

    /**
     * Compter les transformateurs critiques (health score < 30)
     */
    @Query("SELECT COUNT(t) FROM Transformateur t WHERE t.healthScore < 30")
    long countCritical();

    /**
     * Calculer la moyenne du health score
     */
    @Query("SELECT AVG(t.healthScore) FROM Transformateur t")
    Double getAverageHealthScore();

    /**
     * Obtenir tous les transformateurs en alerte ou défaut
     */
    @Query("""
        SELECT t FROM Transformateur t 
        WHERE t.status IN (com.steg.smartmonitoring.transformateur.entity.StatusTransformateur.ALERTE, 
                          com.steg.smartmonitoring.transformateur.entity.StatusTransformateur.DEFAUT)
        """)
    List<Transformateur> findAllAlerting();

    /**
     * Obtenir les transformateurs critiques (health score < 30 OU risk CRITICAL)
     */
    @Query("""
        SELECT t FROM Transformateur t 
        WHERE t.healthScore < 30 
        OR t.riskLevel = com.steg.smartmonitoring.transformateur.entity.RiskLevel.CRITICAL
        """)
    List<Transformateur> findAllCritical();
}
