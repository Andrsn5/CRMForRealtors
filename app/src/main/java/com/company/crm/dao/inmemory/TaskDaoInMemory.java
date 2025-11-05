package com.company.crm.dao.inmemory;

import com.company.crm.dao.interfaces.TaskDao;
import com.company.crm.model.Task;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskDaoInMemory implements TaskDao {
    private final Map<Integer, Task> storage = new HashMap<>();
    private final AtomicInteger idGen = new AtomicInteger();
    @Override
    public List<Task> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Optional<Task> findById(int id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Task save(Task task) {
        int id = idGen.incrementAndGet();
        task.setId(id);
        storage.put(id,task);
        return task;
    }

    @Override
    public void update(Task task) {
        if (!storage.containsKey(task.getId())) throw new NoSuchElementException("Not found");
        storage.put(task.getId(),task);
    }

    @Override
    public void delete(int id) {
        storage.remove(id);
    }
}
