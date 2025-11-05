package com.company.crm.controller;

import com.company.crm.model.Client;
import com.company.crm.service.interfaces.ClientService;
import com.company.crm.util.ConsoleHelper;

import java.math.BigDecimal;
import java.util.List;

public class ClientController {
    private final ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
        seedData();
    }

    private void seedData() {
        if (service.getAll().isEmpty()) {
            for (int i = 1; i <= 10; i++) {
                Client client = new Client();
                client.setFirstName("Client" + i);
                client.setLastName("Smith" + i);
                client.setEmail("client" + i + "@example.com");
                client.setPhone("555-010-" + i);
                client.setClientType(i % 2 == 0 ? "Buyer" : "Seller");
                client.setBudget(new BigDecimal(100000 + i * 50000));
                client.setNotes("Sample client " + i);
                service.create(client);
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
            all.forEach(System.out::println);
        }
    }

    private void create() {
        String firstName = ConsoleHelper.ask("First Name");
        String lastName = ConsoleHelper.ask("Last Name");
        String email = ConsoleHelper.ask("Email");
        String phone = ConsoleHelper.ask("Phone");
        String clientType = ConsoleHelper.ask("Client Type (Buyer/Seller)");
        String budgetStr = ConsoleHelper.ask("Budget");

        Client client = new Client();
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setEmail(email);
        client.setPhone(phone);
        client.setClientType(clientType);

        try {
            client.setBudget(new BigDecimal(budgetStr));
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid budget format");
            return;
        }

        client.setNotes(ConsoleHelper.ask("Notes"));
        service.create(client);
        System.out.println("✅ Client added: " + client);
        ConsoleHelper.pause();
    }

    private void edit() {
        int id = ConsoleHelper.askInt("Enter client ID");
        service.getById(id).ifPresentOrElse(client -> {
            client.setFirstName(ConsoleHelper.ask("First Name (" + client.getFirstName() + ")"));
            client.setLastName(ConsoleHelper.ask("Last Name (" + client.getLastName() + ")"));
            client.setEmail(ConsoleHelper.ask("Email (" + client.getEmail() + ")"));
            client.setPhone(ConsoleHelper.ask("Phone (" + client.getPhone() + ")"));
            service.update(client);
            System.out.println("✅ Client updated: " + client);
        }, () -> System.out.println("❌ Client not found."));
        ConsoleHelper.pause();
    }

    private void delete() {
        int id = ConsoleHelper.askInt("Enter client ID to delete");
        service.delete(id);
        System.out.println("✅ Client deleted");
        ConsoleHelper.pause();
    }
}