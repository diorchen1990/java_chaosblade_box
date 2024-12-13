package com.chaos.platform.model.dto;

import lombok.Data;

@Data
public class ExperimentParams {
    private String className;
    private String methodName;
    private Long delay;
    private String exception;
    private String datasource;
    private Integer timeout;
    private Integer duration;
    private Double probability;
} 