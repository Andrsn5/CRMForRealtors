package com.company.crm.controller;

import com.company.crm.model.AdditionalCondition;
import com.company.crm.service.interfaces.AdditionalConditionService;
import com.company.crm.util.ConsoleHelper;
import com.company.crm.util.ValidationException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class AdditionalConditionController {
    private final AdditionalConditionService service;

    public AdditionalConditionController(AdditionalConditionService service) {
        this.service = service;
    }

    public void seedData() {
        if (service.getAll().isEmpty()) {
            for (int i = 1; i <= 10; i++) {
                try {
                    AdditionalCondition condition = new AdditionalCondition();
                    condition.setConditionType("marketing");
                    condition.setDescription("Description for additional condition " + i);
                    condition.setDeadline(LocalDate.now().plusDays(i * 7));
                    condition.setRequired(i % 2 == 0);
                    condition.setStatus("canceled");
                    condition.setPriority("high");
                    condition.setNotes("Notes for additional condition");
                    service.create(condition);
                } catch (ValidationException e) {
                    System.out.println("❌ Seed data validation error: " + e.getMessage());
                }
            }
        }
    }



    public void menu() {
        while (true) {
            System.out.println("\n=== ADDITIONAL CONDITIONS MANAGEMENT ===");
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
        List<AdditionalCondition> all = service.getAll();
        if (all.isEmpty()) {
            System.out.println("No additional conditions found");
        } else {
            System.out.println("\n=== ADDITIONAL CONDITIONS LIST ===");
            all.forEach(System.out::println);
        }
    }

    private void create() {
        while (true) {
            try {
                System.out.println("\n=== CREATE NEW ADDITIONAL CONDITION ===");
                String conditionType = ConsoleHelper.ask("Condition Type (marketing, administrative, customer service, logistics)");
                String description = ConsoleHelper.ask("Description");
                String deadlineStr = ConsoleHelper.ask("Deadline (YYYY-MM-DD)");
                String requiredStr = ConsoleHelper.ask("Required (true/false)");
                String status = ConsoleHelper.ask("Status (completed, active, canceled)");
                String priority = ConsoleHelper.ask("Priority (high/medium/low)");
                String notes = ConsoleHelper.ask("Notes");

                AdditionalCondition condition = new AdditionalCondition();
                condition.setConditionType(conditionType);
                condition.setDescription(description);
                condition.setStatus(status);
                condition.setPriority(priority);
                condition.setNotes(notes);

                // Валидация даты
                try {
                    condition.setDeadline(LocalDate.parse(deadlineStr));
                } catch (Exception e) {
                    throw new ValidationException("Invalid date format. Please use YYYY-MM-DD.");
                }

                // Валидация boolean
                try {
                    condition.setRequired(Boolean.parseBoolean(requiredStr));
                } catch (Exception e) {
                    throw new ValidationException("Invalid boolean value. Please use 'true' or 'false'.");
                }

                service.create(condition);
                System.out.println("✅ Additional condition added: " + condition);
                break;

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
                System.out.println("\n=== EDIT ADDITIONAL CONDITION ===");
                int id = ConsoleHelper.askInt("Enter additional condition ID");

                Optional<AdditionalCondition> conditionOpt = service.getById(id);
                if (conditionOpt.isEmpty()) {
                    System.out.println("❌ Additional condition not found with ID: " + id);

                    String retry = ConsoleHelper.ask("Try different ID? (y/n)");
                    if (!retry.equalsIgnoreCase("y")) {
                        break;
                    }
                    continue;
                }

                AdditionalCondition condition = conditionOpt.get();
                System.out.println("Editing: " + condition);

                // Запрашиваем новые данные с текущими значениями по умолчанию
                condition.setConditionType(ConsoleHelper.ask("Condition Type (" + condition.getConditionType() + ")"));
                condition.setDescription(ConsoleHelper.ask("Description (" + condition.getDescription() + ")"));

                // Обработка даты
                String deadlineStr = ConsoleHelper.ask("Deadline (" + condition.getDeadline() + ")");
                if (!deadlineStr.trim().isEmpty()) {
                    try {
                        condition.setDeadline(LocalDate.parse(deadlineStr));
                    } catch (Exception e) {
                        throw new ValidationException("Invalid date format. Please use YYYY-MM-DD.");
                    }
                }

                // Обработка required
                String requiredStr = ConsoleHelper.ask("Required (" + condition.isRequired() + ")");
                if (!requiredStr.trim().isEmpty()) {
                    try {
                        condition.setRequired(Boolean.parseBoolean(requiredStr));
                    } catch (Exception e) {
                        throw new ValidationException("Invalid boolean value. Please use 'true' or 'false'.");
                    }
                }

                condition.setStatus(ConsoleHelper.ask("Status (" + condition.getStatus() + ")"));
                condition.setPriority(ConsoleHelper.ask("Priority (" + condition.getPriority() + ")"));
                condition.setNotes(ConsoleHelper.ask("Notes (" + condition.getNotes() + ")"));

                service.update(condition);
                System.out.println("✅ Additional condition updated: " + condition);
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

    private void delete() {
        while (true) {
            try {
                System.out.println("\n=== DELETE ADDITIONAL CONDITION ===");
                int id = ConsoleHelper.askInt("Enter additional condition ID to delete");

                // Проверяем существование условия перед удалением
                Optional<AdditionalCondition> conditionOpt = service.getById(id);
                if (conditionOpt.isEmpty()) {
                    System.out.println("❌ Additional condition not found with ID: " + id);

                    String retry = ConsoleHelper.ask("Try different ID? (y/n)");
                    if (!retry.equalsIgnoreCase("y")) {
                        break;
                    }
                    continue;
                }

                AdditionalCondition condition = conditionOpt.get();
                System.out.println("About to delete: " + condition);

                String confirm = ConsoleHelper.ask("Are you sure you want to delete this additional condition? (y/n)");
                if (confirm.equalsIgnoreCase("y")) {
                    service.delete(id);
                    System.out.println("✅ Additional condition deleted successfully");
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

}