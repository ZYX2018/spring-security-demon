package com.example.securitydemon;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.example.securitydemon.entry.RRoleGrantedAuthority;
import com.example.securitydemon.entry.SecurityRole;
import com.example.securitydemon.entry.SecurityUser;
import com.example.securitydemon.entry.UserGrantedAuthority;
import com.example.securitydemon.mapper.*;
import org.apache.ibatis.javassist.util.HotSwapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
class SecurityDemonApplicationTests {

    @Autowired
    private  UserMapper userMapper;
    @Autowired
    private  RoleMapper roleMapper;
    @Autowired
    private  UserGrantedAuthorityMapper userGrantedAuthorityMapper;
    @Autowired
    private  RUserGrantedAuthorityMapper rUserGrantedAuthorityMapper;
    @Autowired
    private  RRoleGrantedAuthorityMapper rRoleGrantedAuthorityMapper;

    @Autowired
    WebApplicationContext applicationContext;

    private final String passwordPrefix = "{bcrypt}";


    @Test
    public void  getUrls(){
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        //获取url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        map.forEach((key,val)->{
            //获取url的Set集合，一个方法可能对应多个url
            String methodName = val.getMethod().getDeclaringClass().getName()+"."+val.getMethod().getName();
            if (methodName.startsWith("com.example.securitydemon")){
                Set<String> patterns = key.getDirectPaths();
                System.out.println(methodName+"->"+patterns);
                UserGrantedAuthority userGrantedAuthority = userGrantedAuthorityMapper.selectOne(Wrappers.<UserGrantedAuthority>lambdaQuery().eq(UserGrantedAuthority::getAuthorityClassPath,methodName));
                if (Objects.isNull(userGrantedAuthority)){
                    userGrantedAuthority = new UserGrantedAuthority();
                    userGrantedAuthority.setAuthority(String.join(",", patterns));
                    userGrantedAuthority.setAuthorityClassPath(methodName);
                    userGrantedAuthority.setCreateTime(new Date());
                    userGrantedAuthority.setUpdateTime(new Date());
                    userGrantedAuthorityMapper.insert(userGrantedAuthority);
                }else {
                    if (!methodName.equals(userGrantedAuthority.getAuthorityClassPath())){
                        userGrantedAuthority.setAuthority(String.join(",", patterns));
                        userGrantedAuthorityMapper.update(userGrantedAuthority,null);
                    }
                }
            }

        });
//        List<String> urlList = new ArrayList<>();
//        for (RequestMappingInfo info : map.keySet()){
//            //获取url的Set集合，一个方法可能对应多个url
//            Set<String> patterns = info.getPatternsCondition().getPatterns();
//            for (String url : patterns){
//                urlList.add(url);
//            }
//        }
    }



    @Test
    void contextLoads() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        SecurityRole role = new SecurityRole();
        role.setRoleName("管理员");
        role.setRoleCode("ADMIN");
        role.setDescription("系统管理员");
        role.setCreateTime(new Date());
        role.setUpdateTime(new Date());
        roleMapper.insert(role);

        SecurityUser user = new SecurityUser();
        user.setUsername("超级管理员");
        user.setPassword(passwordEncoder.encode("1"));
        user.setUserSex("男");
        user.setRoleId(role.getId());
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        userMapper.insert(user);

        UserGrantedAuthority userGrantedAuthority = new UserGrantedAuthority();
        userGrantedAuthority.setAuthority("test");
        userGrantedAuthorityMapper.insert(userGrantedAuthority);

        RRoleGrantedAuthority rRoleGrantedAuthority = new RRoleGrantedAuthority();
        rRoleGrantedAuthority.setAuthorityId(userGrantedAuthority.getId());
        rRoleGrantedAuthority.setRoleId(role.getId());
        rRoleGrantedAuthorityMapper.insert(rRoleGrantedAuthority);

        userGrantedAuthority = new UserGrantedAuthority();
        userGrantedAuthority.setAuthority("admin");
        userGrantedAuthorityMapper.insert(userGrantedAuthority);
        rRoleGrantedAuthority = new RRoleGrantedAuthority();
        rRoleGrantedAuthority.setAuthorityId(userGrantedAuthority.getId());
        rRoleGrantedAuthority.setRoleId(role.getId());
        rRoleGrantedAuthorityMapper.insert(rRoleGrantedAuthority);


    }

    @Test
    void updateUser(){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        SecurityUser user = userMapper.selectById("1610178365751496705");
        user.setPassword(passwordPrefix+passwordEncoder.encode("1"));
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setAccountNonLocked(true);
        userMapper.updateById(user);
    }
@Test
void query(){
        List<UserGrantedAuthority> userGrantedAuthorities = userGrantedAuthorityMapper.selectList(Wrappers.<UserGrantedAuthority>lambdaQuery().ne(UserGrantedAuthority::getAuthority,"/login/to-login"));
    userGrantedAuthorities.forEach(System.out::println);
    }

}
