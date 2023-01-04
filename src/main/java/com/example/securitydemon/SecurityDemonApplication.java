package com.example.securitydemon;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan({"com.example.securitydemon.mapper","com.gitee.sunchenbin.mybatis.actable.dao.*"})
@ComponentScan({"com.gitee.sunchenbin.mybatis.actable.manager.*","com.example.securitydemon"})
public class SecurityDemonApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityDemonApplication.class, args);
    }

}
