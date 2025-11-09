package com.company.crm.web.servlet;

import com.company.crm.config.AppConfig;
import com.company.crm.model.Meeting;
import com.company.crm.service.interfaces.MeetingService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;

public class MeetingServlet extends HttpServlet {
    private MeetingService meetingService;

    @Override
    public void init() {
        AppConfig config = (AppConfig) getServletContext().getAttribute("appConfig");
        meetingService = config.getMeetingService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        if (action == null || action.equals("list")) {
            req.setAttribute("meetings", meetingService.getAll());
            req.getRequestDispatcher("/WEB-INF/jsp/meetings/list.jsp").forward(req, resp);
        } else if (action.equals("edit")) {
            int id = Integer.parseInt(req.getParameter("id"));
            req.setAttribute("meeting", meetingService.getById(id).orElse(null));
            req.getRequestDispatcher("/WEB-INF/jsp/meetings/form.jsp").forward(req, resp);
        } else if (action.equals("new")) {
            req.getRequestDispatcher("/WEB-INF/jsp/meetings/form.jsp").forward(req, resp);
        } else if (action.equals("delete")) {
            int id = Integer.parseInt(req.getParameter("id"));
            meetingService.delete(id);
            resp.sendRedirect("meeting?action=list");
        } else if (action.equals("view")) {
            int id = Integer.parseInt(req.getParameter("id"));
            req.setAttribute("meeting", meetingService.getById(id).orElse(null));
            req.getRequestDispatcher("/WEB-INF/jsp/meetings/view.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Meeting meeting = new Meeting();
        meeting.setTitle(req.getParameter("title"));
        meeting.setLocation(req.getParameter("location"));
        meeting.setNotes(req.getParameter("notes"));
        meeting.setStatus(req.getParameter("status"));

        String meetingDate = req.getParameter("meetingDate");
        if (meetingDate != null && !meetingDate.isBlank()) {
            meeting.setMeetingDate(LocalDateTime.parse(meetingDate.replace("T", " ") + ":00"));
        }

        String clientId = req.getParameter("clientId");
        if (clientId != null && !clientId.isBlank()) {
            meeting.setClientId(Integer.parseInt(clientId));
        }

        String taskId = req.getParameter("taskId");
        if (taskId != null && !taskId.isBlank()) {
            meeting.setTaskId(Integer.parseInt(taskId));
        }

        String id = req.getParameter("id");
        if (id == null || id.isBlank()) {
            meetingService.create(meeting);
        } else {
            meeting.setId(Integer.parseInt(id));
            meetingService.update(meeting);
        }
        resp.sendRedirect("meeting?action=list");
    }
}