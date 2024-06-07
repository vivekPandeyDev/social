package com.media.social.user.app;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUniqueName(String uniqueName);
    boolean existsByEmail(String email);
    boolean existsByUniqueName(String uniqueName);
}
