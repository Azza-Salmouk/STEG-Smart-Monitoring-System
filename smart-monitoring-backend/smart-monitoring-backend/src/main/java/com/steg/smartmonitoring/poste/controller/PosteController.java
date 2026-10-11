package com.steg.smartmonitoring.poste.controller;

import com.steg.smartmonitoring.common.excel.ExcelImportResult;
import com.steg.smartmonitoring.poste.dto.CreatePosteRequest;
import com.steg.smartmonitoring.poste.dto.PosteResponse;
import com.steg.smartmonitoring.poste.dto.UpdatePosteRequest;
import com.steg.smartmonitoring.poste.entity.EtatPoste;
import com.steg.smartmonitoring.poste.entity.TypePoste;
import com.steg.smartmonitoring.poste.excel.PosteExcelImporter;
import com.steg.smartmonitoring.poste.service.PosteService;
import com.steg.smartmonitoring.poste.service.PosteStatisticsResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Contrôleur REST pour la gestion des postes
 */
@RestController
@RequestMapping("/api/postes")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class PosteController {

    private final PosteService posteService;
    private final PosteExcelImporter posteExcelImporter;

    /**
     * GET /api/postes - Récupérer tous les postes (paginé)
     */
    @GetMapping
    public ResponseEntity<Page<PosteResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(posteService.getAll(pageable));
    }

    /**
     * GET /api/postes/{id} - Récupérer un poste par ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<PosteResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(posteService.getById(id));
    }

    /**
     * POST /api/postes - Créer un nouveau poste (Admin seulement)
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PosteResponse> create(@Valid @RequestBody CreatePosteRequest request) {
        PosteResponse response = posteService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * PUT /api/postes/{id} - Mettre à jour un poste (Admin/Supervisor)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<PosteResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePosteRequest request) {
        PosteResponse response = posteService.update(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/postes/{id} - Supprimer un poste (Admin seulement)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        posteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/postes/search/by-nom - Recherche par nom
     */
    @GetMapping("/search/by-nom")
    public ResponseEntity<Page<PosteResponse>> searchByNom(
            @RequestParam String nom,
            Pageable pageable) {
        return ResponseEntity.ok(posteService.searchByNom(nom, pageable));
    }

    /**
     * GET /api/postes/search/by-region - Recherche par région
     */
    @GetMapping("/search/by-region")
    public ResponseEntity<Page<PosteResponse>> searchByRegion(
            @RequestParam String region,
            Pageable pageable) {
        return ResponseEntity.ok(posteService.searchByRegion(region, pageable));
    }

    /**
     * GET /api/postes/search/by-etat - Recherche par état
     */
    @GetMapping("/search/by-etat")
    public ResponseEntity<Page<PosteResponse>> searchByEtat(
            @RequestParam EtatPoste etat,
            Pageable pageable) {
        return ResponseEntity.ok(posteService.searchByEtat(etat, pageable));
    }

    /**
     * GET /api/postes/search/by-type - Recherche par type
     */
    @GetMapping("/search/by-type")
    public ResponseEntity<Page<PosteResponse>> searchByType(
            @RequestParam TypePoste typePoste,
            Pageable pageable) {
        return ResponseEntity.ok(posteService.searchByType(typePoste, pageable));
    }

    /**
     * GET /api/postes/search/advanced - Recherche avancée
     */
    @GetMapping("/search/advanced")
    public ResponseEntity<Page<PosteResponse>> search(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) EtatPoste etat,
            @RequestParam(required = false) TypePoste typePoste,
            Pageable pageable) {
        return ResponseEntity.ok(posteService.search(nom, region, etat, typePoste, pageable));
    }

    /**
     * GET /api/postes/statistics - Obtenir les statistiques
     */
    @GetMapping("/statistics")
    public ResponseEntity<PosteStatisticsResponse> getStatistics() {
        return ResponseEntity.ok(posteService.getStatistics());
    }

    /**
     * POST /api/postes/import - Importer des postes depuis Excel
     */
    @PostMapping("/import")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<ExcelImportResult> importPostes(@RequestParam("file") MultipartFile file) {
        try {
            ExcelImportResult result = posteExcelImporter.importPostes(file);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            ExcelImportResult error = new ExcelImportResult();
            error.addError("Import failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
