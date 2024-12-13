package org.example.services;

import org.example.dtos.TaskDto;
import org.example.dtos.ValueDto;
import org.example.entitis.Comment;
import org.example.entitis.Task;
import org.example.enums.Priority;
import org.example.enums.Status;
import org.example.exception.TaskNotFoundException;
import org.example.repositories.TaskRepository;
import org.example.utils.UserPermissionChecker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserPermissionChecker userPermissionChecker;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;
    @InjectMocks
    private TaskService taskService;

    @Test
    public void successfulCreateTask(){
        Task task = new Task(
                null,
                "title",
                "description",
                Status.COMPLETED,
                Priority.MEDIUM,
                "user1@mail.ru",
                "Kirill@mail.ru",
                null
        );
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

        taskService.createTask(taskDto);
        verify(taskRepository).save(task);
    }

    @Test
    public void createTaskWithNotValidStatus(){
        TaskDto taskDto = new TaskDto(
                "title",
                "description",
                "not_valid_status",
                "MEDIUM",
                "Kirill@mail.ru"
        );

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                taskService.createTask(taskDto));
    }

    @Test
    public void createTaskWithNotValidPriority(){
        TaskDto taskDto = new TaskDto(
                "title",
                "description",
                "COMPLETED",
                "not_valid_priority",
                "Kirill@mail.ru"
        );

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                taskService.createTask(taskDto));
    }

    @Test
    public void successfulAddComment(){
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
        Task newTask = new Task(
                1L,
                "title",
                "description",
                Status.COMPLETED,
                Priority.MEDIUM,
                author,
                "Kirill@mail.ru",
                List.of(comment)
        );

        taskService.addComment(oldTask, comment);
        verify(taskRepository).save(newTask);
    }

    @Test
    public void successfulUpdateStatus(){
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
        Task newTask = new Task(
                1L,
                "title",
                "description",
                Status.valueOf(valueDto.getValue()),
                Priority.MEDIUM,
                "user1@mail.ru",
                "Kirill@mail.ru",
                new LinkedList<>()
        );

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(oldTask));

        taskService.updateStatus(taskId, valueDto);
        verify(taskRepository).save(newTask);
    }

    @Test
    public void successfulUpdateStatusWithTaskNotFoundException(){
        Long taskId = 1L;
        ValueDto valueDto = new ValueDto("COMPLETED");

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        Assertions.assertThrows(TaskNotFoundException.class, () ->
                taskService.updateStatus(taskId, valueDto));
    }

    @Test
    public void successfulUpdatePriority(){
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
        Task newTask = new Task(
                1L,
                "title",
                "description",
                Status.IN_PROGRESS,
                Priority.valueOf(valueDto.getValue()),
                "user1@mail.ru",
                "Kirill@mail.ru",
                new LinkedList<>()
        );

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(oldTask));

        taskService.updatePriority(taskId, valueDto);
        verify(taskRepository).save(newTask);
    }

    @Test
    public void successfulUpdateExecutor(){
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
        Task newTask = new Task(
                1L,
                "title",
                "description",
                Status.IN_PROGRESS,
                Priority.MEDIUM,
                "user1@mail.ru",
                valueDto.getValue(),
                new LinkedList<>()
        );

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(oldTask));

        taskService.updateExecutor(taskId, valueDto);
        verify(taskRepository).save(newTask);
    }

    @Test
    public void successfulDeleteTask() {
        Long taskId = 1L;

        taskService.deleteTask(taskId);
        verify(taskRepository).deleteById(taskId);
    }

}
