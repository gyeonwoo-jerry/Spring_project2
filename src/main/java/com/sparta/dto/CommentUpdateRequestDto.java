package com.sparta.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentUpdateRequestDto {
    @NotBlank
    private String contents;
}
