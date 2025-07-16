package com.example.sharding.service.impl;

import com.example.sharding.context.DatabaseContext;
import com.example.sharding.service.TableAutoCreateService;
import com.example.sharding.strategy.ShardingStrategy;
import com.example.sharding.utils.SqlExecuteUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 表自动创建服务实现
 * 
 * @author example
 */
@Slf4j
@Service
public class TableAutoCreateServiceImpl implements TableAutoCreateService {
    
    @Autowired
    private ShardingStrategy shardingStrategy;
    
    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private SqlExecuteUtils sqlExecuteUtils;
    
    @Value("${sharding.auto-create-table:true}")
    private boolean autoCreateTableEnabled;
    
    /**
     * 表创建缓存，避免重复检查
     */
    private final ConcurrentHashMap<String, Boolean> tableCreatedCache = new ConcurrentHashMap<>();
    
    @Override
    public boolean autoCreateTableIfNotExists(String originalTableName, String projectId) {
        if (!autoCreateTableEnabled) {
            log.debug("表自动创建功能已关闭");
            return true;
        }
        
        // 获取数据源键和表后缀
        String dataSourceKey = shardingStrategy.getDataSourceKey(projectId);
        String actualTableName = shardingStrategy.getActualTableName(originalTableName, projectId);
        
        // 生成缓存键
        String cacheKey = dataSourceKey + ":" + actualTableName;
        
        // 如果已经创建过，直接返回
        if (tableCreatedCache.containsKey(cacheKey)) {
            log.debug("表[{}]已在缓存中，跳过检查", actualTableName);
            return true;
        }
        
        // 检查表是否存在
        if (isTableExists(actualTableName, dataSourceKey)) {
            log.debug("表[{}]已存在", actualTableName);
            tableCreatedCache.put(cacheKey, true);
            return true;
        }
        
        // 创建表
        boolean created = createTable(originalTableName, actualTableName, dataSourceKey);
        if (created) {
            tableCreatedCache.put(cacheKey, true);
            log.info("自动创建表[{}]成功", actualTableName);
        } else {
            log.error("自动创建表[{}]失败", actualTableName);
        }
        
        return created;
    }
    
    @Override
    public boolean isTableExists(String tableName, String dataSourceKey) {
        try {
            // 切换到目标数据源
            DatabaseContext.setDataSourceKey(dataSourceKey);
            
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            String sql = "SELECT COUNT(*) FROM information_schema.tables " +
                        "WHERE table_schema = DATABASE() AND table_name = ?";
            
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, tableName);
            boolean exists = count != null && count > 0;
            
            log.debug("检查表[{}]存在性: {}", tableName, exists);
            return exists;
            
        } catch (Exception e) {
            log.error("检查表[{}]存在性失败", tableName, e);
            return false;
        }
    }
    
    @Override
    public boolean createTable(String originalTableName, String targetTableName, String dataSourceKey) {
        try {
            // 切换到默认数据源获取原始表结构
            DatabaseContext.setDataSourceKey("default");
            String createTableSql = getCreateTableSql(originalTableName);
            
            if (createTableSql == null) {
                log.error("无法获取原始表[{}]的建表语句", originalTableName);
                return false;
            }
            
            // 替换表名
            String targetCreateSql = createTableSql.replace(
                    "CREATE TABLE `" + originalTableName + "`", 
                    "CREATE TABLE `" + targetTableName + "`"
            );
            
            // 切换到目标数据源执行建表语句
            DatabaseContext.setDataSourceKey(dataSourceKey);
            return sqlExecuteUtils.executeDDL(targetCreateSql);
            
        } catch (Exception e) {
            log.error("创建表[{}]失败", targetTableName, e);
            return false;
        }
    }
    
    /**
     * 获取创建表的SQL语句
     */
    private String getCreateTableSql(String tableName) {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            String sql = "SHOW CREATE TABLE " + tableName;
            
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getString(2));
            
        } catch (Exception e) {
            log.error("获取表[{}]的建表语句失败", tableName, e);
            return null;
        }
    }
}