package com.company.crm.dao.inmemory;

import com.company.crm.dao.interfaces.DealDao;
import com.company.crm.model.Deal;
import com.company.crm.model.Object;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DealDaoInMemory implements DealDao {
    private final Map<Integer, Deal> storage = new HashMap<>();
    private final AtomicInteger idGen = new AtomicInteger();
    @Override
    public List<Deal> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Optional<Deal> findById(int id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Deal save(Deal deal) {
        int id = idGen.incrementAndGet();
        deal.setId(id);
        storage.put(id,deal);
        return deal;
    }

    @Override
    public void update(Deal deal) {
        if (!storage.containsKey(deal.getId())) throw new NoSuchElementException("Not found");
        storage.put(deal.getId(),deal);
    }

    @Override
    public void delete(int id) {
        storage.remove(id);
    }
}
