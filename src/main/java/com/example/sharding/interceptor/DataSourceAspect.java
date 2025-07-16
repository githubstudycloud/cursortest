package com.example.sharding.interceptor;

import com.example.sharding.annotation.ShardingDataSource;
import com.example.sharding.context.DatabaseContext;
import com.example.sharding.strategy.ShardingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 数据源切换切面
 * 基于@ShardingDataSource注解自动切换数据源
 * 
 * @author example
 */
@Slf4j
@Aspect
@Component
@Order(1) // 确保在事务切面之前执行
public class DataSourceAspect {
    
    @Autowired
    private ShardingStrategy shardingStrategy;
    
    /**
     * 环绕通知，处理带有@ShardingDataSource注解的方法
     */
    @Around("@annotation(shardingDataSource) || @within(shardingDataSource)")
    public Object around(ProceedingJoinPoint point, ShardingDataSource shardingDataSource) throws Throwable {
        try {
            // 解析项目ID
            String projectId = parseProjectId(point, shardingDataSource);
            
            if (projectId != null && !projectId.isEmpty()) {
                // 设置上下文信息
                DatabaseContext.setProjectId(projectId);
                
                // 获取数据源键和表后缀
                String dataSourceKey = shardingStrategy.getDataSourceKey(projectId);
                String tableSuffix = shardingStrategy.getTableSuffix(projectId);
                
                DatabaseContext.setDataSourceKey(dataSourceKey);
                DatabaseContext.setTableSuffix(tableSuffix);
                
                log.debug("设置分片上下文 - 项目ID: {}, 数据源: {}, 表后缀: {}", 
                         projectId, dataSourceKey, tableSuffix);
            }
            
            // 执行目标方法
            return point.proceed();
            
        } finally {
            // 清理上下文（如果是最外层调用）
            // 注意：这里不直接清理，让业务方法执行完成后再清理
            // DatabaseContext.clear();
        }
    }
    
    /**
     * 解析项目ID
     */
    private String parseProjectId(ProceedingJoinPoint point, ShardingDataSource shardingDataSource) {
        // 如果注解中指定了值，直接返回
        if (shardingDataSource.value() != null && !shardingDataSource.value().isEmpty()) {
            return shardingDataSource.value();
        }
        
        // 从方法参数中获取
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Parameter[] parameters = method.getParameters();
        Object[] args = point.getArgs();
        
        String paramName = shardingDataSource.projectIdParam();
        
        // 查找指定名称的参数
        for (int i = 0; i < parameters.length; i++) {
            if (paramName.equals(parameters[i].getName())) {
                Object value = args[i];
                return value != null ? value.toString() : null;
            }
        }
        
        // 如果没找到指定参数名，查找projectId参数
        for (int i = 0; i < parameters.length; i++) {
            if ("projectId".equals(parameters[i].getName())) {
                Object value = args[i];
                return value != null ? value.toString() : null;
            }
        }
        
        // 从第一个String类型参数中获取（作为兜底策略）
        for (Object arg : args) {
            if (arg instanceof String) {
                return (String) arg;
            }
        }
        
        log.warn("无法从方法参数中获取项目ID: {}", method.getName());
        return null;
    }
}