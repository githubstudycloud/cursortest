package com.example.sharding.context;

import lombok.extern.slf4j.Slf4j;

/**
 * 数据库上下文管理
 * 使用ThreadLocal管理当前线程的分库分表信息
 * 
 * @author example
 */
@Slf4j
public class DatabaseContext {
    
    /**
     * 当前线程的项目ID
     */
    private static final ThreadLocal<String> PROJECT_ID_HOLDER = new ThreadLocal<>();
    
    /**
     * 当前线程的数据源键
     */
    private static final ThreadLocal<String> DATA_SOURCE_KEY_HOLDER = new ThreadLocal<>();
    
    /**
     * 当前线程的表后缀
     */
    private static final ThreadLocal<String> TABLE_SUFFIX_HOLDER = new ThreadLocal<>();
    
    /**
     * 设置项目ID
     */
    public static void setProjectId(String projectId) {
        log.debug("设置项目ID: {}", projectId);
        PROJECT_ID_HOLDER.set(projectId);
    }
    
    /**
     * 获取项目ID
     */
    public static String getProjectId() {
        return PROJECT_ID_HOLDER.get();
    }
    
    /**
     * 设置数据源键
     */
    public static void setDataSourceKey(String dataSourceKey) {
        log.debug("设置数据源键: {}", dataSourceKey);
        DATA_SOURCE_KEY_HOLDER.set(dataSourceKey);
    }
    
    /**
     * 获取数据源键
     */
    public static String getDataSourceKey() {
        return DATA_SOURCE_KEY_HOLDER.get();
    }
    
    /**
     * 设置表后缀
     */
    public static void setTableSuffix(String tableSuffix) {
        log.debug("设置表后缀: {}", tableSuffix);
        TABLE_SUFFIX_HOLDER.set(tableSuffix);
    }
    
    /**
     * 获取表后缀
     */
    public static String getTableSuffix() {
        return TABLE_SUFFIX_HOLDER.get();
    }
    
    /**
     * 清空当前线程的上下文信息
     */
    public static void clear() {
        log.debug("清空上下文信息");
        PROJECT_ID_HOLDER.remove();
        DATA_SOURCE_KEY_HOLDER.remove();
        TABLE_SUFFIX_HOLDER.remove();
    }
    
    /**
     * 获取完整的上下文信息
     */
    public static ContextInfo getContextInfo() {
        return ContextInfo.builder()
                .projectId(getProjectId())
                .dataSourceKey(getDataSourceKey())
                .tableSuffix(getTableSuffix())
                .build();
    }
    
    /**
     * 设置完整的上下文信息
     */
    public static void setContextInfo(ContextInfo contextInfo) {
        if (contextInfo != null) {
            setProjectId(contextInfo.getProjectId());
            setDataSourceKey(contextInfo.getDataSourceKey());
            setTableSuffix(contextInfo.getTableSuffix());
        }
    }
    
    /**
     * 上下文信息实体
     */
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class ContextInfo {
        private String projectId;
        private String dataSourceKey;
        private String tableSuffix;
    }
}