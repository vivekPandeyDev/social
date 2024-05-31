package com.social.user.authuser;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterDto {

    @NotBlank(message = "firstName cannot be blank")
    private String firstName;

    @Email(message = "email is not valid")
    @NotBlank(message = "email cannot be blank")
    private String email;

    @NotBlank(message = "password cannot be blank")
    private String password;

    @NotBlank(message = "username cannot be blank")
    private String userName;

    private String lastName;
}
