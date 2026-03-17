package com.troubleshoot.chatbot.service;

import com.troubleshoot.chatbot.model.ChatMessage;
import com.troubleshoot.chatbot.model.TroubleshootingKnowledge;
import com.troubleshoot.chatbot.repository.TroubleshootingKnowledgeRepository;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AIService {
    
    private static final Logger log = LoggerFactory.getLogger(AIService.class);
    
    private final TroubleshootingKnowledgeRepository knowledgeRepository;
    private final GeminiService geminiService;
    
    public AIService(TroubleshootingKnowledgeRepository knowledgeRepository, GeminiService geminiService) {
        this.knowledgeRepository = knowledgeRepository;
        this.geminiService = geminiService;
    }
    
    public String generateResponse(String userMessage, List<ChatMessage> conversationHistory) {
        // Extract keywords from user message
        String[] keywords = extractKeywords(userMessage);
        
        // Search knowledge base for relevant context
        List<TroubleshootingKnowledge> relevantSolutions = searchKnowledgeBase(keywords);
        
        // Build context from knowledge base
        String context = buildContextFromKnowledgeBase(relevantSolutions);
        
        // Use Gemini AI to generate intelligent response with context
        log.info("Calling Gemini AI for user message: {}", userMessage);
        String geminiResponse = geminiService.generateResponse(userMessage, context);
        
        return geminiResponse;
    }
    
    private String[] extractKeywords(String message) {
        return message.toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "")
                .split("\\s+");
    }
    
    private List<TroubleshootingKnowledge> searchKnowledgeBase(String[] keywords) {
        return List.of(keywords).stream()
                .flatMap(keyword -> knowledgeRepository.findByKeyword(keyword).stream())
                .distinct()
                .limit(3)
                .collect(Collectors.toList());
    }
    
    private String buildContextFromKnowledgeBase(List<TroubleshootingKnowledge> solutions) {
        if (solutions.isEmpty()) {
            return "";
        }
        
        StringBuilder context = new StringBuilder();
        context.append("Relevant solutions from knowledge base:\n\n");
        
        for (int i = 0; i < solutions.size(); i++) {
            TroubleshootingKnowledge solution = solutions.get(i);
            context.append(String.format("Solution %d - %s:\n", i + 1, solution.getProblem()));
            context.append(solution.getSolution()).append("\n\n");
        }
        
        return context.toString();
    }
    
    public boolean isIssueResolved(String userMessage) {
        String lowerMessage = userMessage.toLowerCase();
        return lowerMessage.contains("solved") || 
               lowerMessage.contains("fixed") || 
               lowerMessage.contains("working now") ||
               lowerMessage.contains("thank");
    }
}