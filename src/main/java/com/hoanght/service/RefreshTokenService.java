package com.hoanght.service;

import com.hoanght.entity.RefreshToken;
import com.hoanght.entity.User;
import com.hoanght.payload.request.RefreshTokenRequest;
import com.hoanght.payload.response.AuthenticationResponse;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(User user);

    RefreshToken verifyExpiration(RefreshToken token);

    AuthenticationResponse generateNewToken(RefreshTokenRequest request);

    void deleteByUser(User user);
}
