package com.chaos.platform.model.dto;

import lombok.Data;

@Data
public class ExperimentRequest {
    private String name;
    private String type;
    private ExperimentParams params;
} 