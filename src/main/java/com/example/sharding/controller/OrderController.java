package com.example.sharding.controller;

import com.example.sharding.entity.Order;
import com.example.sharding.entity.OrderDetail;
import com.example.sharding.entity.vo.OrderDetailVO;
import com.example.sharding.service.OrderDetailService;
import com.example.sharding.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 订单控制器
 * 演示多表联查和批量更新功能，支持同步和异步操作
 * 
 * @author example
 */
@Slf4j
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 创建订单（同步）
     */
    @PostMapping("/create/{projectId}")
    public String createOrder(@RequestBody Order order, @PathVariable String projectId) {
        boolean success = orderService.createOrder(order, projectId);
        return success ? "订单创建成功" : "订单创建失败";
    }

    /**
     * 创建订单（异步）
     */
    @PostMapping("/create/async/{projectId}")
    public CompletableFuture<String> createOrderAsync(@RequestBody Order order, @PathVariable String projectId) {
        return orderService.createOrderAsync(order, projectId)
                .thenApply(success -> success ? "异步订单创建成功" : "异步订单创建失败");
    }

    /**
     * 批量创建订单详情（同步）
     */
    @PostMapping("/detail/batch/{projectId}")
    public String batchCreateOrderDetails(@RequestBody List<OrderDetail> detailList, @PathVariable String projectId) {
        boolean success = orderDetailService.batchCreateOrderDetails(detailList, projectId);
        return success ? "订单详情批量创建成功" : "订单详情批量创建失败";
    }

    /**
     * 批量创建订单详情（异步）
     */
    @PostMapping("/detail/batch/async/{projectId}")
    public CompletableFuture<String> batchCreateOrderDetailsAsync(@RequestBody List<OrderDetail> detailList, @PathVariable String projectId) {
        return orderDetailService.batchCreateOrderDetailsAsync(detailList, projectId)
                .thenApply(success -> success ? "异步订单详情批量创建成功" : "异步订单详情批量创建失败");
    }

    /**
     * 多表联查：查询订单详情（同步）
     */
    @GetMapping("/detail/{projectId}")
    public List<OrderDetailVO> getOrderDetailWithUser(@PathVariable String projectId, 
                                                     @RequestParam(required = false) String orderNo) {
        return orderService.getOrderDetailWithUser(projectId, orderNo);
    }

    /**
     * 多表联查：查询订单详情（异步）
     */
    @GetMapping("/detail/async/{projectId}")
    public CompletableFuture<List<OrderDetailVO>> getOrderDetailWithUserAsync(@PathVariable String projectId, 
                                                                             @RequestParam(required = false) String orderNo) {
        return orderService.getOrderDetailWithUserAsync(projectId, orderNo);
    }

    /**
     * 多表联查：根据用户ID查询订单详情（同步）
     */
    @GetMapping("/detail/user/{projectId}/{userId}")
    public List<OrderDetailVO> getOrderDetailByUserId(@PathVariable String projectId, @PathVariable Long userId) {
        return orderService.getOrderDetailByUserId(projectId, userId);
    }

    /**
     * 多表联查：根据用户ID查询订单详情（异步）
     */
    @GetMapping("/detail/user/async/{projectId}/{userId}")
    public CompletableFuture<List<OrderDetailVO>> getOrderDetailByUserIdAsync(@PathVariable String projectId, @PathVariable Long userId) {
        return orderService.getOrderDetailByUserIdAsync(projectId, userId);
    }

    /**
     * 多表联查：根据状态查询订单详情（同步）
     */
    @GetMapping("/detail/status/{projectId}/{status}")
    public List<OrderDetailVO> getOrderDetailByStatus(@PathVariable String projectId, @PathVariable Integer status) {
        return orderService.getOrderDetailByStatus(projectId, status);
    }

    /**
     * 多表联查：根据状态查询订单详情（异步）
     */
    @GetMapping("/detail/status/async/{projectId}/{status}")
    public CompletableFuture<List<OrderDetailVO>> getOrderDetailByStatusAsync(@PathVariable String projectId, @PathVariable Integer status) {
        return orderService.getOrderDetailByStatusAsync(projectId, status);
    }

    /**
     * 批量更新订单状态（同步）
     */
    @PutMapping("/status/batch/{projectId}")
    public String batchUpdateOrderStatus(@PathVariable String projectId,
                                       @RequestParam List<Long> orderIds,
                                       @RequestParam Integer newStatus) {
        boolean success = orderService.batchUpdateOrderStatus(projectId, orderIds, newStatus);
        return success ? "订单状态批量更新成功" : "订单状态批量更新失败";
    }

    /**
     * 批量更新订单状态（异步）
     */
    @PutMapping("/status/batch/async/{projectId}")
    public CompletableFuture<String> batchUpdateOrderStatusAsync(@PathVariable String projectId,
                                                               @RequestParam List<Long> orderIds,
                                                               @RequestParam Integer newStatus) {
        return orderService.batchUpdateOrderStatusAsync(projectId, orderIds, newStatus)
                .thenApply(success -> success ? "异步订单状态批量更新成功" : "异步订单状态批量更新失败");
    }

    /**
     * 根据用户ID批量更新订单状态（同步）
     */
    @PutMapping("/status/user/{projectId}")
    public String batchUpdateOrderStatusByUserId(@PathVariable String projectId,
                                               @RequestParam Long userId,
                                               @RequestParam Integer oldStatus,
                                               @RequestParam Integer newStatus) {
        boolean success = orderService.batchUpdateOrderStatusByUserId(projectId, userId, oldStatus, newStatus);
        return success ? "用户订单状态批量更新成功" : "用户订单状态批量更新失败";
    }

    /**
     * 根据用户ID批量更新订单状态（异步）
     */
    @PutMapping("/status/user/async/{projectId}")
    public CompletableFuture<String> batchUpdateOrderStatusByUserIdAsync(@PathVariable String projectId,
                                                                        @RequestParam Long userId,
                                                                        @RequestParam Integer oldStatus,
                                                                        @RequestParam Integer newStatus) {
        return orderService.batchUpdateOrderStatusByUserIdAsync(projectId, userId, oldStatus, newStatus)
                .thenApply(success -> success ? "异步用户订单状态批量更新成功" : "异步用户订单状态批量更新失败");
    }

    /**
     * 批量更新商品数量（同步）
     */
    @PutMapping("/detail/quantity/{projectId}")
    public String batchUpdateQuantity(@PathVariable String projectId,
                                    @RequestParam List<Long> detailIds,
                                    @RequestParam Integer quantity) {
        boolean success = orderDetailService.batchUpdateQuantity(projectId, detailIds, quantity);
        return success ? "商品数量批量更新成功" : "商品数量批量更新失败";
    }

    /**
     * 批量更新商品数量（异步）
     */
    @PutMapping("/detail/quantity/async/{projectId}")
    public CompletableFuture<String> batchUpdateQuantityAsync(@PathVariable String projectId,
                                                            @RequestParam List<Long> detailIds,
                                                            @RequestParam Integer quantity) {
        return orderDetailService.batchUpdateQuantityAsync(projectId, detailIds, quantity)
                .thenApply(success -> success ? "异步商品数量批量更新成功" : "异步商品数量批量更新失败");
    }

    /**
     * 根据订单ID批量更新价格（同步）
     */
    @PutMapping("/detail/price/{projectId}")
    public String batchUpdatePriceByOrderId(@PathVariable String projectId,
                                          @RequestParam Long orderId,
                                          @RequestParam BigDecimal discountRate) {
        boolean success = orderDetailService.batchUpdatePriceByOrderId(projectId, orderId, discountRate);
        return success ? "订单价格批量更新成功" : "订单价格批量更新失败";
    }

    /**
     * 根据订单ID批量更新价格（异步）
     */
    @PutMapping("/detail/price/async/{projectId}")
    public CompletableFuture<String> batchUpdatePriceByOrderIdAsync(@PathVariable String projectId,
                                                                  @RequestParam Long orderId,
                                                                  @RequestParam BigDecimal discountRate) {
        return orderDetailService.batchUpdatePriceByOrderIdAsync(projectId, orderId, discountRate)
                .thenApply(success -> success ? "异步订单价格批量更新成功" : "异步订单价格批量更新失败");
    }

    /**
     * 根据商品ID批量删除订单详情（同步）
     */
    @DeleteMapping("/detail/product/{projectId}")
    public String batchDeleteByProductId(@PathVariable String projectId,
                                       @RequestParam List<Long> productIds) {
        boolean success = orderDetailService.batchDeleteByProductId(projectId, productIds);
        return success ? "订单详情批量删除成功" : "订单详情批量删除失败";
    }

    /**
     * 根据商品ID批量删除订单详情（异步）
     */
    @DeleteMapping("/detail/product/async/{projectId}")
    public CompletableFuture<String> batchDeleteByProductIdAsync(@PathVariable String projectId,
                                                               @RequestParam List<Long> productIds) {
        return orderDetailService.batchDeleteByProductIdAsync(projectId, productIds)
                .thenApply(success -> success ? "异步订单详情批量删除成功" : "异步订单详情批量删除失败");
    }
}