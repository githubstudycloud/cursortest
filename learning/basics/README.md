# Java基础学习指南

这个目录包含Java编程基础的学习资料和练习。

## 📚 学习内容

### 1. Java语言基础

#### 基本语法
- 变量和数据类型
- 运算符
- 控制结构（if/else, switch, 循环）
- 方法定义和调用

#### 面向对象编程
- 类和对象
- 封装、继承、多态
- 抽象类和接口
- 包和访问修饰符

#### 常用类库
- String和StringBuilder
- 集合框架（List, Set, Map）
- 日期时间API
- IO操作

### 2. 练习项目

#### 计算器项目
位置: `templates/maven-template/src/main/java/com/example/Calculator.java`

这是一个简单的计算器实现，包含：
- 基本数学运算
- 异常处理
- 单元测试

**学习要点:**
- 方法的定义和调用
- 参数传递
- 异常处理机制
- JUnit测试框架使用

#### 学生管理系统
一个简单的学生信息管理系统，练习：
- 类的设计
- 集合的使用
- 文件IO操作
- 简单的CRUD操作

### 3. 编程练习

#### 每日编程题
建议每天完成1-2个编程练习：

1. **数组操作**
   - 数组排序
   - 查找最大/最小值
   - 数组去重

2. **字符串处理**
   - 字符串反转
   - 回文检测
   - 字符统计

3. **面向对象练习**
   - 银行账户系统
   - 图书管理系统
   - 员工管理系统

### 4. 学习资源

#### 在线资源
- [Oracle官方Java教程](https://docs.oracle.com/javase/tutorial/)
- [Java代码规范](https://www.oracle.com/java/technologies/javase/codeconventions-introduction.html)
- [菜鸟教程Java](https://www.runoob.com/java/java-tutorial.html)

#### 推荐书籍
- 《Java核心技术》- Cay S. Horstmann
- 《Effective Java》- Joshua Bloch
- 《Java编程思想》- Bruce Eckel

### 5. 学习计划

#### 第1周：基础语法
- [x] 环境搭建
- [ ] 变量和数据类型
- [ ] 运算符和表达式
- [ ] 控制结构

#### 第2周：面向对象
- [ ] 类和对象
- [ ] 构造方法
- [ ] 封装性
- [ ] 继承

#### 第3周：高级特性
- [ ] 多态性
- [ ] 抽象类和接口
- [ ] 异常处理
- [ ] 集合框架

#### 第4周：实战项目
- [ ] 完成计算器项目
- [ ] 学生管理系统
- [ ] 代码review和优化

### 6. 练习检查清单

每完成一个练习，检查以下内容：

- [ ] 代码编译无错误
- [ ] 程序运行正确
- [ ] 代码格式规范
- [ ] 包含必要的注释
- [ ] 编写了单元测试
- [ ] 异常处理得当

### 7. 常见问题

#### Q: 如何运行Java程序？
```bash
# 编译
javac MyClass.java

# 运行
java MyClass
```

#### Q: 如何使用Maven运行项目？
```bash
# 编译
mvn compile

# 运行测试
mvn test

# 运行主程序
mvn exec:java -Dexec.mainClass="com.example.Main"
```

#### Q: 如何调试Java程序？
- 使用IDE的调试功能
- 添加断点
- 查看变量值
- 单步执行

---

记住：**编程是一门实践性很强的技能，多写代码，多练习！** 🚀