package com.media.social.user.app;

import com.media.social.user.dto.Operation;
import com.media.social.user.dto.RegisterDto;
import com.media.social.user.dto.UserDto;

import java.util.UUID;

public interface UserService {

    UserDto saveUser(RegisterDto registerDto);
    UserDto getUserByUsername(String username);

    UserDto getUserByUUID(UUID uuid);

    boolean isUserExist(String uniqueName,String email);

    UserDto updateFollower(UUID currentUserUUIID,UUID followerUUID, Operation operation);

    UserDto updateFollowing(UUID currentUserUUIID,UUID followingUUID,Operation operation);
}
