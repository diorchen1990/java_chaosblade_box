@Data
public class ProbeInstallRequest {
    private String target;           // 目标标识
    private TargetType targetType;   // 目标类型
    
    // Java进程相关
    private String processName;      // 进程名
    private Integer port;            // 端口号
    
    // Docker相关
    private String containerId;      // 容器ID
    
    // K8s相关
    private String namespace;        // 命名空间
    private String podName;          // Pod名称
    
    public enum TargetType {
        JAVA_PROCESS,
        DOCKER_CONTAINER,
        K8S_POD
    }
} 