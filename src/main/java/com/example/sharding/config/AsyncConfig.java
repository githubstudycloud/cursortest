package com.example.sharding.config;

import com.example.sharding.context.DatabaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * 异步配置
 * 支持跨线程上下文传递
 * 
 * @author example
 */
@Slf4j
@Configuration
@EnableAsync
public class AsyncConfig {
    
    /**
     * 异步任务执行器
     */
    @Bean("taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(200);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("async-sharding-");
        executor.setRejectedExecutionHandler(new java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy());
        
        // 设置任务装饰器，用于上下文传递
        executor.setTaskDecorator(new ContextCopyingDecorator());
        
        executor.initialize();
        return executor;
    }
    
    /**
     * 上下文复制装饰器
     * 在异步执行时复制当前线程的上下文到异步线程
     */
    public static class ContextCopyingDecorator implements TaskDecorator {
        
        @Override
        public Runnable decorate(Runnable runnable) {
            // 获取当前线程的上下文
            DatabaseContext.ContextInfo contextInfo = DatabaseContext.getContextInfo();
            
            return () -> {
                try {
                    // 在异步线程中设置上下文
                    DatabaseContext.setContextInfo(contextInfo);
                    log.debug("异步线程设置上下文: {}", contextInfo);
                    
                    // 执行原始任务
                    runnable.run();
                } finally {
                    // 清理上下文
                    DatabaseContext.clear();
                    log.debug("异步线程清理上下文");
                }
            };
        }
    }
}