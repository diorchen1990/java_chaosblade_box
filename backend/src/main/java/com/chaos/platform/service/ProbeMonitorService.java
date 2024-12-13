package com.chaos.platform.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProbeMonitorService {
    
    private final ProbeRepository probeRepository;
    private final MetricsCollector metricsCollector;
    
    @Scheduled(fixedRate = 30000) // 每30秒检查一次
    public void checkProbeStatus() {
        List<ProbeRecord> probes = probeRepository.findAll();
        
        for (ProbeRecord probe : probes) {
            try {
                boolean isActive = validateProbeStatus(probe.getProbeId());
                updateProbeStatus(probe, isActive);
                
                // 记录监控指标
                metricsCollector.recordProbeStatus(
                    probe.getTarget(),
                    isActive ? "active" : "inactive"
                );
            } catch (Exception e) {
                log.error("探针状态检查失败: {}", probe.getProbeId(), e);
            }
        }
    }
} 