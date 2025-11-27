package com.company.crm.web.servlet.user;

import com.company.crm.config.AppConfig;
import com.company.crm.model.AdditionalCondition;
import com.company.crm.service.interfaces.AdditionalConditionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/quick-condition")
public class MyConditionsServlet extends HttpServlet {
    private AdditionalConditionService conditionService;

    @Override
    public void init() {
        AppConfig config = (AppConfig) getServletContext().getAttribute("appConfig");
        conditionService = config.getAdditionalConditionService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        System.out.println("QuickConditionServlet doGet called with action: " + action);

        if ("add".equals(action)) {
            showAddConditionForm(req, resp);
        } else if ("list".equals(action)) {
            resp.setContentType("application/json");
            resp.getWriter().write("{\"status\":\"success\"}");
        } else {
            showAddConditionForm(req, resp);
        }
    }

    private void showAddConditionForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.getRequestDispatcher("/WEB-INF/jsp/addCondition.jsp").forward(req, resp);
        } catch (Exception e) {
            System.err.println("Error forwarding to JSP: " + e.getMessage());
            e.printStackTrace();
            resp.getWriter().write("<html><body><h1>Quick Add Condition</h1><form method='post' action='quick-condition?action=add'><input name='description' placeholder='Description' required><button>Submit</button></form></body></html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("add".equals(action)) {
            addCondition(req, resp);
        } else {
            resp.sendRedirect("quick-condition");
        }
    }

    private void addCondition(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            AdditionalCondition condition = new AdditionalCondition();
            condition.setConditionType(req.getParameter("conditionType"));
            condition.setDescription(req.getParameter("description"));
            condition.setStatus(req.getParameter("status"));
            condition.setPriority(req.getParameter("priority"));

            // Обработка даты
            String deadlineStr = req.getParameter("deadline");
            if (deadlineStr != null && !deadlineStr.isEmpty()) {
                LocalDate deadline = LocalDate.parse(deadlineStr);
                condition.setDeadline(deadline);
            }

            AdditionalCondition createdCondition = conditionService.create(condition);
            System.out.println("Condition created with ID: " + createdCondition.getId());

            resp.setContentType("text/html");
            resp.getWriter().write("<script>"
                    + "if (window.opener) {"
                    + "  window.opener.postMessage({type:'entityCreated',entity:'condition'},'*');"
                    + "}"
                    + "window.close();"
                    + "</script>");

        } catch (Exception e) {
            System.err.println("Error creating condition: " + e.getMessage());
            e.printStackTrace();
            resp.setContentType("text/html");
            resp.getWriter().write("<script>alert('Error: " + e.getMessage() + "'); history.back();</script>");
        }
    }
}