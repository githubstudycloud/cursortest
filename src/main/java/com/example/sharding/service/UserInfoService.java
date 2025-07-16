package com.example.sharding.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sharding.entity.UserInfo;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 用户信息服务接口
 * 
 * @author example
 */
public interface UserInfoService extends IService<UserInfo> {
    
    /**
     * 保存用户信息（带分库分表）
     */
    boolean saveUser(UserInfo userInfo, String projectId);
    
    /**
     * 根据项目ID查询用户列表
     */
    List<UserInfo> listByProjectId(String projectId);
    
    /**
     * 异步保存用户信息
     */
    CompletableFuture<Boolean> saveUserAsync(UserInfo userInfo, String projectId);
    
    /**
     * 异步查询用户列表
     */
    CompletableFuture<List<UserInfo>> listByProjectIdAsync(String projectId);
}