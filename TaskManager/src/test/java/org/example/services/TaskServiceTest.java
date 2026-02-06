package org.example.services;

import org.example.dtos.request.TaskDto;
import org.example.dtos.request.ValueDto;
import org.example.dtos.filter.TaskFilter;
import org.example.entitis.Comment;
import org.example.entitis.Task;
import org.example.enums.Priority;
import org.example.enums.Status;
import org.example.exception.TaskNotFoundException;
import org.example.mappers.impl.TaskMapper;
import org.example.repositories.FilterTaskRepository;
import org.example.repositories.TaskRepository;
import org.example.utils.UserPermissionChecker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.LinkedList;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private FilterTaskRepository filterTaskRepository;
    @Mock
    private UserPermissionChecker userPermissionChecker;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;
    @Spy
    private TaskMapper taskMapper = Mappers.getMapper(TaskMapper.class);
    @InjectMocks
    private TaskService taskService;

    @Test
    public void successfulCreateTask(){
        // Given
        TaskDto taskDto = new TaskDto(
                "title",
                "description",
                "COMPLETED",
                "MEDIUM",
                "Kirill@mail.ru"
        );
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("user1@mail.ru");

        // When
        taskService.createTask(taskDto);

        // Then
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    public void createTaskWithNotValidStatus(){
        // Given
        TaskDto taskDto = new TaskDto(
                "title",
                "description",
                "not_valid_status",
                "MEDIUM",
                "Kirill@mail.ru"
        );

        // When & Then
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                taskService.createTask(taskDto));
    }

    @Test
    public void createTaskWithNotValidPriority(){
        // Given
        TaskDto taskDto = new TaskDto(
                "title",
                "description",
                "COMPLETED",
                "not_valid_priority",
                "Kirill@mail.ru"
        );

        // When & Then
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                taskService.createTask(taskDto));
    }

    @Test
    public void successfulAddComment(){
        // Given
        String author = "user1@mail.ru";
        Comment comment = new Comment(1L, "text", author);
        Task oldTask = new Task(
                1L,
                "title",
                "description",
                Status.COMPLETED,
                Priority.MEDIUM,
                author,
                "Kirill@mail.ru",
                new LinkedList<>()
        );

        // When
        taskService.addComment(oldTask, comment);

        // Then
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    public void successfulUpdateStatus(){
        // Given
        Long taskId = 1L;
        ValueDto valueDto = new ValueDto("COMPLETED");
        Task oldTask = new Task(
                1L,
                "title",
                "description",
                Status.IN_PROGRESS,
                Priority.MEDIUM,
                "user1@mail.ru",
                "Kirill@mail.ru",
                new LinkedList<>()
        );
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(oldTask));

        // When
        taskService.updateStatus(taskId, valueDto);

        // Then
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    public void successfulUpdateStatusWithTaskNotFoundException(){
        // Given
        Long taskId = 1L;
        ValueDto valueDto = new ValueDto("COMPLETED");
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // When & Then
        Assertions.assertThrows(TaskNotFoundException.class, () ->
                taskService.updateStatus(taskId, valueDto));
    }

    @Test
    public void successfulUpdatePriority(){
        // Given
        Long taskId = 1L;
        ValueDto valueDto = new ValueDto("HIGH");
        Task oldTask = new Task(
                1L,
                "title",
                "description",
                Status.IN_PROGRESS,
                Priority.MEDIUM,
                "user1@mail.ru",
                "Kirill@mail.ru",
                new LinkedList<>()
        );
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(oldTask));

        // When
        taskService.updatePriority(taskId, valueDto);

        // Then
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    public void successfulUpdateExecutor(){
        // Given
        Long taskId = 1L;
        ValueDto valueDto = new ValueDto("new_executor");
        Task oldTask = new Task(
                1L,
                "title",
                "description",
                Status.IN_PROGRESS,
                Priority.MEDIUM,
                "user1@mail.ru",
                "Kirill@mail.ru",
                new LinkedList<>()
        );
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(oldTask));

        // When
        taskService.updateExecutor(taskId, valueDto);

        // Then
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    public void successfulDeleteTask() {
        // Given
        Long taskId = 1L;

        // When
        taskService.deleteTask(taskId);

        // Then
        verify(taskRepository).deleteById(taskId);
    }

    @Test
    public void successfulGetTasksByFilterHandler() {
        // Given
        TaskFilter taskFilter = new TaskFilter("author", "executor");

        // When
        taskService.getTasksByFilter(taskFilter, 0, 5);

        // Then
        verify(filterTaskRepository).findTaskByFilter(eq(taskFilter), anyInt(), anyInt());
    }


}
