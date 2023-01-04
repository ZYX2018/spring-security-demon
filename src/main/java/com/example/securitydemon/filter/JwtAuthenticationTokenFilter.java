package com.example.securitydemon.filter;

import com.alibaba.fastjson2.JSONObject;
import com.example.securitydemon.entry.SecurityUser;
import com.example.securitydemon.util.JWTUtils;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private final RedisTemplate redisTemplate;

    public JwtAuthenticationTokenFilter(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(JWTUtils.USER_LOGIN_TOKEN);
        String url = request.getRequestURI();
        if (url.endsWith("/login/to-login")){
            //放行
            filterChain.doFilter(request, response);
        }else {
            if (!StringUtils.hasLength(token)){
                throw new  Exception("尚未登录");
            }
            String userId = JWTUtils.validateToken(token);
            ValueOperations<String, JSONObject> operation = redisTemplate.opsForValue();
            SecurityUser user =  operation.get("token"+userId).toJavaObject(SecurityUser.class);
            if (!StringUtils.hasLength(userId)|| Objects.isNull(user)){
                throw new  Exception("token失效");
            }
            if (JWTUtils.isNeedUpdate(token)){
                String newToken = JWTUtils.createToken(userId);
                redisTemplate.opsForValue().getAndDelete("token"+userId);
                redisTemplate.opsForValue().set("token"+userId,newToken);
                response.setHeader(JWTUtils.USER_LOGIN_TOKEN, newToken);
            }

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword(),user.getGrantedAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            //放行
            filterChain.doFilter(request, response);
        }

    }
}
