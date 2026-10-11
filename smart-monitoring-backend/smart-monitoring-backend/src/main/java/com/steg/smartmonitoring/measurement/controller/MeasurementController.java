package com.steg.smartmonitoring.measurement.controller;

import com.steg.smartmonitoring.measurement.dto.CreateMeasurementRequest;
import com.steg.smartmonitoring.measurement.dto.MeasurementResponse;
import com.steg.smartmonitoring.measurement.dto.UpdateMeasurementRequest;
import com.steg.smartmonitoring.measurement.entity.MeasurementStatus;
import com.steg.smartmonitoring.measurement.service.MeasurementService;
import com.steg.smartmonitoring.measurement.service.MeasurementStatisticsResponse;
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
 * Contrôleur REST pour la gestion des mesures IoT
 */
@RestController
@RequestMapping("/api/measurements")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class MeasurementController {

    private final MeasurementService measurementService;

    @GetMapping
    public ResponseEntity<Page<MeasurementResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(measurementService.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeasurementResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(measurementService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR', 'AGENT')")
    public ResponseEntity<MeasurementResponse> create(@Valid @RequestBody CreateMeasurementRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(measurementService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<MeasurementResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateMeasurementRequest request) {
        return ResponseEntity.ok(measurementService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        measurementService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-transformateur/{transformateurId}")
    public ResponseEntity<Page<MeasurementResponse>> getByTransformateur(
            @PathVariable Long transformateurId,
            Pageable pageable) {
        return ResponseEntity.ok(measurementService.getHistoryByTransformateur(transformateurId, pageable));
    }

    @GetMapping("/latest/transformateur/{transformateurId}")
    public ResponseEntity<MeasurementResponse> getLatestByTransformateur(
            @PathVariable Long transformateurId) {
        return ResponseEntity.ok(measurementService.getLatestByTransformateur(transformateurId));
    }

    @GetMapping("/latest")
    public ResponseEntity<MeasurementResponse> getLatest(
            @RequestParam Long transformateurId) {
        return ResponseEntity.ok(measurementService.getLatestByTransformateur(transformateurId));
    }

    @GetMapping("/history/{transformateurId}")
    public ResponseEntity<Page<MeasurementResponse>> getHistory(
            @PathVariable Long transformateurId,
            Pageable pageable) {
        return ResponseEntity.ok(measurementService.getHistoryByTransformateur(transformateurId, pageable));
    }

    @GetMapping("/search/by-date-range")
    public ResponseEntity<Page<MeasurementResponse>> getByDateRange(
            @RequestParam Long transformateurId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Pageable pageable) {
        return ResponseEntity.ok(measurementService.getMeasurementsByDateRange(transformateurId, startDate, endDate, pageable));
    }

    @GetMapping("/critical")
    public ResponseEntity<Page<MeasurementResponse>> getCritical(Pageable pageable) {
        return ResponseEntity.ok(measurementService.getCriticalMeasurements(pageable));
    }

    @GetMapping("/warning")
    public ResponseEntity<Page<MeasurementResponse>> getWarning(Pageable pageable) {
        return ResponseEntity.ok(measurementService.getWarningMeasurements(pageable));
    }

    @GetMapping("/today")
    public ResponseEntity<Page<MeasurementResponse>> getTodayMeasurements(Pageable pageable) {
        return ResponseEntity.ok(measurementService.getTodayMeasurements(pageable));
    }

    @GetMapping("/statistics")
    public ResponseEntity<MeasurementStatisticsResponse> getStatistics() {
        return ResponseEntity.ok(measurementService.getStatistics());
    }

    @GetMapping("/statistics/{transformateurId}")
    public ResponseEntity<MeasurementStatisticsResponse> getStatisticsByTransformateur(
            @PathVariable Long transformateurId) {
        return ResponseEntity.ok(measurementService.getStatisticsByTransformateur(transformateurId));
    }

    @GetMapping("/statistics/transformateur/{transformateurId}")
    public ResponseEntity<MeasurementStatisticsResponse> getStatisticsByTransformateurAlt(
            @PathVariable Long transformateurId) {
        return ResponseEntity.ok(measurementService.getStatisticsByTransformateur(transformateurId));
    }

    @GetMapping("/search/advanced")
    public ResponseEntity<Page<MeasurementResponse>> searchAdvanced(
            @RequestParam(required = false) Long transformateurId,
            @RequestParam(required = false) MeasurementStatus status,
            @RequestParam(required = false) Double minTemperature,
            @RequestParam(required = false) Double maxTemperature,
            @RequestParam(required = false) Double minVoltage,
            @RequestParam(required = false) Double maxVoltage,
            @RequestParam(required = false) Double minCurrent,
            @RequestParam(required = false) Integer maxHealthScore,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Pageable pageable) {
        return ResponseEntity.ok(measurementService.searchAdvanced(
            transformateurId, status, minTemperature, maxTemperature, 
            minVoltage, maxVoltage, minCurrent, maxHealthScore, 
            startDate, endDate, pageable));
    }

    @GetMapping("/count/critical")
    public ResponseEntity<Long> countCritical() {
        return ResponseEntity.ok(measurementService.countCriticalMeasurements());
    }

    @GetMapping("/transformateurs/critical")
    public ResponseEntity<?> getTransformateursWithCritical() {
        return ResponseEntity.ok(measurementService.getTransformateursWithCriticalMeasurements());
    }

    @GetMapping("/health-score/average")
    public ResponseEntity<Double> getAverageHealthScore() {
        return ResponseEntity.ok(measurementService.getAverageHealthScore());
    }

    @GetMapping("/health-score/average/{transformateurId}")
    public ResponseEntity<Double> getAverageHealthScoreByTransformateur(
            @PathVariable Long transformateurId) {
        return ResponseEntity.ok(measurementService.getAverageHealthScoreByTransformateur(transformateurId));
    }
}