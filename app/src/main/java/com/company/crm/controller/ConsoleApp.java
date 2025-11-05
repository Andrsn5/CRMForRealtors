package com.company.crm.controller;

import com.company.crm.util.ConsoleHelper;

public class ConsoleApp {
    private final EmployeeController employeeController;
    private final ClientController clientController;
    private final DealController dealController;
    private final MeetingController meetingController;
    private final ObjectController objectController;
    private final PhotoController photoController;
    private final AdditionalConditionController conditionController;
    private final TaskController taskController;

    public ConsoleApp(EmployeeController e, ClientController c, DealController d,
                      MeetingController m, ObjectController o, PhotoController p,
                      AdditionalConditionController cond, TaskController t) {
        this.employeeController = e;
        this.clientController = c;
        this.dealController = d;
        this.meetingController = m;
        this.objectController = o;
        this.photoController = p;
        this.conditionController = cond;
        this.taskController = t;
    }

    public void start() {
        while (true) {
            System.out.println("""
                    \n=== CRM MENU ===
                    1. Employees
                    2. Clients
                    3. Objects
                    4. Photos
                    5. Conditions
                    6. Tasks
                    7. Deals
                    8. Meetings
                    0. Exit
                    """);
            int choice = ConsoleHelper.askInt("Change");
            switch (choice) {
                case 1 -> employeeController.menu();
                case 2 -> clientController.menu();
                case 3 -> objectController.menu();
                case 4 -> photoController.menu();
                case 5 -> conditionController.menu();
                case 6 -> taskController.menu();
                case 7 -> dealController.menu();
                case 8 -> meetingController.menu();
                case 0 -> { System.out.println("Bye bye 👋"); return; }
                default -> System.out.println("❌ Wrong number.");
            }
        }
    }
}
