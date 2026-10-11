package com.steg.smartmonitoring.alert.controller;

import com.steg.smartmonitoring.alert.dto.AlertResponse;
import com.steg.smartmonitoring.alert.dto.AlertStatisticsResponse;
import com.steg.smartmonitoring.alert.dto.CreateAlertRequest;
import com.steg.smartmonitoring.alert.dto.UpdateAlertRequest;
import com.steg.smartmonitoring.alert.entity.AlertLevel;
import com.steg.smartmonitoring.alert.entity.AlertStatus;
import com.steg.smartmonitoring.alert.entity.AlertType;
import com.steg.smartmonitoring.alert.service.AlertService;
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
 * Contrôleur REST pour la gestion des alertes
 */
@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class AlertController {

    private final AlertService alertService;

    /**
     * GET /api/alerts - Récupérer toutes les alertes (paginé)
     */
    @GetMapping
    public ResponseEntity<Page<AlertResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(alertService.getAll(pageable));
    }

    /**
     * GET /api/alerts/{id} - Récupérer une alerte par ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlertResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(alertService.getById(id));
    }

    /**
     * POST /api/alerts - Créer une nouvelle alerte (ADMIN/SUPERVISOR)
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<AlertResponse> create(@Valid @RequestBody CreateAlertRequest request) {
        AlertResponse response = alertService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * PUT /api/alerts/{id} - Mettre à jour une alerte (ADMIN/SUPERVISOR)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<AlertResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAlertRequest request) {
        AlertResponse response = alertService.update(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/alerts/{id} - Supprimer une alerte (ADMIN uniquement)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        alertService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/alerts/search/by-statut - Recherche par statut
     */
    @GetMapping("/search/by-statut")
    public ResponseEntity<Page<AlertResponse>> getByStatut(
            @RequestParam AlertStatus statut,
            Pageable pageable) {
        return ResponseEntity.ok(alertService.getByStatut(statut, pageable));
    }

    /**
     * GET /api/alerts/search/by-niveau - Recherche par niveau
     */
    @GetMapping("/search/by-niveau")
    public ResponseEntity<Page<AlertResponse>> getByNiveau(
            @RequestParam AlertLevel niveau,
            Pageable pageable) {
        return ResponseEntity.ok(alertService.getByNiveau(niveau, pageable));
    }

    /**
     * GET /api/alerts/search/by-type - Recherche par type
     */
    @GetMapping("/search/by-type")
    public ResponseEntity<Page<AlertResponse>> getByType(
            @RequestParam AlertType type,
            Pageable pageable) {
        return ResponseEntity.ok(alertService.getByType(type, pageable));
    }

    /**
     * GET /api/alerts/search/by-titre - Recherche par titre
     */
    @GetMapping("/search/by-titre")
    public ResponseEntity<Page<AlertResponse>> searchByTitre(
            @RequestParam String titre,
            Pageable pageable) {
        return ResponseEntity.ok(alertService.searchByTitre(titre, pageable));
    }

    /**
     * GET /api/alerts/by-transformateur/{transformateurId} - Alertes d'un transformateur
     */
    @GetMapping("/by-transformateur/{transformateurId}")
    public ResponseEntity<Page<AlertResponse>> getByTransformateur(
            @PathVariable Long transformateurId,
            Pageable pageable) {
        return ResponseEntity.ok(alertService.getByTransformateur(transformateurId, pageable));
    }

    /**
     * GET /api/alerts/by-poste/{posteId} - Alertes d'un poste
     */
    @GetMapping("/by-poste/{posteId}")
    public ResponseEntity<Page<AlertResponse>> getByPoste(
            @PathVariable Long posteId,
            Pageable pageable) {
        return ResponseEntity.ok(alertService.getByPoste(posteId, pageable));
    }

    /**
     * GET /api/alerts/search/by-date-range - Recherche par période
     */
    @GetMapping("/search/by-date-range")
    public ResponseEntity<Page<AlertResponse>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Pageable pageable) {
        return ResponseEntity.ok(alertService.getByDateRange(startDate, endDate, pageable));
    }

    /**
     * GET /api/alerts/search/advanced - Recherche avancée
     */
    @GetMapping("/search/advanced")
    public ResponseEntity<Page<AlertResponse>> searchAdvanced(
            @RequestParam(required = false) String titre,
            @RequestParam(required = false) AlertStatus statut,
            @RequestParam(required = false) AlertLevel niveau,
            @RequestParam(required = false) AlertType type,
            @RequestParam(required = false) Long transformateurId,
            @RequestParam(required = false) Long posteId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Pageable pageable) {
        return ResponseEntity.ok(alertService.searchAdvanced(
                titre,
                statut,
                niveau,
                type,
                transformateurId,
                posteId,
                startDate,
                endDate,
                pageable
        ));
    }

    /**
     * GET /api/alerts/critical - Obtenir les alertes critiques
     */
    @GetMapping("/critical")
    public ResponseEntity<Page<AlertResponse>> getCritical(Pageable pageable) {
        return ResponseEntity.ok(alertService.getCritical(pageable));
    }

    /**
     * GET /api/alerts/active - Obtenir les alertes actives
     */
    @GetMapping("/active")
    public ResponseEntity<Page<AlertResponse>> getActive(Pageable pageable) {
        return ResponseEntity.ok(alertService.getActive(pageable));
    }

    /**
     * GET /api/alerts/recent - Obtenir les alertes récentes
     */
    @GetMapping("/recent")
    public ResponseEntity<Page<AlertResponse>> getRecent(Pageable pageable) {
        return ResponseEntity.ok(alertService.getRecent(pageable));
    }

    /**
     * GET /api/alerts/statistics - Obtenir les statistiques
     */
    @GetMapping("/statistics")
    public ResponseEntity<AlertStatisticsResponse> getStatistics() {
        return ResponseEntity.ok(alertService.getStatistics());
    }

    /**
     * POST /api/alerts/{id}/acknowledge - Reconnaître une alerte (ADMIN/SUPERVISOR/AGENT)
     */
    @PostMapping("/{id}/acknowledge")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR', 'AGENT')")
    public ResponseEntity<AlertResponse> acknowledge(@PathVariable Long id) {
        return ResponseEntity.ok(alertService.acknowledge(id));
    }

    /**
     * POST /api/alerts/{id}/resolve/{userId} - Résoudre une alerte (ADMIN/SUPERVISOR)
     */
    @PostMapping("/{id}/resolve/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<AlertResponse> resolve(
            @PathVariable Long id,
            @PathVariable Long userId) {
        return ResponseEntity.ok(alertService.resolve(id, userId));
    }

    /**
     * POST /api/alerts/{id}/archive - Archiver une alerte (ADMIN/SUPERVISOR)
     */
    @PostMapping("/{id}/archive")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<AlertResponse> archive(@PathVariable Long id) {
        return ResponseEntity.ok(alertService.archive(id));
    }

    /**
     * PUT /api/alerts/{id}/status - Changer le statut d'une alerte (ADMIN/SUPERVISOR)
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<AlertResponse> changeStatus(
            @PathVariable Long id,
            @RequestParam AlertStatus newStatus) {
        return ResponseEntity.ok(alertService.changeStatus(id, newStatus));
    }
}
