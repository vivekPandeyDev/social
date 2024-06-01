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
    private Role role;
    private String profileUrl;
    private boolean privateAccount;
    private String uniqueName;
    private LocalDate createdAt;
}
