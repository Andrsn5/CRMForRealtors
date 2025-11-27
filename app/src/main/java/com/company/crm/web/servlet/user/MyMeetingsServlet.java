package com.company.crm.web.servlet.user;

import com.company.crm.config.AppConfig;
import com.company.crm.model.Meeting;
import com.company.crm.service.interfaces.MeetingService;
import com.company.crm.service.interfaces.ClientService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/quick-meeting")
public class MyMeetingsServlet extends HttpServlet {
    private MeetingService meetingService;
    private ClientService clientService;

    @Override
    public void init() {
        AppConfig config = (AppConfig) getServletContext().getAttribute("appConfig");
        meetingService = config.getMeetingService();
        clientService = config.getClientService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        System.out.println("QuickMeetingServlet doGet called with action: " + action);

        if ("add".equals(action)) {
            showAddMeetingForm(req, resp);
        } else if ("list".equals(action)) {
            resp.setContentType("application/json");
            resp.getWriter().write("{\"status\":\"success\"}");
        } else {
            showAddMeetingForm(req, resp);
        }
    }

    private void showAddMeetingForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            // Получаем список клиентов для выпадающего списка
            List<com.company.crm.model.Client> clients = clientService.getAll();
            req.setAttribute("clients", clients);

            req.getRequestDispatcher("/WEB-INF/jsp/addMeeting.jsp").forward(req, resp);
        } catch (Exception e) {
            System.err.println("Error forwarding to JSP: " + e.getMessage());
            e.printStackTrace();
            resp.getWriter().write("<html><body><h1>Quick Add Meeting</h1><form method='post' action='quick-meeting?action=add'><input name='title' placeholder='Title' required><button>Submit</button></form></body></html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("add".equals(action)) {
            addMeeting(req, resp);
        } else {
            resp.sendRedirect("quick-meeting");
        }
    }

    private void addMeeting(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            Meeting meeting = new Meeting();
            meeting.setTitle(req.getParameter("title"));
            meeting.setLocation(req.getParameter("location"));
            meeting.setStatus(req.getParameter("status"));

            // Обработка даты и времени
            String meetingDateStr = req.getParameter("meetingDate");
            if (meetingDateStr != null && !meetingDateStr.isEmpty()) {
                LocalDateTime meetingDate = LocalDateTime.parse(meetingDateStr.replace(" ", "T"));
                meeting.setMeetingDate(meetingDate);
            }

            // Обработка клиента
            String clientIdStr = req.getParameter("clientId");
            if (clientIdStr != null && !clientIdStr.isEmpty()) {
                meeting.setClientId(Integer.parseInt(clientIdStr));
            }

            Meeting createdMeeting = meetingService.create(meeting);
            System.out.println("Meeting created with ID: " + createdMeeting.getId());

            resp.setContentType("text/html");
            resp.getWriter().write("<script>"
                    + "if (window.opener) {"
                    + "  window.opener.postMessage({type:'entityCreated',entity:'meeting'},'*');"
                    + "}"
                    + "window.close();"
                    + "</script>");

        } catch (Exception e) {
            System.err.println("Error creating meeting: " + e.getMessage());
            e.printStackTrace();
            resp.setContentType("text/html");
            resp.getWriter().write("<script>alert('Error: " + e.getMessage() + "'); history.back();</script>");
        }
    }
}