package com.chaos.platform.service;

import com.chaos.platform.client.ChaosBladeCli;
import com.chaos.platform.model.dto.ExperimentDTO;
import com.chaos.platform.model.dto.ExperimentRequest;
import com.chaos.platform.model.entity.Experiment;
import com.chaos.platform.model.entity.ExperimentRecord;
import com.chaos.platform.repository.ExperimentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExperimentService {
    
    private final ExperimentRepository experimentRepository;
    private final ChaosBladeCli chaosBladeCli;
    
    @Transactional
    public ExperimentRecord createExperiment(ExperimentRequest request) {
        // 1. 创建实验记录
        ExperimentRecord record = new ExperimentRecord();
        record.setName(request.getName());
        record.setType(request.getType());
        record.setParams(request.getParams());
        record.setStatus("PREPARING");
        record.setStartTime(LocalDateTime.now());
        
        // 2. 保存记录
        ExperimentRecord savedRecord = experimentRepository.save(record);
        
        try {
            // 3. 执行 ChaosBlade 命令
            String experimentId = chaosBladeCli.execute(buildCommand(request));
            
            // 4. 更新实验ID
            savedRecord.setExperimentId(experimentId);
            savedRecord.setStatus("RUNNING");
            return experimentRepository.save(savedRecord);
            
        } catch (Exception e) {
            // 5. 失败处理
            savedRecord.setStatus("FAILED");
            savedRecord.setResult("Error: " + e.getMessage());
            experimentRepository.save(savedRecord);
            throw e;
        }
    }
    
    @Transactional
    public void completeExperiment(String experimentId, String result) {
        experimentRepository.findByExperimentId(experimentId)
            .ifPresent(record -> {
                record.setStatus("COMPLETED");
                record.setResult(result);
                record.setEndTime(LocalDateTime.now());
                experimentRepository.save(record);
            });
    }
    
    public ExperimentRecord getExperiment(String experimentId) {
        return experimentRepository.findByExperimentId(experimentId)
            .orElseThrow(() -> new NotFoundException("Experiment not found"));
    }
    
    public List<ExperimentRecord> getRunningExperiments() {
        return experimentRepository.findByStatus("RUNNING");
    }
    
    public Map<String, Long> getExperimentStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", experimentRepository.count());
        stats.put("running", experimentRepository.countByStatus("RUNNING"));
        stats.put("completed", experimentRepository.countByStatus("COMPLETED"));
        stats.put("failed", experimentRepository.countByStatus("FAILED"));
        return stats;
    }
} 