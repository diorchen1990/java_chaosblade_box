package com.chaos.platform.client;

import com.chaos.platform.service.ChaosBladeInstallService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChaosBladeCli {
    
    @Value("${chaosblade.install.path:/opt/chaosblade}")
    private String installPath;
    
    private final ChaosBladeInstallService installService;
    
    @PostConstruct
    public void init() {
        // 应用启动时检查并安装 ChaosBlade
        installService.installChaosBlade();
    }
    
    public String execute(List<String> command) {
        try {
            // 构建完整命令
            List<String> fullCommand = new ArrayList<>();
            fullCommand.add(installPath + "/blade");
            fullCommand.addAll(command);
            
            ProcessBuilder pb = new ProcessBuilder(fullCommand);
            pb.redirectErrorStream(true);
            Process p = pb.start();
            
            // 读取命令输出
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(p.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                    log.debug("ChaosBlade output: {}", line);
                }
            }
            
            // 检查执行结果
            int exitCode = p.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("ChaosBlade command failed with exit code: " + exitCode);
            }
            
            // 解析实验ID
            return parseExperimentId(output.toString());
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to execute ChaosBlade command", e);
        }
    }
    
    public void destroy(String experimentId) {
        try {
            List<String> command = Arrays.asList(
                installPath + "/blade",
                "destroy",
                experimentId
            );
            
            ProcessBuilder pb = new ProcessBuilder(command);
            Process p = pb.start();
            
            int exitCode = p.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("Failed to destroy experiment: " + experimentId);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error destroying experiment", e);
        }
    }
    
    private String parseExperimentId(String output) {
        // 从输出中解析实验ID
        Pattern pattern = Pattern.compile("\"uid\":\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(output);
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new RuntimeException("Could not parse experiment ID from output");
    }
} 