package com.sparta.dto;

import com.sparta.entity.User;
import lombok.Getter;

@Getter
public class UserResponseDto {
    private final Long id;
    private final String username;
    private final String email;

    public UserResponseDto(User savedUser) {
        this.id = savedUser.getId();
        this.username = savedUser.getUsername();
        this.email = savedUser.getEmail();
    }
}
