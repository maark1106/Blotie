package com.example.foreignstudentmatch.service;

import com.example.foreignstudentmatch.domain.Feed;
import com.example.foreignstudentmatch.domain.Student;
import com.example.foreignstudentmatch.domain.UploadImage;
import com.example.foreignstudentmatch.dto.feed.*;
import com.example.foreignstudentmatch.repository.FeedRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final S3UploadService s3UploadService;
    private final FeedRepository feedRepository;
    private final StudentRepository studentRepository;
    private final UploadImageRepository uploadImageRepository;

    @Transactional
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
        List<UploadImage> uploadImages = new ArrayList<>();

        for (MultipartFile image : images) {
            if (image != null && !image.isEmpty()) {
                String filename = s3UploadService.upload(image, "feeds");

                UploadImage uploadImage = UploadImage.builder()
                        .feed(feed)
                        .filename(filename)
                        .build();

                uploadImages.add(uploadImage);
                uploadImageRepository.save(uploadImage);
            }
        }
    }

    @Transactional(readOnly = true)
    public FeedListResponseDto getFeeds(int page) {
        int pageSize = 4; // 페이지당 게시글 수
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Feed> feedPage = feedRepository.findAllByOrderByCreatedDateDesc(pageable);

        List<FeedResponseDto> feedList = feedPage.stream()
                .map(feed -> new FeedResponseDto(
                        feed.getId(),
                        feed.getCreatedDate(),
                        feed.getStudent().getProfileImage(),
                        feed.getTitle(),
                        feed.getContent(),
                        feed.getLikeCount(),
                        feed.getCommentCount()
                ))
                .collect(Collectors.toList());

        return new FeedListResponseDto(feedList, page, feedPage.getTotalPages());
    }

    @Transactional(readOnly = true)
    public SingleFeedResponseDto getFeed(Long feedId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));

        List<FeedCommentResponseDto> comments = feed.getComments().stream()
                .map(comment -> new FeedCommentResponseDto(
                        comment.getId(),
                        comment.getCreatedDate().toString(),
                        comment.getCommentNumber(),
                        comment.getStudent().getProfileImage(),
                        comment.getContent()
                ))
                .collect(Collectors.toList());

        List<String> images = feed.getUploadImages().stream()
                .map(UploadImage::getFilename)
                .collect(Collectors.toList());

        return new SingleFeedResponseDto(
                feed.getId(),
                feed.getCreatedDate().toString(),
                feed.getStudent().getProfileImage(),
                feed.getStudent().getId(),
                feed.getTitle(),
                feed.getContent(),
                images,
                feed.getCommentCount(),
                comments
        );
    }
}
