package com.company.crm.web.servlet.admin;

import com.company.crm.config.AppConfig;
import com.company.crm.model.Deal;
import com.company.crm.service.interfaces.DealService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

public class DealServlet extends HttpServlet {
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
        if (action == null || action.equals("list")) {
            req.setAttribute("deals", dealService.getAll());
            req.getRequestDispatcher("/WEB-INF/jsp/deals/list.jsp").forward(req, resp);
        } else if (action.equals("edit")) {
            int id = Integer.parseInt(req.getParameter("id"));
            req.setAttribute("deal", dealService.getById(id).orElse(null));
            req.getRequestDispatcher("/WEB-INF/jsp/deals/form.jsp").forward(req, resp);
        } else if (action.equals("new")) {
            req.getRequestDispatcher("/WEB-INF/jsp/deals/form.jsp").forward(req, resp);
        } else if (action.equals("delete")) {
            int id = Integer.parseInt(req.getParameter("id"));
            dealService.delete(id);
            resp.sendRedirect("deal?action=list");
        } else if (action.equals("view")) {
            int id = Integer.parseInt(req.getParameter("id"));
            req.setAttribute("deal", dealService.getById(id).orElse(null));
            req.getRequestDispatcher("/WEB-INF/jsp/deals/view.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Deal deal = new Deal();
        deal.setDealNumber(req.getParameter("dealNumber"));
        deal.setStatus(req.getParameter("status"));

        String taskId = req.getParameter("taskId");
        if (taskId != null && !taskId.isBlank()) {
            deal.setTaskId(Integer.parseInt(taskId));
        }

        String dealAmount = req.getParameter("dealAmount");
        if (dealAmount != null && !dealAmount.isBlank()) {
            deal.setDealAmount(new BigDecimal(dealAmount));
        }

        String dealDate = req.getParameter("dealDate");
        if (dealDate != null && !dealDate.isBlank()) {
            deal.setDealDate(LocalDate.parse(dealDate));
        }

        String commission = req.getParameter("commission");
        if (commission != null && !commission.isBlank()) {
            deal.setCommission(new BigDecimal(commission));
        }

        String id = req.getParameter("id");
        if (id == null || id.isBlank()) {
            dealService.create(deal);
        } else {
            deal.setId(Integer.parseInt(id));
            dealService.update(deal);
        }
        resp.sendRedirect("deal?action=list");
    }
}