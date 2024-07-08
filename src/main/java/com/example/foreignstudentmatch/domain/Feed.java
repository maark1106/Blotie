package com.example.foreignstudentmatch.domain;

import com.example.foreignstudentmatch.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "feed")
public class Feed extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    public Student student;

    @OneToMany(mappedBy = "feed", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    private List<Comment> comments = new ArrayList<>(); // 댓글

    @Column(name = "comment_count")
    private int commentCount = 0;     // 댓글 수

    @OneToMany(mappedBy = "feed", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    private List<Like> likes = new ArrayList<>(); // 좋아요

    @Column(name = "like_count")
    private int likeCount = 0;     // 좋아요 수

    @OneToMany(mappedBy = "feed", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    private List<UploadImage> uploadImages = new ArrayList<>();

    public void commentChange(int commentCnt) {
        this.commentCount = commentCnt;
    }

    public void likeChange(int likeCount) {
        this.likeCount = likeCount;
    }

    //== 수정 Dirty Checking ==//
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    //== Student & Feed 연관관계 편의 메소드 ==//
    private void setMappingStudent(Student student) {
        this.student = student;
        student.getFeeds().add(this);
    }

    @Builder
    public Feed(Long id, String title, String content, Student student, List<UploadImage> uploadImages) {
        this.id = id;
        this.title = title;
        this.content = content;
        setMappingStudent(student);
        this.uploadImages = uploadImages;
    }
}
