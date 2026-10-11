package com.steg.smartmonitoring.intervention.service.impl;

import com.steg.smartmonitoring.common.exception.ResourceNotFoundException;
import com.steg.smartmonitoring.intervention.dto.*;
import com.steg.smartmonitoring.intervention.entity.*;
import com.steg.smartmonitoring.intervention.mapper.InterventionMapper;
import com.steg.smartmonitoring.intervention.repository.InterventionRepository;
import com.steg.smartmonitoring.intervention.service.InterventionService;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Service d'implémentation pour la gestion des interventions
 */
@Service
@Transactional
@RequiredArgsConstructor
public class InterventionServiceImpl implements InterventionService {

    private static final Logger log = LoggerFactory.getLogger(InterventionServiceImpl.class);

    private final InterventionRepository interventionRepository;
    private final InterventionMapper interventionMapper;
    private final TransformateurRepository transformateurRepository;
    private final UserRepository userRepository;

    @Override
    public InterventionResponse create(CreateInterventionRequest request) {
        log.debug("Creating new intervention for transformateur: {}", request.getTransformateurId());

        // Vérifier que le transformateur existe
        Transformateur transformateur = transformateurRepository.findById(request.getTransformateurId())
                .orElseThrow(() -> new ResourceNotFoundException("Transformateur not found with id: " + request.getTransformateurId()));

        // Vérifier que l'agent existe
        User agent = userRepository.findById(request.getAgentId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getAgentId()));

        // Générer la référence unique
        String reference = generateReference();

        // Créer l'intervention
        Intervention intervention = Intervention.builder()
                .reference(reference)
                .transformateur(transformateur)
                .agent(agent)
                .dateCreation(LocalDateTime.now())
                .datePlanifiee(request.getDatePlanifiee())
                .typeIntervention(request.getTypeIntervention())
                .priorite(request.getPriorite())
                .statut(StatutIntervention.EN_ATTENTE)
                .description(request.getDescription())
                .observations(request.getObservations())
                .cout(request.getCout())
                .build();

        Intervention saved = interventionRepository.save(intervention);
        log.info("Intervention created with reference: {}", reference);

        return interventionMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public InterventionResponse getById(Long id) {
        log.debug("Fetching intervention with id: {}", id);

        Intervention intervention = interventionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Intervention not found with id: " + id));

        return interventionMapper.toResponse(intervention);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InterventionResponse> getAll(Pageable pageable) {
        log.debug("Fetching all interventions with pagination");

        return interventionRepository.findAll(pageable)
                .map(interventionMapper::toResponse);
    }

    @Override
    public InterventionResponse update(Long id, UpdateInterventionRequest request) {
        log.debug("Updating intervention with id: {}", id);

        Intervention intervention = interventionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Intervention not found with id: " + id));

        // Mettre à jour les champs
        if (request.getDatePlanifiee() != null) {
            intervention.setDatePlanifiee(request.getDatePlanifiee());
        }
        if (request.getDateDebut() != null) {
            intervention.setDateDebut(request.getDateDebut());
        }
        if (request.getDateFin() != null) {
            intervention.setDateFin(request.getDateFin());
        }
        if (request.getTypeIntervention() != null) {
            intervention.setTypeIntervention(request.getTypeIntervention());
        }
        if (request.getPriorite() != null) {
            intervention.setPriorite(request.getPriorite());
        }
        if (request.getStatut() != null) {
            intervention.setStatut(request.getStatut());
        }
        if (request.getDescription() != null) {
            intervention.setDescription(request.getDescription());
        }
        if (request.getResultat() != null) {
            intervention.setResultat(request.getResultat());
        }
        if (request.getObservations() != null) {
            intervention.setObservations(request.getObservations());
        }
        if (request.getDureeMinutes() != null) {
            intervention.setDureeMinutes(request.getDureeMinutes());
        }
        if (request.getCout() != null) {
            intervention.setCout(request.getCout());
        }

        Intervention updated = interventionRepository.save(intervention);
        log.info("Intervention updated with id: {}", id);

        return interventionMapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {
        log.debug("Deleting intervention with id: {}", id);

        if (!interventionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Intervention not found with id: " + id);
        }

        interventionRepository.deleteById(id);
        log.info("Intervention deleted with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InterventionResponse> getByAgent(Long agentId, Pageable pageable) {
        log.debug("Fetching interventions for agent: {}", agentId);

        // Vérifier que l'agent existe
        if (!userRepository.existsById(agentId)) {
            throw new ResourceNotFoundException("User not found with id: " + agentId);
        }

        return interventionRepository.findByAgentId(agentId, pageable)
                .map(interventionMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InterventionResponse> getByTransformateur(Long transformateurId, Pageable pageable) {
        log.debug("Fetching interventions for transformateur: {}", transformateurId);

        // Vérifier que le transformateur existe
        if (!transformateurRepository.existsById(transformateurId)) {
            throw new ResourceNotFoundException("Transformateur not found with id: " + transformateurId);
        }

        return interventionRepository.findByTransformateurId(transformateurId, pageable)
                .map(interventionMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InterventionResponse> getByStatut(StatutIntervention statut, Pageable pageable) {
        log.debug("Fetching interventions with statut: {}", statut);

        return interventionRepository.findByStatut(statut, pageable)
                .map(interventionMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InterventionResponse> getByPriorite(Priorite priorite, Pageable pageable) {
        log.debug("Fetching interventions with priorite: {}", priorite);

        return interventionRepository.findByPriorite(priorite, pageable)
                .map(interventionMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InterventionResponse> getByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        log.debug("Fetching interventions between {} and {}", startDate, endDate);

        return interventionRepository.findByDatePlanifieeBetween(startDate, endDate, pageable)
                .map(interventionMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InterventionResponse> searchAdvanced(
            Long transformateurId,
            Long agentId,
            StatutIntervention statut,
            Priorite priorite,
            TypeIntervention typeIntervention,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable) {
        log.debug("Executing advanced search with filters");

        return interventionRepository.searchAdvanced(
                transformateurId,
                agentId,
                statut,
                priorite,
                typeIntervention,
                startDate,
                endDate,
                pageable
        ).map(interventionMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public InterventionStatisticsResponse getStatistics() {
        log.debug("Calculating intervention statistics");

        Long totalCount = interventionRepository.count();
        Long enAttenteCount = interventionRepository.countByStatut(StatutIntervention.EN_ATTENTE);
        Long enCoursCount = interventionRepository.countByStatut(StatutIntervention.EN_COURS);
        Long termineesCount = interventionRepository.countByStatut(StatutIntervention.TERMINEE);
        Long critiquesCount = interventionRepository.countByPriorite(Priorite.CRITIQUE);
        
        // Count all priority levels
        Long basseCount = interventionRepository.countByPriorite(Priorite.FAIBLE);
        Long moyenneCount = interventionRepository.countByPriorite(Priorite.MOYENNE);
        Long hauteCount = interventionRepository.countByPriorite(Priorite.HAUTE);
        
        // Build priority map with French keys
        Map<String, Long> priorityMap = new HashMap<>();
        priorityMap.put("basse", basseCount != null ? basseCount : 0);
        priorityMap.put("moyenne", moyenneCount != null ? moyenneCount : 0);
        priorityMap.put("haute", hauteCount != null ? hauteCount : 0);
        priorityMap.put("critique", critiquesCount != null ? critiquesCount : 0);
        
        Double averageDuration = interventionRepository.getAverageDuration();
        Double averageCost = interventionRepository.getAverageCost(StatutIntervention.TERMINEE);
        Long completedCount = interventionRepository.getCompletedInterventionsCount();

        return InterventionStatisticsResponse.builder()
                .totalInterventions(totalCount)
                .enAttente(enAttenteCount != null ? enAttenteCount : 0)
                .enCours(enCoursCount != null ? enCoursCount : 0)
                .terminees(termineesCount != null ? termineesCount : 0)
                .critiques(critiquesCount != null ? critiquesCount : 0)
                .completionRate(totalCount > 0 ? (completedCount * 100.0 / totalCount) : 0.0)
                .averageDuration(averageDuration != null ? averageDuration : 0.0)
                .averageCost(averageCost != null ? averageCost : 0.0)
                .byPriority(priorityMap)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InterventionResponse> getCritical(Pageable pageable) {
        log.debug("Fetching critical interventions");

        return interventionRepository.findByPrioriteAndStatutNot(Priorite.CRITIQUE, StatutIntervention.TERMINEE, pageable)
                .map(interventionMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InterventionResponse> getOngoingByAgent(Long agentId, Pageable pageable) {
        log.debug("Fetching ongoing interventions for agent: {}", agentId);

        // Vérifier que l'agent existe
        if (!userRepository.existsById(agentId)) {
            throw new ResourceNotFoundException("User not found with id: " + agentId);
        }

        return interventionRepository.findByStatutAndAgentId(StatutIntervention.EN_COURS, agentId, pageable)
                .map(interventionMapper::toResponse);
    }

    /**
     * Génère une référence unique au format INT-YYYY-MMDD-XXXX
     */
    private String generateReference() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String datePart = now.format(formatter).replace("-", "");
        String randomPart = String.format("%04d", (int) (Math.random() * 10000));
        return String.format("INT-%s-%s", datePart, randomPart);
    }
}
