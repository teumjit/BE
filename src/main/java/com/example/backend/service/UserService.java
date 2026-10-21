package com.example.backend.service;

import com.example.backend.domain.User;
import com.example.backend.dto.UserRequest;
import com.example.backend.repository.UserRepository;
import com.example.backend.dto.LoginRequest;
import com.example.backend.dto.LoginResponse;
import com.example.backend.dto.MyInfoResponse;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EmailVerificationService emailVerificationService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(
            UserRepository userRepository,
            EmailVerificationService emailVerificationService,
            PasswordEncoder passwordEncoder,
            JwtService jwtService)
        {

        this.userRepository = userRepository;
        this.emailVerificationService = emailVerificationService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public User createUser(UserRequest request) {

        System.out.println("request is null: " + (request == null));

        if (request != null) {
            System.out.println("userId: " + request.getUserId());
            System.out.println(
                "password is null: " + (request.getPassword() == null)
            );
            System.out.println(
                "password is blank: " +
                (request.getPassword() != null
                    && request.getPassword().isBlank())
            );
        }

        if (request == null
                || request.getUserId() == null
                || request.getPassword() == null
                || request.getUserId().isBlank()
                || request.getPassword().isBlank()) {
            throw new IllegalArgumentException(
                "이메일과 비밀번호를 입력해 주세요."
            );
        }

        String email = request.getUserId()
                .trim()
                .toLowerCase(Locale.ROOT);

        if (userRepository.findByUserId(email).isPresent()) {
            throw new IllegalArgumentException(
                "이미 가입된 이메일입니다."
            );
        }

        if (!emailVerificationService.consumeVerifiedEmail(email)) {
            throw new IllegalArgumentException(
                "이메일 인증을 먼저 완료해 주세요."
            );
        }

        String encodedPassword =
                passwordEncoder.encode(request.getPassword());

        User user = new User(email, encodedPassword);

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User updateUser(Long id, UserRequest request) {

        if (request == null
                || request.getUserId() == null
                || request.getPassword() == null
                || request.getUserId().isBlank()
                || request.getPassword().isBlank()) {
            throw new IllegalArgumentException(
                "이메일과 비밀번호를 입력해 주세요."
            );
        }

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("사용자를 찾을 수 없습니다.")
                );

        String newEmail = request.getUserId()
                .trim()
                .toLowerCase(Locale.ROOT);

        if (!newEmail.equals(user.getUserId())) {

            if (userRepository.findByUserId(newEmail).isPresent()) {
                throw new IllegalArgumentException(
                    "이미 사용 중인 이메일입니다."
                );
            }
            
            // if (!emailVerificationService
            //         .consumeVerifiedEmail(newEmail)) {
            //     throw new IllegalArgumentException(
            //         "새 이메일의 인증을 먼저 완료해 주세요."
            //     );
            // }

            user.setUserId(newEmail);
        }

        user.setPassword(
            passwordEncoder.encode(request.getPassword())
        );

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("사용자를 찾을 수 없습니다.")
                );

        userRepository.delete(user);
    }

    // 로그인 부분
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {

        if (request == null
                || request.getUserId() == null
                || request.getPassword() == null
                || request.getUserId().isBlank()
                || request.getPassword().isBlank()) {
            throw new IllegalArgumentException(
                "이메일과 비밀번호를 입력해 주세요."
            );
        }

        String email = request.getUserId()
                .trim()
                .toLowerCase(Locale.ROOT);

        User user = userRepository.findByUserId(email)
                .orElseThrow(() ->
                    new IllegalArgumentException(
                        "이메일 또는 비밀번호가 올바르지 않습니다."
                    )
                );

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {
            throw new IllegalArgumentException(
                "이메일 또는 비밀번호가 올바르지 않습니다."
            );
        }

        String token = jwtService.generateToken(user.getUserId());

        return new LoginResponse(
            "로그인에 성공했습니다.",
            user.getId(),
            user.getUserId(),
            token
        );
    }
    
    @Transactional(readOnly = true)
    public MyInfoResponse getMyInfo(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("인증된 사용자 이메일이 없습니다.");
        }

        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);

        User user = userRepository.findByUserId(normalizedEmail)
                .orElseThrow(() ->
                        new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return new MyInfoResponse(user.getId(), user.getUserId());
    }

}
