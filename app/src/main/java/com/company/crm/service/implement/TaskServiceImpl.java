package com.company.crm.service.implement;

import com.company.crm.dao.interfaces.*;
import com.company.crm.model.Task;
import com.company.crm.service.interfaces.TaskService;
import com.company.crm.util.ValidationException;
import com.company.crm.util.ValidationUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class TaskServiceImpl implements TaskService {
    private final TaskDao dao;
    private final EmployeeDao employeeDao;
    private final ClientDao clientDao;
    private final ObjectDao objectDao;
    private final AdditionalConditionDao conditionDao;
    private final DealDao dealDao;
    private final MeetingDao meetingDao;

    public TaskServiceImpl(TaskDao dao, EmployeeDao employeeDao, ClientDao clientDao,
                           ObjectDao objectDao, AdditionalConditionDao conditionDao,
                           DealDao dealDao, MeetingDao meetingDao) {
        this.dao = dao;
        this.employeeDao = employeeDao;
        this.clientDao = clientDao;
        this.objectDao = objectDao;
        this.conditionDao = conditionDao;
        this.dealDao = dealDao;
        this.meetingDao = meetingDao;
    }

    @Override
    public List<Task> getAll() {
        return dao.findAll();
    }

    @Override
    public Optional<Task> getById(int id) {
        if (id <= 0) {
            throw new ValidationException("Task ID must be positive");
        }
        return dao.findById(id);
    }

    @Override
    public Task create(Task task) {
        validateTask(task);
        validateForeignKeys(task);
        checkUniqueForeignKeys(task); // Проверка уникальности внешних ключей
        return dao.save(task);
    }

    @Override
    public void update(Task task) {
        if (task.getId() == null || task.getId() <= 0) {
            throw new ValidationException("Valid task ID is required for update");
        }
        validateTask(task);
        validateForeignKeys(task);
        checkUniqueForeignKeysForUpdate(task);
        dao.update(task);
    }

    @Override
    public void delete(int id) {
        if (id <= 0) {
            throw new ValidationException("Task ID must be positive");
        }
        dao.delete(id);
    }

    private void validateTask(Task task) {
        ValidationUtils.validateRequired(task.getTitle(), "Title");
        ValidationUtils.validateRequired(task.getPriority(), "Priority");
        ValidationUtils.validateRequired(task.getStatus(), "Status");
        ValidationUtils.validateRequired(task.getResponsibleId(), "Responsible employee");
        ValidationUtils.validateRequired(task.getCreatorId(), "Creator employee");

        ValidationUtils.validateMaxLength(task.getTitle(), 255, "Title");
        ValidationUtils.validateMaxLength(task.getPriority(), 50, "Priority");
        ValidationUtils.validateMaxLength(task.getStatus(), 50, "Status");

        // Валидация даты - может быть в прошлом или будущем
        if (task.getDueDate() != null) {
            LocalDateTime minDateTime = LocalDateTime.now().minusYears(1);
            if (task.getDueDate().isBefore(minDateTime)) {
                throw new ValidationException("Due date cannot be older than 1 year");
            }

            LocalDateTime maxDateTime = LocalDateTime.now().plusYears(1);
            if (task.getDueDate().isAfter(maxDateTime)) {
                throw new ValidationException("Due date cannot be more than 1 year in the future");
            }
        }

        // Валидация приоритета
        validatePriority(task.getPriority());

        // Валидация статуса
        validateStatus(task.getStatus());

        // Валидация числовых полей
        if (task.getResponsibleId() != null && task.getResponsibleId() <= 0) {
            throw new ValidationException("Responsible Employee ID must be positive");
        }
        if (task.getCreatorId() != null && task.getCreatorId() <= 0) {
            throw new ValidationException("Creator Employee ID must be positive");
        }
    }

    private void validateForeignKeys(Task task) {
        // Проверка существования ответственного сотрудника
        if (task.getResponsibleId() != null) {
            employeeDao.findById(task.getResponsibleId())
                    .orElseThrow(() -> new ValidationException("Responsible employee not found with ID: " + task.getResponsibleId()));
        }

        // Проверка существования создателя
        if (task.getCreatorId() != null) {
            employeeDao.findById(task.getCreatorId())
                    .orElseThrow(() -> new ValidationException("Creator employee not found with ID: " + task.getCreatorId()));
        }

        // Проверка существования клиента (если указан)
        if (task.getClientId() != null) {
            clientDao.findById(task.getClientId())
                    .orElseThrow(() -> new ValidationException("Client not found with ID: " + task.getClientId()));
        }

        // Проверка существования объекта (если указан)
        if (task.getObjectId() != null) {
            objectDao.findById(task.getObjectId())
                    .orElseThrow(() -> new ValidationException("Object not found with ID: " + task.getObjectId()));
        }

        // Проверка существования дополнительного условия (если указано)
        if (task.getConditionId() != null) {
            conditionDao.findById(task.getConditionId())
                    .orElseThrow(() -> new ValidationException("Additional condition not found with ID: " + task.getConditionId()));
        }

        // Проверка существования сделки (если указана)
        if (task.getDealId() != null) {
            dealDao.findById(task.getDealId())
                    .orElseThrow(() -> new ValidationException("Deal not found with ID: " + task.getDealId()));
        }

        // Проверка существования встречи (если указана)
        if (task.getMeetingId() != null) {
            meetingDao.findById(task.getMeetingId())
                    .orElseThrow(() -> new ValidationException("Meeting not found with ID: " + task.getMeetingId()));
        }
    }

    private void validatePriority(String priority) {
        if (priority == null || priority.trim().isEmpty()) {
            throw new ValidationException("Priority is required");
        }

        String[] validPriorities = {"High", "Medium", "Low"};

        boolean isValid = false;
        for (String validPriority : validPriorities) {
            if (validPriority.equalsIgnoreCase(priority.trim())) {
                isValid = true;
                break;
            }
        }

        if (!isValid) {
            throw new ValidationException("Invalid priority. Allowed values: High, Medium, Low");
        }
    }

    private void validateStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new ValidationException("Status is required");
        }

        String[] validStatuses = {"Pending", "In Progress", "Completed", "Cancelled", "On Hold"};

        boolean isValid = false;
        for (String validStatus : validStatuses) {
            if (validStatus.equalsIgnoreCase(status.trim())) {
                isValid = true;
                break;
            }
        }

        if (!isValid) {
            throw new ValidationException("Invalid status. Allowed values: Pending, In Progress, Completed, Cancelled, On Hold");
        }
    }

    // НОВАЯ ПРОВЕРКА: Уникальность внешних ключей при создании
    private void checkUniqueForeignKeys(Task task) {
        // Проверка уникальности Additional Condition ID
        if (task.getConditionId() != null) {
            dao.findAll().stream()
                    .filter(existingTask ->
                            task.getConditionId().equals(existingTask.getConditionId())
                    )
                    .findFirst()
                    .ifPresent(existingTask -> {
                        throw new ValidationException("Additional condition with ID " + task.getConditionId() +
                                " is already used in task (ID: " + existingTask.getId() + ")");
                    });
        }

        // Проверка уникальности Deal ID
        if (task.getDealId() != null) {
            dao.findAll().stream()
                    .filter(existingTask ->
                            task.getDealId().equals(existingTask.getDealId())
                    )
                    .findFirst()
                    .ifPresent(existingTask -> {
                        throw new ValidationException("Deal with ID " + task.getDealId() +
                                " is already used in task (ID: " + existingTask.getId() + ")");
                    });
        }

        // Проверка уникальности Meeting ID
        if (task.getMeetingId() != null) {
            dao.findAll().stream()
                    .filter(existingTask ->
                            task.getMeetingId().equals(existingTask.getMeetingId())
                    )
                    .findFirst()
                    .ifPresent(existingTask -> {
                        throw new ValidationException("Meeting with ID " + task.getMeetingId() +
                                " is already used in task (ID: " + existingTask.getId() + ")");
                    });
        }
    }

    // НОВАЯ ПРОВЕРКА: Уникальность внешних ключей при обновлении
    private void checkUniqueForeignKeysForUpdate(Task task) {
        // Проверка уникальности Additional Condition ID
        if (task.getConditionId() != null) {
            dao.findAll().stream()
                    .filter(existingTask ->
                            !existingTask.getId().equals(task.getId()) &&
                                    task.getConditionId().equals(existingTask.getConditionId())
                    )
                    .findFirst()
                    .ifPresent(existingTask -> {
                        throw new ValidationException("Additional condition with ID " + task.getConditionId() +
                                " is already used in task (ID: " + existingTask.getId() + ")");
                    });
        }

        // Проверка уникальности Deal ID
        if (task.getDealId() != null) {
            dao.findAll().stream()
                    .filter(existingTask ->
                            !existingTask.getId().equals(task.getId()) &&
                                    task.getDealId().equals(existingTask.getDealId())
                    )
                    .findFirst()
                    .ifPresent(existingTask -> {
                        throw new ValidationException("Deal with ID " + task.getDealId() +
                                " is already used in task (ID: " + existingTask.getId() + ")");
                    });
        }

        // Проверка уникальности Meeting ID
        if (task.getMeetingId() != null) {
            dao.findAll().stream()
                    .filter(existingTask ->
                            !existingTask.getId().equals(task.getId()) &&
                                    task.getMeetingId().equals(existingTask.getMeetingId())
                    )
                    .findFirst()
                    .ifPresent(existingTask -> {
                        throw new ValidationException("Meeting with ID " + task.getMeetingId() +
                                " is already used in task (ID: " + existingTask.getId() + ")");
                    });
        }
    }
}