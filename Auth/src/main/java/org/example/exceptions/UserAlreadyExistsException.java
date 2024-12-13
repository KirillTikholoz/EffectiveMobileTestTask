package org.example.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {
        super("User not found");
    }
    public UserAlreadyExistsException(String message) {
        super(message);
    }
    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
    public UserAlreadyExistsException(Throwable cause) {
        super(cause);
    }
}
