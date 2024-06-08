package com.media.social.user.webconfig;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:config.properties")
@EnableDiscoveryClient
//@EnableCaching
public class WebConfiguration {

	@Value("${auth.keycloak.config.realm}")
	private String realm;
	@Value("${auth.keycloak.config.domain}")
	private String baseUrl;

	@Value("${auth.keycloak.config.admin-client-id}")
	private String adminClientId;

	@Value("${auth.keycloak.config.admin-client-secret}")
	private String adminClientSecret;

	@Bean
	@Primary
	ModelMapper modelMapper() {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setFieldMatchingEnabled(true).setMatchingStrategy(MatchingStrategies.LOOSE);
		return mapper;
	}



	@Bean
	Keycloak keycloak(){
		return KeycloakBuilder
				.builder()
				.realm(realm)
				.serverUrl(baseUrl)
				.clientId(adminClientId)
				.clientSecret(adminClientSecret)
				.grantType(OAuth2Constants.CLIENT_CREDENTIALS)
				.build();
	}
}
