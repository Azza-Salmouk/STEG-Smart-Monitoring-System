package com.steg.smartmonitoring.measurement.service.impl;

import com.steg.smartmonitoring.measurement.analyzer.MeasurementAnalyzer;
import com.steg.smartmonitoring.measurement.calculator.HealthScoreCalculator;
import com.steg.smartmonitoring.measurement.dto.CreateMeasurementRequest;
import com.steg.smartmonitoring.measurement.dto.MeasurementResponse;
import com.steg.smartmonitoring.measurement.dto.UpdateMeasurementRequest;
import com.steg.smartmonitoring.measurement.entity.Measurement;
import com.steg.smartmonitoring.measurement.entity.MeasurementStatus;
import com.steg.smartmonitoring.measurement.mapper.MeasurementMapper;
import com.steg.smartmonitoring.measurement.repository.MeasurementRepository;
import com.steg.smartmonitoring.measurement.service.MeasurementService;
import com.steg.smartmonitoring.measurement.service.MeasurementStatisticsResponse;
import com.steg.smartmonitoring.transformateur.entity.Transformateur;
import com.steg.smartmonitoring.transformateur.repository.TransformateurRepository;
import com.steg.smartmonitoring.alert.service.AlertGenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MeasurementServiceImpl implements MeasurementService {

    private final MeasurementRepository measurementRepository;
    private final TransformateurRepository transformateurRepository;
    private final MeasurementMapper measurementMapper;
    private final HealthScoreCalculator healthScoreCalculator;
    private final MeasurementAnalyzer measurementAnalyzer;
    private final AlertGenerationService alertGenerationService;

    @Override
    public MeasurementResponse create(CreateMeasurementRequest request) {
        // Vérifier que le transformateur existe
        Transformateur transformateur = transformateurRepository.findById(request.getTransformateurId())
                .orElseThrow(() -> new RuntimeException("Transformateur not found"));
        
        Measurement measurement = new Measurement();
        measurement.setTransformateur(transformateur);
        measurement.setTemperature(request.getTemperature());
        measurement.setVoltage(request.getVoltage());
        measurement.setCurrent(request.getCurrent());
        measurement.setPower(request.getPower());
        measurement.setEnergyConsumption(request.getEnergyConsumption());
        measurement.setFrequency(request.getFrequency());
        measurement.setHumidity(request.getHumidity());
        
        // Calculer Health Score (4 paramètres: temperature, voltage, current, frequency)
        Integer healthScore = healthScoreCalculator.calculateHealthScore(
            request.getTemperature(),
            request.getVoltage(),
            request.getCurrent(),
            request.getFrequency()
        );
        measurement.setHealthScore(healthScore);
        
        // Déterminer le statut (5 paramètres: temperature, voltage, current, frequency, healthScore)
        MeasurementStatus status = measurementAnalyzer.determineMeasurementStatus(
            request.getTemperature(),
            request.getVoltage(),
            request.getCurrent(),
            request.getFrequency(),
            healthScore
        );
        measurement.setStatus(status);
        
        measurement.setMeasurementDate(request.getMeasurementDate() != null 
            ? request.getMeasurementDate() 
            : LocalDateTime.now());
        
        Measurement saved = measurementRepository.save(measurement);
        
        // ✅ Générer les alertes automatiquement à partir de la mesure
        try {
            alertGenerationService.generateAlertsFromMeasurement(saved);
        } catch (Exception e) {
            // Log l'exception mais ne bloque pas la création de la mesure
            System.err.println("Erreur lors de la génération des alertes: " + e.getMessage());
        }
        
        return measurementMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public MeasurementResponse getById(Long id) {
        return measurementRepository.findById(id)
                .map(measurementMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Measurement not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MeasurementResponse> getAll(Pageable pageable) {
        return measurementRepository.findAll(pageable)
                .map(measurementMapper::toResponse);
    }

    @Override
    public MeasurementResponse update(Long id, UpdateMeasurementRequest request) {
        Measurement measurement = measurementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Measurement not found"));
        
        if (request.getTemperature() != null) measurement.setTemperature(request.getTemperature());
        if (request.getVoltage() != null) measurement.setVoltage(request.getVoltage());
        if (request.getCurrent() != null) measurement.setCurrent(request.getCurrent());
        if (request.getPower() != null) measurement.setPower(request.getPower());
        if (request.getEnergyConsumption() != null) measurement.setEnergyConsumption(request.getEnergyConsumption());
        
        Measurement saved = measurementRepository.save(measurement);
        return measurementMapper.toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        measurementRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MeasurementResponse> getHistoryByTransformateur(Long transformateurId, Pageable pageable) {
        return measurementRepository.findByTransformateurIdOrderByMeasurementDateDesc(transformateurId, pageable)
                .map(measurementMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public MeasurementResponse getLatestByTransformateur(Long transformateurId) {
        return measurementRepository.findByTransformateurIdOrderByMeasurementDateDesc(transformateurId)
                .stream()
                .findFirst()
                .map(measurementMapper::toResponse)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MeasurementResponse> getMeasurementsByDateRange(
            Long transformateurId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable) {
        return measurementRepository.findByTransformateurIdAndMeasurementDateBetween(
                transformateurId, startDate, endDate, pageable)
                .map(measurementMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MeasurementResponse> getCriticalMeasurements(Pageable pageable) {
        return measurementRepository.findByStatusOrderByMeasurementDateDesc(MeasurementStatus.CRITICAL, pageable)
                .map(measurementMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MeasurementResponse> getWarningMeasurements(Pageable pageable) {
        return measurementRepository.findByStatusOrderByMeasurementDateDesc(MeasurementStatus.WARNING, pageable)
                .map(measurementMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MeasurementResponse> searchAdvanced(
            Long transformateurId,
            MeasurementStatus status,
            Double minTemperature,
            Double maxTemperature,
            Double minVoltage,
            Double maxVoltage,
            Double minCurrent,
            Integer maxHealthScore,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable) {
        // For now, return all measurements
        return measurementRepository.findAll(pageable)
                .map(measurementMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MeasurementResponse> getTodayMeasurements(Pageable pageable) {
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime tomorrow = today.plusDays(1);
        return measurementRepository.findByMeasurementDateBetween(today, tomorrow, pageable)
                .map(measurementMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public MeasurementStatisticsResponse getStatistics() {
        long total = measurementRepository.count();
        long critical = measurementRepository.countByStatus(MeasurementStatus.CRITICAL);
        long warning = measurementRepository.countByStatus(MeasurementStatus.WARNING);
        
        // Récupérer les agrégats des mesures
        Double averageTemperature = measurementRepository.getAverageTemperature();
        Double minTemperature = measurementRepository.getMinTemperature();
        Double maxTemperature = measurementRepository.getMaxTemperature();
        
        Double averageVoltage = measurementRepository.getAverageVoltage();
        Double averageCurrent = measurementRepository.getAverageCurrent();
        Double averagePower = measurementRepository.getAveragePower();
        
        Double averageHealthScore = measurementRepository.getAverageHealthScore();
        Integer minHealthScore = measurementRepository.getMinHealthScore();
        Integer maxHealthScore = measurementRepository.getMaxHealthScore();
        
        LocalDateTime firstMeasurementDate = measurementRepository.getFirstMeasurementDate();
        LocalDateTime lastMeasurementDate = measurementRepository.getLastMeasurementDate();
        
        Long transformateurCount = measurementRepository.countDistinctTransformateurs();
        
        // Compter les mesures offline (aucune mesure ne peut être offline par statut)
        long offline = 0;
        
        return MeasurementStatisticsResponse.builder()
                .totalMeasurements(total)
                .criticalMeasurements(critical)
                .warningMeasurements(warning)
                .normalMeasurements(total - critical - warning)
                .offlineMeasurements(offline)
                .averageTemperature(averageTemperature)
                .minTemperature(minTemperature)
                .maxTemperature(maxTemperature)
                .averageVoltage(averageVoltage)
                .averageCurrent(averageCurrent)
                .averagePower(averagePower)
                .averageHealthScore(averageHealthScore)
                .minHealthScore(minHealthScore)
                .maxHealthScore(maxHealthScore)
                .firstMeasurementDate(firstMeasurementDate)
                .lastMeasurementDate(lastMeasurementDate)
                .transformateurCount(transformateurCount)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public MeasurementStatisticsResponse getStatisticsByTransformateur(Long transformateurId) {
        long total = measurementRepository.countByTransformateurId(transformateurId);
        long critical = measurementRepository.countByTransformateurIdAndStatus(transformateurId, MeasurementStatus.CRITICAL);
        long warning = measurementRepository.countByTransformateurIdAndStatus(transformateurId, MeasurementStatus.WARNING);
        
        // Pour les statistiques par transformateur, on récupère les mesures de ce transformateur spécifique
        List<Measurement> measurements = measurementRepository.findByTransformateurIdOrderByMeasurementDateDesc(transformateurId);
        
        Double averageTemperature = measurements.stream()
                .map(Measurement::getTemperature)
                .filter(t -> t != null && t > 0)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
        averageTemperature = averageTemperature > 0 ? averageTemperature : null;
        
        Double minTemperature = measurements.stream()
                .map(Measurement::getTemperature)
                .filter(t -> t != null && t > 0)
                .min(Double::compare)
                .orElse(null);
        
        Double maxTemperature = measurements.stream()
                .map(Measurement::getTemperature)
                .filter(t -> t != null && t > 0)
                .max(Double::compare)
                .orElse(null);
        
        Double averageVoltage = measurements.stream()
                .map(Measurement::getVoltage)
                .filter(v -> v != null && v > 0)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
        averageVoltage = averageVoltage > 0 ? averageVoltage : null;
        
        Double averageCurrent = measurements.stream()
                .map(Measurement::getCurrent)
                .filter(c -> c != null && c > 0)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
        averageCurrent = averageCurrent > 0 ? averageCurrent : null;
        
        Double averagePower = measurements.stream()
                .map(Measurement::getPower)
                .filter(p -> p != null && p > 0)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
        averagePower = averagePower > 0 ? averagePower : null;
        
        Double averageHealthScore = measurements.stream()
                .map(Measurement::getHealthScore)
                .filter(h -> h != null && h > 0)
                .mapToDouble(Integer::doubleValue)
                .average()
                .orElse(0.0);
        averageHealthScore = averageHealthScore > 0 ? averageHealthScore : null;
        
        Integer minHealthScore = measurements.stream()
                .map(Measurement::getHealthScore)
                .filter(h -> h != null && h > 0)
                .min(Integer::compare)
                .orElse(null);
        
        Integer maxHealthScore = measurements.stream()
                .map(Measurement::getHealthScore)
                .filter(h -> h != null && h > 0)
                .max(Integer::compare)
                .orElse(null);
        
        LocalDateTime firstMeasurementDate = measurements.stream()
                .map(Measurement::getMeasurementDate)
                .min(LocalDateTime::compareTo)
                .orElse(null);
        
        LocalDateTime lastMeasurementDate = measurements.stream()
                .map(Measurement::getMeasurementDate)
                .max(LocalDateTime::compareTo)
                .orElse(null);
        
        return MeasurementStatisticsResponse.builder()
                .totalMeasurements(total)
                .criticalMeasurements(critical)
                .warningMeasurements(warning)
                .normalMeasurements(total - critical - warning)
                .averageTemperature(averageTemperature)
                .minTemperature(minTemperature)
                .maxTemperature(maxTemperature)
                .averageVoltage(averageVoltage)
                .averageCurrent(averageCurrent)
                .averagePower(averagePower)
                .averageHealthScore(averageHealthScore)
                .minHealthScore(minHealthScore)
                .maxHealthScore(maxHealthScore)
                .firstMeasurementDate(firstMeasurementDate)
                .lastMeasurementDate(lastMeasurementDate)
                .transformateurCount(1L)  // Pour un transformateur spécifique, le count est 1
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public long countCriticalMeasurements() {
        return measurementRepository.countByStatus(MeasurementStatus.CRITICAL);
    }

    @Override
    @Transactional(readOnly = true)
    public long countWarningMeasurements() {
        return measurementRepository.countByStatus(MeasurementStatus.WARNING);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> getTransformateursWithCriticalMeasurements() {
        return measurementRepository.findDistinctTransformateurIdByStatus(MeasurementStatus.CRITICAL);
    }

    @Override
    @Transactional(readOnly = true)
    public Double getAverageHealthScore() {
        return measurementRepository.getAverageHealthScore();
    }

    @Override
    @Transactional(readOnly = true)
    public Double getAverageHealthScoreByTransformateur(Long transformateurId) {
        return measurementRepository.getAverageHealthScoreByTransformateur(transformateurId);
    }
}
