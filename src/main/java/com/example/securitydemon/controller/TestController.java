package com.example.securitydemon.controller;

import com.example.securitydemon.entry.SecurityUser;
import com.example.securitydemon.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/test/")
public class TestController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

        @RequestMapping("hello-word")
    public String helloWord(){
        List<SecurityUser> userList = userMapper.selectList(null);
        userList.forEach( user ->redisTemplate.opsForValue().set("security",user));
        return Objects.requireNonNull(redisTemplate.opsForValue().get("security")).toString();
    }


}
