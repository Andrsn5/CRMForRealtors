<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<body>
<h2><c:if test="${empty employee}">Новый</c:if><c:if test="${!empty employee}">Редактировать</c:if> сотрудник</h2>
<form method="post" action="employee">
    <input type="hidden" name="id" value="${employee.id}" />
    Имя: <input type="text" name="firstName" value="${employee.firstName}" /><br/>
    Фамилия: <input type="text" name="lastName" value="${employee.lastName}" /><br/>
    Email: <input type="email" name="email" value="${employee.email}" /><br/>
    Телефон: <input type="text" name="phone" value="${employee.phone}" /><br/>
    Должность: <input type="text" name="position" value="${employee.position}" /><br/>
    <button type="submit">Сохранить</button>
</form>
<a href="employee?action=list">Назад</a>
</body>
</html>