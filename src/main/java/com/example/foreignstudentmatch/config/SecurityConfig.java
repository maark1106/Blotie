package com.example.foreignstudentmatch.config;

import com.example.foreignstudentmatch.repository.RefreshTokenRepository;
import com.example.foreignstudentmatch.security.CustomUserDetailService;
import com.example.foreignstudentmatch.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailService customUserDetailService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/auth").permitAll()
                .requestMatchers("/login").permitAll()
//                .requestMatchers("/api/**").permitAll()
//                .requestMatchers("/api/feed/save").permitAll()
//                .requestMatchers("/api/feed").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // 세션을 사용하지 않음

        // JWT 필터 추가
        http.addFilterBefore(new JwtAuthenticationFilter(customUserDetailService, refreshTokenRepository), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}