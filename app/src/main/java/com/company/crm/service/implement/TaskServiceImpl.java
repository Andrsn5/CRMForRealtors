package com.company.crm.service.implement;

import com.company.crm.dao.interfaces.TaskDao;
import com.company.crm.model.Task;
import com.company.crm.service.interfaces.TaskService;

import java.util.List;
import java.util.Optional;

public class TaskServiceImpl implements TaskService {
    private final TaskDao dao;
    public TaskServiceImpl(TaskDao dao) { this.dao = dao; }

    @Override public List<Task> getAll() { return dao.findAll(); }
    @Override public Optional<Task> getById(int id) { return dao.findById(id); }
    @Override public Task create(Task e) { return dao.save(e); }
    @Override public void update(Task e) { dao.update(e); }
    @Override public void delete(int id) { dao.delete(id); }
}