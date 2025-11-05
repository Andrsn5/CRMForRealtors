package com.company.crm.controller;

import com.company.crm.model.Task;
import com.company.crm.service.interfaces.TaskService;
import com.company.crm.util.ConsoleHelper;

import java.time.LocalDateTime;
import java.util.List;

public class TaskController {
    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
        seedData();
    }

    private void seedData() {
        if (service.getAll().isEmpty()) {
            for (int i = 1; i <= 10; i++) {
                Task task = new Task();
                task.setTitle("Task " + i);
                task.setDescription("Description for task " + i);
                task.setDueDate(LocalDateTime.now().plusDays(i));
                task.setPriority(i % 3 == 0 ? "High" : "Medium");
                task.setStatus("Pending");
                task.setResponsibleId((i % 5) + 1);
                task.setCreatorId(1);
                task.setClientId((i % 3) + 1);
                task.setObjectId((i % 4) + 1);
                service.create(task);
            }
        }
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
            all.forEach(System.out::println);
        }
    }

    private void create() {
        String title = ConsoleHelper.ask("Task Title");
        String description = ConsoleHelper.ask("Description");
        String dueDateStr = ConsoleHelper.ask("Due Date (YYYY-MM-DD)");
        String priority = ConsoleHelper.ask("Priority (High/Medium/Low)");

        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setPriority(priority);
        task.setStatus("Pending");

        try {
            LocalDateTime dueDate = LocalDateTime.parse(dueDateStr + "T23:59:59");
            task.setDueDate(dueDate);
            task.setResponsibleId(Integer.parseInt(ConsoleHelper.ask("Responsible Employee ID")));
            task.setClientId(Integer.parseInt(ConsoleHelper.ask("Client ID")));
        } catch (Exception e) {
            System.out.println("❌ Invalid date or number format");
            return;
        }

        service.create(task);
        System.out.println("✅ Task created: " + task);
        ConsoleHelper.pause();
    }

    private void edit() {
        int id = ConsoleHelper.askInt("Enter task ID");
        service.getById(id).ifPresentOrElse(task -> {
            task.setStatus(ConsoleHelper.ask("Status (" + task.getStatus() + ")"));
            task.setPriority(ConsoleHelper.ask("Priority (" + task.getPriority() + ")"));
            service.update(task);
            System.out.println("✅ Task updated: " + task);
        }, () -> System.out.println("❌ Task not found."));
        ConsoleHelper.pause();
    }

    private void delete() {
        int id = ConsoleHelper.askInt("Enter task ID to delete");
        service.delete(id);
        System.out.println("✅ Task deleted");
        ConsoleHelper.pause();
    }
}