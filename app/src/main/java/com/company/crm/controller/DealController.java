package com.company.crm.controller;

import com.company.crm.model.Deal;
import com.company.crm.service.interfaces.DealService;
import com.company.crm.util.ConsoleHelper;
import com.company.crm.util.ValidationException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class DealController {
    private final DealService service;

    public DealController(DealService service) {
        this.service = service;
    }

    public void seedData() {
        if (service.getAll().isEmpty()) {
            for (int i = 1; i <= 10; i++) {
                try {
                    Deal deal = new Deal();
                    deal.setDealNumber("DEAL-" + (1000 + i));
                    deal.setTaskId(i);
                    deal.setDealAmount(new BigDecimal(200000 + i * 50000));
                    deal.setDealDate(LocalDate.now().plusDays(i));
                    deal.setCommission(new BigDecimal(5000 + i * 1000));
                    deal.setStatus(i % 3 == 0 ? "sold" : i % 3 == 1 ? "active" : "cancelled");
                    service.create(deal);
                } catch (ValidationException e) {
                    System.out.println("❌ Seed data validation error: " + e.getMessage());
                }
            }
        }
    }

    public void menu() {
        while (true) {
            System.out.println("\n=== DEAL MANAGEMENT ===");
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
        List<Deal> all = service.getAll();
        if (all.isEmpty()) {
            System.out.println("No deals found");
        } else {
            System.out.println("\n=== DEAL LIST ===");
            all.forEach(System.out::println);
        }
    }

    private void create() {
        while (true) {
            try {
                System.out.println("\n=== CREATE NEW DEAL ===");
                String dealNumber = ConsoleHelper.ask("Deal Number");
                String taskIdStr = ConsoleHelper.ask("Task ID");
                String amountStr = ConsoleHelper.ask("Deal Amount");
                String dateStr = ConsoleHelper.ask("Deal Date (YYYY-MM-DD) or leave empty for today");
                String commissionStr = ConsoleHelper.ask("Commission");
                String status = ConsoleHelper.ask("Status (active, sold, completed, withdrawn in progress, cancelled)");

                Deal deal = new Deal();
                deal.setDealNumber(dealNumber);
                deal.setStatus(status);

                // Валидация числовых полей
                try {
                    if (!taskIdStr.trim().isEmpty()) {
                        deal.setTaskId(Integer.parseInt(taskIdStr));
                    }
                    deal.setDealAmount(new BigDecimal(amountStr));
                    deal.setCommission(new BigDecimal(commissionStr));
                } catch (NumberFormatException e) {
                    throw new ValidationException("Invalid number format. Please enter valid numbers.");
                }

                // Обработка даты
                if (dateStr.trim().isEmpty()) {
                    deal.setDealDate(LocalDate.now());
                } else {
                    try {
                        deal.setDealDate(LocalDate.parse(dateStr));
                    } catch (Exception e) {
                        throw new ValidationException("Invalid date format. Please use YYYY-MM-DD.");
                    }
                }

                service.create(deal);
                System.out.println("✅ Deal added: " + deal);
                break; // Выход из цикла при успешном создании

            } catch (ValidationException e) {
                System.out.println("❌ Validation error: " + e.getMessage());
                System.out.println("Please correct the data and try again.\n");

                String retry = ConsoleHelper.ask("Try again? (y/n)");
                if (!retry.equalsIgnoreCase("y")) {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Number format error: " + e.getMessage());
                System.out.println("Please enter valid numbers for amount and commission.\n");

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
                System.out.println("\n=== EDIT DEAL ===");
                int id = ConsoleHelper.askInt("Enter deal ID");

                Optional<Deal> dealOpt = service.getById(id);
                if (dealOpt.isEmpty()) {
                    System.out.println("❌ Deal not found with ID: " + id);

                    String retry = ConsoleHelper.ask("Try different ID? (y/n)");
                    if (!retry.equalsIgnoreCase("y")) {
                        break;
                    }
                    continue;
                }

                Deal deal = dealOpt.get();
                System.out.println("Editing: " + deal);

                // Запрашиваем новые данные с текущими значениями по умолчанию
                deal.setDealNumber(ConsoleHelper.ask("Deal Number (" + deal.getDealNumber() + ")"));

                // Обработка Task ID
                String taskIdStr = ConsoleHelper.ask("Task ID (" + deal.getTaskId() + ")");
                if (!taskIdStr.trim().isEmpty()) {
                    try {
                        deal.setTaskId(Integer.parseInt(taskIdStr));
                    } catch (NumberFormatException e) {
                        throw new ValidationException("Invalid Task ID format. Please enter a valid number.");
                    }
                }

                // Обработка суммы сделки
                String amountStr = ConsoleHelper.ask("Deal Amount (" + deal.getDealAmount() + ")");
                if (!amountStr.trim().isEmpty()) {
                    try {
                        deal.setDealAmount(new BigDecimal(amountStr));
                    } catch (NumberFormatException e) {
                        throw new ValidationException("Invalid deal amount format. Please enter a valid number.");
                    }
                }

                // Обработка даты
                String dateStr = ConsoleHelper.ask("Deal Date (" + deal.getDealDate() + ")");
                if (!dateStr.trim().isEmpty()) {
                    try {
                        deal.setDealDate(LocalDate.parse(dateStr));
                    } catch (Exception e) {
                        throw new ValidationException("Invalid date format. Please use YYYY-MM-DD.");
                    }
                }

                // Обработка комиссии
                String commissionStr = ConsoleHelper.ask("Commission (" + deal.getCommission() + ")");
                if (!commissionStr.trim().isEmpty()) {
                    try {
                        deal.setCommission(new BigDecimal(commissionStr));
                    } catch (NumberFormatException e) {
                        throw new ValidationException("Invalid commission format. Please enter a valid number.");
                    }
                }

                deal.setStatus(ConsoleHelper.ask("Status (" + deal.getStatus() + ")"));

                service.update(deal);
                System.out.println("✅ Deal updated: " + deal);
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
                System.out.println("\n=== DELETE DEAL ===");
                int id = ConsoleHelper.askInt("Enter deal ID to delete");

                // Проверяем существование сделки перед удалением
                Optional<Deal> dealOpt = service.getById(id);
                if (dealOpt.isEmpty()) {
                    System.out.println("❌ Deal not found with ID: " + id);

                    String retry = ConsoleHelper.ask("Try different ID? (y/n)");
                    if (!retry.equalsIgnoreCase("y")) {
                        break;
                    }
                    continue;
                }

                Deal deal = dealOpt.get();
                System.out.println("About to delete: " + deal);

                String confirm = ConsoleHelper.ask("Are you sure you want to delete this deal? (y/n)");
                if (confirm.equalsIgnoreCase("y")) {
                    service.delete(id);
                    System.out.println("✅ Deal deleted successfully");
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