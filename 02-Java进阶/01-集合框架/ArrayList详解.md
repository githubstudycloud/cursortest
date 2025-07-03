# ArrayList详解

## 学习日期
2024-01-XX

## 学习目标
- [ ] 理解ArrayList的内部实现原理
- [ ] 掌握ArrayList的常用方法
- [ ] 了解ArrayList的性能特点
- [ ] 掌握ArrayList的使用场景

## 核心概念

### ArrayList特点
- **动态数组**: 可自动扩容
- **有序集合**: 保持插入顺序
- **允许重复**: 可存储重复元素
- **非线程安全**: 多线程需要同步

### 内部实现
- 基于数组实现
- 默认初始容量：10
- 扩容机制：新容量 = 旧容量 * 1.5

## 代码示例

```java
import java.util.*;

public class ArrayListExample {
    public static void main(String[] args) {
        // 创建ArrayList
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> listWithCapacity = new ArrayList<>(20);
        ArrayList<String> listFromCollection = new ArrayList<>(Arrays.asList("a", "b", "c"));
        
        // 添加元素
        list.add("Java");
        list.add("Python");
        list.add("JavaScript");
        list.add(1, "C++"); // 在指定位置插入
        
        // 访问元素
        System.out.println("第一个元素: " + list.get(0));
        System.out.println("列表大小: " + list.size());
        
        // 修改元素
        list.set(0, "Kotlin");
        
        // 删除元素
        list.remove("Python");          // 按值删除
        list.remove(0);                 // 按索引删除
        
        // 查找元素
        boolean contains = list.contains("Java");
        int index = list.indexOf("JavaScript");
        
        // 遍历元素
        // 方式1：for循环
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
        
        // 方式2：增强for循环
        for (String item : list) {
            System.out.println(item);
        }
        
        // 方式3：迭代器
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
        
        // 方式4：Stream API
        list.stream().forEach(System.out::println);
        
        // 批量操作
        List<String> newList = Arrays.asList("Go", "Rust");
        list.addAll(newList);           // 添加集合
        list.removeAll(newList);        // 删除集合中的元素
        
        // 转换为数组
        String[] array = list.toArray(new String[0]);
        
        // 清空列表
        list.clear();
        
        System.out.println("列表是否为空: " + list.isEmpty());
    }
}
```

## 性能分析

| 操作 | 时间复杂度 | 说明 |
|------|------------|------|
| get(index) | O(1) | 随机访问 |
| add(element) | O(1)摊还 | 尾部添加，可能触发扩容 |
| add(index, element) | O(n) | 中间插入需要移动元素 |
| remove(index) | O(n) | 删除后需要移动元素 |
| contains(element) | O(n) | 线性搜索 |
| size() | O(1) | 直接返回size字段 |

## 使用场景
✅ **适用场景**:
- 频繁随机访问元素
- 主要在尾部添加/删除元素
- 需要保持插入顺序
- 元素数量相对稳定

❌ **不适用场景**:
- 频繁在中间插入/删除元素
- 多线程并发访问
- 需要固定大小的数组

## 最佳实践

```java
// 1. 预估容量，避免频繁扩容
ArrayList<String> list = new ArrayList<>(100);

// 2. 使用泛型，确保类型安全
ArrayList<Integer> numbers = new ArrayList<>();

// 3. 批量操作优于逐个操作
list.addAll(Arrays.asList("a", "b", "c"));

// 4. 使用迭代器进行安全删除
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    if (it.next().equals("target")) {
        it.remove();  // 安全删除
    }
}

// 5. 及时释放引用
list.clear();  // 而不是 list = null;
```

## 常见问题
1. **ConcurrentModificationException**: 遍历时修改集合
2. **IndexOutOfBoundsException**: 索引越界
3. **内存泄漏**: 持有不必要的对象引用

## 相关知识点
- [Vector vs ArrayList](./Vector对比.md)
- [LinkedList vs ArrayList](./LinkedList对比.md)
- [Collections工具类](./Collections工具类.md)
- [泛型详解](../泛型/泛型详解.md)

## 学习总结
<!-- 记录ArrayList学习心得，包括使用技巧和注意事项 -->

## 参考资料
- Java源码分析
- 《Java集合框架详解》
- ArrayList官方文档

---
**标签**: #集合框架 #ArrayList #动态数组 #数据结构
**难度**: ⭐⭐⭐
**完成状态**: [ ] 学习中 [ ] 已完成 [ ] 需复习