package org.example.services;

import org.example.mappers.impl.TaskMapper;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.dtos.*;
import org.example.entitis.Comment;
import org.example.entitis.Task;
import org.example.enums.Priority;
import org.example.enums.Status;
import org.example.exception.TaskNotFoundException;
import org.example.repositories.TaskRepository;
import org.example.utils.UserPermissionChecker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserPermissionChecker userPermissionChecker;
    private final TaskMapper taskMapper;

    @Transactional
    public void createTask(TaskDto taskDto){
        Task task = taskMapper.toEntity(taskDto);
        task.setAuthor(SecurityContextHolder.getContext().getAuthentication().getName());
        taskRepository.save(task);
    }

    @Transactional
    public void addComment(Task task, Comment comment){
        task.getComments().add(comment);
        taskRepository.save(task);
    }

    @Transactional(readOnly = true)
    public Task readTask(Long id){
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task not found"));
    }

    @Transactional(readOnly = true)
    public Page<Task> getTasksByAuthor(String author, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return taskRepository.findByAuthor(author, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Task> getTasksByExecutor(String executor, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return taskRepository.findByExecutor(executor, pageable);
    }

    @Transactional
    public void updateTask(Long taskId, TaskDto taskDto){
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        taskMapper.updateFromDto(taskDto, task);
        task.setAuthor(SecurityContextHolder.getContext().getAuthentication().getName());
        taskRepository.save(task);
    }

    @Transactional
    public void updateStatus(Long taskId, ValueDto valueDto){
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        userPermissionChecker.checkUserPermissions(task);

        task.setStatus(Status.valueOf(valueDto.getValue().toUpperCase()));
        taskRepository.save(task);
    }

    @Transactional
    public void updatePriority(Long taskId, ValueDto valueDto){
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        task.setPriority(Priority.valueOf(valueDto.getValue().toUpperCase()));
        taskRepository.save(task);
    }

    @Transactional
    public void updateExecutor(Long taskId, ValueDto valueDto){
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        task.setExecutor(valueDto.getValue());
        taskRepository.save(task);
    }

    @Transactional
    public void deleteTask(Long id){
        taskRepository.deleteById(id);
    }
}
