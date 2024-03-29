package fr.stonehaven.apigateway.api.response.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {

    private final String id;
    private final String email;
    private final String username;
    private final String role;
}
