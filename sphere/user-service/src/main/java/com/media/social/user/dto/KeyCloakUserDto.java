package com.media.social.user.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class KeyCloakUserDto {
    private String username;
    private boolean enabled;
    private boolean emailVerified;
    private String firstName;
    private String lastName;
    private String email;
    private List<KeyCloakCredentialDto> credentials;
    private List<String> realmRoles;
    private Map<String, List<String>> clientRoles;
}
