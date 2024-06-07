package com.media.social.user.app;

import com.media.social.user.dto.RegisterDto;
import com.media.social.user.dto.UserDto;

public interface UserService {
    UserDto saveUser(RegisterDto registerDto);

    UserDto getUserByEmail(String email);

    UserDto getUserByUniqueName(String uniqueName);

    boolean isUserExist(String uniqueName,String email);
}
