package com.troubleshoot.chatbot.config;

import com.troubleshoot.chatbot.model.TroubleshootingKnowledge;
import com.troubleshoot.chatbot.repository.TroubleshootingKnowledgeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    private final TroubleshootingKnowledgeRepository knowledgeRepository;
    
    public DataInitializer(TroubleshootingKnowledgeRepository knowledgeRepository) {
        this.knowledgeRepository = knowledgeRepository;
    }
    
    @Override
    public void run(String... args) {
        if (knowledgeRepository.count() == 0) {
            initializeSampleData();
        }
    }
    
    private void initializeSampleData() {
        knowledgeRepository.save(new TroubleshootingKnowledge(
            null, "Network", "Internet not working",
            "1. Check if your router is powered on\n2. Restart your router by unplugging it for 30 seconds\n3. Check if other devices can connect\n4. Reset network settings on your device",
            "internet, connection, wifi, network, offline"
        ));
        
        knowledgeRepository.save(new TroubleshootingKnowledge(
            null, "Application", "Application crashes on startup",
            "1. Clear application cache and data\n2. Uninstall and reinstall the application\n3. Check for system updates\n4. Ensure sufficient storage space is available",
            "crash, freeze, startup, launch, error"
        ));
        
        knowledgeRepository.save(new TroubleshootingKnowledge(
            null, "Performance", "Computer running slow",
            "1. Close unnecessary programs and browser tabs\n2. Check Task Manager for high CPU/memory usage\n3. Run disk cleanup\n4. Check for malware\n5. Restart your computer",
            "slow, lag, performance, freeze, hang"
        ));
        
        knowledgeRepository.save(new TroubleshootingKnowledge(
            null, "Audio", "No sound from speakers",
            "1. Check volume settings and ensure not muted\n2. Verify correct output device is selected\n3. Update audio drivers\n4. Check physical connections\n5. Run audio troubleshooter",
            "sound, audio, speakers, mute, volume"
        ));
    }
}