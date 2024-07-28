package com.example.foreignstudentmatch.service;

import com.example.foreignstudentmatch.domain.Feed;
import com.example.foreignstudentmatch.domain.Student;
import com.example.foreignstudentmatch.domain.UploadImage;
import com.example.foreignstudentmatch.dto.feed.*;
import com.example.foreignstudentmatch.repository.FeedRepository;
import com.example.foreignstudentmatch.repository.LikeRepository;
import com.example.foreignstudentmatch.repository.StudentRepository;
import com.example.foreignstudentmatch.repository.UploadImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FeedService {

    private final S3UploadService s3UploadService;
    private final FeedRepository feedRepository;
    private final StudentRepository studentRepository;
    private final UploadImageRepository uploadImageRepository;
    private final LikeRepository likeRepository;

    public FeedSaveResponseDto saveFeed(Long studentId, String title, String content, List<MultipartFile> images) throws IOException {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("학생을 찾을 수 없습니다."));

        Feed feed = Feed.builder()
                .title(title)
                .content(content)
                .student(student)
                .build();

        feedRepository.save(feed);

        if (images != null && !images.isEmpty()) {
            uploadImages(images, feed);
        }

        return new FeedSaveResponseDto(feed.getId());
    }

    private void uploadImages(List<MultipartFile> images, Feed feed) throws IOException {
        for (MultipartFile image : images) {
            if (image != null && !image.isEmpty()) {
                String filename = s3UploadService.upload(image, "feeds");

                UploadImage uploadImage = UploadImage.builder()
                        .feed(feed)
                        .filename(filename)
                        .build();

                uploadImageRepository.save(uploadImage);
            }
        }
    }

    @Transactional(readOnly = true)
    public FeedListResponseDto getFeeds(int page, Long studentId) {
        int pageSize = 40; // 페이지당 게시글 수
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Feed> feedPage = feedRepository.findAllByOrderByCreatedDateDesc(pageable);

        List<FeedResponseDto> feedList = feedPage.stream()
                .map(feed -> new FeedResponseDto(
                        feed.getId(),
                        feed.getCreatedDate().toString(),
                        feed.getStudent().getProfileImage(),
                        feed.getTitle(),
                        feed.getContent(),
                        feed.getLikeCount(),
                        feed.getCommentCount(),
                        likeRepository.findByStudentIdAndFeedId(studentId, feed.getId()).isPresent() // 좋아요 여부
                ))
                .collect(Collectors.toList());

        return new FeedListResponseDto(feedList, page, feedPage.getTotalPages());
    }

    @Transactional(readOnly = true)
    public FeedListResponseDto getFeedsByLikes(int page, Long studentId) {
        int pageSize = 40; // 페이지당 게시글 수
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Feed> feedPage = feedRepository.findAllByOrderByLikeCountDesc(pageable);

        List<FeedResponseDto> feedList = feedPage.stream()
                .map(feed -> new FeedResponseDto(
                        feed.getId(),
                        feed.getCreatedDate().toString(),
                        feed.getStudent().getProfileImage(),
                        feed.getTitle(),
                        feed.getContent(),
                        feed.getLikeCount(),
                        feed.getCommentCount(),
                        likeRepository.findByStudentIdAndFeedId(studentId, feed.getId()).isPresent() // 좋아요 여부
                ))
                .collect(Collectors.toList());

        return new FeedListResponseDto(feedList, page, feedPage.getTotalPages());
    }

    @Transactional(readOnly = true)
    public FeedListResponseDto getFeedsByStudent(int page, Long studentId) {
        int pageSize = 40; // 페이지당 게시글 수
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Feed> feedPage = feedRepository.findAllByStudentIdOrderByCreatedDateDesc(studentId, pageable);

        List<FeedResponseDto> feedList = feedPage.stream()
                .map(feed -> new FeedResponseDto(
                        feed.getId(),
                        feed.getCreatedDate().toString(),
                        feed.getStudent().getProfileImage(),
                        feed.getTitle(),
                        feed.getContent(),
                        feed.getLikeCount(),
                        feed.getCommentCount(),
                        likeRepository.findByStudentIdAndFeedId(studentId, feed.getId()).isPresent() // 좋아요 여부
                ))
                .collect(Collectors.toList());

        return new FeedListResponseDto(feedList, page, feedPage.getTotalPages());
    }

    @Transactional(readOnly = true)
    public SingleFeedResponseDto getFeed(Long feedId, Long studentId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));

        List<FeedCommentResponseDto> comments = feed.getComments().stream()
                .map(comment -> new FeedCommentResponseDto(
                        comment.getId(),
                        comment.getCreatedDate(),
                        comment.getCommentNumber(),
                        comment.getStudent().getProfileImage(),
                        comment.getContent()
                ))
                .collect(Collectors.toList());

        List<String> images = feed.getUploadImages().stream()
                .map(UploadImage::getFilename)
                .collect(Collectors.toList());

        boolean isLike = likeRepository.findByStudentIdAndFeedId(studentId, feedId).isPresent();

        return SingleFeedResponseDto.builder()
                .feedId(feed.getId())
                .createdDate(feed.getCreatedDate())
                .profileImage(feed.getStudent().getProfileImage())
                .studentId(feed.getStudent().getId())
                .title(feed.getTitle())
                .content(feed.getContent())
                .images(images)
                .isLike(isLike)
                .likeCount(feed.getLikeCount())
                .commentsCount(feed.getCommentCount())
                .comments(comments)
                .build();
    }

    public void deleteFeed(Long feedId, Long studentId) {
        Feed deletedFeed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));

        if (!deletedFeed.getStudent().getId().equals(studentId)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        feedRepository.delete(deletedFeed);
    }

    public SingleFeedResponseDto updateFeed(Long feedId, Long studentId, FeedUpdateRequestDto requestDto) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));

        if (requestDto.getTitle() != null) {
            feed.update(requestDto.getTitle(), feed.getContent());
        }
        if (requestDto.getContent() != null) {
            feed.update(feed.getTitle(), requestDto.getContent());
        }

        List<String> images = feed.getUploadImages().stream()
                .map(uploadImage -> uploadImage.getFilename())
                .collect(Collectors.toList());

        List<FeedCommentResponseDto> comments = feed.getComments().stream()
                .map(comment -> new FeedCommentResponseDto(
                        comment.getId(),
                        comment.getCreatedDate().toString(),
                        comment.getCommentNumber(),
                        comment.getStudent().getProfileImage(),
                        comment.getContent()
                ))
                .collect(Collectors.toList());

        boolean isLike = likeRepository.findByStudentIdAndFeedId(studentId, feedId).isPresent();

        return SingleFeedResponseDto.builder()
                .feedId(feed.getId())
                .createdDate(feed.getCreatedDate())
                .profileImage(feed.getStudent().getProfileImage())
                .studentId(feed.getStudent().getId())
                .title(feed.getTitle())
                .content(feed.getContent())
                .images(images)
                .isLike(isLike)
                .commentsCount(feed.getCommentCount())
                .comments(comments)
                .build();
    }
}
