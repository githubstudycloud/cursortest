package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 应用程序入口类
 * 
 * @author developer
 * @version 1.0.0
 */
public class Main {
    
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    
    public static void main(String[] args) {
        logger.info("应用程序启动...");
        
        Main app = new Main();
        app.run();
        
        logger.info("应用程序结束。");
    }
    
    public void run() {
        logger.info("执行主要业务逻辑");
        
        // 示例业务逻辑
        Calculator calculator = new Calculator();
        int result = calculator.add(5, 3);
        
        logger.info("计算结果: 5 + 3 = {}", result);
    }
}