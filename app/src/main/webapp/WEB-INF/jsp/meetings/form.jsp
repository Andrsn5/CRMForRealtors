<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title><c:if test="${empty meeting}">Новая</c:if><c:if test="${!empty meeting}">Редактировать</c:if> встреча</title>
</head>
<body>
<h2><c:if test="${empty meeting}">Новая</c:if><c:if test="${!empty meeting}">Редактировать</c:if> встреча</h2>
<form method="post" action="meeting">
    <input type="hidden" name="id" value="${meeting.id}" />
    Название: <input type="text" name="title" value="${meeting.title}" required/><br/>
    Дата и время: <input type="datetime-local" name="meetingDate"
        value="<c:if test="${not empty meeting.meetingDate}">${meeting.meetingDate.toString().replace(' ', 'T')}</c:if>"/><br/>
    Место: <input type="text" name="location" value="${meeting.location}"/><br/>
    ID клиента: <input type="number" name="clientId" value="${meeting.clientId}"/><br/>
    ID задачи: <input type="number" name="taskId" value="${meeting.taskId}"/><br/>
    Статус:
    <select name="status">
        <option value="запланирована" <c:if test="${meeting.status == 'запланирована'}">selected</c:if>>Запланирована</option>
        <option value="состоялась" <c:if test="${meeting.status == 'состоялась'}">selected</c:if>>Состоялась</option>
        <option value="отменена" <c:if test="${meeting.status == 'отменена'}">selected</c:if>>Отменена</option>
        <option value="перенесена" <c:if test="${meeting.status == 'перенесена'}">selected</c:if>>Перенесена</option>
    </select><br/>
    Примечания: <textarea name="notes">${meeting.notes}</textarea><br/>
    <button type="submit">Сохранить</button>
</form>
<a href="meeting?action=list">Назад к списку</a>
</body>
</html>