package com.chaos.platform.fault;

import com.chaos.platform.model.dto.FaultParams;
import com.chaos.platform.model.enums.FaultType;
import com.chaos.platform.service.ChaosBladeCli;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class DistributedFaultExecutor implements FaultExecutor {
    
    private final ChaosBladeCli chaosBladeCli;
    
    @Override
    public void execute(FaultParams params) {
        List<String> command = buildCommand(params);
        String experimentId = chaosBladeCli.execute(command);
        log.info("Started distributed fault experiment: {}", experimentId);
    }
    
    private List<String> buildCommand(FaultParams params) {
        List<String> command = new ArrayList<>();
        command.add("create");
        
        switch (params.getFaultType()) {
            case DIST_SERVICE_DELAY:
                buildDubboDelayCommand(command, params);
                break;
            case DIST_NETWORK_PARTITION:
                buildNetworkPartitionCommand(command, params);
                break;
            case DIST_MESSAGE_DELAY:
                buildKafkaDelayCommand(command, params);
                break;
            case DIST_RPC_EXCEPTION:
                buildRpcExceptionCommand(command, params);
                break;
            case DIST_DUBBO_TIMEOUT:
                buildDubboTimeoutCommand(command, params);
                break;
            default:
                throw new IllegalArgumentException("Unsupported fault type: " + params.getFaultType());
        }
        
        // 添加通用参数
        addCommonParams(command, params);
        return command;
    }
    
    private void buildDubboDelayCommand(List<String> command, FaultParams params) {
        command.add("dubbo");
        command.add("delay");
        command.add("--service"); // 目标服务
        command.add(params.getService());
        command.add("--version"); // 服务版本
        command.add(params.getVersion());
        command.add("--group"); // 服务分组
        command.add(params.getGroup());
        command.add("--time"); // 延迟时间
        command.add(String.valueOf(params.getDelay()));
    }
    
    private void buildNetworkPartitionCommand(List<String> command, FaultParams params) {
        command.add("network");
        command.add("loss");
        command.add("--interface"); // 网络接口
        command.add(params.getNetworkInterface());
        command.add("--destination-ip"); // 目标IP
        command.add(params.getTargetIp());
        command.add("--percent"); // 丢包率
        command.add(String.valueOf(params.getProbability()));
        if (params.getPort() != null) {
            command.add("--destination-port");
            command.add(String.valueOf(params.getPort()));
        }
    }
    
    private void buildKafkaDelayCommand(List<String> command, FaultParams params) {
        command.add("kafka");
        command.add("delay");
        command.add("--topic"); // Kafka主题
        command.add(params.getTopic());
        command.add("--time"); // 延迟时间
        command.add(String.valueOf(params.getDelay()));
        command.add("--cluster"); // 集群信息
        command.add(params.getCluster());
        if (params.getConsumerGroup() != null) {
            command.add("--group");
            command.add(params.getConsumerGroup());
        }
    }
    
    private void buildRpcExceptionCommand(List<String> command, FaultParams params) {
        command.add("rpc");
        command.add("throw");
        command.add("--service"); // 目标服务
        command.add(params.getService());
        command.add("--exception"); // 异常类型
        command.add(params.getException());
    }
    
    private void buildDubboTimeoutCommand(List<String> command, FaultParams params) {
        command.add("dubbo");
        command.add("timeout");
        command.add("--service");
        command.add(params.getService());
        command.add("--time");
        command.add(String.valueOf(params.getTimeout()));
    }
    
    private void addCommonParams(List<String> command, FaultParams params) {
        // 添加故障触发概率
        if (params.getProbability() != null) {
            command.add("--probability");
            command.add(String.valueOf(params.getProbability()));
        }
        
        // 添加故障持续时间
        if (params.getDuration() != null) {
            command.add("--timeout");
            command.add(String.valueOf(params.getDuration()));
        }
        
        // 添加自定义标签
        if (params.getLabels() != null && !params.getLabels().isEmpty()) {
            command.add("--labels");
            String labels = params.getLabels().entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining(","));
            command.add(labels);
        }
    }
    
    @Override
    public void stop(String experimentId) {
        List<String> command = Arrays.asList("destroy", experimentId);
        chaosBladeCli.execute(command);
        log.info("Stopped distributed fault experiment: {}", experimentId);
    }
    
    @Override
    public String getStatus(String experimentId) {
        List<String> command = Arrays.asList("status", experimentId);
        return chaosBladeCli.execute(command);
    }
    
    @Override
    public boolean supports(FaultType faultType) {
        return faultType.getCode().startsWith("dist-");
    }
} 