package com.social.user.authuser;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class JpaUserDto {
    private UUID userId;
    private String firstName;
    private String lastName;
    private String email;
    private Role roles;
    private String imageUrl;
    private LocalDate createdAt;
}
