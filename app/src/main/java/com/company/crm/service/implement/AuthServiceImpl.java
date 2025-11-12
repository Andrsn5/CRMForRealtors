package com.company.crm.service.implement;

import com.company.crm.dao.interfaces.EmployeeDao;
import com.company.crm.model.Employee;
import com.company.crm.service.interfaces.AuthService;
import com.company.crm.util.PasswordUtil;

import java.util.Optional;

public class AuthServiceImpl implements AuthService {
    private final EmployeeDao employeeDao;

    public AuthServiceImpl(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    @Override
    public Optional<Employee> login(String email, String password) {
        Optional<Employee> employeeOpt = employeeDao.findByEmail(email);

        if (employeeOpt.isPresent()) {
            Employee employee = employeeOpt.get();
            System.out.println("Found employee: " + employee.getEmail());
            System.out.println("Stored hash: " + employee.getPasswordHash());
            System.out.println("Input password: " + password);

            String inputHash = PasswordUtil.hashPassword(password);
            System.out.println("Input hash: " + inputHash);

            boolean passwordMatches = PasswordUtil.checkPassword(password, employee.getPasswordHash());
            System.out.println("Password matches: " + passwordMatches);

            if (passwordMatches) {
                return Optional.of(employee);
            }
        } else {
            System.out.println("Employee not found with email: " + email);
        }

        return Optional.empty();
    }
}
