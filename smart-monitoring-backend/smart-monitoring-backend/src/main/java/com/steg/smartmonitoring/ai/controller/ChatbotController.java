package com.steg.smartmonitoring.ai.controller;

import com.steg.smartmonitoring.ai.dto.ChatbotRequest;
import com.steg.smartmonitoring.ai.dto.ChatbotResponse;
import com.steg.smartmonitoring.ai.service.ChatbotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * REST Controller for the intelligent chatbot assistant
 * 
 * Endpoint: POST /api/ai/chatbot/message
 * Authentication: JWT required (@PreAuthorize)
 * RBAC: All authenticated roles can access
 * 
 * This endpoint is completely isolated and does not modify any existing functionality.
 */
@RestController
@RequestMapping("/api/ai/chatbot")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("isAuthenticated()")
@CrossOrigin(origins = {
    "http://localhost:3000",
    "http://localhost:5173",
    "http://localhost:4200"
})
public class ChatbotController {
    
    private final ChatbotService chatbotService;
    
    /**
     * Process user message through chatbot
     * 
     * POST /api/ai/chatbot/message
     * 
     * Request Example:
     * {
     *   "message": "Combien d'alertes critiques sont actuellement ouvertes ?"
     * }
     * 
     * Response Example (Success):
     * {
     *   "response": "Actuellement, il y a 5 alertes critiques ouvertes dans le système.",
     *   "status": "success",
     *   "timestamp": "2026-08-12T14:30:00.123",
     *   "processingTimeMs": 1250
     * }
     * 
     * Response Example (Error):
     * {
     *   "status": "error",
     *   "errorMessage": "Le service d'assistance est temporairement indisponible...",
     *   "timestamp": "2026-08-12T14:30:00.123"
     * }
     * 
     * @param request ChatbotRequest with user message
     * @return ChatbotResponse with AI response or error
     */
    @PostMapping("/message")
    public ResponseEntity<ChatbotResponse> processMessage(@Valid @RequestBody ChatbotRequest request) {
        try {
            log.info("Received chatbot message request");
            ChatbotResponse response = chatbotService.processMessage(request);
            
            // Determine HTTP status based on response status
            HttpStatus status = "success".equals(response.getStatus()) 
                    ? HttpStatus.OK 
                    : HttpStatus.SERVICE_UNAVAILABLE;
            
            return ResponseEntity.status(status).body(response);
            
        } catch (Exception e) {
            log.error("Error processing chatbot message: {}", e.getMessage(), e);
            ChatbotResponse errorResponse = ChatbotResponse.error("Une erreur interne s'est produite.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * Health check endpoint for chatbot service
     * 
     * GET /api/ai/chatbot/health
     * 
     * @return Health status
     */
    @GetMapping("/health")
    @PreAuthorize("permitAll()")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("{\"status\": \"chatbot service is running\"}");
    }
    
    /**
     * Diagnostic endpoint - check Ollama connectivity
     * 
     * GET /api/ai/chatbot/debug/ollama-status
     * 
     * @return Ollama status details
     */
    @GetMapping("/debug/ollama-status")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, Object>> ollamaStatus() {
        Map<String, Object> status = new LinkedHashMap<>();
        status.put("service", "chatbot");
        status.put("timestamp", java.time.LocalDateTime.now());
        
        try {
            boolean ollamaAvailable = chatbotService.isOllamaAvailable();
            status.put("ollama_available", ollamaAvailable);
            status.put("status", ollamaAvailable ? "ready" : "unavailable");
        } catch (Exception e) {
            status.put("ollama_available", false);
            status.put("error", e.getMessage());
            status.put("status", "error");
        }
        
        return ResponseEntity.ok(status);
    }
}
