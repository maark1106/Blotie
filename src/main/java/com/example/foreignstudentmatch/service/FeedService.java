package com.example.foreignstudentmatch.service;

import com.example.foreignstudentmatch.domain.Feed;
import com.example.foreignstudentmatch.domain.Student;
import com.example.foreignstudentmatch.domain.UploadImage;
import com.example.foreignstudentmatch.dto.feed.FeedSaveResponseDto;
import com.example.foreignstudentmatch.repository.FeedRepository;
import com.example.foreignstudentmatch.repository.StudentRepository;
import com.example.foreignstudentmatch.repository.UploadImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
}
