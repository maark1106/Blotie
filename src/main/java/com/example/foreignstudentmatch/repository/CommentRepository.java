package com.example.foreignstudentmatch.repository;

import com.example.foreignstudentmatch.domain.Comment;
import com.example.foreignstudentmatch.domain.Feed;
import com.example.foreignstudentmatch.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByFeedAndStudent(Feed feed, Student student);
    List<Comment> findByFeed(Feed feed);
}
