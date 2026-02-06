package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.description.TaskApi;
import org.example.dtos.response.SuccessfulResponseDto;
import org.example.dtos.request.TaskDto;
import org.example.dtos.request.ValueDto;
import org.example.dtos.filter.TaskFilter;
import org.example.dtos.response.TaskResponseDto;
import org.example.entitis.Task;
import org.example.services.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/tasks")
public class TaskController implements TaskApi {
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<SuccessfulResponseDto> createTaskHandler(@Valid @RequestBody TaskDto taskDto){
        taskService.createTask(taskDto);
        return new ResponseEntity<>(
                new SuccessfulResponseDto("The task has been successfully created"),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{task_id}")
    public ResponseEntity<Task> getTaskHandler(@PathVariable("task_id") Long taskId) {
        Task task = taskService.readTask(taskId);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @PostMapping("/filter")
    public List<TaskResponseDto> getTasksByFilterHandler(
            @RequestBody TaskFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return taskService.getTasksByFilter(filter, page, size);
    }

    @PutMapping("/{task_id}")
    public ResponseEntity<SuccessfulResponseDto> updateTaskHandler(@PathVariable("task_id") Long taskId,
                                                                   @Valid @RequestBody TaskDto taskDto) {
        taskService.updateTask(taskId, taskDto);
        return new ResponseEntity<>(
                new SuccessfulResponseDto("The task has been successfully updated"),
                HttpStatus.OK
        );
    }

    @PatchMapping("/{task_id}/status")
    public ResponseEntity<SuccessfulResponseDto> updateStatusHandler(@PathVariable("task_id") Long taskId,
                                                                     @Valid @RequestBody ValueDto valueDto) {
        taskService.updateStatus(taskId, valueDto);
        return new ResponseEntity<>(
                new SuccessfulResponseDto("The status has been successfully updated"),
                HttpStatus.OK
        );
    }

    @PatchMapping("/{task_id}/priority")
    public ResponseEntity<SuccessfulResponseDto> updatePriorityHandler(@PathVariable("task_id") Long taskId,
                                                                       @Valid @RequestBody ValueDto valueDto) {
        taskService.updatePriority(taskId, valueDto);
        return new ResponseEntity<>(
                new SuccessfulResponseDto("The priority has been successfully updated"),
                HttpStatus.OK
        );
    }

    @PatchMapping("/{task_id}/executor")
    public ResponseEntity<SuccessfulResponseDto> updateExecutorHandler(@PathVariable("task_id") Long taskId,
                                                                       @Valid @RequestBody ValueDto valueDto) {
        taskService.updateExecutor(taskId, valueDto);
        return new ResponseEntity<>(
                new SuccessfulResponseDto("The executor has been successfully updated"),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{task_id}")
    public ResponseEntity<SuccessfulResponseDto> deleteTaskHandler(@PathVariable("task_id") Long taskId) {
        taskService.deleteTask(taskId);
        return new ResponseEntity<>(
                new SuccessfulResponseDto("The task was successfully deleted"),
                HttpStatus.OK
        );
    }
}
