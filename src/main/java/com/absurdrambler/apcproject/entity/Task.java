package com.absurdrambler.apcproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Task entity representing todo tasks
 */
@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "is_important")
    private Boolean isImportant = false;

    @Column(name = "is_completed")
    private Boolean isCompleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id")
    private User assignee;

    // Constructor for easy creation
    public Task(String title, String description, LocalDate dueDate, Boolean isImportant, Boolean isCompleted, User assignee) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.isImportant = isImportant != null ? isImportant : false;
        this.isCompleted = isCompleted != null ? isCompleted : false;
        this.assignee = assignee;
    }
}
