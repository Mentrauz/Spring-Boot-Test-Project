package com.troubleshoot.chatbot.repository;

import com.troubleshoot.chatbot.model.TroubleshootingKnowledge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TroubleshootingKnowledgeRepository extends JpaRepository<TroubleshootingKnowledge, Long> {
    
    @Query("SELECT tk FROM TroubleshootingKnowledge tk WHERE " +
           "LOWER(tk.problem) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(tk.keywords) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<TroubleshootingKnowledge> findByKeyword(@Param("keyword") String keyword);
}