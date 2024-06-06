package com.social.user.authuser;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class JpaUserDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 92414210438655957L;
    private UUID userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role role;
    private String profileUrl;
    private boolean privateAccount;
    private String uniqueName;
    private LocalDate createdAt;
}
