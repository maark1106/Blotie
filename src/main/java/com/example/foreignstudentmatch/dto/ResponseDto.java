package com.example.foreignstudentmatch.dto;

import io.micrometer.common.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto<T> {
    private String status;
    private String message;
    private T data;

    public ResponseDto(@Nullable T data){
        this.status = "200";
        this.message = "응답 성공";
        this.data = data;
    }
}