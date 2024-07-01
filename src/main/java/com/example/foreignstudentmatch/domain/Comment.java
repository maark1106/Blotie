package com.example.foreignstudentmatch.domain;

import com.example.foreignstudentmatch.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "comment")
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    public Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    public Feed feed;

    // Feed와의 다대일(N:1) 관계를 설정하는 메소드
    public void setFeed(Feed feed) {
        this.feed = feed;
        feed.getComments().add(this); // Feed 엔티티에도 Comment를 추가
    }

    // Student와의 다대일(N:1) 관계를 설정하는 메소드
    private void setStudent(Student student) {
        this.student = student;
        student.getComments().add(this); // Student 엔티티에도 Comment를 추가
    }

    // update
    private void update(String content) {
        this.content = content;
    }

    @Builder
    public Comment(String content, Student student, Feed feed) {
        this.content = content;
        setStudent(student);
        setFeed(feed);
    }
}
