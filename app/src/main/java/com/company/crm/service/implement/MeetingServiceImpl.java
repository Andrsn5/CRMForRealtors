package com.company.crm.service.implement;

import com.company.crm.dao.interfaces.MeetingDao;
import com.company.crm.dao.interfaces.ClientDao;
import com.company.crm.dao.interfaces.TaskDao;
import com.company.crm.model.Meeting;
import com.company.crm.service.interfaces.MeetingService;
import com.company.crm.util.ValidationException;
import com.company.crm.util.ValidationUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

public class MeetingServiceImpl implements MeetingService {
    private final MeetingDao dao;
    private final ClientDao clientDao;
    private final TaskDao taskDao;

    public MeetingServiceImpl(MeetingDao dao, ClientDao clientDao, TaskDao taskDao) {
        this.dao = dao;
        this.clientDao = clientDao;
        this.taskDao = taskDao;
    }

    @Override
    public List<Meeting> getAll() {
        return dao.findAll();
    }

    @Override
    public Optional<Meeting> getById(int id) {
        if (id <= 0) {
            throw new ValidationException("Meeting ID must be positive");
        }
        return dao.findById(id);
    }

    @Override
    public Meeting create(Meeting meeting) {
        validateMeeting(meeting);
        validateForeignKeys(meeting);
        checkMeetingUniqueness(meeting); // Проверка уникальности
        return dao.save(meeting);
    }

    @Override
    public void update(Meeting meeting) {
        if (meeting.getId() == null || meeting.getId() <= 0) {
            throw new ValidationException("Valid meeting ID is required for update");
        }
        validateMeeting(meeting);
        validateForeignKeys(meeting);
        checkMeetingUniquenessForUpdate(meeting); // Проверка уникальности при обновлении
        dao.update(meeting);
    }

    @Override
    public void delete(int id) {
        if (id <= 0) {
            throw new ValidationException("Meeting ID must be positive");
        }
        dao.delete(id);
    }

    private void validateMeeting(Meeting meeting) {
        ValidationUtils.validateRequired(meeting.getTitle(), "Title");
        ValidationUtils.validateRequired(meeting.getMeetingDate(), "Meeting date");
        ValidationUtils.validateRequired(meeting.getLocation(), "Location");
        ValidationUtils.validateRequired(meeting.getStatus(), "Status");
        ValidationUtils.validateRequired(meeting.getClientId(), "Client");

        ValidationUtils.validateMaxLength(meeting.getTitle(), 255, "Title");
        ValidationUtils.validateMaxLength(meeting.getLocation(), 255, "Location");
        ValidationUtils.validateMaxLength(meeting.getStatus(), 50, "Status");
        ValidationUtils.validateMaxLength(meeting.getNotes(), 1000, "Notes");

        // Валидация даты встречи
        if (meeting.getMeetingDate() != null) {
            LocalDateTime minDateTime = LocalDateTime.now().minusYears(1);
            if (meeting.getMeetingDate().isBefore(minDateTime)) {
                throw new ValidationException("Meeting date cannot be older than 1 year");
            }

            LocalDateTime maxDateTime = LocalDateTime.now().plusYears(1);
            if (meeting.getMeetingDate().isAfter(maxDateTime)) {
                throw new ValidationException("Meeting date cannot be more than 1 year in the future");
            }
        }

        // Валидация статуса
        validateStatus(meeting.getStatus());

        // Валидация Client ID
        if (meeting.getClientId() != null && meeting.getClientId() <= 0) {
            throw new ValidationException("Client ID must be positive");
        }

        // Валидация Task ID - может быть null, но если указан, должен быть положительным
        if (meeting.getTaskId() != null && meeting.getTaskId() <= 0) {
            throw new ValidationException("Task ID must be positive if provided");
        }
    }

    private void validateForeignKeys(Meeting meeting) {
        // Проверка существования клиента
        if (meeting.getClientId() != null) {
            clientDao.findById(meeting.getClientId())
                    .orElseThrow(() -> new ValidationException("Client not found with ID: " + meeting.getClientId()));
        }

        // Проверка существования задачи (если указана)
        if (meeting.getTaskId() != null) {
            taskDao.findById(meeting.getTaskId())
                    .orElseThrow(() -> new ValidationException("Task not found with ID: " + meeting.getTaskId()));
        }
    }

    private void validateStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new ValidationException("Status is required");
        }

        String[] validStatuses = {"scheduled", "completed", "cancelled"};

        boolean isValid = false;
        for (String validStatus : validStatuses) {
            if (validStatus.equalsIgnoreCase(status.trim())) {
                isValid = true;
                break;
            }
        }

        if (!isValid) {
            throw new ValidationException("Invalid status. Allowed values: scheduled, completed, cancelled");
        }
    }

    private void checkMeetingUniqueness(Meeting meeting) {
        if (meeting.getClientId() == null || meeting.getMeetingDate() == null) {
            return;
        }

        List<Meeting> existingMeetings = dao.findAll().stream()
                .filter(existingMeeting ->
                        meeting.getClientId().equals(existingMeeting.getClientId()) &&
                                isSameDateTime(meeting.getMeetingDate(), existingMeeting.getMeetingDate()) &&
                                isActiveStatus(existingMeeting.getStatus())
                )
                .toList();

        if (!existingMeetings.isEmpty()) {
            Meeting conflictingMeeting = existingMeetings.get(0);
            throw new ValidationException("Client with ID " + meeting.getClientId() +
                    " already has an active meeting scheduled at " +
                    formatDateTime(meeting.getMeetingDate()) +
                    " (Meeting ID: " + conflictingMeeting.getId() + ")");
        }
    }

    private void checkMeetingUniquenessForUpdate(Meeting meeting) {
        if (meeting.getClientId() == null || meeting.getMeetingDate() == null) {
            return;
        }

        List<Meeting> existingMeetings = dao.findAll().stream()
                .filter(existingMeeting ->
                        !existingMeeting.getId().equals(meeting.getId()) &&
                                meeting.getClientId().equals(existingMeeting.getClientId()) &&
                                isSameDateTime(meeting.getMeetingDate(), existingMeeting.getMeetingDate()) &&
                                isActiveStatus(existingMeeting.getStatus())
                )
                .toList();

        if (!existingMeetings.isEmpty()) {
            Meeting conflictingMeeting = existingMeetings.get(0);
            throw new ValidationException("Client with ID " + meeting.getClientId() +
                    " already has an active meeting scheduled at " +
                    formatDateTime(meeting.getMeetingDate()) +
                    " (Meeting ID: " + conflictingMeeting.getId() + ")");
        }
    }

    // Вспомогательный метод для сравнения дат/времени (игнорируем секунды и наносекунды)
    private boolean isSameDateTime(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        if (dateTime1 == null || dateTime2 == null) {
            return false;
        }
        // Сравниваем с точностью до минут
        return dateTime1.truncatedTo(java.time.temporal.ChronoUnit.MINUTES)
                .equals(dateTime2.truncatedTo(java.time.temporal.ChronoUnit.MINUTES));
    }

    // Вспомогательный метод для определения активного статуса
    private boolean isActiveStatus(String status) {
        return status != null &&
                ("scheduled".equalsIgnoreCase(status));
    }

    // Вспомогательный метод для форматирования даты/времени
    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.toString().replace('T', ' ') : "null";
    }

    // Новый метод для парсинга даты и времени из строки
    public LocalDateTime parseDateTime(String dateTimeString) {
        if (dateTimeString == null || dateTimeString.trim().isEmpty()) {
            return null;
        }

        try {
            // Пробуем разные форматы даты и времени
            if (dateTimeString.contains("T")) {
                // HTML5 datetime-local format: "2024-11-18T15:30"
                if (dateTimeString.length() == 16) {
                    return LocalDateTime.parse(dateTimeString + ":00");
                }
                return LocalDateTime.parse(dateTimeString);
            } else if (dateTimeString.contains(" ")) {
                // Формат с временем: "2024-11-18 15:30:00" или "2024-11-18 15:30"
                String normalized = dateTimeString.trim();
                if (normalized.length() == 16) {
                    // "2024-11-18 15:30" -> добавляем секунды
                    normalized += ":00";
                }
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                return LocalDateTime.parse(normalized, formatter);
            } else {
                // Только дата: "2024-11-18" -> устанавливаем время на 00:00
                return LocalDateTime.parse(dateTimeString + "T00:00:00");
            }
        } catch (DateTimeParseException e) {
            throw new ValidationException("Invalid date/time format: " + dateTimeString +
                    ". Expected formats: yyyy-MM-dd HH:mm:ss, yyyy-MM-ddTHH:mm, or yyyy-MM-dd");
        }
    }

    // Дополнительные методы для бизнес-логики
    public List<Meeting> getMeetingsByClient(int clientId) {
        if (clientId <= 0) {
            throw new ValidationException("Valid client ID is required");
        }
        return dao.findAll().stream()
                .filter(meeting -> clientId == meeting.getClientId())
                .toList();
    }

    public List<Meeting> getMeetingsByStatus(String status) {
        validateStatus(status);
        return dao.findAll().stream()
                .filter(meeting -> status.equalsIgnoreCase(meeting.getStatus()))
                .toList();
    }

    public List<Meeting> getUpcomingMeetings() {
        LocalDateTime now = LocalDateTime.now();
        return dao.findAll().stream()
                .filter(meeting -> meeting.getMeetingDate() != null)
                .filter(meeting -> meeting.getMeetingDate().isAfter(now))
                .filter(meeting -> isActiveStatus(meeting.getStatus()))
                .toList();
    }

    public List<Meeting> getMeetingsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            throw new ValidationException("Start date and end date are required");
        }
        if (startDate.isAfter(endDate)) {
            throw new ValidationException("Start date cannot be after end date");
        }

        return dao.findAll().stream()
                .filter(meeting -> meeting.getMeetingDate() != null)
                .filter(meeting -> !meeting.getMeetingDate().isBefore(startDate) &&
                        !meeting.getMeetingDate().isAfter(endDate))
                .toList();
    }

    // Метод для проверки доступности времени для клиента
    public boolean isTimeSlotAvailable(int clientId, LocalDateTime dateTime) {
        if (clientId <= 0 || dateTime == null) {
            return false;
        }

        return dao.findAll().stream()
                .filter(meeting -> clientId == meeting.getClientId())
                .filter(meeting -> isSameDateTime(dateTime, meeting.getMeetingDate()))
                .filter(meeting -> isActiveStatus(meeting.getStatus()))
                .findFirst()
                .isEmpty();
    }

    @Override
    public Optional<Meeting> safeGetById(Integer id) {
        if (id == null || id <= 0) {
            return Optional.empty();
        }
        return dao.findById(id);
    }
}