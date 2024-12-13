package org.example.descriptions;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.example.dtos.ErrorResponseDto;
import org.example.dtos.JwtRequest;
import org.example.dtos.JwtResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthApi {
    @Operation(summary = "User authorization", description = "Creating jwt tokens")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Creating jwt tokens",
                    content = @Content(schema = @Schema(implementation = JwtResponse.class)),
                    headers = @Header(
                            name = "Set-Cookie",
                            description = "Sets the 'refreshToken' cookie with the new refresh token. " +
                                    "Cookie attributes: HttpOnly, Max-Age=604800 (7 days).",
                            schema = @Schema(type = "string", example = "refreshToken=abc123; HttpOnly; Max-Age=604800")
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Incorrect data was transmitted",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Incorrect email or password",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Invalid jwt token",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public ResponseEntity<JwtResponse> createJwtTokens(@Valid @RequestBody JwtRequest jwtRequest);

    @Operation(summary = "Refresh tokens", description = "Updating jwt tokens")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Updating jwt tokens",
                    content = @Content(schema = @Schema(implementation = JwtResponse.class)),
                    headers = @Header(
                            name = "Set-Cookie",
                            description = "Sets the 'refreshToken' cookie with the new refresh token. " +
                                    "Cookie attributes: HttpOnly, Max-Age=604800 (7 days).",
                            schema = @Schema(type = "string", example = "refreshToken=abc123; HttpOnly; Max-Age=604800")
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Incorrect data was transmitted",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Refresh token is missing from the cookie",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Incorrect email or password",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Invalid jwt token",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public ResponseEntity<JwtResponse> refreshJwtTokens(
            @CookieValue(value = "refreshToken", required = false) String refreshTokenArg
    );
}
