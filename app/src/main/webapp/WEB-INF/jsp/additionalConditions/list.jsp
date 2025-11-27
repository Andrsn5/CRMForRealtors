<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Дополнительные условия</title>
</head>
<body>
<h2>Список дополнительных условий</h2>
<a href="additionalCondition?action=new">Добавить условие</a>
<a href="index.jsp">На главную</a>
<table border="1">
<tr><th>ID</th><th>Тип условия</th><th>Описание</th><th>Срок</th><th>Статус</th><th>Приоритет</th><th>Действия</th></tr>
<c:forEach var="condition" items="${conditions}">
    <tr>
        <td>${condition.id}</td>
        <td>${condition.conditionType}</td>
        <td>${condition.description}</td>
        <td>${condition.deadline}</td>
        <td>${condition.status}</td>
        <td>${condition.priority}</td>
        <td>
            <a href="additionalCondition?action=view&id=${condition.id}">Просмотр</a>
            <a href="additionalCondition?action=edit&id=${condition.id}">Редактировать</a>
            <a href="additionalCondition?action=delete&id=${condition.id}">Удалить</a>
        </td>
    </tr>
</c:forEach>
</table>
<br/>
<a href="index.jsp">На главную</a>
</body>
</html>