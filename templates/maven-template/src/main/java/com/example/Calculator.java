package com.example;

/**
 * 简单计算器类
 * 用于演示基本的Java编程概念
 * 
 * @author developer
 * @version 1.0.0
 */
public class Calculator {
    
    /**
     * 两个整数相加
     * 
     * @param a 第一个数
     * @param b 第二个数
     * @return 两数之和
     */
    public int add(int a, int b) {
        return a + b;
    }
    
    /**
     * 两个整数相减
     * 
     * @param a 被减数
     * @param b 减数
     * @return 差值
     */
    public int subtract(int a, int b) {
        return a - b;
    }
    
    /**
     * 两个整数相乘
     * 
     * @param a 第一个数
     * @param b 第二个数
     * @return 乘积
     */
    public int multiply(int a, int b) {
        return a * b;
    }
    
    /**
     * 两个数相除
     * 
     * @param a 被除数
     * @param b 除数
     * @return 商
     * @throws IllegalArgumentException 当除数为0时抛出
     */
    public double divide(double a, double b) {
        if (b == 0) {
            throw new IllegalArgumentException("除数不能为零");
        }
        return a / b;
    }
}