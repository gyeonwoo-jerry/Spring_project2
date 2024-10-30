package com.sparta.controller;

import com.sparta.dto.LoginRequestDto;
import com.sparta.dto.SignupRequestDto;
import com.sparta.dto.UserRequestDto;
import com.sparta.dto.UserResponseDto;
import com.sparta.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
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

    @PostMapping("/user/signup")
    public String signup(SignupRequestDto requestDto) {
        userService.signup(requestDto);

        return "회원가입이 잘되었습니다.";
    }

    @PostMapping("/user/login")
    public String login(LoginRequestDto requestDto, HttpServletResponse res) {
        try {
            userService.login(requestDto, res);
        } catch (Exception e) {
            return "다시입력해주세요";
        }

        return "로그인이 잘되었습니다.";
    }
}
