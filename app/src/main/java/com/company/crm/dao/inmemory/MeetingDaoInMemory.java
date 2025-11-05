package com.company.crm.dao.inmemory;

import com.company.crm.dao.interfaces.MeetingDao;
import com.company.crm.model.AdditionalCondition;
import com.company.crm.model.Meeting;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MeetingDaoInMemory implements MeetingDao {
    private final Map<Integer, Meeting> storage = new HashMap<>();
    private final AtomicInteger idGen = new AtomicInteger();
    @Override
    public List<Meeting> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Optional<Meeting> findById(int id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Meeting save(Meeting meeting) {
        int id = idGen.incrementAndGet();
        meeting.setId(id);
        storage.put(id,meeting);
        return meeting;
    }

    @Override
    public void update(Meeting meeting) {
        if (!storage.containsKey(meeting.getId())) throw new NoSuchElementException("Not found");
        storage.put(meeting.getId(),meeting);
    }

    @Override
    public void delete(int id) {
        storage.remove(id);
    }
}
