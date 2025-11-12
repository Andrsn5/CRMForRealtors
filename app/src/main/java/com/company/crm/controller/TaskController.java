package com.company.crm.controller;

import com.company.crm.model.Task;
import com.company.crm.service.interfaces.TaskService;
import com.company.crm.util.ConsoleHelper;
import com.company.crm.util.ValidationException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

public class TaskController {
    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    public void seedData() {
        if (service.getAll().isEmpty()) {
            for (int i = 1; i <= 10; i++) {
                try {
                    Task task = new Task();
                    task.setTitle("Task " + i);
                    task.setDescription("Description for task " + i);
                    task.setDueDate(LocalDateTime.now().plusDays(i));
                    task.setPriority(getPriority(i));
                    task.setStatus(getStatus(i));
                    task.setResponsibleId((i % 5) + 1);
                    task.setCreatorId(1);
                    task.setClientId((i % 3) + 1);
                    task.setObjectId((i % 4) + 1);
                    service.create(task);
                } catch (ValidationException e) {
                    System.out.println("❌ Seed data validation error: " + e.getMessage());
                }
            }
        }
    }

    private String getPriority(int i) {
        String[] priorities = {"High", "Medium", "Low"};
        return priorities[i % priorities.length];
    }

    private String getStatus(int i) {
        String[] statuses = {"Pending", "In Progress", "Completed", "Cancelled", "On Hold"};
        return statuses[i % statuses.length];
    }

    public void menu() {
        while (true) {
            System.out.println("\n=== TASK MANAGEMENT ===");
            System.out.println("1. List 2. Create 3. Edit 4. Delete 0. Back");
            int choice = ConsoleHelper.askInt("Choose option");
            switch (choice) {
                case 1 -> list();
                case 2 -> create();
                case 3 -> edit();
                case 4 -> delete();
                case 0 -> { return; }
                default -> System.out.println("❌ Invalid option");
            }
        }
    }

    private void list() {
        List<Task> all = service.getAll();
        if (all.isEmpty()) {
            System.out.println("No tasks found");
        } else {
            System.out.println("\n=== TASKS LIST ===");
            all.forEach(System.out::println);
        }
    }

    private void create() {
        while (true) {
            try {
                System.out.println("\n=== CREATE NEW TASK ===");
                String title = ConsoleHelper.ask("Task Title");
                String description = ConsoleHelper.ask("Description");
                String dueDateStr = ConsoleHelper.ask("Due Date (YYYY-MM-DD)");
                String dueTimeStr = ConsoleHelper.ask("Due Time (HH:MM) or leave empty for 23:59");
                String priority = ConsoleHelper.ask("Priority (High/Medium/Low)");
                String status = ConsoleHelper.ask("Status (In Progress/Completed/Cancelled/On Hold)");
                String responsibleIdStr = ConsoleHelper.ask("Responsible Employee ID");
                String creatorIdStr = ConsoleHelper.ask("Creator Employee ID");
                String clientIdStr = ConsoleHelper.ask("Client ID (optional)");
                String objectIdStr = ConsoleHelper.ask("Object ID (optional)");
                String conditionIdStr = ConsoleHelper.ask("Additional Condition ID (optional)");
                String dealIdStr = ConsoleHelper.ask("Deal ID (optional)");
                String meetingIdStr = ConsoleHelper.ask("Meeting ID (optional)");

                Task task = new Task();
                task.setTitle(title);
                task.setDescription(description);
                task.setPriority(priority);
                task.setStatus(status);

                // Валидация даты и времени
                try {
                    LocalDate date = LocalDate.parse(dueDateStr);
                    LocalTime time = dueTimeStr.trim().isEmpty() ?
                            LocalTime.of(23, 59) : LocalTime.parse(dueTimeStr + ":00");
                    LocalDateTime dueDateTime = LocalDateTime.of(date, time);
                    task.setDueDate(dueDateTime);
                } catch (DateTimeParseException e) {
                    throw new ValidationException("Invalid date/time format. Please use YYYY-MM-DD for date and HH:MM for time.");
                }

                // Валидация обязательных числовых полей
                try {
                    task.setResponsibleId(Integer.parseInt(responsibleIdStr));
                } catch (NumberFormatException e) {
                    throw new ValidationException("Invalid Responsible Employee ID format. Please enter a valid number.");
                }

                try {
                    if (creatorIdStr != null && !creatorIdStr.trim().isEmpty()) {
                        task.setCreatorId(Integer.parseInt(creatorIdStr));
                    } else {
                        throw new ValidationException("Creator Employee ID is required");
                    }
                } catch (NumberFormatException e) {
                    throw new ValidationException("Invalid Creator Employee ID format. Please enter a valid number.");
                }

                // Валидация опциональных числовых полей
                try {
                    if (clientIdStr != null && !clientIdStr.trim().isEmpty()) {
                        task.setClientId(Integer.parseInt(clientIdStr));
                    }
                } catch (NumberFormatException e) {
                    throw new ValidationException("Invalid Client ID format. Please enter a valid number.");
                }

                try {
                    if (objectIdStr != null && !objectIdStr.trim().isEmpty()) {
                        task.setObjectId(Integer.parseInt(objectIdStr));
                    }
                } catch (NumberFormatException e) {
                    throw new ValidationException("Invalid Object ID format. Please enter a valid number.");
                }

                try {
                    if (conditionIdStr != null && !conditionIdStr.trim().isEmpty()) {
                        task.setConditionId(Integer.parseInt(conditionIdStr));
                    }
                } catch (NumberFormatException e) {
                    throw new ValidationException("Invalid Additional Condition ID format. Please enter a valid number.");
                }

                try {
                    if (dealIdStr != null && !dealIdStr.trim().isEmpty()) {
                        task.setDealId(Integer.parseInt(dealIdStr));
                    }
                } catch (NumberFormatException e) {
                    throw new ValidationException("Invalid Deal ID format. Please enter a valid number.");
                }

                try {
                    if (meetingIdStr != null && !meetingIdStr.trim().isEmpty()) {
                        task.setMeetingId(Integer.parseInt(meetingIdStr));
                    }
                } catch (NumberFormatException e) {
                    throw new ValidationException("Invalid Meeting ID format. Please enter a valid number.");
                }

                service.create(task);
                System.out.println("✅ Task created: " + task);
                break; // Выход из цикла при успешном создании

            } catch (ValidationException e) {
                System.out.println("❌ Validation error: " + e.getMessage());
                System.out.println("Please correct the data and try again.\n");

                String retry = ConsoleHelper.ask("Try again? (y/n)");
                if (!retry.equalsIgnoreCase("y")) {
                    break;
                }
            } catch (Exception e) {
                System.out.println("❌ Error: " + e.getMessage());
                System.out.println("Please check your input and try again.\n");

                String retry = ConsoleHelper.ask("Try again? (y/n)");
                if (!retry.equalsIgnoreCase("y")) {
                    break;
                }
            }
        }
        ConsoleHelper.pause();
    }

    private void edit() {
        while (true) {
            try {
                System.out.println("\n=== EDIT TASK ===");
                int id = ConsoleHelper.askInt("Enter task ID");

                Optional<Task> taskOpt = service.getById(id);
                if (taskOpt.isEmpty()) {
                    System.out.println("❌ Task not found with ID: " + id);

                    String retry = ConsoleHelper.ask("Try different ID? (y/n)");
                    if (!retry.equalsIgnoreCase("y")) {
                        break;
                    }
                    continue;
                }

                Task task = taskOpt.get();
                System.out.println("Editing: " + task);

                // Запрашиваем новые данные с текущими значениями по умолчанию
                task.setTitle(ConsoleHelper.ask("Title (" + task.getTitle() + ")"));
                task.setDescription(ConsoleHelper.ask("Description (" + task.getDescription() + ")"));
                task.setPriority(ConsoleHelper.ask("Priority (" + task.getPriority() + ")"));
                task.setStatus(ConsoleHelper.ask("Status (" + task.getStatus() + ")"));

                // Обработка даты и времени
                String dueDateStr = ConsoleHelper.ask("Due Date (" + task.getDueDate().toLocalDate() + ")");
                String dueTimeStr = ConsoleHelper.ask("Due Time (" + task.getDueDate().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")) + ")");

                if (!dueDateStr.trim().isEmpty() || !dueTimeStr.trim().isEmpty()) {
                    try {
                        LocalDate date = dueDateStr.trim().isEmpty() ?
                                task.getDueDate().toLocalDate() : LocalDate.parse(dueDateStr);
                        LocalTime time = dueTimeStr.trim().isEmpty() ?
                                task.getDueDate().toLocalTime() : LocalTime.parse(dueTimeStr + ":00");
                        LocalDateTime dueDateTime = LocalDateTime.of(date, time);
                        task.setDueDate(dueDateTime);
                    } catch (DateTimeParseException e) {
                        throw new ValidationException("Invalid date/time format. Please use YYYY-MM-DD for date and HH:MM for time.");
                    }
                }

                // Обработка числовых полей
                updateNumericField(task, "Responsible Employee ID", task.getResponsibleId(),
                        value -> task.setResponsibleId(value));
                updateNumericField(task, "Creator Employee ID", task.getCreatorId(),
                        value -> task.setCreatorId(value));
                updateOptionalNumericField(task, "Client ID", task.getClientId(),
                        value -> task.setClientId(value));
                updateOptionalNumericField(task, "Object ID", task.getObjectId(),
                        value -> task.setObjectId(value));
                updateOptionalNumericField(task, "Additional Condition ID", task.getConditionId(),
                        value -> task.setConditionId(value));
                updateOptionalNumericField(task, "Deal ID", task.getDealId(),
                        value -> task.setDealId(value));
                updateOptionalNumericField(task, "Meeting ID", task.getMeetingId(),
                        value -> task.setMeetingId(value));

                service.update(task);
                System.out.println("✅ Task updated: " + task);
                break; // Выход из цикла при успешном обновлении

            } catch (ValidationException e) {
                System.out.println("❌ Validation error: " + e.getMessage());
                System.out.println("Please correct the data and try again.\n");

                String retry = ConsoleHelper.ask("Try again? (y/n)");
                if (!retry.equalsIgnoreCase("y")) {
                    break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("❌ Error: " + e.getMessage());

                String retry = ConsoleHelper.ask("Try again? (y/n)");
                if (!retry.equalsIgnoreCase("y")) {
                    break;
                }
            }
        }
        ConsoleHelper.pause();
    }

    private void updateNumericField(Task task, String fieldName, Integer currentValue, java.util.function.Consumer<Integer> setter) {
        String valueStr = ConsoleHelper.ask(fieldName + " (" + currentValue + ")");
        if (!valueStr.trim().isEmpty()) {
            try {
                setter.accept(Integer.parseInt(valueStr));
            } catch (NumberFormatException e) {
                throw new ValidationException("Invalid " + fieldName + " format. Please enter a valid number.");
            }
        }
    }

    private void updateOptionalNumericField(Task task, String fieldName, Integer currentValue, java.util.function.Consumer<Integer> setter) {
        String valueStr = ConsoleHelper.ask(fieldName + " (" + (currentValue != null ? currentValue : "null") + ")");
        if (!valueStr.trim().isEmpty()) {
            try {
                if ("null".equalsIgnoreCase(valueStr.trim())) {
                    setter.accept(null);
                } else {
                    setter.accept(Integer.parseInt(valueStr));
                }
            } catch (NumberFormatException e) {
                throw new ValidationException("Invalid " + fieldName + " format. Please enter a valid number or 'null'.");
            }
        }
    }

    private void delete() {
        while (true) {
            try {
                System.out.println("\n=== DELETE TASK ===");
                int id = ConsoleHelper.askInt("Enter task ID to delete");

                // Проверяем существование задачи перед удалением
                Optional<Task> taskOpt = service.getById(id);
                if (taskOpt.isEmpty()) {
                    System.out.println("❌ Task not found with ID: " + id);

                    String retry = ConsoleHelper.ask("Try different ID? (y/n)");
                    if (!retry.equalsIgnoreCase("y")) {
                        break;
                    }
                    continue;
                }

                Task task = taskOpt.get();
                System.out.println("About to delete: " + task);

                String confirm = ConsoleHelper.ask("Are you sure you want to delete this task? (y/n)");
                if (confirm.equalsIgnoreCase("y")) {
                    service.delete(id);
                    System.out.println("✅ Task deleted successfully");
                } else {
                    System.out.println("❌ Deletion cancelled");
                }
                break; // Выход из цикла

            } catch (ValidationException e) {
                System.out.println("❌ Validation error: " + e.getMessage());
                System.out.println("Please correct the data and try again.\n");

                String retry = ConsoleHelper.ask("Try again? (y/n)");
                if (!retry.equalsIgnoreCase("y")) {
                    break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("❌ Error: " + e.getMessage());

                String retry = ConsoleHelper.ask("Try again? (y/n)");
                if (!retry.equalsIgnoreCase("y")) {
                    break;
                }
            }
        }
        ConsoleHelper.pause();
    }

    // Дополнительный метод для быстрого создания тестовых данных
    public void createSampleData() {
        try {
            String[] titles = {"Client Meeting Preparation", "Property Documentation", "Contract Review", "Market Analysis", "Client Follow-up"};
            String[] priorities = {"High", "Medium", "Low", "High", "Medium"};
            String[] statuses = {"Pending", "In Progress", "Completed", "On Hold", "Pending"};

            for (int i = 0; i < 5; i++) {
                Task task = new Task();
                task.setTitle(titles[i]);
                task.setDescription("Sample task description for " + titles[i]);
                task.setDueDate(LocalDateTime.now().plusDays(i + 1).plusHours(9)); // 9:00 AM
                task.setPriority(priorities[i]);
                task.setStatus(statuses[i]);
                task.setResponsibleId(1);
                task.setCreatorId(1);
                task.setClientId(i + 1);
                task.setObjectId(i + 1);

                service.create(task);
            }
            System.out.println("✅ Sample tasks created successfully");
        } catch (ValidationException e) {
            System.out.println("❌ Error creating sample data: " + e.getMessage());
        }
    }
}