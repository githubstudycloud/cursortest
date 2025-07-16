package com.example.sharding.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sharding.entity.ShardingConfig;

/**
 * 分片配置服务接口
 * 
 * @author example
 */
public interface ShardingConfigService extends IService<ShardingConfig> {
    
    /**
     * 根据项目ID获取配置
     */
    ShardingConfig getConfigByProjectId(String projectId);
    
    /**
     * 创建分片配置
     */
    boolean createConfig(String projectId, String databaseSuffix, String tableSuffix);
}