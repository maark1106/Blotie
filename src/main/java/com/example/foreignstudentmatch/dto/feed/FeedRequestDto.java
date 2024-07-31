package com.example.foreignstudentmatch.dto.feed;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedRequestDto {

    private String title;
    private String content;
    private List<MultipartFile> images;
}
