package com.steg.smartmonitoring.alert.service;

import com.steg.smartmonitoring.alert.dto.AlertResponse;
import com.steg.smartmonitoring.alert.dto.AlertStatisticsResponse;
import com.steg.smartmonitoring.alert.dto.CreateAlertRequest;
import com.steg.smartmonitoring.alert.dto.UpdateAlertRequest;
import com.steg.smartmonitoring.alert.entity.AlertLevel;
import com.steg.smartmonitoring.alert.entity.AlertStatus;
import com.steg.smartmonitoring.alert.entity.AlertType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

/**
 * Service pour la gestion des alertes
 */
public interface AlertService {

    /**
     * Créer une nouvelle alerte
     */
    AlertResponse create(CreateAlertRequest request);

    /**
     * Récupérer une alerte par son ID
     */
    AlertResponse getById(Long id);

    /**
     * Récupérer toutes les alertes
     */
    Page<AlertResponse> getAll(Pageable pageable);

    /**
     * Mettre à jour une alerte
     */
    AlertResponse update(Long id, UpdateAlertRequest request);

    /**
     * Supprimer une alerte
     */
    void delete(Long id);

    /**
     * Rechercher les alertes par statut
     */
    Page<AlertResponse> getByStatut(AlertStatus statut, Pageable pageable);

    /**
     * Rechercher les alertes par niveau
     */
    Page<AlertResponse> getByNiveau(AlertLevel niveau, Pageable pageable);

    /**
     * Rechercher les alertes par type
     */
    Page<AlertResponse> getByType(AlertType type, Pageable pageable);

    /**
     * Rechercher les alertes par transformateur
     */
    Page<AlertResponse> getByTransformateur(Long transformateurId, Pageable pageable);

    /**
     * Rechercher les alertes par poste
     */
    Page<AlertResponse> getByPoste(Long posteId, Pageable pageable);

    /**
     * Rechercher les alertes par titre
     */
    Page<AlertResponse> searchByTitre(String titre, Pageable pageable);

    /**
     * Rechercher les alertes par période
     */
    Page<AlertResponse> getByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    /**
     * Recherche avancée
     */
    Page<AlertResponse> searchAdvanced(
            String titre,
            AlertStatus statut,
            AlertLevel niveau,
            AlertType type,
            Long transformateurId,
            Long posteId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable
    );

    /**
     * Obtenir les alertes critiques
     */
    Page<AlertResponse> getCritical(Pageable pageable);

    /**
     * Obtenir les alertes actives
     */
    Page<AlertResponse> getActive(Pageable pageable);

    /**
     * Obtenir les alertes récentes
     */
    Page<AlertResponse> getRecent(Pageable pageable);

    /**
     * Reconnaître une alerte (marquer comme acknowledgée)
     */
    AlertResponse acknowledge(Long id);

    /**
     * Résoudre une alerte
     */
    AlertResponse resolve(Long id, Long resolvedByUserId);

    /**
     * Archiver une alerte
     */
    AlertResponse archive(Long id);

    /**
     * Obtenir les statistiques
     */
    AlertStatisticsResponse getStatistics();

    /**
     * Changer le statut d'une alerte
     */
    AlertResponse changeStatus(Long id, AlertStatus newStatus);
}
