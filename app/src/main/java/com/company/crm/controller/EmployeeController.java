package com.company.crm.controller;

import com.company.crm.model.Employee;
import com.company.crm.service.interfaces.EmployeeService;
import com.company.crm.util.ConsoleHelper;

import java.time.LocalDate;
import java.util.List;

public class EmployeeController {
    private final EmployeeService service;
    public EmployeeController(EmployeeService service) { 
        this.service = service;
        seedData();
    }

    private void seedData() {
        if (service.getAll().isEmpty()) {
            for (int i = 1; i <= 10; i++) {
                Employee e = new Employee();
                e.setFirstName("Emp" + i);
                e.setLastName("Test" + i);
                e.setEmail("emp" + i + "@crm.com");
                e.setPhone("100-200-" + i);
                e.setPosition("Agent");
                e.setHireDate(LocalDate.now());
                service.create(e);
            }
        }
    }

    public void menu() {
        while (true) {
            System.out.println("\n1.List 2.Create 3.Edit 4.Delete 0.Back");
            int choice = ConsoleHelper.askInt("Choose");
            switch (choice) {
                case 1 -> list();
                case 2 -> create();
                case 3 -> edit();
                case 4 -> delete();
                case 0 -> { return; }
            }
        }
    }

    private void list() {
        List<Employee> all = service.getAll();
        all.forEach(System.out::println);
    }

    private void create() {
        String fn = ConsoleHelper.ask("Name");
        String ln = ConsoleHelper.ask("LastName");
        String email = ConsoleHelper.ask("Email");
        String phone = ConsoleHelper.ask("Number");
        String position = ConsoleHelper.ask("position");
        Employee e = new Employee();
        e.setFirstName(fn);
        e.setLastName(ln);
        e.setEmail(email);
        e.setPhone(phone);
        e.setPosition(position);
        e.setHireDate(LocalDate.now());
        service.create(e);
        System.out.println("✅ Add: " + e);
        ConsoleHelper.pause();
    }

    private void edit() {
        int id = ConsoleHelper.askInt("Add id");
        service.getById(id).ifPresentOrElse(e -> {
            e.setFirstName(ConsoleHelper.ask("Name (" + e.getFirstName() + ")"));
            e.setLastName(ConsoleHelper.ask("Lastnem (" + e.getLastName() + ")"));
            service.update(e);
            System.out.println("✅ Recycle: " + e);
        }, () -> System.out.println("❌ Not found."));
        ConsoleHelper.pause();
    }
    private void delete() {
        int id = ConsoleHelper.askInt("Add Int");
        service.delete(id);
        System.out.println("Delete complete");
        ConsoleHelper.pause();
    }
}
