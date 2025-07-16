package com.example.sharding.annotation;

import java.lang.annotation.*;

/**
 * 分库注解
 * 用于标识需要进行分库操作的方法
 * 
 * @author example
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ShardingDataSource {
    
    /**
     * 数据源键值，支持SpEL表达式
     * 默认从方法参数中获取projectId
     */
    String value() default "";
    
    /**
     * 项目ID参数名称
     * 当value为空时，从方法参数中按此名称获取projectId
     */
    String projectIdParam() default "projectId";
}