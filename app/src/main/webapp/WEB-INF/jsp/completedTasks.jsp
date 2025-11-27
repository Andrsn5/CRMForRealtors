<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Завершенные задачи</title>
    <style>
        body { font-family: Arial; background-color: #f8f9fa; margin: 20px; }
        .container { max-width: 1000px; margin: 0 auto; }
        .back-btn {
            background: #6c757d;
            color: white;
            padding: 10px 20px;
            text-decoration: none;
            border-radius: 5px;
            display: inline-block;
            margin-bottom: 20px;
        }
        .back-btn:hover { background: #545b62; }
        .task-card {
            background: white;
            border: 1px solid #ddd;
            border-radius: 8px;
            padding: 15px;
            margin-bottom: 15px;
            transition: all 0.3s;
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
        .status-completed { background: #d4edda; color: #155724; }
        .task-details {
            color: #666;
            font-size: 14px;
        }
        .view-task-btn {
            background: #17a2b8;
            color: white;
            border: none;
            padding: 8px 16px;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
        }
        .view-task-btn:hover {
            background: #138496;
        }
        .section-title {
            color: #495057;
            border-bottom: 2px solid #28a745;
            padding-bottom: 10px;
            margin-bottom: 20px;
        }
        .stats {
            background: #fff;
            border: 1px solid #ddd;
            padding: 15px;
            border-radius: 10px;
            margin-bottom: 20px;
            text-align: center;
        }
    </style>
</head>
<body>
<div class="container">
    <a href="dashboard" class="back-btn">← Назад к активным задачам</a>

    <div class="stats">
        <h2>✅ Завершенные задачи</h2>
        <p>Всего выполнено: ${tasks.size()} задач</p>
    </div>

    <!-- Завершенные задачи -->
    <c:if test="${not empty tasksByStatus.COMPLETED}">
        <h3 class="section-title">Список выполненных задач</h3>
        <c:forEach var="task" items="${tasksByStatus.COMPLETED}">
            <div class="task-card">
                <div class="task-header">
                    <span class="task-title">${task.title}</span>
                    <span class="task-status status-completed">✅ Завершено</span>
                </div>
                <div class="task-details">
                    <p>${task.description}</p>
                    <p><strong>Срок выполнения был:</strong> ${task.dueDate}</p>
                    <p><strong>Приоритет:</strong> ${task.priority}</p>
                </div>
                <a href="task?action=view&id=${task.id}" class="view-task-btn">📖 Посмотреть детали</a>
            </div>
        </c:forEach>
    </c:if>

    <c:if test="${empty tasksByStatus.COMPLETED}">
        <div style="text-align: center; padding: 40px; background: white; border-radius: 8px;">
            <h3>📝 Пока нет завершенных задач</h3>
            <p>Выполненные задачи будут отображаться здесь.</p>
            <a href="dashboard" class="back-btn">← Вернуться к активным задачам</a>
        </div>
    </c:if>
</div>
</body>
</html>