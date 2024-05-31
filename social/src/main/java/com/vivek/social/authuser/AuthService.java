package com.vivek.social.authuser;

public interface AuthService {
    JpaUserDto saveUser(RegisterDto registerDto);
    JpaUserDto getUserByEmail(String email);
    String getAuthToken(LoginDto loginDto);
}
