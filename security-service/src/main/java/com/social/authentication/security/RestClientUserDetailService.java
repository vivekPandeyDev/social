package com.social.authentication.security;

import com.social.authentication.controller.JpaUserDto;
import com.social.authentication.response.ApiResponse;
import com.social.authentication.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Objects;

import static com.social.authentication.util.RequestUtil.getResponseType;


@RequiredArgsConstructor
@Service
public class RestClientUserDetailService implements UserDetailsService {

    private final RestClient restClient;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Cacheable(value = "jpaUser", key = "#uniqueName")
    public UserDetails loadUserByUsername(String uniqueName) throws UsernameNotFoundException {
        ParameterizedTypeReference<ApiResponse<JpaUserDto>> responseParameterizedTypeReference = new ParameterizedTypeReference<ApiResponse<JpaUserDto>>() {};
        ResponseEntity<ApiResponse<JpaUserDto>> response = restClient
                .get()
                .uri("/users/"+uniqueName)
                .retrieve()
                .toEntity(responseParameterizedTypeReference);
        if(response.getStatusCode().is2xxSuccessful()){
            var userMap = Objects.requireNonNull(response.getBody()).data().get("user");
            return User.withUsername(userMap.getUniqueName()).password(userMap.getPassword()).build();
        }else{
            throw new UsernameNotFoundException("No User found with given user name: " + uniqueName);
        }
    }
}
