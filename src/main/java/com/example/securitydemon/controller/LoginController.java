package com.example.securitydemon.controller;

import com.example.securitydemon.entry.SecurityUser;
import com.example.securitydemon.service.ILoginService;
import com.example.securitydemon.util.JWTUtils;
import com.example.securitydemon.util.ResultObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/login/")
public class LoginController {

    private final ILoginService loginService;

    public LoginController(ILoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/to-login")
    public ResultObject<?> login(@RequestBody  SecurityUser user, HttpServletResponse response){
        //将token存入Http的header中
        ResultObject<String> jwtRes = loginService.login(user);
        response.setHeader(JWTUtils.USER_LOGIN_TOKEN, jwtRes.getData());
        return jwtRes;
    }

}
