package com.example.foreignstudentmatch.repository;

import com.example.foreignstudentmatch.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

}
