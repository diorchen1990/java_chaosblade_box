package com.chaos.platform.fault;

import com.chaos.platform.client.ChaosBladeCli;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DatabaseFaultExecutor implements FaultExecutor {
    
    private final ChaosBladeCli chaosBladeCli;
    
    @Override
    public void execute(FaultParams params) {
        Map<String, String> chaosParams = new HashMap<>();
        chaosParams.put("target", "mysql");
        
        switch (params.getFaultType()) {
            case DB_SLOW_QUERY:
                chaosParams.put("action", "delay");
                chaosParams.put("time", String.valueOf(params.getDelay()));
                break;
            case DB_CONNECTION_TIMEOUT:
                chaosParams.put("action", "timeout");
                chaosParams.put("time", String.valueOf(params.getTimeout()));
                break;
            // ... 其他数据库故障类型
        }
        
        chaosBladeCli.runExperiment(params.getExperimentId(), chaosParams);
    }
    
    @Override
    public void stop(String experimentId) {
        chaosBladeCli.stopExperiment(experimentId);
    }
    
    @Override
    public boolean supports(FaultType faultType) {
        return faultType.getCode().startsWith("db-");
    }
} 