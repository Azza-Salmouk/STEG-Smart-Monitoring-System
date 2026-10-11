package com.steg.smartmonitoring.complaint.controller;

import com.steg.smartmonitoring.complaint.dto.*;
import com.steg.smartmonitoring.complaint.entity.ComplaintCategory;
import com.steg.smartmonitoring.complaint.entity.ComplaintPriority;
import com.steg.smartmonitoring.complaint.entity.ComplaintStatus;
import com.steg.smartmonitoring.complaint.service.ComplaintService;
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
 * Contrôleur REST pour la gestion des réclamations
 */
@RestController
@RequestMapping("/api/complaints")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class ComplaintController {

    private final ComplaintService complaintService;

    /**
     * GET /api/complaints - Récupérer toutes les réclamations (paginé)
     */
    @GetMapping
    public ResponseEntity<Page<ComplaintResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(complaintService.getAll(pageable));
    }

    /**
     * GET /api/complaints/{id} - Récupérer une réclamation par ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ComplaintResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(complaintService.getById(id));
    }

    /**
     * POST /api/complaints - Créer une nouvelle réclamation
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR', 'SERVICE_CLIENT')")
    public ResponseEntity<ComplaintResponse> create(@Valid @RequestBody CreateComplaintRequest request) {
        ComplaintResponse response = complaintService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * PUT /api/complaints/{id} - Mettre à jour une réclamation
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR', 'SERVICE_CLIENT')")
    public ResponseEntity<ComplaintResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateComplaintRequest request) {
        ComplaintResponse response = complaintService.update(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/complaints/{id} - Supprimer une réclamation (ADMIN/SUPERVISOR uniquement)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        complaintService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/complaints/search/by-statut - Recherche par statut
     */
    @GetMapping("/search/by-statut")
    public ResponseEntity<Page<ComplaintResponse>> getByStatut(
            @RequestParam ComplaintStatus statut,
            Pageable pageable) {
        return ResponseEntity.ok(complaintService.getByStatut(statut, pageable));
    }

    /**
     * GET /api/complaints/search/by-priorite - Recherche par priorité
     */
    @GetMapping("/search/by-priorite")
    public ResponseEntity<Page<ComplaintResponse>> getByPriorite(
            @RequestParam ComplaintPriority priorite,
            Pageable pageable) {
        return ResponseEntity.ok(complaintService.getByPriorite(priorite, pageable));
    }

    /**
     * GET /api/complaints/search/by-categorie - Recherche par catégorie
     */
    @GetMapping("/search/by-categorie")
    public ResponseEntity<Page<ComplaintResponse>> getByCategorie(
            @RequestParam ComplaintCategory categorie,
            Pageable pageable) {
        return ResponseEntity.ok(complaintService.getByCategorie(categorie, pageable));
    }

    /**
     * GET /api/complaints/search/by-client - Recherche par client
     */
    @GetMapping("/search/by-client")
    public ResponseEntity<Page<ComplaintResponse>> searchByClient(
            @RequestParam String nomClient,
            Pageable pageable) {
        return ResponseEntity.ok(complaintService.searchByClient(nomClient, pageable));
    }

    /**
     * GET /api/complaints/by-supervisor/{supervisorId} - Réclamations d'un superviseur
     */
    @GetMapping("/by-supervisor/{supervisorId}")
    public ResponseEntity<Page<ComplaintResponse>> getBySupervisor(
            @PathVariable Long supervisorId,
            Pageable pageable) {
        return ResponseEntity.ok(complaintService.getBySupervisor(supervisorId, pageable));
    }

    /**
     * GET /api/complaints/by-poste/{posteId} - Réclamations d'un poste
     */
    @GetMapping("/by-poste/{posteId}")
    public ResponseEntity<Page<ComplaintResponse>> getByPoste(
            @PathVariable Long posteId,
            Pageable pageable) {
        return ResponseEntity.ok(complaintService.getByPoste(posteId, pageable));
    }

    /**
     * GET /api/complaints/by-transformateur/{transformateurId} - Réclamations d'un transformateur
     */
    @GetMapping("/by-transformateur/{transformateurId}")
    public ResponseEntity<Page<ComplaintResponse>> getByTransformateur(
            @PathVariable Long transformateurId,
            Pageable pageable) {
        return ResponseEntity.ok(complaintService.getByTransformateur(transformateurId, pageable));
    }

    /**
     * GET /api/complaints/search/by-date-range - Recherche par période
     */
    @GetMapping("/search/by-date-range")
    public ResponseEntity<Page<ComplaintResponse>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Pageable pageable) {
        return ResponseEntity.ok(complaintService.getByDateRange(startDate, endDate, pageable));
    }

    /**
     * GET /api/complaints/search/advanced - Recherche avancée
     */
    @GetMapping("/search/advanced")
    public ResponseEntity<Page<ComplaintResponse>> searchAdvanced(
            @RequestParam(required = false) String nomClient,
            @RequestParam(required = false) ComplaintStatus statut,
            @RequestParam(required = false) ComplaintPriority priorite,
            @RequestParam(required = false) ComplaintCategory categorie,
            @RequestParam(required = false) Long posteId,
            @RequestParam(required = false) Long transformateurId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Pageable pageable) {
        return ResponseEntity.ok(complaintService.searchAdvanced(
                nomClient,
                statut,
                priorite,
                categorie,
                posteId,
                transformateurId,
                startDate,
                endDate,
                pageable
        ));
    }

    /**
     * GET /api/complaints/statistics - Obtenir les statistiques
     */
    @GetMapping("/statistics")
    public ResponseEntity<ComplaintStatisticsResponse> getStatistics() {
        return ResponseEntity.ok(complaintService.getStatistics());
    }

    /**
     * GET /api/complaints/critical - Obtenir les réclamations critiques
     */
    @GetMapping("/critical")
    public ResponseEntity<Page<ComplaintResponse>> getCritical(Pageable pageable) {
        return ResponseEntity.ok(complaintService.getCritical(pageable));
    }

    /**
     * POST /api/complaints/{id}/assign/{supervisorId} - Assigner une réclamation
     */
    @PostMapping("/{id}/assign/{supervisorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<ComplaintResponse> assign(
            @PathVariable Long id,
            @PathVariable Long supervisorId) {
        return ResponseEntity.ok(complaintService.assign(id, supervisorId));
    }
}
