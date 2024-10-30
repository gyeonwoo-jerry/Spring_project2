package com.sparta.dto;

import com.sparta.entity.Schedule;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleResponseDto {
    private final Long id;
    private final String subject;
    private final String username;
    private final String contents;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public ScheduleResponseDto(Schedule saveSchedule) {
        this.id = saveSchedule.getId();
        this.username = saveSchedule.getUser().getUsername();
        this.subject = saveSchedule.getSubject();
        this.contents = saveSchedule.getContents();
        this.createdAt = saveSchedule.getCreatedAt();
        this.modifiedAt = saveSchedule.getModifiedAt();
    }
}
