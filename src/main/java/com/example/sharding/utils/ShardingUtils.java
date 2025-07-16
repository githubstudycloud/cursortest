package com.example.sharding.utils;

import com.example.sharding.context.DatabaseContext;
import com.example.sharding.strategy.ShardingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 分库分表工具类
 * 
 * @author example
 */
@Slf4j
@Component
public class ShardingUtils {
    
    @Autowired
    private ShardingStrategy shardingStrategy;
    
    /**
     * 手动设置分片上下文
     * 
     * @param projectId 项目ID
     */
    public void setShardingContext(String projectId) {
        if (projectId == null || projectId.isEmpty()) {
            log.warn("项目ID为空，无法设置分片上下文");
            return;
        }
        
        DatabaseContext.setProjectId(projectId);
        
        String dataSourceKey = shardingStrategy.getDataSourceKey(projectId);
        String tableSuffix = shardingStrategy.getTableSuffix(projectId);
        
        DatabaseContext.setDataSourceKey(dataSourceKey);
        DatabaseContext.setTableSuffix(tableSuffix);
        
        log.debug("手动设置分片上下文 - 项目ID: {}, 数据源: {}, 表后缀: {}", 
                 projectId, dataSourceKey, tableSuffix);
    }
    
    /**
     * 清理分片上下文
     */
    public void clearShardingContext() {
        DatabaseContext.clear();
        log.debug("清理分片上下文");
    }
    
    /**
     * 在指定项目上下文中执行操作
     * 
     * @param projectId 项目ID
     * @param runnable 要执行的操作
     */
    public void executeInContext(String projectId, Runnable runnable) {
        try {
            setShardingContext(projectId);
            runnable.run();
        } finally {
            clearShardingContext();
        }
    }
    
    /**
     * 获取当前上下文信息的字符串表示
     */
    public String getCurrentContextInfo() {
        DatabaseContext.ContextInfo contextInfo = DatabaseContext.getContextInfo();
        return String.format("项目ID: %s, 数据源: %s, 表后缀: %s", 
                            contextInfo.getProjectId(), 
                            contextInfo.getDataSourceKey(), 
                            contextInfo.getTableSuffix());
    }
}