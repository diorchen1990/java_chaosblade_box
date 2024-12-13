package com.chaos.platform.repository;

import com.chaos.platform.model.entity.SystemConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SystemConfigRepository extends JpaRepository<SystemConfig, String> {
    List<SystemConfig> findByCategory(String category);
    
    @Query("SELECT s FROM SystemConfig s WHERE s.editable = true")
    List<SystemConfig> findEditableConfigs();
} 