@Data
public class DistributedFaultParams extends FaultParams {
    // 服务相关
    private String service;      // 目标服务名
    private String version;      // 服务版本
    private String group;        // 服务分组
    
    // 网络相关
    private String networkInterface;  // 网络接口
    private String targetIp;         // 目标IP
    private Integer port;            // 目标端口
    
    // 消息队列相关
    private String topic;            // 消息主题
    private String cluster;          // 集群信息
    private String consumerGroup;    // 消费者组
} 