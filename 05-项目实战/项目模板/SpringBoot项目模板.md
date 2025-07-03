# Spring Boot项目实战模板

## 项目信息
- **项目名称**: [项目名称]
- **创建日期**: 2024-01-XX
- **技术栈**: Spring Boot + MySQL + MyBatis Plus + Redis
- **难度等级**: ⭐⭐⭐⭐
- **预计用时**: 2-3周

## 项目目标
- [ ] 掌握Spring Boot项目搭建
- [ ] 理解RESTful API设计
- [ ] 学习数据库设计和操作
- [ ] 掌握前后端分离开发

## 技术栈

### 后端技术
```xml
<!-- 核心框架 -->
Spring Boot 2.7.x
Spring MVC
Spring Data JPA / MyBatis Plus

<!-- 数据库 -->
MySQL 8.0
Redis 6.x
HikariCP (连接池)

<!-- 工具库 -->
Lombok
Validation
Jackson
Swagger/OpenAPI 3
```

### 前端技术
```
Vue.js 3.x / React 18.x
Axios (HTTP客户端)
Element Plus / Ant Design
```

## 项目结构

```
project-name/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/project/
│   │   │       ├── ProjectApplication.java          // 启动类
│   │   │       ├── config/                          // 配置类
│   │   │       │   ├── WebConfig.java
│   │   │       │   ├── RedisConfig.java
│   │   │       │   └── SwaggerConfig.java
│   │   │       ├── controller/                      // 控制器
│   │   │       │   ├── UserController.java
│   │   │       │   └── BaseController.java
│   │   │       ├── service/                         // 服务层
│   │   │       │   ├── UserService.java
│   │   │       │   └── impl/
│   │   │       │       └── UserServiceImpl.java
│   │   │       ├── mapper/                          // 数据访问层
│   │   │       │   └── UserMapper.java
│   │   │       ├── entity/                          // 实体类
│   │   │       │   └── User.java
│   │   │       ├── dto/                             // 数据传输对象
│   │   │       │   ├── UserDTO.java
│   │   │       │   └── PageDTO.java
│   │   │       ├── vo/                              // 视图对象
│   │   │       │   └── UserVO.java
│   │   │       ├── common/                          // 公共类
│   │   │       │   ├── Result.java
│   │   │       │   ├── PageResult.java
│   │   │       │   └── GlobalExceptionHandler.java
│   │   │       └── utils/                           // 工具类
│   │   │           ├── JwtUtils.java
│   │   │           └── RedisUtils.java
│   │   └── resources/
│   │       ├── application.yml                      // 配置文件
│   │       ├── application-dev.yml                  // 开发环境配置
│   │       ├── application-prod.yml                 // 生产环境配置
│   │       ├── mapper/                              // MyBatis映射文件
│   │       │   └── UserMapper.xml
│   │       └── static/                              // 静态资源
│   └── test/                                        // 测试代码
├── frontend/                                        // 前端代码
├── docs/                                           // 项目文档
├── docker/                                         // Docker相关
├── pom.xml                                         // Maven配置
└── README.md                                       // 项目说明
```

## 核心代码模板

### 1. 启动类
```java
@SpringBootApplication
@EnableScheduling
@MapperScan("com.example.project.mapper")
public class ProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
    }
}
```

### 2. 实体类模板
```java
@Data
@Entity
@Table(name = "sys_user")
@ApiModel("用户实体")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("用户ID")
    private Long id;
    
    @NotBlank(message = "用户名不能为空")
    @Column(length = 50, unique = true)
    @ApiModelProperty("用户名")
    private String username;
    
    @NotBlank(message = "密码不能为空")
    @Column(length = 100)
    @ApiModelProperty("密码")
    private String password;
    
    @Email(message = "邮箱格式不正确")
    @Column(length = 100)
    @ApiModelProperty("邮箱")
    private String email;
    
    @CreationTimestamp
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    
    @UpdateTimestamp
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
```

### 3. 控制器模板
```java
@RestController
@RequestMapping("/api/users")
@Api(tags = "用户管理")
@Slf4j
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping
    @ApiOperation("创建用户")
    public Result<UserVO> createUser(@Valid @RequestBody UserDTO userDTO) {
        UserVO userVO = userService.createUser(userDTO);
        return Result.success(userVO);
    }
    
    @GetMapping("/{id}")
    @ApiOperation("根据ID查询用户")
    public Result<UserVO> getUserById(@PathVariable Long id) {
        UserVO userVO = userService.getUserById(id);
        return Result.success(userVO);
    }
    
    @GetMapping
    @ApiOperation("分页查询用户")
    public Result<PageResult<UserVO>> getUsers(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        PageResult<UserVO> result = userService.getUsers(page, size, keyword);
        return Result.success(result);
    }
    
    @PutMapping("/{id}")
    @ApiOperation("更新用户")
    public Result<UserVO> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        UserVO userVO = userService.updateUser(id, userDTO);
        return Result.success(userVO);
    }
    
    @DeleteMapping("/{id}")
    @ApiOperation("删除用户")
    public Result<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success();
    }
}
```

### 4. 统一返回结果
```java
@Data
@ApiModel("统一返回结果")
public class Result<T> {
    
    @ApiModelProperty("状态码")
    private Integer code;
    
    @ApiModelProperty("返回消息")
    private String message;
    
    @ApiModelProperty("返回数据")
    private T data;
    
    @ApiModelProperty("时间戳")
    private Long timestamp;
    
    public Result() {
        this.timestamp = System.currentTimeMillis();
    }
    
    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.code = 200;
        result.message = "操作成功";
        return result;
    }
    
    public static <T> Result<T> success(T data) {
        Result<T> result = success();
        result.data = data;
        return result;
    }
    
    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.code = 500;
        result.message = message;
        return result;
    }
    
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.code = code;
        result.message = message;
        return result;
    }
}
```

## 配置文件

### application.yml
```yaml
spring:
  profiles:
    active: dev
  
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/project_db?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD:123456}
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      connection-timeout: 30000
  
  redis:
    host: ${REDIS_HOST:localhost}
    port: ${REDIS_PORT:6379}
    password: ${REDIS_PASSWORD:}
    database: 0
    timeout: 5000ms
    lettuce:
      pool:
        max-active: 8
        max-idle: 8
        min-idle: 0
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true

mybatis-plus:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: com.example.project.entity
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl

logging:
  level:
    com.example.project: debug
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{50} - %msg%n"

springdoc:
  api-docs:
    path: /v3/api-docs
  swagger-ui:
    path: /swagger-ui.html
```

## 开发流程

### 1. 需求分析阶段
- [ ] 梳理功能需求
- [ ] 设计数据库表结构
- [ ] 定义API接口规范
- [ ] 制定开发计划

### 2. 环境搭建阶段
- [ ] 创建Spring Boot项目
- [ ] 配置数据库连接
- [ ] 集成相关依赖
- [ ] 编写基础配置类

### 3. 开发阶段
- [ ] 创建实体类和DTO
- [ ] 编写Mapper和Service
- [ ] 开发Controller层
- [ ] 编写单元测试

### 4. 测试阶段
- [ ] 单元测试
- [ ] 集成测试
- [ ] API测试
- [ ] 性能测试

### 5. 部署阶段
- [ ] 打包应用
- [ ] 部署到测试环境
- [ ] 部署到生产环境
- [ ] 监控和日志

## 学习要点
1. **分层架构设计**: Controller-Service-Mapper
2. **统一异常处理**: @ControllerAdvice
3. **参数校验**: @Valid注解
4. **API文档**: Swagger集成
5. **缓存使用**: Redis应用
6. **事务管理**: @Transactional

## 扩展功能
- [ ] JWT认证授权
- [ ] 文件上传下载
- [ ] 定时任务
- [ ] 消息队列集成
- [ ] 分布式锁
- [ ] 接口限流

---
**创建日期**: 2024-01-XX
**最后更新**: 2024-01-XX