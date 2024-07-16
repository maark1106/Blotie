package com.example.foreignstudentmatch.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "matching_request")
public class MatchingRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    private Nationality nationality;

    @Builder
    public MatchingRequest(Student student, Nationality nationality) {
        this.student = student;
        this.nationality = nationality;
    }
}
