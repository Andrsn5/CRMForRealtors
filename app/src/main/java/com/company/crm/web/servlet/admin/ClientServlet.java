package com.company.crm.web.servlet.admin;

import com.company.crm.config.AppConfig;
import com.company.crm.model.Client;
import com.company.crm.service.interfaces.ClientService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;

public class ClientServlet extends HttpServlet {
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
        if (action == null || action.equals("list")) {
            req.setAttribute("clients", clientService.getAll());
            req.getRequestDispatcher("/WEB-INF/jsp/clients/list.jsp").forward(req, resp);
        } else if (action.equals("edit")) {
            int id = Integer.parseInt(req.getParameter("id"));
            req.setAttribute("client", clientService.getById(id).orElse(null));
            req.getRequestDispatcher("/WEB-INF/jsp/clients/form.jsp").forward(req, resp);
        } else if (action.equals("new")) {
            req.getRequestDispatcher("/WEB-INF/jsp/clients/form.jsp").forward(req, resp);
        } else if (action.equals("delete")) {
            int id = Integer.parseInt(req.getParameter("id"));
            clientService.delete(id);
            resp.sendRedirect("client?action=list");
        } else if (action.equals("view")) {
            int id = Integer.parseInt(req.getParameter("id"));
            req.setAttribute("client", clientService.getById(id).orElse(null));
            req.getRequestDispatcher("/WEB-INF/jsp/clients/view.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Client client = new Client();
        client.setFirstName(req.getParameter("firstName"));
        client.setLastName(req.getParameter("lastName"));
        client.setEmail(req.getParameter("email"));
        client.setPhone(req.getParameter("phone"));
        client.setClientType(req.getParameter("clientType"));
        client.setNotes(req.getParameter("notes"));

        String budget = req.getParameter("budget");
        if (budget != null && !budget.isBlank()) {
            client.setBudget(new BigDecimal(budget));
        }

        String id = req.getParameter("id");
        if (id == null || id.isBlank()) {
            clientService.create(client);
        } else {
            client.setId(Integer.parseInt(id));
            clientService.update(client);
        }
        resp.sendRedirect("client?action=list");
    }
}