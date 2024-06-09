package com.media.social.user.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class UserDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 92414210438655957L;
    private UUID userId;
    private String firstName;
    private String username;
    private String email;
    private List<String> roles = new ArrayList<>();
    private List<UUID> followers = new ArrayList<>();
    private List<UUID> followings = new ArrayList<>();
    private String profileUrl;
    private String bio;
    private String lastName;
    private boolean privateAccount;
    private LocalDate createdAt;
    private boolean enable;
    private boolean emailVerified;
}
