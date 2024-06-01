package com.social.authentication.controller;

public interface AuthService {
    JpaUserDto getUserByEmail(String email);

    String getAuthToken(LoginDto loginDto);

    String validateToken(String token);
}
