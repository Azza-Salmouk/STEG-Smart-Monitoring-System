package com.steg.smartmonitoring.ai.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for Ollama local LLM
 * Load from application.properties with prefix "ollama."
 */
@Component
@ConfigurationProperties(prefix = "ollama")
@Data
public class OllamaProperties {
    
    private String baseUrl = "http://localhost:11434";
    private String model = "qwen2.5:3b";
    private Long timeout = 120000L;
    private Boolean enableChatbot = true;
    
    /**
     * Get the complete Ollama API URL for /api/chat endpoint (modern Ollama API)
     * The legacy /api/generate endpoint is deprecated
     */
    public String getApiUrl() {
        return baseUrl + "/api/chat";
    }
    
    /**
     * Get the Ollama tags URL for health check
     */
    public String getTagsUrl() {
        return baseUrl + "/api/tags";
    }
    
    /**
     * Validate configuration
     */
    public void validate() throws IllegalArgumentException {
        if (!enableChatbot) {
            throw new IllegalArgumentException("Chatbot is disabled in configuration");
        }
        if (baseUrl == null || baseUrl.isEmpty()) {
            throw new IllegalArgumentException("ollama.base-url is not configured");
        }
        if (model == null || model.isEmpty()) {
            throw new IllegalArgumentException("ollama.model is not configured");
        }
    }
}
