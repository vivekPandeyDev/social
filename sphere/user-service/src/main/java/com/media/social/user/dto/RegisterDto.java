package com.media.social.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class RegisterDto {

    @NotBlank(message = "user name cannot be blank")
    @Pattern(regexp = "^[a-z]{5,20}|[a-z]{5,20}(_[a-z]{1,20})?$", message = "Only lowercase letters 'a' to 'b' and underscore '_' are allowed with max 20 character")
    private String username;

    @NotBlank(message = "FirstName cannot be blank")
    private String firstName;

    @NotBlank(message = "Lastname cannot be blank")
    private String lastName;

    @Email(message = "Email is not valid")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @NotBlank(message = "password cannot be null")
    private String password;

    private boolean enable = true;

    private boolean emailVerified = false;


    private String profileUrl;

    private String bio;


    public UserRepresentation toUserRepresentation(String password) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(this.username);
        userRepresentation.setFirstName(this.firstName);
        userRepresentation.setLastName(this.lastName);
        userRepresentation.setEmail(this.email);
        userRepresentation.setEnabled(this.enable);
        userRepresentation.setEmailVerified(this.emailVerified);
        var defaultCredential = Stream.of(new KeyCloakCredentialDto(password).toCredentialRepresentation()).toList();
        userRepresentation.setCredentials(defaultCredential);
        //TODO -> could add attribute like followers following etc
        return userRepresentation;
    }
}
