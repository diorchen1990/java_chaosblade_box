package com.chaos.platform.fault;

import com.chaos.platform.client.ChaosBladeCli;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JavaMethodFaultExecutor implements FaultExecutor {
    
    private final ChaosBladeCli chaosBladeCli;
    
    @Override
    public void execute(FaultParams params) {
        Map<String, String> chaosParams = new HashMap<>();
        chaosParams.put("target", "jvm");
        chaosParams.put("action", "delay");
        chaosParams.put("class", params.getTarget());
        chaosParams.put("method", params.getMethodName());
        chaosParams.put("time", String.valueOf(params.getDelay()));
        
        chaosBladeCli.runExperiment(params.getExperimentId(), chaosParams);
    }
    
    @Override
    public void stop(String experimentId) {
        chaosBladeCli.stopExperiment(experimentId);
    }
    
    @Override
    public boolean supports(FaultType faultType) {
        return faultType == FaultType.JAVA_METHOD_DELAY || 
               faultType == FaultType.JAVA_METHOD_EXCEPTION;
    }
} 