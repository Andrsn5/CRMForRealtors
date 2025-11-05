package com.company.crm.service.implement;

import com.company.crm.dao.interfaces.ClientDao;
import com.company.crm.dao.interfaces.DealDao;
import com.company.crm.model.Client;
import com.company.crm.model.Deal;
import com.company.crm.service.interfaces.ClientService;
import com.company.crm.service.interfaces.DealService;

import java.util.List;
import java.util.Optional;

public class DealServiceImpl implements DealService {
    private final DealDao dao;
    public DealServiceImpl(DealDao dao) { this.dao = dao; }

    @Override public List<Deal> getAll() { return dao.findAll(); }
    @Override public Optional<Deal> getById(int id) { return dao.findById(id); }
    @Override public Deal create(Deal e) { return dao.save(e); }
    @Override public void update(Deal e) { dao.update(e); }
    @Override public void delete(int id) { dao.delete(id); }
}