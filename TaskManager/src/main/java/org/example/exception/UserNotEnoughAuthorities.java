package org.example.exception;

public class UserNotEnoughAuthorities extends RuntimeException {
    public UserNotEnoughAuthorities() {
        super("The user does not have sufficient permissions for this operation");
    }
    public UserNotEnoughAuthorities(String message) {
        super(message);
    }
    public UserNotEnoughAuthorities(String message, Throwable cause) {
        super(message, cause);
    }
    public UserNotEnoughAuthorities(Throwable cause) {
        super(cause);
    }
}
