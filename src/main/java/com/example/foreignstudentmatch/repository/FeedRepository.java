package com.example.foreignstudentmatch.repository;

import com.example.foreignstudentmatch.domain.Feed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {
    Page<Feed> findAllByOrderByCreatedDateDesc(Pageable pageable);
}
