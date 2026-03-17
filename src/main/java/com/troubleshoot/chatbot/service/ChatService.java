package com.troubleshoot.chatbot.service;

import com.troubleshoot.chatbot.dto.ChatRequest;
import com.troubleshoot.chatbot.dto.ChatResponse;
import com.troubleshoot.chatbot.model.ChatMessage;
import com.troubleshoot.chatbot.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class ChatService {
    
    private final ChatMessageRepository chatMessageRepository;
    private final AIService aiService;
    
    public ChatService(ChatMessageRepository chatMessageRepository, AIService aiService) {
        this.chatMessageRepository = chatMessageRepository;
        this.aiService = aiService;
    }
    
    public ChatResponse processMessage(ChatRequest request) {
        String sessionId = request.getSessionId() != null ? 
                          request.getSessionId() : 
                          UUID.randomUUID().toString();
        
        // Save user message
        ChatMessage userMessage = new ChatMessage();
        userMessage.setSessionId(sessionId);
        userMessage.setRole("USER");
        userMessage.setContent(request.getMessage());
        chatMessageRepository.save(userMessage);
        
        // Get conversation history
        List<ChatMessage> history = chatMessageRepository.findBySessionIdOrderByTimestampAsc(sessionId);
        
        // Generate AI response
        String aiResponse = aiService.generateResponse(request.getMessage(), history);
        boolean resolved = aiService.isIssueResolved(request.getMessage());
        
        // Save AI response
        ChatMessage assistantMessage = new ChatMessage();
        assistantMessage.setSessionId(sessionId);
        assistantMessage.setRole("ASSISTANT");
        assistantMessage.setContent(aiResponse);
        chatMessageRepository.save(assistantMessage);
        
        return new ChatResponse(aiResponse, sessionId, resolved);
    }
    
    public List<ChatMessage> getConversationHistory(String sessionId) {
        return chatMessageRepository.findBySessionIdOrderByTimestampAsc(sessionId);
    }
}