package com.company.crm.service.interfaces;

import com.company.crm.model.Deal;

import java.util.Optional;

public interface DealService extends GenericService<Deal> {
               // строгая версия
    Optional<Deal> safeGetById(Integer id);
}
