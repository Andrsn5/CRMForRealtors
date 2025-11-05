package com.company.crm.service.implement;

import com.company.crm.dao.interfaces.EmployeeDao;
import com.company.crm.dao.interfaces.PhotoDao;
import com.company.crm.model.Employee;
import com.company.crm.model.Photo;
import com.company.crm.service.interfaces.EmployeeService;
import com.company.crm.service.interfaces.PhotoService;

import java.util.List;
import java.util.Optional;

public class PhotoServiceImpl implements PhotoService {
    private final PhotoDao dao;
    public PhotoServiceImpl(PhotoDao dao) { this.dao = dao; }

    @Override public List<Photo> getAll() { return dao.findAll(); }
    @Override public Optional<Photo> getById(int id) { return dao.findById(id); }
    @Override public Photo create(Photo e) { return dao.save(e); }
    @Override public void update(Photo e) { dao.update(e); }
    @Override public void delete(int id) { dao.delete(id); }
}