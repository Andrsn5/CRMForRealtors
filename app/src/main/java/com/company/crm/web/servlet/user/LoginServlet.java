package com.company.crm.web.servlet.user;

import com.company.crm.config.AppConfig;
import com.company.crm.model.Employee;
import com.company.crm.service.interfaces.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private AuthService authService;

    @Override
    public void init() {
        AppConfig config = (AppConfig) getServletContext().getAttribute("appConfig");
        authService = config.getAuthService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        System.out.println("Login attempt - Email: " + email + ", Password: " + password);

        authService.login(email, password).ifPresentOrElse(employee -> {
            System.out.println("Login successful for: " + email);
            System.out.println("Employee position: " + employee.getPosition());

            HttpSession session = req.getSession();
            session.setAttribute("employee", employee);

            try {
                // Проверяем позицию сотрудника для определения куда перенаправлять
                if ("Senior Manager".equals(employee.getPosition())) {
                    System.out.println("Redirecting to admin dashboard");
                    resp.sendRedirect(req.getContextPath() + "/");
                } else {
                    System.out.println("Redirecting to employee dashboard");
                    resp.sendRedirect(req.getContextPath() + "/dashboard");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, () -> {
            System.out.println("Login failed for: " + email);
            req.setAttribute("error", "Неверный логин или пароль");
            try {
                req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
            } catch (IOException | ServletException e) {
                throw new RuntimeException(e);
            }
        });
    }
}