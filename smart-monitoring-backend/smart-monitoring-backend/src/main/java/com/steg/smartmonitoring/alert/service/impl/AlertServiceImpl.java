package com.steg.smartmonitoring.alert.service.impl;

import com.steg.smartmonitoring.alert.dto.AlertResponse;
import com.steg.smartmonitoring.alert.dto.AlertStatisticsResponse;
import com.steg.smartmonitoring.alert.dto.CreateAlertRequest;
import com.steg.smartmonitoring.alert.dto.UpdateAlertRequest;
import com.steg.smartmonitoring.alert.entity.Alert;
import com.steg.smartmonitoring.alert.entity.AlertLevel;
import com.steg.smartmonitoring.alert.entity.AlertStatus;
import com.steg.smartmonitoring.alert.entity.AlertType;
import com.steg.smartmonitoring.alert.mapper.AlertMapper;
import com.steg.smartmonitoring.alert.repository.AlertRepository;
import com.steg.smartmonitoring.alert.service.AlertService;
import com.steg.smartmonitoring.common.exception.ResourceNotFoundException;
import com.steg.smartmonitoring.measurement.entity.Measurement;
import com.steg.smartmonitoring.measurement.repository.MeasurementRepository;
import com.steg.smartmonitoring.poste.entity.Poste;
import com.steg.smartmonitoring.poste.repository.PosteRepository;
import com.steg.smartmonitoring.transformateur.entity.Transformateur;
import com.steg.smartmonitoring.transformateur.repository.TransformateurRepository;
import com.steg.smartmonitoring.user.entity.User;
import com.steg.smartmonitoring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Implémentation du service de gestion des alertes
 */
@Service
@Transactional
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {

    private static final Logger log = LoggerFactory.getLogger(AlertServiceImpl.class);

    private final AlertRepository alertRepository;
    private final AlertMapper alertMapper;
    private final TransformateurRepository transformateurRepository;
    private final PosteRepository posteRepository;
    private final MeasurementRepository measurementRepository;
    private final UserRepository userRepository;

    @Override
    public AlertResponse create(CreateAlertRequest request) {
        log.debug("Creating new alert: {}", request.getTitre());

        // Charger le transformateur (obligatoire)
        Transformateur transformateur = transformateurRepository.findById(request.getTransformateurId())
                .orElseThrow(() -> new ResourceNotFoundException("Transformateur not found with id: " + request.getTransformateurId()));

        // Charger le poste si fourni
        Poste poste = null;
        if (request.getPosteId() != null) {
            poste = posteRepository.findById(request.getPosteId())
                    .orElseThrow(() -> new ResourceNotFoundException("Poste not found with id: " + request.getPosteId()));
        }

        // Charger la mesure si fournie
        Measurement measurement = null;
        if (request.getMeasurementId() != null) {
            measurement = measurementRepository.findById(request.getMeasurementId())
                    .orElseThrow(() -> new ResourceNotFoundException("Measurement not found with id: " + request.getMeasurementId()));
        }

        // Générer la référence unique
        String reference = generateReference();

        // Créer l'alerte
        Alert alert = Alert.builder()
                .reference(reference)
                .titre(request.getTitre())
                .description(request.getDescription())
                .type(request.getType())
                .niveau(request.getNiveau())
                .statut(AlertStatus.ACTIVE)
                .transformateur(transformateur)
                .poste(poste)
                .measurement(measurement)
                .isAcknowledged(false)
                .isArchived(false)
                .build();

        Alert saved = alertRepository.save(alert);
        log.info("Alert created with reference: {}", reference);

        return alertMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AlertResponse getById(Long id) {
        log.debug("Fetching alert with id: {}", id);

        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found with id: " + id));

        return alertMapper.toResponse(alert);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AlertResponse> getAll(Pageable pageable) {
        log.debug("Fetching all alerts");

        return alertRepository.findAll(pageable)
                .map(alertMapper::toResponse);
    }

    @Override
    public AlertResponse update(Long id, UpdateAlertRequest request) {
        log.debug("Updating alert with id: {}", id);

        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found with id: " + id));

        // Mettre à jour les champs
        if (request.getTitre() != null) {
            alert.setTitre(request.getTitre());
        }
        if (request.getDescription() != null) {
            alert.setDescription(request.getDescription());
        }
        if (request.getNiveau() != null) {
            alert.setNiveau(request.getNiveau());
        }
        if (request.getStatut() != null) {
            alert.setStatut(request.getStatut());
            // Si l'alerte est résolue, enregistrer la date de résolution
            if ((request.getStatut() == AlertStatus.RESOLVED || request.getStatut() == AlertStatus.CLOSED) 
                    && alert.getResolvedAt() == null) {
                alert.setResolvedAt(LocalDateTime.now());
            }
        }
        if (request.getIsAcknowledged() != null) {
            alert.setIsAcknowledged(request.getIsAcknowledged());
        }
        if (request.getIsArchived() != null) {
            alert.setIsArchived(request.getIsArchived());
        }
        if (request.getResolvedByUserId() != null) {
            User user = userRepository.findById(request.getResolvedByUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getResolvedByUserId()));
            alert.setResolvedBy(user);
        }

        Alert updated = alertRepository.save(alert);
        log.info("Alert updated with id: {}", id);

        return alertMapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {
        log.debug("Deleting alert with id: {}", id);

        if (!alertRepository.existsById(id)) {
            throw new ResourceNotFoundException("Alert not found with id: " + id);
        }

        alertRepository.deleteById(id);
        log.info("Alert deleted with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AlertResponse> getByStatut(AlertStatus statut, Pageable pageable) {
        log.debug("Fetching alerts with statut: {}", statut);

        return alertRepository.findByStatut(statut, pageable)
                .map(alertMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AlertResponse> getByNiveau(AlertLevel niveau, Pageable pageable) {
        log.debug("Fetching alerts with niveau: {}", niveau);

        return alertRepository.findByNiveau(niveau, pageable)
                .map(alertMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AlertResponse> getByType(AlertType type, Pageable pageable) {
        log.debug("Fetching alerts with type: {}", type);

        return alertRepository.findByType(type, pageable)
                .map(alertMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AlertResponse> getByTransformateur(Long transformateurId, Pageable pageable) {
        log.debug("Fetching alerts for transformateur: {}", transformateurId);

        if (!transformateurRepository.existsById(transformateurId)) {
            throw new ResourceNotFoundException("Transformateur not found with id: " + transformateurId);
        }

        return alertRepository.findByTransformateurId(transformateurId, pageable)
                .map(alertMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AlertResponse> getByPoste(Long posteId, Pageable pageable) {
        log.debug("Fetching alerts for poste: {}", posteId);

        if (!posteRepository.existsById(posteId)) {
            throw new ResourceNotFoundException("Poste not found with id: " + posteId);
        }

        return alertRepository.findByPosteId(posteId, pageable)
                .map(alertMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AlertResponse> searchByTitre(String titre, Pageable pageable) {
        log.debug("Searching alerts by titre: {}", titre);

        return alertRepository.findByTitreContainsIgnoreCase(titre, pageable)
                .map(alertMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AlertResponse> getByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        log.debug("Fetching alerts between {} and {}", startDate, endDate);

        return alertRepository.findByCreatedAtBetween(startDate, endDate, pageable)
                .map(alertMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AlertResponse> searchAdvanced(
            String titre,
            AlertStatus statut,
            AlertLevel niveau,
            AlertType type,
            Long transformateurId,
            Long posteId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable) {
        log.debug("Executing advanced alert search");

        return alertRepository.searchAdvanced(
                titre,
                statut,
                niveau,
                type,
                transformateurId,
                posteId,
                startDate,
                endDate,
                pageable
        ).map(alertMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AlertResponse> getCritical(Pageable pageable) {
        log.debug("Fetching critical alerts");

        return alertRepository.findByNiveauAndIsArchived(AlertLevel.CRITICAL, false, pageable)
                .map(alertMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AlertResponse> getActive(Pageable pageable) {
        log.debug("Fetching active alerts");

        return alertRepository.findByStatutAndIsArchived(AlertStatus.ACTIVE, false, pageable)
                .map(alertMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AlertResponse> getRecent(Pageable pageable) {
        log.debug("Fetching recent alerts");

        return alertRepository.findRecentActiveAlerts(pageable)
                .map(alertMapper::toResponse);
    }

    @Override
    public AlertResponse acknowledge(Long id) {
        log.debug("Acknowledging alert with id: {}", id);

        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found with id: " + id));

        alert.setIsAcknowledged(true);
        Alert updated = alertRepository.save(alert);
        log.info("Alert {} acknowledged", id);

        return alertMapper.toResponse(updated);
    }

    @Override
    public AlertResponse resolve(Long id, Long resolvedByUserId) {
        log.debug("Resolving alert with id: {}", id);

        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found with id: " + id));

        User user = userRepository.findById(resolvedByUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + resolvedByUserId));

        alert.setStatut(AlertStatus.RESOLVED);
        alert.setResolvedAt(LocalDateTime.now());
        alert.setResolvedBy(user);

        Alert updated = alertRepository.save(alert);
        log.info("Alert {} resolved by user {}", id, resolvedByUserId);

        return alertMapper.toResponse(updated);
    }

    @Override
    public AlertResponse archive(Long id) {
        log.debug("Archiving alert with id: {}", id);

        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found with id: " + id));

        alert.setIsArchived(true);
        Alert updated = alertRepository.save(alert);
        log.info("Alert {} archived", id);

        return alertMapper.toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public AlertStatisticsResponse getStatistics() {
        log.debug("Calculating alert statistics");

        Long totalAlerts = alertRepository.count();
        Long activeAlerts = alertRepository.countByStatut(AlertStatus.ACTIVE);
        Long criticalAlerts = alertRepository.countByNiveau(AlertLevel.CRITICAL);
        Long resolvedAlerts = alertRepository.countByStatut(AlertStatus.RESOLVED);
        Long acknowledgedAlerts = alertRepository.countByIsAcknowledged(true);
        Long archivedAlerts = alertRepository.countByIsArchived(true);

        Long infoCount = alertRepository.countByNiveau(AlertLevel.INFO);
        Long warningCount = alertRepository.countByNiveau(AlertLevel.WARNING);
        Long highCount = alertRepository.countByNiveau(AlertLevel.HIGH);
        Long criticalCount = alertRepository.countByNiveau(AlertLevel.CRITICAL);

        Long temperatureCount = alertRepository.countByType(AlertType.TEMPERATURE);
        Long overloadCount = alertRepository.countByType(AlertType.SURCHARGE);
        Long overconsumptionCount = alertRepository.countByType(AlertType.SURCONSOMMATION);
        Long breakdownCount = alertRepository.countByType(AlertType.PANNE);
        Long voltageDipCount = alertRepository.countByType(AlertType.CHUTE_TENSION);
        Long shortCircuitCount = alertRepository.countByType(AlertType.COURT_CIRCUIT);
        Long fireCount = alertRepository.countByType(AlertType.INCENDIE);
        Long communicationCount = alertRepository.countByType(AlertType.COMMUNICATION);
        Long otherCount = alertRepository.countByType(AlertType.AUTRE);

        Double averageResolutionTime = null;  // alertRepository.getAverageResolutionTimeHours();
        Double resolutionRate = null;  // alertRepository.getResolutionRate();
        Double acknowledgmentRate = null;  // alertRepository.getAcknowledgmentRate();

        return AlertStatisticsResponse.builder()
                .totalAlerts(totalAlerts)
                .activeAlerts(activeAlerts != null ? activeAlerts : 0)
                .criticalAlerts(criticalAlerts != null ? criticalAlerts : 0)
                .resolvedAlerts(resolvedAlerts != null ? resolvedAlerts : 0)
                .acknowledgedAlerts(acknowledgedAlerts != null ? acknowledgedAlerts : 0)
                .archivedAlerts(archivedAlerts != null ? archivedAlerts : 0)
                .infoCount(infoCount != null ? infoCount : 0)
                .warningCount(warningCount != null ? warningCount : 0)
                .highCount(highCount != null ? highCount : 0)
                .criticalCount(criticalCount != null ? criticalCount : 0)
                .temperatureCount(temperatureCount != null ? temperatureCount : 0)
                .overloadCount(overloadCount != null ? overloadCount : 0)
                .overconsumptionCount(overconsumptionCount != null ? overconsumptionCount : 0)
                .breakdownCount(breakdownCount != null ? breakdownCount : 0)
                .voltageDipCount(voltageDipCount != null ? voltageDipCount : 0)
                .shortCircuitCount(shortCircuitCount != null ? shortCircuitCount : 0)
                .fireCount(fireCount != null ? fireCount : 0)
                .communicationCount(communicationCount != null ? communicationCount : 0)
                .otherCount(otherCount != null ? otherCount : 0)
                .averageResolutionTimeHours(averageResolutionTime != null ? averageResolutionTime : 0.0)
                .resolutionRate(resolutionRate != null ? resolutionRate : 0.0)
                .acknowledgmentRate(acknowledgmentRate != null ? acknowledgmentRate : 0.0)
                .build();
    }

    @Override
    public AlertResponse changeStatus(Long id, AlertStatus newStatus) {
        log.debug("Changing alert status to: {}", newStatus);

        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found with id: " + id));

        alert.setStatut(newStatus);

        // Si le nouveau statut est résolu ou fermé, enregistrer la date
        if ((newStatus == AlertStatus.RESOLVED || newStatus == AlertStatus.CLOSED) && alert.getResolvedAt() == null) {
            alert.setResolvedAt(LocalDateTime.now());
        }

        Alert updated = alertRepository.save(alert);
        log.info("Alert {} status changed to {}", id, newStatus);

        return alertMapper.toResponse(updated);
    }

    /**
     * Génère une référence unique au format ALR-YYYY-MMDD-XXXX
     */
    private String generateReference() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String datePart = now.format(formatter).replace("-", "");
        String randomPart = String.format("%04d", (int) (Math.random() * 10000));
        return String.format("ALR-%s-%s", datePart, randomPart);
    }
}
