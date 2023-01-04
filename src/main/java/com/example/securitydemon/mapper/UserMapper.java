package com.example.securitydemon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.securitydemon.entry.SecurityUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<SecurityUser> {
}
