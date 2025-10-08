package com.proselyte.webfluxsecurity.service;

import com.proselyte.webfluxsecurity.entity.UserEntity;
import com.proselyte.webfluxsecurity.entity.UserRole;
import com.proselyte.webfluxsecurity.repository.UserRepositoty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepositoty userRepositoty;
    private final PasswordEncoder passwordEncoder;

    public Mono<UserEntity> registerUser(UserEntity user) {
        return userRepositoty.save(
                user.toBuilder()
                        .password(passwordEncoder.encode(user.getPassword()))
                        .role(UserRole.USER)
                        .enabled(true)
                        .created_at(LocalDateTime.now())
                        .updated_at(LocalDateTime.now())
                        .build()
        ).doOnSuccess(u -> log.info("IN registerUser - user {} created", u));
    }

    public Mono<UserEntity> getUserById(Long id) {
        return userRepositoty.findById(id);
    }

    public Mono<UserEntity> getUserByUsername(String username) {
        return userRepositoty.findByUsername(username);
    }

}
