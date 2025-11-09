<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Просмотр встречи</title>
</head>
<body>
<h2>Карточка встречи</h2>
<table border="1" cellpadding="5">
    <tr><th>ID</th><td>${meeting.id}</td></tr>
    <tr><th>Название</th><td>${meeting.title}</td></tr>
    <tr><th>Дата и время</th><td>${meeting.meetingDate}</td></tr>
    <tr><th>Место</th><td>${meeting.location}</td></tr>
    <tr><th>ID клиента</th><td>${meeting.clientId}</td></tr>
    <tr><th>ID задачи</th><td>${meeting.taskId}</td></tr>
    <tr><th>Статус</th><td>${meeting.status}</td></tr>
    <tr><th>Примечания</th><td>${meeting.notes}</td></tr>
</table>
<br/>
<a href="meeting?action=edit&id=${meeting.id}">Редактировать</a> |
<a href="meeting?action=list">Назад к списку</a>
</body>
</html>