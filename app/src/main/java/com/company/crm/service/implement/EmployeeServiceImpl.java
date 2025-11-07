package com.company.crm.service.implement;

import com.company.crm.dao.interfaces.EmployeeDao;
import com.company.crm.model.Employee;
import com.company.crm.service.interfaces.EmployeeService;
import com.company.crm.util.ValidationException;
import com.company.crm.util.ValidationUtils;

import java.util.*;

public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeDao dao;
    private final Set<String> validPositions = new HashSet<>(Arrays.asList(
            "Analyst", "Assistant", "Sales Manager", "Real Estate Agent", "Senior Manager"
    ));

    public EmployeeServiceImpl(EmployeeDao dao) {
        this.dao = dao;
    }

    @Override
    public List<Employee> getAll() {
        return dao.findAll();
    }

    @Override
    public Optional<Employee> getById(int id) {
        if (id <= 0) {
            throw new ValidationException("Employee ID must be positive");
        }
        return dao.findById(id);
    }

    @Override
    public Employee create(Employee employee) {
        validateEmployee(employee);
        checkEmailUniqueness(employee.getEmail());
        checkPosition(employee.getPosition());
        return dao.save(employee);
    }



    @Override
    public void update(Employee employee) {
        if (employee.getId() == null || employee.getId() <= 0) {
            throw new IllegalArgumentException("Valid employee ID is required for update");
        }
        validateEmployee(employee);
        checkEmailUniquenessForUpdate(employee.getId(), employee.getEmail());
        checkPosition(employee.getPosition());
        dao.update(employee);
    }

    @Override
    public void delete(int id) {
        if (id <= 0) {
            throw new ValidationException("Employee ID must be positive");
        }
        dao.delete(id);
    }

    private void validateEmployee(Employee employee) {
        ValidationUtils.validateRequired(employee.getFirstName(), "First name");
        ValidationUtils.validateRequired(employee.getLastName(), "Last name");
        ValidationUtils.validateRequired(employee.getEmail(), "Email");
        ValidationUtils.validateRequired(employee.getPosition(), "Position");


        ValidationUtils.validateEmail(employee.getEmail());
        ValidationUtils.validatePhone(employee.getPhone());
        ValidationUtils.validateMaxLength(employee.getFirstName(), 100, "First name");
        ValidationUtils.validateMaxLength(employee.getLastName(), 100, "Last name");
        ValidationUtils.validateMaxLength(employee.getEmail(), 255, "Email");
        ValidationUtils.validateMaxLength(employee.getPosition(), 100, "Position");
    }

    private void checkEmailUniqueness(String email) {
        dao.findAll().stream()
                .filter(emp -> email.equals(emp.getEmail()))
                .findFirst()
                .ifPresent(emp -> {
                    throw new ValidationException("Employee with email " + email + " already exists");
                });
    }

    private void checkPosition(String position) {
        if (!validPositions.contains(position)) {
            throw new ValidationException("Invalid position: " + position + ". Valid positions: " + validPositions);
        }
    }

    private void checkEmailUniquenessForUpdate(int employeeId, String email) {
        dao.findAll().stream()
                .filter(emp -> email.equals(emp.getEmail()) && !emp.getId().equals(employeeId))
                .findFirst()
                .ifPresent(emp -> {
                    throw new ValidationException("Employee with email " + email + " already exists");
                });
    }
}