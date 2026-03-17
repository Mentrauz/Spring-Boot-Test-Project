package com.troubleshoot.chatbot.dto;

public class ChatResponse {
    private String response;
    private String sessionId;
    private boolean resolved;
    
    public ChatResponse() {
    }
    
    public ChatResponse(String response, String sessionId, boolean resolved) {
        this.response = response;
        this.sessionId = sessionId;
        this.resolved = resolved;
    }
    
    public String getResponse() {
        return response;
    }
    
    public void setResponse(String response) {
        this.response = response;
    }
    
    public String getSessionId() {
        return sessionId;
    }
    
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    
    public boolean isResolved() {
        return resolved;
    }
    
    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }
}