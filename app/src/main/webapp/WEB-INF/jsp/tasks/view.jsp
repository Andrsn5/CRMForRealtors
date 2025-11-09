<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Просмотр задачи</title>
</head>
<body>
<h2>Карточка задачи</h2>
<table border="1" cellpadding="5">
    <tr><th>ID</th><td>${task.id}</td></tr>
    <tr><th>Название</th><td>${task.title}</td></tr>
    <tr><th>Описание</th><td>${task.description}</td></tr>
    <tr><th>Срок выполнения</th><td>${task.dueDate}</td></tr>
    <tr><th>Приоритет</th><td>${task.priority}</td></tr>
    <tr><th>Статус</th><td>${task.status}</td></tr>
    <tr><th>ID ответственного</th><td>${task.responsibleId}</td></tr>
    <tr><th>ID создателя</th><td>${task.creatorId}</td></tr>
    <tr><th>ID клиента</th><td>${task.clientId}</td></tr>
    <tr><th>ID объекта</th><td>${task.objectId}</td></tr>
    <tr><th>ID условия</th><td>${task.conditionId}</td></tr>
    <tr><th>ID сделки</th><td>${task.dealId}</td></tr>
    <tr><th>ID встречи</th><td>${task.meetingId}</td></tr>
</table>
<br/>
<a href="task?action=edit&id=${task.id}">Редактировать</a> |
<a href="task?action=list">Назад к списку</a>
</body>
</html>