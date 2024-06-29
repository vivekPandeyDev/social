package com.vivek.events.config;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class KafkaProducerConfig {

    private KafkaProducerConfig() {
    }


    private static final KafkaProducer<String, String> producer = createProducer();
    private static KafkaProducer<String, String> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, getEnvVar("KAFKA_BOOTSTRAP_SERVERS", "localhost:9092"));
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new KafkaProducer<>(props);
    }

    public static KafkaProducer<String, String> getProducer() {
        return producer;
    }

    private static String getEnvVar(String varName, String defaultValue) {
        String value = System.getenv(varName);
        return value != null ? value : defaultValue;
    }
}
