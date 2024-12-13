package com.chaos.platform.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.chaos.platform.entity.SystemConfigRepository;
import com.chaos.platform.dto.DatabaseConfigDTO;
import com.chaos.platform.exception.InvalidDatabaseConfigException;

import com.zaxxer.hikari.HikariDataSource;

@Service
@Slf4j
@RequiredArgsConstructor
public class DatabaseConfigService {

    private final DataSource dataSource;
    private final SystemConfigRepository configRepository;
    
    @Transactional
    public void updateDatabaseConfig(DatabaseConfigDTO config) {
        // 1. 验证新的数据库连接
        validateDatabaseConnection(config);
        
        // 2. 更新配置到数据库
        updateConfig("spring.datasource.host", config.getHost());
        updateConfig("spring.datasource.port", config.getPort());
        updateConfig("spring.datasource.database", config.getDatabase());
        updateConfig("spring.datasource.username", config.getUsername());
        if (StringUtils.hasText(config.getPassword())) {
            updateConfig("spring.datasource.password", config.getPassword());
        }
        updateConfig("spring.datasource.params", config.getParams());
        
        // 3. 重新初始化数据源
        refreshDataSource(config);
    }
    
    private void validateDatabaseConnection(DatabaseConfigDTO config) {
        try (Connection conn = DriverManager.getConnection(
                buildJdbcUrl(config), 
                config.getUsername(), 
                config.getPassword())) {
            log.info("Database connection test successful");
        } catch (SQLException e) {
            log.error("Database connection test failed", e);
            throw new InvalidDatabaseConfigException("数据库连接测试失败: " + e.getMessage());
        }
    }
    
    private String buildJdbcUrl(DatabaseConfigDTO config) {
        return String.format("jdbc:mysql://%s:%s/%s?%s",
            config.getHost(),
            config.getPort(),
            config.getDatabase(),
            config.getParams()
        );
    }
    
    private void refreshDataSource(DatabaseConfigDTO config) {
        if (dataSource instanceof HikariDataSource) {
            HikariDataSource hikariDS = (HikariDataSource) dataSource;
            hikariDS.setJdbcUrl(buildJdbcUrl(config));
            hikariDS.setUsername(config.getUsername());
            if (StringUtils.hasText(config.getPassword())) {
                hikariDS.setPassword(config.getPassword());
            }
            // 强制重新初始化连接池
            hikariDS.getHikariPoolMXBean().softEvictConnections();
        }
    }
} 