package fr.stonehaven.apigateway.configuration;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> unsecuredEndpoints = List.of(
            "/eureka",
            "/auth/login"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> unsecuredEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));

}