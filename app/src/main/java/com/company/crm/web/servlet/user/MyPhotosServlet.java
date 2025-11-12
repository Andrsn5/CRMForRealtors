package com.company.crm.web.servlet.user;

import com.company.crm.config.AppConfig;
import com.company.crm.model.Photo;
import com.company.crm.service.interfaces.PhotoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/object-photos")
public class MyPhotosServlet extends HttpServlet {
    private PhotoService photoService;

    @Override
    public void init() {
        AppConfig config = (AppConfig) getServletContext().getAttribute("appConfig");
        photoService = config.getPhotoService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String objectIdStr = req.getParameter("objectId");
        if (objectIdStr != null && !objectIdStr.isEmpty()) {
            try {
                int objectId = Integer.parseInt(objectIdStr);
                List<Photo> photos = photoService.getAll().stream()
                        .filter(photo -> objectId == photo.getObjectId())
                        .toList();

                req.setAttribute("photos", photos);
                req.getRequestDispatcher("/WEB-INF/jsp/objectPhotos.jsp").forward(req, resp);
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid object ID");
            }
        }
    }
}