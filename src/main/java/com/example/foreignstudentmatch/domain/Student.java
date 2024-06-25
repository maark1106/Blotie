package com.example.foreignstudentmatch.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "school", nullable = false, length = 30)
    private String school;

    @Column(name = "department", nullable = false, length = 100)
    private String department;

    @Column(name = "student_number", nullable = false, length = 20)
    private String studentNumber;

    @Column(name = "grade", nullable = false)
    private int grade;

    @Column(name = "mbti", nullable = false, length = 10)
    private String mbti;

    @Column(name = "language", columnDefinition = "json")
    private String language;

    @Column(name = "interests_korean", columnDefinition = "json")
    private String interestsKorean;

    @Column(name = "interests_english", columnDefinition = "json")
    private String interestsEnglish;

    @Column(name = "is_korean", nullable = false)
    private boolean isKorean;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RefreshToken> refreshTokens;

}
