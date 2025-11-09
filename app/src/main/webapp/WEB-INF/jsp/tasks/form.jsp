<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title><c:if test="${empty task}">Новая</c:if><c:if test="${!empty task}">Редактировать</c:if> задача</title>
</head>
<body>
<h2><c:if test="${empty task}">Новая</c:if><c:if test="${!empty task}">Редактировать</c:if> задача</h2>
<form method="post" action="task">
    <input type="hidden" name="id" value="${task.id}" />
    Название: <input type="text" name="title" value="${task.title}" required/><br/>
    Описание: <textarea name="description">${task.description}</textarea><br/>
    Срок выполнения: <input type="datetime-local" name="dueDate"
        value="<c:if test="${not empty task.dueDate}">${task.dueDate.toString().replace(' ', 'T')}</c:if>"/><br/>
    Приоритет:
    <select name="priority">
        <option value="низкий" <c:if test="${task.priority == 'низкий'}">selected</c:if>>Низкий</option>
        <option value="средний" <c:if test="${task.priority == 'средний'}">selected</c:if>>Средний</option>
        <option value="высокий" <c:if test="${task.priority == 'высокий'}">selected</c:if>>Высокий</option>
        <option value="критический" <c:if test="${task.priority == 'критический'}">selected</c:if>>Критический</option>
    </select><br/>
    Статус:
    <select name="status">
        <option value="к выполнению" <c:if test="${task.status == 'к выполнению'}">selected</c:if>>К выполнению</option>
        <option value="в работе" <c:if test="${task.status == 'в работе'}">selected</c:if>>В работе</option>
        <option value="выполнена" <c:if test="${task.status == 'выполнена'}">selected</c:if>>Выполнена</option>
        <option value="отложена" <c:if test="${task.status == 'отложена'}">selected</c:if>>Отложена</option>
        <option value="отменена" <c:if test="${task.status == 'отменена'}">selected</c:if>>Отменена</option>
    </select><br/>
    ID ответственного: <input type="number" name="responsibleId" value="${task.responsibleId}"/><br/>
    ID создателя: <input type="number" name="creatorId" value="${task.creatorId}"/><br/>
    ID клиента: <input type="number" name="clientId" value="${task.clientId}"/><br/>
    ID объекта: <input type="number" name="objectId" value="${task.objectId}"/><br/>
    ID условия: <input type="number" name="conditionId" value="${task.conditionId}"/><br/>
    ID сделки: <input type="number" name="dealId" value="${task.dealId}"/><br/>
    ID встречи: <input type="number" name="meetingId" value="${task.meetingId}"/><br/>
    <button type="submit">Сохранить</button>
</form>
<a href="task?action=list">Назад к списку</a>
</body>
</html>