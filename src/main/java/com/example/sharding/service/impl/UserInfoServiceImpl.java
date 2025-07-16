package com.example.sharding.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sharding.annotation.ShardingDataSource;
import com.example.sharding.context.DatabaseContext;
import com.example.sharding.entity.UserInfo;
import com.example.sharding.mapper.UserInfoMapper;
import com.example.sharding.service.TableAutoCreateService;
import com.example.sharding.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 用户信息服务实现
 * 
 * @author example
 */
@Slf4j
@Service
@ShardingDataSource
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> 
        implements UserInfoService {
    
    @Autowired
    private TableAutoCreateService tableAutoCreateService;
    
    @Override
    @Transactional
    public boolean saveUser(UserInfo userInfo, String projectId) {
        log.info("保存用户信息 - 项目ID: {}, 用户: {}", projectId, userInfo.getUsername());
        
        // 设置项目ID
        userInfo.setProjectId(projectId);
        
        // 自动创建表（如果不存在）
        tableAutoCreateService.autoCreateTableIfNotExists("user_info", projectId);
        
        return save(userInfo);
    }
    
    @Override
    public List<UserInfo> listByProjectId(String projectId) {
        log.info("查询用户列表 - 项目ID: {}", projectId);
        
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("project_id", projectId);
        
        return list(queryWrapper);
    }
    
    @Override
    @Async("taskExecutor")
    public CompletableFuture<Boolean> saveUserAsync(UserInfo userInfo, String projectId) {
        log.info("异步保存用户信息 - 项目ID: {}, 用户: {}", projectId, userInfo.getUsername());
        
        // 在异步方法中手动设置上下文（异步配置会自动传递，这里是双重保险）
        DatabaseContext.setProjectId(projectId);
        
        boolean result = saveUser(userInfo, projectId);
        return CompletableFuture.completedFuture(result);
    }
    
    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<UserInfo>> listByProjectIdAsync(String projectId) {
        log.info("异步查询用户列表 - 项目ID: {}", projectId);
        
        // 在异步方法中手动设置上下文
        DatabaseContext.setProjectId(projectId);
        
        List<UserInfo> result = listByProjectId(projectId);
        return CompletableFuture.completedFuture(result);
    }
}