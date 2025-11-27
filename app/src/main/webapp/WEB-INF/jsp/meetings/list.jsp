<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Встречи</title>
</head>
<body>
<h2>Список встреч</h2>
<a href="meeting?action=new">Добавить встречу</a>
<a href="index.jsp">На главную</a>

<table border="1">
<tr><th>ID</th><th>Название</th><th>Дата и время</th><th>Место</th><th>ID клиента</th><th>Статус</th><th>Действия</th></tr>
<c:forEach var="meeting" items="${meetings}">
    <tr>
        <td>${meeting.id}</td>
        <td>${meeting.title}</td>
        <td>${meeting.meetingDate}</td>
        <td>${meeting.location}</td>
        <td>${meeting.clientId}</td>
        <td>${meeting.status}</td>
        <td>
            <a href="meeting?action=view&id=${meeting.id}">Просмотр</a>
            <a href="meeting?action=edit&id=${meeting.id}">Редактировать</a>
            <a href="meeting?action=delete&id=${meeting.id}">Удалить</a>
        </td>
    </tr>
</c:forEach>
</table>
<br/>
<a href="index.jsp">На главную</a>
</body>
</html>