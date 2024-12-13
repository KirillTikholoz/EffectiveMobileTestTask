package org.example.descriptions;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.example.dtos.ErrorResponseDto;
import org.example.dtos.RegistrationUserDto;
import org.example.dtos.SuccessfulUserRegistrationDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface RegistrationApi {
    @Operation(summary = "User registration", description = "Saving users in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully saved",
                    content = @Content(schema = @Schema(implementation = SuccessfulUserRegistrationDto.class))),
            @ApiResponse(responseCode = "400", description = "Incorrect data was transmitted",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Password and confirmation password do not match",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Such a user already exists",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public ResponseEntity<SuccessfulUserRegistrationDto> registrationUser(
            @Valid @RequestBody RegistrationUserDto registrationUserDto
    );

    @Operation(summary = "Admin registration", description = "Saving admins in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Admin successfully saved",
                    content = @Content(schema = @Schema(implementation = SuccessfulUserRegistrationDto.class))),
            @ApiResponse(responseCode = "400", description = "Incorrect data was transmitted",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Password and confirmation password do not match",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Such a user already exists",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
    })
    public ResponseEntity<SuccessfulUserRegistrationDto> registrationAdmin(
            @Valid @RequestBody RegistrationUserDto registrationUserDto
    );
}
