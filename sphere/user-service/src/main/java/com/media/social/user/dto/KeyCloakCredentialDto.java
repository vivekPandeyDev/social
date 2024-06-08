package com.media.social.user.dto;

import lombok.Data;
import org.keycloak.representations.idm.CredentialRepresentation;

@Data
public class KeyCloakCredentialDto {
    private String type = CredentialRepresentation.PASSWORD;
    private String value;
    private boolean temporary = false;

    public KeyCloakCredentialDto(String value) {
        this.value = value;
    }

    public CredentialRepresentation toCredentialRepresentation() {
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(this.type);
        credentialRepresentation.setValue(this.value);
        credentialRepresentation.setTemporary(this.temporary);
        return credentialRepresentation;
    }
}
