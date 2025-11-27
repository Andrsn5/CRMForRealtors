package com.company.crm.service;

import com.company.crm.config.AppConfig;
import com.company.crm.model.Employee;
import com.company.crm.service.interfaces.EmployeeService;
import com.company.crm.util.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeServiceTest {
    private EmployeeService employeeService;
    private AppConfig appConfig;

    @BeforeEach
    void setUp() {
        appConfig = new AppConfig();
        employeeService = appConfig.getEmployeeService();
    }

    @Test
    void testCreateEmployee_WithValidData_ShouldSuccess() {
        Employee employee = createTestEmployee("John", "Doe", "john@test.com", "Real Estate Agent");

        Employee created = employeeService.create(employee);

        assertNotNull(created);
        assertEquals("John", created.getFirstName());
        assertEquals("Doe", created.getLastName());
        assertEquals("john@test.com", created.getEmail());
        assertEquals("Real Estate Agent", created.getPosition());
    }

    @Test
    void testCreateEmployee_NullFirstName_ShouldThrowValidationException() {
        Employee employee = createTestEmployee(null, "Doe", "john@test.com", "Real Estate Agent");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> employeeService.create(employee));

        assertEquals("First name is required", exception.getMessage());
    }

    @Test
    void testCreateEmployee_NullLastName_ShouldThrowValidationException() {
        Employee employee = createTestEmployee("John", null, "john@test.com", "Real Estate Agent");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> employeeService.create(employee));

        assertEquals("Last name is required", exception.getMessage());
    }

    @Test
    void testCreateEmployee_NullEmail_ShouldThrowValidationException() {
        Employee employee = createTestEmployee("John", "Doe", null, "Real Estate Agent");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> employeeService.create(employee));

        assertEquals("Email is required", exception.getMessage());
    }

    @Test
    void testCreateEmployee_NullPosition_ShouldThrowValidationException() {
        Employee employee = createTestEmployee("John", "Doe", "john@test.com", null);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> employeeService.create(employee));

        assertEquals("Position is required", exception.getMessage());
    }

    @Test
    void testCreateEmployee_InvalidEmail_ShouldThrowValidationException() {
        Employee employee = createTestEmployee("John", "Doe", "invalid-email", "Real Estate Agent");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> employeeService.create(employee));

        assertTrue(exception.getMessage().contains("Invalid email format"));
    }

    @Test
    void testCreateEmployee_DuplicateEmail_ShouldThrowException() {
        Employee employee1 = createTestEmployee("John", "Doe", "duplicate@test.com", "Real Estate Agent");
        employeeService.create(employee1);

        Employee employee2 = createTestEmployee("Jane", "Smith", "duplicate@test.com", "Sales Manager");

        Exception exception = assertThrows(Exception.class,
                () -> employeeService.create(employee2));

        assertTrue(exception.getMessage().contains("already exists"));
    }

    @Test
    void testUpdateEmployee_ChangePosition_ShouldUpdate() {
        Employee employee = employeeService.create(createTestEmployee("John", "Doe", "update@test.com", "Real Estate Agent"));
        employee.setPosition("Sales Manager");

        employeeService.update(employee);
        Optional<Employee> updated = employeeService.getById(employee.getId());

        assertTrue(updated.isPresent());
        assertEquals("Sales Manager", updated.get().getPosition());
    }

    @Test
    void testGetAllEmployees_ShouldReturnAll() {
        employeeService.create(createTestEmployee("John", "Doe", "john1@test.com", "Real Estate Agent"));
        employeeService.create(createTestEmployee("Jane", "Smith", "jane@test.com", "Sales Manager"));

        List<Employee> allEmployees = employeeService.getAll();

        assertTrue(allEmployees.size() >= 2);
    }

    @Test
    void testDeleteEmployee_ThenTryToFind_ShouldBeEmpty() {
        Employee employee = employeeService.create(createTestEmployee("John", "Doe", "delete@test.com", "Real Estate Agent"));
        int employeeId = employee.getId();

        employeeService.delete(employeeId);
        Optional<Employee> found = employeeService.getById(employeeId);

        assertTrue(found.isEmpty());
    }

    @Test
    void testCreateEmployee_WithOnlyRequiredFields_ShouldSuccess() {
        Employee employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("required@test.com");
        employee.setPosition("Real Estate Agent");

        Employee created = employeeService.create(employee);

        assertNotNull(created);
        assertEquals("John", created.getFirstName());
        assertEquals("Doe", created.getLastName());
        assertEquals("required@test.com", created.getEmail());
        assertEquals("Real Estate Agent", created.getPosition());
    }

    @Test
    void testCreateEmployee_WithLongNames_ShouldThrowValidationException() {
        Employee employee = createTestEmployee("A".repeat(101), "Doe", "long@test.com", "Real Estate Agent");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> employeeService.create(employee));

        assertTrue(exception.getMessage().contains("First name"));
    }

    @Test
    void testGetEmployee_WithInvalidId_ShouldThrowException() {
        Exception exception = assertThrows(Exception.class,
                () -> employeeService.getById(-1));

        assertNotNull(exception.getMessage());
    }

    @Test
    void testCreateEmployee_WithInactiveStatus_ShouldWork() {
        Employee employee = createTestEmployee("Inactive", "Employee", "inactive@test.com", "Real Estate Agent");
        employee.setActive(false);

        Employee created = employeeService.create(employee);

        assertNotNull(created);
        assertFalse(created.isActive());
    }

    @Test
    void testCreateEmployee_WithValidPositions_ShouldSuccess() {
        String[] validPositions = {"Analyst", "Assistant", "Real Estate Agent", "Senior Manager", "Sales Manager"};

        for (String position : validPositions) {
            Employee employee = createTestEmployee("Test", "Employee", "test" + position.hashCode() + "@test.com", position);
            assertDoesNotThrow(() -> employeeService.create(employee),
                    "Создание сотрудника с позицией '" + position + "' должно работать");
        }
    }

    @Test
    void testCreateEmployee_WithInvalidPosition_ShouldThrowValidationException() {
        Employee employee = createTestEmployee("John", "Doe", "invalid@test.com", "Invalid Position");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> employeeService.create(employee));

        assertTrue(exception.getMessage().contains("Invalid position"));
    }

    @Test
    void testUpdateEmployee_WithInvalidPosition_ShouldThrowValidationException() {
        Employee employee = employeeService.create(createTestEmployee("John", "Doe", "update-invalid@test.com", "Real Estate Agent"));
        employee.setPosition("Invalid Position");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> employeeService.update(employee));

        assertTrue(exception.getMessage().contains("Invalid position"));
    }

    private Employee createTestEmployee(String firstName, String lastName, String email, String position) {
        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmail(email);
        employee.setPosition(position);
        employee.setPhone("123-456-7890");
        employee.setActive(true);
        return employee;
    }
}