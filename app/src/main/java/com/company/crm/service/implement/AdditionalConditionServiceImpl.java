package com.company.crm.service.implement;

import com.company.crm.dao.interfaces.AdditionalConditionDao;
import com.company.crm.model.AdditionalCondition;
import com.company.crm.service.interfaces.AdditionalConditionService;
import com.company.crm.util.ValidationException;
import com.company.crm.util.ValidationUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class AdditionalConditionServiceImpl implements AdditionalConditionService {
    private final AdditionalConditionDao dao;

    public AdditionalConditionServiceImpl(AdditionalConditionDao dao) {
        this.dao = dao;
    }

    @Override
    public List<AdditionalCondition> getAll() {
        return dao.findAll();
    }

    @Override
    public Optional<AdditionalCondition> getById(int id) {
        if (id <= 0) {
            throw new ValidationException("Additional condition ID must be positive");
        }
        return dao.findById(id);
    }

    @Override
    public AdditionalCondition create(AdditionalCondition condition) {
        validateCondition(condition);
        return dao.save(condition);
    }

    @Override
    public void update(AdditionalCondition condition) {
        if (condition.getId() == null || condition.getId() <= 0) {
            throw new ValidationException("Valid additional condition ID is required for update");
        }
        validateCondition(condition);
        dao.update(condition);
    }

    @Override
    public void delete(int id) {
        if (id <= 0) {
            throw new ValidationException("Additional condition ID must be positive");
        }
        dao.delete(id);
    }

    private void validateCondition(AdditionalCondition condition) {
        // Обязательные поля
        ValidationUtils.validateRequired(condition.getConditionType(), "Condition type");
        ValidationUtils.validateRequired(condition.getDescription(), "Description");
        ValidationUtils.validateRequired(condition.getStatus(), "Status");
        ValidationUtils.validateRequired(condition.getPriority(), "Priority");

        // Валидация длины строк
        ValidationUtils.validateMaxLength(condition.getConditionType(), 100, "Condition type");
        ValidationUtils.validateMaxLength(condition.getStatus(), 50, "Status");
        ValidationUtils.validateMaxLength(condition.getPriority(), 50, "Priority");

        // Валидация даты - может быть в прошлом или будущем
        if (condition.getDeadline() != null) {
            LocalDate minDate = LocalDate.now().minusYears(5);
            if (condition.getDeadline().isBefore(minDate)) {
                throw new ValidationException("Deadline cannot be older than 5 years");
            }

            LocalDate maxDate = LocalDate.now().plusYears(5);
            if (condition.getDeadline().isAfter(maxDate)) {
                throw new ValidationException("Deadline cannot be more than 5 years in the future");
            }
        }

        // Валидация типов условий
        validateConditionType(condition.getConditionType());

        // Валидация статусов
        validateStatus(condition.getStatus());

        // Валидация приоритетов
        validatePriority(condition.getPriority());
    }

    private void validateConditionType(String conditionType) {
        if (conditionType == null || conditionType.trim().isEmpty()) {
            throw new ValidationException("Condition type is required");
        }

        String[] validTypes = {"marketing", "administrative", "customer service", "legal", "technical"};

        boolean isValid = false;
        for (String validType : validTypes) {
            if (validType.equalsIgnoreCase(conditionType.trim())) {
                isValid = true;
                break;
            }
        }

        if (!isValid) {
            throw new ValidationException("Invalid condition type. Allowed values: marketing, administrative, customer service, legal, technical");
        }
    }

    private void validateStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new ValidationException("Status is required");
        }

        String[] validStatuses = {"completed", "active", "canceled", "in progress", "paused"};

        boolean isValid = false;
        for (String validStatus : validStatuses) {
            if (validStatus.equalsIgnoreCase(status.trim())) {
                isValid = true;
                break;
            }
        }

        if (!isValid) {
            throw new ValidationException("Invalid status. Allowed values: completed, active, canceled, in progress, paused");
        }
    }

    private void validatePriority(String priority) {
        if (priority == null || priority.trim().isEmpty()) {
            throw new ValidationException("Priority is required");
        }

        String[] validPriorities = {"high", "medium", "low"};

        boolean isValid = false;
        for (String validPriority : validPriorities) {
            if (validPriority.equalsIgnoreCase(priority.trim())) {
                isValid = true;
                break;
            }
        }

        if (!isValid) {
            throw new ValidationException("Invalid priority. Allowed values: high, medium, low");
        }
    }


}