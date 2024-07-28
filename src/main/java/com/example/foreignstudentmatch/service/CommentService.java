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
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final FeedRepository feedRepository;
    private final StudentRepository studentRepository;

    public CommentSaveResponseDto saveComment(Long feedId, CommentSaveRequestDto commentSaveRequestDto, Long studentId) {
        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new IllegalArgumentException("피드를 찾을 수 없습니다."));
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new IllegalArgumentException("학생을 찾을 수 없습니다."));

        int commentNumber = getOrCreateCommentNumber(feed, student);

        Comment comment = Comment.builder()
                .feed(feed)
                .student(student)
                .content(commentSaveRequestDto.getContent())
                .commentNumber(commentNumber)
                .build();

        commentRepository.save(comment);
        feed.commentChange(feed.getCommentCount() + 1); // 댓글 수 업데이트

        return new CommentSaveResponseDto(comment.getId());
    }

    private int getOrCreateCommentNumber(Feed feed, Student student) {
        return commentRepository.findByFeedAndStudent(feed, student)
                .stream()
                .findFirst()
                .map(Comment::getCommentNumber)
                .orElseGet(() -> {
                    Set<Long> studentIds = commentRepository.findByFeed(feed)
                            .stream()
                            .map(comment -> comment.getStudent().getId())
                            .collect(Collectors.toSet());
                    return studentIds.size() + 1;
                });
    }
}
