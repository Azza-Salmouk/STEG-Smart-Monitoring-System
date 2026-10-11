package com.steg.smartmonitoring.transformateur.service;

import com.steg.smartmonitoring.transformateur.dto.CreateTransformateurRequest;
import com.steg.smartmonitoring.transformateur.dto.TransformateurResponse;
import com.steg.smartmonitoring.transformateur.dto.UpdateTransformateurRequest;
import com.steg.smartmonitoring.transformateur.entity.RiskLevel;
import com.steg.smartmonitoring.transformateur.entity.StatusTransformateur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service pour la gestion des transformateurs
 */
public interface TransformateurService {

    /**
     * Créer un nouveau transformateur
     */
    TransformateurResponse create(CreateTransformateurRequest request);

    /**
     * Récupérer un transformateur par son ID
     */
    TransformateurResponse getById(Long id);

    /**
     * Récupérer tous les transformateurs avec pagination
     */
    Page<TransformateurResponse> getAll(Pageable pageable);

    /**
     * Mettre à jour un transformateur
     */
    TransformateurResponse update(Long id, UpdateTransformateurRequest request);

    /**
     * Supprimer un transformateur
     */
    void delete(Long id);

    /**
     * Obtenir les transformateurs d'un poste
     */
    List<TransformateurResponse> getByPosteId(Long posteId);

    /**
     * Recherche par référence
     */
    Page<TransformateurResponse> searchByReference(String reference, Pageable pageable);

    /**
     * Recherche par fabricant
     */
    Page<TransformateurResponse> searchByManufacturer(String manufacturer, Pageable pageable);

    /**
     * Recherche par status
     */
    Page<TransformateurResponse> searchByStatus(StatusTransformateur status, Pageable pageable);

    /**
     * Recherche par niveau de risque
     */
    Page<TransformateurResponse> searchByRiskLevel(RiskLevel riskLevel, Pageable pageable);

    /**
     * Recherche par health score (score <= maxScore)
     */
    Page<TransformateurResponse> searchByHealthScore(Integer maxScore, Pageable pageable);

    /**
     * Recherche avancée combinée
     */
    Page<TransformateurResponse> search(
        String reference,
        String manufacturer,
        StatusTransformateur status,
        RiskLevel riskLevel,
        Integer healthScoreMax,
        Long posteId,
        Pageable pageable
    );

    /**
     * Obtenir les transformateurs en alerte ou défaut
     */
    List<TransformateurResponse> getAlertingTransformateurs();

    /**
     * Obtenir les transformateurs critiques
     */
    List<TransformateurResponse> getCriticalTransformateurs();

    /**
     * Obtenir les statistiques des transformateurs
     */
    TransformateurStatisticsResponse getStatistics();
}
