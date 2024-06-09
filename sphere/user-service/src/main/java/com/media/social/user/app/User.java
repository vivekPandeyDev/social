package com.media.social.user.app;

import com.media.social.user.dto.UserDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "jpa_user")
@Getter
@Setter
@NoArgsConstructor
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private UUID userId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @ElementCollection
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private List<String> roles = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "user_followers", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "follower")
    private Set<UUID> followers = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "user_followings", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "following")
    private Set<UUID> followings = new HashSet<>();

    // optional user details
    private String profileUrl;

    private String bio;

    private String lastName;

    private boolean privateAccount = false;

    private boolean emailVerified = false;

    private boolean enable = true;

    @Column(updatable = false)
    private LocalDate createdAt;

    @PrePersist
    public void postConstruct() {
        if (createdAt == null)
            this.createdAt = LocalDate.now();
    }
    public boolean checkForPrivateAccount() {
        return privateAccount;
    }

    public UserDto toUserDto(ModelMapper mapper){
        return mapper.map(this,UserDto.class);
    }
}
