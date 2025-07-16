package com.example.sharding.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sharding.entity.OrderDetail;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 订单详情服务接口
 * 
 * @author example
 */
public interface OrderDetailService extends IService<OrderDetail> {

    /**
     * 批量创建订单详情（同步）
     */
    boolean batchCreateOrderDetails(List<OrderDetail> detailList, String projectId);

    /**
     * 批量创建订单详情（异步）
     */
    CompletableFuture<Boolean> batchCreateOrderDetailsAsync(List<OrderDetail> detailList, String projectId);

    /**
     * 批量更新商品数量（同步）
     */
    boolean batchUpdateQuantity(String projectId, List<Long> detailIds, Integer quantity);

    /**
     * 批量更新商品数量（异步）
     */
    CompletableFuture<Boolean> batchUpdateQuantityAsync(String projectId, List<Long> detailIds, Integer quantity);

    /**
     * 根据订单ID批量更新价格（同步）
     */
    boolean batchUpdatePriceByOrderId(String projectId, Long orderId, BigDecimal discountRate);

    /**
     * 根据订单ID批量更新价格（异步）
     */
    CompletableFuture<Boolean> batchUpdatePriceByOrderIdAsync(String projectId, Long orderId, BigDecimal discountRate);

    /**
     * 根据商品ID批量删除订单详情（同步）
     */
    boolean batchDeleteByProductId(String projectId, List<Long> productIds);

    /**
     * 根据商品ID批量删除订单详情（异步）
     */
    CompletableFuture<Boolean> batchDeleteByProductIdAsync(String projectId, List<Long> productIds);
}