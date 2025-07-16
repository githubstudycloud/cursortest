package com.example.sharding.service;

/**
 * 表自动创建服务接口
 * 
 * @author example
 */
public interface TableAutoCreateService {
    
    /**
     * 如果表不存在则自动创建
     * 
     * @param originalTableName 原始表名
     * @param projectId 项目ID
     * @return 是否创建成功或已存在
     */
    boolean autoCreateTableIfNotExists(String originalTableName, String projectId);
    
    /**
     * 检查表是否存在
     * 
     * @param tableName 表名
     * @param dataSourceKey 数据源键
     * @return 是否存在
     */
    boolean isTableExists(String tableName, String dataSourceKey);
    
    /**
     * 创建表
     * 
     * @param originalTableName 原始表名
     * @param targetTableName 目标表名
     * @param dataSourceKey 数据源键
     * @return 是否创建成功
     */
    boolean createTable(String originalTableName, String targetTableName, String dataSourceKey);
}