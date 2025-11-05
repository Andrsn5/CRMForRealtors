package com.company.crm.config;
import com.company.crm.dao.inmemory.*;
import com.company.crm.service.implement.*;
import com.company.crm.service.interfaces.*;
import com.company.crm.controller.*;

public class AppConfig {
    private final EmployeeDaoInMemory employeeDao = new EmployeeDaoInMemory();
    private final EmployeeService employeeService = new EmployeeServiceImpl(employeeDao);
    private final EmployeeController employeeController = new EmployeeController(employeeService);
    private final ClientDaoInMemory ClientDao = new ClientDaoInMemory();
    private final ClientService ClientService = new ClientServiceImpl(ClientDao);
    private final ClientController ClientController = new ClientController(ClientService);
    private final TaskDaoInMemory TaskDao = new TaskDaoInMemory();
    private final TaskService TaskService = new TaskServiceImpl(TaskDao);
    private final TaskController TaskController = new TaskController(TaskService);
    private final ObjectDaoInMemory ObjectDao = new ObjectDaoInMemory();
    private final ObjectService ObjectService = new ObjectServiceImpl(ObjectDao);
    private final ObjectController ObjectController = new ObjectController(ObjectService);
    private final PhotoDaoInMemory PhotoDao = new PhotoDaoInMemory();
    private final PhotoService PhotoService = new PhotoServiceImpl(PhotoDao);
    private final PhotoController PhotoController = new PhotoController(PhotoService);
    private final MeetingDaoInMemory MeetingDao = new MeetingDaoInMemory();
    private final MeetingService MeetingService = new MeetingServiceImpl(MeetingDao);
    private final MeetingController MeetingController = new MeetingController(MeetingService);
    private final DealDaoInMemory DealDao = new DealDaoInMemory();
    private final DealService DealService = new DealServiceImpl(DealDao);
    private final DealController DealController = new DealController(DealService);
    private final AdditionalConditionDaoInMemory AdditionalConditionDao = new AdditionalConditionDaoInMemory();
    private final AdditionalConditionService AdditionalConditionService = new AdditionalConditionServiceImpl(AdditionalConditionDao);
    private final AdditionalConditionController AdditionalConditionController = new AdditionalConditionController(AdditionalConditionService);


    private final ConsoleApp consoleApp ;
    public AppConfig() {

        this.consoleApp = new ConsoleApp(employeeController, ClientController,DealController,MeetingController,ObjectController,PhotoController,AdditionalConditionController,TaskController);
    }

    public ConsoleApp getConsoleApp() { return consoleApp; }
}
