package com.social.user.authuser;

import com.social.user.exception.NotFoundException;
import com.social.user.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final JpaUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;
    private static final String USER_DETAIL = "user details -> {}";

    @Override
    public JpaUserDto saveUser(RegisterDto registerDto) {
        if (
                        Boolean.TRUE.equals(userRepository.existsByEmail(registerDto.getEmail())) ||
                        Boolean.TRUE.equals(userRepository.existsByUniqueName(registerDto.getUniqueName()))
        ) {
            log.error("Cannot save user as email or username is already present in database {}",registerDto.getEmail());
            throw new ServiceException("User is already present choose different email or username!", HttpStatus.FOUND,"User Already exists");
        }
        JpaUser user = mapper.map(registerDto, JpaUser.class);
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        if (log.isDebugEnabled()) log.debug(USER_DETAIL, user);
        final var jpaUser = userRepository.save(user);
        return mapper.map(jpaUser,JpaUserDto.class);
    }

    @Override
    @Cacheable(value = "jpaUser", key = "#email")
    public JpaUserDto getUserByEmail(String email) {
        final var jpaUser = userRepository.findByEmail(email).orElseThrow(
                () -> new ServiceException("No User Found with email: %s".formatted(email),HttpStatus.NOT_FOUND,"No User Found")
        );
        if (log.isDebugEnabled()) log.debug(USER_DETAIL, jpaUser);
        return mapper.map(jpaUser,JpaUserDto.class);
    }

    @Override
    @Cacheable(value = "jpaUser", key = "#uniqueName")
    public JpaUserDto getUserByUniqueName(String uniqueName) {
        final var jpaUser = userRepository.findByUniqueName(uniqueName).orElseThrow(
                () -> new ServiceException("No User Found with username: %s".formatted(uniqueName),HttpStatus.NOT_FOUND,"No User Found")
        );
        if (log.isDebugEnabled()) log.debug(USER_DETAIL, jpaUser);
        return mapper.map(jpaUser,JpaUserDto.class);
    }

}
