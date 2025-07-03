package com.example.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.HashMap;

/**
 * Hello世界REST控制器
 * 演示Spring Boot Web开发基础
 * 
 * @author developer
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class HelloController {
    
    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);
    
    /**
     * 简单的Hello接口
     * 
     * @return 问候消息
     */
    @GetMapping("/hello")
    public ResponseEntity<Map<String, String>> hello() {
        logger.info("收到Hello请求");
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Hello, World!");
        response.put("timestamp", java.time.Instant.now().toString());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 带参数的问候接口
     * 
     * @param name 姓名参数
     * @return 个性化问候消息
     */
    @GetMapping("/hello/{name}")
    public ResponseEntity<Map<String, String>> helloWithName(@PathVariable String name) {
        logger.info("收到带姓名的Hello请求: {}", name);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", String.format("Hello, %s!", name));
        response.put("timestamp", java.time.Instant.now().toString());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * POST请求示例
     * 
     * @param request 请求体
     * @return 响应消息
     */
    @PostMapping("/greet")
    public ResponseEntity<Map<String, String>> greet(@RequestBody Map<String, String> request) {
        String name = request.getOrDefault("name", "Anonymous");
        logger.info("收到问候请求，姓名: {}", name);
        
        Map<String, String> response = new HashMap<>();
        response.put("greeting", String.format("Nice to meet you, %s!", name));
        response.put("timestamp", java.time.Instant.now().toString());
        
        return ResponseEntity.ok(response);
    }
}