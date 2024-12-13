package com.chaos.platform.validator;

import com.chaos.platform.model.dto.FaultParams;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FaultParamsValidator {
    
    public void validate(FaultParams params) {
        switch (params.getFaultType()) {
            case DIST_SERVICE_DELAY:
                validateServiceDelay(params);
                break;
            case DIST_NETWORK_PARTITION:
                validateNetworkPartition(params);
                break;
            case DIST_MESSAGE_DELAY:
                validateMessageDelay(params);
                break;
            // ... 其他故障类型的验证
        }
        validateCommonParams(params);
    }
    
    private void validateServiceDelay(FaultParams params) {
        if (StringUtils.isEmpty(params.getService())) {
            throw new IllegalArgumentException("Service name is required");
        }
        if (params.getDelay() == null || params.getDelay() < 0) {
            throw new IllegalArgumentException("Invalid delay value");
        }
    }
    
    private void validateNetworkPartition(FaultParams params) {
        if (StringUtils.isEmpty(params.getNetworkInterface())) {
            throw new IllegalArgumentException("Network interface is required");
        }
        if (StringUtils.isEmpty(params.getTargetIp())) {
            throw new IllegalArgumentException("Target IP is required");
        }
    }
    
    private void validateMessageDelay(FaultParams params) {
        if (StringUtils.isEmpty(params.getTopic())) {
            throw new IllegalArgumentException("Topic is required");
        }
        if (params.getDelay() == null || params.getDelay() < 0) {
            throw new IllegalArgumentException("Invalid delay value");
        }
    }
    
    private void validateCommonParams(FaultParams params) {
        if (params.getProbability() != null && 
            (params.getProbability() < 0 || params.getProbability() > 100)) {
            throw new IllegalArgumentException("Probability must be between 0 and 100");
        }
        if (params.getDuration() != null && params.getDuration() < 0) {
            throw new IllegalArgumentException("Duration must be positive");
        }
    }
} 