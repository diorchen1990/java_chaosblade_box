package com.chaos.platform.model.event;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ExperimentEvent {
    private String experimentId;
    private EventType type;
    private String message;
    private LocalDateTime timestamp;
    private Object payload;
    
    public enum EventType {
        STARTED,
        COMPLETED,
        FAILED
    }
} 