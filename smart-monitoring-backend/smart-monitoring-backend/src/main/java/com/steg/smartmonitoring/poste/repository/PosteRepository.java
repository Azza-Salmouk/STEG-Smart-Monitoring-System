package com.steg.smartmonitoring.poste.repository;

import com.steg.smartmonitoring.poste.entity.EtatPoste;
import com.steg.smartmonitoring.poste.entity.Poste;
import com.steg.smartmonitoring.poste.entity.TypePoste;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour l'entité Poste
 * Contient les requêtes personnalisées pour la recherche et le filtrage
 */
@Repository
public interface PosteRepository extends JpaRepository<Poste, Long> {

    /**
     * Vérifier si un poste avec ce code existe
     */
    boolean existsByCode(String code);

    /**
     * Trouver un poste par son code
     */
    Optional<Poste> findByCode(String code);

    /**
     * Recherche par nom (case-insensitive)
     */
    Page<Poste> findByNomContainingIgnoreCase(String nom, Pageable pageable);

    /**
     * Recherche par région
     */
    Page<Poste> findByRegionContainingIgnoreCase(String region, Pageable pageable);

    /**
     * Recherche par état
     */
    Page<Poste> findByEtat(EtatPoste etat, Pageable pageable);

    /**
     * Recherche par type de poste
     */
    Page<Poste> findByTypePoste(TypePoste typePoste, Pageable pageable);

    /**
     * Recherche combinée avancée
     */
    @Query("""
        SELECT p FROM Poste p 
        WHERE (:nom IS NULL OR p.nom LIKE %:nom%) 
        AND (:region IS NULL OR p.region LIKE %:region%) 
        AND (:etat IS NULL OR p.etat = :etat)
        AND (:typePoste IS NULL OR p.typePoste = :typePoste)
        """)
    Page<Poste> search(
        @Param("nom") String nom,
        @Param("region") String region,
        @Param("etat") EtatPoste etat,
        @Param("typePoste") TypePoste typePoste,
        Pageable pageable
    );

    /**
     * Compter les postes par état
     */
    long countByEtat(EtatPoste etat);

    /**
     * Compter les postes par type
     */
    long countByTypePoste(TypePoste typePoste);

    /**
     * Obtenir tous les postes d'une région
     */
    List<Poste> findByRegion(String region);
}
