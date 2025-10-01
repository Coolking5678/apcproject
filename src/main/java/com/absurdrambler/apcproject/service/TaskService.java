package com.absurdrambler.apcproject.service;

import com.absurdrambler.apcproject.entity.Task;
import com.absurdrambler.apcproject.entity.User;
import com.absurdrambler.apcproject.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service class for Task-related business logic
 */
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    /**
     * Get all tasks assigned to a user
     * @param user the user to get tasks for
     * @return list of tasks assigned to the user
     */
    public List<Task> getTasksAssignedToUser(User user) {
        return taskRepository.findByAssignee(user);
    }

    /**
     * Get all important tasks assigned to a user
     * @param user the user to get tasks for
     * @return list of important tasks assigned to the user
     */
    public List<Task> getImportantTasksForUser(User user) {
        return taskRepository.findByAssigneeAndIsImportant(user, true);
    }

    /**
     * Get all tasks assigned to a user due today
     * @param user the user to get tasks for
     * @return list of tasks assigned to the user due today
     */
    public List<Task> getDailyTasksForUser(User user) {
        return taskRepository.findByAssigneeAndDueDate(user, LocalDate.now());
    }

    /**
     * Create a new task
     * @param task the task to create
     * @return the created task
     */
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    /**
     * Update an existing task
     * @param task the task to update
     * @return the updated task
     */
    public Task updateTask(Task task) {
        return taskRepository.save(task);
    }

    /**
     * Find a task by ID
     * @param id the task ID
     * @return Optional containing the task if found
     */
    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    /**
     * Find a task by ID that belongs to a specific user (for security)
     * @param id the task ID
     * @param user the user who should own the task
     * @return Optional containing the task if found and belongs to the user
     */
    public Optional<Task> findByIdAndUser(Long id, User user) {
        List<Task> tasks = taskRepository.findByIdAndAssignee(id, user);
        return tasks.isEmpty() ? Optional.empty() : Optional.of(tasks.get(0));
    }

    /**
     * Delete a task
     * @param task the task to delete
     */
    public void deleteTask(Task task) {
        taskRepository.delete(task);
    }

    /**
     * Delete a task by ID
     * @param id the ID of the task to delete
     */
    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }
}
