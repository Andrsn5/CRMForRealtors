package com.company.crm.web.servlet.admin;

import com.company.crm.model.Employee;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/")
public class AdminDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("employee") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Employee employee = (Employee) session.getAttribute("employee");

        // Проверяем, имеет ли сотрудник права администратора
        if (!"Senior Manager".equals(employee.getPosition())) {
            // Если нет прав - перенаправляем на дашборд сотрудника
            resp.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }

        // Показываем админ-панель
        req.getRequestDispatcher("/WEB-INF/index.jsp").forward(req, resp);
    }
}