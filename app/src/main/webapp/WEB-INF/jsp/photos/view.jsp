<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Просмотр фотографии</title>
</head>
<body>
<h2>Карточка фотографии</h2>
<table border="1" cellpadding="5">
    <tr><th>ID</th><td>${photo.id}</td></tr>
    <tr><th>URL фотографии</th><td><a href="${photo.photoUrl}" target="_blank">${photo.photoUrl}</a></td></tr>
    <tr><th>Подпись</th><td>${photo.caption}</td></tr>
    <tr><th>Порядок отображения</th><td>${photo.displayOrder}</td></tr>
    <tr><th>ID объекта</th><td>${photo.objectId}</td></tr>
</table>
<br/>
<c:if test="${not empty photo.photoUrl}">
    <img src="${photo.photoUrl}" alt="${photo.caption}" style="max-width: 400px; max-height: 300px;"/><br/>
</c:if>
<br/>
<a href="photo?action=edit&id=${photo.id}">Редактировать</a> |
<a href="photo?action=list">Назад к списку</a>
</body>
</html>