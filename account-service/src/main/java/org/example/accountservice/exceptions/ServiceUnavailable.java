package org.example.accountservice.exceptions;

public class ServiceUnavailable extends RuntimeException {
    String message;;
    public ServiceUnavailable(String message) {
        super(message);
        this.message = message;
    }
}
