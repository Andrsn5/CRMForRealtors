<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Добавить сделку</title>
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
        <h2>🤝 Добавить сделку</h2>

        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>

        <c:if test="${not empty success}">
            <div class="success">${success}</div>
        </c:if>

        <form method="post" action="quick-deal?action=add">
            <div class="form-group">
                <label for="dealNumber" class="required">Номер сделки</label>
                <input type="text" id="dealNumber" name="dealNumber" required
                       placeholder="DEAL-2024-001" maxlength="100"
                       value="${param.dealNumber}">
            </div>

            <div class="form-group">
                <label for="dealAmount" class="required">Сумма сделки</label>
                <input type="number" id="dealAmount" name="dealAmount" required
                       placeholder="1000000" step="0.01" min="1000"
                       value="${param.dealAmount}">
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="dealDate">Дата сделки</label>
                    <input type="date" id="dealDate" name="dealDate"
                           value="${param.dealDate}">
                </div>

                <div class="form-group">
                    <label for="commission">Комиссия</label>
                    <input type="number" id="commission" name="commission"
                           placeholder="50000" step="0.01" min="0"
                           value="${param.commission}">
                </div>
            </div>

            <div class="form-group">
                <label for="status" class="required">Статус</label>
                <select id="status" name="status" required>
                    <option value="">-- Выберите статус --</option>
                    <option value="active" ${param.status == 'active' ? 'selected' : ''}>Активна</option>
                    <option value="sold" ${param.status == 'sold' ? 'selected' : ''}>Продана</option>
                    <option value="completed" ${param.status == 'completed' ? 'selected' : ''}>Завершена</option>
                    <option value="withdrawn in progress" ${param.status == 'withdrawn in progress' ? 'selected' : ''}>В процессе отмены</option>
                    <option value="cancelled" ${param.status == 'cancelled' ? 'selected' : ''}>Отменена</option>
                </select>
            </div>

            <div class="form-group">
                <button type="submit" class="submit-btn">✅ Создать сделку</button>
            </div>
        </form>
    </div>
</div>

<script>
document.addEventListener('DOMContentLoaded', function() {
    // Устанавливаем сегодняшнюю дату по умолчанию
    const dealDateInput = document.getElementById('dealDate');
    if (!dealDateInput.value) {
        const today = new Date().toISOString().split('T')[0];
        dealDateInput.value = today;
    }

    const form = document.querySelector('form');
    const successMessage = document.querySelector('.success');

    if (successMessage) {
        setTimeout(() => {
            if (window.opener) {
                window.opener.postMessage({ type: 'entityCreated', entity: 'deal' }, '*');
                window.close();
            }
        }, 2000);
    }
});
</script>
</body>
</html>