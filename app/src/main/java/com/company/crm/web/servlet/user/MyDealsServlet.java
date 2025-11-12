package com.company.crm.web.servlet.user;

import com.company.crm.config.AppConfig;
import com.company.crm.model.Deal;
import com.company.crm.service.interfaces.DealService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

@WebServlet("/quick-deal")
public class MyDealsServlet extends HttpServlet {
    private DealService dealService;

    @Override
    public void init() {
        AppConfig config = (AppConfig) getServletContext().getAttribute("appConfig");
        dealService = config.getDealService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        System.out.println("QuickDealServlet doGet called with action: " + action);

        if ("add".equals(action)) {
            showAddDealForm(req, resp);
        } else if ("list".equals(action)) {
            resp.setContentType("application/json");
            resp.getWriter().write("{\"status\":\"success\"}");
        } else {
            showAddDealForm(req, resp);
        }
    }

    private void showAddDealForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.getRequestDispatcher("/WEB-INF/jsp/addDeal.jsp").forward(req, resp);
        } catch (Exception e) {
            System.err.println("Error forwarding to JSP: " + e.getMessage());
            e.printStackTrace();
            resp.getWriter().write("<html><body><h1>Quick Add Deal</h1><form method='post' action='quick-deal?action=add'><input name='dealNumber' placeholder='Deal Number' required><button>Submit</button></form></body></html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("add".equals(action)) {
            addDeal(req, resp);
        } else {
            resp.sendRedirect("quick-deal");
        }
    }

    private void addDeal(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            Deal deal = new Deal();
            deal.setDealNumber(req.getParameter("dealNumber"));
            deal.setStatus(req.getParameter("status"));

            // Обработка числовых полей
            String dealAmountStr = req.getParameter("dealAmount");
            if (dealAmountStr != null && !dealAmountStr.isEmpty()) {
                deal.setDealAmount(new BigDecimal(dealAmountStr));
            }

            String commissionStr = req.getParameter("commission");
            if (commissionStr != null && !commissionStr.isEmpty()) {
                deal.setCommission(new BigDecimal(commissionStr));
            }

            // Обработка даты
            String dealDateStr = req.getParameter("dealDate");
            if (dealDateStr != null && !dealDateStr.isEmpty()) {
                LocalDate dealDate = LocalDate.parse(dealDateStr);
                deal.setDealDate(dealDate);
            } else {
                deal.setDealDate(LocalDate.now());
            }

            Deal createdDeal = dealService.create(deal);
            System.out.println("Deal created with ID: " + createdDeal.getId());

            resp.setContentType("text/html");
            resp.getWriter().write("<script>"
                    + "if (window.opener) {"
                    + "  window.opener.postMessage({type:'entityCreated',entity:'deal'},'*');"
                    + "}"
                    + "window.close();"
                    + "</script>");

        } catch (Exception e) {
            System.err.println("Error creating deal: " + e.getMessage());
            e.printStackTrace();
            resp.setContentType("text/html");
            resp.getWriter().write("<script>alert('Error: " + e.getMessage() + "'); history.back();</script>");
        }
    }
}