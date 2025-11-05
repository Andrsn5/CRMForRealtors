package com.company.crm.config;

import com.company.crm.controller.*;
import com.company.crm.dao.inmemory.*;
import com.company.crm.dao.jdbc.*;
import com.company.crm.service.implement.*;
import com.company.crm.service.interfaces.*;

public class AppConfig {
    private final EmployeeDaoJdbcImpl employeeDao = new EmployeeDaoJdbcImpl();
    private final EmployeeService employeeService = new EmployeeServiceImpl(employeeDao);
    private final EmployeeController employeeController = new EmployeeController(employeeService);
    private final ClientDaoJdbcImpl ClientDao = new ClientDaoJdbcImpl();
    private final ClientService ClientService = new ClientServiceImpl(ClientDao);
    private final ClientController ClientController = new ClientController(ClientService);
    private final TaskDaoJdbcImpl TaskDao = new TaskDaoJdbcImpl();
    private final TaskService TaskService = new TaskServiceImpl(TaskDao);
    private final TaskController TaskController = new TaskController(TaskService);
    private final ObjectDaoJdbcImpl ObjectDao = new ObjectDaoJdbcImpl();
    private final ObjectService ObjectService = new ObjectServiceImpl(ObjectDao);
    private final ObjectController ObjectController = new ObjectController(ObjectService);
    private final PhotoDaoJdbcImpl PhotoDao = new PhotoDaoJdbcImpl();
    private final PhotoService PhotoService = new PhotoServiceImpl(PhotoDao);
    private final PhotoController PhotoController = new PhotoController(PhotoService);
    private final MeetingDaoJdbcImpl MeetingDao = new MeetingDaoJdbcImpl();
    private final MeetingService MeetingService = new MeetingServiceImpl(MeetingDao);
    private final MeetingController MeetingController = new MeetingController(MeetingService);
    private final DealDaoJdbcImpl DealDao = new DealDaoJdbcImpl();
    private final DealService DealService = new DealServiceImpl(DealDao);
    private final DealController DealController = new DealController(DealService);
    private final AdditionalConditionDaoJdbcImpl AdditionalConditionDao = new AdditionalConditionDaoJdbcImpl();
    private final AdditionalConditionService AdditionalConditionService = new AdditionalConditionServiceImpl(AdditionalConditionDao);
    private final AdditionalConditionController AdditionalConditionController = new AdditionalConditionController(AdditionalConditionService);


    private final ConsoleApp consoleApp ;
    public AppConfig() {

        this.consoleApp = new ConsoleApp(employeeController, ClientController,DealController,MeetingController,ObjectController,PhotoController,AdditionalConditionController,TaskController);
    }

    public ConsoleApp getConsoleApp() { return consoleApp; }
}
