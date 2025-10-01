package com.absurdrambler.apcproject.dto;

import com.absurdrambler.apcproject.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for task responses
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private Boolean isImportant;
    private Boolean isCompleted;
    private String assigneeName;

    public static TaskResponse fromEntity(Task task) {
        return new TaskResponse(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.getDueDate(),
            task.getIsImportant(),
            task.getIsCompleted(),
            task.getAssignee() != null ? task.getAssignee().getUsername() : null
        );
    }
}
