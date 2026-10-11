package com.steg.smartmonitoring.complaint.entity;

import com.steg.smartmonitoring.poste.entity.Poste;
import com.steg.smartmonitoring.transformateur.entity.Transformateur;
import com.steg.smartmonitoring.user.entity.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Entité Complaint pour la gestion des réclamations clients
 */
@Entity
@Table(name = "complaints")
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String reference;

    @Column(nullable = false, length = 100)
    private String nomClient;

    @Column(nullable = false, length = 20)
    private String telephone;

    @Column(nullable = false, length = 255)
    private String adresse;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComplaintCategory categorie;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "priorite", nullable = false)
    private ComplaintPriority priorite;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private ComplaintStatus statut;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_supervisor_id")
    private User assignedSupervisor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poste_id")
    private Poste poste;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transformateur_id")
    private Transformateur transformateur;

    @Column(columnDefinition = "TEXT")
    private String commentaires;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime dateResolution;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Complaint() {
    }

    public Complaint(Long id, String reference, String nomClient, String telephone, String adresse, ComplaintCategory categorie, String description, ComplaintPriority priorite, ComplaintStatus statut, User assignedSupervisor, Poste poste, Transformateur transformateur, String commentaires, LocalDateTime createdAt, LocalDateTime dateResolution, LocalDateTime updatedAt) {
        this.id = id;
        this.reference = reference;
        this.nomClient = nomClient;
        this.telephone = telephone;
        this.adresse = adresse;
        this.categorie = categorie;
        this.description = description;
        this.priorite = priorite;
        this.statut = statut;
        this.assignedSupervisor = assignedSupervisor;
        this.poste = poste;
        this.transformateur = transformateur;
        this.commentaires = commentaires;
        this.createdAt = createdAt;
        this.dateResolution = dateResolution;
        this.updatedAt = updatedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String reference;
        private String nomClient;
        private String telephone;
        private String adresse;
        private ComplaintCategory categorie;
        private String description;
        private ComplaintPriority priorite;
        private ComplaintStatus statut;
        private User assignedSupervisor;
        private Poste poste;
        private Transformateur transformateur;
        private String commentaires;
        private LocalDateTime createdAt;
        private LocalDateTime dateResolution;
        private LocalDateTime updatedAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder reference(String reference) {
            this.reference = reference;
            return this;
        }

        public Builder nomClient(String nomClient) {
            this.nomClient = nomClient;
            return this;
        }

        public Builder telephone(String telephone) {
            this.telephone = telephone;
            return this;
        }

        public Builder adresse(String adresse) {
            this.adresse = adresse;
            return this;
        }

        public Builder categorie(ComplaintCategory categorie) {
            this.categorie = categorie;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder priorite(ComplaintPriority priorite) {
            this.priorite = priorite;
            return this;
        }

        public Builder statut(ComplaintStatus statut) {
            this.statut = statut;
            return this;
        }

        public Builder assignedSupervisor(User assignedSupervisor) {
            this.assignedSupervisor = assignedSupervisor;
            return this;
        }

        public Builder poste(Poste poste) {
            this.poste = poste;
            return this;
        }

        public Builder transformateur(Transformateur transformateur) {
            this.transformateur = transformateur;
            return this;
        }

        public Builder commentaires(String commentaires) {
            this.commentaires = commentaires;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder dateResolution(LocalDateTime dateResolution) {
            this.dateResolution = dateResolution;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Complaint build() {
            return new Complaint(id, reference, nomClient, telephone, adresse, categorie, description, priorite, statut, assignedSupervisor, poste, transformateur, commentaires, createdAt, dateResolution, updatedAt);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public ComplaintCategory getCategorie() {
        return categorie;
    }

    public void setCategorie(ComplaintCategory categorie) {
        this.categorie = categorie;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ComplaintPriority getPriorite() {
        return priorite;
    }

    public void setPriorite(ComplaintPriority priorite) {
        this.priorite = priorite;
    }

    public ComplaintStatus getStatut() {
        return statut;
    }

    public void setStatut(ComplaintStatus statut) {
        this.statut = statut;
    }

    public User getAssignedSupervisor() {
        return assignedSupervisor;
    }

    public void setAssignedSupervisor(User assignedSupervisor) {
        this.assignedSupervisor = assignedSupervisor;
    }

    public Poste getPoste() {
        return poste;
    }

    public void setPoste(Poste poste) {
        this.poste = poste;
    }

    public Transformateur getTransformateur() {
        return transformateur;
    }

    public void setTransformateur(Transformateur transformateur) {
        this.transformateur = transformateur;
    }

    public String getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(String commentaires) {
        this.commentaires = commentaires;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getDateResolution() {
        return dateResolution;
    }

    public void setDateResolution(LocalDateTime dateResolution) {
        this.dateResolution = dateResolution;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.statut == null) {
            this.statut = ComplaintStatus.NOUVELLE;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
