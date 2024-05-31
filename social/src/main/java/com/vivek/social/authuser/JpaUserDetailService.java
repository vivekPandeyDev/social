package com.vivek.social.authuser;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class JpaUserDetailService implements UserDetailsService {

    private final JpaUserRepository jpaUserRepository;

    @Override
    @Cacheable(value = "jpaUser", key = "#email")
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return jpaUserRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("No User found with given email: " + email));
    }
}
