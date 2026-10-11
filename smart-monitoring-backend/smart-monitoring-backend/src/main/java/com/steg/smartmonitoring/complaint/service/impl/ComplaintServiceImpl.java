package com.steg.smartmonitoring.complaint.service.impl;

import com.steg.smartmonitoring.common.exception.ResourceNotFoundException;
import com.steg.smartmonitoring.complaint.dto.*;
import com.steg.smartmonitoring.complaint.entity.*;
import com.steg.smartmonitoring.complaint.mapper.ComplaintMapper;
import com.steg.smartmonitoring.complaint.repository.ComplaintRepository;
import com.steg.smartmonitoring.complaint.service.ComplaintService;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Service d'implémentation pour la gestion des réclamations
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ComplaintServiceImpl implements ComplaintService {

    private static final Logger log = LoggerFactory.getLogger(ComplaintServiceImpl.class);

    private final ComplaintRepository complaintRepository;
    private final ComplaintMapper complaintMapper;
    private final PosteRepository posteRepository;
    private final TransformateurRepository transformateurRepository;
    private final UserRepository userRepository;

    @Override
    public ComplaintResponse create(CreateComplaintRequest request) {
        log.debug("Creating new complaint for client: {}", request.getNomClient());

        // Charger le poste si fourni
        Poste poste = null;
        if (request.getPosteId() != null) {
            poste = posteRepository.findById(request.getPosteId())
                    .orElseThrow(() -> new ResourceNotFoundException("Poste not found with id: " + request.getPosteId()));
        }

        // Charger le transformateur si fourni
        Transformateur transformateur = null;
        if (request.getTransformateurId() != null) {
            transformateur = transformateurRepository.findById(request.getTransformateurId())
                    .orElseThrow(() -> new ResourceNotFoundException("Transformateur not found with id: " + request.getTransformateurId()));
        }

        // Générer la référence unique
        String reference = generateReference();

        // Créer la réclamation
        Complaint complaint = Complaint.builder()
                .reference(reference)
                .nomClient(request.getNomClient())
                .telephone(request.getTelephone())
                .adresse(request.getAdresse())
                .categorie(request.getCategorie())
                .description(request.getDescription())
                .priorite(request.getPriorite())
                .statut(ComplaintStatus.NOUVELLE)
                .poste(poste)
                .transformateur(transformateur)
                .build();

        Complaint saved = complaintRepository.save(complaint);
        log.info("Complaint created with reference: {}", reference);

        return complaintMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ComplaintResponse getById(Long id) {
        log.debug("Fetching complaint with id: {}", id);

        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found with id: " + id));

        return complaintMapper.toResponse(complaint);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ComplaintResponse> getAll(Pageable pageable) {
        log.debug("Fetching all complaints with pagination");

        return complaintRepository.findAll(pageable)
                .map(complaintMapper::toResponse);
    }

    @Override
    public ComplaintResponse update(Long id, UpdateComplaintRequest request) {
        log.debug("Updating complaint with id: {}", id);

        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found with id: " + id));

        // Mettre à jour les champs
        if (request.getStatut() != null) {
            complaint.setStatut(request.getStatut());
            // Si la plainte est résolue ou fermée, enregistrer la date de résolution
            if ((request.getStatut() == ComplaintStatus.RESOLUE || request.getStatut() == ComplaintStatus.FERMEE) 
                    && complaint.getDateResolution() == null) {
                complaint.setDateResolution(LocalDateTime.now());
            }
        }
        if (request.getPriorite() != null) {
            complaint.setPriorite(request.getPriorite());
        }
        if (request.getAssignedSupervisorId() != null) {
            User supervisor = userRepository.findById(request.getAssignedSupervisorId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getAssignedSupervisorId()));
            complaint.setAssignedSupervisor(supervisor);
        }
        if (request.getCommentaires() != null) {
            complaint.setCommentaires(request.getCommentaires());
        }
        if (request.getTelephone() != null) {
            complaint.setTelephone(request.getTelephone());
        }
        if (request.getAdresse() != null) {
            complaint.setAdresse(request.getAdresse());
        }
        if (request.getDescription() != null) {
            complaint.setDescription(request.getDescription());
        }

        Complaint updated = complaintRepository.save(complaint);
        log.info("Complaint updated with id: {}", id);

        return complaintMapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {
        log.debug("Deleting complaint with id: {}", id);

        if (!complaintRepository.existsById(id)) {
            throw new ResourceNotFoundException("Complaint not found with id: " + id);
        }

        complaintRepository.deleteById(id);
        log.info("Complaint deleted with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ComplaintResponse> getByStatut(ComplaintStatus statut, Pageable pageable) {
        log.debug("Fetching complaints with statut: {}", statut);

        return complaintRepository.findByStatut(statut, pageable)
                .map(complaintMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ComplaintResponse> getByPriorite(ComplaintPriority priorite, Pageable pageable) {
        log.debug("Fetching complaints with priorite: {}", priorite);

        return complaintRepository.findByPriorite(priorite, pageable)
                .map(complaintMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ComplaintResponse> getByCategorie(ComplaintCategory categorie, Pageable pageable) {
        log.debug("Fetching complaints with categorie: {}", categorie);

        return complaintRepository.findByCategorie(categorie, pageable)
                .map(complaintMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ComplaintResponse> searchByClient(String nomClient, Pageable pageable) {
        log.debug("Searching complaints by client: {}", nomClient);

        return complaintRepository.findByNomClientContainsIgnoreCase(nomClient, pageable)
                .map(complaintMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ComplaintResponse> getBySupervisor(Long supervisorId, Pageable pageable) {
        log.debug("Fetching complaints for supervisor: {}", supervisorId);

        if (!userRepository.existsById(supervisorId)) {
            throw new ResourceNotFoundException("User not found with id: " + supervisorId);
        }

        return complaintRepository.findByAssignedSupervisorId(supervisorId, pageable)
                .map(complaintMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ComplaintResponse> getByPoste(Long posteId, Pageable pageable) {
        log.debug("Fetching complaints for poste: {}", posteId);

        if (!posteRepository.existsById(posteId)) {
            throw new ResourceNotFoundException("Poste not found with id: " + posteId);
        }

        return complaintRepository.findByPosteId(posteId, pageable)
                .map(complaintMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ComplaintResponse> getByTransformateur(Long transformateurId, Pageable pageable) {
        log.debug("Fetching complaints for transformateur: {}", transformateurId);

        if (!transformateurRepository.existsById(transformateurId)) {
            throw new ResourceNotFoundException("Transformateur not found with id: " + transformateurId);
        }

        return complaintRepository.findByTransformateurId(transformateurId, pageable)
                .map(complaintMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ComplaintResponse> getByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        log.debug("Fetching complaints between {} and {}", startDate, endDate);

        return complaintRepository.findByCreatedAtBetween(startDate, endDate, pageable)
                .map(complaintMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ComplaintResponse> searchAdvanced(
            String nomClient,
            ComplaintStatus statut,
            ComplaintPriority priorite,
            ComplaintCategory categorie,
            Long posteId,
            Long transformateurId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable) {
        log.debug("Executing advanced search with filters");

        return complaintRepository.searchAdvanced(
                nomClient,
                statut,
                priorite,
                categorie,
                posteId,
                transformateurId,
                startDate,
                endDate,
                pageable
        ).map(complaintMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ComplaintStatisticsResponse getStatistics() {
        log.debug("Calculating complaint statistics");

        Long totalCount = complaintRepository.count();
        Long nouvellesCount = complaintRepository.countByStatut(ComplaintStatus.NOUVELLE);
        Long enAnalyseCount = complaintRepository.countByStatut(ComplaintStatus.EN_ANALYSE);
        Long assigneesCount = complaintRepository.countByStatut(ComplaintStatus.ASSIGNEE);
        Long enTraitementCount = complaintRepository.countByStatut(ComplaintStatus.EN_TRAITEMENT);
        Long resoluCount = complaintRepository.countByStatut(ComplaintStatus.RESOLUE);
        Long fermeeCount = complaintRepository.countByStatut(ComplaintStatus.FERMEE);
        Long critiquesCount = complaintRepository.countByPriorite(ComplaintPriority.CRITICAL);
        
        // Count all priority levels
        Long basseCount = complaintRepository.countByPriorite(ComplaintPriority.LOW);
        Long moyenneCount = complaintRepository.countByPriorite(ComplaintPriority.MEDIUM);
        Long hauteCount = complaintRepository.countByPriorite(ComplaintPriority.HIGH);
        
        // Build priority map with French keys
        Map<String, Long> priorityMap = new HashMap<>();
        priorityMap.put("basse", basseCount != null ? basseCount : 0);
        priorityMap.put("moyenne", moyenneCount != null ? moyenneCount : 0);
        priorityMap.put("haute", hauteCount != null ? hauteCount : 0);
        priorityMap.put("critique", critiquesCount != null ? critiquesCount : 0);
        
        Double averageResolutionTime = null;  // complaintRepository.getAverageResolutionTimeHours();
        Long resolvedCount = complaintRepository.getResolvedCount(ComplaintStatus.RESOLUE, ComplaintStatus.FERMEE);

        return ComplaintStatisticsResponse.builder()
                .totalComplaints(totalCount)
                .nouvelles(nouvellesCount != null ? nouvellesCount : 0)
                .enAnalyse(enAnalyseCount != null ? enAnalyseCount : 0)
                .assignees(assigneesCount != null ? assigneesCount : 0)
                .enTraitement(enTraitementCount != null ? enTraitementCount : 0)
                .resolues(resoluCount != null ? resoluCount : 0)
                .fermees(fermeeCount != null ? fermeeCount : 0)
                .critiques(critiquesCount != null ? critiquesCount : 0)
                .averageResolutionTime(averageResolutionTime != null ? averageResolutionTime : 0.0)
                .resolutionRate(totalCount > 0 ? (resolvedCount * 100.0 / totalCount) : 0.0)
                .byPriority(priorityMap)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ComplaintResponse> getCritical(Pageable pageable) {
        log.debug("Fetching critical complaints");

        return complaintRepository.findByPriorite(ComplaintPriority.CRITICAL, pageable)
                .map(complaintMapper::toResponse);
    }

    @Override
    public ComplaintResponse assign(Long complaintId, Long supervisorId) {
        log.debug("Assigning complaint {} to supervisor {}", complaintId, supervisorId);

        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found with id: " + complaintId));

        User supervisor = userRepository.findById(supervisorId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + supervisorId));

        complaint.setAssignedSupervisor(supervisor);
        complaint.setStatut(ComplaintStatus.ASSIGNEE);

        Complaint updated = complaintRepository.save(complaint);
        log.info("Complaint {} assigned to supervisor {}", complaintId, supervisorId);

        return complaintMapper.toResponse(updated);
    }

    /**
     * Génère une référence unique au format REC-YYYY-MMDD-XXXX
     */
    private String generateReference() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String datePart = now.format(formatter).replace("-", "");
        String randomPart = String.format("%04d", (int) (Math.random() * 10000));
        return String.format("REC-%s-%s", datePart, randomPart);
    }
}
