package com.example.securitydemon.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
@Component
@Slf4j
public class UrlAccessDecisionManager implements AccessDecisionManager {
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        // 获取用户拥有的权限信息
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        // 这里判断用户拥有的角色和该url需要的角色是否有匹配
        for (ConfigAttribute configAttribute : configAttributes) {
            String attribute = configAttribute.getAttribute();
            for (GrantedAuthority authority : authorities) {
                if (attribute.equals(authority.getAuthority())) {
                    log.info("匹配成功.");
                    return;
                }
            }
        }
        // 没有匹配就抛出异常
        throw new AccessDeniedException("权限不足,无法访问");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
