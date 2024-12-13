package com.chaos.platform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProbeService {
    
    private final ChaosBladeCli chaosBladeCli;
    private final MetricsCollector metricsCollector;
    
    // 安装探针
    public void installProbe(ProbeInstallRequest request) {
        log.info("开始安装探针到目标: {}", request.getTarget());
        try {
            // 1. 构建探针安装命令
            List<String> command = buildInstallCommand(request);
            
            // 2. 执行安装
            String probeId = chaosBladeCli.execute(command);
            
            // 3. 记录探针状态
            recordProbeStatus(probeId, request.getTarget());
            
            // 4. 验证探针安装
            validateProbeInstallation(probeId);
            
        } catch (Exception e) {
            log.error("探针安装失败", e);
            throw new ProbeInstallException("探针安装失败: " + e.getMessage());
        }
    }
    
    private List<String> buildInstallCommand(ProbeInstallRequest request) {
        List<String> command = new ArrayList<>();
        command.add("prepare");
        
        // 根据目标类型选择探针
        switch (request.getTargetType()) {
            case JAVA_PROCESS:
                buildJavaProbeCommand(command, request);
                break;
            case DOCKER_CONTAINER:
                buildDockerProbeCommand(command, request);
                break;
            case K8S_POD:
                buildK8sProbeCommand(command, request);
                break;
        }
        
        return command;
    }
    
    // Java进程探针安装
    private void buildJavaProbeCommand(List<String> command, ProbeInstallRequest request) {
        command.add("jvm");
        command.add("--process");
        command.add(request.getProcessName());
        if (request.getPort() != null) {
            command.add("--port");
            command.add(String.valueOf(request.getPort()));
        }
    }
    
    // Docker容器探针安装
    private void buildDockerProbeCommand(List<String> command, ProbeInstallRequest request) {
        command.add("docker");
        command.add("--container-id");
        command.add(request.getContainerId());
    }
    
    // K8s Pod探针安装
    private void buildK8sProbeCommand(List<String> command, ProbeInstallRequest request) {
        command.add("k8s");
        command.add("--namespace");
        command.add(request.getNamespace());
        command.add("--pod");
        command.add(request.getPodName());
    }
    
    // 验证探针安装
    private void validateProbeInstallation(String probeId) {
        String status = chaosBladeCli.execute(Arrays.asList("status", "--type", "prepare", "--uid", probeId));
        if (!status.contains("success")) {
            throw new ProbeInstallException("探针安装验证失败");
        }
    }
    
    // 卸载探针
    public void uninstallProbe(String probeId) {
        log.info("开始卸载探针: {}", probeId);
        try {
            List<String> command = Arrays.asList("revoke", "--uid", probeId);
            chaosBladeCli.execute(command);
        } catch (Exception e) {
            log.error("探针卸载失败", e);
            throw new ProbeUninstallException("探针卸载失败: " + e.getMessage());
        }
    }
} 