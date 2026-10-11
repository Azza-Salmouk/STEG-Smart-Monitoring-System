package com.steg.smartmonitoring.ai.exception;

/**
 * Exception for Chatbot-related errors
 */
public class ChatbotException extends RuntimeException {
    
    public ChatbotException(String message) {
        super(message);
    }
    
    public ChatbotException(String message, Throwable cause) {
        super(message, cause);
    }
}
