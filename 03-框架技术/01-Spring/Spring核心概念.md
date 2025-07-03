# Spring核心概念

## 学习日期
2024-01-XX

## 学习目标
- [ ] 理解Spring框架的核心理念
- [ ] 掌握IoC容器和DI依赖注入
- [ ] 理解AOP面向切面编程
- [ ] 掌握Spring Bean的生命周期

## 核心概念

### Spring框架优势
- **轻量级**: 非侵入性框架
- **IoC容器**: 控制反转，降低耦合度
- **AOP支持**: 面向切面编程
- **事务管理**: 声明式事务
- **集成性强**: 易于与其他框架集成

### IoC控制反转
```
传统方式: 对象主动创建依赖
IoC方式: 容器负责创建和注入依赖
```

## 配置示例

### 1. 基于XML配置
```xml
<!-- applicationContext.xml -->
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd">
    
    <!-- 定义Bean -->
    <bean id="userDao" class="com.example.dao.UserDaoImpl"/>
    
    <!-- 依赖注入 -->
    <bean id="userService" class="com.example.service.UserServiceImpl">
        <property name="userDao" ref="userDao"/>
    </bean>
    
    <!-- 构造器注入 -->
    <bean id="orderService" class="com.example.service.OrderServiceImpl">
        <constructor-arg ref="userService"/>
        <constructor-arg value="defaultStatus"/>
    </bean>
</beans>
```

### 2. 基于注解配置
```java
// 配置类
@Configuration
@ComponentScan("com.example")
@EnableAspectJAutoProxy
public class AppConfig {
    
    @Bean
    public DataSource dataSource() {
        return new HikariDataSource();
    }
    
    @Bean
    public UserDao userDao() {
        return new UserDaoImpl();
    }
}

// 服务类
@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserDao userDao;
    
    // 或使用构造器注入（推荐）
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }
    
    @Override
    public User findById(Long id) {
        return userDao.findById(id);
    }
}

// DAO类
@Repository
public class UserDaoImpl implements UserDao {
    
    @Override
    public User findById(Long id) {
        // 数据库查询逻辑
        return new User();
    }
}

// 控制器
@RestController
@RequestMapping("/users")
public class UserController {
    
    private final UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.findById(id);
    }
}
```

### 3. AOP切面编程
```java
@Aspect
@Component
public class LoggingAspect {
    
    // 定义切点
    @Pointcut("execution(* com.example.service.*.*(..))")
    public void serviceLayer() {}
    
    // 前置通知
    @Before("serviceLayer()")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("调用方法: " + joinPoint.getSignature().getName());
    }
    
    // 后置通知
    @After("serviceLayer()")
    public void logAfter(JoinPoint joinPoint) {
        System.out.println("方法执行完成: " + joinPoint.getSignature().getName());
    }
    
    // 环绕通知
    @Around("serviceLayer()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        
        Object result = joinPoint.proceed();
        
        long endTime = System.currentTimeMillis();
        System.out.println("方法执行时间: " + (endTime - startTime) + "ms");
        
        return result;
    }
    
    // 异常通知
    @AfterThrowing(pointcut = "serviceLayer()", throwing = "ex")
    public void logException(JoinPoint joinPoint, Exception ex) {
        System.out.println("方法异常: " + joinPoint.getSignature().getName() + ", " + ex.getMessage());
    }
}
```

## Bean生命周期

```java
@Component
public class MyBean implements InitializingBean, DisposableBean {
    
    // 1. 构造器
    public MyBean() {
        System.out.println("1. 构造器执行");
    }
    
    // 2. 依赖注入
    @Autowired
    public void setDependency(SomeDependency dependency) {
        System.out.println("2. 依赖注入");
    }
    
    // 3. BeanNameAware
    @Override
    public void setBeanName(String name) {
        System.out.println("3. setBeanName: " + name);
    }
    
    // 4. @PostConstruct
    @PostConstruct
    public void init() {
        System.out.println("4. @PostConstruct初始化");
    }
    
    // 5. InitializingBean
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("5. afterPropertiesSet初始化");
    }
    
    // 6. @PreDestroy
    @PreDestroy
    public void cleanup() {
        System.out.println("6. @PreDestroy销毁");
    }
    
    // 7. DisposableBean
    @Override
    public void destroy() throws Exception {
        System.out.println("7. destroy销毁");
    }
}
```

## 重点难点
- **循环依赖**: Spring如何解决循环依赖问题
- **Bean作用域**: singleton, prototype, request, session
- **条件装配**: @Conditional注解的使用
- **事件机制**: ApplicationEvent和ApplicationListener

## 最佳实践
1. **构造器注入优于字段注入**
2. **使用@Configuration代替XML配置**
3. **合理使用@Lazy延迟初始化**
4. **避免过度使用AOP**
5. **正确处理事务边界**

## 实战项目
- [ ] [用户管理系统](../../05-项目实战/用户管理系统/README.md)
- [ ] [博客系统](../../05-项目实战/博客系统/README.md)

## 相关知识点
- [Spring Boot](./SpringBoot快速入门.md)
- [Spring MVC](./SpringMVC详解.md)
- [Spring Data JPA](./SpringDataJPA.md)
- [Spring Security](./SpringSecurity.md)

## 学习总结
<!-- 记录Spring学习心得，包括理解难点和实践经验 -->

## 参考资料
- Spring官方文档
- 《Spring实战》
- 《深入理解Spring》
- Spring源码分析

---
**标签**: #Spring #IoC #DI #AOP #框架
**难度**: ⭐⭐⭐⭐
**完成状态**: [ ] 学习中 [ ] 已完成 [ ] 需复习