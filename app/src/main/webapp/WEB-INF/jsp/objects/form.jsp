<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title><c:if test="${empty object}">Новый</c:if><c:if test="${!empty object}">Редактировать</c:if> объект</title>
</head>
<body>
<h2><c:if test="${empty object}">Новый</c:if><c:if test="${!empty object}">Редактировать</c:if> объект недвижимости</h2>
<form method="post" action="object">
    <input type="hidden" name="id" value="${object.id}" />
    Название: <input type="text" name="title" value="${object.title}" required/><br/>
    Описание: <textarea name="description">${object.description}</textarea><br/>
    Тип объекта:
    <select name="objectType">
        <option value="квартира" <c:if test="${object.objectType == 'квартира'}">selected</c:if>>Квартира</option>
        <option value="дом" <c:if test="${object.objectType == 'дом'}">selected</c:if>>Дом</option>
        <option value="офис" <c:if test="${object.objectType == 'офис'}">selected</c:if>>Офис</option>
        <option value="участок" <c:if test="${object.objectType == 'участок'}">selected</c:if>>Земельный участок</option>
    </select><br/>
    Тип сделки:
    <select name="dealType">
        <option value="продажа" <c:if test="${object.dealType == 'продажа'}">selected</c:if>>Продажа</option>
        <option value="аренда" <c:if test="${object.dealType == 'аренда'}">selected</c:if>>Аренда</option>
    </select><br/>
    Цена: <input type="number" step="0.01" name="price" value="${object.price}"/><br/>
    Адрес: <input type="text" name="address" value="${object.address}"/><br/>
    Площадь: <input type="number" step="0.01" name="area" value="${object.area}"/><br/>
    Комнат: <input type="number" name="rooms" value="${object.rooms}"/><br/>
    Санузлов: <input type="number" name="bathrooms" value="${object.bathrooms}"/><br/>
    Статус:
    <select name="status">
        <option value="активен" <c:if test="${object.status == 'активен'}">selected</c:if>>Активен</option>
        <option value="продан" <c:if test="${object.status == 'продан'}">selected</c:if>>Продан</option>
        <option value="сдан" <c:if test="${object.status == 'сдан'}">selected</c:if>>Сдан в аренду</option>
        <option value="архив" <c:if test="${object.status == 'архив'}">selected</c:if>>В архиве</option>
    </select><br/>
    <button type="submit">Сохранить</button>
</form>
<a href="object?action=list">Назад к списку</a>
</body>
</html>