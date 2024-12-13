package org.example.exceptions;

public class PasswordMismatchException extends RuntimeException {
    public PasswordMismatchException() {
        super("Passwords don't match");
    }
    public PasswordMismatchException(String message) {
        super(message);
    }
    public PasswordMismatchException(String message, Throwable cause) {
        super(message, cause);
    }
    public PasswordMismatchException(Throwable cause) {
        super(cause);
    }
}
