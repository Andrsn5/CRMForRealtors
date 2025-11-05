package com.company.crm.service.implement;

import com.company.crm.dao.interfaces.EmployeeDao;
import com.company.crm.model.Employee;
import com.company.crm.service.interfaces.EmployeeService;

import java.util.List;
import java.util.Optional;

public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeDao dao;
    public EmployeeServiceImpl(EmployeeDao dao) { this.dao = dao; }

    @Override public List<Employee> getAll() { return dao.findAll(); }
    @Override public Optional<Employee> getById(int id) { return dao.findById(id); }
    @Override public Employee create(Employee e) { return dao.save(e); }
    @Override public void update(Employee e) { dao.update(e); }
    @Override public void delete(int id) { dao.delete(id); }
}