package com.chaos.platform.service;

import com.chaos.platform.fault.FaultExecutor;
import com.chaos.platform.model.dto.FaultParams;
import com.chaos.platform.model.enums.FaultType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FaultService {
    
    private final List<FaultExecutor> faultExecutors;
    
    public void executeFault(FaultType faultType, FaultParams params) {
        FaultExecutor executor = faultExecutors.stream()
            .filter(e -> e.supports(faultType))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No executor found for fault type: " + faultType));
            
        executor.execute(params);
    }
    
    public void stopFault(FaultType faultType, String experimentId) {
        FaultExecutor executor = faultExecutors.stream()
            .filter(e -> e.supports(faultType))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No executor found for fault type: " + faultType));
            
        executor.stop(experimentId);
    }
} 