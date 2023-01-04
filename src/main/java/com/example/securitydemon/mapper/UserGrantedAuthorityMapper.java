package com.example.securitydemon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.securitydemon.entry.UserGrantedAuthority;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserGrantedAuthorityMapper extends BaseMapper<UserGrantedAuthority> {
}
