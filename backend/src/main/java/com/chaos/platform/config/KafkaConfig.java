package com.chaos.platform.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConfig {
    
    public static final String EXPERIMENT_EVENTS_TOPIC = "experiment-events";
    public static final String EXPERIMENT_RESULTS_TOPIC = "experiment-results";
    public static final String EXPERIMENT_LOGS_TOPIC = "experiment-logs";
    
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
            ConsumerFactory<String, String> consumerFactory,
            KafkaTemplate<String, String> template) {
        
        ConcurrentKafkaListenerContainerFactory<String, String> factory = 
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        
        // 配置错误处理和重试
        factory.setErrorHandler(new SeekToCurrentErrorHandler(
            new DeadLetterPublishingRecoverer(template),
            new FixedBackOff(1000L, 3L) // 重试3次，间隔1秒
        ));
        
        return factory;
    }
    
    @Bean
    public NewTopic experimentEventsTopic() {
        return new NewTopic(EXPERIMENT_EVENTS_TOPIC, 3, (short) 1);
    }
    
    @Bean
    public NewTopic experimentResultsTopic() {
        return new NewTopic(EXPERIMENT_RESULTS_TOPIC, 3, (short) 1);
    }
    
    @Bean
    public NewTopic experimentLogsTopic() {
        return new NewTopic(EXPERIMENT_LOGS_TOPIC, 3, (short) 1);
    }
} 