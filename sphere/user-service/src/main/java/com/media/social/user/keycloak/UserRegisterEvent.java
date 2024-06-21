package com.media.social.user.keycloak;


import com.media.social.user.app.User;
import com.media.social.user.app.UserRepository;
import com.media.social.user.app.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.media.social.user.keycloak.UserUtility.toUserDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserRegisterEvent {

    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final KeyCloakService keyCloakService;


    @KafkaListener(topics = "oauth-user-register-event", groupId = "oauth-user-group-1")
    public void consumeUserEvent(String username) {
        log.info("user id: {}", username);
        var userRep = keyCloakService.getKeycloakUser(username);
        var role = keyCloakService.getKeycloakDefaultRoles();
        var userDto =  UserUtility.setDefaultRoles(toUserDto(userRep),role);
        userRepository.save(mapper.map(userDto, User.class));

    }
}
