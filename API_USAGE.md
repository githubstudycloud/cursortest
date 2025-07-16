# API 使用示例

## 启动前准备

1. **数据库准备**
   ```bash
   # 执行初始化SQL脚本
   mysql -u root -p < src/main/resources/sql/init.sql
   ```

2. **配置数据库连接**
   - 修改 `application.yml` 中的数据库连接配置
   - 确保各个数据库实例正常运行

## API 接口示例

### 1. 分片配置管理

#### 创建分片配置
```bash
curl -X POST "http://localhost:8080/api/sharding-config" \
  -d "projectId=new_project&databaseSuffix=001&tableSuffix=new"
```

#### 查询分片配置
```bash
curl -X GET "http://localhost:8080/api/sharding-config/project_001"
```

#### 查询所有配置
```bash
curl -X GET "http://localhost:8080/api/sharding-config"
```

### 2. 用户信息管理

#### 保存用户信息
```bash
curl -X POST "http://localhost:8080/api/users/project_001" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "newuser",
    "realName": "新用户",
    "email": "newuser@example.com",
    "phone": "13800138888",
    "age": 25,
    "gender": 1
  }'
```

#### 查询用户列表
```bash
curl -X GET "http://localhost:8080/api/users/project_001"
```

#### 异步保存用户
```bash
curl -X POST "http://localhost:8080/api/users/project_002/async" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "asyncuser",
    "realName": "异步用户",
    "email": "async@example.com",
    "phone": "13900139999",
    "age": 28,
    "gender": 0
  }'
```

#### 异步查询用户
```bash
curl -X GET "http://localhost:8080/api/users/project_002/async"
```

### 3. 上下文管理

#### 获取当前上下文
```bash
curl -X GET "http://localhost:8080/api/users/context"
```

#### 手动设置上下文
```bash
curl -X POST "http://localhost:8080/api/users/context/project_003"
```

#### 清理上下文
```bash
curl -X DELETE "http://localhost:8080/api/users/context"
```

## 分库分表验证

### 1. 验证数据源切换

```bash
# 为project_001保存用户（会路由到database-001）
curl -X POST "http://localhost:8080/api/users/project_001" \
  -H "Content-Type: application/json" \
  -d '{"username": "user001", "realName": "用户001", "email": "user001@test.com"}'

# 为project_002保存用户（会路由到database-002）
curl -X POST "http://localhost:8080/api/users/project_002" \
  -H "Content-Type: application/json" \
  -d '{"username": "user002", "realName": "用户002", "email": "user002@test.com"}'
```

### 2. 验证表名替换

查看日志输出，可以看到：
- project_001 的数据会存储到 `database_001.user_info_001` 表
- project_002 的数据会存储到 `database_002.user_info_002` 表

### 3. 验证自动建表

当访问一个新的项目ID时，系统会自动：
1. 根据配置查找对应的数据源和表后缀
2. 检查目标表是否存在
3. 如果不存在，自动根据原始表结构创建新表

## Java 代码示例

### 1. 使用注解方式

```java
@Service
@ShardingDataSource
public class BusinessService {
    
    @Autowired
    private UserInfoService userInfoService;
    
    public void processUser(String projectId, UserInfo user) {
        // 自动根据projectId切换数据源和表名
        userInfoService.saveUser(user, projectId);
    }
}
```

### 2. 使用工具类方式

```java
@Service
public class ManualService {
    
    @Autowired
    private ShardingUtils shardingUtils;
    
    @Autowired
    private UserInfoService userInfoService;
    
    public void processInContext(String projectId) {
        shardingUtils.executeInContext(projectId, () -> {
            // 在指定项目上下文中执行业务逻辑
            List<UserInfo> users = userInfoService.list();
            // 处理用户数据...
        });
    }
}
```

### 3. 异步处理示例

```java
@Service
public class AsyncService {
    
    @Autowired
    private UserInfoService userInfoService;
    
    public CompletableFuture<Void> processUsersAsync(String projectId, List<UserInfo> users) {
        List<CompletableFuture<Boolean>> futures = users.stream()
            .map(user -> userInfoService.saveUserAsync(user, projectId))
            .collect(Collectors.toList());
            
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }
}
```

## 监控和调试

### 1. 查看SQL执行日志

在 `application.yml` 中启用SQL日志：
```yaml
logging:
  level:
    com.example.sharding: DEBUG
    org.springframework.jdbc: DEBUG
```

### 2. 监控上下文切换

观察日志中的上下文设置信息：
```
设置分片上下文 - 项目ID: project_001, 数据源: database-001, 表后缀: 001
SQL表名替换:
原始SQL: SELECT * FROM user_info WHERE project_id = ?
新SQL: SELECT * FROM user_info_001 WHERE project_id = ?
```

### 3. 验证表自动创建

首次访问新项目时会看到：
```
自动创建表[user_info_new]成功
执行DDL成功: CREATE TABLE `user_info_new` (...)
```

## 注意事项

1. **事务管理**：分库操作需要注意事务边界
2. **连接池配置**：根据数据库数量合理配置连接池
3. **缓存策略**：分片配置建议启用缓存
4. **异常处理**：处理数据源切换和建表可能的异常
5. **性能优化**：避免频繁的上下文切换