package com.media.social.user.app;

import com.media.social.user.dto.RegisterDto;
import com.media.social.user.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.client.jaxrs.internal.FinalizedClientResponse;
import org.keycloak.admin.client.Keycloak;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class KeyCloakService {

    private final Keycloak keycloak;


    @Value("${auth.keycloak.config.realm}")
    private String realm;

    @Value("${auth.keycloak.config.default-role}")
    private String defaultRoleName;
    @Value("${auth.keycloak.config.client-id}")
    private String clientId;

    public void createUser(RegisterDto registerDto){
        var realmResource = keycloak.realm(realm);
        try (var response = realmResource.users().create(registerDto.toUserRepresentation(registerDto.getPassword()))) {
            var isUserCreated = Objects.equals(201, response.getStatus());
            if (!isUserCreated) {
                var reason = ((FinalizedClientResponse) response).getReasonPhrase();
                throw new ServiceException("user cannot be created, reason -> : " + reason);
            }else{
                // Get user ID
                var users = realmResource.users().search(registerDto.getUsername());
                if (users.isEmpty()) {
                    throw new ServiceException("user cannot be find by given username: " + registerDto.getUsername());
                }
                addDefaultRoles(users.get(0).getId());
            }

        }
    }

    private void addDefaultRoles(String userId){
        var realmResource = keycloak.realm(realm);

        // Get the client ID from the client name
        var clients = realmResource.clients().findByClientId(clientId);
        if (clients.isEmpty()) {
            throw new ServiceException("No client found with clientId: "+ clientId);
        }
        String clientUUID = clients.get(0).getId();

        // Get role representation
        var role = realmResource.clients().get(clientUUID).roles().get(defaultRoleName).toRepresentation();

        // Assign role to user
        var userResource = realmResource.users().get(userId);
        userResource.roles().clientLevel(clientUUID).add(Collections.singletonList(role));
    }




}
