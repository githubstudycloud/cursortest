package com.example.sharding.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sharding.entity.Order;
import com.example.sharding.entity.vo.OrderDetailVO;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 订单服务接口
 * 
 * @author example
 */
public interface OrderService extends IService<Order> {

    /**
     * 创建订单（同步）
     */
    boolean createOrder(Order order, String projectId);

    /**
     * 创建订单（异步）
     */
    CompletableFuture<Boolean> createOrderAsync(Order order, String projectId);

    /**
     * 多表联查：查询订单详情（同步）
     */
    List<OrderDetailVO> getOrderDetailWithUser(String projectId, String orderNo);

    /**
     * 多表联查：查询订单详情（异步）
     */
    CompletableFuture<List<OrderDetailVO>> getOrderDetailWithUserAsync(String projectId, String orderNo);

    /**
     * 多表联查：根据用户ID查询订单详情（同步）
     */
    List<OrderDetailVO> getOrderDetailByUserId(String projectId, Long userId);

    /**
     * 多表联查：根据用户ID查询订单详情（异步）
     */
    CompletableFuture<List<OrderDetailVO>> getOrderDetailByUserIdAsync(String projectId, Long userId);

    /**
     * 多表联查：查询指定状态的订单详情（同步）
     */
    List<OrderDetailVO> getOrderDetailByStatus(String projectId, Integer status);

    /**
     * 多表联查：查询指定状态的订单详情（异步）
     */
    CompletableFuture<List<OrderDetailVO>> getOrderDetailByStatusAsync(String projectId, Integer status);

    /**
     * 批量更新订单状态（同步）
     */
    boolean batchUpdateOrderStatus(String projectId, List<Long> orderIds, Integer newStatus);

    /**
     * 批量更新订单状态（异步）
     */
    CompletableFuture<Boolean> batchUpdateOrderStatusAsync(String projectId, List<Long> orderIds, Integer newStatus);

    /**
     * 根据用户ID批量更新订单状态（同步）
     */
    boolean batchUpdateOrderStatusByUserId(String projectId, Long userId, Integer oldStatus, Integer newStatus);

    /**
     * 根据用户ID批量更新订单状态（异步）
     */
    CompletableFuture<Boolean> batchUpdateOrderStatusByUserIdAsync(String projectId, Long userId, Integer oldStatus, Integer newStatus);
}