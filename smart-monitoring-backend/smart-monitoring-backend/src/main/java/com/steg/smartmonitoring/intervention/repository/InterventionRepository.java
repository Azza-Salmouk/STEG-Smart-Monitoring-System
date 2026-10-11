package com.steg.smartmonitoring.intervention.repository;

import com.steg.smartmonitoring.intervention.entity.Intervention;
import com.steg.smartmonitoring.intervention.entity.Priorite;
import com.steg.smartmonitoring.intervention.entity.StatutIntervention;
import com.steg.smartmonitoring.intervention.entity.TypeIntervention;
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
 * Repository pour la gestion des interventions
 */
@Repository
public interface InterventionRepository extends JpaRepository<Intervention, Long> {

    // Recherche par référence
    Optional<Intervention> findByReference(String reference);

    // Recherche par statut
    Page<Intervention> findByStatut(StatutIntervention statut, Pageable pageable);

    // Recherche par priorité
    Page<Intervention> findByPriorite(Priorite priorite, Pageable pageable);

    // Recherche par agent
    Page<Intervention> findByAgentId(Long agentId, Pageable pageable);

    // Recherche par transformateur
    Page<Intervention> findByTransformateurId(Long transformateurId, Pageable pageable);

    // Recherche par type d'intervention
    Page<Intervention> findByTypeIntervention(TypeIntervention typeIntervention, Pageable pageable);

    // Recherche par statut et agent
    Page<Intervention> findByStatutAndAgentId(StatutIntervention statut, Long agentId, Pageable pageable);

    // Recherche par statut et priorité
    Page<Intervention> findByStatutAndPriorite(StatutIntervention statut, Priorite priorite, Pageable pageable);

    // Recherche par période
    Page<Intervention> findByDatePlanifieeBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    // Recherche critiques
    Page<Intervention> findByPrioriteAndStatutNot(Priorite priorite, StatutIntervention statut, Pageable pageable);

    // Statistiques
    @Query("SELECT COUNT(i) FROM Intervention i WHERE i.statut = :statut")
    Long countByStatut(@Param("statut") StatutIntervention statut);

    @Query("SELECT COUNT(i) FROM Intervention i WHERE i.priorite = :priorite")
    Long countByPriorite(@Param("priorite") Priorite priorite);

    @Query("SELECT AVG(i.dureeMinutes) FROM Intervention i WHERE i.dateFin IS NOT NULL")
    Double getAverageDuration();

    @Query("SELECT AVG(i.cout) FROM Intervention i WHERE i.statut = :statut")
    Double getAverageCost(@Param("statut") StatutIntervention statut);

    @Query("SELECT COUNT(i) FROM Intervention i WHERE i.dateFin IS NOT NULL")
    Long getCompletedInterventionsCount();

    // Recherche avancée
    @Query("SELECT i FROM Intervention i WHERE " +
            "(:transformateurId IS NULL OR i.transformateur.id = :transformateurId) AND " +
            "(:agentId IS NULL OR i.agent.id = :agentId) AND " +
            "(:statut IS NULL OR i.statut = :statut) AND " +
            "(:priorite IS NULL OR i.priorite = :priorite) AND " +
            "(:typeIntervention IS NULL OR i.typeIntervention = :typeIntervention) AND " +
            "(:startDate IS NULL OR i.datePlanifiee >= :startDate) AND " +
            "(:endDate IS NULL OR i.datePlanifiee <= :endDate)")
    Page<Intervention> searchAdvanced(
            @Param("transformateurId") Long transformateurId,
            @Param("agentId") Long agentId,
            @Param("statut") StatutIntervention statut,
            @Param("priorite") Priorite priorite,
            @Param("typeIntervention") TypeIntervention typeIntervention,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );
}
