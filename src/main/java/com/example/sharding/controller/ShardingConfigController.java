package com.example.sharding.controller;

import com.example.sharding.entity.ShardingConfig;
import com.example.sharding.service.ShardingConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分片配置控制器
 * 
 * @author example
 */
@Slf4j
@RestController
@RequestMapping("/api/sharding-config")
public class ShardingConfigController {
    
    @Autowired
    private ShardingConfigService shardingConfigService;
    
    /**
     * 创建分片配置
     */
    @PostMapping
    public ResponseEntity<String> createConfig(@RequestParam String projectId,
                                             @RequestParam String databaseSuffix,
                                             @RequestParam String tableSuffix) {
        try {
            log.info("创建分片配置 - 项目ID: {}, 数据库后缀: {}, 表后缀: {}", 
                    projectId, databaseSuffix, tableSuffix);
            
            boolean result = shardingConfigService.createConfig(projectId, databaseSuffix, tableSuffix);
            
            if (result) {
                return ResponseEntity.ok("分片配置创建成功");
            } else {
                return ResponseEntity.badRequest().body("分片配置创建失败");
            }
        } catch (Exception e) {
            log.error("创建分片配置失败", e);
            return ResponseEntity.internalServerError().body("创建失败: " + e.getMessage());
        }
    }
    
    /**
     * 查询分片配置
     */
    @GetMapping("/{projectId}")
    public ResponseEntity<ShardingConfig> getConfig(@PathVariable String projectId) {
        try {
            log.info("查询分片配置 - 项目ID: {}", projectId);
            
            ShardingConfig config = shardingConfigService.getConfigByProjectId(projectId);
            if (config != null) {
                return ResponseEntity.ok(config);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("查询分片配置失败", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * 查询所有分片配置
     */
    @GetMapping
    public ResponseEntity<List<ShardingConfig>> listConfigs() {
        try {
            log.info("查询所有分片配置");
            
            List<ShardingConfig> configs = shardingConfigService.list();
            return ResponseEntity.ok(configs);
            
        } catch (Exception e) {
            log.error("查询所有分片配置失败", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * 更新分片配置
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateConfig(@PathVariable Long id,
                                             @RequestBody ShardingConfig config) {
        try {
            log.info("更新分片配置 - ID: {}", id);
            
            config.setId(id);
            boolean result = shardingConfigService.updateById(config);
            
            if (result) {
                return ResponseEntity.ok("分片配置更新成功");
            } else {
                return ResponseEntity.badRequest().body("分片配置更新失败");
            }
        } catch (Exception e) {
            log.error("更新分片配置失败", e);
            return ResponseEntity.internalServerError().body("更新失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除分片配置
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteConfig(@PathVariable Long id) {
        try {
            log.info("删除分片配置 - ID: {}", id);
            
            boolean result = shardingConfigService.removeById(id);
            
            if (result) {
                return ResponseEntity.ok("分片配置删除成功");
            } else {
                return ResponseEntity.badRequest().body("分片配置删除失败");
            }
        } catch (Exception e) {
            log.error("删除分片配置失败", e);
            return ResponseEntity.internalServerError().body("删除失败: " + e.getMessage());
        }
    }
}