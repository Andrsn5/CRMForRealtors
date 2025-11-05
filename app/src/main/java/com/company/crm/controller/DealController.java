package com.company.crm.controller;

import com.company.crm.model.Deal;
import com.company.crm.service.interfaces.DealService;
import com.company.crm.util.ConsoleHelper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class DealController {
    private final DealService service;

    public DealController(DealService service) {
        this.service = service;
        seedData();
    }

    private void seedData() {
        if (service.getAll().isEmpty()) {
            for (int i = 1; i <= 5; i++) {
                Deal deal = new Deal();
                deal.setDealNumber("DEAL-" + i);
                deal.setTaskId(i);
                deal.setDealAmount(new BigDecimal(200000 + i * 50000));
                deal.setDealDate(LocalDate.now().plusDays(i));
                deal.setCommission(new BigDecimal(5000 + i * 1000));
                deal.setStatus("Pending");
                service.create(deal);
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
            all.forEach(System.out::println);
        }
    }

    private void create() {
        String dealNumber = ConsoleHelper.ask("Deal Number");
        String taskIdStr = ConsoleHelper.ask("Task ID");
        String amountStr = ConsoleHelper.ask("Deal Amount");
        String commissionStr = ConsoleHelper.ask("Commission");

        Deal deal = new Deal();
        deal.setDealNumber(dealNumber);

        try {
            deal.setTaskId(Integer.parseInt(taskIdStr));
            deal.setDealAmount(new BigDecimal(amountStr));
            deal.setCommission(new BigDecimal(commissionStr));
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid number format");
            return;
        }

        deal.setDealDate(LocalDate.now());
        deal.setStatus("Pending");
        service.create(deal);
        System.out.println("✅ Deal created: " + deal);
        ConsoleHelper.pause();
    }

    private void edit() {
        int id = ConsoleHelper.askInt("Enter deal ID");
        service.getById(id).ifPresentOrElse(deal -> {
            deal.setDealNumber(ConsoleHelper.ask("Deal Number (" + deal.getDealNumber() + ")"));
            deal.setStatus(ConsoleHelper.ask("Status (" + deal.getStatus() + ")"));
            service.update(deal);
            System.out.println("✅ Deal updated: " + deal);
        }, () -> System.out.println("❌ Deal not found."));
        ConsoleHelper.pause();
    }

    private void delete() {
        int id = ConsoleHelper.askInt("Enter deal ID to delete");
        service.delete(id);
        System.out.println("✅ Deal deleted");
        ConsoleHelper.pause();
    }
}