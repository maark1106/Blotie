package com.example.foreignstudentmatch.service;

import com.example.foreignstudentmatch.domain.Comment;
import com.example.foreignstudentmatch.domain.Feed;
import com.example.foreignstudentmatch.domain.Student;
import com.example.foreignstudentmatch.dto.comment.CommentSaveRequestDto;
import com.example.foreignstudentmatch.dto.comment.CommentSaveResponseDto;
import com.example.foreignstudentmatch.repository.CommentRepository;
import com.example.foreignstudentmatch.repository.FeedRepository;
import com.example.foreignstudentmatch.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final FeedRepository feedRepository;
    private final StudentRepository studentRepository;

    @Transactional
    public CommentSaveResponseDto saveComment(Long feedId, CommentSaveRequestDto commentSaveRequestDto) {
        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new IllegalArgumentException("피드를 찾을 수 없습니다."));
        Student student = studentRepository.findById(commentSaveRequestDto.getStudentId()).orElseThrow(() -> new IllegalArgumentException("학생을 찾을 수 없습니다."));

        String commentName = getOrCreateCommentName(feed, student);

        Comment comment = Comment.builder()
                .feed(feed)
                .student(student)
                .content(commentSaveRequestDto.getContent())
                .commentName(commentName)
                .build();

        commentRepository.save(comment);
        feed.commentChange(feed.getCommentCount() + 1); // 댓글 수 업데이트

        return new CommentSaveResponseDto(comment.getId());
    }

    private String getOrCreateCommentName(Feed feed, Student student) {
        return commentRepository.findByFeedAndStudent(feed, student)
                .stream()
                .findFirst()
                .map(Comment::getCommentName)
                .orElseGet(() -> {
                    Set<Long> studentIds = commentRepository.findByFeed(feed)
                            .stream()
                            .map(comment -> comment.getStudent().getId())
                            .collect(Collectors.toSet());
                    return "익명이 " + (studentIds.size() + 1);
                });
    }
}
