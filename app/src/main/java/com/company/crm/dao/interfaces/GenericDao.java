package com.company.crm.dao.interfaces;




import java.util.List;
import java.util.Optional;

public interface GenericDao<T> {
    List<T> findAll();
    Optional<T> findById(int id);
    T save(T t);
    void update(T t);
    void delete(int id);
}
