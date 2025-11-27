package com.company.crm.web.servlet.admin;

import com.company.crm.config.AppConfig;
import com.company.crm.model.Employee;
import com.company.crm.service.interfaces.EmployeeService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class EmployeeServlet extends HttpServlet {
    private EmployeeService employeeService;

    @Override
    public void init() {
        AppConfig config = (AppConfig) getServletContext().getAttribute("appConfig");
        employeeService = config.getEmployeeService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        if (action == null || action.equals("list")) {
            req.setAttribute("employees", employeeService.getAll());
            req.getRequestDispatcher("/WEB-INF/jsp/employees/list.jsp").forward(req, resp);
        } else if (action.equals("edit")) {
            int id = Integer.parseInt(req.getParameter("id"));
            req.setAttribute("employee", employeeService.getById(id).orElse(null));
            req.getRequestDispatcher("/WEB-INF/jsp/employees/form.jsp").forward(req, resp);
        } else if (action.equals("new")) {
            req.getRequestDispatcher("/WEB-INF/jsp/employees/form.jsp").forward(req, resp);
        } else if (action.equals("delete")) {
            int id = Integer.parseInt(req.getParameter("id"));
            employeeService.delete(id);
            resp.sendRedirect("employee?action=list");
        }
        else if (action.equals("view")) {
            int id = Integer.parseInt(req.getParameter("id"));
            req.setAttribute("employee", employeeService.getById(id).orElse(null));
            req.getRequestDispatcher("/WEB-INF/jsp/employees/view.jsp").forward(req, resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Установка кодировки UTF-8 для корректной обработки русских символов
        req.setCharacterEncoding("UTF-8");

        Employee emp = new Employee();
        emp.setFirstName(req.getParameter("firstName"));
        emp.setLastName(req.getParameter("lastName"));
        emp.setEmail(req.getParameter("email"));
        emp.setPhone(req.getParameter("phone"));
        emp.setPosition(req.getParameter("position"));

        String id = req.getParameter("id");
        if (id == null || id.isBlank()) {
            employeeService.create(emp);
        } else {
            emp.setId(Integer.parseInt(id));
            employeeService.update(emp);
        }
        resp.sendRedirect("employee?action=list");
    }
}