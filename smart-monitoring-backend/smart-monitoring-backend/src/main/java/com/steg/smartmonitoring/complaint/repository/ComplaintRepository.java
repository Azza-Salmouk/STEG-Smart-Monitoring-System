package com.steg.smartmonitoring.complaint.repository;

import com.steg.smartmonitoring.complaint.entity.Complaint;
import com.steg.smartmonitoring.complaint.entity.ComplaintCategory;
import com.steg.smartmonitoring.complaint.entity.ComplaintPriority;
import com.steg.smartmonitoring.complaint.entity.ComplaintStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Repository pour la gestion des réclamations
 */
@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    // Recherche par référence
    Optional<Complaint> findByReference(String reference);

    // Recherche par statut
    Page<Complaint> findByStatut(ComplaintStatus statut, Pageable pageable);

    // Recherche par priorité
    Page<Complaint> findByPriorite(ComplaintPriority priorite, Pageable pageable);

    // Recherche par catégorie
    Page<Complaint> findByCategorie(ComplaintCategory categorie, Pageable pageable);

    // Recherche par client
    Page<Complaint> findByNomClientContainsIgnoreCase(String nomClient, Pageable pageable);

    // Recherche par superviseur assigné
    Page<Complaint> findByAssignedSupervisorId(Long supervisorId, Pageable pageable);

    // Recherche par poste
    Page<Complaint> findByPosteId(Long posteId, Pageable pageable);

    // Recherche par transformateur
    Page<Complaint> findByTransformateurId(Long transformateurId, Pageable pageable);

    // Recherche par période
    Page<Complaint> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    // Statistiques
    @Query("SELECT COUNT(c) FROM Complaint c WHERE c.statut = :statut")
    Long countByStatut(@Param("statut") ComplaintStatus statut);

    @Query("SELECT COUNT(c) FROM Complaint c WHERE c.priorite = :priorite")
    Long countByPriorite(@Param("priorite") ComplaintPriority priorite);

    //@Query("SELECT AVG(EXTRACT(HOUR FROM (c.dateResolution - c.createdAt))) FROM Complaint c WHERE c.dateResolution IS NOT NULL")
    //Double getAverageResolutionTimeHours();

    @Query("SELECT COUNT(c) FROM Complaint c WHERE c.statut = :statut1 OR c.statut = :statut2")
    Long getResolvedCount(@Param("statut1") ComplaintStatus statut1, @Param("statut2") ComplaintStatus statut2);

    // Recherche avancée
    @Query("SELECT c FROM Complaint c WHERE " +
            "(:nomClient IS NULL OR c.nomClient LIKE %:nomClient%) AND " +
            "(:statut IS NULL OR c.statut = :statut) AND " +
            "(:priorite IS NULL OR c.priorite = :priorite) AND " +
            "(:categorie IS NULL OR c.categorie = :categorie) AND " +
            "(:posteId IS NULL OR c.poste.id = :posteId) AND " +
            "(:transformateurId IS NULL OR c.transformateur.id = :transformateurId) AND " +
            "(:startDate IS NULL OR c.createdAt >= :startDate) AND " +
            "(:endDate IS NULL OR c.createdAt <= :endDate)")
    Page<Complaint> searchAdvanced(
            @Param("nomClient") String nomClient,
            @Param("statut") ComplaintStatus statut,
            @Param("priorite") ComplaintPriority priorite,
            @Param("categorie") ComplaintCategory categorie,
            @Param("posteId") Long posteId,
            @Param("transformateurId") Long transformateurId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );
}
