package com.example.foreignstudentmatch.security;

import com.example.foreignstudentmatch.domain.RefreshToken;
import com.example.foreignstudentmatch.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final CustomUserDetailService userDetailService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final String secretKey = "my-secret-key-123123";
    private final long accessTokenExpiry = 1000 * 60 * 60; // 60분

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
                Claims claims = e.getClaims();
                studentId = claims.getSubject();

                if (studentId != null) {
                    Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByStudentId(Long.valueOf(studentId));
                    if (refreshTokenOptional.isPresent()) {
                        RefreshToken refreshToken = refreshTokenOptional.get();
                        if (JwtTokenUtil.validateToken(refreshToken.getToken(), userDetailService.loadUserByUsername(studentId), secretKey)) {
                            String newAccessToken = JwtTokenUtil.createToken(Long.valueOf(studentId), secretKey, accessTokenExpiry); // 30분
                            response.setHeader(HttpHeaders.AUTHORIZATION, newAccessToken);
                            request.setAttribute("STUDENT_ID", Long.valueOf(studentId));
                        } else {
                            response.sendRedirect("/auth");
                            return;
                        }
                    } else {
                        response.sendRedirect("/auth");
                        return;
                    }
                } else {
                    response.sendRedirect("/auth");
                    return;
                }
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰이 유효하지 않습니다.");
                return;
            }
        }

        if (studentId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            CustomUserDetail userDetail = userDetailService.loadUserByUsername(studentId);
            if (JwtTokenUtil.validateToken(jwt, userDetail, secretKey)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetail, null, userDetail.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }
}