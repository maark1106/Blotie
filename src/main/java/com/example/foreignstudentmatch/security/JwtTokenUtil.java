package com.example.foreignstudentmatch.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

public class JwtTokenUtil {

    // JWT Token 발급
    public static String createToken(Long studentId, String key, long expireTimeMs) {
        Claims claims = Jwts.claims().setSubject(studentId.toString());
        claims.put("studentId", studentId);

        return "Bearer " + Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    // Refresh Token 발급
    public static String createRefreshToken(Long studentId, String key, long expireTimeMs) {
        Claims claims = Jwts.claims().setSubject(studentId.toString());
        claims.put("studentId", studentId);

        return "Bearer " + Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    // Claims에서 studentId 꺼내기
    public static Long getStudentId(String token, String secretKey) {
        return Long.valueOf(extractClaims(token, secretKey).get("studentId").toString());
    }

    // 발급된 Token이 만료 시간이 지났는지 체크
    public static boolean isExpired(String token, String secretKey) {
        Date expiredDate = extractClaims(token, secretKey).getExpiration();
        return expiredDate.before(new Date());
    }

    // SecretKey를 사용해 Token Parsing
    public static Claims extractClaims(String token, String secretKey) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    // Token 유효성 검사
    public static boolean validateToken(String token, UserDetails userDetails, String secretKey) {
        Long studentId = getStudentId(token, secretKey);
        return (studentId.equals(Long.valueOf(userDetails.getUsername())) && !isExpired(token, secretKey));
    }
}