package com.example.securitydemon.service;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.example.securitydemon.entry.RRoleGrantedAuthority;
import com.example.securitydemon.entry.RUserGrantedAuthority;
import com.example.securitydemon.entry.SecurityUser;
import com.example.securitydemon.entry.UserGrantedAuthority;
import com.example.securitydemon.mapper.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;

    private final RoleMapper roleMapper;

    private final UserGrantedAuthorityMapper userGrantedAuthorityMapper;

    private final RUserGrantedAuthorityMapper rUserGrantedAuthorityMapper;

    private final RRoleGrantedAuthorityMapper rRoleGrantedAuthorityMapper;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();



    public UserDetailsServiceImpl(UserMapper userMapper, RoleMapper roleMapper, UserGrantedAuthorityMapper userGrantedAuthorityMapper, RUserGrantedAuthorityMapper rUserGrantedAuthorityMapper, RRoleGrantedAuthorityMapper rRoleGrantedAuthorityMapper) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.userGrantedAuthorityMapper = userGrantedAuthorityMapper;
        this.rUserGrantedAuthorityMapper = rUserGrantedAuthorityMapper;
        this.rRoleGrantedAuthorityMapper = rRoleGrantedAuthorityMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SecurityUser user = new LambdaQueryChainWrapper<>(userMapper).eq(SecurityUser::getUsername,username).one();
        List<RUserGrantedAuthority> rUserGrantedAuthorities = new LambdaQueryChainWrapper<>(rUserGrantedAuthorityMapper).eq(RUserGrantedAuthority::getUserId,user.getUsername()).list();
        if (CollectionUtils.isEmpty(rUserGrantedAuthorities)){
            List<RRoleGrantedAuthority> roleGrantedAuthorities = new LambdaQueryChainWrapper<>(rRoleGrantedAuthorityMapper).eq(RRoleGrantedAuthority::getRoleId,user.getRoleId()).list();
            user.setGrantedAuthorities(new LambdaQueryChainWrapper<>(userGrantedAuthorityMapper).in(UserGrantedAuthority::getId,roleGrantedAuthorities.stream().map(RRoleGrantedAuthority::getAuthorityId).collect(Collectors.toList())).list());
        }else {
            user.setGrantedAuthorities(new LambdaQueryChainWrapper<>(userGrantedAuthorityMapper).in(UserGrantedAuthority::getId,rUserGrantedAuthorities.stream().map(RUserGrantedAuthority::getAuthorityId).collect(Collectors.toList())).list());
        }
    //    user.setPassword(passwordPrefix+user.getPassword());
        return user;
    }
}
