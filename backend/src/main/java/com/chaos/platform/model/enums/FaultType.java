package com.chaos.platform.model.enums;

public enum FaultType {
    // Java应用故障
    JAVA_METHOD_DELAY("java-method-delay", "方法延迟注入"),
    JAVA_METHOD_EXCEPTION("java-method-exception", "异常注入"),
    JAVA_THREADPOOL("java-threadpool", "线程池故障"),
    JAVA_GC("java-gc", "GC压力模拟"),
    JAVA_MEMORY_LEAK("java-memory", "内存泄漏模拟"),
    
    // 数据库故障
    DB_CONNECTION_TIMEOUT("db-connection-timeout", "连接超时"),
    DB_SLOW_QUERY("db-slow-query", "慢查询模拟"),
    DB_CONNECTION_POOL_FULL("db-connection-pool", "连接池满载"),
    DB_SQL_EXCEPTION("db-sql-exception", "SQL执行异常"),
    
    // 分布式故障
    DIST_SERVICE_DELAY("dist-service-delay", "服务调用延迟"),
    DIST_NETWORK_PARTITION("dist-network", "网络分区模拟"),
    DIST_MESSAGE_DELAY("dist-message-delay", "消息延迟投递"),
    DIST_RPC_EXCEPTION("dist-rpc-exception", "RPC调用异常"),
    DIST_DUBBO_TIMEOUT("dist-dubbo-timeout", "Dubbo服务超时"),
    DIST_KAFKA_DELAY("dist-kafka-delay", "Kafka消息延迟");

    private final String code;
    private final String description;

    FaultType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
} 