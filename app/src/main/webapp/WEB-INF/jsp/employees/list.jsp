<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Сотрудники</title>
</head>
<body>
<h2>Список сотрудников</h2>
<a href="employee?action=new">Добавить сотрудника</a>
<a href="index.jsp">На главную</a>
<table border="1">
<tr><th>ID</th><th>Имя</th><th>Фамилия</th><th>Email</th><th>Действия</th></tr>
<c:forEach var="emp" items="${employees}">
    <tr>
        <td>${emp.id}</td>
        <td>${emp.firstName}</td>
        <td>${emp.lastName}</td>
        <td>${emp.email}</td>
        <td>
            <a href="employee?action=edit&id=${emp.id}">Редактировать</a> |
            <a href="employee?action=delete&id=${emp.id}">Удалить</a>
        </td>
    </tr>
</c:forEach>
</table>
<br>
<a href="index.jsp">На главную</a>
</body>
</html>