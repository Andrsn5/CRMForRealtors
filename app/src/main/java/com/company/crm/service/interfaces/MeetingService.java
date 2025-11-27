package com.company.crm.service.interfaces;

import com.company.crm.model.AdditionalCondition;
import com.company.crm.model.Meeting;

import java.util.Optional;

public interface MeetingService extends GenericService<Meeting> {
    Optional<Meeting> safeGetById(Integer id);
}
