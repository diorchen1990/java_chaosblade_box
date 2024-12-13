package com.chaos.platform.repository;

import com.chaos.platform.model.entity.TestCase;
import com.chaos.platform.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestCaseRepository extends JpaRepository<TestCase, Long> {
    
    @Query("SELECT tc FROM TestCase tc WHERE tc.owner = :owner OR tc.shared = true")
    List<TestCase> findByOwnerOrSharedTrue(User owner);
    
    List<TestCase> findByOwnerOrderByCreatedAtDesc(User owner);
    
    List<TestCase> findByParentId(Long parentId);
} 