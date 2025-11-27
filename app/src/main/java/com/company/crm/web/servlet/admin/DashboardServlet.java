package com.company.crm.web.servlet.admin;

import com.company.crm.config.AppConfig;
import com.company.crm.model.Employee;
import com.company.crm.model.Task;
import com.company.crm.service.interfaces.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.*;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    private TaskService taskService;
    private EmployeeService employeeService;

    @Override
    public void init() {
        AppConfig config = (AppConfig) getServletContext().getAttribute("appConfig");
        taskService = config.getTaskService();
        employeeService = config.getEmployeeService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Employee emp = (Employee) req.getSession().getAttribute("employee");
        if (emp == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Все задачи текущего сотрудника
        List<Task> myTasks = taskService.findByResponsible(emp.getId());

        // Статистика
        Map<String, Object> stats = new HashMap<>();
        stats.put("tasksCount", myTasks.size());
        stats.put("totalEmployees", employeeService.getAll().size());

        // Группируем задачи по статусу для лучшего отображения
        Map<String, List<Task>> tasksByStatus = new HashMap<>();
        tasksByStatus.put("CANCELLED", new ArrayList<>());
        tasksByStatus.put("IN_PROGRESS", new ArrayList<>());
        tasksByStatus.put("ON_HOLD", new ArrayList<>());
        tasksByStatus.put("COMPLETED", new ArrayList<>()); // Оставляем для статистики

        for (Task task : myTasks) {
            String status = task.getStatus() != null ? task.getStatus().toUpperCase() : "PENDING";
            String normalizedStatus = status.replace(" ", "_");
            tasksByStatus.computeIfAbsent(normalizedStatus, k -> new ArrayList<>()).add(task);
        }

        // Подсчитываем только активные задачи (исключая завершенные)
        int activeTasksCount = myTasks.stream()
                .filter(task -> !"COMPLETED".equals(task.getStatus().toUpperCase()))
                .mapToInt(task -> 1)
                .sum();

        stats.put("activeTasksCount", activeTasksCount);
        stats.put("completedTasksCount", tasksByStatus.get("COMPLETED").size());

        req.setAttribute("myTasks", myTasks);
        req.setAttribute("tasksByStatus", tasksByStatus);
        req.setAttribute("stats", stats);
        req.getRequestDispatcher("/WEB-INF/jsp/dashboard.jsp").forward(req, resp);
    }
}



