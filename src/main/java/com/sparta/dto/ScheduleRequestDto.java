package com.sparta.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ScheduleRequestDto {
    @NotBlank
    private String subject;
    @NotBlank
    private Long userId;
    @NotBlank
    private String contents;
}
