package com.steg.smartmonitoring.intervention.controller;

import com.steg.smartmonitoring.intervention.dto.*;
import com.steg.smartmonitoring.intervention.entity.Priorite;
import com.steg.smartmonitoring.intervention.entity.StatutIntervention;
import com.steg.smartmonitoring.intervention.entity.TypeIntervention;
import com.steg.smartmonitoring.intervention.service.InterventionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * Contrôleur REST pour la gestion des interventions
 */
@RestController
@RequestMapping("/api/interventions")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class InterventionController {

    private final InterventionService interventionService;

    /**
     * GET /api/interventions - Récupérer toutes les interventions (paginé)
     */
    @GetMapping
    public ResponseEntity<Page<InterventionResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(interventionService.getAll(pageable));
    }

    /**
     * GET /api/interventions/{id} - Récupérer une intervention par ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<InterventionResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(interventionService.getById(id));
    }

    /**
     * POST /api/interventions - Créer une nouvelle intervention
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR', 'AGENT')")
    public ResponseEntity<InterventionResponse> create(@Valid @RequestBody CreateInterventionRequest request) {
        InterventionResponse response = interventionService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * PUT /api/interventions/{id} - Mettre à jour une intervention
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR', 'AGENT')")
    public ResponseEntity<InterventionResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateInterventionRequest request) {
        InterventionResponse response = interventionService.update(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/interventions/{id} - Supprimer une intervention (ADMIN et SUPERVISOR uniquement)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        interventionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/interventions/by-agent/{agentId} - Récupérer les interventions d'un agent
     */
    @GetMapping("/by-agent/{agentId}")
    public ResponseEntity<Page<InterventionResponse>> getByAgent(
            @PathVariable Long agentId,
            Pageable pageable) {
        return ResponseEntity.ok(interventionService.getByAgent(agentId, pageable));
    }

    /**
     * GET /api/interventions/by-transformateur/{transformateurId} - Récupérer les interventions d'un transformateur
     */
    @GetMapping("/by-transformateur/{transformateurId}")
    public ResponseEntity<Page<InterventionResponse>> getByTransformateur(
            @PathVariable Long transformateurId,
            Pageable pageable) {
        return ResponseEntity.ok(interventionService.getByTransformateur(transformateurId, pageable));
    }

    /**
     * GET /api/interventions/search/by-statut - Recherche par statut
     */
    @GetMapping("/search/by-statut")
    public ResponseEntity<Page<InterventionResponse>> getByStatut(
            @RequestParam StatutIntervention statut,
            Pageable pageable) {
        return ResponseEntity.ok(interventionService.getByStatut(statut, pageable));
    }

    /**
     * GET /api/interventions/search/by-priorite - Recherche par priorité
     */
    @GetMapping("/search/by-priorite")
    public ResponseEntity<Page<InterventionResponse>> getByPriorite(
            @RequestParam Priorite priorite,
            Pageable pageable) {
        return ResponseEntity.ok(interventionService.getByPriorite(priorite, pageable));
    }

    /**
     * GET /api/interventions/search/by-date-range - Recherche par période
     */
    @GetMapping("/search/by-date-range")
    public ResponseEntity<Page<InterventionResponse>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Pageable pageable) {
        return ResponseEntity.ok(interventionService.getByDateRange(startDate, endDate, pageable));
    }

    /**
     * GET /api/interventions/search/advanced - Recherche avancée
     */
    @GetMapping("/search/advanced")
    public ResponseEntity<Page<InterventionResponse>> searchAdvanced(
            @RequestParam(required = false) Long transformateurId,
            @RequestParam(required = false) Long agentId,
            @RequestParam(required = false) StatutIntervention statut,
            @RequestParam(required = false) Priorite priorite,
            @RequestParam(required = false) TypeIntervention typeIntervention,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Pageable pageable) {
        return ResponseEntity.ok(interventionService.searchAdvanced(
                transformateurId,
                agentId,
                statut,
                priorite,
                typeIntervention,
                startDate,
                endDate,
                pageable
        ));
    }

    /**
     * GET /api/interventions/statistics - Obtenir les statistiques
     */
    @GetMapping("/statistics")
    public ResponseEntity<InterventionStatisticsResponse> getStatistics() {
        return ResponseEntity.ok(interventionService.getStatistics());
    }

    /**
     * GET /api/interventions/critical - Obtenir les interventions critiques
     */
    @GetMapping("/critical")
    public ResponseEntity<Page<InterventionResponse>> getCritical(Pageable pageable) {
        return ResponseEntity.ok(interventionService.getCritical(pageable));
    }

    /**
     * GET /api/interventions/ongoing/by-agent/{agentId} - Obtenir les interventions en cours d'un agent
     */
    @GetMapping("/ongoing/by-agent/{agentId}")
    public ResponseEntity<Page<InterventionResponse>> getOngoingByAgent(
            @PathVariable Long agentId,
            Pageable pageable) {
        return ResponseEntity.ok(interventionService.getOngoingByAgent(agentId, pageable));
    }
}
