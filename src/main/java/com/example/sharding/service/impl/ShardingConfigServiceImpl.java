package com.example.sharding.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sharding.entity.ShardingConfig;
import com.example.sharding.mapper.ShardingConfigMapper;
import com.example.sharding.service.ShardingConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 分片配置服务实现
 * 
 * @author example
 */
@Slf4j
@Service
public class ShardingConfigServiceImpl extends ServiceImpl<ShardingConfigMapper, ShardingConfig> 
        implements ShardingConfigService {
    
    @Override
    @Cacheable(value = "shardingConfig", key = "#projectId", unless = "#result == null")
    public ShardingConfig getConfigByProjectId(String projectId) {
        log.debug("查询项目[{}]的分片配置", projectId);
        return baseMapper.selectByProjectId(projectId);
    }
    
    @Override
    public boolean createConfig(String projectId, String databaseSuffix, String tableSuffix) {
        ShardingConfig config = new ShardingConfig()
                .setProjectId(projectId)
                .setDatabaseSuffix(databaseSuffix)
                .setTableSuffix(tableSuffix);
        
        boolean result = save(config);
        log.info("创建分片配置 - 项目ID: {}, 数据库后缀: {}, 表后缀: {}, 结果: {}", 
                projectId, databaseSuffix, tableSuffix, result);
        return result;
    }
}