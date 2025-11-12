<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Клиенты</title>
</head>
<body>
<h2>Список клиентов</h2>
<a href="client?action=new">Добавить клиента</a>
<a href="index.jsp">На главную</a>
<table border="1">
<tr><th>ID</th><th>Имя</th><th>Фамилия</th><th>Email</th><th>Телефон</th><th>Тип</th><th>Действия</th></tr>
<c:forEach var="client" items="${clients}">
    <tr>
        <td>${client.id}</td>
        <td>${client.firstName}</td>
        <td>${client.lastName}</td>
        <td>${client.email}</td>
        <td>${client.phone}</td>
        <td>${client.clientType}</td>
        <td>
            <a href="client?action=view&id=${client.id}">Просмотр</a>
            <a href="client?action=edit&id=${client.id}">Редактировать</a>
            <a href="client?action=delete&id=${client.id}">Удалить</a>
        </td>
    </tr>
</c:forEach>
</table>
<br/>
<a href="index.jsp">На главную</a>
</body>
</html>