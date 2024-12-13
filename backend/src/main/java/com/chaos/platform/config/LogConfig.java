package com.chaos.platform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class LogConfig {
    
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
    
    @Bean
    public LogCollector logCollector(KafkaTemplate<String, String> kafkaTemplate) {
        return new KafkaLogCollector(kafkaTemplate);
    }
} 