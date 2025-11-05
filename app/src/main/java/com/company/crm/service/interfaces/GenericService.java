package com.company.crm.service.interfaces;

import com.company.crm.model.*;
import com.company.crm.model.Object;

import java.util.List;
import java.util.Optional;

public interface GenericService<T> {
    List<T> getAll();
    Optional<T> getById(int id);
    T create(T e);
    void update(T e);
    void delete(int id);
}
