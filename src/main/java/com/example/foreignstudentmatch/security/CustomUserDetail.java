package com.example.foreignstudentmatch.security;

import com.example.foreignstudentmatch.domain.Student;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
public class CustomUserDetail implements UserDetails {

    private final Student student;

    public CustomUserDetail(Student student) {
        this.student = student;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // 필요에 따라 권한을 설정
    }

    @Override
    public String getPassword() {
        return null; // 비밀번호가 없으므로 null을 반환
    }

    @Override
    public String getUsername() {
        return student.getId().toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
