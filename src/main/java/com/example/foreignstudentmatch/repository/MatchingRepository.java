package com.example.foreignstudentmatch.repository;

import com.example.foreignstudentmatch.domain.MatchingRequest;
import com.example.foreignstudentmatch.domain.enums.Nationality;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchingRepository extends JpaRepository<MatchingRequest, Long> {
    List<MatchingRequest> findByNationality(Nationality nationality);
}
