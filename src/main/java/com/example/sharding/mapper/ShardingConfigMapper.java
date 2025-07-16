package com.example.sharding.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.sharding.entity.ShardingConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 分片配置Mapper
 * 
 * @author example
 */
@Mapper
public interface ShardingConfigMapper extends BaseMapper<ShardingConfig> {
    
    /**
     * 根据项目ID查询配置
     */
    @Select("SELECT * FROM sharding_config WHERE project_id = #{projectId} AND deleted = 0")
    ShardingConfig selectByProjectId(@Param("projectId") String projectId);
}