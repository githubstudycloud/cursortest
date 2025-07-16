package com.example.sharding.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.sharding.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户信息Mapper
 * 
 * @author example
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {
    
}