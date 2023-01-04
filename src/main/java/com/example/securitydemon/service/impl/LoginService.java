package com.example.securitydemon.service.impl;

import com.example.securitydemon.entry.SecurityUser;
import com.example.securitydemon.service.ILoginService;
import com.example.securitydemon.util.JWTUtils;
import com.example.securitydemon.util.ResultObject;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class LoginService implements ILoginService {

    private final AuthenticationProvider authenticationProvider;

    private final RedisTemplate redisTemplate;

    public LoginService(AuthenticationProvider authenticationProvider, RedisTemplate redisTemplate) {
        this.authenticationProvider = authenticationProvider;
        this.redisTemplate = redisTemplate;
    }


    @Override
    public ResultObject<String> login(SecurityUser user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword());
        Authentication authenticate = authenticationProvider.authenticate(authenticationToken);
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
     //   Object obj =  authenticate.getPrincipal();
        //使用userid生成token
        SecurityUser loginUser = (SecurityUser) authenticate.getPrincipal();
        String userId = loginUser.getId();
        String jwt = JWTUtils.createToken(userId);
        //authenticate存入redis
        redisTemplate.opsForValue().set("token"+userId,loginUser);
        // redisCache.setCacheObject("login:"+userId,loginUser);
        //把token响应给前端
//        HashMap<String,String> map = new HashMap<>();
//        map.put("token",jwt);
        return ResultObject.success(jwt);

    }
}
