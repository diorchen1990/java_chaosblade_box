package com.chaos.platform.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExperimentExecutor {
    
    private final ChaosBladeCli chaosBladeCli;
    private final MetricsCollector metricsCollector;
    
    public void executeExperiment(ExperimentRequest request) {
        log.info("开始执行实验: {}", request.getName());
        
        try {
            // 构建 ChaosBlade 命令
            List<String> command = buildCommand(request);
            
            // 执行故障注入
            String experimentId = chaosBladeCli.execute(command);
            
            // 记录实验开始指标
            metricsCollector.recordExperimentStart(request.getType());
            
            // 设置实验自动结束定时器
            scheduleExperimentEnd(experimentId, request.getParams().getDuration());
            
        } catch (Exception e) {
            log.error("实验执行失败", e);
            throw new ExperimentExecutionException("实验执行失败: " + e.getMessage());
        }
    }
    
    private List<String> buildCommand(ExperimentRequest request) {
        List<String> command = new ArrayList<>();
        command.add("blade");
        command.add("create");
        
        switch (request.getType()) {
            case JAVA_METHOD_DELAY:
                buildJavaMethodDelayCommand(command, request.getParams());
                break;
            case JAVA_METHOD_EXCEPTION:
                buildJavaMethodExceptionCommand(command, request.getParams());
                break;
            case DB_TIMEOUT:
                buildDbTimeoutCommand(command, request.getParams());
                break;
            // ... 其他故障类型
        }
        
        return command;
    }
    
    private void buildJavaMethodDelayCommand(List<String> command, ExperimentParams params) {
        command.add("jvm");
        command.add("delay");
        command.add("--class");
        command.add(params.getClassName());
        command.add("--method");
        command.add(params.getMethodName());
        command.add("--time");
        command.add(String.valueOf(params.getDelay()));
    }
    
    private void scheduleExperimentEnd(String experimentId, int duration) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.schedule(() -> {
            try {
                chaosBladeCli.destroy(experimentId);
                metricsCollector.recordExperimentEnd(experimentId);
            } catch (Exception e) {
                log.error("停止实验失败", e);
            }
        }, duration, TimeUnit.SECONDS);
    }
} 