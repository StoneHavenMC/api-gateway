package fr.stonehaven.apigateway.api.response.user;

import fr.stonehaven.authentication.entity.User;
import lombok.Getter;

@Getter
public class UserResponse {

    private final String id;
    private final String email;
    private final String username;
    private final String role;

    public UserResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getName();
        this.role = user.getRole().getId();
    }
}
