<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Добавить встречу</title>
    <style>
        body { font-family: Arial; background-color: #f8f9fa; margin: 20px; }
        .container { max-width: 600px; margin: 0 auto; }
        .back-btn {
            background: #6c757d;
            color: white;
            padding: 10px 20px;
            text-decoration: none;
            border-radius: 5px;
            display: inline-block;
            margin-bottom: 20px;
        }
        .back-btn:hover { background: #545b62; }
        .form-card {
            background: white;
            border-radius: 8px;
            padding: 30px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .form-group {
            margin-bottom: 20px;
        }
        .form-group label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
            color: #333;
        }
        .form-group input,
        .form-group select,
        .form-group textarea {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 14px;
            box-sizing: border-box;
        }
        .form-group textarea {
            height: 80px;
            resize: vertical;
        }
        .submit-btn {
            background: #28a745;
            color: white;
            border: none;
            padding: 12px 30px;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
        }
        .submit-btn:hover { background: #218838; }
        .error {
            color: #dc3545;
            background: #f8d7da;
            padding: 10px;
            border-radius: 4px;
            margin-bottom: 20px;
        }
        .required::after {
            content: " *";
            color: red;
        }
        .success {
            color: #155724;
            background: #d4edda;
            padding: 10px;
            border-radius: 4px;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
<div class="container">
    <a href="javascript:window.close()" class="back-btn">← Закрыть</a>

    <div class="form-card">
        <h2>📅 Добавить встречу</h2>

        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>

        <c:if test="${not empty success}">
            <div class="success">${success}</div>
        </c:if>

        <form method="post" action="quick-meeting?action=add">
            <div class="form-group">
                <label for="title" class="required">Название встречи</label>
                <input type="text" id="title" name="title" required
                       placeholder="Например: Просмотр квартиры" maxlength="255"
                       value="${param.title}">
            </div>

            <div class="form-group">
                <label for="meetingDate" class="required">Дата и время</label>
                <input type="datetime-local" id="meetingDate" name="meetingDate" required
                       value="${param.meetingDate}">
            </div>

            <div class="form-group">
                <label for="location" class="required">Место</label>
                <input type="text" id="location" name="location" required
                       placeholder="Адрес или место встречи" maxlength="255"
                       value="${param.location}">
            </div>

            <div class="form-group">
                <label for="status" class="required">Статус</label>
                <select id="status" name="status" required>
                    <option value="">-- Выберите статус --</option>
                    <option value="scheduled" ${param.status == 'scheduled' ? 'selected' : ''}>Запланирована</option>
                    <option value="completed" ${param.status == 'completed' ? 'selected' : ''}>Завершена</option>
                    <option value="cancelled" ${param.status == 'cancelled' ? 'selected' : ''}>Отменена</option>
                </select>
            </div>

            <div class="form-group">
                <label for="clientId" class="required">Клиент</label>
                <select id="clientId" name="clientId" required>
                    <option value="">-- Выберите клиента --</option>
                    <c:forEach var="client" items="${clients}">
                        <option value="${client.id}" ${param.clientId == client.id ? 'selected' : ''}>
                            ${client.firstName} ${client.lastName}
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <button type="submit" class="submit-btn">✅ Создать встречу</button>
            </div>
        </form>
    </div>
</div>

<script>
document.addEventListener('DOMContentLoaded', function() {
    // Устанавливаем минимальную и максимальную дату
    const meetingDateInput = document.getElementById('meetingDate');
    const now = new Date();
    const minDate = new Date(now.getFullYear() - 1, now.getMonth(), now.getDate());
    const maxDate = new Date(now.getFullYear() + 1, now.getMonth(), now.getDate());

    meetingDateInput.min = minDate.toISOString().slice(0, 16);
    meetingDateInput.max = maxDate.toISOString().slice(0, 16);

    const form = document.querySelector('form');
    const successMessage = document.querySelector('.success');

    if (successMessage) {
        setTimeout(() => {
            if (window.opener) {
                window.opener.postMessage({ type: 'entityCreated', entity: 'meeting' }, '*');
                window.close();
            }
        }, 2000);
    }
});
</script>
</body>
</html>