<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Добавить клиента</title>
    <style>
        /* Ваши существующие стили остаются без изменений */
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
        .form-group select {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 14px;
            box-sizing: border-box;
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
        .form-row {
            display: flex;
            gap: 20px;
        }
        .form-row .form-group {
            flex: 1;
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
        <h2>➕ Добавить нового клиента</h2>

        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>

        <c:if test="${not empty success}">
            <div class="success">${success}</div>
        </c:if>

        <!-- ИЗМЕНИТЕ ACTION НА НОВЫЙ URL -->
        <form method="post" action="quick-client?action=add">
            <div class="form-row">
                <div class="form-group">
                    <label for="firstName" class="required">Имя</label>
                    <input type="text" id="firstName" name="firstName" required
                           placeholder="Введите имя" maxlength="100"
                           value="${param.firstName}">
                </div>

                <div class="form-group">
                    <label for="lastName" class="required">Фамилия</label>
                    <input type="text" id="lastName" name="lastName" required
                           placeholder="Введите фамилию" maxlength="100"
                           value="${param.lastName}">
                </div>
            </div>

            <div class="form-group">
                <label for="email" class="required">Email</label>
                <input type="email" id="email" name="email" required
                       placeholder="email@example.com" maxlength="255"
                       value="${param.email}">
            </div>

            <div class="form-group">
                <label for="phone">Телефон</label>
                <input type="tel" id="phone" name="phone"
                       placeholder="+7 (XXX) XXX-XX-XX"
                       value="${param.phone}">
            </div>

            <div class="form-group">
                <label for="clientType" class="required">Тип клиента</label>
                <select id="clientType" name="clientType" required>
                    <option value="">-- Выберите тип --</option>
                    <option value="customer" ${param.clientType == 'customer' ? 'selected' : ''}>customer</option>
                    <option value="seller" ${param.clientType == 'seller' ? 'selected' : ''}>seller</option>
                    <option value="tenant" ${param.clientType == 'tenant' ? 'selected' : ''}>tenant</option>
                    <option value="landlord" ${param.clientType == 'landlord' ? 'selected' : ''}>landlord</option>
                </select>
            </div>

            <div class="form-group">
                <label for="budget">Бюджет</label>
                <input type="number" id="budget" name="budget"
                       placeholder="100000" step="0.01" min="0"
                       value="${param.budget}">
            </div>

            <div class="form-group">
                <button type="submit" class="submit-btn">✅ Создать клиента</button>
            </div>
        </form>
    </div>
</div>

<script>
// Автоматическое закрытие окна после успешного создания
document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('form');
    const successMessage = document.querySelector('.success');

    if (successMessage) {
        // Если есть сообщение об успехе, закрываем окно через 2 секунды
        setTimeout(() => {
            if (window.opener) {
                window.opener.postMessage({ type: 'entityCreated', entity: 'client' }, '*');
                window.close();
            }
        }, 2000);
    }

    form.addEventListener('submit', function(e) {
        // Можно добавить валидацию перед отправкой
        const email = document.getElementById('email').value;
        if (!isValidEmail(email)) {
            e.preventDefault();
            alert('Пожалуйста, введите корректный email адрес');
            return;
        }
    });

    function isValidEmail(email) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    }
});
</script>
</body>
</html>