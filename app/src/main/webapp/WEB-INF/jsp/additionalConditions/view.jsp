<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Просмотр условия</title>
</head>
<body>
<h2>Карточка дополнительного условия</h2>
<table border="1" cellpadding="5">
    <tr><th>ID</th><td>${condition.id}</td></tr>
    <tr><th>Тип условия</th><td>${condition.conditionType}</td></tr>
    <tr><th>Описание</th><td>${condition.description}</td></tr>
    <tr><th>Срок выполнения</th><td>${condition.deadline}</td></tr>
    <tr><th>Обязательное</th><td><c:if test="${condition.required}">Да</c:if><c:if test="${!condition.required}">Нет</c:if></td></tr>
    <tr><th>Статус</th><td>${condition.status}</td></tr>
    <tr><th>Приоритет</th><td>${condition.priority}</td></tr>
    <tr><th>Примечания</th><td>${condition.notes}</td></tr>
</table>
<br/>
<a href="additionalCondition?action=edit&id=${condition.id}">Редактировать</a> |
<a href="additionalCondition?action=list">Назад к списку</a>
<a href="index.jsp">На главную</a>
</body>
</html>