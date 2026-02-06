package org.example.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enums.Priority;
import org.example.enums.Status;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponseDto {
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private String author;
    private String executor;
    private List<CommentResponseDto> comments;
}
