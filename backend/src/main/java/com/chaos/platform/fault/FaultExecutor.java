package com.chaos.platform.fault;

import com.chaos.platform.model.dto.FaultParams;
import com.chaos.platform.model.enums.FaultType;

public interface FaultExecutor {
    void execute(FaultParams params);
    void stop(String experimentId);
    boolean supports(FaultType faultType);
    String getStatus(String experimentId);
} 