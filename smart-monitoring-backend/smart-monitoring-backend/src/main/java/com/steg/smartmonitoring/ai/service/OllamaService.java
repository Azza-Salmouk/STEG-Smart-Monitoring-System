package com.steg.smartmonitoring.ai.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.steg.smartmonitoring.ai.config.OllamaProperties;
import com.steg.smartmonitoring.ai.dto.OllamaRequest;
import com.steg.smartmonitoring.ai.dto.OllamaResponse;
import com.steg.smartmonitoring.ai.exception.OllamaException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Service for communicating with local Ollama instance
 * Handles HTTP requests to Ollama API using the /api/chat endpoint
 * (which replaces the deprecated /api/generate endpoint)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OllamaService {
    
    private final OllamaProperties ollamaProperties;
    private final OkHttpClient ollamaOkHttpClient;
    
    /**
     * Send request to Ollama model using /api/chat endpoint and get response
     * This endpoint supports the messages format which is the modern Ollama API
     * 
     * @param prompt User prompt to send to the model
     * @return Generated response from the model
     * @throws OllamaException if communication fails
     */
    public String generateResponse(String prompt) throws OllamaException {
        try {
            // Validate configuration
            ollamaProperties.validate();
            
            // Build request using /api/chat format (modern Ollama API)
            JsonObject requestJson = buildChatRequest(prompt);
            
            // Make HTTP POST request to /api/chat
            String chatUrl = ollamaProperties.getBaseUrl() + "/api/chat";
            Request request = new Request.Builder()
                    .url(chatUrl)
                    .post(RequestBody.create(
                            requestJson.toString(),
                            MediaType.parse("application/json")
                    ))
                    .build();
            
            log.info("Sending chat request to Ollama: {}", chatUrl);
            log.debug("Model: {}, Prompt length: {}", ollamaProperties.getModel(), prompt.length());
            
            try (Response response = ollamaOkHttpClient.newCall(request).execute()) {
                
                if (!response.isSuccessful()) {
                    String errorBody = response.body() != null ? response.body().string() : "Unknown error";
                    log.error("Ollama /api/chat returned error: HTTP {} - {}", response.code(), errorBody);
                    throw new OllamaException("Ollama returned error: HTTP " + response.code(), response.code());
                }
                
                String responseBody = response.body().string();
                log.debug("Ollama response received, size: {} bytes", responseBody.length());
                
                // Parse response from /api/chat format
                String modelResponse = parseChatResponse(responseBody);
                log.info("Model response processed successfully");
                
                return modelResponse;
                
            }
            
        } catch (IOException e) {
            log.error("IO Error communicating with Ollama: {}", e.getMessage());
            throw new OllamaException("Failed to connect to Ollama: " + e.getMessage(), e);
        } catch (OllamaException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error in OllamaService: {}", e.getMessage(), e);
            throw new OllamaException("Unexpected error: " + e.getMessage(), e);
        }
    }
    
    /**
     * Build request JSON for /api/chat endpoint (modern Ollama API format)
     * 
     * @param prompt User prompt
     * @return JSON request object
     */
    private JsonObject buildChatRequest(String prompt) {
        JsonObject request = new JsonObject();
        request.addProperty("model", ollamaProperties.getModel());
        request.addProperty("stream", false);
        
        // Build messages array with user message
        JsonArray messages = new JsonArray();
        JsonObject userMessage = new JsonObject();
        userMessage.addProperty("role", "user");
        userMessage.addProperty("content", prompt);
        messages.add(userMessage);
        
        request.add("messages", messages);
        
        return request;
    }
    
    /**
     * Parse response from /api/chat endpoint
     * Extracts the assistant's message from the chat response format
     * 
     * @param jsonResponse Raw JSON response from Ollama
     * @return Extracted response text
     */
    private String parseChatResponse(String jsonResponse) {
        try {
            JsonObject response = com.google.gson.JsonParser.parseString(jsonResponse).getAsJsonObject();
            
            // Extract message.content from /api/chat response
            if (response.has("message") && response.get("message").isJsonObject()) {
                JsonObject message = response.getAsJsonObject("message");
                if (message.has("content") && !message.get("content").isJsonNull()) {
                    String content = message.get("content").getAsString();
                    log.debug("Extracted response: {} characters", content.length());
                    return content;
                }
            }
            
            log.warn("Unexpected response format from Ollama");
            return "Erreur: format de réponse inattendu de Ollama";
            
        } catch (Exception e) {
            log.error("Failed to parse Ollama chat response: {}", e.getMessage());
            throw new RuntimeException("Failed to parse Ollama response: " + e.getMessage());
        }
    }
    
    /**
     * Check if Ollama is available and responding
     * 
     * @return true if Ollama is available, false otherwise
     */
    public boolean isAvailable() {
        try {
            Request request = new Request.Builder()
                    .url(ollamaProperties.getTagsUrl())
                    .get()
                    .build();
            
            try (Response response = ollamaOkHttpClient.newCall(request).execute()) {
                boolean isAvailable = response.isSuccessful();
                if (isAvailable) {
                    log.debug("Ollama is available");
                } else {
                    log.warn("Ollama returned status: {}", response.code());
                }
                return isAvailable;
            }
        } catch (IOException e) {
            log.warn("Ollama is not available: {}", e.getMessage());
            return false;
        }
    }
}
