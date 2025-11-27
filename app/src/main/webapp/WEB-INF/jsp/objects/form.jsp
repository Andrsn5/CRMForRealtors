<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title><c:if test="${empty object}">Новый</c:if><c:if test="${!empty object}">Редактировать</c:if> объект</title>
</head>
<body>

<h2><c:if test="${empty object}">Новый</c:if><c:if test="${!empty object}">Редактировать</c:if> объект недвижимости</h2>

<%-- Отображение ошибок --%>
<c:if test="${not empty error}">
    <div style="color: red; border: 1px solid red; padding: 10px; margin: 10px 0;">
        Ошибка: ${error}
    </div>
</c:if>

<form method="post" action="object">
    <input type="hidden" name="id" value="${object.id}" />
    Название: <input type="text" name="title" value="${object.title}" required/><br/>
    Описание: <textarea name="description">${object.description}</textarea><br/>
    Тип объекта:
    <select name="objectType">
        <option value="Apartment" <c:if test="${object.objectType == 'Apartment'}">selected</c:if>>Apartment</option>
        <option value="House" <c:if test="${object.objectType == 'House'}">selected</c:if>>House</option>
        <option value="Commercial" <c:if test="${object.objectType == 'Commercial'}">selected</c:if>>Commercial</option>
        <option value="Land" <c:if test="${object.objectType == 'Land'}">selected</c:if>>Land</option>
        <option value="Villa" <c:if test="${object.objectType == 'Villa'}">selected</c:if>>Villa</option>
    </select><br/>
    Тип сделки:
    <select name="dealType">
        <option value="Sale" <c:if test="${object.dealType == 'Sale'}">selected</c:if>>Sale</option>
        <option value="Rent" <c:if test="${object.dealType == 'Rent'}">selected</c:if>>Rent</option>
    </select><br/>
    Цена: <input type="number" step="0.01" name="price" value="${object.price}"/><br/>
    Адрес: <input type="text" name="address" value="${object.address}"/><br/>
    Площадь: <input type="number" step="0.01" name="area" value="${object.area}"/><br/>
    Комнат: <input type="number" name="rooms" value="${object.rooms}"/><br/>
    Санузлов: <input type="number" name="bathrooms" value="${object.bathrooms}"/><br/>
    Статус:
    <select name="status">
        <option value="Available" <c:if test="${object.status == 'Available'}">selected</c:if>>Available</option>
        <option value="Sold" <c:if test="${object.status == 'Sold'}">selected</c:if>>Sold</option>
        <option value="Rented" <c:if test="${object.status == 'Rented'}">selected</c:if>>Rented</option>
        <option value="Reserved" <c:if test="${object.status == 'Reserved'}">selected</c:if>>Reserved</option>
        <option value="Draft" <c:if test="${object.status == 'Draft'}">selected</c:if>>Draft</option>
    </select><br/>
    <button type="submit">Сохранить</button>
</form>
<br>
<a href="object?action=list">Назад к списку</a> |
<a href="index.jsp">На главную</a>
</body>
</html>