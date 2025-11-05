package com.company.crm.dao.inmemory;

import com.company.crm.dao.interfaces.ObjectDao;
import com.company.crm.model.Object;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ObjectDaoInMemory implements ObjectDao {
    private final Map<Integer,Object> storage = new HashMap<>();
    private final AtomicInteger idGen = new AtomicInteger();
    @Override
    public List<Object> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Optional<Object> findById(int id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Object save(Object object) {
        int id = idGen.incrementAndGet();
        object.setId(id);
        storage.put(id,object);
        return object;
    }

    @Override
    public void update(Object object) {
        if (!storage.containsKey(object.getId())) throw new NoSuchElementException("Not found");
        storage.put(object.getId(),object);
    }

    @Override
    public void delete(int id) {
        storage.remove(id);
    }
}
