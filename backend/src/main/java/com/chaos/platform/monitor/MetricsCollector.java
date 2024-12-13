package com.chaos.platform.monitor;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class MetricsCollector {
    
    private final MeterRegistry registry;
    
    // 实验执行计数器
    public void recordExperimentExecution(String type, boolean success) {
        Counter.builder("chaos.experiments.total")
            .tag("type", type)
            .tag("result", success ? "success" : "failure")
            .register(registry)
            .increment();
    }
    
    // 实验执行时长
    public void recordExperimentDuration(String type, long durationMs) {
        Timer.builder("chaos.experiments.duration")
            .tag("type", type)
            .register(registry)
            .record(durationMs, TimeUnit.MILLISECONDS);
    }
    
    // 系统资源使用情况
    public void recordSystemMetrics(String target, SystemMetrics metrics) {
        registry.gauge("chaos.system.cpu", 
            metrics.getCpuUsage(),
            value -> value);
            
        registry.gauge("chaos.system.memory", 
            metrics.getMemoryUsage(),
            value -> value);
            
        registry.gauge("chaos.system.disk", 
            metrics.getDiskUsage(),
            value -> value);
    }
    
    // 探针状态监控
    public void recordProbeStatus(String target, String status) {
        registry.gauge("chaos.probe.status",
            1.0,
            value -> "active".equals(status) ? 1.0 : 0.0);
    }
} 