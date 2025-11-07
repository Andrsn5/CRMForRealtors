package com.company.crm.controller;

import com.company.crm.model.Employee;
import com.company.crm.service.interfaces.EmployeeService;
import com.company.crm.util.ConsoleHelper;
import com.company.crm.util.ValidationException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class EmployeeController {
    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    public void seedData() {
        if (service.getAll().isEmpty()) {
            for (int i = 1; i <= 10; i++) {
                try {
                    Employee e = new Employee();
                    e.setFirstName("Emp" + i);
                    e.setLastName("Test" + i);
                    e.setEmail("emp" + i + "@crm.com");
                    e.setPhone("100-200-" + i);
                    e.setPosition("Analyst");
                    e.setHireDate(LocalDate.now());
                    service.create(e);
                } catch (ValidationException e) {
                    System.out.println("❌ Seed data validation error: " + e.getMessage());
                }
            }
        }
    }

    public void menu() {
        while (true) {
            System.out.println("\n=== EMPLOYEE MANAGEMENT ===");
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
        List<Employee> all = service.getAll();
        if (all.isEmpty()) {
            System.out.println("No employees found");
        } else {
            System.out.println("\n=== EMPLOYEE LIST ===");
            all.forEach(System.out::println);
        }
    }

    private void create() {
        while (true) {
            try {
                System.out.println("\n=== CREATE NEW EMPLOYEE ===");
                String fn = ConsoleHelper.ask("First Name");
                String ln = ConsoleHelper.ask("Last Name");
                String email = ConsoleHelper.ask("Email");
                String phone = ConsoleHelper.ask("Phone");
                String position = ConsoleHelper.ask("Position");

                Employee e = new Employee();
                e.setFirstName(fn);
                e.setLastName(ln);
                e.setEmail(email);
                e.setPhone(phone);
                e.setPosition(position);
                e.setHireDate(LocalDate.now());

                service.create(e);
                System.out.println("✅ Employee added: " + e);
                break; // Выход из цикла при успешном создании

            } catch (ValidationException e) {
                System.out.println("❌ Validation error: " + e.getMessage());
                System.out.println("Please correct the data and try again.\n");

                // Спрашиваем, хочет ли пользователь повторить попытку
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
                System.out.println("\n=== EDIT EMPLOYEE ===");
                int id = ConsoleHelper.askInt("Enter employee ID");

                Optional<Employee> employeeOpt = service.getById(id);
                if (employeeOpt.isEmpty()) {
                    System.out.println("❌ Employee not found with ID: " + id);

                    String retry = ConsoleHelper.ask("Try different ID? (y/n)");
                    if (!retry.equalsIgnoreCase("y")) {
                        break;
                    }
                    continue;
                }

                Employee employee = employeeOpt.get();
                System.out.println("Editing: " + employee);

                // Запрашиваем новые данные с текущими значениями по умолчанию
                employee.setFirstName(ConsoleHelper.ask("First Name (" + employee.getFirstName() + ")"));
                employee.setLastName(ConsoleHelper.ask("Last Name (" + employee.getLastName() + ")"));
                employee.setEmail(ConsoleHelper.ask("Email (" + employee.getEmail() + ")"));
                employee.setPhone(ConsoleHelper.ask("Phone (" + employee.getPhone() + ")"));
                employee.setPosition(ConsoleHelper.ask("Position (" + employee.getPosition() + ")"));

                service.update(employee);
                System.out.println("✅ Employee updated: " + employee);
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
                System.out.println("\n=== DELETE EMPLOYEE ===");
                int id = ConsoleHelper.askInt("Enter employee ID to delete");

                // Проверяем существование сотрудника перед удалением
                Optional<Employee> employeeOpt = service.getById(id);
                if (employeeOpt.isEmpty()) {
                    System.out.println("❌ Employee not found with ID: " + id);

                    String retry = ConsoleHelper.ask("Try different ID? (y/n)");
                    if (!retry.equalsIgnoreCase("y")) {
                        break;
                    }
                    continue;
                }

                Employee employee = employeeOpt.get();
                System.out.println("About to delete: " + employee);

                String confirm = ConsoleHelper.ask("Are you sure you want to delete this employee? (y/n)");
                if (confirm.equalsIgnoreCase("y")) {
                    service.delete(id);
                    System.out.println("✅ Employee deleted successfully");
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