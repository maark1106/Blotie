package com.example.foreignstudentmatch.domain;

import com.example.foreignstudentmatch.common.BaseTimeEntity;
import com.example.foreignstudentmatch.util.ListToStringConverter;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "student")
public class Student extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "school", nullable = false, length = 30)
    private String school;

    @Column(name = "major", nullable = false, length = 100)
    private String major;

    @Column(name = "student_number", nullable = false, length = 20)
    private String studentNumber;

    @Column(name = "grade", nullable = false)
    private int grade;

    @Column(name = "mbti", nullable = false, length = 10)
    private String mbti;

    @Convert(converter = ListToStringConverter.class)
    @Column(name = "language", columnDefinition = "json")
    private List<String> language;

    @Convert(converter = ListToStringConverter.class)
    @Column(name = "interests_korean", columnDefinition = "json")
    private List<String> interestsKorean;

    @Convert(converter = ListToStringConverter.class)
    @Column(name = "interests_english", columnDefinition = "json")
    private List<String> interestsEnglish;

    @Column(name = "is_korean", nullable = false)
    private boolean korean;

    @Column(name = "profile_image")
    private String profileImage;

    @OneToMany(mappedBy = "student", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<RefreshToken> refreshTokens;

    @OneToMany(mappedBy = "student", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Feed> feeds = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<MatchingRequest> matchings = new ArrayList<>();

    @Builder
    public Student(String name, String school, String major, String studentNumber, int grade, String mbti, List<String> language, List<String> interestsKorean, List<String> interestsEnglish, boolean isKorean, String profileImage) {
        this.name = name;
        this.school = school;
        this.major = major;
        this.studentNumber = studentNumber;
        this.grade = grade;
        this.mbti = mbti;
        this.language = language;
        this.interestsKorean = interestsKorean;
        this.interestsEnglish = interestsEnglish;
        this.korean = isKorean;
        this.profileImage = profileImage;
    }
}
