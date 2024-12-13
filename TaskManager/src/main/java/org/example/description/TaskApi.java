package org.example.description;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.example.dtos.ErrorResponseDto;
import org.example.dtos.SuccessfulResponseDto;
import org.example.dtos.TaskDto;
import org.example.dtos.ValueDto;
import org.example.entitis.Task;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface TaskApi {
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task successfully saved",
                    content = @Content(schema = @Schema(implementation = SuccessfulResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Incorrect data was transmitted",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public ResponseEntity<SuccessfulResponseDto> createTaskHandler(@Valid @RequestBody TaskDto taskDto);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully get task",
                    content = @Content(schema = @Schema(implementation = Task.class))),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public ResponseEntity<Task> getTaskHandler(@PathVariable("task_id") Long taskId);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully get tasks",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "400", description = "Incorrect data was transmitted",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public Page<Task> getTasksByAuthorHandler(
            @RequestParam @NotEmpty String author,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    );

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully get tasks",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "400", description = "Incorrect data was transmitted",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public Page<Task> getTasksByExecutorHandler(
            @RequestParam @NotEmpty String executor,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    );

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully update task",
                    content = @Content(schema = @Schema(implementation = SuccessfulResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Incorrect data was transmitted",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public ResponseEntity<SuccessfulResponseDto> updateTaskHandler(@PathVariable("task_id") Long taskId,
                                                                   @Valid @RequestBody TaskDto taskDto);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully update task",
                    content = @Content(schema = @Schema(implementation = SuccessfulResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Incorrect data was transmitted",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "The user does not have enough authorities",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public ResponseEntity<SuccessfulResponseDto> updateStatusHandler(@PathVariable("task_id") Long taskId,
                                                                     @Valid @RequestBody ValueDto valueDto);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully update task",
                    content = @Content(schema = @Schema(implementation = SuccessfulResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Incorrect data was transmitted",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public ResponseEntity<SuccessfulResponseDto> updatePriorityHandler(@PathVariable("task_id") Long taskId,
                                                                       @Valid @RequestBody ValueDto valueDto);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully update task",
                    content = @Content(schema = @Schema(implementation = SuccessfulResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Incorrect data was transmitted",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public ResponseEntity<SuccessfulResponseDto> updateExecutorHandler(@PathVariable("task_id") Long taskId,
                                                                       @Valid @RequestBody ValueDto valueDto);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully delete task",
                    content = @Content(schema = @Schema(implementation = SuccessfulResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public ResponseEntity<SuccessfulResponseDto> deleteTaskHandler(@PathVariable("task_id") Long taskId);
}
