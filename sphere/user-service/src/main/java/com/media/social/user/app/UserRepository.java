package com.media.social.user.app;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username);
    Optional<User> findByUserId(UUID userId);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);





}
