package com.steg.smartmonitoring.prediction.controller;

import com.steg.smartmonitoring.prediction.dto.FailurePredictionResult;
import com.steg.smartmonitoring.prediction.service.FailurePredictionService;
import com.steg.smartmonitoring.prediction.service.MaintenanceRecommendation;
import com.steg.smartmonitoring.transformateur.entity.Transformateur;
import com.steg.smartmonitoring.transformateur.repository.TransformateurRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controller for transformer failure prediction and maintenance recommendations
 */
@RestController
@RequestMapping("/api/prediction")
public class PredictionController {

    private final FailurePredictionService failurePredictionService;
    private final TransformateurRepository transformateurRepository;

    public PredictionController(
        FailurePredictionService failurePredictionService,
        TransformateurRepository transformateurRepository
    ) {
        this.failurePredictionService = failurePredictionService;
        this.transformateurRepository = transformateurRepository;
    }

    /**
     * Get failure prediction for a specific transformateur
     */
    @GetMapping("/transformateurs/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR', 'AGENT')")
    public ResponseEntity<?> getPrediction(@PathVariable Long id) {
        Optional<Transformateur> transformateur = transformateurRepository.findById(id);

        if (transformateur.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("Transformateur not found"));
        }

        FailurePredictionResult prediction = failurePredictionService.predictFailure(transformateur.get());

        return ResponseEntity.ok(prediction);
    }

    /**
     * Get maintenance recommendations for a specific transformateur
     */
    @GetMapping("/transformateurs/{id}/recommendations")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR', 'AGENT')")
    public ResponseEntity<?> getRecommendations(@PathVariable Long id) {
        Optional<Transformateur> transformateur = transformateurRepository.findById(id);

        if (transformateur.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("Transformateur not found"));
        }

        MaintenanceRecommendation recommendation = failurePredictionService.getRecommendations(transformateur.get());

        return ResponseEntity.ok(recommendation);
    }

    /**
     * Get predictions for all transformateurs
     */
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<?> getAllPredictions() {
        var predictions = transformateurRepository.findAll().stream()
            .map(failurePredictionService::predictFailure)
            .toList();

        return ResponseEntity.ok(predictions);
    }

    /**
     * Get high-risk transformateurs (risk score > 60)
     */
    @GetMapping("/high-risk")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR', 'AGENT')")
    public ResponseEntity<?> getHighRiskTransformateurs() {
        var highRisk = transformateurRepository.findAll().stream()
            .map(failurePredictionService::predictFailure)
            .filter(p -> p.getFailureRiskScore() != null && p.getFailureRiskScore() > 60)
            .toList();

        return ResponseEntity.ok(highRisk);
    }

    /**
     * Error response DTO
     */
    public record ErrorResponse(String message) {
    }
}
