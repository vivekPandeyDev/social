package com.media.social.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;

@SpringBootApplication
@Slf4j
public class UserServiceApplication implements CommandLineRunner {


    public static void main(String[] args) {

        SpringApplication.run(UserServiceApplication.class, args);

    }


    @Override
    public void run(String... args) throws Exception {
        for (int i = 1; i <= 10; i++) {
            log.info("UUID: {}", UUID.randomUUID());
        }
    }
}
