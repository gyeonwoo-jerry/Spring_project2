package com.sparta.controller;

import com.sparta.dto.UserRequestDto;
import com.sparta.dto.UserResponseDto;
import com.sparta.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public UserResponseDto createUser(@RequestBody UserRequestDto requestDto) {
        return userService.createUser(requestDto);
    }

    @GetMapping("/user/{id}")
    public UserResponseDto getUser(@PathVariable("id") Long id) {
        return userService.getUser(id);
    }

    @PutMapping("/user/{id}")
    public UserResponseDto updateUser(@PathVariable("id") Long id, @RequestBody UserRequestDto requestDto) {
        return userService.updateUser(id, requestDto);
    }

    @DeleteMapping("/user/{id}")
    public Long deleteUser(@PathVariable("id") Long id) {
        return userService.deleteUser(id);
    }

}
