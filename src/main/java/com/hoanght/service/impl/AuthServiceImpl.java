package com.hoanght.service.impl;

import com.hoanght.entity.User;
import com.hoanght.entity.UserDetail;
import com.hoanght.exception.BadRequestException;
import com.hoanght.payload.request.LoginRequest;
import com.hoanght.payload.request.RegisterRequest;
import com.hoanght.payload.response.AuthenticationResponse;
import com.hoanght.payload.response.MessageResponse;
import com.hoanght.payload.response.UserResponse;
import com.hoanght.repository.UserRepository;
import com.hoanght.service.AuthService;
import com.hoanght.service.RefreshTokenService;
import com.hoanght.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public MessageResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername()))
            throw new BadRequestException("USERNAME_ALREADY_EXISTS");

        User user = createUser(request);
        userRepository.save(user);

        return new MessageResponse("User registered successfully", 200);
    }

    @Override
    public AuthenticationResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(
                () -> new BadRequestException("User not found"));

        validatePassword(request.getPassword(), user.getPassword());

        Authentication authentication = authenticateUser(request.getUsername(), request.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtService.generateToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(user).getToken();

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .build();
    }

    @Override
    public UserResponse getProfile(UserDetail userDetail) {
        User user = userDetail.getUser();
        return modelMapper.map(user, UserResponse.class);
    }

    @Override
    public MessageResponse logout(UserDetail userDetail) {
        User user = userDetail.getUser();
        refreshTokenService.deleteByUser(user);
        return new MessageResponse("Logout successfully", 200);
    }

    private User createUser(RegisterRequest request) {
        return User.builder().username(request.getUsername()).password(
                bCryptPasswordEncoder.encode(request.getPassword())).build();
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!bCryptPasswordEncoder.matches(rawPassword, encodedPassword)) {
            throw new BadRequestException("Invalid password");
        }
    }

    private Authentication authenticateUser(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }
}
