package com.social.user.authuser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaUserRepository extends JpaRepository<JpaUser, UUID> {

    Optional<JpaUser> findByEmail(String email);

    Optional<JpaUser> findByUniqueName(String uniqueName);
    boolean existsByEmail(String email);

    boolean existsByUniqueName(String uniqueName);
}
