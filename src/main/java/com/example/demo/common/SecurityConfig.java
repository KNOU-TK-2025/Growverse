package com.example.demo.common;

import com.example.demo.common.dao.DaoPost;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private AuthManager authManager;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authenticationManager(authManager)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/static/css/**", "/static/js/**").permitAll()  // 로그인은 누구나 접근 가능
                        .anyRequest().authenticated()  // 그 외는 로그인 필요
                )
                .formLogin(form -> form
                        .loginPage("/login")           // 커스텀 로그인 페이지 URL
                        .defaultSuccessUrl("/")        // 로그인 성공 시 이동할 기본 페이지
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }
}