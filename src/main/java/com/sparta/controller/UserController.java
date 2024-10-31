package com.sparta.controller;

import com.sparta.dto.*;
import com.sparta.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserResponseDto createUser(@RequestBody UserRequestDto requestDto) {
        return userService.createUser(requestDto);
    }

    @GetMapping("/{id}")
    public UserResponseDto getUser(@PathVariable("id") Long id) {
        return userService.getUser(id);
    }

    @PutMapping("/{id}")
    public UserResponseDto updateUser(@PathVariable("id") Long id, @RequestBody @Valid UserRequestDto requestDto) {
        return userService.updateUser(id, requestDto);
    }

    @DeleteMapping("/{id}")
    public Long deleteUser(@PathVariable("id") Long id) {
        return userService.deleteUser(id);
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequestDto requestDto, HttpServletResponse res) {
        userService.signup(requestDto, res);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginRequestDto requestDto, HttpServletResponse res) {
        try {
            userService.login(requestDto, res);
        } catch (Exception e)   {
            return "다시입력해주세요";
        }

        return "로그인이 잘되었습니다.";
    }
}
