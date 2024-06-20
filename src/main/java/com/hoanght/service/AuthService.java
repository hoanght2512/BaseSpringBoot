package com.hoanght.service;

import com.hoanght.entity.UserDetail;
import com.hoanght.payload.request.LoginRequest;
import com.hoanght.payload.request.RegisterRequest;
import com.hoanght.payload.response.AuthenticationResponse;
import com.hoanght.payload.response.MessageResponse;
import com.hoanght.payload.response.UserResponse;

public interface AuthService {
    MessageResponse register(RegisterRequest request);

    AuthenticationResponse login(LoginRequest request);

    UserResponse getProfile(UserDetail userDetail);

    MessageResponse logout(UserDetail userDetail);
}
