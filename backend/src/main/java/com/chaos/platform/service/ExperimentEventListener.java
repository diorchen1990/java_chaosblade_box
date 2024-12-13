package com.chaos.platform.service;

import com.chaos.platform.model.event.ExperimentEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.chaos.platform.config.KafkaConfig.EXPERIMENT_EVENTS_TOPIC;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExperimentEventListener {
    
    private final ObjectMapper objectMapper;
    private final ExperimentService experimentService;
    
    @KafkaListener(topics = EXPERIMENT_EVENTS_TOPIC)
    public void handleExperimentEvent(String message) {
        try {
            ExperimentEvent event = objectMapper.readValue(message, ExperimentEvent.class);
            log.info("Received experiment event: {}", event);
            
            switch (event.getType()) {
                case STARTED:
                    experimentService.handleExperimentStarted(event);
                    break;
                case COMPLETED:
                    experimentService.handleExperimentCompleted(event);
                    break;
                case FAILED:
                    experimentService.handleExperimentFailed(event);
                    break;
                default:
                    log.warn("Unknown event type: {}", event.getType());
            }
        } catch (Exception e) {
            log.error("Error processing event: {}", message, e);
        }
    }
} 