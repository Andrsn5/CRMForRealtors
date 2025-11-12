package com.company.crm.web.servlet.admin;

import com.company.crm.config.AppConfig;
import com.company.crm.model.Object;
import com.company.crm.service.interfaces.ObjectService;
import com.company.crm.util.ValidationException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;

public class ObjectServlet extends HttpServlet {
    private ObjectService objectService;

    @Override
    public void init() {
        AppConfig config = (AppConfig) getServletContext().getAttribute("appConfig");
        objectService = config.getObjectService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        if (action == null || action.equals("list")) {
            req.setAttribute("objects", objectService.getAll());
            req.getRequestDispatcher("/WEB-INF/jsp/objects/list.jsp").forward(req, resp);
        } else if (action.equals("edit")) {
            int id = Integer.parseInt(req.getParameter("id"));
            req.setAttribute("object", objectService.getById(id).orElse(null));
            req.getRequestDispatcher("/WEB-INF/jsp/objects/form.jsp").forward(req, resp);
        } else if (action.equals("new")) {
            req.getRequestDispatcher("/WEB-INF/jsp/objects/form.jsp").forward(req, resp);
        } else if (action.equals("delete")) {
            int id = Integer.parseInt(req.getParameter("id"));
            objectService.delete(id);
            resp.sendRedirect("object?action=list");
        } else if (action.equals("view")) {
            int id = Integer.parseInt(req.getParameter("id"));
            req.setAttribute("object", objectService.getById(id).orElse(null));
            req.getRequestDispatcher("/WEB-INF/jsp/objects/view.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Установка кодировки для корректной обработки русских символов
        req.setCharacterEncoding("UTF-8");

        try {
            Object obj = new Object();
            obj.setTitle(req.getParameter("title"));
            obj.setDescription(req.getParameter("description"));
            obj.setObjectType(req.getParameter("objectType"));
            obj.setDealType(req.getParameter("dealType"));
            obj.setAddress(req.getParameter("address"));
            obj.setStatus(req.getParameter("status"));

            String price = req.getParameter("price");
            if (price != null && !price.isBlank()) {
                obj.setPrice(new BigDecimal(price));
            }

            String area = req.getParameter("area");
            if (area != null && !area.isBlank()) {
                obj.setArea(new BigDecimal(area));
            }

            String rooms = req.getParameter("rooms");
            if (rooms != null && !rooms.isBlank()) {
                obj.setRooms(Integer.parseInt(rooms));
            }

            String bathrooms = req.getParameter("bathrooms");
            if (bathrooms != null && !bathrooms.isBlank()) {
                obj.setBathrooms(Integer.parseInt(bathrooms));
            }

            String id = req.getParameter("id");
            if (id == null || id.isBlank()) {
                objectService.create(obj);
            } else {
                obj.setId(Integer.parseInt(id));
                objectService.update(obj);
            }
            resp.sendRedirect("object?action=list");

        } catch (ValidationException e) {
            // Обработка ошибки валидации - возвращаем на форму с сообщением об ошибке
            req.setAttribute("error", e.getMessage());
            req.setAttribute("object", createObjectFromRequest(req));
            req.getRequestDispatcher("/WEB-INF/jsp/objects/form.jsp").forward(req, resp);
        } catch (Exception e) {
            // Обработка других ошибок
            req.setAttribute("error", "Произошла ошибка: " + e.getMessage());
            req.setAttribute("object", createObjectFromRequest(req));
            req.getRequestDispatcher("/WEB-INF/jsp/objects/form.jsp").forward(req, resp);
        }
    }

    private Object createObjectFromRequest(HttpServletRequest req) {
        Object obj = new Object();
        obj.setId(req.getParameter("id") != null && !req.getParameter("id").isBlank()
                ? Integer.parseInt(req.getParameter("id")) : 0);
        obj.setTitle(req.getParameter("title"));
        obj.setDescription(req.getParameter("description"));
        obj.setObjectType(req.getParameter("objectType"));
        obj.setDealType(req.getParameter("dealType"));
        obj.setAddress(req.getParameter("address"));
        obj.setStatus(req.getParameter("status"));

        String price = req.getParameter("price");
        if (price != null && !price.isBlank()) {
            obj.setPrice(new BigDecimal(price));
        }

        String area = req.getParameter("area");
        if (area != null && !area.isBlank()) {
            obj.setArea(new BigDecimal(area));
        }

        String rooms = req.getParameter("rooms");
        if (rooms != null && !rooms.isBlank()) {
            obj.setRooms(Integer.parseInt(rooms));
        }

        String bathrooms = req.getParameter("bathrooms");
        if (bathrooms != null && !bathrooms.isBlank()) {
            obj.setBathrooms(Integer.parseInt(bathrooms));
        }

        return obj;
    }
}