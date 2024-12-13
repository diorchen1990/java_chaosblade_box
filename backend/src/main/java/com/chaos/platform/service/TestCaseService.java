package com.chaos.platform.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TestCaseService {

    private final TestCaseRepository testCaseRepository;
    private final UserService userService;
    private final CacheManager cacheManager;

    @Cacheable(value = "testCases", key = "#user.id")
    public List<TestCase> getCurrentUserCases(User user) {
        return testCaseRepository.findByOwnerOrSharedTrue(user);
    }
    
    @CacheEvict(value = "testCases", key = "#result.owner.id")
    @Transactional
    public TestCase createCase(TestCaseDTO dto) {
        User currentUser = userService.getCurrentUser();
        
        TestCase testCase = new TestCase();
        testCase.setName(dto.getName());
        testCase.setDescription(dto.getDescription());
        testCase.setOwner(currentUser);
        testCase.setConfig(dto.getConfig());
        testCase.setCreatedAt(LocalDateTime.now());
        testCase.setUpdatedAt(LocalDateTime.now());
        
        return testCaseRepository.save(testCase);
    }
    
    @CacheEvict(value = "testCases", key = "#result.owner.id")
    @Transactional
    public TestCase updateCase(Long id, TestCaseDTO dto) {
        TestCase testCase = testCaseRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Case not found"));
            
        // 检查权限
        validateOwnership(testCase);
        
        testCase.setName(dto.getName());
        testCase.setDescription(dto.getDescription());
        testCase.setConfig(dto.getConfig());
        testCase.setUpdatedAt(LocalDateTime.now());
        
        return testCaseRepository.save(testCase);
    }
    
    @Transactional
    public void deleteCase(Long id) {
        TestCase testCase = testCaseRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Case not found"));
            
        // 检查权限
        validateOwnership(testCase);
        
        testCaseRepository.delete(testCase);
    }
    
    @Transactional
    public TestCase copyCase(Long id) {
        TestCase source = testCaseRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Case not found"));
            
        User currentUser = userService.getCurrentUser();
        
        TestCase copy = new TestCase();
        copy.setName(source.getName() + " - 副本");
        copy.setDescription(source.getDescription());
        copy.setConfig(source.getConfig());
        copy.setOwner(currentUser);
        copy.setParentId(source.getId());
        copy.setCreatedAt(LocalDateTime.now());
        copy.setUpdatedAt(LocalDateTime.now());
        
        return testCaseRepository.save(copy);
    }
    
    private void validateOwnership(TestCase testCase) {
        User currentUser = userService.getCurrentUser();
        if (!testCase.getOwner().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("No permission to modify this case");
        }
    }
} 