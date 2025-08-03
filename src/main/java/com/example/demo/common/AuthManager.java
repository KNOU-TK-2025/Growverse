package com.example.demo.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthManager implements AuthenticationManager {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        // ✅ 여기서 당신이 직접 로그인 검증
        if (isValidUser(username, password)) {
            // 로그인 성공 → 인증된 Authentication 반환
            return new UsernamePasswordAuthenticationToken(username, password, List.of());
        } else {
            // 로그인 실패 → 예외 발생
            throw new BadCredentialsException("Invalid credentials");
        }
    }

    private boolean isValidUser(String username, String password) {
        // 여기에 로그인 검증 로직 (DB, 외부 API, etc)
        return true;
    }
}