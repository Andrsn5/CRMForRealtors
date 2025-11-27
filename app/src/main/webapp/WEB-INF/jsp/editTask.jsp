<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Редактировать задачу: ${task.title}</title>
    <style>
        body { font-family: Arial; background-color: #f8f9fa; margin: 20px; }
        .container { max-width: 800px; margin: 0 auto; }
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
        .submit-btn:hover {
            background: #218838;
        }
        .section-title {
            color: #495057;
            border-bottom: 2px solid #007bff;
            padding-bottom: 10px;
            margin-bottom: 30px;
        }
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
        .info-text {
            font-size: 12px;
            color: #6c757d;
            margin-top: 5px;
        }
        .quick-add-btn {
            background: #17a2b8;
            color: white;
            border: none;
            padding: 8px 12px;
            border-radius: 4px;
            cursor: pointer;
            font-size: 12px;
            margin-top: 5px;
            text-decoration: none;
            display: inline-block;
        }
        .quick-add-btn:hover {
            background: #138496;
            text-decoration: none;
            color: white;
        }
        .select-with-btn {
            display: flex;
            gap: 10px;
            align-items: flex-start;
        }
        .select-with-btn select {
            flex: 1;
        }
        .btn-container {
            display: flex;
            flex-direction: column;
            gap: 5px;
        }
    </style>
</head>
<body>
<div class="container">
    <a href="task?action=view&id=${task.id}" class="back-btn">← Назад к задаче</a>

    <div class="form-card">
        <h2 class="section-title">✏️ Редактировать задачу: ${task.title}</h2>

        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>

        <form method="post" action="task?action=edit&id=${task.id}" id="taskForm">
            <!-- Основная информация -->
            <div class="form-group">
                <label for="title" class="required">Название задачи</label>
                <input type="text" id="title" name="title" required
                       placeholder="Введите название задачи" maxlength="255"
                       value="${task.title}">
            </div>

            <div class="form-group">
                <label for="description">Описание</label>
                <textarea id="description" name="description"
                          placeholder="Опишите детали задачи">${task.description}</textarea>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="dueDate">Срок выполнения</label>
                    <input type="datetime-local" id="dueDate" name="dueDate"
                           value="${task.dueDate}">
                    <div class="info-text">Не более 1 года в будущем</div>
                </div>

                <div class="form-group">
                    <label for="priority" class="required">Приоритет</label>
                    <select id="priority" name="priority" required>
                        <option value="">-- Выберите приоритет --</option>
                        <option value="High" ${task.priority == 'High' ? 'selected' : ''}>Высокий</option>
                        <option value="Medium" ${task.priority == 'Medium' ? 'selected' : ''}>Средний</option>
                        <option value="Low" ${task.priority == 'Low' ? 'selected' : ''}>Низкий</option>
                    </select>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="status" class="required">Статус</label>
                    <select id="status" name="status" required>
                        <option value="">-- Выберите статус --</option>
                        <option value="In Progress" ${task.status == 'In Progress' ? 'selected' : ''}>В процессе</option>
                        <option value="Completed" ${task.status == 'Completed' ? 'selected' : ''}>Завершено</option>
                        <option value="Cancelled" ${task.status == 'Cancelled' ? 'selected' : ''}>Отменено</option>
                        <option value="On Hold" ${task.status == 'On Hold' ? 'selected' : ''}>В работе</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="responsibleId" class="required">Ответственный сотрудник</label>
                    <div class="select-with-btn">
                        <select id="responsibleId" name="responsibleId" required>
                            <option value="">-- Выберите сотрудника --</option>
                            <c:forEach var="employee" items="${employees}">
                                <option value="${employee.id}"
                                        <c:if test="${employee.id == task.responsibleId}">selected</c:if>>
                                    ${employee.firstName} ${employee.lastName} (${employee.position})
                                </option>
                            </c:forEach>
                        </select>
                        <div class="btn-container">
                            <button type="button" class="quick-add-btn" onclick="refreshEmployees()">🔄</button>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Связанные сущности -->
            <h3 style="color: #495057; margin: 30px 0 15px 0;">📎 Связанные объекты (опционально)</h3>

            <div class="form-row">
                <div class="form-group">
                    <label for="clientId">Клиент</label>
                    <div class="select-with-btn">
                        <select id="clientId" name="clientId">
                            <option value="">-- Не выбрано --</option>
                            <c:forEach var="client" items="${clients}">
                                <option value="${client.id}"
                                        <c:if test="${client.id == task.clientId}">selected</c:if>>
                                    ${client.firstName} ${client.lastName} (${client.clientType})
                                </option>
                            </c:forEach>
                        </select>
                        <div class="btn-container">
                            <a href="quick-client?action=add" class="quick-add-btn" target="_blank">➕</a>
                            <button type="button" class="quick-add-btn" onclick="refreshClients()">🔄</button>
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <label for="objectId">Объект недвижимости</label>
                    <div class="select-with-btn">
                        <select id="objectId" name="objectId">
                            <option value="">-- Не выбрано --</option>
                            <c:forEach var="object" items="${objects}">
                                <option value="${object.id}"
                                        <c:if test="${object.id == task.objectId}">selected</c:if>>
                                    ${object.title} - ${object.objectType} (${object.dealType})
                                </option>
                            </c:forEach>
                        </select>
                        <div class="btn-container">
                            <a href="quick-object?action=add" class="quick-add-btn" target="_blank">➕</a>
                            <button type="button" class="quick-add-btn" onclick="refreshObjects()">🔄</button>
                        </div>
                    </div>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="meetingId">Встреча</label>
                    <div class="select-with-btn">
                        <select id="meetingId" name="meetingId">
                            <option value="">-- Не выбрано --</option>
                            <c:forEach var="meeting" items="${meetings}">
                                <option value="${meeting.id}"
                                        <c:if test="${meeting.id == task.meetingId}">selected</c:if>>
                                    ${meeting.title} - ${meeting.meetingDate}
                                </option>
                            </c:forEach>
                        </select>
                        <div class="btn-container">
                            <a href="quick-meeting?action=add" class="quick-add-btn" target="_blank">➕</a>
                            <button type="button" class="quick-add-btn" onclick="refreshMeetings()">🔄</button>
                            <button type="button" class="quick-add-btn" onclick="clearSelection('meetingId')">❌</button>
                        </div>
                    </div>
                    <div class="info-text">Каждая встреча может быть привязана только к одной задаче</div>
                </div>

                <div class="form-group">
                    <label for="dealId">Сделка</label>
                    <div class="select-with-btn">
                        <select id="dealId" name="dealId">
                            <option value="">-- Не выбрано --</option>
                            <c:forEach var="deal" items="${deals}">
                                <option value="${deal.id}"
                                        <c:if test="${deal.id == task.dealId}">selected</c:if>>
                                    ${deal.dealNumber} - ${deal.status}
                                </option>
                            </c:forEach>
                        </select>
                        <div class="btn-container">
                            <a href="quick-deal?action=add" class="quick-add-btn" target="_blank">➕</a>
                            <button type="button" class="quick-add-btn" onclick="refreshDeals()">🔄</button>
                            <button type="button" class="quick-add-btn" onclick="clearSelection('dealId')">❌</button>
                        </div>
                    </div>
                    <div class="info-text">Каждая сделка может быть привязана только к одной задаче</div>
                </div>
            </div>

            <!-- Дополнительные условия -->
            <div class="form-group">
                <label for="conditionId">Дополнительное условие</label>
                <div class="select-with-btn">
                    <select id="conditionId" name="conditionId">
                        <option value="">-- Не выбрано --</option>
                        <c:forEach var="condition" items="${conditions}">
                            <option value="${condition.id}"
                                    <c:if test="${condition.id == task.conditionId}">selected</c:if>>
                                ${condition.conditionType} - ${condition.description}
                            </option>
                        </c:forEach>
                    </select>
                    <div class="btn-container">
                        <a href="quick-condition?action=add" class="quick-add-btn" target="_blank">➕</a>
                        <button type="button" class="quick-add-btn" onclick="refreshConditions()">🔄</button>
                        <button type="button" class="quick-add-btn" onclick="clearSelection('conditionId')">❌</button>
                    </div>
                </div>
                <div class="info-text">Каждое условие может быть привязано только к одной задаче</div>
            </div>

            <div class="form-group">
                <button type="submit" class="submit-btn">💾 Сохранить изменения</button>
            </div>
        </form>
    </div>
</div>

<script>
// Устанавливаем минимальную и максимальную дату согласно валидации
document.addEventListener('DOMContentLoaded', function() {
    const dueDateInput = document.getElementById('dueDate');
    const now = new Date();
    const minDate = new Date(now.getFullYear() - 1, now.getMonth(), now.getDate());
    const maxDate = new Date(now.getFullYear() + 1, now.getMonth(), now.getDate());

    dueDateInput.min = minDate.toISOString().slice(0, 16);
    dueDateInput.max = maxDate.toISOString().slice(0, 16);
});

// Функции для обновления списков
function refreshEmployees() {
    location.reload();
}

function refreshClients() {
    fetch('quick-client?action=list')
        .then(() => location.reload())
        .catch(() => location.reload());
}

function refreshObjects() {
    fetch('quick-object?action=list')
        .then(() => location.reload())
        .catch(() => location.reload());
}

function refreshMeetings() {
    fetch('quick-meeting?action=list')
        .then(() => location.reload())
        .catch(() => location.reload());
}

function refreshDeals() {
    fetch('quick-deal?action=list')
        .then(() => location.reload())
        .catch(() => location.reload());
}

function refreshConditions() {
    fetch('quick-condition?action=list')
        .then(() => location.reload())
        .catch(() => location.reload());
}

// Функция для очистки выбора
function clearSelection(fieldId) {
    document.getElementById(fieldId).value = '';
}

// Обработка создания новых сущностей в новых вкладках
document.addEventListener('DOMContentLoaded', function() {
    // Слушаем сообщения от дочерних окон
    window.addEventListener('message', function(event) {
        if (event.data && event.data.type === 'entityCreated') {
            console.log('Сущность создана:', event.data);
            // Обновляем страницу после создания сущности
            setTimeout(() => {
                location.reload();
            }, 1000);
        }
    });
});
</script>
</body>
</html>