package com.steg.smartmonitoring.measurement.mapper;

import com.steg.smartmonitoring.measurement.dto.CreateMeasurementRequest;
import com.steg.smartmonitoring.measurement.dto.MeasurementResponse;
import com.steg.smartmonitoring.measurement.entity.Measurement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Mapper pour les conversions entre les entités et les DTOs
 * pour le module de mesures IoT.
 * 
 * Respecte le pattern Builder pour la création d'objets.
 */
@Slf4j
@Component
public class MeasurementMapper {

    /**
     * Convertit une entité Measurement en MeasurementResponse
     * 
     * @param measurement Entité à convertir
     * @return DTO de réponse
     */
    public MeasurementResponse toResponse(Measurement measurement) {
        if (measurement == null) {
            return null;
        }

        return MeasurementResponse.builder()
            .id(measurement.getId())
            .transformateurId(measurement.getTransformateur().getId())
            .transformateurReference(measurement.getTransformateur().getReference())
            .measurementDate(measurement.getMeasurementDate())
            .temperature(measurement.getTemperature())
            .voltage(measurement.getVoltage())
            .current(measurement.getCurrent())
            .power(measurement.getPower())
            .energyConsumption(measurement.getEnergyConsumption())
            .frequency(measurement.getFrequency())
            .humidity(measurement.getHumidity())
            .healthScore(measurement.getHealthScore())
            .status(measurement.getStatus())
            .createdAt(measurement.getCreatedAt())
            .updatedAt(measurement.getUpdatedAt())
            .build();
    }

    /**
     * Convertit une CreateMeasurementRequest en entité Measurement
     * NOTE: Le transformateur, healthScore et status doivent être définis
     * après la création par le service.
     * 
     * @param request DTO de création
     * @return Entité non persistée
     */
    public Measurement mapToEntity(CreateMeasurementRequest request) {
        if (request == null) {
            return null;
        }

        return Measurement.builder()
            .measurementDate(request.getMeasurementDate())
            .temperature(request.getTemperature())
            .voltage(request.getVoltage())
            .current(request.getCurrent())
            .power(request.getPower())
            .energyConsumption(request.getEnergyConsumption())
            .frequency(request.getFrequency())
            .humidity(request.getHumidity())
            .build();
    }

    /**
     * Copie les champs d'une CreateMeasurementRequest vers une entité existante
     * NOTE: Peut être utilisé pour les mises à jour partielles (PATCH)
     * 
     * @param request DTO source
     * @param measurement Entité destination
     */
    public void updateEntityFromRequest(CreateMeasurementRequest request, Measurement measurement) {
        if (request == null || measurement == null) {
            return;
        }

        if (request.getMeasurementDate() != null) {
            measurement.setMeasurementDate(request.getMeasurementDate());
        }
        if (request.getTemperature() != null) {
            measurement.setTemperature(request.getTemperature());
        }
        if (request.getVoltage() != null) {
            measurement.setVoltage(request.getVoltage());
        }
        if (request.getCurrent() != null) {
            measurement.setCurrent(request.getCurrent());
        }
        if (request.getPower() != null) {
            measurement.setPower(request.getPower());
        }
        if (request.getEnergyConsumption() != null) {
            measurement.setEnergyConsumption(request.getEnergyConsumption());
        }
        if (request.getFrequency() != null) {
            measurement.setFrequency(request.getFrequency());
        }
        if (request.getHumidity() != null) {
            measurement.setHumidity(request.getHumidity());
        }
    }
}
