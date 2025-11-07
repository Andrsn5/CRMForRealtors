package com.company.crm.controller;

import com.company.crm.model.Object;
import com.company.crm.service.interfaces.ObjectService;
import com.company.crm.util.ConsoleHelper;
import com.company.crm.util.ValidationException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ObjectController {
    private final ObjectService service;

    public ObjectController(ObjectService service) {
        this.service = service;
    }

    public void seedData() {
        if (service.getAll().isEmpty()) {
            for (int i = 1; i <= 10; i++) {
                try {
                    Object obj = new Object();
                    obj.setTitle("Property " + i);
                    obj.setDescription("Beautiful property " + i);
                    obj.setObjectType(getObjectType(i));
                    obj.setDealType(getDealType(i));
                    obj.setPrice(new BigDecimal(150000 + i * 25000));
                    obj.setAddress("Street " + i + ", City");
                    obj.setArea(new BigDecimal(75 + i * 10));
                    obj.setRooms(2 + (i % 4));
                    obj.setBathrooms(1 + (i % 3));
                    obj.setStatus(getStatus(i));
                    service.create(obj);
                } catch (ValidationException e) {
                    System.out.println("❌ Seed data validation error: " + e.getMessage());
                }
            }
        }
    }

    private String getObjectType(int i) {
        String[] types = {"Apartment", "House", "Commercial", "Land", "Villa"};
        return types[i % types.length];
    }

    private String getDealType(int i) {
        String[] dealTypes = {"Sale", "Rent"};
        return dealTypes[i % dealTypes.length];
    }

    private String getStatus(int i) {
        String[] statuses = {"Available", "Sold", "Rented", "Under Contract", "Reserved"};
        return statuses[i % statuses.length];
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
            System.out.println("\n=== PROPERTIES LIST ===");
            all.forEach(System.out::println);
        }
    }

    private void create() {
        while (true) {
            try {
                System.out.println("\n=== CREATE NEW PROPERTY ===");
                String title = ConsoleHelper.ask("Property Title");
                String description = ConsoleHelper.ask("Description");
                String objectType = ConsoleHelper.ask("Object Type (Apartment/House/Commercial/Land/Villa)");
                String dealType = ConsoleHelper.ask("Deal Type (Sale/Rent)");
                String priceStr = ConsoleHelper.ask("Price");
                String address = ConsoleHelper.ask("Address");
                String areaStr = ConsoleHelper.ask("Area (sqm)");
                String roomsStr = ConsoleHelper.ask("Rooms");
                String bathroomsStr = ConsoleHelper.ask("Bathrooms");
                String status = ConsoleHelper.ask("Status (Available/Sold/Rented/Under Contract/Reserved)");

                Object obj = new Object();
                obj.setTitle(title);
                obj.setDescription(description);
                obj.setObjectType(objectType);
                obj.setDealType(dealType);
                obj.setAddress(address);
                obj.setStatus(status);

                // Валидация числовых полей
                try {
                    obj.setPrice(new BigDecimal(priceStr));
                } catch (NumberFormatException e) {
                    throw new ValidationException("Invalid price format. Please enter a valid number.");
                }

                try {
                    if (!areaStr.trim().isEmpty()) {
                        obj.setArea(new BigDecimal(areaStr));
                    }
                } catch (NumberFormatException e) {
                    throw new ValidationException("Invalid area format. Please enter a valid number.");
                }

                try {
                    if (!roomsStr.trim().isEmpty()) {
                        obj.setRooms(Integer.parseInt(roomsStr));
                    }
                } catch (NumberFormatException e) {
                    throw new ValidationException("Invalid rooms format. Please enter a valid integer.");
                }

                try {
                    if (!bathroomsStr.trim().isEmpty()) {
                        obj.setBathrooms(Integer.parseInt(bathroomsStr));
                    }
                } catch (NumberFormatException e) {
                    throw new ValidationException("Invalid bathrooms format. Please enter a valid integer.");
                }

                service.create(obj);
                System.out.println("✅ Property added: " + obj);
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
                System.out.println("Please enter valid numbers for price, area, rooms, and bathrooms.\n");

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
                System.out.println("\n=== EDIT PROPERTY ===");
                int id = ConsoleHelper.askInt("Enter property ID");

                Optional<Object> objectOpt = service.getById(id);
                if (objectOpt.isEmpty()) {
                    System.out.println("❌ Property not found with ID: " + id);

                    String retry = ConsoleHelper.ask("Try different ID? (y/n)");
                    if (!retry.equalsIgnoreCase("y")) {
                        break;
                    }
                    continue;
                }

                Object obj = objectOpt.get();
                System.out.println("Editing: " + obj);

                // Запрашиваем новые данные с текущими значениями по умолчанию
                obj.setTitle(ConsoleHelper.ask("Title (" + obj.getTitle() + ")"));
                obj.setDescription(ConsoleHelper.ask("Description (" + obj.getDescription() + ")"));
                obj.setObjectType(ConsoleHelper.ask("Object Type (" + obj.getObjectType() + ")"));
                obj.setDealType(ConsoleHelper.ask("Deal Type (" + obj.getDealType() + ")"));
                obj.setAddress(ConsoleHelper.ask("Address (" + obj.getAddress() + ")"));
                obj.setStatus(ConsoleHelper.ask("Status (" + obj.getStatus() + ")"));

                // Обработка цены
                String priceStr = ConsoleHelper.ask("Price (" + obj.getPrice() + ")");
                if (!priceStr.trim().isEmpty()) {
                    try {
                        obj.setPrice(new BigDecimal(priceStr));
                    } catch (NumberFormatException e) {
                        throw new ValidationException("Invalid price format. Please enter a valid number.");
                    }
                }

                // Обработка площади
                String areaStr = ConsoleHelper.ask("Area (" + (obj.getArea() != null ? obj.getArea() : "null") + ")");
                if (!areaStr.trim().isEmpty()) {
                    try {
                        if ("null".equalsIgnoreCase(areaStr.trim())) {
                            obj.setArea(null);
                        } else {
                            obj.setArea(new BigDecimal(areaStr));
                        }
                    } catch (NumberFormatException e) {
                        throw new ValidationException("Invalid area format. Please enter a valid number or 'null'.");
                    }
                }

                // Обработка комнат
                String roomsStr = ConsoleHelper.ask("Rooms (" + (obj.getRooms() != null ? obj.getRooms() : "null") + ")");
                if (!roomsStr.trim().isEmpty()) {
                    try {
                        if ("null".equalsIgnoreCase(roomsStr.trim())) {
                            obj.setRooms(null);
                        } else {
                            obj.setRooms(Integer.parseInt(roomsStr));
                        }
                    } catch (NumberFormatException e) {
                        throw new ValidationException("Invalid rooms format. Please enter a valid integer or 'null'.");
                    }
                }

                // Обработка ванных комнат
                String bathroomsStr = ConsoleHelper.ask("Bathrooms (" + (obj.getBathrooms() != null ? obj.getBathrooms() : "null") + ")");
                if (!bathroomsStr.trim().isEmpty()) {
                    try {
                        if ("null".equalsIgnoreCase(bathroomsStr.trim())) {
                            obj.setBathrooms(null);
                        } else {
                            obj.setBathrooms(Integer.parseInt(bathroomsStr));
                        }
                    } catch (NumberFormatException e) {
                        throw new ValidationException("Invalid bathrooms format. Please enter a valid integer or 'null'.");
                    }
                }

                service.update(obj);
                System.out.println("✅ Property updated: " + obj);
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
                System.out.println("\n=== DELETE PROPERTY ===");
                int id = ConsoleHelper.askInt("Enter property ID to delete");

                // Проверяем существование свойства перед удалением
                Optional<Object> objectOpt = service.getById(id);
                if (objectOpt.isEmpty()) {
                    System.out.println("❌ Property not found with ID: " + id);

                    String retry = ConsoleHelper.ask("Try different ID? (y/n)");
                    if (!retry.equalsIgnoreCase("y")) {
                        break;
                    }
                    continue;
                }

                Object obj = objectOpt.get();
                System.out.println("About to delete: " + obj);

                String confirm = ConsoleHelper.ask("Are you sure you want to delete this property? (y/n)");
                if (confirm.equalsIgnoreCase("y")) {
                    service.delete(id);
                    System.out.println("✅ Property deleted successfully");
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