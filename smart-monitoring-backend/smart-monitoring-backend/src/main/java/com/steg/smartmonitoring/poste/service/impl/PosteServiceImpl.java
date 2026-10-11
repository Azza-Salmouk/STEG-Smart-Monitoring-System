package com.steg.smartmonitoring.poste.service.impl;

import com.steg.smartmonitoring.common.exception.ResourceAlreadyExistsException;
import com.steg.smartmonitoring.common.exception.ResourceNotFoundException;
import com.steg.smartmonitoring.poste.dto.CreatePosteRequest;
import com.steg.smartmonitoring.poste.dto.PosteResponse;
import com.steg.smartmonitoring.poste.dto.UpdatePosteRequest;
import com.steg.smartmonitoring.poste.entity.EtatPoste;
import com.steg.smartmonitoring.poste.entity.Poste;
import com.steg.smartmonitoring.poste.entity.TypePoste;
import com.steg.smartmonitoring.poste.repository.PosteRepository;
import com.steg.smartmonitoring.poste.service.PosteService;
import com.steg.smartmonitoring.poste.service.PosteStatisticsResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implémentation du service de gestion des postes
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PosteServiceImpl implements PosteService {

    private static final Logger log = LoggerFactory.getLogger(PosteServiceImpl.class);

    private final PosteRepository posteRepository;

    @Override
    public PosteResponse create(CreatePosteRequest request) {
        log.info("Création d'un nouveau poste avec le code: {}", request.getCode());

        // Vérifier que le code est unique
        if (posteRepository.existsByCode(request.getCode())) {
            log.warn("Le code du poste {} existe déjà", request.getCode());
            throw new ResourceAlreadyExistsException(
                    "Un poste avec le code " + request.getCode() + " existe déjà"
            );
        }

        // Créer l'entité Poste
        Poste poste = Poste.builder()
                .nom(request.getNom())
                .code(request.getCode())
                .region(request.getRegion())
                .adresse(request.getAdresse())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .typePoste(request.getTypePoste())
                .description(request.getDescription())
                .etat(EtatPoste.ACTIF)
                .build();

        Poste savedPoste = posteRepository.save(poste);
        log.info("Poste créé avec succès: ID={}, code={}", savedPoste.getId(), savedPoste.getCode());

        return mapToResponse(savedPoste);
    }

    @Override
    @Transactional(readOnly = true)
    public PosteResponse getById(Long id) {
        log.debug("Récupération du poste avec l'ID: {}", id);

        Poste poste = posteRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Poste non trouvé avec l'ID: {}", id);
                    return new ResourceNotFoundException("Poste non trouvé avec l'ID: " + id);
                });

        return mapToResponse(poste);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PosteResponse> getAll(Pageable pageable) {
        log.debug("Récupération de tous les postes avec pagination");
        return posteRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Override
    public PosteResponse update(Long id, UpdatePosteRequest request) {
        log.info("Mise à jour du poste avec l'ID: {}", id);

        Poste poste = posteRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Poste non trouvé avec l'ID: {}", id);
                    return new ResourceNotFoundException("Poste non trouvé avec l'ID: " + id);
                });

        // Mettre à jour les champs si fournis
        if (request.getNom() != null) {
            poste.setNom(request.getNom());
        }
        if (request.getRegion() != null) {
            poste.setRegion(request.getRegion());
        }
        if (request.getAdresse() != null) {
            poste.setAdresse(request.getAdresse());
        }
        if (request.getLatitude() != null) {
            poste.setLatitude(request.getLatitude());
        }
        if (request.getLongitude() != null) {
            poste.setLongitude(request.getLongitude());
        }
        if (request.getTypePoste() != null) {
            poste.setTypePoste(request.getTypePoste());
        }
        if (request.getEtat() != null) {
            poste.setEtat(request.getEtat());
        }
        if (request.getDescription() != null) {
            poste.setDescription(request.getDescription());
        }

        Poste updatedPoste = posteRepository.save(poste);
        log.info("Poste mis à jour avec succès: ID={}", updatedPoste.getId());

        return mapToResponse(updatedPoste);
    }

    @Override
    public void delete(Long id) {
        log.info("Suppression du poste avec l'ID: {}", id);

        Poste poste = posteRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Poste non trouvé avec l'ID: {}", id);
                    return new ResourceNotFoundException("Poste non trouvé avec l'ID: " + id);
                });

        posteRepository.delete(poste);
        log.info("Poste supprimé avec succès: ID={}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PosteResponse> searchByNom(String nom, Pageable pageable) {
        log.debug("Recherche de postes par nom: {}", nom);
        return posteRepository.findByNomContainingIgnoreCase(nom, pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PosteResponse> searchByRegion(String region, Pageable pageable) {
        log.debug("Recherche de postes par région: {}", region);
        return posteRepository.findByRegionContainingIgnoreCase(region, pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PosteResponse> searchByEtat(EtatPoste etat, Pageable pageable) {
        log.debug("Recherche de postes par état: {}", etat);
        return posteRepository.findByEtat(etat, pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PosteResponse> searchByType(TypePoste typePoste, Pageable pageable) {
        log.debug("Recherche de postes par type: {}", typePoste);
        return posteRepository.findByTypePoste(typePoste, pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PosteResponse> search(String nom, String region, EtatPoste etat, TypePoste typePoste, Pageable pageable) {
        log.debug("Recherche avancée: nom={}, région={}, état={}, type={}", nom, region, etat, typePoste);
        return posteRepository.search(nom, region, etat, typePoste, pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public PosteStatisticsResponse getStatistics() {
        log.debug("Récupération des statistiques des postes");

        return PosteStatisticsResponse.builder()
                .totalPostes(posteRepository.count())
                .posteActifs(posteRepository.countByEtat(EtatPoste.ACTIF))
                .posteInactifs(posteRepository.countByEtat(EtatPoste.INACTIF))
                .posteMaintenance(posteRepository.countByEtat(EtatPoste.MAINTENANCE))
                .build();
    }

    /**
     * Convertir une entité Poste en PosteResponse DTO
     */
    private PosteResponse mapToResponse(Poste poste) {
        return PosteResponse.builder()
                .id(poste.getId())
                .nom(poste.getNom())
                .code(poste.getCode())
                .region(poste.getRegion())
                .adresse(poste.getAdresse())
                .latitude(poste.getLatitude())
                .longitude(poste.getLongitude())
                .typePoste(poste.getTypePoste())
                .etat(poste.getEtat())
                .description(poste.getDescription())
                .nombreTransformateurs(poste.getTransformateurs() != null ? poste.getTransformateurs().size() : 0)
                .createdAt(poste.getCreatedAt())
                .updatedAt(poste.getUpdatedAt())
                .build();
    }
}
