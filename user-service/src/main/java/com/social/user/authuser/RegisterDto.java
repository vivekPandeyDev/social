package com.social.user.authuser;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterDto {

    @NotBlank(message = "FirstName cannot be blank")
    private String firstName;

    @Email(message = "Email is not valid")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    @NotBlank(message = "Unique name cannot be blank")
    @Pattern(regexp = "^[a-z_]{6,20}$", message = "Only lowercase letters 'a' to 'b' and underscore '_' are allowed with max 20 character")
    private String uniqueName;

    private String lastName;

    private String profileUrl;
}
