<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title><c:if test="${empty task}">Новая</c:if><c:if test="${!empty task}">Редактировать</c:if> задача</title>
</head>
<body>
<%-- Отображение ошибок --%>
<c:if test="${not empty error}">
    <div style="color: red; margin-bottom: 10px; padding: 10px; border: 1px solid red;">
        <strong>Ошибка:</strong> ${error}
    </div>
</c:if>
<h2><c:if test="${empty task}">Новая</c:if><c:if test="${!empty task}">Редактировать</c:if> задача</h2>
<form method="post" action="adminTask">
    <input type="hidden" name="id" value="${task.id}" />
    Название: <input type="text" name="title" value="${task.title}" required/><br/>
    Описание: <textarea name="description">${task.description}</textarea><br/>
    Срок выполнения: <input type="datetime-local" name="dueDate"
        value="<c:if test="${not empty task.dueDate}">${task.dueDate.toString().replace(' ', 'T')}</c:if>"/><br/>
    Приоритет:
    <select name="priority">
        <option value="High" <c:if test="${task.priority == 'High'}">selected</c:if>>High</option>
        <option value="Medium" <c:if test="${task.priority == 'Medium'}">selected</c:if>>Medium</option>
        <option value="Low" <c:if test="${task.priority == 'Low'}">selected</c:if>>Low</option>
     </select><br/>
     Статус:
         <select name="status" required>
             <option value="">-- Выберите статус --</option>
             <option value="In Progress" <c:if test="${task.status == 'In Progress'}">selected</c:if>>In Progress</option>
             <option value="Completed" <c:if test="${task.status == 'Completed'}">selected</c:if>>Completed</option>
             <option value="Cancelled" <c:if test="${task.status == 'Cancelled'}">selected</c:if>>Cancelled</option>
             <option value="On Hold" <c:if test="${task.status == 'On Hold'}">selected</c:if>>On Hold</option>
         </select><br/>

        ID ответственного: <input type="number" name="responsibleId" value="${task.responsibleId}" required/><br/>

        ID создателя: <input type="number" name="creatorId" value="${task.creatorId}" required/><br/>

        ID клиента: <input type="number" name="clientId" value="${task.clientId}" required//><br/>

        ID объекта: <input type="number" name="objectId" value="${task.objectId}" required//><br/>

        ID условия:
        <input type="number" name="conditionId" value="${task.conditionId}"
            placeholder="Оставьте пустым для NULL"/><br/>

        ID сделки:
        <input type="number" name="dealId" value="${task.dealId}"
            placeholder="Оставьте пустым для NULL"/><br/>

        ID встречи:
        <input type="number" name="meetingId" value="${task.meetingId}"
            placeholder="Оставьте пустым для NULL"/><br/>

        <button type="submit">Сохранить</button>
    </form>
<a href="adminTask?action=list">Назад к списку</a>
<a href="index.jsp">На главную</a>
</body>
</html>