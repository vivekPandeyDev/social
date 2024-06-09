package com.media.social.user.app;

import com.media.social.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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


    public static UserDto toUserDto(UserRepresentation userRep) {
        UserDto userDto = new UserDto();
        userDto.setUserId(UUID.fromString(userRep.getId()));
        userDto.setFirstName(userRep.getFirstName());
        userDto.setLastName(userRep.getLastName());
        userDto.setUsername(userRep.getUsername());
        userDto.setEmail(userRep.getEmail());
        userDto.setEnable(userRep.isEnabled());
        userDto.setEmailVerified(userRep.isEmailVerified());

        if (userRep.getCreatedTimestamp() != null) {
            userDto.setCreatedAt(LocalDate.ofEpochDay(userRep.getCreatedTimestamp()));
        }

        List<String> roles = new ArrayList<>();
        if (userRep.getRealmRoles() != null) {
            roles.addAll(userRep.getRealmRoles());
        }
        if (userRep.getClientRoles() != null) {
            userRep.getClientRoles().values().forEach(roles::addAll);
        }
        userDto.setRoles(roles);

        Map<String, List<String>> attributes = userRep.getAttributes();
        if (attributes != null) {
            userDto.setProfileUrl(getAttribute(attributes, "profileUrl"));
            userDto.setBio(getAttribute(attributes, "bio"));
            userDto.setFollowers(getUUIDListAttribute(attributes, "followers"));
            userDto.setFollowings(getUUIDListAttribute(attributes, "followings"));
        }

        return userDto;
    }

    private static String getAttribute(Map<String, List<String>> attributes, String key) {
        List<String> values = attributes.get(key);
        return (values != null && !values.isEmpty()) ? values.get(0) : null;
    }

    private static List<UUID> getUUIDListAttribute(Map<String, List<String>> attributes, String key) {
        List<String> values = attributes.get(key);
        List<UUID> uuidList = new ArrayList<>();
        if (values != null) {
            for (String value : values) {
                uuidList.add(UUID.fromString(value));
            }
        }
        return uuidList;
    }
}
