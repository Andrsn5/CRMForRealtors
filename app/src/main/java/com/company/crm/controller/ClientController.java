package com.company.crm.controller;

import com.company.crm.model.Client;
import com.company.crm.service.interfaces.ClientService;
import com.company.crm.util.ConsoleHelper;
import com.company.crm.util.ValidationException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ClientController {
    private final ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
    }

    public void seedData() {
        if (service.getAll().isEmpty()) {
            for (int i = 1; i <= 10; i++) {
                try {
                    Client client = new Client();
                    client.setFirstName("Client" + i);
                    client.setLastName("Smith" + i);
                    client.setEmail("client" + i + "@example.com");
                    client.setPhone("555-010-" + i);
                    client.setClientType(i % 2 == 0 ? "customer" : "seller");
                    client.setBudget(new BigDecimal(100000 + i * 50000));
                    client.setNotes("Sample client " + i);
                    service.create(client);
                } catch (ValidationException e) {
                    System.out.println("❌ Seed data validation error: " + e.getMessage());
                }
            }
        }
    }

    public void menu() {
        while (true) {
            System.out.println("\n=== CLIENT MANAGEMENT ===");
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
        List<Client> all = service.getAll();
        if (all.isEmpty()) {
            System.out.println("No clients found");
        } else {
            System.out.println("\n=== CLIENT LIST ===");
            all.forEach(System.out::println);
        }
    }

    private void create() {
        while (true) {
            try {
                System.out.println("\n=== CREATE NEW CLIENT ===");
                String firstName = ConsoleHelper.ask("First Name");
                String lastName = ConsoleHelper.ask("Last Name");
                String email = ConsoleHelper.ask("Email");
                String phone = ConsoleHelper.ask("Phone");
                String clientType = ConsoleHelper.ask("Client Type (customer/seller/tenant/landlord)");
                String budgetStr = ConsoleHelper.ask("Budget");
                String notes = ConsoleHelper.ask("Notes");

                Client client = new Client();
                client.setFirstName(firstName);
                client.setLastName(lastName);
                client.setEmail(email);
                client.setPhone(phone);
                client.setClientType(clientType);
                client.setNotes(notes);

                // Валидация бюджета
                try {
                    BigDecimal budget = new BigDecimal(budgetStr);
                    client.setBudget(budget);
                } catch (NumberFormatException e) {
                    throw new ValidationException("Invalid budget format. Please enter a valid number.");
                }

                service.create(client);
                System.out.println("✅ Client added: " + client);
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
                System.out.println("Please enter a valid number for budget.\n");

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
                System.out.println("\n=== EDIT CLIENT ===");
                int id = ConsoleHelper.askInt("Enter client ID");

                Optional<Client> clientOpt = service.getById(id);
                if (clientOpt.isEmpty()) {
                    System.out.println("❌ Client not found with ID: " + id);

                    String retry = ConsoleHelper.ask("Try different ID? (y/n)");
                    if (!retry.equalsIgnoreCase("y")) {
                        break;
                    }
                    continue;
                }

                Client client = clientOpt.get();
                System.out.println("Editing: " + client);

                // Запрашиваем новые данные с текущими значениями по умолчанию
                client.setFirstName(ConsoleHelper.ask("First Name (" + client.getFirstName() + ")"));
                client.setLastName(ConsoleHelper.ask("Last Name (" + client.getLastName() + ")"));
                client.setEmail(ConsoleHelper.ask("Email (" + client.getEmail() + ")"));
                client.setPhone(ConsoleHelper.ask("Phone (" + client.getPhone() + ")"));
                client.setClientType(ConsoleHelper.ask("Client Type (" + client.getClientType() + ")"));

                // Обработка бюджета
                String budgetStr = ConsoleHelper.ask("Budget (" + client.getBudget() + ")");
                if (!budgetStr.trim().isEmpty()) {
                    try {
                        BigDecimal budget = new BigDecimal(budgetStr);
                        client.setBudget(budget);
                    } catch (NumberFormatException e) {
                        throw new ValidationException("Invalid budget format. Please enter a valid number.");
                    }
                }

                client.setNotes(ConsoleHelper.ask("Notes (" + client.getNotes() + ")"));

                service.update(client);
                System.out.println("✅ Client updated: " + client);
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
                System.out.println("\n=== DELETE CLIENT ===");
                int id = ConsoleHelper.askInt("Enter client ID to delete");

                // Проверяем существование клиента перед удалением
                Optional<Client> clientOpt = service.getById(id);
                if (clientOpt.isEmpty()) {
                    System.out.println("❌ Client not found with ID: " + id);

                    String retry = ConsoleHelper.ask("Try different ID? (y/n)");
                    if (!retry.equalsIgnoreCase("y")) {
                        break;
                    }
                    continue;
                }

                Client client = clientOpt.get();
                System.out.println("About to delete: " + client);

                String confirm = ConsoleHelper.ask("Are you sure you want to delete this client? (y/n)");
                if (confirm.equalsIgnoreCase("y")) {
                    service.delete(id);
                    System.out.println("✅ Client deleted successfully");
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