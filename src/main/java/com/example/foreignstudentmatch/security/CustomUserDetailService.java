package com.example.foreignstudentmatch.security;

import com.example.foreignstudentmatch.domain.Student;
import com.example.foreignstudentmatch.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final StudentRepository studentRepository;

    @Override
    public UserDetails loadUserByUsername(String studentId) throws UsernameNotFoundException {
        Student student = studentRepository.findById(Long.valueOf(studentId))
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다 id: " + studentId));

        return new CustomUserDetail(student);
    }
}