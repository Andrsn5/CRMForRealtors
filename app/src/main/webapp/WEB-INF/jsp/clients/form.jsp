<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title><c:if test="${empty client}">Новый</c:if><c:if test="${!empty client}">Редактировать</c:if> клиент</title>
</head>
<body>
<%-- Отображение ошибок --%>
<c:if test="${not empty error}">
    <div style="color: red; margin-bottom: 10px; padding: 10px; border: 1px solid red;">
        <strong>Ошибка:</strong> ${error}
    </div>
</c:if>
<h2><c:if test="${empty client}">Новый</c:if><c:if test="${!empty client}">Редактировать</c:if> клиент</h2>
<form method="post" action="client">
    <input type="hidden" name="id" value="${client.id}" />
    Имя: <input type="text" name="firstName" value="${client.firstName}" required/><br/>
    Фамилия: <input type="text" name="lastName" value="${client.lastName}" required/><br/>
    Email: <input type="email" name="email" value="${client.email}"/><br/>
    Телефон: <input type="text" name="phone" value="${client.phone}"/><br/>
    Тип клиента:
    <select name="clientType">
        <option value="customer" <c:if test="${client.clientType == 'customer'}">selected</c:if>>customer</option>
        <option value="seller" <c:if test="${client.clientType == 'seller'}">selected</c:if>>seller</option>
        <option value="tenant" <c:if test="${client.clientType == 'tenant'}">selected</c:if>>tenant</option>
        <option value="landlord" <c:if test="${client.clientType == 'landlord'}">selected</c:if>>landlord</option>
    </select><br/>
    Бюджет: <input type="number" step="0.01" name="budget" value="${client.budget}"/><br/>
    Примечания: <textarea name="notes">${client.notes}</textarea><br/>
    <button type="submit">Сохранить</button>
</form>
<a href="client?action=list">Назад к списку</a>
<a href="index.jsp">На главную</a>
</body>
</html>