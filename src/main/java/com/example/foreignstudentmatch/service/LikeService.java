package com.example.foreignstudentmatch.service;

import com.example.foreignstudentmatch.domain.Feed;
import com.example.foreignstudentmatch.domain.Like;
import com.example.foreignstudentmatch.domain.Student;
import com.example.foreignstudentmatch.repository.FeedRepository;
import com.example.foreignstudentmatch.repository.LikeRepository;
import com.example.foreignstudentmatch.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final FeedRepository feedRepository;
    private final StudentRepository studentRepository;

    @Transactional
    public void addLike(Long studentId, Long feedId) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학생"));
        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 feed"));

        Like like = Like.builder()
                .student(student)
                .feed(feed)
                .build();

        likeRepository.save(like);

        feed.likeChange(likeRepository.countByFeedId(feedId));
        feedRepository.save(feed);
    }

    @Transactional
    public void removeLike(Long studentId, Long feedId) {
        Like like = likeRepository.findByStudentIdAndFeedId(studentId, feedId)
                .orElseThrow(() -> new IllegalArgumentException("좋아요가 존재하지 않습니다."));

        likeRepository.delete(like);

        Feed feed = like.getFeed();
        feed.likeChange(likeRepository.countByFeedId(feedId));
        feedRepository.save(feed);
    }
}
