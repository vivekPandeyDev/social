package com.media.social.user.app;

import com.media.social.user.dto.RegisterDto;
import com.media.social.user.dto.UserDto;
import com.media.social.user.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private static final String USER_DETAIL = "user details -> {}";

    @Override
    public UserDto saveUser(RegisterDto registerDto) {
        if (isUserExist(registerDto.getUniqueName(), registerDto.getEmail())) {
            log.error("Cannot save user as email or username is already present in database {}", registerDto.getEmail());
            throw new ServiceException("User is already present choose different email or username!", HttpStatus.FOUND, "User Already exists");
        }
        User user = mapper.map(registerDto, User.class);
        if (log.isDebugEnabled()) log.debug(USER_DETAIL, user);
        final var savedUser = userRepository.save(user);
        return mapper.map(savedUser, UserDto.class);
    }

    @Override
    @Cacheable(value = "user", key = "#email")
    public UserDto getUserByEmail(String email) {
        final var savedUser = userRepository.findByEmail(email).orElseThrow(
                () -> new ServiceException("No User Found with email: %s".formatted(email), HttpStatus.NOT_FOUND, "No User Found")
        );
        if (log.isDebugEnabled()) log.debug(USER_DETAIL, savedUser);
        return mapper.map(savedUser, UserDto.class);
    }

    @Override
    @Cacheable(value = "user", key = "#uniqueName")
    public UserDto getUserByUniqueName(String uniqueName) {
        final var savedUser = userRepository.findByUniqueName(uniqueName).orElseThrow(
                () -> new ServiceException("No User Found with username: %s".formatted(uniqueName), HttpStatus.NOT_FOUND, "No User Found")
        );
        if (log.isDebugEnabled()) log.debug(USER_DETAIL, savedUser);
        return mapper.map(savedUser, UserDto.class);
    }

    @Override
    public boolean isUserExist(String uniqueName, String email) {
        return Boolean.TRUE.equals(userRepository.existsByEmail(email)) ||
                Boolean.TRUE.equals(userRepository.existsByUniqueName(uniqueName));
    }
}
