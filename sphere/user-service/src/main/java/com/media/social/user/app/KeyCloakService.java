package com.media.social.user.app;

import com.media.social.user.dto.RegisterDto;
import com.media.social.user.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class KeyCloakService {

    private final Keycloak keycloak;


    @Value("${auth.keycloak.config.realm}")
    private String realm;

    public UserRepresentation createUser(RegisterDto registerDto){
        var realmResource = keycloak.realm(realm);
        var response = realmResource.users().create(registerDto.toUserRepresentation(registerDto.getPassword()));
        var isUserCreated = Objects.equals(201,response.getStatus());
        if(isUserCreated){
            return response.readEntity(UserRepresentation.class);
        }else{
            throw new ServiceException("Cannot create user: ");
        }


    }




}
