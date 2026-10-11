package com.steg.smartmonitoring.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * DTO for chatbot message responses
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatbotResponse {
    
    private String response;
    private String status;  // "success", "error", "unavailable"
    private String errorMessage;  // if status = error
    private LocalDateTime timestamp;
    private Long processingTimeMs;
    
    /**
     * Create success response
     */
    public static ChatbotResponse success(String response, Long processingTimeMs) {
        return ChatbotResponse.builder()
                .response(response)
                .status("success")
                .timestamp(LocalDateTime.now())
                .processingTimeMs(processingTimeMs)
                .build();
    }
    
    /**
     * Create error response
     */
    public static ChatbotResponse error(String errorMessage) {
        return ChatbotResponse.builder()
                .status("error")
                .errorMessage(errorMessage)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Create unavailable response
     */
    public static ChatbotResponse unavailable() {
        return ChatbotResponse.builder()
                .status("unavailable")
                .errorMessage("Le service d'assistance est temporairement indisponible. Vérifiez que le moteur IA local est démarré.")
                .timestamp(LocalDateTime.now())
                .build();
    }
}
