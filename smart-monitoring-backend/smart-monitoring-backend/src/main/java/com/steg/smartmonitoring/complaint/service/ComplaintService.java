package com.steg.smartmonitoring.complaint.service;

import com.steg.smartmonitoring.complaint.dto.*;
import com.steg.smartmonitoring.complaint.entity.ComplaintCategory;
import com.steg.smartmonitoring.complaint.entity.ComplaintPriority;
import com.steg.smartmonitoring.complaint.entity.ComplaintStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

/**
 * Service métier pour la gestion des réclamations
 */
public interface ComplaintService {

    /**
     * Créer une nouvelle réclamation
     */
    ComplaintResponse create(CreateComplaintRequest request);

    /**
     * Récupérer une réclamation par son ID
     */
    ComplaintResponse getById(Long id);

    /**
     * Récupérer toutes les réclamations avec pagination
     */
    Page<ComplaintResponse> getAll(Pageable pageable);

    /**
     * Mettre à jour une réclamation
     */
    ComplaintResponse update(Long id, UpdateComplaintRequest request);

    /**
     * Supprimer une réclamation (ADMIN/SUPERVISOR uniquement)
     */
    void delete(Long id);

    /**
     * Récupérer les réclamations par statut
     */
    Page<ComplaintResponse> getByStatut(ComplaintStatus statut, Pageable pageable);

    /**
     * Récupérer les réclamations par priorité
     */
    Page<ComplaintResponse> getByPriorite(ComplaintPriority priorite, Pageable pageable);

    /**
     * Récupérer les réclamations par catégorie
     */
    Page<ComplaintResponse> getByCategorie(ComplaintCategory categorie, Pageable pageable);

    /**
     * Rechercher par nom du client
     */
    Page<ComplaintResponse> searchByClient(String nomClient, Pageable pageable);

    /**
     * Récupérer les réclamations d'un superviseur
     */
    Page<ComplaintResponse> getBySupervisor(Long supervisorId, Pageable pageable);

    /**
     * Récupérer les réclamations d'un poste
     */
    Page<ComplaintResponse> getByPoste(Long posteId, Pageable pageable);

    /**
     * Récupérer les réclamations d'un transformateur
     */
    Page<ComplaintResponse> getByTransformateur(Long transformateurId, Pageable pageable);

    /**
     * Rechercher par période
     */
    Page<ComplaintResponse> getByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    /**
     * Recherche avancée
     */
    Page<ComplaintResponse> searchAdvanced(
            String nomClient,
            ComplaintStatus statut,
            ComplaintPriority priorite,
            ComplaintCategory categorie,
            Long posteId,
            Long transformateurId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable
    );

    /**
     * Obtenir les statistiques
     */
    ComplaintStatisticsResponse getStatistics();

    /**
     * Obtenir les réclamations critiques non résolues
     */
    Page<ComplaintResponse> getCritical(Pageable pageable);

    /**
     * Assigner une réclamation à un superviseur
     */
    ComplaintResponse assign(Long complaintId, Long supervisorId);
}
