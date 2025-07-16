package com.example.sharding.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.example.sharding.context.DatabaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 动态数据源配置
 * 
 * @author example
 */
@Slf4j
@Configuration
public class DataSourceConfig {
    
    /**
     * 默认数据源
     */
    @Bean("defaultDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.default")
    public DataSource defaultDataSource() {
        return new DruidDataSource();
    }
    
    /**
     * 数据库001数据源
     */
    @Bean("database001DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.dynamic.database-001")
    public DataSource database001DataSource() {
        return new DruidDataSource();
    }
    
    /**
     * 数据库002数据源
     */
    @Bean("database002DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.dynamic.database-002")
    public DataSource database002DataSource() {
        return new DruidDataSource();
    }
    
    /**
     * 数据库003数据源
     */
    @Bean("database003DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.dynamic.database-003")
    public DataSource database003DataSource() {
        return new DruidDataSource();
    }
    
    /**
     * 动态数据源路由
     */
    @Bean
    @Primary
    public DataSource dynamicDataSource() {
        DynamicRoutingDataSource routingDataSource = new DynamicRoutingDataSource();
        
        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put("default", defaultDataSource());
        dataSourceMap.put("database-001", database001DataSource());
        dataSourceMap.put("database-002", database002DataSource());
        dataSourceMap.put("database-003", database003DataSource());
        
        // 设置数据源映射
        routingDataSource.setTargetDataSources(dataSourceMap);
        // 设置默认数据源
        routingDataSource.setDefaultTargetDataSource(defaultDataSource());
        
        return routingDataSource;
    }
    
    /**
     * 动态路由数据源
     */
    public static class DynamicRoutingDataSource extends AbstractRoutingDataSource {
        
        @Override
        protected Object determineCurrentLookupKey() {
            String dataSourceKey = DatabaseContext.getDataSourceKey();
            log.debug("当前数据源键: {}", dataSourceKey);
            return dataSourceKey != null ? dataSourceKey : "default";
        }
    }
}