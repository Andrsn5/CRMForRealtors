package com.company.crm.service.interfaces;

import com.company.crm.model.Employee;
import java.util.Optional;

public interface AuthService {
    Optional<Employee> login(String username, String password);
}
