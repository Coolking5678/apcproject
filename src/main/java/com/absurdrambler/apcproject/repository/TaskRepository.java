package com.absurdrambler.apcproject.repository;

import com.absurdrambler.apcproject.entity.Task;
import com.absurdrambler.apcproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for Task entity operations
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Find all tasks assigned to a specific user
     * @param assignee the user to find tasks for
     * @return list of tasks assigned to the user
     */
    List<Task> findByAssignee(User assignee);

    /**
     * Find all important tasks assigned to a specific user
     * @param assignee the user to find tasks for
     * @param isImportant true to find important tasks
     * @return list of important tasks assigned to the user
     */
    List<Task> findByAssigneeAndIsImportant(User assignee, Boolean isImportant);

    /**
     * Find all tasks assigned to a specific user with a specific due date
     * @param assignee the user to find tasks for
     * @param dueDate the due date to filter by
     * @return list of tasks assigned to the user with the specified due date
     */
    List<Task> findByAssigneeAndDueDate(User assignee, LocalDate dueDate);

    /**
     * Find task by ID and assignee (for security - users can only access their own tasks)
     * @param id the task ID
     * @param assignee the assigned user
     * @return list containing the task if found and belongs to the user
     */
    List<Task> findByIdAndAssignee(Long id, User assignee);
}
