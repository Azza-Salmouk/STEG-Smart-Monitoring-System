package com.steg.smartmonitoring.ai.exception;

/**
 * Exception for Ollama-related errors
 */
public class OllamaException extends RuntimeException {
    
    public OllamaException(String message) {
        super(message);
    }
    
    public OllamaException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public OllamaException(String message, int statusCode) {
        super(message + " (HTTP " + statusCode + ")");
    }
}
