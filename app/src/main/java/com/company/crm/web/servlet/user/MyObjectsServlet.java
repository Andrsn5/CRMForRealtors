package com.company.crm.web.servlet.user;

import com.company.crm.config.AppConfig;
import com.company.crm.model.Object;
import com.company.crm.model.Photo;
import com.company.crm.service.interfaces.ObjectService;
import com.company.crm.service.interfaces.PhotoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/quick-object")
@MultipartConfig(
        maxFileSize = 1024 * 1024 * 10, // 10MB max file size
        maxRequestSize = 1024 * 1024 * 50 // 50MB max request size
)
public class MyObjectsServlet extends HttpServlet {
    private ObjectService objectService;
    private PhotoService photoService;

    @Override
    public void init() {
        AppConfig config = (AppConfig) getServletContext().getAttribute("appConfig");
        objectService = config.getObjectService();
        photoService = config.getPhotoService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        System.out.println("QuickObjectServlet doGet called with action: " + action);

        if ("add".equals(action)) {
            showAddObjectForm(req, resp);
        } else if ("list".equals(action)) {
            // Для AJAX обновления списка
            resp.setContentType("application/json");
            resp.getWriter().write("{\"status\":\"success\"}");
        } else {
            // По умолчанию показываем форму добавления
            showAddObjectForm(req, resp);
        }
    }

    private void showAddObjectForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        System.out.println("Showing quick add object form");
        try {
            req.getRequestDispatcher("/WEB-INF/jsp/addObject.jsp").forward(req, resp);
        } catch (Exception e) {
            System.err.println("Error forwarding to JSP: " + e.getMessage());
            e.printStackTrace();
            // Fallback - show simple form
            resp.getWriter().write("<html><body><h1>Quick Add Object</h1><form method='post' action='quick-object?action=add' enctype='multipart/form-data'><input name='title' placeholder='Title' required><button>Submit</button></form></body></html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        System.out.println("QuickObjectServlet doPost called with action: " + action);

        if ("add".equals(action)) {
            addObject(req, resp);
        } else {
            resp.sendRedirect("quick-object");
        }
    }

    private void addObject(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            // Создаем объект недвижимости
            Object object = new Object();
            object.setTitle(req.getParameter("title"));
            object.setAddress(req.getParameter("address"));
            object.setObjectType(req.getParameter("objectType"));
            object.setDealType(req.getParameter("dealType"));
            object.setStatus(req.getParameter("status"));

            // Обработка числовых полей
            String priceStr = req.getParameter("price");
            if (priceStr != null && !priceStr.isEmpty()) {
                object.setPrice(new BigDecimal(priceStr));
            }

            String areaStr = req.getParameter("area");
            if (areaStr != null && !areaStr.isEmpty()) {
                object.setArea(new BigDecimal(areaStr));
            }

            String roomsStr = req.getParameter("rooms");
            if (roomsStr != null && !roomsStr.isEmpty()) {
                object.setRooms(Integer.parseInt(roomsStr));
            }

            String bathroomsStr = req.getParameter("bathrooms");
            if (bathroomsStr != null && !bathroomsStr.isEmpty()) {
                object.setBathrooms(Integer.parseInt(bathroomsStr));
            }

            // Сохраняем объект
            Object createdObject = objectService.create(object);
            System.out.println("Object created with ID: " + createdObject.getId());

            // Обрабатываем загруженные фотографии
            processUploadedPhotos(req, createdObject.getId());

            resp.setContentType("text/html");
            resp.getWriter().write("<script>"
                    + "if (window.opener) {"
                    + "  window.opener.postMessage({type:'entityCreated',entity:'object'},'*');"
                    + "}"
                    + "window.close();"
                    + "</script>");

        } catch (Exception e) {
            System.err.println("Error creating object: " + e.getMessage());
            e.printStackTrace();
            resp.setContentType("text/html");
            resp.getWriter().write("<script>alert('Error: " + e.getMessage() + "'); history.back();</script>");
        }
    }

    private void processUploadedPhotos(HttpServletRequest req, Integer objectId) {
        try {
            List<Part> fileParts = new ArrayList<>();

            // Собираем все части с файлами
            for (Part part : req.getParts()) {
                if (part.getName().startsWith("photos") && part.getSize() > 0) {
                    fileParts.add(part);
                }
            }

            // Обрабатываем каждое фото
            for (int i = 0; i < fileParts.size(); i++) {
                Part filePart = fileParts.get(i);
                if (filePart.getSize() > 0) {
                    // Генерируем URL для фото (в реальном приложении нужно сохранять файл)
                    String fileName = getFileName(filePart);
                    String photoUrl = "/uploads/objects/" + objectId + "/" + System.currentTimeMillis() + "_" + fileName;

                    // Получаем порядок отображения и подпись
                    String displayOrderStr = req.getParameter("photoOrder" + i);
                    String caption = req.getParameter("photoCaption" + i);

                    // Создаем запись о фото в базе данных
                    Photo photo = new Photo();
                    photo.setPhotoUrl(photoUrl);
                    photo.setObjectId(objectId);

                    // Устанавливаем порядок отображения
                    if (displayOrderStr != null && !displayOrderStr.isEmpty()) {
                        try {
                            photo.setDisplayOrder(Integer.parseInt(displayOrderStr));
                        } catch (NumberFormatException e) {
                            // Если порядок невалидный, используем порядок по умолчанию
                            photo.setDisplayOrder(i + 1);
                        }
                    } else {
                        photo.setDisplayOrder(i + 1);
                    }

                    // Устанавливаем подпись
                    if (caption != null && !caption.trim().isEmpty()) {
                        photo.setCaption(caption.trim());
                    }

                    photoService.create(photo);
                    System.out.println("Photo created for object " + objectId +
                            ": " + photoUrl + ", order: " + photo.getDisplayOrder() +
                            ", caption: " + (photo.getCaption() != null ? photo.getCaption() : "null"));
                }
            }

        } catch (Exception e) {
            System.err.println("Error processing photos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        String[] tokens = contentDisposition.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length() - 1);
            }
        }
        return "unknown.jpg";
    }
}