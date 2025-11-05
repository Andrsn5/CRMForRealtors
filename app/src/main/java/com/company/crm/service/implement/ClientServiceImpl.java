package com.company.crm.service.implement;

import com.company.crm.dao.interfaces.ClientDao;
import com.company.crm.dao.interfaces.EmployeeDao;
import com.company.crm.model.Client;
import com.company.crm.model.Employee;
import com.company.crm.service.interfaces.ClientService;
import com.company.crm.service.interfaces.EmployeeService;

import java.util.List;
import java.util.Optional;

public class ClientServiceImpl implements ClientService {
    private final ClientDao dao;
    public ClientServiceImpl(ClientDao dao) { this.dao = dao; }

    @Override public List<Client> getAll() { return dao.findAll(); }
    @Override public Optional<Client> getById(int id) { return dao.findById(id); }
    @Override public Client create(Client e) { return dao.save(e); }
    @Override public void update(Client e) { dao.update(e); }
    @Override public void delete(int id) { dao.delete(id); }
}