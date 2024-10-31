package com.sparta.service;

import com.sparta.dto.LoginRequestDto;
import com.sparta.dto.SignupRequestDto;
import com.sparta.dto.UserRequestDto;
import com.sparta.dto.UserResponseDto;
import com.sparta.entity.User;
import com.sparta.entity.UserRoleEnum;
import com.sparta.jwt.JwtUtil;
import com.sparta.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
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

    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Transactional
    public void signup(SignupRequestDto requestDto, HttpServletResponse res) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // email 중복확인
        String email = requestDto.getEmail();
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 Email 입니다.");
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        // 사용자 등록
        User user = new User(username, password, email, role);
        User saveUser = userRepository.save(user);

        // 토큰 생성 및 추가
        String token = jwtUtil.createToken(saveUser.getEmail(), saveUser.getRole());
        jwtUtil.addJwtToCookie(token, res);
    }

    public void login(LoginRequestDto requestDto, HttpServletResponse res) {
        String email = requestDto.getEmail();
        String password = requestDto.getPassword();

        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            throw new IllegalArgumentException("비밀번호가 일치 하지 않습니다.");
        }
        //토큰 생성 및 쿠키 저장 후 Responser 객체에 추가
        String token = jwtUtil.createToken(user.getEmail(), user.getRole());
        jwtUtil.addJwtToCookie(token, res);
    }
}
