package com.example.sharding.controller;

import com.example.sharding.entity.UserInfo;
import com.example.sharding.service.UserInfoService;
import com.example.sharding.utils.ShardingUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 用户信息控制器
 * 
 * @author example
 */
@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserInfoController {
    
    @Autowired
    private UserInfoService userInfoService;
    
    @Autowired
    private ShardingUtils shardingUtils;
    
    /**
     * 保存用户信息
     */
    @PostMapping("/{projectId}")
    public ResponseEntity<String> saveUser(@PathVariable String projectId, 
                                          @RequestBody UserInfo userInfo) {
        try {
            log.info("保存用户请求 - 项目ID: {}, 用户: {}", projectId, userInfo.getUsername());
            
            boolean result = userInfoService.saveUser(userInfo, projectId);
            
            if (result) {
                return ResponseEntity.ok("用户保存成功");
            } else {
                return ResponseEntity.badRequest().body("用户保存失败");
            }
        } catch (Exception e) {
            log.error("保存用户失败", e);
            return ResponseEntity.internalServerError().body("保存失败: " + e.getMessage());
        }
    }
    
    /**
     * 查询用户列表
     */
    @GetMapping("/{projectId}")
    public ResponseEntity<List<UserInfo>> listUsers(@PathVariable String projectId) {
        try {
            log.info("查询用户列表 - 项目ID: {}", projectId);
            
            List<UserInfo> users = userInfoService.listByProjectId(projectId);
            return ResponseEntity.ok(users);
            
        } catch (Exception e) {
            log.error("查询用户列表失败", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * 异步保存用户信息
     */
    @PostMapping("/{projectId}/async")
    public ResponseEntity<String> saveUserAsync(@PathVariable String projectId, 
                                               @RequestBody UserInfo userInfo) {
        try {
            log.info("异步保存用户请求 - 项目ID: {}, 用户: {}", projectId, userInfo.getUsername());
            
            CompletableFuture<Boolean> future = userInfoService.saveUserAsync(userInfo, projectId);
            
            // 异步处理，立即返回
            future.whenComplete((result, throwable) -> {
                if (throwable != null) {
                    log.error("异步保存用户失败", throwable);
                } else {
                    log.info("异步保存用户结果: {}", result);
                }
            });
            
            return ResponseEntity.ok("异步保存任务已提交");
            
        } catch (Exception e) {
            log.error("提交异步保存任务失败", e);
            return ResponseEntity.internalServerError().body("提交失败: " + e.getMessage());
        }
    }
    
    /**
     * 异步查询用户列表
     */
    @GetMapping("/{projectId}/async")
    public ResponseEntity<CompletableFuture<List<UserInfo>>> listUsersAsync(@PathVariable String projectId) {
        try {
            log.info("异步查询用户列表 - 项目ID: {}", projectId);
            
            CompletableFuture<List<UserInfo>> future = userInfoService.listByProjectIdAsync(projectId);
            return ResponseEntity.ok(future);
            
        } catch (Exception e) {
            log.error("异步查询用户列表失败", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * 获取当前上下文信息
     */
    @GetMapping("/context")
    public ResponseEntity<String> getCurrentContext() {
        try {
            String contextInfo = shardingUtils.getCurrentContextInfo();
            return ResponseEntity.ok(contextInfo);
        } catch (Exception e) {
            log.error("获取上下文信息失败", e);
            return ResponseEntity.internalServerError().body("获取失败: " + e.getMessage());
        }
    }
    
    /**
     * 手动设置上下文
     */
    @PostMapping("/context/{projectId}")
    public ResponseEntity<String> setContext(@PathVariable String projectId) {
        try {
            shardingUtils.setShardingContext(projectId);
            String contextInfo = shardingUtils.getCurrentContextInfo();
            return ResponseEntity.ok("上下文设置成功: " + contextInfo);
        } catch (Exception e) {
            log.error("设置上下文失败", e);
            return ResponseEntity.internalServerError().body("设置失败: " + e.getMessage());
        }
    }
    
    /**
     * 清理上下文
     */
    @DeleteMapping("/context")
    public ResponseEntity<String> clearContext() {
        try {
            shardingUtils.clearShardingContext();
            return ResponseEntity.ok("上下文清理成功");
        } catch (Exception e) {
            log.error("清理上下文失败", e);
            return ResponseEntity.internalServerError().body("清理失败: " + e.getMessage());
        }
    }
}