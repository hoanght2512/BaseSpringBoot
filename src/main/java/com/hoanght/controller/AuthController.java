package com.hoanght.controller;

import com.hoanght.entity.UserDetail;
import com.hoanght.payload.request.LoginRequest;
import com.hoanght.payload.request.RefreshTokenRequest;
import com.hoanght.payload.request.RegisterRequest;
import com.hoanght.payload.response.AuthenticationResponse;
import com.hoanght.payload.response.MessageResponse;
import com.hoanght.payload.response.UserResponse;
import com.hoanght.service.AuthService;
import com.hoanght.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@RequestBody @Validated RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Validated LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/logout")
    public ResponseEntity<MessageResponse> logout(@AuthenticationPrincipal UserDetail userDetail) {
        return ResponseEntity.ok(authService.logout(userDetail));
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getProfile(@AuthenticationPrincipal UserDetail user) {
        return ResponseEntity.ok(authService.getProfile(user));
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(refreshTokenService.generateNewToken(request));
    }
}
