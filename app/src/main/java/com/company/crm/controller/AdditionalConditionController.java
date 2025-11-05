package com.company.crm.controller;

import com.company.crm.model.AdditionalCondition;
import com.company.crm.service.interfaces.AdditionalConditionService;
import com.company.crm.util.ConsoleHelper;

import java.time.LocalDate;
import java.util.List;

public class AdditionalConditionController {
    private final AdditionalConditionService service;

    public AdditionalConditionController(AdditionalConditionService service) {
        this.service = service;
        seedData();
    }

    private void seedData() {
        if (service.getAll().isEmpty()) {
            for (int i = 1; i <= 5; i++) {
                AdditionalCondition condition = new AdditionalCondition();
                condition.setConditionType("Condition Type " + i);
                condition.setDescription("Description for condition " + i);
                condition.setDeadline(LocalDate.now().plusDays(i * 7));
                condition.setRequired(i % 2 == 0);
                condition.setStatus("Active");
                condition.setPriority(i % 3 == 0 ? "High" : "Medium");
                condition.setNotes("Notes for condition " + i);
                service.create(condition);
            }
        }
    }

    public void menu() {
        while (true) {
            System.out.println("\n=== ADDITIONAL CONDITIONS ===");
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
            System.out.println("No conditions found");
        } else {
            all.forEach(System.out::println);
        }
    }

    private void create() {
        String conditionType = ConsoleHelper.ask("Condition Type");
        String description = ConsoleHelper.ask("Description");
        String deadlineStr = ConsoleHelper.ask("Deadline (YYYY-MM-DD)");
        String requiredStr = ConsoleHelper.ask("Required (true/false)");

        AdditionalCondition condition = new AdditionalCondition();
        condition.setConditionType(conditionType);
        condition.setDescription(description);

        try {
            condition.setDeadline(LocalDate.parse(deadlineStr));
            condition.setRequired(Boolean.parseBoolean(requiredStr));
        } catch (Exception e) {
            System.out.println("❌ Invalid date or boolean format");
            return;
        }

        condition.setPriority(ConsoleHelper.ask("Priority (High/Medium/Low)"));
        condition.setNotes(ConsoleHelper.ask("Notes"));
        service.create(condition);
        System.out.println("✅ Condition added: " + condition);
        ConsoleHelper.pause();
    }

    private void edit() {
        int id = ConsoleHelper.askInt("Enter condition ID");
        service.getById(id).ifPresentOrElse(condition -> {
            condition.setStatus(ConsoleHelper.ask("Status (" + condition.getStatus() + ")"));
            condition.setPriority(ConsoleHelper.ask("Priority (" + condition.getPriority() + ")"));
            service.update(condition);
            System.out.println("✅ Condition updated: " + condition);
        }, () -> System.out.println("❌ Condition not found."));
        ConsoleHelper.pause();
    }

    private void delete() {
        int id = ConsoleHelper.askInt("Enter condition ID to delete");
        service.delete(id);
        System.out.println("✅ Condition deleted");
        ConsoleHelper.pause();
    }
}