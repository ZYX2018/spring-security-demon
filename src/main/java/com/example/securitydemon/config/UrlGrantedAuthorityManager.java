package com.example.securitydemon.config;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.securitydemon.entry.UserGrantedAuthority;
import com.example.securitydemon.mapper.UserGrantedAuthorityMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class UrlGrantedAuthorityManager implements FilterInvocationSecurityMetadataSource {

    private final UserGrantedAuthorityMapper userGrantedAuthorityMapper;

    public UrlGrantedAuthorityManager(UserGrantedAuthorityMapper userGrantedAuthorityMapper) {
        this.userGrantedAuthorityMapper = userGrantedAuthorityMapper;
    }

    /**
     * 判断访问的url需要哪些权限
     * @param object the object being secured
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {

        // 获取url
        FilterInvocation filterInvocation = (FilterInvocation) object;
        String requestUrl = filterInvocation.getRequestUrl();
        // 获取拥有url需要的权限
        List<UserGrantedAuthority> userGrantedAuthorities = userGrantedAuthorityMapper.selectList(Wrappers.<UserGrantedAuthority>lambdaQuery().ne(UserGrantedAuthority::getAuthority,"/login/to-login"));
        List<String> grantedAuthorities = userGrantedAuthorities.stream().map(UserGrantedAuthority::getAuthority).filter(val->val.equals(requestUrl)).collect(Collectors.toList());
        log.info("{} 对应的角色。{}",requestUrl,grantedAuthorities);
        if (CollectionUtils.isEmpty(grantedAuthorities)) {
            return null;
        }
        // 自定义角色信息 --> Security的权限格式
        String[] attributes = grantedAuthorities.toArray(new String[0]);
        return SecurityConfig.createList(attributes);
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }
  // 是否能为 Class 提供 Collection<ConfigAttribute>
    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
