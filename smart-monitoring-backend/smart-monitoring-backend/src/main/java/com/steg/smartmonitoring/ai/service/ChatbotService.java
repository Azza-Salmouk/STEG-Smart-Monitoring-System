package com.steg.smartmonitoring.ai.service;

import com.steg.smartmonitoring.ai.dto.ChatbotRequest;
import com.steg.smartmonitoring.ai.dto.ChatbotResponse;
import com.steg.smartmonitoring.ai.exception.ChatbotException;
import com.steg.smartmonitoring.ai.exception.OllamaException;
import com.steg.smartmonitoring.alert.service.AlertService;
import com.steg.smartmonitoring.intervention.service.InterventionService;
import com.steg.smartmonitoring.complaint.service.ComplaintService;
import com.steg.smartmonitoring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Main chatbot service for processing user messages and generating AI responses
 * Applies RBAC filtering to ensure users only see data they have access to
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ChatbotService {
    
    private final OllamaService ollamaService;
    private final AlertService alertService;
    private final InterventionService interventionService;
    private final ComplaintService complaintService;
    private final UserRepository userRepository;
    
    private static final String SYSTEM_PROMPT = """
        Tu es l'Assistant Intelligent STEG Smart Monitoring System.
        Tu aides les équipes du système de surveillance électrique STEG à gérer les alertes, interventions, 
        réclamations et mesures IoT des transformateurs et postes de distribution.
        
        === DIRECTIVES STRICTES ===
        1. LANGUE & STYLE: Réponds toujours en français. Sois professionnel, concis et clair. Pas de jargon inutile.
        2. DONNÉES: Utilise UNIQUEMENT les informations fournies dans le contexte. Aucune invention de chiffres, statuts ou détails.
        3. TRANSPARENCE: Si les données demandées ne sont pas disponibles, réponds: "Je ne dispose d'aucune donnée correspondant à votre demande."
        4. SÉCURITÉ: Ne jamais donner d'ordres directs concernant l'équipement électrique. Recommande toujours une vérification par un agent habilité.
        5. CONTEXTE PERSONNALISÉ: Respecte strictement les permissions de l'utilisateur (ADMIN=accès complet, SUPERVISOR=données assignées, AGENT=interventions assignées, SERVICE_CLIENT=données personnelles).
        
        === COMPÉTENCES MÉTIER ===
        - Analyser les alertes (actives, critiques, résolues) et expliquer les statuts
        - Résumer les interventions en cours (statut, priorité, transformer assigné, agent responsable)
        - Traiter les réclamations: statut, catégorie, progression
        - Interpréter les mesures IoT: tension, courant, température des transformateurs
        - Aider à diagnostiquer les problèmes électriques simples
        - Recommander les actions appropriées (escalade, intervention technique, maintenance)
        
        === EXEMPLES DE QUESTIONS ===
        Alertes: "Combien d'alertes critiques sont actives?", "Quels transformateurs ont des alertes?"
        Interventions: "Quelles interventions m'ont été assignées?", "Qui intervient sur le transformateur TR-042?"
        Réclamations: "Combien de réclamations sont en cours?", "Quel est le statut de ma réclamation?"
        Mesures: "Quelle est la température du transformateur TR-031?", "Y a-t-il des mesures anormales?"
        
        === TONE & ATTITUDE ===
        - Aide proactive et sympathique envers les clients (SERVICE_CLIENT)
        - Efficacité et précision pour les superviseurs (SUPERVISOR) et agents (AGENT)
        - Vue d'ensemble stratégique pour les administrateurs (ADMIN)
        - Jamais condescendant, toujours respectueux de l'expertise de l'utilisateur
        """;
    
    /**
     * Process user message and generate AI response
     * Applies role-based filtering before sending to LLM
     * Orchestrates the complete chatbot workflow
     * 
     * @param request Chatbot request with user message
     * @return Chatbot response with AI-generated answer
     */
    public ChatbotResponse processMessage(ChatbotRequest request) {
        long startTime = System.currentTimeMillis();
        
        try {
            // Validate request
            if (!request.isValid()) {
                log.warn("Invalid chatbot request: message empty or too long");
                return ChatbotResponse.error("Le message est vide ou trop long (max 2000 caractères)");
            }
            
            // Extract user identity and role from JWT
            String userRole = getCurrentUserRole();
            Long userId = getCurrentUserId();
            log.info("Chatbot request from user ID: {}, role: {}, message length: {}", 
                    userId, userRole, request.getMessage().length());
            
            // Check if Ollama service is available before proceeding
            if (!ollamaService.isAvailable()) {
                log.warn("Ollama service unavailable for user role: {}", userRole);
                return ChatbotResponse.unavailable();
            }
            
            // Build role-specific context for data filtering
            // CRITICAL: This ensures RBAC is respected - users only see their authorized data
            long contextBuildStart = System.currentTimeMillis();
            String context = buildContextForRole(userRole, request.getMessage());
            long contextBuildTime = System.currentTimeMillis() - contextBuildStart;
            log.debug("Context built in {}ms for role {}", contextBuildTime, userRole);
            
            // Build complete prompt combining system instructions, context, and user question
            String fullPrompt = buildFullPrompt(request.getMessage(), context);
            
            // Send to Ollama and get response
            long ollamaStart = System.currentTimeMillis();
            String ollamaResponse = ollamaService.generateResponse(fullPrompt);
            long ollamaTime = System.currentTimeMillis() - ollamaStart;
            log.debug("Ollama response generated in {}ms", ollamaTime);
            
            long processingTime = System.currentTimeMillis() - startTime;
            log.info("Chatbot response complete in {}ms (context: {}ms, ollama: {}ms)", 
                    processingTime, contextBuildTime, ollamaTime);
            
            // Return successful response with metadata
            return ChatbotResponse.success(ollamaResponse, processingTime);
            
        } catch (OllamaException e) {
            log.error("Ollama service error: {}", e.getMessage());
            return ChatbotResponse.error("Le service d'assistance a rencontré une erreur. Vérifiez que le moteur IA local est démarré.");
        } catch (ChatbotException e) {
            log.error("Chatbot error: {}", e.getMessage());
            return ChatbotResponse.error(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error in chatbot service", e);
            return ChatbotResponse.error("Une erreur interne s'est produite. Veuillez réessayer plus tard.");
        }
    }
    
    /**
     * Public method to check if Ollama is available
     * Used for diagnostic purposes
     * 
     * @return true if Ollama is available
     */
    public boolean isOllamaAvailable() {
        return ollamaService.isAvailable();
    }
    
    /**
     * Used for role-based data filtering
     * 
     * @return User ID or null if not authenticated
     */
    private Long getCurrentUserId() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || auth.getName() == null) {
                return null;
            }
            
            String userEmail = auth.getName();
            var userOptional = userRepository.findByEmail(userEmail);
            return userOptional.map(user -> user.getId()).orElse(null);
        } catch (Exception e) {
            log.warn("Error extracting current user ID: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * Extract current user role from JWT
     * Used to apply role-based data filtering
     * 
     * @return User role or "GUEST" if not authenticated
     */
    private String getCurrentUserRole() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getAuthorities() != null) {
                return authentication.getAuthorities().stream()
                        .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                        .findFirst()
                        .orElse("GUEST");
            }
            return "GUEST";
        } catch (Exception e) {
            log.warn("Error extracting user role: {}", e.getMessage());
            return "GUEST";
        }
    }
    
    /**
     * Build role-specific context for data filtering
     * CRITICAL: This ensures users only see data they have access to
     * 
     * @param userRole User's role (ADMIN, SUPERVISOR, AGENT, SERVICE_CLIENT)
     * @param userQuestion User's question for context relevance
     * @return Filtered context data for the LLM
     */
    private String buildContextForRole(String userRole, String userQuestion) {
        StringBuilder context = new StringBuilder();
        
        try {
            // Add role-specific data based on user's permissions
            switch (userRole) {
                case "ADMIN" -> {
                    log.debug("Building context for ADMIN role (full access)");
                    context.append(buildAdminContext(userQuestion));
                }
                case "SUPERVISOR" -> {
                    log.debug("Building context for SUPERVISOR role (regional access)");
                    context.append(buildSupervisorContext(userQuestion));
                }
                case "AGENT" -> {
                    log.debug("Building context for AGENT role (assigned access)");
                    context.append(buildAgentContext(userQuestion));
                }
                case "SERVICE_CLIENT" -> {
                    log.debug("Building context for SERVICE_CLIENT role (personal access)");
                    context.append(buildServiceClientContext(userQuestion));
                }
                default -> {
                    log.debug("Building context for GUEST role (limited access)");
                    context.append("Contexte limité pour utilisateur non authentifié.");
                }
            }
        } catch (Exception e) {
            log.warn("Error building context for role {}: {}", userRole, e.getMessage());
            context.append("Contexte données indisponible. ");
        }
        
        return context.toString();
    }
    
    /**
     * Build context for ADMIN role (full system access)
     * ADMIN can see all data: all alerts, interventions, complaints, statistics
     * 
     * @param question User question for context relevance
     * @return Full context data
     */
    private String buildAdminContext(String question) {
        StringBuilder context = new StringBuilder();
        
        // Include global statistics if relevant
        if (containsKeyword(question, "alerte", "alert")) {
            try {
                var stats = alertService.getStatistics();
                context.append("Statistiques d'alertes globales :\n");
                context.append("- Total alertes : ").append(stats.getTotalAlerts()).append("\n");
                context.append("- Alertes actives : ").append(stats.getActiveAlerts()).append("\n");
                context.append("- Alertes critiques : ").append(stats.getCriticalAlerts()).append("\n");
                context.append("- Alertes résolues : ").append(stats.getResolvedAlerts()).append("\n");
                context.append("- Alertes reconnues : ").append(stats.getAcknowledgedAlerts()).append("\n");
            } catch (Exception e) {
                log.warn("Error fetching alert statistics: {}", e.getMessage());
                context.append("Données d'alertes temporairement indisponibles.\n");
            }
        }
        
        if (containsKeyword(question, "intervention")) {
            try {
                var stats = interventionService.getStatistics();
                context.append("Statistiques d'interventions globales :\n");
                context.append("- Total interventions : ").append(stats.getTotalInterventions()).append("\n");
                context.append("- En cours : ").append(stats.getEnCours()).append("\n");
                context.append("- Terminées : ").append(stats.getTerminees()).append("\n");
                context.append("- En attente : ").append(stats.getEnAttente()).append("\n");
            } catch (Exception e) {
                log.warn("Error fetching intervention statistics: {}", e.getMessage());
                context.append("Données d'interventions temporairement indisponibles.\n");
            }
        }
        
        if (containsKeyword(question, "réclamation", "complaint")) {
            try {
                var stats = complaintService.getStatistics();
                context.append("Statistiques de réclamations globales :\n");
                context.append("- Total réclamations : ").append(stats.getTotalComplaints()).append("\n");
                context.append("- En traitement : ").append(stats.getEnTraitement()).append("\n");
                context.append("- Résolues : ").append(stats.getResolues()).append("\n");
                context.append("- Fermées : ").append(stats.getFermees()).append("\n");
            } catch (Exception e) {
                log.warn("Error fetching complaint statistics: {}", e.getMessage());
                context.append("Données de réclamations temporairement indisponibles.\n");
            }
        }
        
        return context.toString();
    }
    
    /**
     * Build context for SUPERVISOR role (regional access)
     * SUPERVISOR can see:
     * 1. Complaints assigned to them (via ComplaintService.getBySupervisor())
     * 2. Related alerts and interventions for their assigned transformers
     * 3. Statistics limited to their scope
     * 
     * @param question User question for context relevance
     * @return Region-filtered context data
     */
    private String buildSupervisorContext(String question) {
        StringBuilder context = new StringBuilder();
        
        try {
            // Get current supervisor ID from authentication
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || auth.getName() == null) {
                context.append("Contexte superviseur indisponible (authentification manquante).\n");
                return context.toString();
            }
            
            String supervisorEmail = auth.getName();
            var supervisorUser = userRepository.findByEmail(supervisorEmail);
            
            if (supervisorUser.isEmpty()) {
                log.warn("Supervisor user not found: {}", supervisorEmail);
                context.append("Contexte superviseur indisponible.\n");
                return context.toString();
            }
            
            Long supervisorId = supervisorUser.get().getId();
            context.append("=== CONTEXTE SUPERVISEUR ===\n");
            context.append("Accès limité aux données assignées.\n\n");
            
            // Fetch supervisor-assigned complaints
            if (containsKeyword(question, "réclamation", "complaint")) {
                try {
                    var complaints = complaintService.getBySupervisor(supervisorId, Pageable.unpaged());
                    if (complaints.hasContent()) {
                        context.append("Réclamations assignées (").append(complaints.getTotalElements()).append(") :\n");
                        complaints.getContent().stream()
                                .limit(5)  // Limit to 5 most recent for context size
                                .forEach(complaint -> {
                                    context.append("- ID: ").append(complaint.getId())
                                            .append(", Statut: ").append(complaint.getStatut())
                                            .append(", Catégorie: ").append(complaint.getCategorie())
                                            .append("\n");
                                });
                        if (complaints.getTotalElements() > 5) {
                            context.append("... et ").append(complaints.getTotalElements() - 5).append(" autres.\n");
                        }
                    } else {
                        context.append("Aucune réclamation assignée.\n");
                    }
                } catch (Exception e) {
                    log.warn("Error fetching supervisor complaints: {}", e.getMessage());
                    context.append("Données de réclamations temporairement indisponibles.\n");
                }
                context.append("\n");
            }
            
            // Fetch global alert statistics (supervisors can see system-wide alerts)
            if (containsKeyword(question, "alerte", "alert")) {
                try {
                    var stats = alertService.getStatistics();
                    context.append("Statistiques d'alertes système :\n");
                    context.append("- Total alertes : ").append(stats.getTotalAlerts()).append("\n");
                    context.append("- Alertes actives : ").append(stats.getActiveAlerts()).append("\n");
                    context.append("- Alertes critiques : ").append(stats.getCriticalAlerts()).append("\n");
                } catch (Exception e) {
                    log.warn("Error fetching alert statistics: {}", e.getMessage());
                    context.append("Données d'alertes temporairement indisponibles.\n");
                }
                context.append("\n");
            }
            
            // Fetch intervention statistics
            if (containsKeyword(question, "intervention")) {
                try {
                    var stats = interventionService.getStatistics();
                    context.append("Statistiques d'interventions système :\n");
                    context.append("- Total interventions : ").append(stats.getTotalInterventions()).append("\n");
                    context.append("- En cours : ").append(stats.getEnCours()).append("\n");
                    context.append("- Terminées : ").append(stats.getTerminees()).append("\n");
                } catch (Exception e) {
                    log.warn("Error fetching intervention statistics: {}", e.getMessage());
                    context.append("Données d'interventions temporairement indisponibles.\n");
                }
                context.append("\n");
            }
            
        } catch (Exception e) {
            log.error("Error building supervisor context: {}", e.getMessage());
            context.append("Contexte superviseur indisponible.\n");
        }
        
        return context.toString();
    }
    
    /**
     * Build context for AGENT role (assigned resources only)
     * AGENT can see:
     * 1. Only their assigned interventions via InterventionService.getByAgent()
     * 2. Measurements for their assigned transformers
     * 3. Related alerts for context
     * 
     * @param question User question for context relevance
     * @return Agent-specific context data
     */
    private String buildAgentContext(String question) {
        StringBuilder context = new StringBuilder();
        
        try {
            // Get current agent ID from authentication
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || auth.getName() == null) {
                context.append("Contexte agent indisponible (authentification manquante).\n");
                return context.toString();
            }
            
            String agentEmail = auth.getName();
            var agentUser = userRepository.findByEmail(agentEmail);
            
            if (agentUser.isEmpty()) {
                log.warn("Agent user not found: {}", agentEmail);
                context.append("Contexte agent indisponible.\n");
                return context.toString();
            }
            
            Long agentId = agentUser.get().getId();
            context.append("=== CONTEXTE AGENT ===\n");
            context.append("Accès limité aux interventions assignées.\n\n");
            
            // Fetch agent-assigned interventions
            if (containsKeyword(question, "intervention")) {
                try {
                    // Use try-catch to handle ResourceNotFoundException gracefully
                    var interventions = interventionService.getByAgent(agentId, Pageable.unpaged());
                    if (interventions.hasContent()) {
                        context.append("Interventions assignées (").append(interventions.getTotalElements()).append(") :\n");
                        interventions.getContent().stream()
                                .limit(5)  // Limit to 5 most recent for context size
                                .forEach(intervention -> {
                                    context.append("- ID: ").append(intervention.getId())
                                            .append(", Statut: ").append(intervention.getStatut())
                                            .append(", Priorité: ").append(intervention.getPriorite())
                                            .append(", Transformateur: ").append(intervention.getTransformateurNom())
                                            .append("\n");
                                });
                        if (interventions.getTotalElements() > 5) {
                            context.append("... et ").append(interventions.getTotalElements() - 5).append(" autres.\n");
                        }
                    } else {
                        context.append("Aucune intervention assignée.\n");
                    }
                } catch (Exception e) {
                    // Catch ResourceNotFoundException or any other error gracefully
                    log.debug("Agent interventions not available (may be new agent): {}", e.getMessage());
                    context.append("Aucune intervention assignée actuellement.\n");
                }
                context.append("\n");
            }
            
            // For alerts, agents get general context (they don't own alerts but should be aware)
            if (containsKeyword(question, "alerte", "alert")) {
                try {
                    var stats = alertService.getStatistics();
                    context.append("Statistiques d'alertes (contexte général) :\n");
                    context.append("- Total alertes : ").append(stats.getTotalAlerts()).append("\n");
                    context.append("- Alertes critiques : ").append(stats.getCriticalAlerts()).append("\n");
                } catch (Exception e) {
                    log.warn("Error fetching alert statistics: {}", e.getMessage());
                    context.append("Données d'alertes temporairement indisponibles.\n");
                }
                context.append("\n");
            }
            
        } catch (Exception e) {
            log.error("Error building agent context: {}", e.getMessage());
            context.append("Contexte agent indisponible.\n");
        }
        
        return context.toString();
    }
    
    /**
     * Build context for SERVICE_CLIENT role (personal data only)
     * SERVICE_CLIENT can see:
     * 1. Only their own complaints via ComplaintService
     * 2. No access to other users' data
     * 3. No statistics - only personal information
     * 
     * @param question User question for context relevance
     * @return Customer personal context data
     */
    private String buildServiceClientContext(String question) {
        StringBuilder context = new StringBuilder();
        
        try {
            // Get current service client ID from authentication
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || auth.getName() == null) {
                context.append("Contexte client indisponible (authentification manquante).\n");
                return context.toString();
            }
            
            String clientEmail = auth.getName();
            var clientUser = userRepository.findByEmail(clientEmail);
            
            if (clientUser.isEmpty()) {
                log.warn("Service client user not found: {}", clientEmail);
                context.append("Contexte client indisponible.\n");
                return context.toString();
            }
            
            Long clientId = clientUser.get().getId();
            context.append("=== CONTEXTE CLIENT SERVICE ===\n");
            context.append("Accès limité à vos données personnelles.\n\n");
            
            // Fetch only client's own complaints by client name
            try {
                // Search complaints by the authenticated client's name (from their user email or profile)
                // Since complaints are identified by nomClient, we use the user's email or username as identifier
                var clientName = clientUser.get().getUsername() != null ? clientUser.get().getUsername() : clientUser.get().getEmail();
                var complaints = complaintService.searchByClient(clientName, Pageable.unpaged());
                
                if (complaints.hasContent()) {
                    context.append("Vos réclamations (").append(complaints.getTotalElements()).append(") :\n");
                    complaints.getContent().stream()
                            .limit(5)  // Limit to 5 most recent for context size
                            .forEach(complaint -> {
                                context.append("- ID: ").append(complaint.getId())
                                        .append(", Statut: ").append(complaint.getStatut())
                                        .append(", Catégorie: ").append(complaint.getCategorie())
                                        .append(", Priorité: ").append(complaint.getPriorite())
                                        .append("\n");
                            });
                    if (complaints.getTotalElements() > 5) {
                        context.append("... et ").append(complaints.getTotalElements() - 5).append(" autres.\n");
                    }
                } else {
                    context.append("Vous n'avez aucune réclamation enregistrée.\n");
                }
            } catch (Exception e) {
                log.debug("Error fetching client complaints: {}", e.getMessage());
                context.append("Vos données de réclamations sont temporairement indisponibles.\n");
            }
            context.append("\n");
            
            // SERVICE_CLIENT should not see statistics or system-wide data
            context.append("Note: Accès limité aux données personnelles uniquement. ");
            context.append("Pour toute assistance supplémentaire, veuillez contacter le service client.\n");
            
        } catch (Exception e) {
            log.error("Error building service client context: {}", e.getMessage());
            context.append("Contexte client indisponible.\n");
        }
        
        return context.toString();
    }
    
    /**
     * Build complete prompt to send to Ollama
     * Combines system prompt, context, and user question
     * Orchestrates the full chain for LLM processing
     * 
     * @param userQuestion Original user question
     * @param context Filtered context data
     * @return Complete prompt for LLM
     */
    private String buildFullPrompt(String userQuestion, String context) {
        // Ensure proper spacing and structure for LLM parsing
        String prompt = SYSTEM_PROMPT + "\n\n"
                + "=== CONTEXTE DATA ===\n"
                + (context.isEmpty() ? "Aucun contexte disponible." : context) + "\n\n"
                + "=== QUESTION DE L'UTILISATEUR ===\n"
                + userQuestion + "\n\n"
                + "=== INSTRUCTIONS FINALES ===\n"
                + "Basez votre réponse EXCLUSIVEMENT sur le contexte fourni ci-dessus.\n"
                + "Soyez concis, professionnel et honnête si les données sont insuffisantes.\n\n"
                + "Réponse :";
        
        // Log prompt size for monitoring
        log.debug("Full prompt size: {} characters", prompt.length());
        
        return prompt;
    }
    
    /**
     * Check if question contains relevant keyword
     * Used for context filtering
     * 
     * @param question User question
     * @param keywords Keywords to search
     * @return true if any keyword found (case-insensitive)
     */
    private boolean containsKeyword(String question, String... keywords) {
        String lowerQuestion = question.toLowerCase();
        for (String keyword : keywords) {
            if (lowerQuestion.contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
