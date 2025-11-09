<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title><c:if test="${empty client}">Новый</c:if><c:if test="${!empty client}">Редактировать</c:if> клиент</title>
</head>
<body>
<h2><c:if test="${empty client}">Новый</c:if><c:if test="${!empty client}">Редактировать</c:if> клиент</h2>
<form method="post" action="client">
    <input type="hidden" name="id" value="${client.id}" />
    Имя: <input type="text" name="firstName" value="${client.firstName}" required/><br/>
    Фамилия: <input type="text" name="lastName" value="${client.lastName}" required/><br/>
    Email: <input type="email" name="email" value="${client.email}"/><br/>
    Телефон: <input type="text" name="phone" value="${client.phone}"/><br/>
    Тип клиента:
    <select name="clientType">
        <option value="покупатель" <c:if test="${client.clientType == 'покупатель'}">selected</c:if>>Покупатель</option>
        <option value="продавец" <c:if test="${client.clientType == 'продавец'}">selected</c:if>>Продавец</option>
        <option value="арендатор" <c:if test="${client.clientType == 'арендатор'}">selected</c:if>>Арендатор</option>
        <option value="арендодатель" <c:if test="${client.clientType == 'арендодатель'}">selected</c:if>>Арендодатель</option>
    </select><br/>
    Бюджет: <input type="number" step="0.01" name="budget" value="${client.budget}"/><br/>
    Примечания: <textarea name="notes">${client.notes}</textarea><br/>
    <button type="submit">Сохранить</button>
</form>
<a href="client?action=list">Назад к списку</a>
</body>
</html>