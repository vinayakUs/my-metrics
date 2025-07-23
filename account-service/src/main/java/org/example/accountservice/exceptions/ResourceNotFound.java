package org.example.accountservice.exceptions;

public class ResourceNotFound extends RuntimeException {
    final String message;

    public ResourceNotFound(String message) {
        super(message);
        this.message = message;
    }
}
