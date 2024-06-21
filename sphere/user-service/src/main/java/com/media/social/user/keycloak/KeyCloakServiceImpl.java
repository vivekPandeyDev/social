package com.media.social.user.keycloak;

import com.media.social.user.dto.RegisterDto;
import com.media.social.user.exception.ServiceException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.client.jaxrs.internal.FinalizedClientResponse;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KeyCloakServiceImpl implements KeyCloakService {

    private final Keycloak keycloak;


    @Value("${auth.keycloak.config.realm}")
    private String realm;

    @Value("${auth.keycloak.config.default-role}")
    private String defaultRoleName;
    @Value("${auth.keycloak.config.client-id}")
    private String clientId;

    private RealmResource realmResource;


    public UUID createUser(RegisterDto registerDto) {
        try (var response = realmResource.users().create(registerDto.toUserRepresentation())) {
            var isUserCreated = Objects.equals(201, response.getStatus());
            if (!isUserCreated) {
                var reason = ((FinalizedClientResponse) response).getReasonPhrase();
                throw new ServiceException("user cannot be created, reason -> : " + reason);
            } else {
                // Get user ID
                var users = realmResource.users().search(registerDto.getUsername());
                if (users.isEmpty()) {
                    throw new ServiceException("user cannot be find by given username: " + registerDto.getUsername());
                }


                return UUID.fromString(users.get(0).getId());
            }

        }

    }


    public UserRepresentation getKeycloakUser(String username) {
        var users = realmResource.users().search(username);
        if (users.isEmpty()) {
            throw new ServiceException("user cannot be find by given username: " + username);
        }
        return users.get(0);
    }

    public RoleRepresentation getKeycloakRoles(String roleName) {
        var clientUUID = getClientId();
        return realmResource.clients().get(clientUUID).roles().get(roleName).toRepresentation();
    }

    public RoleRepresentation getKeycloakDefaultRoles() {
        return getKeycloakRoles(defaultRoleName);
    }

    public void addRoles(UUID userId, String roleName) {
        // Get the client ID from the client name
        var clientUUID = getClientId();

        // Get role representation
        var role = realmResource.clients().get(clientUUID).roles().get(roleName).toRepresentation();

        // Assign role to user
        var userResource = realmResource.users().get(userId.toString());
        userResource.roles().clientLevel(clientUUID).add(Collections.singletonList(role));
    }


    private String getClientId() {
        var clients = realmResource.clients().findByClientId(clientId);
        if (clients.isEmpty()) {
            throw new ServiceException("No client found with clientId: " + clientId);
        }
        return clients.get(0).getId();
    }




    @PostConstruct
    public void postConstruct() {
        realmResource = keycloak.realm(realm);
    }


}
