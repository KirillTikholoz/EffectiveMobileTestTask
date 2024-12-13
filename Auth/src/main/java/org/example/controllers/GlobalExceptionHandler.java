package org.example.controllers;

import lombok.extern.slf4j.Slf4j;
import org.example.dtos.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(
                new ErrorResponseDto("Bad Request", "Incorrect data was transmitted"),
                HttpStatus.BAD_REQUEST
        );
    }
}
