package com.proselyte.webfluxsecurity.rest;

import com.proselyte.webfluxsecurity.dto.AuthRequestDto;
import com.proselyte.webfluxsecurity.dto.AuthResponseDto;
import com.proselyte.webfluxsecurity.dto.UserDto;
import com.proselyte.webfluxsecurity.entity.UserEntity;
import com.proselyte.webfluxsecurity.mapper.UserMapper;
import com.proselyte.webfluxsecurity.repository.UserRepositoty;
import com.proselyte.webfluxsecurity.security.CustomPrincipal;
import com.proselyte.webfluxsecurity.security.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping ("/api/v1/auth")
public class AuthRestControllerV1 {

    private final SecurityService securityService;
    private final UserRepositoty userRepositoty;
    private final UserMapper userMapper;

    @PostMapping("/register")
    public Mono<UserDto> register(@RequestBody UserDto dto) {
        UserEntity entity = userMapper.map(dto);
        return userRepositoty.save(entity)
                .map(userMapper::map);
    }

    @PostMapping("/login")
    public Mono<AuthResponseDto> login(@RequestBody AuthRequestDto dto) {
        return securityService.authenticate(dto.getUsername(), dto.getPassword())
                .flatMap(tokenDetails -> Mono.just(
                        AuthResponseDto.builder()
                                .userId(tokenDetails.getUserId())
                                .token(tokenDetails.getToken())
                                .issuedAt(tokenDetails.getIssuedAt())
                                .expiredAt(tokenDetails.getExpiredAt())
                                .build()
                ));
    }

    @GetMapping("/info")
    public Mono<UserDto> getUserInfo(Authentication authentication) {
        CustomPrincipal customPrincipal = (CustomPrincipal) authentication.getPrincipal();

        return userRepositoty.findById(customPrincipal.getId())
                .map(userMapper::map);
    }
}
