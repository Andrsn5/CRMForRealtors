package com.company.crm.web.servlet.admin;

import com.company.crm.config.AppConfig;
import com.company.crm.model.AdditionalCondition;
import com.company.crm.service.interfaces.AdditionalConditionService;
import com.company.crm.util.ValidationException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;

public class AdditionalConditionServlet extends HttpServlet {
    private AdditionalConditionService additionalConditionService;

    @Override
    public void init() {
        AppConfig config = (AppConfig) getServletContext().getAttribute("appConfig");
        additionalConditionService = config.getAdditionalConditionService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        if (action == null || action.equals("list")) {
            req.setAttribute("conditions", additionalConditionService.getAll());
            req.getRequestDispatcher("/WEB-INF/jsp/additionalConditions/list.jsp").forward(req, resp);
        } else if (action.equals("edit")) {
            int id = Integer.parseInt(req.getParameter("id"));
            req.setAttribute("condition", additionalConditionService.getById(id).orElse(null));
            req.getRequestDispatcher("/WEB-INF/jsp/additionalConditions/form.jsp").forward(req, resp);
        } else if (action.equals("new")) {
            req.getRequestDispatcher("/WEB-INF/jsp/additionalConditions/form.jsp").forward(req, resp);
        } else if (action.equals("delete")) {
            int id = Integer.parseInt(req.getParameter("id"));
            additionalConditionService.delete(id);
            resp.sendRedirect("additionalCondition?action=list");
        } else if (action.equals("view")) {
            int id = Integer.parseInt(req.getParameter("id"));
            req.setAttribute("condition", additionalConditionService.getById(id).orElse(null));
            req.getRequestDispatcher("/WEB-INF/jsp/additionalConditions/view.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Установка кодировки UTF-8 для корректной обработки русских символов
        req.setCharacterEncoding("UTF-8");

        try {
            AdditionalCondition condition = new AdditionalCondition();
            condition.setConditionType(req.getParameter("conditionType"));
            condition.setDescription(req.getParameter("description"));
            condition.setStatus(req.getParameter("status"));
            condition.setPriority(req.getParameter("priority"));

            // Обработка даты
            String deadlineStr = req.getParameter("deadline");
            if (deadlineStr != null && !deadlineStr.trim().isEmpty()) {
                // Используем метод парсинга из сервиса
                LocalDate deadline = ((com.company.crm.service.implement.AdditionalConditionServiceImpl) additionalConditionService)
                        .parseDate(deadlineStr);
                condition.setDeadline(deadline);
            }

            String id = req.getParameter("id");
            if (id == null || id.isBlank()) {
                additionalConditionService.create(condition);
            } else {
                condition.setId(Integer.parseInt(id));
                additionalConditionService.update(condition);
            }
            resp.sendRedirect("additionalCondition?action=list");
        } catch (ValidationException e) {
            // Обработка ошибок валидации
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/jsp/additionalConditions/form.jsp").forward(req, resp);
        } catch (Exception e) {
            // Обработка других ошибок
            req.setAttribute("error", "Произошла ошибка: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/jsp/additionalConditions/form.jsp").forward(req, resp);
        }
    }
}