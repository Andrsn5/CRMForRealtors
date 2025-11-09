package com.company.crm.web.servlet;

import com.company.crm.config.AppConfig;
import com.company.crm.model.Photo;
import com.company.crm.service.interfaces.PhotoService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class PhotoServlet extends HttpServlet {
    private PhotoService photoService;

    @Override
    public void init() {
        AppConfig config = (AppConfig) getServletContext().getAttribute("appConfig");
        photoService = config.getPhotoService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        if (action == null || action.equals("list")) {
            req.setAttribute("photos", photoService.getAll());
            req.getRequestDispatcher("/WEB-INF/jsp/photos/list.jsp").forward(req, resp);
        } else if (action.equals("edit")) {
            int id = Integer.parseInt(req.getParameter("id"));
            req.setAttribute("photo", photoService.getById(id).orElse(null));
            req.getRequestDispatcher("/WEB-INF/jsp/photos/form.jsp").forward(req, resp);
        } else if (action.equals("new")) {
            req.getRequestDispatcher("/WEB-INF/jsp/photos/form.jsp").forward(req, resp);
        } else if (action.equals("delete")) {
            int id = Integer.parseInt(req.getParameter("id"));
            photoService.delete(id);
            resp.sendRedirect("photo?action=list");
        } else if (action.equals("view")) {
            int id = Integer.parseInt(req.getParameter("id"));
            req.setAttribute("photo", photoService.getById(id).orElse(null));
            req.getRequestDispatcher("/WEB-INF/jsp/photos/view.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Photo photo = new Photo();
        photo.setPhotoUrl(req.getParameter("photoUrl"));
        photo.setCaption(req.getParameter("caption"));

        String displayOrder = req.getParameter("displayOrder");
        if (displayOrder != null && !displayOrder.isBlank()) {
            photo.setDisplayOrder(Integer.parseInt(displayOrder));
        }

        String objectId = req.getParameter("objectId");
        if (objectId != null && !objectId.isBlank()) {
            photo.setObjectId(Integer.parseInt(objectId));
        }

        String id = req.getParameter("id");
        if (id == null || id.isBlank()) {
            photoService.create(photo);
        } else {
            photo.setId(Integer.parseInt(id));
            photoService.update(photo);
        }
        resp.sendRedirect("photo?action=list");
    }
}