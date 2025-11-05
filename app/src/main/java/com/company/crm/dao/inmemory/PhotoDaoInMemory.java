package com.company.crm.dao.inmemory;

import com.company.crm.dao.interfaces.PhotoDao;
import com.company.crm.model.AdditionalCondition;
import com.company.crm.model.Photo;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class PhotoDaoInMemory implements PhotoDao {
    private final Map<Integer, Photo> storage = new HashMap<>();
    private final AtomicInteger idGen = new AtomicInteger();
    @Override
    public List<Photo> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Optional<Photo> findById(int id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Photo save(Photo photo) {
        int id = idGen.incrementAndGet();
        photo.setId(id);
        storage.put(id,photo);
        return photo;
    }

    @Override
    public void update(Photo photo) {
        if (!storage.containsKey(photo.getId())) throw new NoSuchElementException("Not found");
        storage.put(photo.getId(),photo);
    }

    @Override
    public void delete(int id) {
        storage.remove(id);
    }
}
