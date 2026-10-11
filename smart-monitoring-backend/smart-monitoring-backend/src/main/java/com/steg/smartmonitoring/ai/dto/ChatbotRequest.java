package com.steg.smartmonitoring.ai.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for chatbot message requests
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatbotRequest {
    
    @NotBlank(message = "Message cannot be blank")
    private String message;
    
    /**
     * Validate message content
     */
    public boolean isValid() {
        return message != null 
            && !message.trim().isEmpty() 
            && message.length() <= 2000;
    }
}
