package com.absurdrambler.apcproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for task creation and update requests
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {
    private String title;
    private String description;
    private LocalDate dueDate;
    private Boolean isImportant;
    private Boolean isCompleted;
    private Long assigneeId; // Only used by admins
}
