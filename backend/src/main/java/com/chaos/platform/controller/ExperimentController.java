package com.chaos.platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/experiments")
public class ExperimentController {
    
    @Autowired
    private ExperimentService experimentService;
    
    @PostMapping
    public ResponseEntity<ExperimentDTO> createExperiment(@RequestBody ExperimentRequest request) {
        return ResponseEntity.ok(experimentService.createExperiment(request));
    }
    
    @GetMapping("/{id}/status")
    public ResponseEntity<ExperimentStatus> getStatus(@PathVariable Long id) {
        return ResponseEntity.ok(experimentService.getStatus(id));
    }
    
    @PostMapping("/{id}/stop")
    public ResponseEntity<Void> stopExperiment(@PathVariable Long id) {
        experimentService.stopExperiment(id);
        return ResponseEntity.ok().build();
    }
} 