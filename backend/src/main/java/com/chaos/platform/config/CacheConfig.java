@Configuration
@EnableCaching
public class CacheConfig {
    
    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(
            new ConcurrentMapCache("testCases"),
            new ConcurrentMapCache("experiments"),
            new ConcurrentMapCache("configs")
        ));
        return cacheManager;
    }
} 