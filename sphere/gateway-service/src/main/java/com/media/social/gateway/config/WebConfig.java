package com.media.social.gateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClient;

@Configuration
public class WebConfig {
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String tokenApiBaseUrl;
    @Bean
    RestClient.Builder restClient(ObjectMapper objectMapper){
        return RestClient
                .builder()
                .baseUrl(tokenApiBaseUrl)
                .messageConverters( c -> c.add(0,customConverters(objectMapper)));
    }

    private HttpMessageConverter<?> customConverters(ObjectMapper objectMapper) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);
        return converter;
    }
}
