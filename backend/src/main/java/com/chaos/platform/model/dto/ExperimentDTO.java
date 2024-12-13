package com.chaos.platform.model.dto;

import lombok.Data;

@Data
public class ExperimentDTO {
    private Long id;
    private String name;
    private String type;
    private String status;
    private String target;
    private String description;
} 