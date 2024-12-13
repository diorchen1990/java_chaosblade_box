package com.chaos.platform.service;

import com.chaos.platform.client.ChaosBladeCli;
import com.chaos.platform.model.dto.ExperimentRequest;
import com.chaos.platform.repository.ExperimentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExperimentServiceTest {
    
    @Mock
    private ChaosBladeCli chaosBladeCli;
    
    @Mock
    private ExperimentRepository experimentRepository;
    
    @InjectMocks
    private ExperimentService experimentService;
    
    @Test
    void shouldCreateExperiment() {
        // given
        ExperimentRequest request = new ExperimentRequest();
        request.setName("test");
        
        // when
        experimentService.createExperiment(request);
        
        // then
        verify(experimentRepository).save(any());
        verify(chaosBladeCli).runExperiment(any(), any());
    }
} 