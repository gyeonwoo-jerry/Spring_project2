package com.sparta.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentRequestDto {
    @NotBlank
    private String contents;
    @NotBlank
    private Long userId;
    @NotBlank
    private Long scheduleId;
}
