<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Задачи</title>
</head>
<body>
<h2>Список задач</h2>
<a href="adminTask?action=new">Добавить задачу</a>
<a href="index.jsp">На главную</a>

<table border="1">
<tr><th>ID</th><th>Название</th><th>Срок</th><th>Приоритет</th><th>Статус</th><th>Ответственный</th><th>Действия</th></tr>
<c:forEach var="task" items="${tasks}">
    <tr>
        <td>${task.id}</td>
        <td>${task.title}</td>
        <td>${task.dueDate}</td>
        <td>${task.priority}</td>
        <td>${task.status}</td>
        <td>${task.responsibleId}</td>
        <td>
            <a href="adminTask?action=view&id=${task.id}">Просмотр</a>
            <a href="adminTask?action=edit&id=${task.id}">Редактировать</a>
            <a href="adminTask?action=delete&id=${task.id}">Удалить</a>
        </td>
    </tr>
</c:forEach>
</table>
<br/>
<a href="index.jsp">На главную</a>
</body>
</html>