-- ================================================
-- 多表联查和批量更新示例 - 表结构创建脚本
-- ================================================

-- 创建数据库
-- CREATE DATABASE IF NOT EXISTS database_001 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- CREATE DATABASE IF NOT EXISTS database_002 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- CREATE DATABASE IF NOT EXISTS database_003 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库（根据实际情况选择）
-- USE database_001;

-- ================================================
-- 订单信息表（order_info）
-- ================================================
CREATE TABLE IF NOT EXISTS `order_info` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `order_no` varchar(64) NOT NULL COMMENT '订单号',
    `user_id` bigint(20) NOT NULL COMMENT '用户ID',
    `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '订单状态（0:待支付 1:已支付 2:已发货 3:已完成 4:已取消）',
    `total_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '订单总金额',
    `paid_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '实付金额',
    `shipping_address` varchar(500) DEFAULT NULL COMMENT '收货地址',
    `receiver_name` varchar(100) DEFAULT NULL COMMENT '收货人',
    `receiver_phone` varchar(20) DEFAULT NULL COMMENT '收货人电话',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    `project_id` varchar(64) NOT NULL COMMENT '项目ID',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除（0:未删除 1:已删除）',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_project_id` (`project_id`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单信息表';

-- ================================================
-- 订单详情表（order_detail）
-- ================================================
CREATE TABLE IF NOT EXISTS `order_detail` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '详情ID',
    `order_id` bigint(20) NOT NULL COMMENT '订单ID',
    `product_id` bigint(20) NOT NULL COMMENT '商品ID',
    `product_name` varchar(200) NOT NULL COMMENT '商品名称',
    `product_spec` varchar(500) DEFAULT NULL COMMENT '商品规格',
    `unit_price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '商品单价',
    `quantity` int(11) NOT NULL DEFAULT '1' COMMENT '购买数量',
    `subtotal` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '小计金额',
    `project_id` varchar(64) NOT NULL COMMENT '项目ID',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除（0:未删除 1:已删除）',
    PRIMARY KEY (`id`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_product_id` (`product_id`),
    KEY `idx_project_id` (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单详情表';

-- ================================================
-- 插入测试数据
-- ================================================

-- 插入用户数据（假设user_info表已存在）
INSERT IGNORE INTO `user_info` (`id`, `username`, `real_name`, `email`, `phone`, `age`, `gender`, `project_id`, `create_time`, `update_time`, `deleted`) VALUES
(1, 'test_user_1', '测试用户1', 'test1@example.com', '13800138001', 25, 1, 'project_001', NOW(), NOW(), 0),
(2, 'test_user_2', '测试用户2', 'test2@example.com', '13800138002', 30, 0, 'project_001', NOW(), NOW(), 0),
(3, 'test_user_3', '测试用户3', 'test3@example.com', '13800138003', 28, 1, 'project_001', NOW(), NOW(), 0);

-- 插入订单数据
INSERT IGNORE INTO `order_info` (`id`, `order_no`, `user_id`, `status`, `total_amount`, `paid_amount`, `shipping_address`, `receiver_name`, `receiver_phone`, `remark`, `project_id`, `create_time`, `update_time`, `deleted`) VALUES
(1, 'ORD202312010001', 1, 1, 299.00, 299.00, '北京市朝阳区测试街道123号', '测试用户1', '13800138001', '测试订单1', 'project_001', NOW(), NOW(), 0),
(2, 'ORD202312010002', 2, 0, 599.00, 0.00, '上海市浦东新区测试路456号', '测试用户2', '13800138002', '测试订单2', 'project_001', NOW(), NOW(), 0),
(3, 'ORD202312010003', 1, 2, 199.00, 199.00, '广州市天河区测试大道789号', '测试用户1', '13800138001', '测试订单3', 'project_001', NOW(), NOW(), 0),
(4, 'ORD202312010004', 3, 1, 399.00, 399.00, '深圳市南山区测试中心101号', '测试用户3', '13800138003', '测试订单4', 'project_001', NOW(), NOW(), 0);

-- 插入订单详情数据
INSERT IGNORE INTO `order_detail` (`id`, `order_id`, `product_id`, `product_name`, `product_spec`, `unit_price`, `quantity`, `subtotal`, `project_id`, `create_time`, `update_time`, `deleted`) VALUES
(1, 1, 1001, 'iPhone 15', '128GB 黑色', 299.00, 1, 299.00, 'project_001', NOW(), NOW(), 0),
(2, 2, 1002, 'MacBook Pro', '13寸 M2芯片', 599.00, 1, 599.00, 'project_001', NOW(), NOW(), 0),
(3, 3, 1003, 'iPad Air', '64GB WiFi版', 199.00, 1, 199.00, 'project_001', NOW(), NOW(), 0),
(4, 4, 1001, 'iPhone 15', '256GB 白色', 349.00, 1, 349.00, 'project_001', NOW(), NOW(), 0),
(5, 4, 1004, 'AirPods Pro', '第二代', 50.00, 1, 50.00, 'project_001', NOW(), NOW(), 0);

-- ================================================
-- 创建分表示例（用于演示分表功能）
-- ================================================

-- 项目 project_001 的分表
CREATE TABLE IF NOT EXISTS `order_info_001` LIKE `order_info`;
CREATE TABLE IF NOT EXISTS `order_detail_001` LIKE `order_detail`;
CREATE TABLE IF NOT EXISTS `user_info_001` LIKE `user_info`;

-- 项目 project_002 的分表
CREATE TABLE IF NOT EXISTS `order_info_002` LIKE `order_info`;
CREATE TABLE IF NOT EXISTS `order_detail_002` LIKE `order_detail`;
CREATE TABLE IF NOT EXISTS `user_info_002` LIKE `user_info`;

-- 项目 project_003 的分表
CREATE TABLE IF NOT EXISTS `order_info_003` LIKE `order_info`;
CREATE TABLE IF NOT EXISTS `order_detail_003` LIKE `order_detail`;
CREATE TABLE IF NOT EXISTS `user_info_003` LIKE `user_info`;

-- ================================================
-- 注意事项
-- ================================================
-- 1. 请根据实际的分库分表策略创建对应的表
-- 2. 表名后缀需要与项目中的分片策略保持一致
-- 3. 在不同的数据库中创建相同的表结构
-- 4. 确保分片键（project_id）的索引优化
-- 5. 建议在生产环境中添加更多的性能优化索引