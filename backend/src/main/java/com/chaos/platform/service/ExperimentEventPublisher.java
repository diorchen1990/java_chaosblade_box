package com.chaos.platform.service;

import com.chaos.platform.model.event.ExperimentEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.chaos.platform.config.KafkaConfig.EXPERIMENT_EVENTS_TOPIC;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExperimentEventPublisher {
    
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    
    public void publishEvent(ExperimentEvent event) {
        try {
            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(EXPERIMENT_EVENTS_TOPIC, event.getExperimentId(), message)
                .addCallback(
                    result -> log.debug("Published event: {}", event),
                    ex -> log.error("Failed to publish event: {}", event, ex)
                );
        } catch (Exception e) {
            log.error("Error serializing event: {}", event, e);
        }
    }
} 