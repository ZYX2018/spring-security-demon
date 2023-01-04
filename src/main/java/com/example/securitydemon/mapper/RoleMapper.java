package com.example.securitydemon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.securitydemon.entry.SecurityRole;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoleMapper extends BaseMapper<SecurityRole> {
}
