package com.example.foreignstudentmatch.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Builder
@Getter
@Table(name = "user_like")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;      // 좋아요를 누른 유저

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;    // 좋아요가 추가된 게시글

    private void setStudent(Student student) {
        this.student = student;
        student.getLikes().add(this);
    }

    private void setBoard(Board board){
        this.board = board;
        board.getLikes().add(this);
    }

    @Builder
    public Like(Student student, Board board){
        setStudent(student);
        setBoard(board);
    }
}