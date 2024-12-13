package com.chaos.platform.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ChaosBladeInstallService {
    
    @Value("${chaosblade.install.path:/opt/chaosblade}")
    private String installPath;
    
    @Value("${chaosblade.version:1.7.0}")
    private String version;
    
    @Value("${chaosblade.install.timeout:300}")
    private int installTimeout;
    
    public void installChaosBlade() {
        try {
            if (!isChaosBladeInstalled()) {
                log.info("开始安装 ChaosBlade 版本: {}", version);
                String installScript = downloadInstallScript();
                executeInstallScript(installScript);
                validateInstallation();
            } else {
                validateVersion();
            }
        } catch (Exception e) {
            log.error("ChaosBlade 安装失败", e);
            throw new RuntimeException("ChaosBlade 安装失败: " + e.getMessage());
        }
    }
    
    private boolean isChaosBladeInstalled() {
        Path bladePath = Paths.get(installPath, "blade");
        boolean exists = Files.exists(bladePath);
        boolean executable = Files.isExecutable(bladePath);
        log.debug("ChaosBlade 安装检查 - 存在: {}, 可执行: {}", exists, executable);
        return exists && executable;
    }
    
    private String downloadInstallScript() {
        log.info("开始下载 ChaosBlade 安装脚本");
        try {
            ProcessBuilder pb = new ProcessBuilder(
                "curl",
                "-sS",
                "-m", "30",
                "-L",
                "https://raw.githubusercontent.com/chaosblade-io/chaosblade/master/install.sh"
            );
            pb.redirectErrorStream(true);
            Process p = pb.start();
            
            StringBuilder script = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(p.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    script.append(line).append("\n");
                }
            }
            
            if (!p.waitFor(30, TimeUnit.SECONDS)) {
                p.destroyForcibly();
                throw new RuntimeException("下载超时");
            }
            
            if (p.exitValue() != 0) {
                throw new RuntimeException("下载失败，退出码: " + p.exitValue());
            }
            
            return script.toString();
        } catch (Exception e) {
            throw new RuntimeException("下载安装脚本失败", e);
        }
    }
    
    private void executeInstallScript(String script) {
        log.info("开始执行 ChaosBlade 安装脚本");
        try {
            Path scriptPath = Files.createTempFile("install-chaosblade", ".sh");
            Files.write(scriptPath, script.getBytes());
            scriptPath.toFile().setExecutable(true);
            
            ProcessBuilder pb = new ProcessBuilder("/bin/bash", scriptPath.toString());
            pb.redirectErrorStream(true);
            pb.environment().put("CHAOSBLADE_VERSION", version);
            pb.environment().put("CHAOSBLADE_HOME", installPath);
            
            Process p = pb.start();
            
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(p.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.info("安装输出: {}", line);
                }
            }
            
            if (!p.waitFor(installTimeout, TimeUnit.SECONDS)) {
                p.destroyForcibly();
                throw new RuntimeException("安装超时");
            }
            
            if (p.exitValue() != 0) {
                throw new RuntimeException("安装失败，退出码: " + p.exitValue());
            }
            
            Files.delete(scriptPath);
            log.info("ChaosBlade 安装完成");
            
        } catch (Exception e) {
            throw new RuntimeException("执行安装脚本失败", e);
        }
    }
    
    private void validateVersion() {
        try {
            ProcessBuilder pb = new ProcessBuilder(installPath + "/blade", "version");
            Process p = pb.start();
            
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(p.getInputStream()))) {
                String installedVersion = reader.readLine();
                if (!StringUtils.hasText(installedVersion)) {
                    throw new RuntimeException("无法获取 ChaosBlade 版本信息");
                }
                if (!installedVersion.contains(version)) {
                    log.warn("ChaosBlade 版本不匹配. 期望: {}, 实际: {}", 
                        version, installedVersion);
                }
                log.info("ChaosBlade 版本验证成功: {}", installedVersion);
            }
        } catch (Exception e) {
            throw new RuntimeException("验证 ChaosBlade 版本失败", e);
        }
    }
} 