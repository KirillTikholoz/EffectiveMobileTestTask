package org.example.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SuccessfulUserRegistrationDto {
    private final String message;
}
