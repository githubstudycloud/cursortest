package com.example.sharding.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * SQL执行工具类
 * 
 * @author example
 */
@Slf4j
@Component
public class SqlExecuteUtils {
    
    @Autowired
    private DataSource dataSource;
    
    /**
     * 执行DDL语句
     * 
     * @param sql DDL语句
     * @return 是否执行成功
     */
    public boolean executeDDL(String sql) {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            jdbcTemplate.execute(sql);
            log.info("执行DDL成功: {}", sql);
            return true;
        } catch (Exception e) {
            log.error("执行DDL失败: {}", sql, e);
            return false;
        }
    }
    
    /**
     * 执行DML语句
     * 
     * @param sql DML语句
     * @param params 参数
     * @return 影响的行数
     */
    public int executeDML(String sql, Object... params) {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            int rows = jdbcTemplate.update(sql, params);
            log.debug("执行DML成功: {}, 影响行数: {}", sql, rows);
            return rows;
        } catch (Exception e) {
            log.error("执行DML失败: {}", sql, e);
            return -1;
        }
    }
}