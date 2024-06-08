package com.media.social.user.app;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserUtility {

    private final UserService userService;
    public UUID getUUIDFromUniqueName(String uniqueName){
        return userService.getUserByUsername(uniqueName).getUserId();
    }

    public String getUserNameFromToken(String token){
        // rest call to get userName;
        return "admin_";
    }
}
