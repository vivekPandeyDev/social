package com.vivek.social;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;

@SpringBootApplication
public class SocialApplication  implements CommandLineRunner {

	@Autowired
	ResourceLoader resourceLoader;

	public static void main(String[] args) throws IOException {

		SpringApplication.run(SocialApplication.class, args);

	}


	@Override
	public void run(String... args) throws Exception {

	}
}
