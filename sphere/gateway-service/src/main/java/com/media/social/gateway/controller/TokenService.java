package com.media.social.gateway.controller;

import com.media.social.gateway.domain.Token;
import jakarta.ws.rs.core.MultivaluedHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {


    @Value("${jwt.auth.converter.resource-id}")
    private String clientId;

    private final RestClient.Builder restBuilder;

    public Token getToken(String username,String password){
        MultiValueMap<String,String> headerMap = new LinkedMultiValueMap<>();
        headerMap.add("Content-Type",MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        String requestBody = String.format("grant_type=password&username=%s&password=%s&client_id=%s",
                username, password, clientId);


        var response = restBuilder.build().post()
                .uri("/protocol/openid-connect/token")
                .headers( httpHeaders -> httpHeaders.addAll(headerMap))
                .body(requestBody)
                .retrieve()
                .onStatus(HttpStatusCode::isError,this::handleError)
                .toEntity(Token.class);

       return response.getBody();
    }

    private void handleError(HttpRequest req, ClientHttpResponse resp) throws IOException {
        String result = new BufferedReader(new InputStreamReader(resp.getBody()))
                .lines().parallel().collect(Collectors.joining("\n"));
        throw new RestClientException(result);
    }

}
