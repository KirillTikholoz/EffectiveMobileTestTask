package org.example.dtos.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
    @NotEmpty
    private String title;

    @NotEmpty
    private String description;

    @NotEmpty
    private String status;

    @NotEmpty
    private String priority;

    @NotEmpty
    private String executor;
}
