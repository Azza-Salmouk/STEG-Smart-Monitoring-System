package com.steg.smartmonitoring.ai.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Full chatbot response DTO
 * Returns only relevant information to client, not raw internal data
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatbotMessageResponse {
    
    // Response message from Ollama
    private String message;
    
    // Response status
    private ResponseStatus status;
    
    // User role (for logging)
    private String userRole;
    
    // Model used
    private String model;
    
    // Metadata
    private LocalDateTime timestamp;
    private Long processingTimeMs;
    
    // Error details (only if status != SUCCESS)
    private String errorMessage;
    
    // Confidence/reliability indicator
    private ResponseConfidence confidence;
    
    public enum ResponseStatus {
        SUCCESS,           // Ollama responded successfully
        UNAVAILABLE,       // Ollama service unavailable
        INVALID_REQUEST,   // Request validation failed
        UNAUTHORIZED,      // User not authenticated
        FORBIDDEN,         // User role doesn't have access
        ERROR              // Internal error
    }
    
    public enum ResponseConfidence {
        HIGH,      // Response is based on good data
        MEDIUM,    // Response is partial or limited
        LOW        // Response is limited or uncertain
    }
}
