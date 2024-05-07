package org.konradchrzanowski.config;

import org.konradchrzanowski.config.filter.AuthenticationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableHystrix
public class GatewayConfig {

    private final AuthenticationFilter filter;

    public GatewayConfig(AuthenticationFilter filter) {
        this.filter = filter;
    }
//here we configure all gateway routes for microservices instead of yml file

    //todo read about gatewayConfig with authenticationFilter - now i have error here after reimplementation of AuthFilter
    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r.path("/api/users/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://user-service"))
                .route("authentication-service", r -> r.path("/api/auth/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://authentication-service"))
                .route("email-service", r-> r.path("/api/email/**")
                        .filters(f-> f.filter(filter))
                        .uri("lb://email-service"))
                .route("token-service", r-> r.path("/api/token/**")
                        .uri("lb://token-service"))
                .build();
    }
}
