package com.example.sharding.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sharding.annotation.ShardingDataSource;
import com.example.sharding.context.DatabaseContext;
import com.example.sharding.entity.Order;
import com.example.sharding.entity.vo.OrderDetailVO;
import com.example.sharding.mapper.OrderMapper;
import com.example.sharding.service.OrderService;
import com.example.sharding.service.TableAutoCreateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 订单服务实现
 * 
 * @author example
 */
@Slf4j
@Service
@ShardingDataSource
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private TableAutoCreateService tableAutoCreateService;

    @Override
    @Transactional
    public boolean createOrder(Order order, String projectId) {
        log.info("创建订单 - 项目ID: {}, 订单号: {}", projectId, order.getOrderNo());
        
        // 设置项目ID
        order.setProjectId(projectId);
        
        // 自动创建表（如果不存在）
        tableAutoCreateService.autoCreateTableIfNotExists("order_info", projectId);
        
        return save(order);
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<Boolean> createOrderAsync(Order order, String projectId) {
        log.info("异步创建订单 - 项目ID: {}, 订单号: {}", projectId, order.getOrderNo());
        
        // 在异步方法中手动设置上下文
        DatabaseContext.setProjectId(projectId);
        
        boolean result = createOrder(order, projectId);
        return CompletableFuture.completedFuture(result);
    }

    @Override
    public List<OrderDetailVO> getOrderDetailWithUser(String projectId, String orderNo) {
        log.info("多表联查订单详情 - 项目ID: {}, 订单号: {}", projectId, orderNo);
        return baseMapper.selectOrderDetailWithUser(projectId, orderNo);
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<OrderDetailVO>> getOrderDetailWithUserAsync(String projectId, String orderNo) {
        log.info("异步多表联查订单详情 - 项目ID: {}, 订单号: {}", projectId, orderNo);
        
        // 在异步方法中手动设置上下文
        DatabaseContext.setProjectId(projectId);
        
        List<OrderDetailVO> result = getOrderDetailWithUser(projectId, orderNo);
        return CompletableFuture.completedFuture(result);
    }

    @Override
    public List<OrderDetailVO> getOrderDetailByUserId(String projectId, Long userId) {
        log.info("根据用户ID查询订单详情 - 项目ID: {}, 用户ID: {}", projectId, userId);
        return baseMapper.selectOrderDetailByUserId(projectId, userId);
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<OrderDetailVO>> getOrderDetailByUserIdAsync(String projectId, Long userId) {
        log.info("异步根据用户ID查询订单详情 - 项目ID: {}, 用户ID: {}", projectId, userId);
        
        // 在异步方法中手动设置上下文
        DatabaseContext.setProjectId(projectId);
        
        List<OrderDetailVO> result = getOrderDetailByUserId(projectId, userId);
        return CompletableFuture.completedFuture(result);
    }

    @Override
    public List<OrderDetailVO> getOrderDetailByStatus(String projectId, Integer status) {
        log.info("根据状态查询订单详情 - 项目ID: {}, 状态: {}", projectId, status);
        return baseMapper.selectOrderDetailByStatus(projectId, status);
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<OrderDetailVO>> getOrderDetailByStatusAsync(String projectId, Integer status) {
        log.info("异步根据状态查询订单详情 - 项目ID: {}, 状态: {}", projectId, status);
        
        // 在异步方法中手动设置上下文
        DatabaseContext.setProjectId(projectId);
        
        List<OrderDetailVO> result = getOrderDetailByStatus(projectId, status);
        return CompletableFuture.completedFuture(result);
    }

    @Override
    @Transactional
    public boolean batchUpdateOrderStatus(String projectId, List<Long> orderIds, Integer newStatus) {
        log.info("批量更新订单状态 - 项目ID: {}, 订单数量: {}, 新状态: {}", projectId, orderIds.size(), newStatus);
        int updateCount = baseMapper.batchUpdateOrderStatus(projectId, orderIds, newStatus);
        return updateCount > 0;
    }

    @Override
    @Async("taskExecutor")
    @Transactional
    public CompletableFuture<Boolean> batchUpdateOrderStatusAsync(String projectId, List<Long> orderIds, Integer newStatus) {
        log.info("异步批量更新订单状态 - 项目ID: {}, 订单数量: {}, 新状态: {}", projectId, orderIds.size(), newStatus);
        
        // 在异步方法中手动设置上下文
        DatabaseContext.setProjectId(projectId);
        
        boolean result = batchUpdateOrderStatus(projectId, orderIds, newStatus);
        return CompletableFuture.completedFuture(result);
    }

    @Override
    @Transactional
    public boolean batchUpdateOrderStatusByUserId(String projectId, Long userId, Integer oldStatus, Integer newStatus) {
        log.info("根据用户ID批量更新订单状态 - 项目ID: {}, 用户ID: {}, 旧状态: {}, 新状态: {}", 
                projectId, userId, oldStatus, newStatus);
        int updateCount = baseMapper.batchUpdateOrderStatusByUserId(projectId, userId, oldStatus, newStatus);
        return updateCount > 0;
    }

    @Override
    @Async("taskExecutor")
    @Transactional
    public CompletableFuture<Boolean> batchUpdateOrderStatusByUserIdAsync(String projectId, Long userId, Integer oldStatus, Integer newStatus) {
        log.info("异步根据用户ID批量更新订单状态 - 项目ID: {}, 用户ID: {}, 旧状态: {}, 新状态: {}", 
                projectId, userId, oldStatus, newStatus);
        
        // 在异步方法中手动设置上下文
        DatabaseContext.setProjectId(projectId);
        
        boolean result = batchUpdateOrderStatusByUserId(projectId, userId, oldStatus, newStatus);
        return CompletableFuture.completedFuture(result);
    }
}