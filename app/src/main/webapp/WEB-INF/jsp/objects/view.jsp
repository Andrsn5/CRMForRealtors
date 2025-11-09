<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Просмотр объекта</title>
</head>
<body>
<h2>Карточка объекта недвижимости</h2>
<table border="1" cellpadding="5">
    <tr><th>ID</th><td>${object.id}</td></tr>
    <tr><th>Название</th><td>${object.title}</td></tr>
    <tr><th>Описание</th><td>${object.description}</td></tr>
    <tr><th>Тип объекта</th><td>${object.objectType}</td></tr>
    <tr><th>Тип сделки</th><td>${object.dealType}</td></tr>
    <tr><th>Цена</th><td>${object.price}</td></tr>
    <tr><th>Адрес</th><td>${object.address}</td></tr>
    <tr><th>Площадь</th><td>${object.area}</td></tr>
    <tr><th>Комнат</th><td>${object.rooms}</td></tr>
    <tr><th>Санузлов</th><td>${object.bathrooms}</td></tr>
    <tr><th>Статус</th><td>${object.status}</td></tr>
</table>
<br/>
<a href="object?action=edit&id=${object.id}">Редактировать</a> |
<a href="object?action=list">Назад к списку</a>
</body>
</html>