package com.example.sharding.strategy;

import com.example.sharding.entity.ShardingConfig;
import com.example.sharding.service.ShardingConfigService;
import com.example.sharding.context.TableContext;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 基于项目的分片策略实现
 * 
 * @author example
 */
@Slf4j
@Component
public class ProjectShardingStrategy implements ShardingStrategy {
    
    @Autowired
    private ShardingConfigService shardingConfigService;
    
    @Override
    public String getDataSourceKey(String projectId) {
        if (StrUtil.isBlank(projectId)) {
            log.warn("项目ID为空，使用默认数据源");
            return "default";
        }
        
        ShardingConfig config = shardingConfigService.getConfigByProjectId(projectId);
        if (config == null) {
            log.warn("未找到项目[{}]的分片配置，使用默认数据源", projectId);
            return "default";
        }
        
        String dataSourceKey = "database-" + config.getDatabaseSuffix();
        log.debug("项目[{}]映射到数据源[{}]", projectId, dataSourceKey);
        return dataSourceKey;
    }
    
    @Override
    public String getTableSuffix(String projectId) {
        if (StrUtil.isBlank(projectId)) {
            log.warn("项目ID为空，不使用表后缀");
            return "";
        }
        
        ShardingConfig config = shardingConfigService.getConfigByProjectId(projectId);
        if (config == null) {
            log.warn("未找到项目[{}]的分片配置，不使用表后缀", projectId);
            return "";
        }
        
        String tableSuffix = config.getTableSuffix();
        log.debug("项目[{}]映射到表后缀[{}]", projectId, tableSuffix);
        return tableSuffix;
    }
    
    @Override
    public String getActualTableName(String originalTableName, String projectId) {
        String tableSuffix = getTableSuffix(projectId);
        String actualTableName = TableContext.buildActualTableName(originalTableName, tableSuffix);
        log.debug("表名转换: {} -> {}", originalTableName, actualTableName);
        return actualTableName;
    }
}