package com.company.crm.web.servlet.user;

import com.company.crm.config.AppConfig;
import com.company.crm.model.*;
import com.company.crm.service.interfaces.*;
import com.company.crm.util.ValidationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet("/task")
public class MyTaskServlet extends HttpServlet {
    private TaskService taskService;
    private ClientService clientService;
    private ObjectService objectService;
    private MeetingService meetingService;
    private DealService dealService;
    private EmployeeService employeeService;
    private AdditionalConditionService conditionService;
    private PhotoService photoService;

    @Override
    public void init() {
        AppConfig config = (AppConfig) getServletContext().getAttribute("appConfig");
        taskService = config.getTaskService();
        clientService = config.getClientService();
        objectService = config.getObjectService();
        meetingService = config.getMeetingService();
        dealService = config.getDealService();
        employeeService = config.getEmployeeService();
        conditionService = config.getAdditionalConditionService();
        photoService = config.getPhotoService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        if ("view".equals(action)) {
            viewTask(req, resp);
        } else if ("completed".equals(action)) {
            showCompletedTasks(req, resp);
        } else if ("add".equals(action)) {
            showAddTaskForm(req, resp);
        } else if ("edit".equals(action)) {
            editTask(req, resp);
        } else {
            String taskId = req.getParameter("id");
            if (taskId != null && !taskId.isEmpty()) {
                viewTask(req, resp);
            } else {
                resp.sendRedirect("dashboard");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("add".equals(action)) {
            addTask(req, resp);
        } else if ("complete".equals(action)) { // ДОБАВЬТЕ ЭТУ СТРОЧКУ
            completeTask(req, resp);
        } else  if ("edit".equals(action)) {
            updateTask(req, resp);
        } else if ("attach".equals(action)) {
            attachEntity(req, resp);
        }else {
            resp.sendRedirect("dashboard");
        }
    }

    private void attachEntity(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int taskId = Integer.parseInt(req.getParameter("id"));
            String entityType = req.getParameter("entityType");
            String entityIdStr = req.getParameter("entityId");

            Optional<Task> taskOpt = taskService.getById(taskId);

            if (taskOpt.isPresent()) {
                Task task = taskOpt.get();

                // Проверяем права на редактирование
                Employee currentUser = (Employee) req.getSession().getAttribute("employee");
                if (currentUser == null ||
                        (!task.getCreatorId().equals(currentUser.getId()) &&
                                !task.getResponsibleId().equals(currentUser.getId()))) {
                    req.setAttribute("error", "У вас нет прав для редактирования этой задачи");
                    viewTask(req, resp);
                    return;
                }

                if (entityIdStr != null && !entityIdStr.isEmpty()) {
                    try {
                        int entityId = Integer.parseInt(entityIdStr);

                        // Привязываем сущность в зависимости от типа
                        switch (entityType) {
                            case "client":
                                // Проверяем существование клиента
                                clientService.getById(entityId)
                                        .orElseThrow(() -> new ValidationException("Клиент не найден"));
                                task.setClientId(entityId);
                                break;

                            case "object":
                                // Проверяем существование объекта
                                objectService.getById(entityId)
                                        .orElseThrow(() -> new ValidationException("Объект не найден"));
                                task.setObjectId(entityId);
                                break;

                            case "meeting":
                                // Проверяем существование встречи
                                meetingService.getById(entityId)
                                        .orElseThrow(() -> new ValidationException("Встреча не найдена"));
                                task.setMeetingId(entityId);
                                break;

                            case "deal":
                                // Проверяем существование сделки
                                dealService.getById(entityId)
                                        .orElseThrow(() -> new ValidationException("Сделка не найдена"));
                                task.setDealId(entityId);
                                break;

                            case "condition":
                                // Проверяем существование условия
                                conditionService.getById(entityId)
                                        .orElseThrow(() -> new ValidationException("Условие не найдено"));
                                task.setConditionId(entityId);
                                break;

                            default:
                                throw new ValidationException("Неизвестный тип сущности: " + entityType);
                        }

                        // Сохраняем изменения
                        taskService.update(task);

                        resp.sendRedirect("task?action=view&id=" + taskId + "&success=attached");
                        return;

                    } catch (NumberFormatException e) {
                        req.setAttribute("error", "Неверный ID сущности");
                    } catch (ValidationException e) {
                        req.setAttribute("error", e.getMessage());
                    }
                } else {
                    req.setAttribute("error", "ID сущности не указан");
                }
            } else {
                req.setAttribute("error", "Задача не найдена");
            }

            // Если произошла ошибка, показываем страницу задачи с ошибкой
            viewTask(req, resp);

        } catch (NumberFormatException e) {
            req.setAttribute("error", "Неверный ID задачи");
            req.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(req, resp);
        }
    }

    private void completeTask(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int taskId = Integer.parseInt(req.getParameter("id"));

            Optional<Task> taskOpt = taskService.getById(taskId);

            if (taskOpt.isPresent()) {
                Task task = taskOpt.get();


                Employee currentUser = (Employee) req.getSession().getAttribute("employee");

                if (currentUser != null &&
                        (task.getResponsibleId() == null ||
                                task.getResponsibleId().equals(currentUser.getId()) ||
                                (task.getCreatorId() != null && task.getCreatorId().equals(currentUser.getId())))) {


                    // Обновляем только статус, сохраняем остальные поля без изменений
                    task.setStatus("Completed");

                    // Убедимся, что optional поля не стали 0 (должны остаться null или оригинальными значениями)
                    if (task.getConditionId() != null && task.getConditionId() == 0) {
                        task.setConditionId(null);
                    }
                    if (task.getDealId() != null && task.getDealId() == 0) {
                        task.setDealId(null);
                    }
                    if (task.getMeetingId() != null && task.getMeetingId() == 0) {
                        task.setMeetingId(null);
                    }

                    taskService.update(task);

                    // Перенаправляем на страницу задачи с сообщением об успехе
                    resp.sendRedirect("task?action=view&id=" + taskId + "&success=completed");
                    return;
                } else {
                    req.setAttribute("error", "У вас нет прав для выполнения этой задачи");
                }
            } else {
                req.setAttribute("error", "Задача не найдена");
            }

            // Если дошли сюда, значит была ошибка - показываем страницу задачи
            viewTask(req, resp);

        } catch (NumberFormatException e) {
            req.setAttribute("error", "Неверный ID задачи");
            req.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Ошибка при выполнении задачи: " + e.getMessage());
            viewTask(req, resp);
        }
    }
    private void showAddTaskForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Employee currentEmployee = (Employee) req.getSession().getAttribute("employee");
        if (currentEmployee != null) {
            // Получаем данные для выпадающих списков
            List<Client> clients = clientService.getAll();
            List<com.company.crm.model.Object> objects = objectService.getAll();
            List<Meeting> meetings = meetingService.getAll();
            List<Deal> deals = dealService.getAll();
            List<Employee> employees = employeeService.getAll();
            List<AdditionalCondition> conditions = conditionService.getAll();

            req.setAttribute("clients", clients);
            req.setAttribute("objects", objects);
            req.setAttribute("meetings", meetings);
            req.setAttribute("deals", deals);
            req.setAttribute("employees", employees);
            req.setAttribute("conditions", conditions);
            req.setAttribute("currentEmployee", currentEmployee);

            req.getRequestDispatcher("/WEB-INF/jsp/addTask.jsp").forward(req, resp);
        } else {
            resp.sendRedirect("login");
        }
    }

    private void addTask(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            Employee currentUser = (Employee) req.getSession().getAttribute("employee");

            Task task = new Task();

            // Обязательные поля
            task.setTitle(req.getParameter("title"));
            task.setDescription(req.getParameter("description"));
            task.setPriority(req.getParameter("priority"));
            task.setStatus(req.getParameter("status"));

            // Обработка даты
            String dueDateStr = req.getParameter("dueDate");
            if (dueDateStr != null && !dueDateStr.isEmpty()) {
                LocalDateTime dueDate = LocalDateTime.parse(dueDateStr.replace(" ", "T"));
                task.setDueDate(dueDate);
            }

            // Ответственный сотрудник
            String responsibleIdStr = req.getParameter("responsibleId");
            if (responsibleIdStr != null && !responsibleIdStr.isEmpty()) {
                task.setResponsibleId(Integer.parseInt(responsibleIdStr));
            } else {
                // Если не выбран, используем текущего пользователя
                task.setResponsibleId(currentUser.getId());
            }

            // Создатель задачи (всегда текущий пользователь)
            task.setCreatorId(currentUser.getId());

            // Опциональные связанные сущности - теперь поддерживают null
            task.setClientId(parseOptionalInteger(req.getParameter("clientId")));
            task.setObjectId(parseOptionalInteger(req.getParameter("objectId")));
            task.setMeetingId(parseOptionalInteger(req.getParameter("meetingId")));
            task.setDealId(parseOptionalInteger(req.getParameter("dealId")));
            task.setConditionId(parseOptionalInteger(req.getParameter("conditionId")));

            // Сохраняем задачу (валидация произойдет в service)
            Task createdTask = taskService.create(task);

            // Перенаправляем на страницу созданной задачи
            resp.sendRedirect("task?action=view&id=" + createdTask.getId());

        } catch (Exception e) {
            // В случае ошибки показываем форму снова с сообщением об ошибке
            req.setAttribute("error", "Ошибка при создании задачи: " + e.getMessage());

            // Сохраняем введенные данные для повторного заполнения формы
            req.setAttribute("formData", req.getParameterMap());
            showAddTaskForm(req, resp);
        }
    }

    // Вспомогательный метод для парсинга опциональных Integer (поддерживает null)
    private Integer parseOptionalInteger(String value) {
        if (value == null || value.isEmpty() || value.equals("")) {
            return null;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void viewTask(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int taskId = Integer.parseInt(req.getParameter("id"));
            Optional<Task> taskOpt = taskService.getById(taskId);

            if (taskOpt.isPresent()) {
                Task task = taskOpt.get();

                // Получаем связанные сущности
                Optional<Client> client = task.getClientId() != null ?
                        clientService.getById(task.getClientId()) : Optional.empty();

                Optional<com.company.crm.model.Object> object = task.getObjectId() != null ?
                        objectService.getById(task.getObjectId()) : Optional.empty();

                Optional<Meeting> meeting = task.getMeetingId() != null ?
                        meetingService.safeGetById(task.getMeetingId()) : Optional.empty();

                Optional<Deal> deal = task.getDealId() != null ?
                        dealService.safeGetById(task.getDealId()) : Optional.empty();

                Optional<Employee> responsible = task.getResponsibleId() != null ?
                        employeeService.getById(task.getResponsibleId()) : Optional.empty();

                Optional<Employee> creator = task.getCreatorId() != null ?
                        employeeService.getById(task.getCreatorId()) : Optional.empty();

                Optional<AdditionalCondition> condition = task.getConditionId() != null ?
                        conditionService.safeGetById(task.getConditionId()) : Optional.empty();

                // Получаем фотографии объекта, если объект привязан
                List<Photo> objectPhotos = new ArrayList<>();
                if (object.isPresent()) {
                    // Предполагая, что у вас есть PhotoService для получения фотографий
                    objectPhotos = photoService.getAll().stream()
                            .filter(photo -> object.get().getId().equals(photo.getObjectId()))
                            .sorted(Comparator.comparing(Photo::getDisplayOrder))
                            .collect(Collectors.toList());
                }


                // Проверяем права пользователя
                Employee currentUser = (Employee) req.getSession().getAttribute("employee");
                boolean canComplete = currentUser != null &&
                        (task.getResponsibleId() == null ||
                                task.getResponsibleId().equals(currentUser.getId()) ||
                                task.getCreatorId().equals(currentUser.getId()));

                // Устанавливаем атрибуты
                req.setAttribute("task", task);
                req.setAttribute("client", client.orElse(null));
                req.setAttribute("object", object.orElse(null));
                req.setAttribute("meeting", meeting.orElse(null));
                req.setAttribute("deal", deal.orElse(null));
                req.setAttribute("responsible", responsible.orElse(null));
                req.setAttribute("creator", creator.orElse(null));
                req.setAttribute("condition", condition.orElse(null));
                req.setAttribute("canComplete", canComplete);
                req.setAttribute("objectPhotos" ,objectPhotos );


                // Сообщение об успехе
                String success = req.getParameter("success");
                if ("Сompleted".equals(success)) {
                    req.setAttribute("successMessage", "Задача успешно отмечена как выполненная!");
                }

                req.getRequestDispatcher("/WEB-INF/jsp/taskDetail.jsp").forward(req, resp);
            } else {
                req.setAttribute("error", "Задача не найдена");
                req.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(req, resp);
            }
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Неверный ID задачи");
            req.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(req, resp);
        }
    }

    private void editTask(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int taskId = Integer.parseInt(req.getParameter("id"));
            Optional<Task> taskOpt = taskService.getById(taskId);

            if (taskOpt.isPresent()) {
                Task task = taskOpt.get();

                // Проверяем права на редактирование
                Employee currentUser = (Employee) req.getSession().getAttribute("employee");
                if (currentUser == null ||
                        (!task.getCreatorId().equals(currentUser.getId()) &&
                                !task.getResponsibleId().equals(currentUser.getId()))) {
                    req.setAttribute("error", "У вас нет прав для редактирования этой задачи");
                    viewTask(req, resp);
                    return;
                }

                // Обработка привязки сущности через GET параметры
                String attachType = req.getParameter("attach");
                String entityIdStr = req.getParameter("entityId");
                if (attachType != null && entityIdStr != null) {
                    try {
                        int entityId = Integer.parseInt(entityIdStr);
                        switch (attachType) {
                            case "client":
                                task.setClientId(entityId);
                                break;
                            case "object":
                                task.setObjectId(entityId);
                                break;
                            case "meeting":
                                task.setMeetingId(entityId);
                                break;
                            case "deal":
                                task.setDealId(entityId);
                                break;
                            case "condition":
                                task.setConditionId(entityId);
                                break;
                        }
                        taskService.update(task);
                        resp.sendRedirect("task?action=view&id=" + taskId + "&success=attached");
                        return;
                    } catch (NumberFormatException e) {
                        req.setAttribute("error", "Неверный ID сущности");
                    }
                }

                // Получаем данные для выпадающих списков
                List<Client> clients = clientService.getAll();
                List<com.company.crm.model.Object> objects = objectService.getAll();
                List<Meeting> meetings = meetingService.getAll();
                List<Deal> deals = dealService.getAll();
                List<Employee> employees = employeeService.getAll();
                List<AdditionalCondition> conditions = conditionService.getAll();

                req.setAttribute("task", task);
                req.setAttribute("clients", clients);
                req.setAttribute("objects", objects);
                req.setAttribute("meetings", meetings);
                req.setAttribute("deals", deals);
                req.setAttribute("employees", employees);
                req.setAttribute("conditions", conditions);
                req.setAttribute("currentEmployee", currentUser);

                req.getRequestDispatcher("/WEB-INF/jsp/editTask.jsp").forward(req, resp);
            } else {
                req.setAttribute("error", "Задача не найдена");
                req.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(req, resp);
            }
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Неверный ID задачи");
            req.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(req, resp);
        }
    }

    private void updateTask(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int taskId = Integer.parseInt(req.getParameter("id"));
            Optional<Task> taskOpt = taskService.getById(taskId);

            if (taskOpt.isPresent()) {
                Task task = taskOpt.get();

                // Проверяем права на редактирование
                Employee currentUser = (Employee) req.getSession().getAttribute("employee");
                if (currentUser == null ||
                        (!task.getCreatorId().equals(currentUser.getId()) &&
                                !task.getResponsibleId().equals(currentUser.getId()))) {
                    req.setAttribute("error", "У вас нет прав для редактирования этой задачи");
                    viewTask(req, resp);
                    return;
                }

                // Обновляем поля задачи
                task.setTitle(req.getParameter("title"));
                task.setDescription(req.getParameter("description"));
                task.setPriority(req.getParameter("priority"));
                task.setStatus(req.getParameter("status"));

                // Обработка даты
                String dueDateStr = req.getParameter("dueDate");
                if (dueDateStr != null && !dueDateStr.isEmpty()) {
                    LocalDateTime dueDate = LocalDateTime.parse(dueDateStr.replace(" ", "T"));
                    task.setDueDate(dueDate);
                } else {
                    task.setDueDate(null);
                }

                // Ответственный сотрудник
                String responsibleIdStr = req.getParameter("responsibleId");
                if (responsibleIdStr != null && !responsibleIdStr.isEmpty()) {
                    task.setResponsibleId(Integer.parseInt(responsibleIdStr));
                } else {
                    task.setResponsibleId(currentUser.getId());
                }

                // Обновляем связанные сущности
                task.setClientId(parseOptionalInteger(req.getParameter("clientId")));
                task.setObjectId(parseOptionalInteger(req.getParameter("objectId")));
                task.setMeetingId(parseOptionalInteger(req.getParameter("meetingId")));
                task.setDealId(parseOptionalInteger(req.getParameter("dealId")));
                task.setConditionId(parseOptionalInteger(req.getParameter("conditionId")));

                // Сохраняем изменения
                taskService.update(task);

                resp.sendRedirect("task?action=view&id=" + taskId + "&success=updated");
            } else {
                req.setAttribute("error", "Задача не найдена");
                req.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            req.setAttribute("error", "Ошибка при обновлении задачи: " + e.getMessage());
            editTask(req, resp);
        }
    }

    private void showCompletedTasks(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Employee emp = (Employee) req.getSession().getAttribute("employee");
        if (emp != null) {
            List<Task> allTasks = taskService.findByResponsible(emp.getId());
            List<Task> completedTasks = new ArrayList<>();
            Map<String, List<Task>> tasksByStatus = new HashMap<>();
            tasksByStatus.put("COMPLETED", new ArrayList<>());

            for (Task task : allTasks) {
                if ("COMPLETED".equalsIgnoreCase(task.getStatus())) {
                    completedTasks.add(task);
                    tasksByStatus.get("COMPLETED").add(task);
                }
            }

            req.setAttribute("tasks", completedTasks);
            req.setAttribute("tasksByStatus", tasksByStatus);
            req.getRequestDispatcher("/WEB-INF/jsp/completedTasks.jsp").forward(req, resp);
        } else {
            resp.sendRedirect("login");
        }
    }
}