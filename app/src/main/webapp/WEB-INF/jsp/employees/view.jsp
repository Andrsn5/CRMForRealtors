<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Просмотр сотрудника</title>
</head>
<body>
<h2>Карточка сотрудника</h2>
<table border="1" cellpadding="5">
    <tr><th>ID</th><td>${employee.id}</td></tr>
    <tr><th>Имя</th><td>${employee.firstName}</td></tr>
    <tr><th>Фамилия</th><td>${employee.lastName}</td></tr>
    <tr><th>Email</th><td>${employee.email}</td></tr>
    <tr><th>Телефон</th><td>${employee.phone}</td></tr>
    <tr><th>Должность</th><td>${employee.position}</td></tr>
    <tr><th>Активен</th><td><c:if test="${employee.active}">Да</c:if><c:if test="${!employee.active}">Нет</c:if></td></tr>
</table>
<br/>
<a href="employee?action=edit&id=${employee.id}">Редактировать</a> |
<a href="employee?action=list">Назад к списку</a>
</body>
</html>