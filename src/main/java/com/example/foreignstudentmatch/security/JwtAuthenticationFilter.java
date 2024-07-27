package com.example.foreignstudentmatch.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final CustomUserDetailService userDetailService;
    private final String secretKey = "my-secret-key-123123";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String jwt = null;
        String studentId = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            try {
                Claims claims = JwtTokenUtil.extractClaims(jwt, secretKey);
                studentId = claims.getSubject();
                request.setAttribute("STUDENT_ID", Long.valueOf(studentId));
            } catch (ExpiredJwtException e) {
                // 액세스 토큰이 만료된 경우 리프레시 토큰을 사용하여 새로운 액세스 토큰을 발급
                String refreshToken = request.getHeader("RefreshToken");
                if (refreshToken != null && !JwtTokenUtil.isExpired(refreshToken, secretKey)) {
                    Claims refreshClaims = JwtTokenUtil.extractClaims(refreshToken, secretKey);
                    studentId = refreshClaims.getSubject();
                    Long newStudentId = Long.valueOf(refreshClaims.get("studentId").toString());
                    String newAccessToken = JwtTokenUtil.createToken(newStudentId, secretKey, 1000 * 60 * 30); // 30분
                    response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + newAccessToken);
                    request.setAttribute("STUDENT_ID", newStudentId);
                } else {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인이 필요합니다.");
                    return;
                }
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰이 유효하지 않습니다.");
                return;
            }
        }

        if (studentId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailService.loadUserByUsername(studentId);
            if (JwtTokenUtil.validateToken(jwt, userDetails, secretKey)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }
}
