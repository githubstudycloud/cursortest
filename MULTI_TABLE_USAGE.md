# 多表联查和批量更新功能使用指南

本文档详细介绍了在动态分库分表系统中添加的多表联查和批量更新功能，支持同步和异步多线程操作。

## 目录

1. [功能概述](#功能概述)
2. [数据模型](#数据模型)
3. [API接口说明](#api接口说明)
4. [多表联查示例](#多表联查示例)
5. [批量更新示例](#批量更新示例)
6. [异步操作示例](#异步操作示例)
7. [AOP分库分表机制](#aop分库分表机制)
8. [性能优化建议](#性能优化建议)

## 功能概述

本项目在原有分库分表功能基础上，新增了以下核心功能：

- **多表联查**：支持订单、订单详情、用户信息的三表联查
- **批量更新**：支持订单状态、商品数量、价格等的批量更新操作
- **同步/异步**：所有操作都提供同步和异步两种调用方式
- **自动分片**：通过AOP注解自动实现分库分表路由
- **事务支持**：批量操作支持事务回滚保证数据一致性

## 数据模型

### 实体关系

```
用户信息(user_info) 1:N 订单信息(order_info) 1:N 订单详情(order_detail)
```

### 核心实体类

- `Order` - 订单信息实体
- `OrderDetail` - 订单详情实体  
- `OrderDetailVO` - 多表联查结果视图对象

## API接口说明

### 基础URL

```
http://localhost:8080/api/order
```

### 接口列表

| 功能分类 | 接口路径 | 方法 | 同步/异步 | 描述 |
|---------|---------|------|----------|------|
| 订单创建 | `/create/{projectId}` | POST | 同步 | 创建订单 |
| 订单创建 | `/create/async/{projectId}` | POST | 异步 | 异步创建订单 |
| 批量创建详情 | `/detail/batch/{projectId}` | POST | 同步 | 批量创建订单详情 |
| 批量创建详情 | `/detail/batch/async/{projectId}` | POST | 异步 | 异步批量创建订单详情 |
| 多表联查 | `/detail/{projectId}` | GET | 同步 | 查询订单详情（含用户信息） |
| 多表联查 | `/detail/async/{projectId}` | GET | 异步 | 异步查询订单详情 |
| 用户订单查询 | `/detail/user/{projectId}/{userId}` | GET | 同步 | 根据用户ID查询订单详情 |
| 用户订单查询 | `/detail/user/async/{projectId}/{userId}` | GET | 异步 | 异步根据用户ID查询 |
| 状态订单查询 | `/detail/status/{projectId}/{status}` | GET | 同步 | 根据状态查询订单详情 |
| 状态订单查询 | `/detail/status/async/{projectId}/{status}` | GET | 异步 | 异步根据状态查询 |
| 批量更新状态 | `/status/batch/{projectId}` | PUT | 同步 | 批量更新订单状态 |
| 批量更新状态 | `/status/batch/async/{projectId}` | PUT | 异步 | 异步批量更新订单状态 |
| 用户状态更新 | `/status/user/{projectId}` | PUT | 同步 | 根据用户ID批量更新状态 |
| 用户状态更新 | `/status/user/async/{projectId}` | PUT | 异步 | 异步根据用户ID更新状态 |
| 批量更新数量 | `/detail/quantity/{projectId}` | PUT | 同步 | 批量更新商品数量 |
| 批量更新数量 | `/detail/quantity/async/{projectId}` | PUT | 异步 | 异步批量更新商品数量 |
| 批量更新价格 | `/detail/price/{projectId}` | PUT | 同步 | 根据订单ID批量更新价格 |
| 批量更新价格 | `/detail/price/async/{projectId}` | PUT | 异步 | 异步批量更新价格 |
| 批量删除详情 | `/detail/product/{projectId}` | DELETE | 同步 | 根据商品ID批量删除详情 |
| 批量删除详情 | `/detail/product/async/{projectId}` | DELETE | 异步 | 异步批量删除详情 |

## 多表联查示例

### 1. 查询所有订单详情（同步）

```bash
curl -X GET "http://localhost:8080/api/order/detail/project_001"
```

**响应示例：**
```json
[
    {
        "orderId": 1,
        "orderNo": "ORD202312010001",
        "userId": 1,
        "username": "test_user_1",
        "realName": "测试用户1",
        "orderStatus": 1,
        "totalAmount": 299.00,
        "paidAmount": 299.00,
        "shippingAddress": "北京市朝阳区测试街道123号",
        "receiverName": "测试用户1",
        "receiverPhone": "13800138001",
        "detailId": 1,
        "productId": 1001,
        "productName": "iPhone 15",
        "productSpec": "128GB 黑色",
        "unitPrice": 299.00,
        "quantity": 1,
        "subtotal": 299.00,
        "projectId": "project_001",
        "orderCreateTime": "2023-12-01T10:30:00"
    }
]
```

### 2. 根据订单号查询详情（异步）

```bash
curl -X GET "http://localhost:8080/api/order/detail/async/project_001?orderNo=ORD202312010001"
```

### 3. 根据用户ID查询订单详情

```bash
curl -X GET "http://localhost:8080/api/order/detail/user/project_001/1"
```

### 4. 根据订单状态查询详情

```bash
curl -X GET "http://localhost:8080/api/order/detail/status/project_001/1"
```

## 批量更新示例

### 1. 批量更新订单状态

```bash
curl -X PUT "http://localhost:8080/api/order/status/batch/project_001" \
  -d "orderIds=1,2,3&newStatus=2"
```

### 2. 根据用户ID批量更新订单状态

```bash
curl -X PUT "http://localhost:8080/api/order/status/user/project_001" \
  -d "userId=1&oldStatus=0&newStatus=1"
```

### 3. 批量更新商品数量

```bash
curl -X PUT "http://localhost:8080/api/order/detail/quantity/project_001" \
  -d "detailIds=1,2,3&quantity=5"
```

### 4. 批量更新价格（应用折扣）

```bash
curl -X PUT "http://localhost:8080/api/order/detail/price/project_001" \
  -d "orderId=1&discountRate=0.8"
```

### 5. 批量删除订单详情

```bash
curl -X DELETE "http://localhost:8080/api/order/detail/product/project_001" \
  -d "productIds=1001,1002"
```

## 异步操作示例

### 1. 异步创建订单

```bash
curl -X POST "http://localhost:8080/api/order/create/async/project_001" \
  -H "Content-Type: application/json" \
  -d '{
    "orderNo": "ORD202312010005",
    "userId": 1,
    "status": 0,
    "totalAmount": 899.00,
    "paidAmount": 0.00,
    "shippingAddress": "测试地址",
    "receiverName": "测试收货人",
    "receiverPhone": "13800138000",
    "remark": "异步测试订单"
  }'
```

### 2. 异步批量创建订单详情

```bash
curl -X POST "http://localhost:8080/api/order/detail/batch/async/project_001" \
  -H "Content-Type: application/json" \
  -d '[
    {
      "orderId": 1,
      "productId": 1005,
      "productName": "测试商品",
      "productSpec": "测试规格",
      "unitPrice": 100.00,
      "quantity": 2,
      "subtotal": 200.00
    }
  ]'
```

### 3. 异步批量更新状态

```bash
curl -X PUT "http://localhost:8080/api/order/status/batch/async/project_001" \
  -d "orderIds=1,2&newStatus=3"
```

## AOP分库分表机制

### 注解使用

项目使用 `@ShardingDataSource` 注解实现自动分库分表：

```java
@Service
@ShardingDataSource  // 类级别注解，所有方法都会自动分片
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    
    @Override
    public boolean createOrder(Order order, String projectId) {
        // 方法会自动根据projectId进行分库分表路由
        return save(order);
    }
}
```

### 分片策略

- **分库策略**：根据 `projectId` 计算哈希值，路由到不同数据源
- **分表策略**：根据 `projectId` 生成表后缀，例如 `order_info_001`
- **上下文管理**：使用 `ThreadLocal` 管理分片上下文，支持异步场景

### 异步上下文传递

在异步方法中，系统会自动传递分片上下文：

```java
@Async("taskExecutor")
public CompletableFuture<Boolean> createOrderAsync(Order order, String projectId) {
    // 手动设置上下文（双重保险）
    DatabaseContext.setProjectId(projectId);
    
    boolean result = createOrder(order, projectId);
    return CompletableFuture.completedFuture(result);
}
```

## 性能优化建议

### 1. 索引优化

确保在分片表上创建合适的索引：

```sql
-- 订单表索引
CREATE INDEX idx_user_id ON order_info_001 (user_id);
CREATE INDEX idx_status ON order_info_001 (status);
CREATE INDEX idx_create_time ON order_info_001 (create_time);

-- 订单详情表索引  
CREATE INDEX idx_order_id ON order_detail_001 (order_id);
CREATE INDEX idx_product_id ON order_detail_001 (product_id);
```

### 2. 批量操作优化

- 批量插入时使用 `batchInsert` 方法，避免逐条插入
- 批量更新时合理控制批次大小，建议每批1000条以内
- 使用事务保证批量操作的原子性

### 3. 连接池配置

```yaml
spring:
  datasource:
    dynamic:
      database-001:
        initial-size: 10
        min-idle: 10
        max-active: 50
        max-wait: 60000
```

### 4. 异步线程池配置

```yaml
# 在 AsyncConfig 中配置合适的线程池大小
task:
  executor:
    core-pool-size: 8
    max-pool-size: 20
    queue-capacity: 200
```

## 注意事项

1. **分片键一致性**：确保所有关联表使用相同的分片键（projectId）
2. **跨分片查询**：避免跨分片的联表查询，会影响性能
3. **事务边界**：分布式事务需要特别注意事务边界和回滚策略
4. **数据一致性**：在异步操作中注意数据的最终一致性
5. **监控告警**：建议添加分库分表操作的监控和告警机制

## 测试数据准备

运行项目前，请执行 `src/main/resources/sql/multi_table_example.sql` 脚本创建表结构和测试数据。

## 技术栈

- Spring Boot 2.7.18
- MyBatis Plus 3.5.3.1
- MySQL 8.0.33
- Druid 连接池
- Spring AOP
- CompletableFuture 异步编程