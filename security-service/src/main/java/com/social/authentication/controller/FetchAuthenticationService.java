package com.social.authentication.controller;

import com.social.authentication.exception.ServiceException;
import com.social.authentication.response.ApiResponse;
import com.social.authentication.security.JWTGenerator;
import com.social.authentication.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Objects;

import static com.social.authentication.util.RequestUtil.getResponseType;

@Service
@RequiredArgsConstructor
@Slf4j
public class FetchAuthenticationService implements AuthService {

    private final RestClient restClient;
    private final JWTGenerator jwtGenerator;
    private final AuthenticationManager authenticationManager;


    @Override
    public JpaUserDto getUserByEmail(String email){
        ParameterizedTypeReference<ApiResponse<JpaUserDto>> responseParameterizedTypeReference = new ParameterizedTypeReference<ApiResponse<JpaUserDto>>() {};

        var jpaUserDtoResponseEntity =  restClient
                .get()
                .uri("/users/emails/" + email)
                .retrieve()
                .toEntity(responseParameterizedTypeReference);

        if(jpaUserDtoResponseEntity.getStatusCode().is2xxSuccessful()){
            return Objects.requireNonNull(jpaUserDtoResponseEntity.getBody()).data().get("user");
        }else{
            throw new ServiceException("No User Found with email: %s".formatted(email), HttpStatus.NOT_FOUND,"No User Found");
        }
    }

    @Override
    public String getAuthToken(LoginDto loginDto){
        JpaUserDto savedUser = getUserByEmail(loginDto.getEmail());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(savedUser.getUniqueName(), loginDto.getPassword())
        );
        final var token = jwtGenerator.generateToken(authentication);
        if (log.isDebugEnabled()) log.info("token details -> {}", token);
        return token;
    }

    @Override
    public String validateToken(String token) {
        if(!jwtGenerator.validateToken(token)){
            return "";
        }else{
            return jwtGenerator.getUsernameFromJWT(token);
        }
    }
}
