package com.social.user;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class UserServiceApplication implements CommandLineRunner {
	public static void main(String[] args) throws IOException {

		SpringApplication.run(UserServiceApplication.class, args);

	}


	@Override
	public void run(String... args) throws Exception {

	}
}
