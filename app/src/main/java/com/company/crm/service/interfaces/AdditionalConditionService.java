package com.company.crm.service.interfaces;

import com.company.crm.model.AdditionalCondition;
import com.company.crm.model.Deal;

import java.util.Optional;

public interface AdditionalConditionService extends GenericService<AdditionalCondition> {
    Optional<AdditionalCondition> safeGetById(Integer id);
}
