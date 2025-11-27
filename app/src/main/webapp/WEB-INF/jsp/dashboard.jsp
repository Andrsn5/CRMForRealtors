<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Моя панель CRM</title>
    <style>
        body { font-family: Arial; background-color: #f8f9fa; margin: 20px; }
        .nav { margin-bottom: 20px; }
        .nav a {
            padding: 10px 15px;
            margin-right: 5px;
            background: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 5px;
        }
        .nav a:hover { background: #0056b3; }
        h2 { color: #333; }
        table { border-collapse: collapse; width: 100%; background: white; margin-bottom: 30px; }
        th, td { padding: 12px; border-bottom: 1px solid #ddd; text-align: left; }
        th { background-color: #f1f1f1; }
        .stats {
            background: #fff; border: 1px solid #ddd; padding: 15px; border-radius: 10px; margin-bottom: 20px;
        }
        .task-card {
            background: white;
            border: 1px solid #ddd;
            border-radius: 8px;
            padding: 15px;
            margin-bottom: 15px;
            cursor: pointer;
            transition: all 0.3s;
        }
        .task-card:hover {
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            transform: translateY(-2px);
        }
        .task-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 10px;
        }
        .task-title {
            font-weight: bold;
            font-size: 16px;
            color: #333;
        }
        .task-status {
            padding: 4px 8px;
            border-radius: 12px;
            font-size: 12px;
            font-weight: bold;
        }
        .status-pending { background: #fff3cd; color: #856404; }
        .status-in_progress { background: #d1ecf1; color: #0c5460; }
        .status-completed { background: #d4edda; color: #155724; }
        .task-details {
            color: #666;
            font-size: 14px;
        }
        .view-task-btn {
            background: #28a745;
            color: white;
            border: none;
            padding: 8px 16px;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
        }
        .view-task-btn:hover {
            background: #218838;
        }
        .section-title {
            color: #495057;
            border-bottom: 2px solid #007bff;
            padding-bottom: 10px;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>

<h2>Добро пожаловать, ${sessionScope.employee.firstName} ${sessionScope.employee.lastName}</h2>

<div class="stats">
    <b>Активные задачи:</b> ${stats.activeTasksCount} |
    <b>Завершенные задачи:</b> ${stats.completedTasksCount} |
    <b>Сотрудников в CRM:</b> ${stats.totalEmployees}
</div>

<div class="nav">
    <a href="task?action=add" class="add-task-btn">➕ Добавить задачу</a>
    <a href="task?action=completed">✅ Выполненные задачи</a>
    <a href="logout" style="background: #dc3545;">🚪 Выйти</a>
</div>

<!-- ЗАДАЧИ ПО СТАТУСАМ -->
<h3 id="tasks" class="section-title">Мои задачи</h3>

<!-- Задачи в процессе -->
<c:if test="${not empty tasksByStatus.IN_PROGRESS}">
    <h4>⏳ В процессе</h4>
    <c:forEach var="task" items="${tasksByStatus.IN_PROGRESS}">
        <div class="task-card">
            <div class="task-header">
                <span class="task-title">${task.title}</span>
                <span class="task-status status-in_progress">В процессе</span>
            </div>
            <div class="task-details">
                <p>${task.description}</p>
                <p><strong>Срок:</strong> ${task.dueDate}</p>
                <p><strong>Приоритет:</strong> ${task.priority}</p>
            </div>
            <a href="task?action=view&id=${task.id}" class="view-task-btn">📖 Подробнее</a>
        </div>
    </c:forEach>
</c:if>


<c:if test="${not empty tasksByStatus.ON_HOLD}">
    <h4>📋 В работе</h4>
    <c:forEach var="task" items="${tasksByStatus.ON_HOLD}">
        <div class="task-card">
            <div class="task-header">
                <span class="task-title">${task.title}</span>
                <span class="task-status status-in_progress">В работе</span>
            </div>
            <div class="task-details">
                <p>${task.description}</p>
                <p><strong>Срок:</strong> ${task.dueDate}</p>
                <p><strong>Приоритет:</strong> ${task.priority}</p>
            </div>
            <a href="task?action=view&id=${task.id}" class="view-task-btn">📖 Подробнее</a>
        </div>
    </c:forEach>
</c:if>

<!-- Отмененные задачи -->
<c:if test="${not empty tasksByStatus.CANCELLED}">
    <h4>❌  Отмененные</h4>
    <c:forEach var="task" items="${tasksByStatus.CANCELLED}">
        <div class="task-card">
            <div class="task-header">
                <span class="task-title">${task.title}</span>
                <span class="task-status status-pending">Отмененные</span>
            </div>
            <div class="task-details">
                <p>${task.description}</p>
                <p><strong>Срок:</strong> ${task.dueDate}</p>
                <p><strong>Приоритет:</strong> ${task.priority}</p>
            </div>
            <a href="task?action=view&id=${task.id}" class="view-task-btn">📖 Подробнее</a>
        </div>
    </c:forEach>
</c:if>



<c:if test="${empty myTasks}">
    <div style="text-align: center; padding: 40px; background: white; border-radius: 8px;">
        <h3>🎉 Отличная работа!</h3>
        <p>У вас нет активных задач.</p>
    </div>
</c:if>

</body>
</html>