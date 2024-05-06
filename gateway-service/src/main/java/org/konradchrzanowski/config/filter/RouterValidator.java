package org.konradchrzanowski.config.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouterValidator {

    public static final List<String> openEndpoints = List.of(
            "/api/auth/login",
            "/api/auth/register",
            "/api/auth/confirm",
            "/api/auth/request-password-reset",
            "/api/auth/reset-password",
            "/api/auth/validate-token",
            "/api/auth/token",
            "/api/auth/authenticate",
            "/api/test/all"
    );

    //todo maybe need to add token service as open endpoints above?

    public Predicate<ServerHttpRequest> isSecured =
            request -> openEndpoints.stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));


}
