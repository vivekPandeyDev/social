package com.vivek.social.authuser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaUserRepository extends JpaRepository<JpaUser, UUID> {

    Optional<JpaUser> findByEmail(String email);
    boolean existsByEmail(String email);
}
