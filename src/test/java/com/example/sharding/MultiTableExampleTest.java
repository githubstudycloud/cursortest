package com.example.sharding;

import com.example.sharding.entity.Order;
import com.example.sharding.entity.OrderDetail;
import com.example.sharding.entity.vo.OrderDetailVO;
import com.example.sharding.service.OrderDetailService;
import com.example.sharding.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 多表联查和批量更新功能测试
 * 
 * @author example
 */
@Slf4j
@SpringBootTest
public class MultiTableExampleTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 测试同步创建订单
     */
    @Test
    public void testCreateOrder() {
        String projectId = "project_001";
        
        Order order = new Order()
                .setOrderNo("TEST_ORDER_001")
                .setUserId(1L)
                .setStatus(0)
                .setTotalAmount(new BigDecimal("199.00"))
                .setPaidAmount(new BigDecimal("0.00"))
                .setShippingAddress("测试地址")
                .setReceiverName("测试收货人")
                .setReceiverPhone("13800138000")
                .setRemark("测试订单");

        boolean success = orderService.createOrder(order, projectId);
        log.info("创建订单结果: {}", success);
    }

    /**
     * 测试异步创建订单
     */
    @Test
    public void testCreateOrderAsync() throws ExecutionException, InterruptedException {
        String projectId = "project_001";
        
        Order order = new Order()
                .setOrderNo("TEST_ORDER_ASYNC_001")
                .setUserId(2L)
                .setStatus(0)
                .setTotalAmount(new BigDecimal("299.00"))
                .setPaidAmount(new BigDecimal("0.00"))
                .setShippingAddress("异步测试地址")
                .setReceiverName("异步测试收货人")
                .setReceiverPhone("13800138001")
                .setRemark("异步测试订单");

        CompletableFuture<Boolean> future = orderService.createOrderAsync(order, projectId);
        Boolean success = future.get();
        log.info("异步创建订单结果: {}", success);
    }

    /**
     * 测试批量创建订单详情
     */
    @Test
    public void testBatchCreateOrderDetails() {
        String projectId = "project_001";
        
        List<OrderDetail> detailList = Arrays.asList(
                new OrderDetail()
                        .setOrderId(1L)
                        .setProductId(2001L)
                        .setProductName("测试商品1")
                        .setProductSpec("测试规格1")
                        .setUnitPrice(new BigDecimal("50.00"))
                        .setQuantity(2)
                        .setSubtotal(new BigDecimal("100.00")),
                
                new OrderDetail()
                        .setOrderId(1L)
                        .setProductId(2002L)
                        .setProductName("测试商品2")
                        .setProductSpec("测试规格2")
                        .setUnitPrice(new BigDecimal("99.00"))
                        .setQuantity(1)
                        .setSubtotal(new BigDecimal("99.00"))
        );

        boolean success = orderDetailService.batchCreateOrderDetails(detailList, projectId);
        log.info("批量创建订单详情结果: {}", success);
    }

    /**
     * 测试多表联查：查询订单详情
     */
    @Test
    public void testGetOrderDetailWithUser() {
        String projectId = "project_001";
        
        List<OrderDetailVO> orderDetails = orderService.getOrderDetailWithUser(projectId, null);
        log.info("查询到 {} 条订单详情记录", orderDetails.size());
        
        orderDetails.forEach(detail -> {
            log.info("订单号: {}, 用户: {}, 商品: {}, 数量: {}", 
                    detail.getOrderNo(), 
                    detail.getUsername(), 
                    detail.getProductName(), 
                    detail.getQuantity());
        });
    }

    /**
     * 测试异步多表联查
     */
    @Test
    public void testGetOrderDetailWithUserAsync() throws ExecutionException, InterruptedException {
        String projectId = "project_001";
        
        CompletableFuture<List<OrderDetailVO>> future = 
                orderService.getOrderDetailWithUserAsync(projectId, "ORD202312010001");
        
        List<OrderDetailVO> orderDetails = future.get();
        log.info("异步查询到 {} 条订单详情记录", orderDetails.size());
        
        orderDetails.forEach(detail -> {
            log.info("异步查询 - 订单号: {}, 用户: {}, 商品: {}", 
                    detail.getOrderNo(), 
                    detail.getUsername(), 
                    detail.getProductName());
        });
    }

    /**
     * 测试根据用户ID查询订单详情
     */
    @Test
    public void testGetOrderDetailByUserId() {
        String projectId = "project_001";
        Long userId = 1L;
        
        List<OrderDetailVO> orderDetails = orderService.getOrderDetailByUserId(projectId, userId);
        log.info("用户 {} 共有 {} 条订单详情记录", userId, orderDetails.size());
    }

    /**
     * 测试根据状态查询订单详情
     */
    @Test
    public void testGetOrderDetailByStatus() {
        String projectId = "project_001";
        Integer status = 1; // 已支付状态
        
        List<OrderDetailVO> orderDetails = orderService.getOrderDetailByStatus(projectId, status);
        log.info("状态为 {} 的订单共有 {} 条详情记录", status, orderDetails.size());
    }

    /**
     * 测试批量更新订单状态
     */
    @Test
    public void testBatchUpdateOrderStatus() {
        String projectId = "project_001";
        List<Long> orderIds = Arrays.asList(1L, 2L);
        Integer newStatus = 2; // 已发货
        
        boolean success = orderService.batchUpdateOrderStatus(projectId, orderIds, newStatus);
        log.info("批量更新订单状态结果: {}", success);
    }

    /**
     * 测试异步批量更新订单状态
     */
    @Test
    public void testBatchUpdateOrderStatusAsync() throws ExecutionException, InterruptedException {
        String projectId = "project_001";
        List<Long> orderIds = Arrays.asList(3L, 4L);
        Integer newStatus = 3; // 已完成
        
        CompletableFuture<Boolean> future = 
                orderService.batchUpdateOrderStatusAsync(projectId, orderIds, newStatus);
        
        Boolean success = future.get();
        log.info("异步批量更新订单状态结果: {}", success);
    }

    /**
     * 测试根据用户ID批量更新订单状态
     */
    @Test
    public void testBatchUpdateOrderStatusByUserId() {
        String projectId = "project_001";
        Long userId = 2L;
        Integer oldStatus = 0; // 待支付
        Integer newStatus = 1; // 已支付
        
        boolean success = orderService.batchUpdateOrderStatusByUserId(projectId, userId, oldStatus, newStatus);
        log.info("根据用户ID批量更新订单状态结果: {}", success);
    }

    /**
     * 测试批量更新商品数量
     */
    @Test
    public void testBatchUpdateQuantity() {
        String projectId = "project_001";
        List<Long> detailIds = Arrays.asList(1L, 2L);
        Integer quantity = 3;
        
        boolean success = orderDetailService.batchUpdateQuantity(projectId, detailIds, quantity);
        log.info("批量更新商品数量结果: {}", success);
    }

    /**
     * 测试批量更新价格（应用折扣）
     */
    @Test
    public void testBatchUpdatePriceByOrderId() {
        String projectId = "project_001";
        Long orderId = 1L;
        BigDecimal discountRate = new BigDecimal("0.8"); // 8折
        
        boolean success = orderDetailService.batchUpdatePriceByOrderId(projectId, orderId, discountRate);
        log.info("批量更新订单价格结果: {}", success);
    }

    /**
     * 测试批量删除订单详情
     */
    @Test
    public void testBatchDeleteByProductId() {
        String projectId = "project_001";
        List<Long> productIds = Arrays.asList(1004L); // AirPods Pro
        
        boolean success = orderDetailService.batchDeleteByProductId(projectId, productIds);
        log.info("批量删除订单详情结果: {}", success);
    }

    /**
     * 综合测试：创建订单 -> 添加详情 -> 多表查询 -> 批量更新
     */
    @Test
    public void testCompleteOrderFlow() throws ExecutionException, InterruptedException {
        String projectId = "project_001";
        
        // 1. 创建订单
        Order order = new Order()
                .setOrderNo("FLOW_TEST_001")
                .setUserId(1L)
                .setStatus(0)
                .setTotalAmount(new BigDecimal("500.00"))
                .setPaidAmount(new BigDecimal("0.00"))
                .setShippingAddress("综合测试地址")
                .setReceiverName("综合测试用户")
                .setReceiverPhone("13800138888")
                .setRemark("综合测试订单");

        boolean orderCreated = orderService.createOrder(order, projectId);
        log.info("步骤1 - 创建订单: {}", orderCreated);

        // 2. 批量添加订单详情
        List<OrderDetail> details = Arrays.asList(
                new OrderDetail()
                        .setOrderId(order.getId())
                        .setProductId(3001L)
                        .setProductName("综合测试商品1")
                        .setUnitPrice(new BigDecimal("200.00"))
                        .setQuantity(1)
                        .setSubtotal(new BigDecimal("200.00")),
                
                new OrderDetail()
                        .setOrderId(order.getId())
                        .setProductId(3002L)
                        .setProductName("综合测试商品2")
                        .setUnitPrice(new BigDecimal("300.00"))
                        .setQuantity(1)
                        .setSubtotal(new BigDecimal("300.00"))
        );

        boolean detailsCreated = orderDetailService.batchCreateOrderDetails(details, projectId);
        log.info("步骤2 - 批量创建详情: {}", detailsCreated);

        // 3. 多表联查订单详情
        List<OrderDetailVO> orderDetails = orderService.getOrderDetailWithUser(projectId, order.getOrderNo());
        log.info("步骤3 - 多表查询结果: {} 条记录", orderDetails.size());

        // 4. 异步批量更新订单状态
        CompletableFuture<Boolean> updateFuture = 
                orderService.batchUpdateOrderStatusAsync(projectId, Arrays.asList(order.getId()), 1);
        Boolean updateResult = updateFuture.get();
        log.info("步骤4 - 异步更新状态: {}", updateResult);

        log.info("综合测试流程完成！");
    }
}