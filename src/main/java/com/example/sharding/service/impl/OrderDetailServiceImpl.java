package com.example.sharding.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sharding.annotation.ShardingDataSource;
import com.example.sharding.context.DatabaseContext;
import com.example.sharding.entity.OrderDetail;
import com.example.sharding.mapper.OrderDetailMapper;
import com.example.sharding.service.OrderDetailService;
import com.example.sharding.service.TableAutoCreateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 订单详情服务实现
 * 
 * @author example
 */
@Slf4j
@Service
@ShardingDataSource
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

    @Autowired
    private TableAutoCreateService tableAutoCreateService;

    @Override
    @Transactional
    public boolean batchCreateOrderDetails(List<OrderDetail> detailList, String projectId) {
        log.info("批量创建订单详情 - 项目ID: {}, 详情数量: {}", projectId, detailList.size());
        
        // 设置项目ID
        detailList.forEach(detail -> detail.setProjectId(projectId));
        
        // 自动创建表（如果不存在）
        tableAutoCreateService.autoCreateTableIfNotExists("order_detail", projectId);
        
        int insertCount = baseMapper.batchInsert(detailList);
        return insertCount > 0;
    }

    @Override
    @Async("taskExecutor")
    @Transactional
    public CompletableFuture<Boolean> batchCreateOrderDetailsAsync(List<OrderDetail> detailList, String projectId) {
        log.info("异步批量创建订单详情 - 项目ID: {}, 详情数量: {}", projectId, detailList.size());
        
        // 在异步方法中手动设置上下文
        DatabaseContext.setProjectId(projectId);
        
        boolean result = batchCreateOrderDetails(detailList, projectId);
        return CompletableFuture.completedFuture(result);
    }

    @Override
    @Transactional
    public boolean batchUpdateQuantity(String projectId, List<Long> detailIds, Integer quantity) {
        log.info("批量更新商品数量 - 项目ID: {}, 详情数量: {}, 新数量: {}", projectId, detailIds.size(), quantity);
        int updateCount = baseMapper.batchUpdateQuantity(projectId, detailIds, quantity);
        return updateCount > 0;
    }

    @Override
    @Async("taskExecutor")
    @Transactional
    public CompletableFuture<Boolean> batchUpdateQuantityAsync(String projectId, List<Long> detailIds, Integer quantity) {
        log.info("异步批量更新商品数量 - 项目ID: {}, 详情数量: {}, 新数量: {}", projectId, detailIds.size(), quantity);
        
        // 在异步方法中手动设置上下文
        DatabaseContext.setProjectId(projectId);
        
        boolean result = batchUpdateQuantity(projectId, detailIds, quantity);
        return CompletableFuture.completedFuture(result);
    }

    @Override
    @Transactional
    public boolean batchUpdatePriceByOrderId(String projectId, Long orderId, BigDecimal discountRate) {
        log.info("批量更新订单价格 - 项目ID: {}, 订单ID: {}, 折扣率: {}", projectId, orderId, discountRate);
        int updateCount = baseMapper.batchUpdatePriceByOrderId(projectId, orderId, discountRate);
        return updateCount > 0;
    }

    @Override
    @Async("taskExecutor")
    @Transactional
    public CompletableFuture<Boolean> batchUpdatePriceByOrderIdAsync(String projectId, Long orderId, BigDecimal discountRate) {
        log.info("异步批量更新订单价格 - 项目ID: {}, 订单ID: {}, 折扣率: {}", projectId, orderId, discountRate);
        
        // 在异步方法中手动设置上下文
        DatabaseContext.setProjectId(projectId);
        
        boolean result = batchUpdatePriceByOrderId(projectId, orderId, discountRate);
        return CompletableFuture.completedFuture(result);
    }

    @Override
    @Transactional
    public boolean batchDeleteByProductId(String projectId, List<Long> productIds) {
        log.info("根据商品ID批量删除订单详情 - 项目ID: {}, 商品数量: {}", projectId, productIds.size());
        int deleteCount = baseMapper.batchDeleteByProductId(projectId, productIds);
        return deleteCount > 0;
    }

    @Override
    @Async("taskExecutor")
    @Transactional
    public CompletableFuture<Boolean> batchDeleteByProductIdAsync(String projectId, List<Long> productIds) {
        log.info("异步根据商品ID批量删除订单详情 - 项目ID: {}, 商品数量: {}", projectId, productIds.size());
        
        // 在异步方法中手动设置上下文
        DatabaseContext.setProjectId(projectId);
        
        boolean result = batchDeleteByProductId(projectId, productIds);
        return CompletableFuture.completedFuture(result);
    }
}