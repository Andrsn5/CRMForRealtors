<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Сделки</title>
</head>
<body>
<h2>Список сделок</h2>
<a href="deal?action=new">Добавить сделку</a>
<table border="1">
<tr><th>ID</th><th>Номер сделки</th><th>ID задачи</th><th>Сумма</th><th>Дата</th><th>Комиссия</th><th>Статус</th><th>Действия</th></tr>
<c:forEach var="deal" items="${deals}">
    <tr>
        <td>${deal.id}</td>
        <td>${deal.dealNumber}</td>
        <td>${deal.taskId}</td>
        <td>${deal.dealAmount}</td>
        <td>${deal.dealDate}</td>
        <td>${deal.commission}</td>
        <td>${deal.status}</td>
        <td>
            <a href="deal?action=view&id=${deal.id}">Просмотр</a>
            <a href="deal?action=edit&id=${deal.id}">Редактировать</a>
            <a href="deal?action=delete&id=${deal.id}">Удалить</a>
        </td>
    </tr>
</c:forEach>
</table>
<br/>
<a href="index.jsp">На главную</a>
</body>
</html>