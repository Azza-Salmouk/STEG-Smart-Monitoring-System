package com.steg.smartmonitoring.poste.service;

import com.steg.smartmonitoring.poste.dto.CreatePosteRequest;
import com.steg.smartmonitoring.poste.dto.PosteResponse;
import com.steg.smartmonitoring.poste.dto.UpdatePosteRequest;
import com.steg.smartmonitoring.poste.entity.EtatPoste;
import com.steg.smartmonitoring.poste.entity.TypePoste;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service pour la gestion des postes
 */
public interface PosteService {

    /**
     * Créer un nouveau poste
     */
    PosteResponse create(CreatePosteRequest request);

    /**
     * Récupérer un poste par son ID
     */
    PosteResponse getById(Long id);

    /**
     * Récupérer tous les postes avec pagination
     */
    Page<PosteResponse> getAll(Pageable pageable);

    /**
     * Mettre à jour un poste
     */
    PosteResponse update(Long id, UpdatePosteRequest request);

    /**
     * Supprimer un poste
     */
    void delete(Long id);

    /**
     * Recherche par nom
     */
    Page<PosteResponse> searchByNom(String nom, Pageable pageable);

    /**
     * Recherche par région
     */
    Page<PosteResponse> searchByRegion(String region, Pageable pageable);

    /**
     * Recherche par état
     */
    Page<PosteResponse> searchByEtat(EtatPoste etat, Pageable pageable);

    /**
     * Recherche par type
     */
    Page<PosteResponse> searchByType(TypePoste typePoste, Pageable pageable);

    /**
     * Recherche avancée combinée
     */
    Page<PosteResponse> search(String nom, String region, EtatPoste etat, TypePoste typePoste, Pageable pageable);

    /**
     * Obtenir les statistiques des postes
     */
    PosteStatisticsResponse getStatistics();
}
