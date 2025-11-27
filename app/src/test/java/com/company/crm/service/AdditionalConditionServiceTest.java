package com.company.crm.service;

import com.company.crm.config.AppConfig;
import com.company.crm.model.AdditionalCondition;
import com.company.crm.service.interfaces.AdditionalConditionService;
import com.company.crm.util.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AdditionalConditionServiceTest {
    private AdditionalConditionService conditionService;
    private AppConfig appConfig;

    @BeforeEach
    void setUp() {
        appConfig = new AppConfig();
        conditionService = appConfig.getAdditionalConditionService();
    }

    @Test
    void testCreateCondition_WithValidData_ShouldSuccess() {
        AdditionalCondition condition = createTestCondition("marketing", "completed", "high");

        AdditionalCondition created = conditionService.create(condition);

        assertNotNull(created, "Условие должно быть успешно создано");
        assertTrue(created.isRequired(), "Флаг обязательности должен быть true");
        assertEquals("high", created.getPriority(), "Приоритет должен быть 'high'");
        assertEquals("marketing", created.getConditionType(), "Тип условия должен быть 'marketing'");
    }

    @Test
    void testCreateCondition_WithPastDeadline_ShouldWork() {
        AdditionalCondition condition = createTestCondition("administrative", "completed", "medium");
        condition.setDeadline(LocalDate.now().minusDays(1));

        assertDoesNotThrow(() -> conditionService.create(condition),
                "Создание условия с прошедшим дедлайном должно работать");
    }

    @Test
    void testCreateCondition_WithTodayDeadline_ShouldSuccess() {
        AdditionalCondition condition = createTestCondition("customer service", "completed", "low");
        condition.setDeadline(LocalDate.now());

        assertDoesNotThrow(() -> conditionService.create(condition),
                "Создание условия с дедлайном на сегодня должно работать");
    }

    @Test
    void testUpdateCondition_ChangeStatusAndPriority_ShouldUpdate() {
        AdditionalCondition condition = conditionService.create(createTestCondition("marketing", "active", "high"));
        condition.setStatus("completed");
        condition.setPriority("low");

        conditionService.update(condition);
        AdditionalCondition updated = conditionService.getById(condition.getId()).orElse(null);

        assertNotNull(updated, "Обновленное условие должно существовать");
        assertEquals("completed", updated.getStatus(), "Статус должен быть 'выполнено'");
        assertEquals("low", updated.getPriority(), "Приоритет должен быть 'low'");
    }

    @Test
    void testConditionWithLongText_ShouldWork() {
        AdditionalCondition condition = createTestCondition("administrative", "active", "medium");
        condition.setDescription("A".repeat(1000));
        condition.setNotes("B".repeat(2000));

        assertDoesNotThrow(() -> conditionService.create(condition),
                "Создание условия с длинным текстом должно работать");
    }

    @Test
    void testDeleteCondition_ShouldRemoveFromSystem() {
        AdditionalCondition condition = conditionService.create(createTestCondition("marketing", "active", "high"));
        int conditionId = condition.getId();

        conditionService.delete(conditionId);
        AdditionalCondition deleted = conditionService.getById(conditionId).orElse(null);

        assertNull(deleted, "Условие не должно существовать после удаления");
    }

    @Test
    void testCreateCondition_WithInvalidConditionType_ShouldThrowValidationException() {
        AdditionalCondition condition = createTestCondition("InvalidType", "active", "high");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> conditionService.create(condition),
                "Создание условия с невалидным типом должно вызывать ValidationException");

        assertTrue(exception.getMessage().contains("Invalid condition type"),
                "Сообщение об ошибке должно указывать на невалидный тип условия");
    }

    @Test
    void testCreateCondition_WithInvalidStatus_ShouldThrowValidationException() {
        AdditionalCondition condition = createTestCondition("marketing", "InvalidStatus", "high");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> conditionService.create(condition),
                "Создание условия с невалидным статусом должно вызывать ValidationException");

        assertTrue(exception.getMessage().contains("Invalid status"),
                "Сообщение об ошибке должно указывать на невалидный статус");
    }

    @Test
    void testCreateCondition_WithInvalidPriority_ShouldThrowValidationException() {
        AdditionalCondition condition = createTestCondition("marketing", "active", "InvalidPriority");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> conditionService.create(condition),
                "Создание условия с невалидным приоритетом должно вызывать ValidationException");

        assertTrue(exception.getMessage().contains("Invalid priority"),
                "Сообщение об ошибке должно указывать на невалидный приоритет");
    }

    @Test
    void testCreateCondition_WithValidConditionTypes_ShouldSuccess() {
        String[] validTypes = {"marketing", "administrative", "customer service", "legal", "technical"};

        for (String type : validTypes) {
            AdditionalCondition condition = createTestCondition(type, "active", "medium");
            assertDoesNotThrow(() -> conditionService.create(condition),
                    "Создание условия с типом '" + type + "' должно работать");
        }
    }

    @Test
    void testCreateCondition_WithValidStatuses_ShouldSuccess() {
        String[] validStatuses = {"completed", "active", "canceled", "in progress", "paused"};

        for (String status : validStatuses) {
            AdditionalCondition condition = createTestCondition("marketing", status, "medium");
            assertDoesNotThrow(() -> conditionService.create(condition),
                    "Создание условия со статусом '" + status + "' должно работать");
        }
    }

    @Test
    void testCreateCondition_WithValidPriorities_ShouldSuccess() {
        String[] validPriorities = {"high", "medium", "low"};

        for (String priority : validPriorities) {
            AdditionalCondition condition = createTestCondition("marketing", "active", priority);
            assertDoesNotThrow(() -> conditionService.create(condition),
                    "Создание условия с приоритетом '" + priority + "' должно работать");
        }
    }

    @Test
    void testCreateCondition_WithRequiredFieldsMissing_ShouldThrowValidationException() {
        AdditionalCondition condition = new AdditionalCondition();

        ValidationException exception = assertThrows(ValidationException.class,
                () -> conditionService.create(condition),
                "Создание условия без обязательных полей должно вызывать ValidationException");

        assertNotNull(exception.getMessage(), "Должно быть сообщение об ошибке валидации");
    }

    private AdditionalCondition createTestCondition(String conditionType, String status, String priority) {
        AdditionalCondition condition = new AdditionalCondition();
        condition.setConditionType(conditionType);
        condition.setDescription("Test description");
        condition.setDeadline(LocalDate.now().plusDays(30));
        condition.setRequired(true);
        condition.setStatus(status);
        condition.setPriority(priority);
        condition.setNotes("Test notes");
        return condition;
    }
}