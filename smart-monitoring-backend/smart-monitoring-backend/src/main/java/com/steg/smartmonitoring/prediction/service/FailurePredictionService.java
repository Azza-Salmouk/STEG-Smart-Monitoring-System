package com.steg.smartmonitoring.prediction.service;

import com.steg.smartmonitoring.prediction.dto.FailurePredictionResult;
import com.steg.smartmonitoring.transformateur.entity.Transformateur;

/**
 * Service for predicting transformer failure risk based on historical data
 */
public interface FailurePredictionService {

    /**
     * Predict failure risk for a transformateur
     */
    FailurePredictionResult predictFailure(Transformateur transformateur);

    /**
     * Get maintenance recommendations based on current state
     */
    MaintenanceRecommendation getRecommendations(Transformateur transformateur);

}
