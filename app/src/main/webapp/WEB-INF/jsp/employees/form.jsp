<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title><c:if test="${empty employee}">Новый</c:if><c:if test="${!empty employee}">Редактировать</c:if> сотрудник</title>
</head>
<body>
<%-- Отображение ошибок --%>
<c:if test="${not empty error}">
    <div style="color: red; margin-bottom: 10px; padding: 10px; border: 1px solid red;">
        <strong>Ошибка:</strong> ${error}
    </div>
</c:if>
<h2><c:if test="${empty employee}">Новый</c:if><c:if test="${!empty employee}">Редактировать</c:if> сотрудник</h2>
<form method="post" action="employee">
    <input type="hidden" name="id" value="${employee.id}" />
    Имя: <input type="text" name="firstName" value="${employee.firstName}" /><br/>
    Фамилия: <input type="text" name="lastName" value="${employee.lastName}" /><br/>
    Email: <input type="email" name="email" value="${employee.email}" /><br/>
    Телефон: <input type="text" name="phone" value="${employee.phone}" /><br/>
    Должность:<select name="position">
                      <option value="Analyst" <c:if test="${employee.position == 'Analyst'}">selected</c:if>>Analyst</option>
                      <option value="Assistant" <c:if test="${employee.position == 'Assistant'}">selected</c:if>>Assistant</option>
                      <option value="Sales Manager" <c:if test="${employee.position == 'Sales Manager'}">selected</c:if>>Sales Manager</option>
                      <option value="Real Estate Agent" <c:if test="${employee.position == 'Real Estate Agent'}">selected</c:if>>Real Estate Agent</option>
                      <option value="Senior Manager" <c:if test="${employee.position == 'Senior Manager'}">selected</c:if>>Senior Manager</option>
                  </select><br/>
    <button type="submit">Сохранить</button>
</form>
<br>
<a href="employee?action=list">Назад к списку</a> |
<a href="index.jsp">На главную</a>
</body>
</html>