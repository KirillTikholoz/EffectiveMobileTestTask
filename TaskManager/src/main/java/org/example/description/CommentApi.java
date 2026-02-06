package org.example.description;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.example.dtos.request.CommentDto;
import org.example.dtos.response.ErrorResponseDto;
import org.example.dtos.response.SuccessfulResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface CommentApi {
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully create comment",
                    content = @Content(schema = @Schema(implementation = SuccessfulResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Incorrect data was transmitted",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public ResponseEntity<SuccessfulResponseDto> createCommentHandler(@Valid @RequestBody CommentDto commentDto);
}
