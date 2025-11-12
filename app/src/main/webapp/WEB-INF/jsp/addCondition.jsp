<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Добавить условие</title>
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
            height: 100px;
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
        <h2>📋 Добавить дополнительное условие</h2>

        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>

        <c:if test="${not empty success}">
            <div class="success">${success}</div>
        </c:if>

        <form method="post" action="quick-condition?action=add">
            <div class="form-group">
                <label for="conditionType" class="required">Тип условия</label>
                <select id="conditionType" name="conditionType" required>
                    <option value="">-- Выберите тип --</option>
                    <option value="marketing" ${param.conditionType == 'marketing' ? 'selected' : ''}>Маркетинг</option>
                    <option value="administrative" ${param.conditionType == 'administrative' ? 'selected' : ''}>Административное</option>
                    <option value="customer service" ${param.conditionType == 'customer service' ? 'selected' : ''}>Обслуживание клиентов</option>
                    <option value="logistics" ${param.conditionType == 'logistics' ? 'selected' : ''}>Логистика</option>
                </select>
            </div>

            <div class="form-group">
                <label for="description" class="required">Описание</label>
                <textarea id="description" name="description" required
                          placeholder="Подробное описание условия">${param.description}</textarea>
            </div>

            <div class="form-group">
                <label for="deadline">Срок выполнения</label>
                <input type="date" id="deadline" name="deadline"
                       value="${param.deadline}">
            </div>

            <div class="form-group">
                <label for="status" class="required">Статус</label>
                <select id="status" name="status" required>
                    <option value="">-- Выберите статус --</option>
                    <option value="active" ${param.status == 'active' ? 'selected' : ''}>Активно</option>
                    <option value="completed" ${param.status == 'completed' ? 'selected' : ''}>Завершено</option>
                    <option value="canceled" ${param.status == 'canceled' ? 'selected' : ''}>Отменено</option>
                </select>
            </div>

            <div class="form-group">
                <label for="priority" class="required">Приоритет</label>
                <select id="priority" name="priority" required>
                    <option value="">-- Выберите приоритет --</option>
                    <option value="high" ${param.priority == 'high' ? 'selected' : ''}>Высокий</option>
                    <option value="medium" ${param.priority == 'medium' ? 'selected' : ''}>Средний</option>
                    <option value="low" ${param.priority == 'low' ? 'selected' : ''}>Низкий</option>
                </select>
            </div>

            <div class="form-group">
                <button type="submit" class="submit-btn">✅ Создать условие</button>
            </div>
        </form>
    </div>
</div>

<script>
document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('form');
    const successMessage = document.querySelector('.success');

    if (successMessage) {
        setTimeout(() => {
            if (window.opener) {
                window.opener.postMessage({ type: 'entityCreated', entity: 'condition' }, '*');
                window.close();
            }
        }, 2000);
    }
});
</script>
</body>
</html>