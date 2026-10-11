package com.steg.smartmonitoring.demo.controller;

import com.steg.smartmonitoring.demo.service.DemoDataGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Contrôleur pour la génération de données de démonstration
 * Accessible uniquement aux administrateurs
 */
@Slf4j
@RestController
@RequestMapping("/api/demo")
@RequiredArgsConstructor
public class DemoDataController {

    private final DemoDataGeneratorService demoDataGeneratorService;

    /**
     * POST /api/demo/generate-data - Générer des données de démonstration
     * Crée des postes, transformateurs, mesures, alertes, interventions et plaintes
     */
    @PostMapping("/generate-data")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> generateDemoData() {
        try {
            log.info("Début de la génération des données de démonstration");
            DemoDataGeneratorService.GeneratedDataSummary summary = demoDataGeneratorService.generateAllData();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Données de démonstration générées avec succès");
            response.put("summary", summary);
            
            log.info("Génération complétée: {} postes, {} transformateurs, {} mesures, {} alertes, {} interventions, {} plaintes",
                    summary.getPostesCount(),
                    summary.getTransformateursCount(),
                    summary.getMeasurementsCount(),
                    summary.getAlertsCount(),
                    summary.getInterventionsCount(),
                    summary.getComplaintsCount());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Erreur lors de la génération des données de démonstration", e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Erreur lors de la génération des données: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * POST /api/demo/generate-data/postes - Générer uniquement des postes
     */
    @PostMapping("/generate-data/postes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> generatePostes() {
        try {
            log.info("Génération des postes uniquement");
            int count = demoDataGeneratorService.generatePostes();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("count", count);
            response.put("message", count + " postes générés");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Erreur lors de la génération des postes", e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Erreur: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * POST /api/demo/generate-data/transformateurs - Générer uniquement des transformateurs
     */
    @PostMapping("/generate-data/transformateurs")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> generateTransformateurs() {
        try {
            log.info("Génération des transformateurs uniquement");
            int count = demoDataGeneratorService.generateTransformateurs();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("count", count);
            response.put("message", count + " transformateurs générés");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Erreur lors de la génération des transformateurs", e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Erreur: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * POST /api/demo/generate-data/measurements - Générer uniquement des mesures
     */
    @PostMapping("/generate-data/measurements")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> generateMeasurements() {
        try {
            log.info("Génération des mesures uniquement");
            int count = demoDataGeneratorService.generateMeasurements();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("count", count);
            response.put("message", count + " mesures générées");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Erreur lors de la génération des mesures", e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Erreur: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * POST /api/demo/clear-data - Supprimer toutes les données de démonstration
     */
    @PostMapping("/clear-data")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> clearDemoData() {
        try {
            log.info("Suppression des données de démonstration");
            demoDataGeneratorService.clearAllData();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Toutes les données ont été supprimées");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Erreur lors de la suppression des données", e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Erreur: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * GET /api/demo/status - Obtenir l'état de la base de données
     */
    @GetMapping("/status")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, Object>> getDatabaseStatus() {
        try {
            Map<String, Object> status = demoDataGeneratorService.getDatabaseStatus();
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            log.error("Erreur lors de la récupération du statut", e);
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Erreur: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
