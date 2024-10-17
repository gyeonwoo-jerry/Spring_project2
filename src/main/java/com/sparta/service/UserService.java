package com.sparta.service;

import com.sparta.dto.UserRequestDto;
import com.sparta.dto.UserResponseDto;
import com.sparta.entity.User;
import com.sparta.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponseDto createUser(UserRequestDto requestDto) {
        User user = new User();
        user.setEmail(requestDto.getEmail());
        user.setUsername(requestDto.getUsername());

        User savedUser = userRepository.save(user);

        UserResponseDto responseDto = new UserResponseDto(savedUser);

        return responseDto;
    }

    public UserResponseDto getUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));

        UserResponseDto responseDto = new UserResponseDto(user);

        return responseDto;
    }


    public UserResponseDto updateUser(Long id, UserRequestDto requestDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setUsername(requestDto.getUsername());
        user.setEmail(requestDto.getEmail());

        UserResponseDto responseDto = new UserResponseDto(user);
        return responseDto;
    }


    public Long deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        userRepository.delete(user);
        return id;
    }
}
