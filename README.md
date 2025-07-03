# Java开发学习工作空间 ☕

这是一个完整的Java开发学习工作空间，包含项目模板、开发工具配置、学习资料和最佳实践。

## 🚀 快速开始

### 1. 环境设置
```bash
# 给脚本执行权限
chmod +x tools/scripts/*.sh

# 自动安装开发环境
./tools/scripts/setup-java-env.sh
```

### 2. 创建新项目
```bash
# 创建Maven项目
./tools/scripts/create-project.sh -t maven my-app

# 创建Spring Boot项目
./tools/scripts/create-project.sh -t spring-boot -g com.mycompany my-web-app

# 创建Gradle项目
./tools/scripts/create-project.sh -t gradle my-gradle-app
```

### 3. 开始编码
```bash
cd projects/my-app
mvn compile
mvn test
```

## 📁 目录结构

```
java-workspace/
├── projects/                    # 项目目录
│   ├── web-projects/           # Web应用项目
│   ├── desktop-projects/       # 桌面应用项目
│   ├── microservices/         # 微服务项目
│   └── spring-boot-projects/   # Spring Boot项目
│
├── templates/                   # 项目模板
│   ├── maven-template/         # Maven项目模板
│   │   ├── pom.xml
│   │   └── src/
│   ├── gradle-template/        # Gradle项目模板
│   │   ├── build.gradle
│   │   └── src/
│   └── spring-boot-template/   # Spring Boot模板
│       ├── pom.xml
│       └── src/
│
├── tools/                      # 开发工具
│   ├── ide-configs/           # IDE配置文件
│   │   └── .vscode/           # VS Code配置
│   ├── build-tools/           # 构建工具配置
│   └── scripts/              # 自动化脚本
│
├── learning/                   # 学习资料
│   ├── basics/               # Java基础
│   ├── advanced/             # 高级特性
│   ├── frameworks/           # 框架学习
│   └── design-patterns/      # 设计模式
│
├── docs/                      # 文档
│   ├── tutorials/            # 教程
│   ├── references/           # 参考资料
│   └── best-practices/       # 最佳实践
│
└── README.md                  # 本文件
```

## 🛠️ 开发工具和插件

### VS Code 推荐插件

#### Java开发核心插件
- **Extension Pack for Java** - Java开发必备插件包
- **Language Support for Java™ by Red Hat** - Java语言支持
- **Debugger for Java** - Java调试器
- **Test Runner for Java** - Java测试运行器
- **Maven for Java** - Maven支持
- **Gradle for Java** - Gradle支持

#### Spring Boot开发
- **Spring Boot Extension Pack** - Spring Boot完整支持
- **Spring Boot Dashboard** - Spring Boot控制面板
- **Spring Initializr Java Support** - 项目初始化

#### 代码质量工具
- **SonarLint** - 代码质量检查
- **Checkstyle for Java** - 代码风格检查
- **SpotBugs** - Bug检测工具

### IDEA 配置建议

#### 必装插件
- **Lombok** - 简化Java代码
- **SonarLint** - 代码质量
- **Alibaba Java Coding Guidelines** - 阿里巴巴代码规范
- **Translation** - 翻译插件
- **Rainbow Brackets** - 彩虹括号

## 📚 学习路径

### 基础阶段 (1-2个月)
1. **Java语法基础**
   - 变量和数据类型
   - 控制结构
   - 方法和类
   
2. **面向对象编程**
   - 封装、继承、多态
   - 抽象类和接口
   - 包和访问修饰符

3. **常用API**
   - String处理
   - 集合框架
   - 异常处理
   - IO操作

### 进阶阶段 (2-3个月)
1. **框架学习**
   - Spring Core
   - Spring MVC
   - Spring Boot
   - MyBatis/JPA

2. **数据库操作**
   - JDBC
   - 连接池
   - 事务管理

3. **Web开发**
   - HTTP协议
   - RESTful API
   - JSON处理

### 高级阶段 (3-6个月)
1. **微服务架构**
   - Spring Cloud
   - 服务注册与发现
   - 配置中心
   - 链路追踪

2. **性能优化**
   - JVM调优
   - 并发编程
   - 缓存策略

## 🔧 工具使用示例

### Maven使用
```bash
# 创建项目
mvn archetype:generate -DgroupId=com.example -DartifactId=my-app

# 编译项目
mvn compile

# 运行测试
mvn test

# 打包
mvn package

# 清理
mvn clean

# 运行Spring Boot应用
mvn spring-boot:run
```

### Gradle使用
```bash
# 构建项目
./gradlew build

# 运行测试
./gradlew test

# 运行应用
./gradlew bootRun

# 清理
./gradlew clean

# 查看依赖
./gradlew dependencies
```

### Git工作流
```bash
# 克隆项目
git clone <repository-url>

# 创建特性分支
git checkout -b feature/new-feature

# 提交更改
git add .
git commit -m "添加新功能"

# 推送分支
git push origin feature/new-feature

# 合并到主分支
git checkout main
git merge feature/new-feature
```

## 🐛 调试技巧

### 1. IDE调试
- 设置断点
- 单步执行
- 查看变量值
- 表达式求值

### 2. 日志调试
```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyClass {
    private static final Logger logger = LoggerFactory.getLogger(MyClass.class);
    
    public void myMethod() {
        logger.debug("方法开始执行");
        logger.info("重要信息: {}", value);
        logger.warn("警告信息");
        logger.error("错误信息", exception);
    }
}
```

### 3. 单元测试
```java
@Test
@DisplayName("测试加法运算")
void testAdd() {
    // Given
    Calculator calculator = new Calculator();
    
    // When
    int result = calculator.add(2, 3);
    
    // Then
    assertEquals(5, result);
}
```

## 📖 代码规范

### 命名规范
- **类名**: PascalCase (如: `UserService`)
- **方法名**: camelCase (如: `getUserById`)
- **变量名**: camelCase (如: `userName`)
- **常量名**: UPPER_SNAKE_CASE (如: `MAX_SIZE`)
- **包名**: lowercase (如: `com.example.service`)

### 注释规范
```java
/**
 * 用户服务类
 * 提供用户相关的业务操作
 * 
 * @author 开发者姓名
 * @version 1.0.0
 * @since 2024-01-01
 */
public class UserService {
    
    /**
     * 根据ID获取用户信息
     * 
     * @param id 用户ID
     * @return 用户信息，如果不存在返回null
     * @throws IllegalArgumentException 当ID为空时抛出
     */
    public User getUserById(String id) {
        // 实现代码
    }
}
```

## 🏗️ 项目结构建议

### Maven/Gradle项目结构
```
src/
├── main/
│   ├── java/                   # Java源代码
│   │   └── com/example/
│   │       ├── controller/     # 控制器
│   │       ├── service/        # 业务层
│   │       ├── repository/     # 数据访问层
│   │       ├── model/          # 数据模型
│   │       └── config/         # 配置类
│   └── resources/              # 资源文件
│       ├── application.yml     # 配置文件
│       ├── static/             # 静态资源
│       └── templates/          # 模板文件
└── test/
    └── java/                   # 测试代码
        └── com/example/
            ├── controller/     # 控制器测试
            ├── service/        # 业务层测试
            └── repository/     # 数据访问层测试
```

## 🚀 性能优化建议

### 1. JVM调优
```bash
# 设置堆内存大小
-Xms1g -Xmx2g

# 选择垃圾收集器
-XX:+UseG1GC

# 启用JIT编译优化
-XX:+UseCompressedOops
```

### 2. 代码优化
- 合理使用数据结构
- 避免不必要的对象创建
- 使用StringBuilder处理字符串拼接
- 正确处理异常
- 合理使用缓存

### 3. 数据库优化
- 使用索引
- 避免N+1查询
- 合理使用连接池
- 实施分页查询

## 🤝 贡献指南

### 提交代码流程
1. Fork项目
2. 创建特性分支
3. 编写代码和测试
4. 提交Pull Request

### 代码检查清单
- [ ] 代码编译无错误
- [ ] 所有测试通过
- [ ] 代码符合规范
- [ ] 包含必要的注释
- [ ] 更新了相关文档

## 📞 获取帮助

### 学习资源
- [Oracle Java官方文档](https://docs.oracle.com/javase/)
- [Spring官方文档](https://spring.io/guides)
- [Maven官方文档](https://maven.apache.org/guides/)
- [Gradle官方文档](https://docs.gradle.org/)

### 社区支持
- [Stack Overflow](https://stackoverflow.com/questions/tagged/java)
- [GitHub Issues](https://github.com/java/java-workspace/issues)
- [Spring Community](https://spring.io/community)

## 📝 更新日志

### Version 1.0.0 (2024-01-01)
- ✅ 初始化项目结构
- ✅ 添加Maven和Gradle模板
- ✅ 创建Spring Boot模板
- ✅ 配置VS Code开发环境
- ✅ 添加自动化脚本
- ✅ 创建学习资料和文档

---

**开始你的Java学习之旅吧！** 🚀

如果你觉得这个工作空间有帮助，请给我们一个 ⭐ Star！