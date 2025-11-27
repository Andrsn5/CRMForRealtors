package com.company.crm.model;

public class EmployeeStats {
    private int tasksCount;
    private int dealsCount;
    private int meetingsCount;
    private int totalEmployees;
    private int totalClients;
    private int activeDeals;

    public EmployeeStats() {}

    // Геттеры и сеттеры
    public int getTasksCount() { return tasksCount; }
    public void setTasksCount(int tasksCount) { this.tasksCount = tasksCount; }

    public int getDealsCount() { return dealsCount; }
    public void setDealsCount(int dealsCount) { this.dealsCount = dealsCount; }

    public int getMeetingsCount() { return meetingsCount; }
    public void setMeetingsCount(int meetingsCount) { this.meetingsCount = meetingsCount; }

    public int getTotalEmployees() { return totalEmployees; }
    public void setTotalEmployees(int totalEmployees) { this.totalEmployees = totalEmployees; }

    public int getTotalClients() { return totalClients; }
    public void setTotalClients(int totalClients) { this.totalClients = totalClients; }

    public int getActiveDeals() { return activeDeals; }
    public void setActiveDeals(int activeDeals) { this.activeDeals = activeDeals; }
}
