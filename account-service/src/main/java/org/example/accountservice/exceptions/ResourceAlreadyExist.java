package org.example.accountservice.exceptions;

public class ResourceAlreadyExist extends RuntimeException {
    public String message;

    public ResourceAlreadyExist(String message) {
        super(message);
        this.message = message;
    }

}
