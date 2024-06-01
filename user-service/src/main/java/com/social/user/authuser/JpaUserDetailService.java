package com.social.user.authuser;

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
    @Cacheable(value = "jpaUser", key = "#uniqueName")
    public UserDetails loadUserByUsername(String uniqueName) throws UsernameNotFoundException {
        return jpaUserRepository.findByUniqueName(uniqueName).orElseThrow(() -> new UsernameNotFoundException("No User found with given user name: " + uniqueName));
    }
}
