package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Calculator类的单元测试
 * 演示JUnit 5测试框架的使用
 * 
 * @author developer
 * @version 1.0.0
 */
@DisplayName("计算器测试")
class CalculatorTest {
    
    private Calculator calculator;
    
    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }
    
    @Test
    @DisplayName("测试加法运算")
    void testAdd() {
        // Given
        int a = 5;
        int b = 3;
        
        // When
        int result = calculator.add(a, b);
        
        // Then
        assertEquals(8, result, "5 + 3 应该等于 8");
    }
    
    @Test
    @DisplayName("测试减法运算")
    void testSubtract() {
        assertEquals(2, calculator.subtract(5, 3));
        assertEquals(-2, calculator.subtract(3, 5));
    }
    
    @ParameterizedTest
    @DisplayName("参数化测试乘法运算")
    @CsvSource({
        "0, 0, 0",
        "1, 1, 1", 
        "2, 3, 6",
        "-2, 3, -6",
        "2, -3, -6"
    })
    void testMultiply(int a, int b, int expected) {
        assertEquals(expected, calculator.multiply(a, b));
    }
    
    @Test
    @DisplayName("测试除法运算")
    void testDivide() {
        assertEquals(2.0, calculator.divide(6, 3), 0.001);
        assertEquals(1.5, calculator.divide(3, 2), 0.001);
    }
    
    @Test
    @DisplayName("测试除零异常")
    void testDivideByZero() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> calculator.divide(5, 0)
        );
        assertEquals("除数不能为零", exception.getMessage());
    }
}