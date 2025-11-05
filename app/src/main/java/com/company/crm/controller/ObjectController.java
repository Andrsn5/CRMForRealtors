package com.company.crm.controller;

import com.company.crm.model.Object;
import com.company.crm.service.interfaces.ObjectService;
import com.company.crm.util.ConsoleHelper;

import java.math.BigDecimal;
import java.util.List;

public class ObjectController {
    private final ObjectService service;

    public ObjectController(ObjectService service) {
        this.service = service;
        seedData();
    }

    private void seedData() {
        if (service.getAll().isEmpty()) {
            for (int i = 1; i <= 10; i++) {
                Object obj = new Object();
                obj.setTitle("Property " + i);
                obj.setDescription("Beautiful property " + i);
                obj.setObjectType(i % 2 == 0 ? "Apartment" : "House");
                obj.setDealType(i % 3 == 0 ? "Rent" : "Sale");
                obj.setPrice(new BigDecimal(150000 + i * 25000));
                obj.setAddress("Street " + i + ", City");
                obj.setArea(new BigDecimal(75 + i * 10));
                obj.setRooms(2 + (i % 4));
                obj.setBathrooms(1 + (i % 3));
                obj.setStatus("Available");
                service.create(obj);
            }
        }
    }

    public void menu() {
        while (true) {
            System.out.println("\n=== PROPERTY MANAGEMENT ===");
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
        List<Object> all = service.getAll();
        if (all.isEmpty()) {
            System.out.println("No properties found");
        } else {
            all.forEach(System.out::println);
        }
    }

    private void create() {
        String title = ConsoleHelper.ask("Property Title");
        String description = ConsoleHelper.ask("Description");
        String objectType = ConsoleHelper.ask("Object Type (Apartment/House)");
        String dealType = ConsoleHelper.ask("Deal Type (Sale/Rent)");
        String priceStr = ConsoleHelper.ask("Price");
        String address = ConsoleHelper.ask("Address");

        Object obj = new Object();
        obj.setTitle(title);
        obj.setDescription(description);
        obj.setObjectType(objectType);
        obj.setDealType(dealType);

        try {
            obj.setPrice(new BigDecimal(priceStr));
            obj.setArea(new BigDecimal(ConsoleHelper.ask("Area")));
            obj.setRooms(Integer.parseInt(ConsoleHelper.ask("Rooms")));
            obj.setBathrooms(Integer.parseInt(ConsoleHelper.ask("Bathrooms")));
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid number format");
            return;
        }

        obj.setAddress(address);
        obj.setStatus("Available");
        service.create(obj);
        System.out.println("✅ Property added: " + obj);
        ConsoleHelper.pause();
    }

    private void edit() {
        int id = ConsoleHelper.askInt("Enter property ID");
        service.getById(id).ifPresentOrElse(obj -> {
            obj.setTitle(ConsoleHelper.ask("Title (" + obj.getTitle() + ")"));
            obj.setStatus(ConsoleHelper.ask("Status (" + obj.getStatus() + ")"));
            obj.setPrice(new BigDecimal(ConsoleHelper.ask("Price (" + obj.getPrice() + ")")));
            service.update(obj);
            System.out.println("✅ Property updated: " + obj);
        }, () -> System.out.println("❌ Property not found."));
        ConsoleHelper.pause();
    }

    private void delete() {
        int id = ConsoleHelper.askInt("Enter property ID to delete");
        service.delete(id);
        System.out.println("✅ Property deleted");
        ConsoleHelper.pause();
    }
}