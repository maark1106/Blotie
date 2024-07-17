package com.example.foreignstudentmatch.domain;

import com.example.foreignstudentmatch.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "chat_room")
public class ChatRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Student koreanStudent;

    @OneToOne
    private Student foreignStudent;

    @Builder
    public ChatRoom(Student koreanStudent, Student foreignStudent) {
        this.koreanStudent = koreanStudent;
        this.foreignStudent = foreignStudent;
    }
}