<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title><c:if test="${empty photo}">Новая</c:if><c:if test="${!empty photo}">Редактировать</c:if> фотография</title>
</head>
<body>
<%-- Отображение ошибок --%>
<c:if test="${not empty error}">
    <div style="color: red; margin-bottom: 10px; padding: 10px; border: 1px solid red;">
        <strong>Ошибка:</strong> ${error}
    </div>
</c:if>
<h2><c:if test="${empty photo}">Новая</c:if><c:if test="${!empty photo}">Редактировать</c:if> фотография</h2>
<form method="post" action="photo">
    <input type="hidden" name="id" value="${photo.id}" />
    URL фотографии: <input type="url" name="photoUrl" value="${photo.photoUrl}" required/><br/>
    Подпись: <input type="text" name="caption" value="${photo.caption}"/><br/>
    Порядок отображения: <input type="number" name="displayOrder" value="${photo.displayOrder}"/><br/>
    ID объекта: <input type="number" name="objectId" value="${photo.objectId}"/><br/>
    <button type="submit">Сохранить</button>
</form>
<a href="photo?action=list">Назад к списку</a>
<a href="index.jsp">На главную</a>
</body>
</html>