package com.company.crm.service;

import com.company.crm.config.AppConfig;
import com.company.crm.model.Deal;
import com.company.crm.model.Task;
import com.company.crm.service.interfaces.DealService;
import com.company.crm.service.interfaces.TaskService;
import com.company.crm.util.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DealServiceTest {
    private DealService dealService;
    private TaskService taskService;
    private AppConfig appConfig;
    private List<Integer> createdTaskIds = new ArrayList<>();

    @BeforeEach
    void setUp() {
        appConfig = new AppConfig();
        dealService = appConfig.getDealService();
        taskService = appConfig.getTaskService();
        clearAllDeals();
        clearCreatedTasks();
    }

    private void clearAllDeals() {
        List<Deal> allDeals = dealService.getAll();
        for (Deal deal : allDeals) {
            dealService.delete(deal.getId());
        }
    }

    private void clearCreatedTasks() {
        for (Integer taskId : createdTaskIds) {
            try {
                taskService.delete(taskId);
            } catch (Exception e) {
            }
        }
        createdTaskIds.clear();
    }

    private int createTestTask() {
        Task task = new Task();
        task.setTitle("Test Task for Deal " + System.currentTimeMillis());
        task.setDescription("Test task description");
        task.setDueDate(LocalDateTime.now().plusDays(7));
        task.setPriority("Medium");
        task.setStatus("Pending");
        task.setResponsibleId(1);
        task.setCreatorId(1);

        Task createdTask = taskService.create(task);
        createdTaskIds.add(createdTask.getId());
        return createdTask.getId();
    }

    @Test
    void testCreateDeal_WithCommission_ShouldSuccess() {
        int taskId = createTestTask();
        Deal deal = createTestDeal("DEAL-001", new BigDecimal("5000.00"), taskId);

        Deal created = dealService.create(deal);

        assertNotNull(created);
        assertEquals("DEAL-001", created.getDealNumber());
        assertEquals(0, new BigDecimal("5000.00").compareTo(created.getCommission()));
    }

    @Test
    void testCreateDeal_WithZeroAmount_ShouldThrowValidationException() {
        int taskId = createTestTask();
        Deal deal = createTestDeal("DEAL-002", new BigDecimal("1000.00"), taskId);
        deal.setDealAmount(BigDecimal.ZERO);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> dealService.create(deal));

        assertTrue(exception.getMessage().contains("Deal amount"));
    }

    @Test
    void testCreateDeal_WithNullRequiredFields_ShouldThrowValidationException() {
        Deal deal = new Deal();
        deal.setDealNumber("DEAL-003");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> dealService.create(deal));

        assertTrue(exception.getMessage().contains("is required"));
    }

    @Test
    void testCreateDeal_WithOnlyRequiredFields_ShouldSuccess() {
        int taskId = createTestTask();
        Deal deal = new Deal();
        deal.setDealNumber("DEAL-004");
        deal.setDealAmount(new BigDecimal("100000"));
        deal.setStatus("active");
        deal.setTaskId(taskId);

        assertDoesNotThrow(() -> dealService.create(deal));
    }

    @Test
    void testUpdateDeal_ChangeStatus_ShouldUpdate() {
        int taskId = createTestTask();
        Deal deal = dealService.create(createTestDeal("DEAL-005", new BigDecimal("3000.00"), taskId));
        deal.setStatus("completed");

        dealService.update(deal);
        Deal updated = dealService.getById(deal.getId()).orElse(null);

        assertNotNull(updated);
        assertEquals("completed", updated.getStatus());
    }

    @Test
    void testMultipleDeals_DifferentTaskIds_ShouldWork() {
        int taskId1 = createTestTask();
        int taskId2 = createTestTask();

        dealService.create(createTestDeal("DEAL-006", new BigDecimal("1000.00"), taskId1));
        dealService.create(createTestDeal("DEAL-007", new BigDecimal("2000.00"), taskId2));

        List<Deal> allDeals = dealService.getAll();

        assertTrue(allDeals.size() >= 2);
    }

    @Test
    void testCreateDeal_WithNegativeCommission_ShouldThrowValidationException() {
        int taskId = createTestTask();
        Deal deal = createTestDeal("DEAL-008", new BigDecimal("-1000.00"), taskId);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> dealService.create(deal));

        assertTrue(exception.getMessage().contains("Commission cannot be negative"));
    }

    @Test
    void testCreateDeal_WithNullCommission_ShouldWork() {
        int taskId = createTestTask();
        Deal deal = createTestDeal("DEAL-009", null, taskId);
        deal.setCommission(null);

        assertDoesNotThrow(() -> dealService.create(deal));
    }

    @Test
    void testCreateDeal_WithPastDate_ShouldWork() {
        int taskId = createTestTask();
        Deal deal = createTestDeal("DEAL-010", new BigDecimal("1000.00"), taskId);
        deal.setDealDate(LocalDate.now().minusDays(1));

        assertDoesNotThrow(() -> dealService.create(deal),
                "Сделки с прошедшей датой должны быть разрешены");
    }

    @Test
    void testCreateDeal_WithDuplicateNumber_ShouldThrowValidationException() {
        int taskId = createTestTask();
        Deal deal1 = createTestDeal("DEAL-DUPLICATE", new BigDecimal("1000.00"), taskId);
        dealService.create(deal1);

        int taskId2 = createTestTask();
        Deal deal2 = createTestDeal("DEAL-DUPLICATE", new BigDecimal("2000.00"), taskId2);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> dealService.create(deal2));

        assertTrue(exception.getMessage().contains("already exists"));
    }

    @Test
    void testCreateDeal_WithNonExistentTask_ShouldThrowValidationException() {
        Deal deal = createTestDeal("DEAL-011", new BigDecimal("1000.00"), 99999);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> dealService.create(deal));

        assertTrue(exception.getMessage().contains("Task not found"));
    }

    @Test
    void testCreateDeal_WithValidStatuses_ShouldSuccess() {
        String[] validStatuses = {"active", "sold", "completed", "withdrawn in progress", "cancelled"};

        for (String status : validStatuses) {
            int taskId = createTestTask();
            Deal deal = createTestDeal("DEAL-" + System.currentTimeMillis() + "-" + status, new BigDecimal("1000.00"), taskId);
            deal.setStatus(status);
            assertDoesNotThrow(() -> dealService.create(deal),
                    "Создание сделки со статусом '" + status + "' должно работать");
        }
    }

    @Test
    void testCreateDeal_WithDuplicateTask_ShouldThrowValidationException() {
        int sharedTaskId = createTestTask();

        Deal deal1 = createTestDeal("DEAL-012", new BigDecimal("1000.00"), sharedTaskId);
        dealService.create(deal1);

        Deal deal2 = createTestDeal("DEAL-013", new BigDecimal("2000.00"), sharedTaskId);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> dealService.create(deal2));

        assertTrue(exception.getMessage().contains("already associated"));
    }

    @Test
    void testUpdateDeal_ChangeTaskIdToUsedOne_ShouldThrowValidationException() {
        int taskId1 = createTestTask();
        int taskId2 = createTestTask();

        // Создаем две сделки с разными taskId
        Deal deal1 = createTestDeal("DEAL-014", new BigDecimal("1000.00"), taskId1);
        dealService.create(deal1);

        Deal deal2 = createTestDeal("DEAL-015", new BigDecimal("2000.00"), taskId2);
        Deal createdDeal2 = dealService.create(deal2);

        // Пытаемся изменить taskId второй сделки на уже использованный
        createdDeal2.setTaskId(taskId1);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> dealService.update(createdDeal2));

        assertTrue(exception.getMessage().contains("already associated"));
    }

    private Deal createTestDeal(String dealNumber, BigDecimal commission, int taskId) {
        Deal deal = new Deal();
        deal.setDealNumber(dealNumber);
        deal.setTaskId(taskId);
        deal.setDealAmount(new BigDecimal("150000.00"));
        deal.setDealDate(LocalDate.now().plusDays(1));
        deal.setCommission(commission);
        deal.setStatus("active");
        return deal;
    }
}