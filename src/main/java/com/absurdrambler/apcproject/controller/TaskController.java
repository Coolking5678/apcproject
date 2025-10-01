package com.absurdrambler.apcproject.controller;

import com.absurdrambler.apcproject.dto.TaskRequest;
import com.absurdrambler.apcproject.dto.TaskResponse;
import com.absurdrambler.apcproject.entity.Task;
import com.absurdrambler.apcproject.entity.User;
import com.absurdrambler.apcproject.service.TaskService;
import com.absurdrambler.apcproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller for task management endpoints
 */
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    /**
     * Get all tasks assigned to the current user
     */
    @GetMapping("/assigned-to-me")
    public ResponseEntity<List<TaskResponse>> getTasksAssignedToMe() {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(401).build();
        }

        List<Task> tasks = taskService.getTasksAssignedToUser(currentUser);
        List<TaskResponse> taskResponses = tasks.stream()
                .map(TaskResponse::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(taskResponses);
    }

    /**
     * Get all important tasks assigned to the current user
     */
    @GetMapping("/important")
    public ResponseEntity<List<TaskResponse>> getImportantTasks() {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(401).build();
        }

        List<Task> tasks = taskService.getImportantTasksForUser(currentUser);
        List<TaskResponse> taskResponses = tasks.stream()
                .map(TaskResponse::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(taskResponses);
    }

    /**
     * Get all tasks assigned to the current user due today
     */
    @GetMapping("/daily")
    public ResponseEntity<List<TaskResponse>> getDailyTasks() {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(401).build();
        }

        List<Task> tasks = taskService.getDailyTasksForUser(currentUser);
        List<TaskResponse> taskResponses = tasks.stream()
                .map(TaskResponse::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(taskResponses);
    }

    /**
     * Create a new task
     */
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest taskRequest) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(401).build();
        }

        Task task = new Task();
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setDueDate(taskRequest.getDueDate());
        task.setIsImportant(taskRequest.getIsImportant() != null ? taskRequest.getIsImportant() : false);
        task.setIsCompleted(taskRequest.getIsCompleted() != null ? taskRequest.getIsCompleted() : false);

        // Admin can assign to any user, regular user can only assign to themselves
        if (isAdmin(currentUser) && taskRequest.getAssigneeId() != null) {
            Optional<User> assignee = userService.findById(taskRequest.getAssigneeId());
            if (assignee.isPresent()) {
                task.setAssignee(assignee.get());
            } else {
                return ResponseEntity.badRequest().build();
            }
        } else {
            // Regular user or admin without assigneeId - assign to current user
            task.setAssignee(currentUser);
        }

        Task createdTask = taskService.createTask(task);
        return ResponseEntity.ok(TaskResponse.fromEntity(createdTask));
    }

    /**
     * Update an existing task
     */
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @RequestBody TaskRequest taskRequest) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(401).build();
        }

        Optional<Task> taskOptional;
        if (isAdmin(currentUser)) {
            taskOptional = taskService.findById(id);
        } else {
            taskOptional = taskService.findByIdAndUser(id, currentUser);
        }

        if (taskOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Task task = taskOptional.get();
        if (taskRequest.getTitle() != null) {
            task.setTitle(taskRequest.getTitle());
        }
        if (taskRequest.getDescription() != null) {
            task.setDescription(taskRequest.getDescription());
        }
        if (taskRequest.getDueDate() != null) {
            task.setDueDate(taskRequest.getDueDate());
        }
        if (taskRequest.getIsImportant() != null) {
            task.setIsImportant(taskRequest.getIsImportant());
        }
        if (taskRequest.getIsCompleted() != null) {
            task.setIsCompleted(taskRequest.getIsCompleted());
        }

        Task updatedTask = taskService.updateTask(task);
        return ResponseEntity.ok(TaskResponse.fromEntity(updatedTask));
    }

    /**
     * Delete a task
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(401).build();
        }

        Optional<Task> taskOptional;
        if (isAdmin(currentUser)) {
            taskOptional = taskService.findById(id);
        } else {
            taskOptional = taskService.findByIdAndUser(id, currentUser);
        }

        if (taskOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        taskService.deleteTask(taskOptional.get());
        return ResponseEntity.noContent().build();
    }

    /**
     * Helper method to get the current authenticated user
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            return userService.findByUsername(username).orElse(null);
        }
        return null;
    }

    /**
     * Helper method to check if user is admin
     */
    private boolean isAdmin(User user) {
        return user != null && "ROLE_ADMIN".equals(user.getRole());
    }
}
