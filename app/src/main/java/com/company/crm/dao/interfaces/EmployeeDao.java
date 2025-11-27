package com.company.crm.dao.interfaces;

import com.company.crm.model.Employee;

import java.util.Optional;

public interface EmployeeDao extends GenericDao<Employee> {
    Optional<Employee> findByEmail(String username);

}
