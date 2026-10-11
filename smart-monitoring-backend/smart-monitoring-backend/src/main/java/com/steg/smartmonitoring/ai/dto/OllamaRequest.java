package com.steg.smartmonitoring.ai.dto;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Ollama API requests (internal format)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OllamaRequest {
    
    private String model;
    private String prompt;
    @Builder.Default
    private Boolean stream = false;
    
    /**
     * Convert to JSON for Ollama API
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("model", model);
        json.addProperty("prompt", prompt);
        json.addProperty("stream", stream);
        return json;
    }
}
