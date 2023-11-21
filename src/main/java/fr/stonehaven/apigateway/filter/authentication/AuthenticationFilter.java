package fr.stonehaven.apigateway.filter.authentication;

import com.google.gson.Gson;
import fr.stonehaven.apigateway.api.response.user.UserResponse;
import fr.stonehaven.apigateway.configuration.RouteValidator;
import fr.stonehaven.apigateway.exception.AccessForbiddenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Value("${services.authentication.url}")
    private String authenticationServiceUrl;

    private final RestTemplate template;
    private final RouteValidator validator;

    public AuthenticationFilter(RestTemplate template, RouteValidator validator) {
        super(Config.class);
        this.template = template;
        this.validator = validator;
    }

    @Override
    public GatewayFilter apply(Config config) throws AccessForbiddenException {
        return ((exchange, chain) -> {
            ServerHttpRequest request = null;
            if (validator.isSecured.test(exchange.getRequest())) {
                //header contains token or not
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new AccessForbiddenException(HttpStatus.FORBIDDEN);
                }

                try {
                    //REST call to AUTH service
                    HttpEntity<String> entity = new HttpEntity<>(null, exchange.getRequest().getHeaders());
                    String response = template.exchange(authenticationServiceUrl + "/auth/user", HttpMethod.GET, entity, String.class).getBody();
                    UserResponse userResponse = new Gson().fromJson(response, UserResponse.class);
                    if (userResponse == null) throw new AccessForbiddenException(HttpStatus.FORBIDDEN);


                    request = exchange.getRequest()
                            .mutate()
                            .header("X-SH-USER-EMAIL", userResponse.getEmail())
                            .header("X-SH-USER-NAME", userResponse.getUsername())
                            .header("X-SH-USER-ROLE", userResponse.getRole())
                            .build();

                } catch (Exception e) {
                    e.printStackTrace();
                    throw new AccessForbiddenException(HttpStatus.FORBIDDEN);
                }
            }
            return chain.filter(exchange.mutate().request(request).build());
        });
    }

    public static class Config {

    }
}
