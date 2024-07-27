package com.example.foreignstudentmatch.common;

import com.example.foreignstudentmatch.security.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@Slf4j
@Component
@RequiredArgsConstructor
public class StudentIdInterceptor implements HandlerInterceptor {

    private final String secretKey = "my-secret-key-123123";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                Claims claims = JwtTokenUtil.extractClaims(token, secretKey);
                Long studentId = Long.valueOf(claims.get("studentId").toString());
                request.setAttribute("STUDENT_ID", studentId);
            } catch (ExpiredJwtException e) {
                // 액세스 토큰이 만료된 경우 처리
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰이 만료되었습니다.");
                return false;
            } catch (Exception e) {
                // 기타 예외 처리
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 토큰입니다.");
                return false;
            }
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰이 필요합니다.");
            return false;
        }
        return true;
    }
}