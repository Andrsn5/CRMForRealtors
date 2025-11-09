<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title><c:if test="${empty condition}">Новое</c:if><c:if test="${!empty condition}">Редактировать</c:if> условие</title>
</head>
<body>
<h2><c:if test="${empty condition}">Новое</c:if><c:if test="${!empty condition}">Редактировать</c:if> дополнительное условие</h2>
<form method="post" action="additionalCondition">
    <input type="hidden" name="id" value="${condition.id}" />
    Тип условия:
    <select name="conditionType">
        <option value="юридическое" <c:if test="${condition.conditionType == 'юридическое'}">selected</c:if>>Юридическое</option>
        <option value="финансовое" <c:if test="${condition.conditionType == 'финансовое'}">selected</c:if>>Финансовое</option>
        <option value="техническое" <c:if test="${condition.conditionType == 'техническое'}">selected</c:if>>Техническое</option>
        <option value="организационное" <c:if test="${condition.conditionType == 'организационное'}">selected</c:if>>Организационное</option>
    </select><br/>
    Описание: <textarea name="description" required>${condition.description}</textarea><br/>
    Срок выполнения: <input type="date" name="deadline" value="${condition.deadline}"/><br/>
    Обязательное: <input type="checkbox" name="required" <c:if test="${condition.required}">checked</c:if>/><br/>
    Статус:
    <select name="status">
        <option value="активно" <c:if test="${condition.status == 'активно'}">selected</c:if>>Активно</option>
        <option value="выполнено" <c:if test="${condition.status == 'выполнено'}">selected</c:if>>Выполнено</option>
        <option value="отменено" <c:if test="${condition.status == 'отменено'}">selected</c:if>>Отменено</option>
        <option value="просрочено" <c:if test="${condition.status == 'просрочено'}">selected</c:if>>Просрочено</option>
    </select><br/>
    Приоритет:
    <select name="priority">
        <option value="low" <c:if test="${condition.priority == 'low'}">selected</c:if>>Низкий</option>
        <option value="medium" <c:if test="${condition.priority == 'medium'}">selected</c:if>>Средний</option>
        <option value="high" <c:if test="${condition.priority == 'high'}">selected</c:if>>Высокий</option>
        <option value="critical" <c:if test="${condition.priority == 'critical'}">selected</c:if>>Критический</option>
    </select><br/>
    Примечания: <textarea name="notes">${condition.notes}</textarea><br/>
    <button type="submit">Сохранить</button>
</form>
<a href="additionalCondition?action=list">Назад к списку</a>
</body>
</html>