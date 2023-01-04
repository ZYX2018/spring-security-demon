package com.example.securitydemon.config;

import com.alibaba.fastjson2.JSONObject;
import com.example.securitydemon.filter.JwtAuthenticationTokenFilter;
import com.example.securitydemon.util.ResultObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.xml.transform.Result;
import java.io.PrintWriter;

@Configuration
@EnableWebSecurity
public class MySecurityConfig {

    private final JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    private final UrlAccessDecisionManager urlAccessDecisionManager;
    private final UrlGrantedAuthorityManager urlGrantedAuthorityManager;

    public MySecurityConfig(JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter, UrlAccessDecisionManager urlAccessDecisionManager, UrlGrantedAuthorityManager urlGrantedAuthorityManager) {
        this.jwtAuthenticationTokenFilter = jwtAuthenticationTokenFilter;
        this.urlAccessDecisionManager = urlAccessDecisionManager;
        this.urlGrantedAuthorityManager = urlGrantedAuthorityManager;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) ->
                    web.ignoring()
                            .antMatchers("/css/**")
                            .antMatchers("/404.html")
                            .antMatchers("/500.html")
                            .antMatchers("/html/**")
                            .antMatchers("/js/**");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 关闭 session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        //动态权限
        http.authorizeRequests().withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
            @Override
            public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                object.setSecurityMetadataSource(urlGrantedAuthorityManager);
                object.setAccessDecisionManager(urlAccessDecisionManager);
                return object;
            }
        })
                .antMatchers("/login/to-login").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .csrf().disable()
        ;
        //认证异常处理
        http.exceptionHandling().authenticationEntryPoint(((request, response, authException) -> {
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write(JSONObject.toJSONString(ResultObject.fail("认证失败"+authException.getMessage(),200)));
            out.flush();
            out.close();
        }));
        //鉴权失败
        http.exceptionHandling().accessDeniedHandler(((request, response, accessDeniedException) -> {
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write(JSONObject.toJSONString(ResultObject.fail("鉴权失败"+accessDeniedException.getMessage(),200)));
            out.flush();
            out.close();
        }));
        return http.build();
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("password")
//                .roles("USER")
//                .build();
//        UserDetails admin = User.withDefaultPasswordEncoder()
//                .username("admin")
//                .password("password")
//                .roles("ADMIN", "USER")
//                .build();
//        return new InMemoryUserDetailsManager(user, admin);
//    }
}
