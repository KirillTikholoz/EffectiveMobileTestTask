package org.example.services;

import org.example.dtos.request.CommentDto;
import org.example.entitis.Comment;
import org.example.entitis.Task;
import org.example.enums.Priority;
import org.example.enums.Status;
import org.example.mappers.impl.CommentMapper;
import org.example.utils.UserPermissionChecker;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
    @Mock
    private TaskService taskService;

    @Mock
    private UserPermissionChecker userPermissionChecker;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Spy
    private CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);

    @InjectMocks
    private CommentService commentService;

    @Test
    public void successfulCreateComment(){
        // Given
        String author = "user1@mail.ru";
        CommentDto commentDto = new CommentDto(1L, "text");
        Task task = new Task(
                1L,
                "title",
                "description",
                Status.COMPLETED,
                Priority.MEDIUM,
                author,
                "Kirill@mail.ru",
                new LinkedList<>()
        );
        SecurityContextHolder.setContext(securityContext);

        when(taskService.readTask(anyLong())).thenReturn(task);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("user1@mail.ru");

        // When
        commentService.createComment(commentDto);

        // Then
        verify(taskService).addComment(eq(task), any(Comment.class));
    }
}
