package com.company.crm.web.servlet.admin;

import com.company.crm.config.AppConfig;
import com.company.crm.model.Meeting;
import com.company.crm.service.interfaces.MeetingService;
import com.company.crm.util.ValidationException;
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
        // Установка кодировки UTF-8 для корректной обработки русских символов
        req.setCharacterEncoding("UTF-8");

        try {
            Meeting meeting = new Meeting();
            meeting.setTitle(req.getParameter("title"));
            meeting.setLocation(req.getParameter("location"));
            meeting.setNotes(req.getParameter("notes"));
            meeting.setStatus(req.getParameter("status"));

            // Обработка даты и времени с использованием метода парсинга из сервиса
            String meetingDate = req.getParameter("meetingDate");
            if (meetingDate != null && !meetingDate.isBlank()) {
                LocalDateTime dateTime = ((com.company.crm.service.implement.MeetingServiceImpl) meetingService)
                        .parseDateTime(meetingDate);
                meeting.setMeetingDate(dateTime);
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
        } catch (ValidationException e) {
            // Обработка ошибок валидации
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/jsp/meetings/form.jsp").forward(req, resp);
        } catch (Exception e) {
            // Обработка других ошибок
            req.setAttribute("error", "Произошла ошибка: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/jsp/meetings/form.jsp").forward(req, resp);
        }
    }
}