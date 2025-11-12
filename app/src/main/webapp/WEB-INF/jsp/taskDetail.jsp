<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Задача: ${task.title}</title>
    <style>
        body { font-family: Arial; background-color: #f8f9fa; margin: 20px; }
        .container { max-width: 1000px; margin: 0 auto; }
        .card { background: white; border-radius: 8px; padding: 20px; margin-bottom: 20px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
        .card-header { border-bottom: 2px solid #007bff; padding-bottom: 15px; margin-bottom: 20px; }
        .back-btn { background: #6c757d; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; display: inline-block; margin-bottom: 20px; }
        .back-btn:hover { background: #545b62; }
        .entity-card { border-left: 4px solid #007bff; padding-left: 15px; margin-bottom: 15px; }
        .entity-card h4 { margin-top: 0; color: #007bff; }
        .no-data { color: #6c757d; font-style: italic; }
        .status-badge { padding: 5px 10px; border-radius: 15px; font-weight: bold; }
        .status-pending { background: #fff3cd; color: #856404; }
        .status-in_progress { background: #d1ecf1; color: #0c5460; }
        .status-completed { background: #d4edda; color: #155724; }
        .action-btn {
            background: #007bff;
            color: white;
            border: none;
            padding: 10px 15px;
            border-radius: 5px;
            cursor: pointer;
            margin-right: 10px;
            text-decoration: none;
            display: inline-block;
        }
        .action-btn:hover { background: #0056b3; }
        .complete-btn { background: #28a745; }
        .complete-btn:hover { background: #218838; }
        .success-message {
            background: #d4edda;
            color: #155724;
            padding: 10px;
            border-radius: 4px;
            margin-bottom: 15px;
            border: 1px solid #c3e6cb;
        }

        /* Стили для галереи фотографий */
        .photos-gallery {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
            gap: 15px;
            margin-top: 10px;
        }
        .photo-item {
            position: relative;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            transition: transform 0.3s ease;
        }
        .photo-item:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(0,0,0,0.15);
        }
        .photo-img {
            width: 100%;
            height: 150px;
            object-fit: cover;
            display: block;
        }
        .photo-info {
            padding: 10px;
            background: white;
        }
        .photo-caption {
            font-size: 12px;
            color: #666;
            margin-bottom: 5px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }
        .photo-order {
            font-size: 11px;
            color: #999;
            font-weight: bold;
        }
        .no-photos {
            text-align: center;
            padding: 20px;
            color: #6c757d;
            font-style: italic;
        }
        .photos-section {
            margin-top: 15px;
        }
        .photos-title {
            font-size: 14px;
            font-weight: bold;
            color: #495057;
            margin-bottom: 10px;
            border-bottom: 1px solid #e9ecef;
            padding-bottom: 5px;
        }

        /* Стили для основной информации */
        .task-info-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 15px;
            margin-top: 15px;
        }
        .info-item {
            display: flex;
            flex-direction: column;
        }
        .info-label {
            font-weight: bold;
            color: #495057;
            font-size: 14px;
            margin-bottom: 5px;
        }
        .info-value {
            color: #212529;
            font-size: 15px;
        }
    </style>
</head>
<body>
<div class="container">
    <a href="dashboard" class="back-btn">← Назад к задачам</a>

    <!-- Сообщение об успехе -->
    <c:if test="${not empty successMessage}">
        <div class="success-message">${successMessage}</div>
    </c:if>

    <!-- Основная информация о задаче -->
    <div class="card">
        <div class="card-header">
            <h1>${task.title}</h1>
            <span class="status-badge status-${task.status.toLowerCase().replace(' ', '_')}">${task.status}</span>
        </div>

        <div class="task-info-grid">
            <div class="info-item">
                <span class="info-label">Описание:</span>
                <span class="info-value">${task.description}</span>
            </div>
            <div class="info-item">
                <span class="info-label">Срок выполнения:</span>
                <span class="info-value">${task.dueDate}</span>
            </div>
            <div class="info-item">
                <span class="info-label">Приоритет:</span>
                <span class="info-value">${task.priority}</span>
            </div>
            <div class="info-item">
                <span class="info-label">Ответственный:</span>
                <span class="info-value">
                    <c:if test="${not empty responsible}">
                        ${responsible.firstName} ${responsible.lastName}
                    </c:if>
                    <c:if test="${empty responsible}">
                        <span class="no-data">Не назначен</span>
                    </c:if>
                </span>
            </div>
        </div>
    </div>

    <!-- Связанные сущности -->
    <div class="card">
        <h3>📎 Связанные объекты</h3>

        <!-- Клиент -->
        <div class="entity-card">
            <h4>👤 Клиент</h4>
            <c:if test="${not empty client}">
                <div class="task-info-grid">
                    <div class="info-item">
                        <span class="info-label">Имя:</span>
                        <span class="info-value">${client.firstName} ${client.lastName}</span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">Телефон:</span>
                        <span class="info-value">${client.phone}</span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">Email:</span>
                        <span class="info-value">${client.email}</span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">Бюджет:</span>
                        <span class="info-value">${client.budget}</span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">Тип:</span>
                        <span class="info-value">${client.clientType}</span>
                    </div>
                </div>
            </c:if>
            <c:if test="${empty client}">
                <p class="no-data">Клиент не привязан к этой задаче</p>
            </c:if>
        </div>

        <!-- Объект недвижимости -->
        <div class="entity-card">
            <h4>🏢 Объект недвижимости</h4>
            <c:if test="${not empty object}">
                <div class="task-info-grid">
                    <div class="info-item">
                        <span class="info-label">Название:</span>
                        <span class="info-value">${object.title}</span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">Адрес:</span>
                        <span class="info-value">${object.address}</span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">Тип:</span>
                        <span class="info-value">${object.objectType}</span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">Цена:</span>
                        <span class="info-value">${object.price}</span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">Площадь:</span>
                        <span class="info-value">${object.area} м²</span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">Комнат:</span>
                        <span class="info-value">${object.rooms}</span>
                    </div>
                </div>

                <!-- Галерея фотографий объекта -->
                <c:if test="${not empty objectPhotos and not objectPhotos.isEmpty()}">
                    <div class="photos-section">
                        <div class="photos-title">📷 Фотографии объекта</div>
                        <div class="photos-gallery">
                            <c:forEach var="photo" items="${objectPhotos}">
                                <div class="photo-item">
                                    <img src="${photo.photoUrl}" alt="${photo.caption}" class="photo-img"
                                         onerror="this.src='data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjAwIiBoZWlnaHQ9IjE1MCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48cmVjdCB3aWR0aD0iMjAwIiBoZWlnaHQ9IjE1MCIgZmlsbD0iI2Y4ZjlmYSIvPjx0ZXh0IHg9IjEwMCIgeT0iNzUiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxNCIgZmlsbD0iIzk5OSIgdGV4dC1hbmNob3I9Im1pZGRsZSI+0J/RgNC+0YTQtdCx0L3QviDQv9GA0L7RhNC40LvRjDwvdGV4dD48L3N2Zz4='">
                                    <div class="photo-info">
                                        <div class="photo-caption" title="${photo.caption}">
                                            <c:choose>
                                                <c:when test="${not empty photo.caption}">
                                                    ${photo.caption}
                                                </c:when>
                                                <c:otherwise>
                                                    Без подписи
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                        <div class="photo-order">Порядок: ${photo.displayOrder}</div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </c:if>
                <c:if test="${empty objectPhotos or objectPhotos.isEmpty()}">
                    <div class="no-photos">Нет фотографий для этого объекта</div>
                </c:if>

            </c:if>
            <c:if test="${empty object}">
                <p class="no-data">Объект не привязан к этой задаче</p>
            </c:if>
        </div>

        <!-- Встреча -->
        <div class="entity-card">
            <h4>📅 Встреча</h4>
            <c:if test="${not empty meeting}">
                <div class="task-info-grid">
                    <div class="info-item">
                        <span class="info-label">Название:</span>
                        <span class="info-value">${meeting.title}</span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">Дата и время:</span>
                        <span class="info-value">${meeting.meetingDate}</span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">Место:</span>
                        <span class="info-value">${meeting.location}</span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">Статус:</span>
                        <span class="info-value">${meeting.status}</span>
                    </div>
                </div>
            </c:if>
            <c:if test="${empty meeting}">
                <p class="no-data">Встреча не привязана к этой задаче</p>
            </c:if>
        </div>

        <!-- Сделка -->
        <div class="entity-card">
            <h4>💼 Сделка</h4>
            <c:if test="${not empty deal}">
                <div class="task-info-grid">
                    <div class="info-item">
                        <span class="info-label">Номер сделки:</span>
                        <span class="info-value">${deal.dealNumber}</span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">Сумма:</span>
                        <span class="info-value">${deal.dealAmount}</span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">Дата:</span>
                        <span class="info-value">${deal.dealDate}</span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">Комиссия:</span>
                        <span class="info-value">${deal.commission}</span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">Статус:</span>
                        <span class="info-value">${deal.status}</span>
                    </div>
                </div>
            </c:if>
            <c:if test="${empty deal}">
                <p class="no-data">Сделка не привязана к этой задаче</p>
            </c:if>
        </div>

        <!-- Доп условия -->
        <div class="entity-card">
            <h4>📋 Дополнительные условия</h4>
            <c:if test="${not empty condition}">
                <div class="task-info-grid">
                    <div class="info-item">
                        <span class="info-label">Тип условия:</span>
                        <span class="info-value">${condition.conditionType}</span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">Описание:</span>
                        <span class="info-value">${condition.description}</span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">Дедлайн:</span>
                        <span class="info-value">${condition.deadline}</span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">Приоритет:</span>
                        <span class="info-value">${condition.priority}</span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">Статус:</span>
                        <span class="info-value">${condition.status}</span>
                    </div>
                </div>
            </c:if>
            <c:if test="${empty condition}">
                <p class="no-data">Дополнительное условие не привязано к этой задаче</p>
            </c:if>
        </div>
    </div>

    <!-- Действия -->
    <div class="card">
        <h3>⚡ Действия</h3>
        <a href="task?action=edit&id=${task.id}" class="action-btn">✏️ Редактировать</a>

        <!-- Кнопка "Отметить выполненной" -->
        <c:if test="${task.status != 'Completed' and canComplete}">
            <form id="completeForm" method="post" action="task" style="display: inline;">
                <input type="hidden" name="action" value="complete">
                <input type="hidden" name="id" value="${task.id}">
                <button type="button" onclick="markComplete(${task.id})" class="action-btn complete-btn">
                    ✅ Отметить выполненной
                </button>
            </form>
        </c:if>

        <c:if test="${task.status == 'Completed'}">
            <button disabled class="action-btn complete-btn">✅ Задача завершена</button>
        </c:if>

        <c:if test="${task.status != 'Completed' and not canComplete}">
            <button disabled class="action-btn complete-btn" title="Вы не можете выполнить эту задачу">
                ✅ Отметить выполненной
            </button>
        </c:if>
    </div>
</div>

<script>
function markComplete(taskId) {
    if (confirm('Вы уверены, что хотите отметить задачу как выполненную?')) {
        document.getElementById('completeForm').submit();
    }
}
</script>
</body>
</html>