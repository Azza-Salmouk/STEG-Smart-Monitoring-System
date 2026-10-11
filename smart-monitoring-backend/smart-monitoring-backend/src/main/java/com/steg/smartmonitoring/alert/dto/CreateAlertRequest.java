package com.steg.smartmonitoring.alert.dto;

import com.steg.smartmonitoring.alert.entity.AlertLevel;
import com.steg.smartmonitoring.alert.entity.AlertType;
import jakarta.validation.constraints.*;

/**
 * DTO pour la création d'une alerte
 */
public class CreateAlertRequest {

    @NotBlank(message = "Le titre est requis")
    @Size(min = 5, max = 255, message = "Le titre doit faire entre 5 et 255 caractères")
    private String titre;

    @NotBlank(message = "La description est requise")
    @Size(min = 10, max = 2000, message = "La description doit faire entre 10 et 2000 caractères")
    private String description;

    @NotNull(message = "Le type est requis")
    private AlertType type;

    @NotNull(message = "Le niveau est requis")
    private AlertLevel niveau;

    @NotNull(message = "L'ID du transformateur est requis")
    @Positive(message = "L'ID du transformateur doit être positif")
    private Long transformateurId;

    @Positive(message = "L'ID de la mesure doit être positif")
    private Long measurementId;

    @Positive(message = "L'ID du poste doit être positif")
    private Long posteId;

    public CreateAlertRequest() {
    }

    public CreateAlertRequest(String titre, String description, AlertType type, AlertLevel niveau,
                             Long transformateurId, Long measurementId, Long posteId) {
        this.titre = titre;
        this.description = description;
        this.type = type;
        this.niveau = niveau;
        this.transformateurId = transformateurId;
        this.measurementId = measurementId;
        this.posteId = posteId;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AlertType getType() {
        return type;
    }

    public void setType(AlertType type) {
        this.type = type;
    }

    public AlertLevel getNiveau() {
        return niveau;
    }

    public void setNiveau(AlertLevel niveau) {
        this.niveau = niveau;
    }

    public Long getTransformateurId() {
        return transformateurId;
    }

    public void setTransformateurId(Long transformateurId) {
        this.transformateurId = transformateurId;
    }

    public Long getMeasurementId() {
        return measurementId;
    }

    public void setMeasurementId(Long measurementId) {
        this.measurementId = measurementId;
    }

    public Long getPosteId() {
        return posteId;
    }

    public void setPosteId(Long posteId) {
        this.posteId = posteId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String titre;
        private String description;
        private AlertType type;
        private AlertLevel niveau;
        private Long transformateurId;
        private Long measurementId;
        private Long posteId;

        public Builder titre(String titre) {
            this.titre = titre;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder type(AlertType type) {
            this.type = type;
            return this;
        }

        public Builder niveau(AlertLevel niveau) {
            this.niveau = niveau;
            return this;
        }

        public Builder transformateurId(Long transformateurId) {
            this.transformateurId = transformateurId;
            return this;
        }

        public Builder measurementId(Long measurementId) {
            this.measurementId = measurementId;
            return this;
        }

        public Builder posteId(Long posteId) {
            this.posteId = posteId;
            return this;
        }

        public CreateAlertRequest build() {
            return new CreateAlertRequest(titre, description, type, niveau, transformateurId, 
                                         measurementId, posteId);
        }
    }
}
