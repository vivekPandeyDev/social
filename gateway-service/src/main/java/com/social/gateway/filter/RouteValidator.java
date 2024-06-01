package com.social.gateway.filter;

import org.springframework.stereotype.Component;

import java.util.List;
import org.springframework.http.server.reactive.ServerHttpRequest;
import java.util.function.Predicate;
@Component
public class RouteValidator {

    private RouteValidator() {
    }

    public static final List<String> openApiEndpoints = List.of(
            "/auth/register",
            "/auth/login",
            "/auth/token",
            "/eureka"
    );

    public static final Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));

}
