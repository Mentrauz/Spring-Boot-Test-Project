package com.troubleshoot.chatbot.model;

import jakarta.persistence.*;

@Entity
@Table(name = "troubleshooting_knowledge")
public class TroubleshootingKnowledge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String category;
    
    @Column(nullable = false)
    private String problem;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String solution;
    
    @Column(columnDefinition = "TEXT")
    private String keywords;
    
    public TroubleshootingKnowledge() {
    }
    
    public TroubleshootingKnowledge(Long id, String category, String problem, String solution, String keywords) {
        this.id = id;
        this.category = category;
        this.problem = problem;
        this.solution = solution;
        this.keywords = keywords;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getProblem() {
        return problem;
    }
    
    public void setProblem(String problem) {
        this.problem = problem;
    }
    
    public String getSolution() {
        return solution;
    }
    
    public void setSolution(String solution) {
        this.solution = solution;
    }
    
    public String getKeywords() {
        return keywords;
    }
    
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
}