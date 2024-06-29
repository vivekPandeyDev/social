package com.vivek.events;

import com.vivek.events.config.KafkaProducerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.jboss.logging.Logger;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

public class UserRegisterEventListenerProvider implements EventListenerProvider {
    private static final Logger log = Logger.getLogger(UserRegisterEventListenerProvider.class);
    private final KeycloakSession keycloakSession;
    private final KafkaProducer<String, String> kafkaProducer;

    public UserRegisterEventListenerProvider(KeycloakSession session) {
        this.keycloakSession = session;
        this.kafkaProducer =  KafkaProducerConfig.getProducer();
    }

    @Override
    public void onEvent(Event event) {
        if (event.getType() == EventType.REGISTER ) {
            RealmModel realm = keycloakSession.realms().getRealm(event.getRealmId());
            UserModel user = keycloakSession.users().getUserById(realm,event.getUserId());
            if (user != null) {
                log.info("***************** oauth type user register   **********************");
                try {
                    kafkaProducer.send(new ProducerRecord<>("oauth-user-register-event", event.getUserId(), event.getUserId()));
                    log.info("OAuth user registration event published to Kafka: ");
                } catch (Exception e) {
                    log.error("Error serializing user details", e);
                }
            }
        }
    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean b) {

    }

    @Override
    public void close() {

    }
}
