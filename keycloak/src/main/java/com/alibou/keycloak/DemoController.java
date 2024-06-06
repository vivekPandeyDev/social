package com.alibou.keycloak;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo")
public class DemoController {

    @GetMapping
    public String guest() {

        return "Hello from Spring boot & Keycloak - Guest";
    }

    @GetMapping("/read")
    @PreAuthorize("hasAuthority('ROLE_READ')")
    public String hello() {

        return "Hello from Spring boot & Keycloak - User";
    }

    @GetMapping("/write")
    @PreAuthorize("hasRole('WRITE')")
    public String hello2() {
        return "Hello from Spring boot & Keycloak - ADMIN";
    }
}
