package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dtos.CommentDto;
import org.example.entitis.Comment;
import org.example.entitis.Task;
import org.example.utils.UserPermissionChecker;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final TaskService taskService;
    private final UserPermissionChecker userPermissionChecker;

    public void createComment(CommentDto commentDto){
        Task task = taskService.readTask(commentDto.getTaskId());

        userPermissionChecker.checkUserPermissions(task);

        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setAuthor(SecurityContextHolder.getContext().getAuthentication().getName());
        taskService.addComment(task, comment);
    }
}
