package com.company.crm.web.servlet;

import com.company.crm.config.AppConfig;
import com.company.crm.model.AdditionalCondition;
import com.company.crm.service.interfaces.AdditionalConditionService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;

public class AdditionalConditionServlet extends HttpServlet {
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
        if (action == null || action.equals("list")) {
            req.setAttribute("conditions", conditionService.getAll());
            req.getRequestDispatcher("/WEB-INF/jsp/additionalConditions/list.jsp").forward(req, resp);
        } else if (action.equals("edit")) {
            int id = Integer.parseInt(req.getParameter("id"));
            req.setAttribute("condition", conditionService.getById(id).orElse(null));
            req.getRequestDispatcher("/WEB-INF/jsp/additionalConditions/form.jsp").forward(req, resp);
        } else if (action.equals("new")) {
            req.getRequestDispatcher("/WEB-INF/jsp/additionalConditions/form.jsp").forward(req, resp);
        } else if (action.equals("delete")) {
            int id = Integer.parseInt(req.getParameter("id"));
            conditionService.delete(id);
            resp.sendRedirect("additionalCondition?action=list");
        } else if (action.equals("view")) {
            int id = Integer.parseInt(req.getParameter("id"));
            req.setAttribute("condition", conditionService.getById(id).orElse(null));
            req.getRequestDispatcher("/WEB-INF/jsp/additionalConditions/view.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        AdditionalCondition condition = new AdditionalCondition();
        condition.setConditionType(req.getParameter("conditionType"));
        condition.setDescription(req.getParameter("description"));
        condition.setNotes(req.getParameter("notes"));
        condition.setStatus(req.getParameter("status"));
        condition.setPriority(req.getParameter("priority"));

        String deadline = req.getParameter("deadline");
        if (deadline != null && !deadline.isBlank()) {
            condition.setDeadline(LocalDate.parse(deadline));
        }

        String required = req.getParameter("required");
        condition.setRequired(required != null && required.equals("on"));

        String id = req.getParameter("id");
        if (id == null || id.isBlank()) {
            conditionService.create(condition);
        } else {
            condition.setId(Integer.parseInt(id));
            conditionService.update(condition);
        }
        resp.sendRedirect("additionalCondition?action=list");
    }
}