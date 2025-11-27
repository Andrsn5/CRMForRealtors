package com.company.crm.web.servlet.user;

import com.company.crm.config.AppConfig;
import com.company.crm.model.Client;
import com.company.crm.service.interfaces.ClientService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/quick-client")  // Измените URL на другой
public class MyClientsServlet extends HttpServlet {
    private ClientService clientService;

    @Override
    public void init() {
        AppConfig config = (AppConfig) getServletContext().getAttribute("appConfig");
        clientService = config.getClientService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        System.out.println("QuickClientServlet doGet called with action: " + action);

        if ("add".equals(action)) {
            showAddClientForm(req, resp);
        } else if ("list".equals(action)) {
            // Для AJAX обновления списка
            resp.setContentType("application/json");
            resp.getWriter().write("{\"status\":\"success\"}");
        } else {
            // По умолчанию показываем форму добавления
            showAddClientForm(req, resp);
        }
    }

    private void showAddClientForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        System.out.println("Showing quick add client form");
        try {
            req.getRequestDispatcher("/WEB-INF/jsp/addClient.jsp").forward(req, resp);
        } catch (Exception e) {
            System.err.println("Error forwarding to JSP: " + e.getMessage());
            e.printStackTrace();
            // Fallback - show simple form
            resp.getWriter().write("<html><body><h1>Quick Add Client</h1><form method='post' action='quick-client?action=add'><input name='firstName' placeholder='First Name' required><input name='lastName' placeholder='Last Name' required><button>Submit</button></form></body></html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        System.out.println("QuickClientServlet doPost called with action: " + action);

        if ("add".equals(action)) {
            addClient(req, resp);
        } else {
            resp.sendRedirect("quick-client");
        }
    }

    private void addClient(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            Client client = new Client();
            client.setFirstName(req.getParameter("firstName"));
            client.setLastName(req.getParameter("lastName"));
            client.setEmail(req.getParameter("email"));
            client.setPhone(req.getParameter("phone"));
            client.setClientType(req.getParameter("clientType"));

            String budgetStr = req.getParameter("budget");
            if (budgetStr != null && !budgetStr.isEmpty()) {
                client.setBudget(new BigDecimal(budgetStr));
            }

            Client createdClient = clientService.create(client);
            System.out.println("Client created with ID: " + createdClient.getId());

            // Для всплывающих окон
            resp.setContentType("text/html");
            resp.getWriter().write("<script>"
                    + "if (window.opener) {"
                    + "  window.opener.postMessage({type:'entityCreated',entity:'client'},'*');"
                    + "}"
                    + "window.close();"
                    + "</script>");

        } catch (Exception e) {
            System.err.println("Error creating client: " + e.getMessage());
            e.printStackTrace();
            resp.setContentType("text/html");
            resp.getWriter().write("<script>alert('Error: " + e.getMessage() + "'); history.back();</script>");
        }
    }
}