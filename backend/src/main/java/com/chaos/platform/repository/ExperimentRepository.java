package com.chaos.platform.repository;

import com.chaos.platform.model.entity.ExperimentRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExperimentRepository extends JpaRepository<ExperimentRecord, Long> {
    
    Optional<ExperimentRecord> findByExperimentId(String experimentId);
    
    List<ExperimentRecord> findByType(String type);
    
    @Query("SELECT e FROM ExperimentRecord e WHERE e.status = :status")
    List<ExperimentRecord> findByStatus(String status);
    
    @Query("SELECT COUNT(e) FROM ExperimentRecord e WHERE e.type = :type AND e.status = :status")
    long countByTypeAndStatus(String type, String status);
} 