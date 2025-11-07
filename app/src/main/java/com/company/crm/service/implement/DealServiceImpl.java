package com.company.crm.service.implement;

import com.company.crm.dao.interfaces.DealDao;
import com.company.crm.dao.interfaces.TaskDao;
import com.company.crm.model.Deal;
import com.company.crm.service.interfaces.DealService;
import com.company.crm.util.ValidationException;
import com.company.crm.util.ValidationUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class DealServiceImpl implements DealService {
    private final DealDao dao;
    private final TaskDao taskDao;

    public DealServiceImpl(DealDao dao, TaskDao taskDao) {
        this.dao = dao;
        this.taskDao = taskDao;
    }

    @Override
    public List<Deal> getAll() {
        return dao.findAll();
    }

    @Override
    public Optional<Deal> getById(int id) {
        if (id <= 0) {
            throw new ValidationException("Deal ID must be positive");
        }
        return dao.findById(id);
    }

    @Override
    public Deal create(Deal deal) {
        validateDeal(deal);
        validateTaskExists(deal.getTaskId());
        checkTaskUniqueness(deal.getTaskId()); // Новая проверка
        checkDealNumberUniqueness(deal.getDealNumber());
        return dao.save(deal);
    }

    @Override
    public void update(Deal deal) {
        if (deal.getId() == null || deal.getId() <= 0) {
            throw new ValidationException("Valid deal ID is required for update");
        }
        validateDeal(deal);
        validateTaskExists(deal.getTaskId());
        checkTaskUniquenessForUpdate(deal.getId(), deal.getTaskId()); // Новая проверка
        checkDealNumberUniquenessForUpdate(deal.getId(), deal.getDealNumber());
        dao.update(deal);
    }

    @Override
    public void delete(int id) {
        if (id <= 0) {
            throw new ValidationException("Deal ID must be positive");
        }
        dao.delete(id);
    }

    private void validateDeal(Deal deal) {
        // Обязательные поля
        ValidationUtils.validateRequired(deal.getDealNumber(), "Deal number");
        ValidationUtils.validateRequired(deal.getDealAmount(), "Deal amount");
        ValidationUtils.validateRequired(deal.getStatus(), "Status");

        // Валидация длины строк
        ValidationUtils.validateMaxLength(deal.getDealNumber(), 100, "Deal number");
        ValidationUtils.validateMaxLength(deal.getStatus(), 50, "Status");

        // Валидация числовых полей
        ValidationUtils.validatePositive(deal.getDealAmount(), "Deal amount");
        ValidationUtils.validateMinValue(deal.getDealAmount(), new BigDecimal("1000"), "Deal amount");

        // Комиссия может быть нулевой или положительной
        if (deal.getCommission() != null && deal.getCommission().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Commission cannot be negative");
        }

        // Валидация даты
        if (deal.getDealDate() != null) {
            LocalDate minDate = LocalDate.now().minusYears(10);
            if (deal.getDealDate().isBefore(minDate)) {
                throw new ValidationException("Deal date cannot be older than 10 years");
            }

            LocalDate maxDate = LocalDate.now().plusYears(5);
            if (deal.getDealDate().isAfter(maxDate)) {
                throw new ValidationException("Deal date cannot be more than 5 years in the future");
            }
        } else {
            deal.setDealDate(LocalDate.now());
        }

        // Валидация статуса
        validateStatus(deal.getStatus());

        // Task ID должен быть указан и положительным
        if (deal.getTaskId() == null) {
            throw new ValidationException("Task ID is required");
        }
        if (deal.getTaskId() <= 0) {
            throw new ValidationException("Task ID must be positive");
        }
    }

    private void validateTaskExists(Integer taskId) {
        if (taskId != null) {
            taskDao.findById(taskId)
                    .orElseThrow(() -> new ValidationException("Task not found with ID: " + taskId));
        }
    }

    private void validateStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new ValidationException("Status is required");
        }

        String[] validStatuses = {
                "active", "sold", "completed", "withdrawn in progress", "cancelled"
        };

        boolean isValid = false;
        for (String validStatus : validStatuses) {
            if (validStatus.equalsIgnoreCase(status.trim())) {
                isValid = true;
                break;
            }
        }

        if (!isValid) {
            throw new ValidationException("Invalid status. Allowed values: active, sold, completed, withdrawn in progress, cancelled");
        }
    }

    // НОВАЯ ПРОВЕРКА: Уникальность задачи при создании
    private void checkTaskUniqueness(Integer taskId) {
        if (taskId == null) {
            return;
        }

        dao.findAll().stream()
                .filter(deal -> taskId.equals(deal.getTaskId()))
                .findFirst()
                .ifPresent(deal -> {
                    throw new ValidationException("Task with ID " + taskId + " is already associated with deal '" +
                            deal.getDealNumber() + "'. One task can only have one deal.");
                });
    }


    private void checkTaskUniquenessForUpdate(int dealId, Integer taskId) {
        if (taskId == null) {
            return;
        }

        dao.findAll().stream()
                .filter(deal -> taskId.equals(deal.getTaskId()) && !deal.getId().equals(dealId))
                .findFirst()
                .ifPresent(deal -> {
                    throw new ValidationException("Task with ID " + taskId + " is already associated with deal '" +
                            deal.getDealNumber() + "'. One task can only have one deal.");
                });
    }

    private void checkDealNumberUniqueness(String dealNumber) {
        if (dealNumber == null || dealNumber.trim().isEmpty()) {
            return;
        }

        dao.findAll().stream()
                .filter(deal -> dealNumber.equalsIgnoreCase(deal.getDealNumber()))
                .findFirst()
                .ifPresent(deal -> {
                    throw new ValidationException("Deal with number '" + dealNumber + "' already exists");
                });
    }

    private void checkDealNumberUniquenessForUpdate(int dealId, String dealNumber) {
        if (dealNumber == null || dealNumber.trim().isEmpty()) {
            return;
        }

        dao.findAll().stream()
                .filter(deal -> dealNumber.equalsIgnoreCase(deal.getDealNumber()) && !deal.getId().equals(dealId))
                .findFirst()
                .ifPresent(deal -> {
                    throw new ValidationException("Deal with number '" + dealNumber + "' already exists");
                });
    }
}