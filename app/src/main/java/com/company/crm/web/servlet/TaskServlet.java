package com.company.crm.web.servlet;

import com.company.crm.config.AppConfig;
import com.company.crm.model.Task;
import com.company.crm.service.interfaces.TaskService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;

public class TaskServlet extends HttpServlet {
    private TaskService taskService;

    @Override
    public void init() {
        AppConfig config = (AppConfig) getServletContext().getAttribute("appConfig");
        taskService = config.getTaskService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        if (action == null || action.equals("list")) {
            req.setAttribute("tasks", taskService.getAll());
            req.getRequestDispatcher("/WEB-INF/jsp/tasks/list.jsp").forward(req, resp);
        } else if (action.equals("edit")) {
            int id = Integer.parseInt(req.getParameter("id"));
            req.setAttribute("task", taskService.getById(id).orElse(null));
            req.getRequestDispatcher("/WEB-INF/jsp/tasks/form.jsp").forward(req, resp);
        } else if (action.equals("new")) {
            req.getRequestDispatcher("/WEB-INF/jsp/tasks/form.jsp").forward(req, resp);
        } else if (action.equals("delete")) {
            int id = Integer.parseInt(req.getParameter("id"));
            taskService.delete(id);
            resp.sendRedirect("task?action=list");
        } else if (action.equals("view")) {
            int id = Integer.parseInt(req.getParameter("id"));
            req.setAttribute("task", taskService.getById(id).orElse(null));
            req.getRequestDispatcher("/WEB-INF/jsp/tasks/view.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Task task = new Task();
        task.setTitle(req.getParameter("title"));
        task.setDescription(req.getParameter("description"));
        task.setPriority(req.getParameter("priority"));
        task.setStatus(req.getParameter("status"));

        String dueDate = req.getParameter("dueDate");
        if (dueDate != null && !dueDate.isBlank()) {
            task.setDueDate(LocalDateTime.parse(dueDate.replace("T", " ") + ":00"));
        }

        String responsibleId = req.getParameter("responsibleId");
        if (responsibleId != null && !responsibleId.isBlank()) {
            task.setResponsibleId(Integer.parseInt(responsibleId));
        }

        String creatorId = req.getParameter("creatorId");
        if (creatorId != null && !creatorId.isBlank()) {
            task.setCreatorId(Integer.parseInt(creatorId));
        }

        String clientId = req.getParameter("clientId");
        if (clientId != null && !clientId.isBlank()) {
            task.setClientId(Integer.parseInt(clientId));
        }

        String objectId = req.getParameter("objectId");
        if (objectId != null && !objectId.isBlank()) {
            task.setObjectId(Integer.parseInt(objectId));
        }

        String conditionId = req.getParameter("conditionId");
        if (conditionId != null && !conditionId.isBlank()) {
            task.setConditionId(Integer.parseInt(conditionId));
        }

        String dealId = req.getParameter("dealId");
        if (dealId != null && !dealId.isBlank()) {
            task.setDealId(Integer.parseInt(dealId));
        }

        String meetingId = req.getParameter("meetingId");
        if (meetingId != null && !meetingId.isBlank()) {
            task.setMeetingId(Integer.parseInt(meetingId));
        }

        String id = req.getParameter("id");
        if (id == null || id.isBlank()) {
            taskService.create(task);
        } else {
            task.setId(Integer.parseInt(id));
            taskService.update(task);
        }
        resp.sendRedirect("task?action=list");
    }
}