package com.example.foreignstudentmatch.dto.matching;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class NotificationPayload {
    private final String title;
    private final String body;

}