package com.vivek.social.authuser;

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

    @NotBlank(message = "lastName cannot be blank")
    private String lastName;
}
