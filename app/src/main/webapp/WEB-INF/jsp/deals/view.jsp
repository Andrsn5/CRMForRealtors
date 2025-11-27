<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Просмотр сделки</title>
</head>
<body>
<h2>Карточка сделки</h2>
<table border="1" cellpadding="5">
    <tr><th>ID</th><td>${deal.id}</td></tr>
    <tr><th>Номер сделки</th><td>${deal.dealNumber}</td></tr>
    <tr><th>ID задачи</th><td>${deal.taskId}</td></tr>
    <tr><th>Сумма сделки</th><td>${deal.dealAmount}</td></tr>
    <tr><th>Дата сделки</th><td>${deal.dealDate}</td></tr>
    <tr><th>Комиссия</th><td>${deal.commission}</td></tr>
    <tr><th>Статус</th><td>${deal.status}</td></tr>
</table>
<br/>
<a href="deal?action=edit&id=${deal.id}">Редактировать</a> |
<a href="deal?action=list">Назад к списку</a>
<a href="index.jsp">На главную</a>
</body>
</html>