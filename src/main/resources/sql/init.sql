-- =============================================
-- 动态分库分表系统 - 数据库初始化脚本
-- =============================================

-- 创建默认数据库（用于配置表）
CREATE DATABASE IF NOT EXISTS default_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE default_db;

-- 创建分片配置表
DROP TABLE IF EXISTS sharding_config;
CREATE TABLE sharding_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    project_id VARCHAR(64) NOT NULL UNIQUE COMMENT '项目ID',
    database_suffix VARCHAR(32) NOT NULL COMMENT '数据库后缀',
    table_suffix VARCHAR(32) NOT NULL COMMENT '表后缀',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '是否删除（0:未删除 1:已删除）',
    INDEX idx_project_id (project_id),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分片配置表';

-- 创建用户信息原始表（作为模板）
DROP TABLE IF EXISTS user_info;
CREATE TABLE user_info (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    real_name VARCHAR(100) COMMENT '真实姓名',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    age INT COMMENT '年龄',
    gender TINYINT COMMENT '性别（0:女 1:男）',
    project_id VARCHAR(64) NOT NULL COMMENT '项目ID',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '是否删除（0:未删除 1:已删除）',
    INDEX idx_username (username),
    INDEX idx_project_id (project_id),
    INDEX idx_email (email),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户信息表';

-- 创建用户档案表（演示JOIN查询）
DROP TABLE IF EXISTS user_profile;
CREATE TABLE user_profile (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '档案ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    avatar VARCHAR(255) COMMENT '头像URL',
    bio TEXT COMMENT '个人简介',
    project_id VARCHAR(64) NOT NULL COMMENT '项目ID',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '是否删除（0:未删除 1:已删除）',
    INDEX idx_user_id (user_id),
    INDEX idx_project_id (project_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户档案表';

-- 插入示例分片配置
INSERT INTO sharding_config (project_id, database_suffix, table_suffix) VALUES
('project_001', '001', '001'),
('project_002', '002', '002'),
('project_003', '003', '003'),
('test_project', '001', 'test'),
('demo_project', '002', 'demo');

-- 插入示例用户数据
INSERT INTO user_info (username, real_name, email, phone, age, gender, project_id) VALUES
('admin', '管理员', 'admin@example.com', '13800138000', 30, 1, 'project_001'),
('user1', '张三', 'zhangsan@example.com', '13800138001', 25, 1, 'project_001'),
('user2', '李四', 'lisi@example.com', '13800138002', 28, 0, 'project_002'),
('test_user', '测试用户', 'test@example.com', '13800138999', 20, 1, 'test_project');

-- =============================================
-- 创建分库（根据需要创建对应的数据库）
-- =============================================

-- 创建database_001
CREATE DATABASE IF NOT EXISTS database_001 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建database_002  
CREATE DATABASE IF NOT EXISTS database_002 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建database_003
CREATE DATABASE IF NOT EXISTS database_003 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- =============================================
-- 在各个分库中创建原始表结构（可选，系统会自动创建）
-- =============================================

-- database_001 中的表
USE database_001;

CREATE TABLE IF NOT EXISTS user_info (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    real_name VARCHAR(100) COMMENT '真实姓名',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    age INT COMMENT '年龄',
    gender TINYINT COMMENT '性别（0:女 1:男）',
    project_id VARCHAR(64) NOT NULL COMMENT '项目ID',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '是否删除（0:未删除 1:已删除）',
    INDEX idx_username (username),
    INDEX idx_project_id (project_id),
    INDEX idx_email (email),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户信息表';

CREATE TABLE IF NOT EXISTS user_profile (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '档案ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    avatar VARCHAR(255) COMMENT '头像URL',
    bio TEXT COMMENT '个人简介',
    project_id VARCHAR(64) NOT NULL COMMENT '项目ID',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '是否删除（0:未删除 1:已删除）',
    INDEX idx_user_id (user_id),
    INDEX idx_project_id (project_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户档案表';

-- database_002 中的表
USE database_002;

CREATE TABLE IF NOT EXISTS user_info (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    real_name VARCHAR(100) COMMENT '真实姓名',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    age INT COMMENT '年龄',
    gender TINYINT COMMENT '性别（0:女 1:男）',
    project_id VARCHAR(64) NOT NULL COMMENT '项目ID',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '是否删除（0:未删除 1:已删除）',
    INDEX idx_username (username),
    INDEX idx_project_id (project_id),
    INDEX idx_email (email),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户信息表';

CREATE TABLE IF NOT EXISTS user_profile (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '档案ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    avatar VARCHAR(255) COMMENT '头像URL',
    bio TEXT COMMENT '个人简介',
    project_id VARCHAR(64) NOT NULL COMMENT '项目ID',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '是否删除（0:未删除 1:已删除）',
    INDEX idx_user_id (user_id),
    INDEX idx_project_id (project_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户档案表';

-- database_003 中的表
USE database_003;

CREATE TABLE IF NOT EXISTS user_info (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    real_name VARCHAR(100) COMMENT '真实姓名',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    age INT COMMENT '年龄',
    gender TINYINT COMMENT '性别（0:女 1:男）',
    project_id VARCHAR(64) NOT NULL COMMENT '项目ID',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '是否删除（0:未删除 1:已删除）',
    INDEX idx_username (username),
    INDEX idx_project_id (project_id),
    INDEX idx_email (email),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户信息表';

CREATE TABLE IF NOT EXISTS user_profile (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '档案ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    avatar VARCHAR(255) COMMENT '头像URL',
    bio TEXT COMMENT '个人简介',
    project_id VARCHAR(64) NOT NULL COMMENT '项目ID',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '是否删除（0:未删除 1:已删除）',
    INDEX idx_user_id (user_id),
    INDEX idx_project_id (project_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户档案表';