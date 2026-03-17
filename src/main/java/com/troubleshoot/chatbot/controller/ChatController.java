package com.troubleshoot.chatbot.controller;

import com.troubleshoot.chatbot.dto.ChatRequest;
import com.troubleshoot.chatbot.dto.ChatResponse;
import com.troubleshoot.chatbot.model.ChatMessage;
import com.troubleshoot.chatbot.service.ChatService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {
    
    private final ChatService chatService;
    
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }
    
    @PostMapping("/message")
    public ResponseEntity<ChatResponse> sendMessage(@Valid @RequestBody ChatRequest request) {
        ChatResponse response = chatService.processMessage(request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/history/{sessionId}")
    public ResponseEntity<List<ChatMessage>> getHistory(@PathVariable String sessionId) {
        List<ChatMessage> history = chatService.getConversationHistory(sessionId);
        return ResponseEntity.ok(history);
    }
}