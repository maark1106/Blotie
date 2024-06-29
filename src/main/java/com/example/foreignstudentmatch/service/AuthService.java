package com.example.foreignstudentmatch.service;

import com.example.foreignstudentmatch.domain.RefreshToken;
import com.example.foreignstudentmatch.domain.Student;
import com.example.foreignstudentmatch.dto.AuthRequestDto;
import com.example.foreignstudentmatch.dto.AuthResponseDto;
import com.example.foreignstudentmatch.dto.RegisterRequestDto;
import com.example.foreignstudentmatch.dto.RegisterResponseDto;
import com.example.foreignstudentmatch.repository.RefreshTokenRepository;
import com.example.foreignstudentmatch.repository.StudentRepository;
import com.example.foreignstudentmatch.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final StudentRepository studentRepository;
    private final RefreshTokenRepository refreshTokenRepository; // RefreshTokenRepository 추가 필요

    private final String secretKey = "my-secret-key-123123"; // 실제 환경에서는 더 안전하게 관리
    private final long accessTokenExpiry = 1000 * 60 * 30; // 30분
    private final long refreshTokenExpiry = 1000 * 60 * 60 * 24 * 7; // 7일

    public AuthResponseDto auth(AuthRequestDto loginRequestDto) {

        // 세종대 인증 api에 학번과 password 보내고 결과 받아오기
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject loginInfo = new JSONObject();
        loginInfo.put("id", loginRequestDto.getId());
        loginInfo.put("pw", loginRequestDto.getPassword());

        HttpEntity<String> request = new HttpEntity<>(loginInfo.toString(), headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(
                "https://auth.imsejong.com/auth?method=ClassicSession",
                HttpMethod.POST, request, String.class);

        String responseBody = response.getBody();

        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(responseBody);
            JSONObject result = (JSONObject) jsonObject.get("result");
            JSONObject body = (JSONObject) result.get("body");

            // 올바른 학번, 비번이면 isAuth = true
            boolean isAuth = (boolean) result.get("is_auth");
            boolean isJoined = false;

            if (isAuth) {

                // 로그인 시 이미 우리 앱에 가입되어 있는 회원이면 isJoined = true로 설정하고 accessToken과 refreshToken 생성
                Optional<Student> optionalStudent = studentRepository.findByStudentNumber(loginRequestDto.getId());
                isJoined = studentRepository.findByStudentNumber(loginRequestDto.getId())
                        .isPresent();

                if (isJoined) {
                    Student student = optionalStudent.get();

                    // AccessToken 생성
                    String accessToken = JwtTokenUtil.createToken(student.getStudentNumber(), secretKey, accessTokenExpiry);

                    // 기존에 발급된 RefreshToken이 있는지 확인
                    Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByStudent(student);
                    String refreshToken;
                    if (optionalRefreshToken.isPresent()) {
                        // 기존 RefreshToken이 있다면, 만료 시간을 업데이트
                        RefreshToken existingRefreshToken = optionalRefreshToken.get();
                        existingRefreshToken.updateExpiryDate(LocalDateTime.now().plusDays(7));
                     refreshToken = existingRefreshToken.getToken();
                    } else {
                        // 기존 RefreshToken이 없다면, 새로 생성
                        refreshToken = JwtTokenUtil.createRefreshToken(student.getStudentNumber(), secretKey, refreshTokenExpiry);
                        RefreshToken refreshTokenEntity = RefreshToken
                                .builder()
                                .student(student)
                                .token(refreshToken)
                                .expiryDate(LocalDateTime.now().plusDays(7))
                                .build();
                        refreshTokenRepository.save(refreshTokenEntity);
                    }

                    // AuthResponseDto에 토큰 정보 추가
                    return AuthResponseDto.builder()
                            .grade((String) body.get("grade"))
                            .major((String) body.get("major"))
                            .name((String) body.get("name"))
                            .isJoined(isJoined)
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .build();
                }
            }

            return AuthResponseDto.builder()
                    .grade((String) body.get("grade"))
                    .major((String) body.get("major"))
                    .name((String) body.get("name"))
                    .isJoined(isJoined)
                    .build();

        } catch (ParseException e) {
            throw new RuntimeException("Failed to parse response", e);
        }
    }

    public RegisterResponseDto register(RegisterRequestDto registerRequestDto) {
        // Student 엔티티로 변환
        Student student = Student.builder()
                .school(registerRequestDto.getSchool())
                .major(registerRequestDto.getMajor())
                .name(registerRequestDto.getName())
                .studentNumber(registerRequestDto.getStudentNumber())
                .grade(registerRequestDto.getGrade())
                .mbti(registerRequestDto.getMbti())
                .language(registerRequestDto.getLanguage())
                .interestsKorean(registerRequestDto.getInterestsKorean())
                .interestsEnglish(registerRequestDto.getInterestsEnglish())
                .isKorean(registerRequestDto.isKorean())
                .build();

        // DB에 저장
        studentRepository.save(student);

        // AccessToken과 RefreshToken 생성
        String accessToken = JwtTokenUtil.createToken(student.getStudentNumber(), secretKey, accessTokenExpiry);
        String refreshToken = JwtTokenUtil.createRefreshToken(student.getStudentNumber(), secretKey, refreshTokenExpiry);

        // RefreshToken을 DB에 저장
        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .student(student)
                .token(refreshToken)
                .expiryDate(LocalDateTime.now().plusDays(7))
                .build();
        refreshTokenRepository.save(refreshTokenEntity);

        // 응답 DTO 생성 및 반환
        return RegisterResponseDto.builder()
                .studentId(student.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}