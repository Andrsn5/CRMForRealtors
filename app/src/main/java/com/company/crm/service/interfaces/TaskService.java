package com.company.crm.service.interfaces;

import com.company.crm.model.EmployeeStats;
import com.company.crm.model.Task;

import java.util.List;

public interface TaskService extends GenericService<Task> {

    List<Task> findByResponsible(Integer id);

    EmployeeStats getEmployeeStats(Integer id);
}
