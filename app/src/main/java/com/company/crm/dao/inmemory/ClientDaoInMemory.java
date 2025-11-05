package com.company.crm.dao.inmemory;

import com.company.crm.dao.interfaces.ClientDao;
import com.company.crm.model.Client;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientDaoInMemory implements ClientDao {
    private final Map<Integer,Client> storage = new HashMap<>();
    private final AtomicInteger idGen = new AtomicInteger();
    @Override
    public List<Client> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Optional<Client> findById(int id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Client save(Client client) {
        int id = idGen.incrementAndGet();
        client.setId(id);
        storage.put(id,client);
        return client;
    }

    @Override
    public void update(Client client) {
        if (!storage.containsKey(client.getId())) throw new NoSuchElementException("Not found");
        storage.put(client.getId(),client);
    }

    @Override
    public void delete(int id) {
        storage.remove(id);
    }
}
