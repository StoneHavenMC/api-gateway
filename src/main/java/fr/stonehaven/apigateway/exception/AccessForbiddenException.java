package fr.stonehaven.apigateway.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class AccessForbiddenException extends ResponseStatusException {


    public AccessForbiddenException(HttpStatusCode status) {
        super(status);
    }

    public AccessForbiddenException(HttpStatusCode status, String reason) {
        super(status, reason);
    }
}
