# 动态分库分表系统

## 项目简介

基于Spring Boot 2和MyBatis Plus的动态分库分表系统，支持根据项目ID(projectId)动态切换数据源和表名。系统通过配置表管理分库分表策略，在运行时自动进行数据源路由和表名替换，支持表自动创建功能。

## 技术栈

- **Spring Boot 2.x** - 主框架
- **MyBatis Plus 3.x** - ORM框架
- **HikariCP** - 数据库连接池
- **MySQL 8.0** - 数据库
- **AOP** - 切面编程实现动态表名
- **ThreadLocal** - 线程本地存储保证一致性
- **异步线程传递** - 跨线程上下文传递

## 架构设计

### 1. 核心组件

```
├── config/                    # 配置类
│   ├── DataSourceConfig       # 动态数据源配置
│   ├── MybatisPlusConfig      # MyBatis Plus配置
│   └── AsyncConfig            # 异步配置
├── context/                   # 上下文管理
│   ├── DatabaseContext        # 数据库上下文
│   └── TableContext           # 表名上下文
├── interceptor/               # 拦截器
│   ├── DynamicTableInterceptor # 动态表名拦截器
│   └── DataSourceAspect       # 数据源切换切面
├── strategy/                  # 分库分表策略
│   ├── ShardingStrategy       # 分片策略接口
│   └── ProjectShardingStrategy # 项目分片策略实现
├── service/                   # 业务服务
│   ├── ConfigService          # 配置服务
│   └── TableAutoCreateService # 表自动创建服务
└── annotation/                # 注解
    ├── ShardingDataSource     # 分库注解
    └── ShardingTable          # 分表注解
```

### 2. 分库分表策略

**分库策略**：
- 根据projectId从配置表查询对应的数据库后缀
- 格式：`database_${suffix}`
- 示例：project_001 -> database_001

**分表策略**：
- 根据projectId从配置表查询对应的表后缀
- 格式：`${original_table}_${suffix}`
- 示例：user_info -> user_info_001

### 3. 数据流程

```
用户请求 -> 获取projectId -> 查询配置表 -> 设置上下文 -> 
切换数据源 -> 执行SQL -> 动态替换表名 -> 检查表存在性 -> 
自动建表(如需要) -> 执行业务逻辑
```

### 4. 线程一致性保证

- **同线程**：使用ThreadLocal存储上下文信息
- **异步线程**：通过TaskDecorator传递上下文
- **手动传递**：提供工具方法手动设置上下文

## 核心功能

### 1. 动态数据源切换
```java
@ShardingDataSource
@Service
public class UserService {
    public void save(User user, String projectId) {
        // 自动根据projectId切换到对应数据源
    }
}
```

### 2. 动态表名替换
```xml
<!-- user.xml -->
<insert id="insert">
    INSERT INTO user_info (name, age) VALUES (#{name}, #{age})
</insert>
```
运行时自动替换为：`INSERT INTO user_info_001 (name, age) VALUES (?, ?)`

### 3. 表自动创建
系统检测到表不存在时，自动根据原始表结构创建新表。

### 4. 异步支持
```java
@Async
public CompletableFuture<Void> asyncProcess(String projectId) {
    DatabaseContext.setProjectId(projectId); // 上下文传递
    // 异步业务逻辑
}
```

## 配置说明

### 1. 数据库配置
```yaml
spring:
  datasource:
    default:
      url: jdbc:mysql://localhost:3306/default_db
      username: root
      password: 123456
    dynamic:
      database-001:
        url: jdbc:mysql://localhost:3306/database_001
        username: root
        password: 123456
      database-002:
        url: jdbc:mysql://localhost:3306/database_002
        username: root
        password: 123456
```

### 2. 分片配置表
```sql
CREATE TABLE sharding_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    project_id VARCHAR(64) NOT NULL UNIQUE,
    database_suffix VARCHAR(32) NOT NULL,
    table_suffix VARCHAR(32) NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_project_id (project_id)
);
```

## 使用指南

### 1. 基本使用
```java
// 设置上下文
DatabaseContext.setProjectId("project_001");

// 执行业务逻辑 - 自动路由到对应库表
userService.save(user);
```

### 2. 批量操作
```java
@Transactional
public void batchSave(List<User> users, String projectId) {
    DatabaseContext.setProjectId(projectId);
    users.forEach(userService::save);
}
```

### 3. 异步操作
```java
@Async
public CompletableFuture<List<User>> asyncQuery(String projectId) {
    DatabaseContext.setProjectId(projectId);
    return CompletableFuture.completedFuture(userService.list());
}
```

## 部署说明

1. 创建主配置数据库和配置表
2. 根据需要创建分库
3. 配置数据源连接信息
4. 启动应用，系统自动检测和创建表

## 注意事项

1. **事务范围**：分库分表后，分布式事务需要特殊处理
2. **查询限制**：跨库查询需要在应用层聚合
3. **数据迁移**：表结构变更需要同步到所有分表
4. **监控告警**：建议配置数据源连接监控

## 扩展功能

- 支持读写分离
- 支持分片键路由优化
- 支持在线扩缩容
- 支持数据迁移工具