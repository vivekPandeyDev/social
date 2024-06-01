package com.social.user.authuser;

public interface UserService {
    JpaUserDto saveUser(RegisterDto registerDto);

    JpaUserDto getUserByEmail(String email);

    JpaUserDto getUserByUniqueName(String uniqueName);
}
