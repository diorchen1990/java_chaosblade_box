package com.chaos.platform.model.dto;

import lombok.Data;
import java.util.Map;

@Data
public class FaultParams {
    // 通用参数
    private String target;      // 目标类/方法/服务
    private Integer timeout;    // 故障持续时间
    private Double rate;        // 故障触发概率
    
    // Java方法故障参数
    private String methodName;  // 方法名
    private String exception;   // 异常类型
    private Long delay;         // 延迟时间(ms)
    
    // 数据库故障参数
    private String sql;         // SQL语句
    private Integer connections; // 连接数
    
    // 分布式故障参数
    private String service;     // 服务名
    private String endpoint;    // 端点
    private Map<String, String> headers; // 请求头
} 