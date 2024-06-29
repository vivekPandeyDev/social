package com.vivek.events;

import org.jboss.logging.Logger;
import org.keycloak.Config;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;


public class UserEventListenerProviderFactory implements EventListenerProviderFactory {


    private static final Logger log = Logger.getLogger(UserEventListenerProviderFactory.class);

    @Override
    public EventListenerProvider create(KeycloakSession keycloakSession) {
        return new UserRegisterEventListenerProvider(keycloakSession);
    }

    @Override
    public void init(Config.Scope scope) {
        log.info("init the userEventListenerProviderFactory");
    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {

    }

    @Override
    public void close() {

        log.info("closing the userEventListenerProviderFactory");
    }

    @Override
    public String getId() {
        return "event-streaming";
    }
}
