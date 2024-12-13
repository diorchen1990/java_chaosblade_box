package com.chaos.platform.model.metrics;

import lombok.Data;

@Data
public class SystemMetrics {
    private double cpuUsage;      // CPU使用率
    private double memoryUsage;   // 内存使用率
    private double diskUsage;     // 磁盘使用率
    private int threadCount;      // 线程数
    private long gcCount;         // GC次数
    private long heapUsed;        // 堆内存使用
    private int connectionCount;  // 连接数
} 