package com.media.social.user.keycloak;

import com.media.social.user.dto.UserDto;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;


public class UserUtility {

    private UserUtility() {
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
            LocalDate localDate = Instant.ofEpochMilli(userRep.getCreatedTimestamp())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            userDto.setCreatedAt(localDate);
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
            userDto.setProfileUrl(getAttribute(attributes, "picture"));
        }

        return userDto;
    }

    public static UserDto setDefaultRoles(UserDto userDto, RoleRepresentation role){
        userDto.setRoles(Collections.singletonList(role.getName()));
        return userDto;
    }

    private static String getAttribute(Map<String, List<String>> attributes, String key) {
        List<String> values = attributes.get(key);
        return (values != null && !values.isEmpty()) ? values.get(0) : null;
    }

}
