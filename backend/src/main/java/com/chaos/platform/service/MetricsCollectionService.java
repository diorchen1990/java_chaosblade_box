package com.chaos.platform.service;

import com.chaos.platform.model.metrics.SystemMetrics;
import com.chaos.platform.monitor.MetricsCollector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MetricsCollectionService {

    private final MetricsCollector metricsCollector;
    private final SystemMetricsProvider systemMetricsProvider;

    @Scheduled(fixedRate = 5000) // 每5秒采集一次
    public void collectMetrics() {
        try {
            for (String target : systemMetricsProvider.getTargets()) {
                SystemMetrics metrics = systemMetricsProvider.collectMetrics(target);
                metricsCollector.recordSystemMetrics(target, metrics);
            }
        } catch (Exception e) {
            log.error("Failed to collect metrics", e);
        }
    }
} 