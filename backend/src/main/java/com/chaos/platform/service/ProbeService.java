package com.chaos.platform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProbeService {
    
    @Autowired
    private ChaosBladeCli chaosBladeClient;
    
    public void installProbe(String target) {
        // 检查目标系统是否已安装探针
        if (!isProbeInstalled(target)) {
            // 执行探针安装
            chaosBladeClient.installProbe(target);
        }
    }
    
    public List<ProbeStatus> listProbes() {
        return chaosBladeClient.getProbeList();
    }
} 