package com.chaos.platform.service;

import com.chaos.platform.client.ChaosBladeCli;
import com.chaos.platform.model.dto.ExperimentDTO;
import com.chaos.platform.model.dto.ExperimentRequest;
import com.chaos.platform.model.entity.Experiment;
import com.chaos.platform.repository.ExperimentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExperimentService {
    
    private final ChaosBladeCli chaosBladeCli;
    private final ExperimentRepository experimentRepository;
    
    @Transactional
    public ExperimentDTO createExperiment(ExperimentRequest request) {
        Experiment experiment = new Experiment();
        experiment.setName(request.getName());
        experiment.setType(request.getType());
        experiment.setTarget(request.getTarget());
        experiment.setDescription(request.getDescription());
        experiment.setStatus("CREATED");
        experiment.setCreatedAt(LocalDateTime.now());
        experiment.setUpdatedAt(LocalDateTime.now());
        
        experiment = experimentRepository.save(experiment);
        
        // 调用 ChaosBlade 执行实验
        chaosBladeCli.runExperiment(experiment.getId(), request.getParameters());
        
        return convertToDTO(experiment);
    }
    
    public ExperimentStatus getStatus(Long id) {
        Experiment experiment = experimentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Experiment not found"));
        return new ExperimentStatus(experiment.getId(), experiment.getStatus());
    }
    
    @Transactional
    public void stopExperiment(Long id) {
        Experiment experiment = experimentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Experiment not found"));
            
        chaosBladeCli.stopExperiment(id);
        experiment.setStatus("STOPPED");
        experiment.setUpdatedAt(LocalDateTime.now());
        experimentRepository.save(experiment);
    }
    
    private ExperimentDTO convertToDTO(Experiment experiment) {
        ExperimentDTO dto = new ExperimentDTO();
        dto.setId(experiment.getId());
        dto.setName(experiment.getName());
        dto.setType(experiment.getType());
        dto.setStatus(experiment.getStatus());
        dto.setTarget(experiment.getTarget());
        dto.setDescription(experiment.getDescription());
        return dto;
    }
} 