package com.media.social.user.app;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "jpa_user")
@Getter
@Setter
@NoArgsConstructor
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false, unique = true)
    private String uniqueName;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private List<Role> roles = new ArrayList<>();

    // optional user details
    private String profileUrl;

    private String bio;

    private String lastName;

    private boolean privateAccount = false;

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
}
