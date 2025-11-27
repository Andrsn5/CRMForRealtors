package com.company.crm.service;

import com.company.crm.config.AppConfig;
import com.company.crm.model.Task;
import com.company.crm.service.interfaces.TaskService;
import com.company.crm.util.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {
    private TaskService taskService;
    private AppConfig appConfig;

    @BeforeEach
    void setUp() {
        appConfig = new AppConfig();
        taskService = appConfig.getTaskService();
    }

    @Test
    void testCreateTask_WithFutureDueDate_ShouldSuccess() {
        Task task = createTestTask("High", LocalDateTime.now().plusDays(7));

        Task created = taskService.create(task);

        assertNotNull(created);
        assertEquals("High", created.getPriority());
        assertNotNull(created.getDueDate());
    }




    @Test
    void testCreateTask_WithNullDueDate_ShouldSuccess() {
        Task task = createTestTask("Low", null);

        assertDoesNotThrow(() -> taskService.create(task));
    }

    @Test
    void testCreateTask_WithCurrentDueDate_ShouldSuccess() {
        Task task = createTestTask("Medium", LocalDateTime.now().plusHours(1));

        assertDoesNotThrow(() -> taskService.create(task));
    }

    @Test
    void testUpdateTask_ChangePriorityAndStatus_ShouldUpdate() {
        Task task = taskService.create(createTestTask("Medium", LocalDateTime.now().plusDays(5)));
        task.setPriority("High");
        task.setStatus("In Progress");

        taskService.update(task);
        Task updated = taskService.getById(task.getId()).orElse(null);

        assertNotNull(updated);
        assertEquals("High", updated.getPriority());
        assertEquals("In Progress", updated.getStatus());
    }

    @Test
    void testGetAllTasks_MixedStatuses_ShouldReturnAll() {
        int initialCount = taskService.getAll().size();

        taskService.create(createTestTask("High", LocalDateTime.now().plusDays(1)));
        taskService.create(createTestTask("Medium", LocalDateTime.now().plusDays(3)));
        taskService.create(createTestTask("Low", null));

        List<Task> allTasks = taskService.getAll();

        assertEquals(initialCount + 3, allTasks.size());
    }

    @Test
    void testTaskWithNullResponsibleId_ShouldThrowValidationException() {
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setPriority("Medium");
        task.setStatus("Pending");
        // responsibleId is null

        ValidationException exception = assertThrows(ValidationException.class,
                () -> taskService.create(task));

        assertEquals("Responsible employee is required", exception.getMessage());
    }


    @Test
    void testCreateTask_WithInvalidPriority_ShouldThrowValidationException() {
        Task task = createTestTask("InvalidPriority", LocalDateTime.now().plusDays(1));

        ValidationException exception = assertThrows(ValidationException.class,
                () -> taskService.create(task));

        assertTrue(exception.getMessage().contains("Invalid priority"));
    }

    @Test
    void testCreateTask_WithValidPriorities_ShouldSuccess() {
        String[] validPriorities = {"High", "Medium", "Low"};

        for (String priority : validPriorities) {
            Task task = createTestTask(priority, LocalDateTime.now().plusDays(1));
            assertDoesNotThrow(() -> taskService.create(task));
        }
    }

    @Test
    void testCreateTask_WithNullTitle_ShouldThrowValidationException() {
        Task task = createTestTask("Medium", LocalDateTime.now().plusDays(1));
        task.setTitle(null);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> taskService.create(task));

        assertEquals("Title is required", exception.getMessage());
    }

    @Test
    void testCreateTask_WithEmptyTitle_ShouldThrowValidationException() {
        Task task = createTestTask("Medium", LocalDateTime.now().plusDays(1));
        task.setTitle("");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> taskService.create(task));

        assertEquals("Title is required", exception.getMessage());
    }

    @Test
    void testCreateTask_WithLongTitle_ShouldThrowValidationException() {
        Task task = createTestTask("Medium", LocalDateTime.now().plusDays(1));
        task.setTitle("A".repeat(256));

        ValidationException exception = assertThrows(ValidationException.class,
                () -> taskService.create(task));

        assertTrue(exception.getMessage().contains("Title exceeds maximum length"));
    }

    @Test
    void testCreateTask_WithNonExistentResponsible_ShouldThrowValidationException() {
        Task task = createTestTask("Medium", LocalDateTime.now().plusDays(1));
        task.setResponsibleId(99999);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> taskService.create(task));

        assertTrue(exception.getMessage().contains("Responsible employee not found"));
    }

    @Test
    void testCreateTask_WithNonExistentClient_ShouldThrowValidationException() {
        Task task = createTestTask("Medium", LocalDateTime.now().plusDays(1));
        task.setClientId(99999);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> taskService.create(task));

        assertTrue(exception.getMessage().contains("Client not found"));
    }

    @Test
    void testCreateTask_WithNonExistentObject_ShouldThrowValidationException() {
        Task task = createTestTask("Medium", LocalDateTime.now().plusDays(1));
        task.setObjectId(99999);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> taskService.create(task));

        assertTrue(exception.getMessage().contains("Object not found"));
    }

    @Test
    void testCreateTask_WithNullClientAndObject_ShouldSuccess() {
        Task task = createTestTask("Medium", LocalDateTime.now().plusDays(1));
        task.setClientId(null);
        task.setObjectId(null);

        assertDoesNotThrow(() -> taskService.create(task));
    }

    @Test
    void testDeleteTask_ShouldRemoveFromSystem() {
        Task task = taskService.create(createTestTask("Medium", LocalDateTime.now().plusDays(1)));
        int taskId = task.getId();

        taskService.delete(taskId);
        Task deleted = taskService.getById(taskId).orElse(null);

        assertNull(deleted);
    }



    private Task createTestTask(String priority, LocalDateTime dueDate) {
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setDueDate(dueDate);
        task.setPriority(priority);
        task.setStatus("Pending");
        task.setResponsibleId(1);
        task.setCreatorId(1);
        task.setClientId(1);
        task.setObjectId(1);
        return task;
    }
}