package com.steg.smartmonitoring.intervention.service;

import com.steg.smartmonitoring.intervention.dto.*;
import com.steg.smartmonitoring.intervention.entity.Priorite;
import com.steg.smartmonitoring.intervention.entity.StatutIntervention;
import com.steg.smartmonitoring.intervention.entity.TypeIntervention;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

/**
 * Service métier pour la gestion des interventions
 */
public interface InterventionService {

    /**
     * Créer une nouvelle intervention
     */
    InterventionResponse create(CreateInterventionRequest request);

    /**
     * Récupérer une intervention par son ID
     */
    InterventionResponse getById(Long id);

    /**
     * Récupérer toutes les interventions avec pagination
     */
    Page<InterventionResponse> getAll(Pageable pageable);

    /**
     * Mettre à jour une intervention
     */
    InterventionResponse update(Long id, UpdateInterventionRequest request);

    /**
     * Supprimer une intervention
     */
    void delete(Long id);

    /**
     * Récupérer les interventions d'un agent
     */
    Page<InterventionResponse> getByAgent(Long agentId, Pageable pageable);

    /**
     * Récupérer les interventions d'un transformateur
     */
    Page<InterventionResponse> getByTransformateur(Long transformateurId, Pageable pageable);

    /**
     * Rechercher par statut
     */
    Page<InterventionResponse> getByStatut(StatutIntervention statut, Pageable pageable);

    /**
     * Rechercher par priorité
     */
    Page<InterventionResponse> getByPriorite(Priorite priorite, Pageable pageable);

    /**
     * Rechercher par période
     */
    Page<InterventionResponse> getByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    /**
     * Recherche avancée
     */
    Page<InterventionResponse> searchAdvanced(
            Long transformateurId,
            Long agentId,
            StatutIntervention statut,
            Priorite priorite,
            TypeIntervention typeIntervention,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable
    );

    /**
     * Obtenir les statistiques
     */
    InterventionStatisticsResponse getStatistics();

    /**
     * Obtenir les interventions critiques
     */
    Page<InterventionResponse> getCritical(Pageable pageable);

    /**
     * Obtenir les interventions en cours pour un agent
     */
    Page<InterventionResponse> getOngoingByAgent(Long agentId, Pageable pageable);
}
