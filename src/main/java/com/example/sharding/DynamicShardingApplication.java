package com.example.sharding;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 动态分库分表系统启动类
 * 
 * @author example
 */
@Slf4j
@SpringBootApplication
@EnableCaching
@EnableTransactionManagement
@MapperScan("com.example.sharding.mapper")
public class DynamicShardingApplication {

    public static void main(String[] args) {
        log.info("启动动态分库分表系统...");
        SpringApplication.run(DynamicShardingApplication.class, args);
        log.info("动态分库分表系统启动完成!");
    }
}