<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>CRM для риелторов</title>
</head>
<body>
    <div class="header">
        <h1>Админ-панель CRM</h1>
        <div class="user-info">
            Вы вошли как: <strong>${employee.firstName} ${employee.lastName}</strong>
            (${employee.position})
            <a href="${pageContext.request.contextPath}/logout" class="logout">Выйти</a>
        </div>
    </div>

<p>Выберите раздел:</p>
<ul>
    <li><a href="employee?action=list">Сотрудники</a></li>
    <li><a href="client?action=list">Клиенты</a></li>
    <li><a href="object?action=list">Объекты недвижимости</a></li>
    <li><a href="additionalCondition?action=list">Дополнительные условия</a></li>
    <li><a href="deal?action=list">Сделки</a></li>
    <li><a href="meeting?action=list">Встречи</a></li>
    <li><a href="photo?action=list">Фотографии</a></li>
    <li><a href="adminTask?action=list">Задачи</a></li>
</ul>

<hr>
<p><i>v1.0 — чистая Java, MVC, JDBC, Servlets, JSP</i></p>
</body>
</html>