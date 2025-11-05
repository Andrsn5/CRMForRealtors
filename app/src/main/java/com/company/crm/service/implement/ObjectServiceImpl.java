package com.company.crm.service.implement;


import com.company.crm.dao.interfaces.ObjectDao;
import com.company.crm.service.interfaces.ObjectService;
import com.company.crm.model.Object;

import java.util.List;
import java.util.Optional;

public class ObjectServiceImpl implements ObjectService {
    private final ObjectDao dao;
    public ObjectServiceImpl(ObjectDao dao) { this.dao = dao; }

    @Override public List<Object> getAll() { return dao.findAll(); }
    @Override public Optional<Object> getById(int id) { return dao.findById(id); }
    @Override public Object create(Object e) { return dao.save(e); }
    @Override public void update(Object e) { dao.update(e); }
    @Override public void delete(int id) { dao.delete(id); }
}