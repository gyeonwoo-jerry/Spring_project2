package com.sparta.controller;

import com.sparta.dto.LoginRequestDto;
import com.sparta.dto.SignupRequestDto;
import com.sparta.dto.UserRequestDto;
import com.sparta.dto.UserResponseDto;
import com.sparta.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
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
    public String signup(@Valid SignupRequestDto requestDto, HttpServletResponse res) {
        userService.signup(requestDto, res);

        return "회원가입이 잘되었습니다.";
    }

    @PostMapping("/login")
    public String login(@Valid LoginRequestDto requestDto, HttpServletResponse res) {
        try {
            userService.login(requestDto, res);
        } catch (Exception e) {
            return "다시입력해주세요";
        }

        return "로그인이 잘되었습니다.";
    }
}
