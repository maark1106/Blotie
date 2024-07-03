package com.example.foreignstudentmatch.repository;

import com.example.foreignstudentmatch.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByStudentIdAndFeedId(Long studentId, Long feedId);
    int countByFeedId(Long feedId);
}
