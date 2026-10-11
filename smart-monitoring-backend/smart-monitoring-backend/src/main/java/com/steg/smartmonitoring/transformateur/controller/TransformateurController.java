package com.steg.smartmonitoring.transformateur.controller;

import com.steg.smartmonitoring.common.excel.ExcelImportResult;
import com.steg.smartmonitoring.transformateur.dto.CreateTransformateurRequest;
import com.steg.smartmonitoring.transformateur.dto.TransformateurResponse;
import com.steg.smartmonitoring.transformateur.dto.UpdateTransformateurRequest;
import com.steg.smartmonitoring.transformateur.entity.RiskLevel;
import com.steg.smartmonitoring.transformateur.entity.StatusTransformateur;
import com.steg.smartmonitoring.transformateur.excel.TransformateurExcelImporter;
import com.steg.smartmonitoring.transformateur.service.TransformateurService;
import com.steg.smartmonitoring.transformateur.service.TransformateurStatisticsResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des transformateurs
 */
@RestController
@RequestMapping("/api/transformateurs")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class TransformateurController {

    private final TransformateurService transformateurService;
    private final TransformateurExcelImporter transformateurExcelImporter;

    /**
     * GET /api/transformateurs - Récupérer tous les transformateurs (paginé)
     */
    @GetMapping
    public ResponseEntity<Page<TransformateurResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(transformateurService.getAll(pageable));
    }

    /**
     * GET /api/transformateurs/{id} - Récupérer un transformateur par ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransformateurResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(transformateurService.getById(id));
    }

    /**
     * POST /api/transformateurs - Créer un nouveau transformateur
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<TransformateurResponse> create(@Valid @RequestBody CreateTransformateurRequest request) {
        TransformateurResponse response = transformateurService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * PUT /api/transformateurs/{id} - Mettre à jour un transformateur
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<TransformateurResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTransformateurRequest request) {
        TransformateurResponse response = transformateurService.update(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/transformateurs/{id} - Supprimer un transformateur
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        transformateurService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/transformateurs/by-poste/{posteId} - Obtenir les transformateurs d'un poste
     */
    @GetMapping("/by-poste/{posteId}")
    public ResponseEntity<List<TransformateurResponse>> getByPosteId(@PathVariable Long posteId) {
        return ResponseEntity.ok(transformateurService.getByPosteId(posteId));
    }

    /**
     * GET /api/transformateurs/search/by-reference - Recherche par référence
     */
    @GetMapping("/search/by-reference")
    public ResponseEntity<Page<TransformateurResponse>> searchByReference(
            @RequestParam String reference,
            Pageable pageable) {
        return ResponseEntity.ok(transformateurService.searchByReference(reference, pageable));
    }

    /**
     * GET /api/transformateurs/search/by-manufacturer - Recherche par fabricant
     */
    @GetMapping("/search/by-manufacturer")
    public ResponseEntity<Page<TransformateurResponse>> searchByManufacturer(
            @RequestParam String manufacturer,
            Pageable pageable) {
        return ResponseEntity.ok(transformateurService.searchByManufacturer(manufacturer, pageable));
    }

    /**
     * GET /api/transformateurs/search/by-status - Recherche par status
     */
    @GetMapping("/search/by-status")
    public ResponseEntity<Page<TransformateurResponse>> searchByStatus(
            @RequestParam StatusTransformateur status,
            Pageable pageable) {
        return ResponseEntity.ok(transformateurService.searchByStatus(status, pageable));
    }

    /**
     * GET /api/transformateurs/search/by-risk-level - Recherche par niveau de risque
     */
    @GetMapping("/search/by-risk-level")
    public ResponseEntity<Page<TransformateurResponse>> searchByRiskLevel(
            @RequestParam RiskLevel riskLevel,
            Pageable pageable) {
        return ResponseEntity.ok(transformateurService.searchByRiskLevel(riskLevel, pageable));
    }

    /**
     * GET /api/transformateurs/search/by-health-score - Recherche par health score
     */
    @GetMapping("/search/by-health-score")
    public ResponseEntity<Page<TransformateurResponse>> searchByHealthScore(
            @RequestParam Integer maxScore,
            Pageable pageable) {
        return ResponseEntity.ok(transformateurService.searchByHealthScore(maxScore, pageable));
    }

    /**
     * GET /api/transformateurs/search/advanced - Recherche avancée
     */
    @GetMapping("/search/advanced")
    public ResponseEntity<Page<TransformateurResponse>> search(
            @RequestParam(required = false) String reference,
            @RequestParam(required = false) String manufacturer,
            @RequestParam(required = false) StatusTransformateur status,
            @RequestParam(required = false) RiskLevel riskLevel,
            @RequestParam(required = false) Integer healthScoreMax,
            @RequestParam(required = false) Long posteId,
            Pageable pageable) {
        return ResponseEntity.ok(transformateurService.search(reference, manufacturer, status, riskLevel, healthScoreMax, posteId, pageable));
    }

    /**
     * GET /api/transformateurs/alerting - Obtenir les transformateurs en alerte
     */
    @GetMapping("/alerting")
    public ResponseEntity<List<TransformateurResponse>> getAlertingTransformateurs() {
        return ResponseEntity.ok(transformateurService.getAlertingTransformateurs());
    }

    /**
     * GET /api/transformateurs/critical - Obtenir les transformateurs critiques
     */
    @GetMapping("/critical")
    public ResponseEntity<List<TransformateurResponse>> getCriticalTransformateurs() {
        return ResponseEntity.ok(transformateurService.getCriticalTransformateurs());
    }

    /**
     * GET /api/transformateurs/statistics - Obtenir les statistiques
     */
    @GetMapping("/statistics")
    public ResponseEntity<TransformateurStatisticsResponse> getStatistics() {
        return ResponseEntity.ok(transformateurService.getStatistics());
    }

    /**
     * POST /api/transformateurs/import - Importer des transformateurs depuis Excel
     */
    @PostMapping("/import")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<ExcelImportResult> importTransformateurs(@RequestParam("file") MultipartFile file) {
        try {
            ExcelImportResult result = transformateurExcelImporter.importTransformateurs(file);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            ExcelImportResult error = new ExcelImportResult();
            error.addError("Import failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
