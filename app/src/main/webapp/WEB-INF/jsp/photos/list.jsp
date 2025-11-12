<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Фотографии</title>
</head>
<body>
<h2>Список фотографий</h2>
<a href="photo?action=new">Добавить фотографию</a>
<a href="index.jsp">На главную</a>
<table border="1">
<tr><th>ID</th><th>URL фотографии</th><th>Подпись</th><th>Порядок</th><th>ID объекта</th><th>Действия</th></tr>
<c:forEach var="photo" items="${photos}">
    <tr>
        <td>${photo.id}</td>
        <td><a href="${photo.photoUrl}" target="_blank">${photo.photoUrl}</a></td>
        <td>${photo.caption}</td>
        <td>${photo.displayOrder}</td>
        <td>${photo.objectId}</td>
        <td>
            <a href="photo?action=view&id=${photo.id}">Просмотр</a>
            <a href="photo?action=edit&id=${photo.id}">Редактировать</a>
            <a href="photo?action=delete&id=${photo.id}">Удалить</a>
        </td>
    </tr>
</c:forEach>
</table>
<br/>
<a href="index.jsp">На главную</a>
</body>
</html>