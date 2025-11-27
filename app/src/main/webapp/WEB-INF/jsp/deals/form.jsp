<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title><c:if test="${empty deal}">Новая</c:if><c:if test="${!empty deal}">Редактировать</c:if> сделка</title>
</head>
<body>
<%-- Отображение ошибок --%>
<c:if test="${not empty error}">
    <div style="color: red; margin-bottom: 10px; padding: 10px; border: 1px solid red;">
        <strong>Ошибка:</strong> ${error}
    </div>
</c:if>
<h2><c:if test="${empty deal}">Новая</c:if><c:if test="${!empty deal}">Редактировать</c:if> сделка</h2>
<form method="post" action="deal">
    <input type="hidden" name="id" value="${deal.id}" />
    Номер сделки: <input type="text" name="dealNumber" value="${deal.dealNumber}" required/><br/>
    ID задачи: <input type="number" name="taskId" value="${deal.taskId}"/><br/>
    Сумма сделки: <input type="number" step="0.01" name="dealAmount" value="${deal.dealAmount}"/><br/>
    Дата сделки: <input type="date" name="dealDate" value="${deal.dealDate}"/><br/>
    Комиссия: <input type="number" step="0.01" name="commission" value="${deal.commission}"/><br/>
    Статус:
    <select name="status">
        <option value="active" <c:if test="${deal.status == 'active'}">selected</c:if>>active</option>
        <option value="sold" <c:if test="${deal.status == 'sold'}">selected</c:if>>sold</option>
        <option value="withdrawn in progress" <c:if test="${deal.status == 'withdrawn in progress'}">selected</c:if>>withdrawn in progress</option>
        <option value="completed" <c:if test="${deal.status == 'completed'}">selected</c:if>>completed</option>
        <option value="cancelled" <c:if test="${deal.status == 'cancelled'}">selected</c:if>>cancelled</option>
    </select><br/>
    <button type="submit">Сохранить</button>
</form>
<a href="deal?action=list">Назад к списку</a>
<a href="index.jsp">На главную</a>
</body>
</html>