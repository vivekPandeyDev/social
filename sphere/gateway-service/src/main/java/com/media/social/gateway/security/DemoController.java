package com.media.social.gateway.security;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/demo")
public class DemoController {

    @GetMapping
    public String guest() {

        return "Hello from Spring boot & Keycloak - Guest";
    }

    @GetMapping("/read")
    @PreAuthorize("hasAuthority('ROLE_READ')")
    public Mono<String> hello() {

        return Mono.just("Hello from Spring boot & Keycloak - User");
    }

    @GetMapping("/write")
    @PreAuthorize("hasRole('WRITE')")
    public Mono<String> hello2() {
        return Mono.just("Hello from Spring boot & Keycloak - ADMIN");
    }
}
