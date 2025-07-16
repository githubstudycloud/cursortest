package com.example.sharding.strategy;

/**
 * 分片策略接口
 * 
 * @author example
 */
public interface ShardingStrategy {
    
    /**
     * 根据项目ID获取数据源键
     * 
     * @param projectId 项目ID
     * @return 数据源键
     */
    String getDataSourceKey(String projectId);
    
    /**
     * 根据项目ID获取表后缀
     * 
     * @param projectId 项目ID
     * @return 表后缀
     */
    String getTableSuffix(String projectId);
    
    /**
     * 根据原始表名和项目ID获取实际表名
     * 
     * @param originalTableName 原始表名
     * @param projectId 项目ID
     * @return 实际表名
     */
    String getActualTableName(String originalTableName, String projectId);
}