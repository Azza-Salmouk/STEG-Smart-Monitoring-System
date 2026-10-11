package com.steg.smartmonitoring.ai.dto;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Ollama API responses (internal format)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OllamaResponse {
    
    private String response;
    private String model;
    private Long totalDuration;
    private Long loadDuration;
    private Long promptEvalCount;
    private Long promptEvalDuration;
    private Long evalCount;
    private Long evalDuration;
    
    /**
     * Parse Ollama JSON response
     */
    public static OllamaResponse fromJson(String json) {
        try {
            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
            
            // Extract response - handle null or missing fields gracefully
            String responseText = "";
            if (jsonObject.has("response") && !jsonObject.get("response").isJsonNull()) {
                responseText = jsonObject.get("response").getAsString();
            }
            
            String modelName = "";
            if (jsonObject.has("model") && !jsonObject.get("model").isJsonNull()) {
                modelName = jsonObject.get("model").getAsString();
            }
            
            Long totalDuration = 0L;
            if (jsonObject.has("total_duration") && !jsonObject.get("total_duration").isJsonNull()) {
                totalDuration = jsonObject.get("total_duration").getAsLong();
            }
            
            return OllamaResponse.builder()
                    .response(responseText)
                    .model(modelName)
                    .totalDuration(totalDuration)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Ollama response: " + e.getMessage());
        }
    }
}
