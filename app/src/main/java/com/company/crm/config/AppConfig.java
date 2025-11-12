package com.company.crm.config;

import com.company.crm.dao.inmemory.*;
import com.company.crm.dao.jdbc.*;
import com.company.crm.service.implement.*;
import com.company.crm.service.interfaces.*;
import com.company.crm.controller.*;

public class AppConfig {
    // DAO instances
    private final EmployeeDaoJdbcImpl employeeDao = new EmployeeDaoJdbcImpl();
    private final ClientDaoJdbcImpl clientDao = new ClientDaoJdbcImpl();
    private final ObjectDaoJdbcImpl objectDao = new ObjectDaoJdbcImpl();
    private final TaskDaoJdbcImpl taskDao = new TaskDaoJdbcImpl();
    private final DealDaoJdbcImpl dealDao = new DealDaoJdbcImpl();
    private final MeetingDaoJdbcImpl meetingDao = new MeetingDaoJdbcImpl();
    private final PhotoDaoJdbcImpl photoDao = new PhotoDaoJdbcImpl();
    private final AdditionalConditionDaoJdbcImpl additionalConditionDao = new AdditionalConditionDaoJdbcImpl();



    // Service instances
    private final EmployeeService employeeService = new EmployeeServiceImpl(employeeDao);
    private final ClientService clientService = new ClientServiceImpl(clientDao);
    private final ObjectService objectService = new ObjectServiceImpl(objectDao);
    private final TaskService taskService = new TaskServiceImpl(taskDao, employeeDao, clientDao, objectDao,additionalConditionDao,dealDao,meetingDao);
    private final DealService dealService = new DealServiceImpl(dealDao, taskDao);
    private final MeetingService meetingService = new MeetingServiceImpl(meetingDao, clientDao, taskDao);
    private final PhotoService photoService = new PhotoServiceImpl(photoDao, objectDao);
    private final AdditionalConditionService additionalConditionService = new AdditionalConditionServiceImpl(additionalConditionDao);
    private final AuthService authService = new AuthServiceImpl(employeeDao);

    // Controller instances
    private final EmployeeController employeeController;
    private final ClientController clientController;
    private final TaskController taskController;
    private final ObjectController objectController;
    private final PhotoController photoController;
    private final MeetingController meetingController;
    private final DealController dealController;
    private final AdditionalConditionController additionalConditionController;

    private final ConsoleApp consoleApp;

    public AppConfig() {

        this.employeeController = new EmployeeController(employeeService);
        this.clientController = new ClientController(clientService);
        this.taskController = new TaskController(taskService);
        this.objectController = new ObjectController(objectService);
        this.photoController = new PhotoController(photoService);
        this.meetingController = new MeetingController(meetingService);
        this.dealController = new DealController(dealService);
        this.additionalConditionController = new AdditionalConditionController(additionalConditionService);

        this.consoleApp = new ConsoleApp(
                employeeController,
                clientController,
                dealController,
                meetingController,
                objectController,
                photoController,
                additionalConditionController,
                taskController
        );


        initializeSeedData();
    }


    private void initializeSeedData() {
        try {
            // Проверяем, не в тестовом ли мы окружении
            if (!isTestEnvironment()) {
                employeeController.seedData();
                clientController.seedData();
                objectController.seedData();
                taskController.seedData();
                photoController.seedData();
                meetingController.seedData();
                dealController.seedData();
                additionalConditionController.seedData();
            }
        } catch (Exception e) {

            System.out.println("Seed data initialization skipped: " + e.getMessage());
        }
    }

    /**
     * Проверяем, работает ли приложение в тестовом окружении
     */
    private boolean isTestEnvironment() {
        return java.lang.management.ManagementFactory.getRuntimeMXBean()
                .getInputArguments()
                .toString()
                .contains("junit");
    }

    // Getters for all components
    public ConsoleApp getConsoleApp() { return consoleApp; }
    public EmployeeService getEmployeeService() { return employeeService; }
    public ClientService getClientService() { return clientService; }
    public TaskService getTaskService() { return taskService; }
    public ObjectService getObjectService() { return objectService; }
    public PhotoService getPhotoService() { return photoService; }
    public MeetingService getMeetingService() { return meetingService; }
    public DealService getDealService() { return dealService; }
    public AdditionalConditionService getAdditionalConditionService() { return additionalConditionService; }
    public AuthService getAuthService() { return authService;}

    public EmployeeController getEmployeeController() { return employeeController; }
    public ClientController getClientController() { return clientController; }
    public TaskController getTaskController() { return taskController; }
    public ObjectController getObjectController() { return objectController; }
    public PhotoController getPhotoController() { return photoController; }
    public MeetingController getMeetingController() { return meetingController; }
    public DealController getDealController() { return dealController; }
    public AdditionalConditionController getAdditionalConditionController() { return additionalConditionController; }
}