package org.example.controllers;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.example.dtos.ErrorResponseDto;
import org.example.exception.TaskNotFoundException;
import org.example.exception.UserNotEnoughAuthorities;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            IllegalArgumentException.class,
            ConstraintViolationException.class
    })
    public ResponseEntity<ErrorResponseDto> incorrectDataExceptionHandler(Exception ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(
                new ErrorResponseDto("Bad Request", "Incorrect data was transmitted"),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({TaskNotFoundException.class})
    public ResponseEntity<ErrorResponseDto> taskNotFoundExceptionHandler(TaskNotFoundException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(
                new ErrorResponseDto("Not found", ex.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler({UserNotEnoughAuthorities.class})
    public ResponseEntity<ErrorResponseDto> userNotEnoughAuthorities(UserNotEnoughAuthorities ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(
                new ErrorResponseDto("Forbidden", ex.getMessage()),
                HttpStatus.FORBIDDEN
        );
    }
}
