package com.steg.smartmonitoring.ai.config;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Configuration for Ollama HTTP client
 */
@Configuration
public class OllamaConfig {
    
    /**
     * Create OkHttpClient bean with Ollama timeout configuration
     */
    @Bean
    public OkHttpClient ollamaOkHttpClient(OllamaProperties ollamaProperties) {
        return new OkHttpClient.Builder()
                .connectTimeout(ollamaProperties.getTimeout(), TimeUnit.MILLISECONDS)
                .readTimeout(ollamaProperties.getTimeout(), TimeUnit.MILLISECONDS)
                .writeTimeout(ollamaProperties.getTimeout(), TimeUnit.MILLISECONDS)
                .build();
    }
}
