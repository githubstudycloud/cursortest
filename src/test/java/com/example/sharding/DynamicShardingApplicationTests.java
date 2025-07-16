package com.example.sharding;

import com.example.sharding.entity.UserInfo;
import com.example.sharding.service.ShardingConfigService;
import com.example.sharding.service.UserInfoService;
import com.example.sharding.utils.ShardingUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@SpringBootTest
class DynamicShardingApplicationTests {

    @Autowired
    private UserInfoService userInfoService;
    
    @Autowired
    private ShardingConfigService shardingConfigService;
    
    @Autowired
    private ShardingUtils shardingUtils;

    @Test
    void contextLoads() {
        log.info("Spring Boot应用启动测试成功");
    }
    
    @Test
    void testShardingConfig() {
        // 创建分片配置
        boolean result = shardingConfigService.createConfig("test_001", "001", "test");
        log.info("创建分片配置结果: {}", result);
        
        // 查询分片配置
        var config = shardingConfigService.getConfigByProjectId("test_001");
        log.info("查询分片配置: {}", config);
    }
    
    @Test
    void testUserSave() {
        UserInfo user = new UserInfo()
                .setUsername("test_user_001")
                .setRealName("测试用户001")
                .setEmail("test001@example.com")
                .setPhone("13800138001")
                .setAge(25)
                .setGender(1);
        
        boolean result = userInfoService.saveUser(user, "project_001");
        log.info("保存用户结果: {}", result);
    }
    
    @Test
    void testUserQuery() {
        List<UserInfo> users = userInfoService.listByProjectId("project_001");
        log.info("查询用户列表: {}", users);
    }
    
    @Test
    void testAsyncOperations() throws Exception {
        UserInfo user = new UserInfo()
                .setUsername("async_user")
                .setRealName("异步用户")
                .setEmail("async@example.com")
                .setPhone("13800138999")
                .setAge(30)
                .setGender(0);
        
        // 异步保存
        CompletableFuture<Boolean> saveResult = userInfoService.saveUserAsync(user, "project_002");
        
        // 异步查询
        CompletableFuture<List<UserInfo>> queryResult = userInfoService.listByProjectIdAsync("project_002");
        
        // 等待结果
        log.info("异步保存结果: {}", saveResult.get());
        log.info("异步查询结果: {}", queryResult.get());
    }
    
    @Test
    void testShardingUtils() {
        // 手动设置上下文
        shardingUtils.setShardingContext("project_003");
        log.info("当前上下文: {}", shardingUtils.getCurrentContextInfo());
        
        // 在指定上下文中执行操作
        shardingUtils.executeInContext("test_project", () -> {
            log.info("在指定上下文中执行: {}", shardingUtils.getCurrentContextInfo());
        });
        
        // 清理上下文
        shardingUtils.clearShardingContext();
        log.info("清理后上下文: {}", shardingUtils.getCurrentContextInfo());
    }
}