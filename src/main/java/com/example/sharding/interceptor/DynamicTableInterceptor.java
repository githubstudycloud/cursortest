package com.example.sharding.interceptor;

import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.example.sharding.context.DatabaseContext;
import com.example.sharding.context.TableContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 动态表名拦截器
 * 在SQL执行前动态替换表名
 * 
 * @author example
 */
@Slf4j
public class DynamicTableInterceptor implements InnerInterceptor {
    
    /**
     * 表名匹配正则（匹配FROM、JOIN、INTO、UPDATE等后面的表名）
     */
    private static final Pattern TABLE_PATTERN = Pattern.compile(
            "(?i)\\b(?:FROM|JOIN|INTO|UPDATE)\\s+([a-zA-Z_][a-zA-Z0-9_]*)",
            Pattern.CASE_INSENSITIVE
    );
    
    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, 
                           RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        processTableName(boundSql);
    }
    
    @Override
    public void beforeUpdate(Executor executor, MappedStatement ms, Object parameter) throws SQLException {
        BoundSql boundSql = ms.getBoundSql(parameter);
        processTableName(boundSql);
    }
    
    /**
     * 处理表名替换
     */
    private void processTableName(BoundSql boundSql) {
        String originalSql = boundSql.getSql();
        String tableSuffix = DatabaseContext.getTableSuffix();
        
        if (tableSuffix == null || tableSuffix.isEmpty()) {
            log.debug("无表后缀，不进行表名替换");
            return;
        }
        
        // 使用正则表达式替换表名
        String newSql = replaceTableNames(originalSql, tableSuffix);
        
        if (!originalSql.equals(newSql)) {
            log.debug("SQL表名替换:\n原始SQL: {}\n新SQL: {}", originalSql, newSql);
            
            // 通过反射修改BoundSql中的SQL
            try {
                java.lang.reflect.Field field = boundSql.getClass().getDeclaredField("sql");
                field.setAccessible(true);
                field.set(boundSql, newSql);
            } catch (Exception e) {
                log.error("修改SQL失败", e);
            }
        }
    }
    
    /**
     * 替换SQL中的表名
     */
    private String replaceTableNames(String sql, String tableSuffix) {
        Matcher matcher = TABLE_PATTERN.matcher(sql);
        StringBuffer sb = new StringBuffer();
        
        while (matcher.find()) {
            String tableName = matcher.group(1);
            String actualTableName;
            
            // 检查是否已经有后缀
            if (tableName.endsWith("_" + tableSuffix)) {
                actualTableName = tableName;
            } else {
                actualTableName = tableName + "_" + tableSuffix;
                // 记录表名映射
                TableContext.setTableName(tableName, actualTableName);
            }
            
            // 替换表名
            String replacement = matcher.group(0).replace(tableName, actualTableName);
            matcher.appendReplacement(sb, replacement);
        }
        matcher.appendTail(sb);
        
        return sb.toString();
    }
}