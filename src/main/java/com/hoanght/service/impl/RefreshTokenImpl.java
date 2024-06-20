package com.hoanght.service.impl;

import com.hoanght.entity.RefreshToken;
import com.hoanght.entity.User;
import com.hoanght.exception.NotFoundException;
import com.hoanght.payload.request.RefreshTokenRequest;
import com.hoanght.payload.response.AuthenticationResponse;
import com.hoanght.repository.RefreshTokenRepository;
import com.hoanght.service.RefreshTokenService;
import com.hoanght.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    @Value("${jwt.refresh-expiration}")
    private Long expiration;

    @Override
    public RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = RefreshToken.builder().user(user).token(
                Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes())).expiryDate(
                Instant.now().plusMillis(expiration)).build();

        refreshTokenRepository.deleteByUser(user);
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new NotFoundException("Token is expired");
        }
        return token;
    }

    @Override
    public AuthenticationResponse generateNewToken(RefreshTokenRequest request) {
        User user = refreshTokenRepository.findByToken(request.getRefreshToken()).map(this::verifyExpiration).map(
                RefreshToken::getUser).orElseThrow(() -> new NotFoundException("Token is invalid"));

        String accessToken = jwtService.generateToken(user);
        String refreshToken = createRefreshToken(user).getToken();

        return AuthenticationResponse.builder().accessToken(accessToken).refreshToken(refreshToken).tokenType(
                "Bearer").build();
    }

    @Override
    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }

}
