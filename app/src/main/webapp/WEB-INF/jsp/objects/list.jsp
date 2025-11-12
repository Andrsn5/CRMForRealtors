<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Объекты недвижимости</title>
</head>
<body>
<h2>Список объектов недвижимости</h2>
<a href="object?action=new">Добавить объект</a>
<a href="index.jsp">На главную</a>
<table border="1">
<tr><th>ID</th><th>Название</th><th>Тип</th><th>Тип сделки</th><th>Цена</th><th>Адрес</th><th>Действия</th></tr>
<c:forEach var="obj" items="${objects}">
    <tr>
        <td>${obj.id}</td>
        <td>${obj.title}</td>
        <td>${obj.objectType}</td>
        <td>${obj.dealType}</td>
        <td>${obj.price}</td>
        <td>${obj.address}</td>
        <td>
            <a href="object?action=view&id=${obj.id}">Просмотр</a>
            <a href="object?action=edit&id=${obj.id}">Редактировать</a>
            <a href="object?action=delete&id=${obj.id}">Удалить</a>
        </td>
    </tr>
</c:forEach>
</table>
<br/>
<a href="index.jsp">На главную</a>
</body>
</html>