package com.example.sharding.context;

import lombok.extern.slf4j.Slf4j;
import cn.hutool.core.util.StrUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 表名上下文管理
 * 负责动态表名的替换和管理
 * 
 * @author example
 */
@Slf4j
public class TableContext {
    
    /**
     * 当前线程的表名映射
     * key: 原始表名, value: 实际表名
     */
    private static final ThreadLocal<Map<String, String>> TABLE_NAME_HOLDER = new ThreadLocal<>();
    
    /**
     * 设置表名映射
     */
    public static void setTableName(String originalTableName, String actualTableName) {
        log.debug("设置表名映射: {} -> {}", originalTableName, actualTableName);
        Map<String, String> tableMap = getTableMap();
        tableMap.put(originalTableName, actualTableName);
    }
    
    /**
     * 获取实际表名
     */
    public static String getActualTableName(String originalTableName) {
        Map<String, String> tableMap = TABLE_NAME_HOLDER.get();
        if (tableMap == null) {
            return originalTableName;
        }
        return tableMap.getOrDefault(originalTableName, originalTableName);
    }
    
    /**
     * 根据原始表名和后缀生成实际表名
     */
    public static String buildActualTableName(String originalTableName, String tableSuffix) {
        if (StrUtil.isBlank(tableSuffix)) {
            return originalTableName;
        }
        return originalTableName + "_" + tableSuffix;
    }
    
    /**
     * 清空表名映射
     */
    public static void clearTableNames() {
        log.debug("清空表名映射");
        TABLE_NAME_HOLDER.remove();
    }
    
    /**
     * 获取表名映射
     */
    private static Map<String, String> getTableMap() {
        Map<String, String> tableMap = TABLE_NAME_HOLDER.get();
        if (tableMap == null) {
            tableMap = new ConcurrentHashMap<>();
            TABLE_NAME_HOLDER.set(tableMap);
        }
        return tableMap;
    }
    
    /**
     * 获取所有表名映射
     */
    public static Map<String, String> getAllTableMappings() {
        Map<String, String> tableMap = TABLE_NAME_HOLDER.get();
        return tableMap == null ? new ConcurrentHashMap<>() : new ConcurrentHashMap<>(tableMap);
    }
}