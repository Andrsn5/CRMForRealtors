package com.company.crm.service.implement;

import com.company.crm.dao.interfaces.AdditionalConditionDao;
import com.company.crm.dao.interfaces.EmployeeDao;
import com.company.crm.model.AdditionalCondition;
import com.company.crm.model.Employee;
import com.company.crm.service.interfaces.AdditionalConditionService;
import com.company.crm.service.interfaces.EmployeeService;

import java.util.List;
import java.util.Optional;

public class AdditionalConditionServiceImpl implements AdditionalConditionService {
    private final AdditionalConditionDao dao;
    public AdditionalConditionServiceImpl(AdditionalConditionDao dao) { this.dao = dao; }

    @Override public List<AdditionalCondition> getAll() { return dao.findAll(); }
    @Override public Optional<AdditionalCondition> getById(int id) { return dao.findById(id); }
    @Override public AdditionalCondition create(AdditionalCondition e) { return dao.save(e); }
    @Override public void update(AdditionalCondition e) { dao.update(e); }
    @Override public void delete(int id) { dao.delete(id); }
}