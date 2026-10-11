package com.steg.smartmonitoring.transformateur.service.impl;

import com.steg.smartmonitoring.common.exception.ResourceAlreadyExistsException;
import com.steg.smartmonitoring.common.exception.ResourceNotFoundException;
import com.steg.smartmonitoring.poste.entity.Poste;
import com.steg.smartmonitoring.poste.repository.PosteRepository;
import com.steg.smartmonitoring.transformateur.dto.CreateTransformateurRequest;
import com.steg.smartmonitoring.transformateur.dto.TransformateurResponse;
import com.steg.smartmonitoring.transformateur.dto.UpdateTransformateurRequest;
import com.steg.smartmonitoring.transformateur.entity.RiskLevel;
import com.steg.smartmonitoring.transformateur.entity.StatusTransformateur;
import com.steg.smartmonitoring.transformateur.entity.Transformateur;
import com.steg.smartmonitoring.transformateur.repository.TransformateurRepository;
import com.steg.smartmonitoring.transformateur.service.TransformateurService;
import com.steg.smartmonitoring.transformateur.service.TransformateurStatisticsResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implémentation du service de gestion des transformateurs
 */
@Service
@RequiredArgsConstructor
@Transactional
public class TransformateurServiceImpl implements TransformateurService {

    private static final Logger log = LoggerFactory.getLogger(TransformateurServiceImpl.class);

    private final TransformateurRepository transformateurRepository;
    private final PosteRepository posteRepository;

    @Override
    public TransformateurResponse create(CreateTransformateurRequest request) {
        log.info("Création d'un nouveau transformateur avec la référence: {}", request.getReference());

        // Vérifier que la référence est unique
        if (transformateurRepository.existsByReference(request.getReference())) {
            log.warn("La référence {} existe déjà", request.getReference());
            throw new ResourceAlreadyExistsException(
                    "Un transformateur avec la référence " + request.getReference() + " existe déjà"
            );
        }

        // Vérifier que le poste existe
        Poste poste = posteRepository.findById(request.getPosteId())
                .orElseThrow(() -> {
                    log.warn("Poste non trouvé avec l'ID: {}", request.getPosteId());
                    return new ResourceNotFoundException(
                            "Poste non trouvé avec l'ID: " + request.getPosteId()
                    );
                });

        // Créer l'entité Transformateur
        Transformateur transformateur = Transformateur.builder()
                .reference(request.getReference())
                .serialNumber(request.getSerialNumber())
                .manufacturer(request.getManufacturer())
                .model(request.getModel())
                .power(request.getPower())
                .installationDate(request.getInstallationDate())
                .status(StatusTransformateur.ACTIF)
                .healthScore(request.getHealthScore())
                .riskLevel(request.getRiskLevel())
                .description(request.getDescription())
                .poste(poste)
                .build();

        Transformateur savedTransformateur = transformateurRepository.save(transformateur);
        log.info("Transformateur créé avec succès: ID={}, référence={}", savedTransformateur.getId(), savedTransformateur.getReference());

        return mapToResponse(savedTransformateur);
    }

    @Override
    @Transactional(readOnly = true)
    public TransformateurResponse getById(Long id) {
        log.debug("Récupération du transformateur avec l'ID: {}", id);

        Transformateur transformateur = transformateurRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Transformateur non trouvé avec l'ID: {}", id);
                    return new ResourceNotFoundException("Transformateur non trouvé avec l'ID: " + id);
                });

        return mapToResponse(transformateur);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransformateurResponse> getAll(Pageable pageable) {
        log.debug("Récupération de tous les transformateurs avec pagination");
        return transformateurRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Override
    public TransformateurResponse update(Long id, UpdateTransformateurRequest request) {
        log.info("Mise à jour du transformateur avec l'ID: {}", id);

        Transformateur transformateur = transformateurRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Transformateur non trouvé avec l'ID: {}", id);
                    return new ResourceNotFoundException("Transformateur non trouvé avec l'ID: " + id);
                });

        // Mettre à jour les champs si fournis
        if (request.getManufacturer() != null) {
            transformateur.setManufacturer(request.getManufacturer());
        }
        if (request.getModel() != null) {
            transformateur.setModel(request.getModel());
        }
        if (request.getPower() != null) {
            transformateur.setPower(request.getPower());
        }
        if (request.getStatus() != null) {
            transformateur.setStatus(request.getStatus());
        }
        if (request.getHealthScore() != null) {
            transformateur.setHealthScore(request.getHealthScore());
        }
        if (request.getRiskLevel() != null) {
            transformateur.setRiskLevel(request.getRiskLevel());
        }
        if (request.getDescription() != null) {
            transformateur.setDescription(request.getDescription());
        }

        Transformateur updatedTransformateur = transformateurRepository.save(transformateur);
        log.info("Transformateur mis à jour avec succès: ID={}", updatedTransformateur.getId());

        return mapToResponse(updatedTransformateur);
    }

    @Override
    public void delete(Long id) {
        log.info("Suppression du transformateur avec l'ID: {}", id);

        Transformateur transformateur = transformateurRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Transformateur non trouvé avec l'ID: {}", id);
                    return new ResourceNotFoundException("Transformateur non trouvé avec l'ID: " + id);
                });

        transformateurRepository.delete(transformateur);
        log.info("Transformateur supprimé avec succès: ID={}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransformateurResponse> getByPosteId(Long posteId) {
        log.debug("Récupération des transformateurs du poste: {}", posteId);

        // Vérifier que le poste existe
        if (!posteRepository.existsById(posteId)) {
            log.warn("Poste non trouvé avec l'ID: {}", posteId);
            throw new ResourceNotFoundException("Poste non trouvé avec l'ID: " + posteId);
        }

        return transformateurRepository.findByPosteId(posteId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransformateurResponse> searchByReference(String reference, Pageable pageable) {
        log.debug("Recherche de transformateurs par référence: {}", reference);
        return transformateurRepository.findByReferenceContainingIgnoreCase(reference, pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransformateurResponse> searchByManufacturer(String manufacturer, Pageable pageable) {
        log.debug("Recherche de transformateurs par fabricant: {}", manufacturer);
        return transformateurRepository.findByManufacturerContainingIgnoreCase(manufacturer, pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransformateurResponse> searchByStatus(StatusTransformateur status, Pageable pageable) {
        log.debug("Recherche de transformateurs par status: {}", status);
        return transformateurRepository.findByStatus(status, pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransformateurResponse> searchByRiskLevel(RiskLevel riskLevel, Pageable pageable) {
        log.debug("Recherche de transformateurs par niveau de risque: {}", riskLevel);
        return transformateurRepository.findByRiskLevel(riskLevel, pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransformateurResponse> searchByHealthScore(Integer maxScore, Pageable pageable) {
        log.debug("Recherche de transformateurs avec health score <= {}", maxScore);
        return transformateurRepository.findByHealthScoreLessThanEqual(maxScore, pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransformateurResponse> search(
            String reference,
            String manufacturer,
            StatusTransformateur status,
            RiskLevel riskLevel,
            Integer healthScoreMax,
            Long posteId,
            Pageable pageable) {
        log.debug("Recherche avancée: ref={}, fab={}, status={}, risk={}, health<={}, poste={}", 
                reference, manufacturer, status, riskLevel, healthScoreMax, posteId);
        return transformateurRepository.search(reference, manufacturer, status, riskLevel, healthScoreMax, posteId, pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransformateurResponse> getAlertingTransformateurs() {
        log.debug("Récupération des transformateurs en alerte ou défaut");
        return transformateurRepository.findAllAlerting()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransformateurResponse> getCriticalTransformateurs() {
        log.debug("Récupération des transformateurs critiques");
        return transformateurRepository.findAllCritical()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TransformateurStatisticsResponse getStatistics() {
        log.debug("Récupération des statistiques des transformateurs");

        return TransformateurStatisticsResponse.builder()
                .totalTransformateurs(transformateurRepository.count())
                .transformateursActifs(transformateurRepository.countByStatus(StatusTransformateur.ACTIF))
                .transformateursAlertes(transformateurRepository.countByStatus(StatusTransformateur.ALERTE))
                .transformateursDefauts(transformateurRepository.countByStatus(StatusTransformateur.DEFAUT))
                .transformateursCritiques(transformateurRepository.countCritical())
                .averageHealthScore(transformateurRepository.getAverageHealthScore())
                .build();
    }

    /**
     * Convertir une entité Transformateur en TransformateurResponse DTO
     */
    private TransformateurResponse mapToResponse(Transformateur transformateur) {
        return TransformateurResponse.builder()
                .id(transformateur.getId())
                .reference(transformateur.getReference())
                .serialNumber(transformateur.getSerialNumber())
                .manufacturer(transformateur.getManufacturer())
                .model(transformateur.getModel())
                .power(transformateur.getPower())
                .installationDate(transformateur.getInstallationDate())
                .status(transformateur.getStatus())
                .healthScore(transformateur.getHealthScore())
                .riskLevel(transformateur.getRiskLevel())
                .description(transformateur.getDescription())
                .posteId(transformateur.getPoste().getId())
                .posteNom(transformateur.getPoste().getNom())
                .createdAt(transformateur.getCreatedAt())
                .updatedAt(transformateur.getUpdatedAt())
                .build();
    }
}
