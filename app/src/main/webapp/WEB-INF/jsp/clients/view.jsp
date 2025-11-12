<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Просмотр клиента</title>
</head>
<body>
<h2>Карточка клиента</h2>
<table border="1" cellpadding="5">
    <tr><th>ID</th><td>${client.id}</td></tr>
    <tr><th>Имя</th><td>${client.firstName}</td></tr>
    <tr><th>Фамилия</th><td>${client.lastName}</td></tr>
    <tr><th>Email</th><td>${client.email}</td></tr>
    <tr><th>Телефон</th><td>${client.phone}</td></tr>
    <tr><th>Тип клиента</th><td>${client.clientType}</td></tr>
    <tr><th>Бюджет</th><td>${client.budget}</td></tr>
    <tr><th>Примечания</th><td>${client.notes}</td></tr>
</table>
<br/>
<a href="client?action=edit&id=${client.id}">Редактировать</a> |
<a href="client?action=list">Назад к списку</a>
<a href="index.jsp">На главную</a>
</body>
</html>