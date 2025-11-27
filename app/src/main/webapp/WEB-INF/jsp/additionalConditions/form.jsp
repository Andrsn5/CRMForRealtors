<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title><c:if test="${empty condition}">Новое</c:if><c:if test="${!empty condition}">Редактировать</c:if> условие</title>
</head>
<body>
<%-- Отображение ошибок --%>
<c:if test="${not empty error}">
    <div style="color: red; margin-bottom: 10px; padding: 10px; border: 1px solid red;">
        <strong>Ошибка:</strong> ${error}
    </div>
</c:if>
<h2><c:if test="${empty condition}">Новое</c:if><c:if test="${!empty condition}">Редактировать</c:if> дополнительное условие</h2>
<form method="post" action="additionalCondition">
    <input type="hidden" name="id" value="${condition.id}" />
    Тип условия:
    <select name="conditionType">
        <option value="marketing" <c:if test="${condition.conditionType == 'marketing'}">selected</c:if>>marketing</option>
        <option value="administrative" <c:if test="${condition.conditionType == 'administrative'}">selected</c:if>>administrative</option>
        <option value="customer service" <c:if test="${condition.conditionType == 'customer service'}">selected</c:if>>customer service</option>
        <option value="logistics" <c:if test="${condition.conditionType == 'logistics'}">selected</c:if>>logistics</option>
    </select><br/>
    Описание: <textarea name="description" required>${condition.description}</textarea><br/>
    Срок выполнения: <input type="date" name="deadline" value="${condition.deadline}"/><br/>
    Обязательное: <input type="checkbox" name="required" <c:if test="${condition.required}">checked</c:if>/><br/>
    Статус:
    <select name="status">
        <option value="active" <c:if test="${condition.status == 'active'}">selected</c:if>>active</option>
        <option value="completed" <c:if test="${condition.status == 'completed'}">selected</c:if>>completed</option>
        <option value="canceled" <c:if test="${condition.status == 'canceled'}">selected</c:if>>canceled</option>
    </select><br/>
    Приоритет:
    <select name="priority">
        <option value="low" <c:if test="${condition.priority == 'low'}">selected</c:if>>Низкий</option>
        <option value="medium" <c:if test="${condition.priority == 'medium'}">selected</c:if>>Средний</option>
        <option value="high" <c:if test="${condition.priority == 'high'}">selected</c:if>>Высокий</option>
    </select><br/>
    Примечания: <textarea name="notes">${condition.notes}</textarea><br/>
    <button type="submit">Сохранить</button>
</form>
<a href="additionalCondition?action=list">Назад к списку</a>
<a href="index.jsp">На главную</a>
</body>
</html>