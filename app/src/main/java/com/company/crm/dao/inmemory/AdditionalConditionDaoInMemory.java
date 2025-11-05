package com.company.crm.dao.inmemory;

import com.company.crm.dao.interfaces.AdditionalConditionDao;
import com.company.crm.model.AdditionalCondition;
import com.company.crm.model.Deal;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AdditionalConditionDaoInMemory implements AdditionalConditionDao {
    private final Map<Integer, AdditionalCondition> storage = new HashMap<>();
    private final AtomicInteger idGen = new AtomicInteger();
    @Override
    public List<AdditionalCondition> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Optional<AdditionalCondition> findById(int id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public AdditionalCondition save(AdditionalCondition additionalCondition) {
        int id = idGen.incrementAndGet();
        additionalCondition.setId(id);
        storage.put(id,additionalCondition);
        return additionalCondition;
    }

    @Override
    public void update(AdditionalCondition additionalCondition) {
        if (!storage.containsKey(additionalCondition.getId())) throw new NoSuchElementException("Not found");
        storage.put(additionalCondition.getId(),additionalCondition);
    }

    @Override
    public void delete(int id) {
        storage.remove(id);
    }
}
