package com.company.crm.dao.inmemory;

import com.company.crm.dao.interfaces.EmployeeDao;
import com.company.crm.model.Employee;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class EmployeeDaoInMemory implements EmployeeDao {
    private final Map<Integer,Employee> storage = new HashMap<>();
    private final AtomicInteger idGen = new AtomicInteger();
    @Override
    public List<Employee> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Optional<Employee> findById(int id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Employee save(Employee employee) {
        int id = idGen.incrementAndGet();
        employee.setId(id);
        storage.put(id,employee);
        return employee;
    }

    @Override
    public void update(Employee employee) {
        if (!storage.containsKey(employee.getId())) throw new NoSuchElementException("Not found");
        storage.put(employee.getId(),employee);
    }

    @Override
    public void delete(int id) {
        storage.remove(id);
    }

    @Override
    public Optional<Employee> findByEmail(String username) {
        return Optional.empty();
    }
}
