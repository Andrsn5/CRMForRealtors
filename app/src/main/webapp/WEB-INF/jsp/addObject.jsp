<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Добавить объект недвижимости</title>
    <style>
        body { font-family: Arial; background-color: #f8f9fa; margin: 20px; }
        .container { max-width: 700px; margin: 0 auto; }
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
        .photo-upload-section {
            border: 2px dashed #ddd;
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 20px;
            background: #f8f9fa;
        }
        .photo-preview {
            display: flex;
            flex-wrap: wrap;
            gap: 10px;
            margin-top: 15px;
        }
        .photo-preview-item {
            position: relative;
            width: 120px;
            height: 140px;
            border: 1px solid #ddd;
            border-radius: 4px;
            overflow: hidden;
            background: white;
            padding: 5px;
        }
        .photo-preview-item img {
            width: 100%;
            height: 80px;
            object-fit: cover;
            border-radius: 3px;
        }
        .photo-preview-info {
            padding: 5px;
            font-size: 11px;
            color: #666;
        }
        .remove-photo {
            position: absolute;
            top: 5px;
            right: 5px;
            background: rgba(220, 53, 69, 0.9);
            color: white;
            border: none;
            border-radius: 50%;
            width: 20px;
            height: 20px;
            cursor: pointer;
            font-size: 12px;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .add-photo-btn {
            background: #17a2b8;
            color: white;
            border: none;
            padding: 8px 15px;
            border-radius: 4px;
            cursor: pointer;
            margin-top: 10px;
            font-size: 14px;
        }
        .add-photo-btn:hover {
            background: #138496;
        }
        .photo-input-group {
            margin-bottom: 15px;
            padding: 15px;
            border: 1px solid #e9ecef;
            border-radius: 6px;
            background: white;
        }
        .photo-input-row {
            display: flex;
            gap: 10px;
            align-items: center;
            margin-bottom: 10px;
        }
        .photo-file-input {
            flex: 2;
        }
        .photo-order-input {
            flex: 1;
            max-width: 80px;
        }
        .photo-caption {
            width: 100%;
            margin-top: 5px;
        }
        .photo-controls {
            display: flex;
            gap: 5px;
            margin-top: 10px;
        }
        .move-up-btn, .move-down-btn {
            background: #6c757d;
            color: white;
            border: none;
            border-radius: 4px;
            padding: 4px 8px;
            cursor: pointer;
            font-size: 12px;
        }
        .move-up-btn:hover, .move-down-btn:hover {
            background: #545b62;
        }
        .remove-btn {
            background: #dc3545;
            color: white;
            border: none;
            border-radius: 4px;
            padding: 4px 8px;
            cursor: pointer;
            font-size: 12px;
        }
        .remove-btn:hover {
            background: #c82333;
        }
        .photo-counter {
            font-size: 12px;
            color: #6c757d;
            margin-bottom: 10px;
        }
    </style>
</head>
<body>
<div class="container">
    <a href="javascript:window.close()" class="back-btn">← Закрыть</a>

    <div class="form-card">
        <h2>🏠 Добавить объект недвижимости</h2>

        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>

        <c:if test="${not empty success}">
            <div class="success">${success}</div>
        </c:if>

        <form method="post" action="quick-object?action=add" enctype="multipart/form-data" id="objectForm">
            <!-- Основная информация об объекте -->
            <div class="form-group">
                <label for="title" class="required">Название</label>
                <input type="text" id="title" name="title" required
                       placeholder="Например: 3-комн. квартира в центре" maxlength="255"
                       value="${param.title}">
            </div>

            <div class="form-group">
                <label for="address">Адрес</label>
                <textarea id="address" name="address"
                          placeholder="Полный адрес объекта" maxlength="500">${param.address}</textarea>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="objectType" class="required">Тип объекта</label>
                    <select id="objectType" name="objectType" required>
                        <option value="">-- Выберите тип --</option>
                        <option value="Apartment" ${param.objectType == 'Apartment' ? 'selected' : ''}>Квартира</option>
                        <option value="House" ${param.objectType == 'House' ? 'selected' : ''}>Дом</option>
                        <option value="Commercial" ${param.objectType == 'Commercial' ? 'selected' : ''}>Коммерческая</option>
                        <option value="Land" ${param.objectType == 'Land' ? 'selected' : ''}>Земельный участок</option>
                        <option value="Villa" ${param.objectType == 'Villa' ? 'selected' : ''}>Вилла</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="dealType" class="required">Тип сделки</label>
                    <select id="dealType" name="dealType" required>
                        <option value="">-- Выберите тип --</option>
                        <option value="Sale" ${param.dealType == 'Sale' ? 'selected' : ''}>Продажа</option>
                        <option value="Rent" ${param.dealType == 'Rent' ? 'selected' : ''}>Аренда</option>
                    </select>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="price" class="required">Цена</label>
                    <input type="number" id="price" name="price" required
                           placeholder="1000000" step="0.01" min="1000"
                           value="${param.price}">
                </div>

                <div class="form-group">
                    <label for="status" class="required">Статус</label>
                    <select id="status" name="status" required>
                        <option value="">-- Выберите статус --</option>
                        <option value="Available" ${param.status == 'Available' ? 'selected' : ''}>Доступно</option>
                        <option value="Sold" ${param.status == 'Sold' ? 'selected' : ''}>Продано</option>
                        <option value="Rented" ${param.status == 'Rented' ? 'selected' : ''}>Сдано</option>
                        <option value="Reserved" ${param.status == 'Reserved' ? 'selected' : ''}>Забронировано</option>
                        <option value="Draft" ${param.status == 'Draft' ? 'selected' : ''}>Черновик</option>
                    </select>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="area">Площадь (м²)</label>
                    <input type="number" id="area" name="area"
                           placeholder="75.5" step="0.1" min="1"
                           value="${param.area}">
                </div>

                <div class="form-group">
                    <label for="rooms">Количество комнат</label>
                    <input type="number" id="rooms" name="rooms"
                           placeholder="3" min="0" max="50"
                           value="${param.rooms}">
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="bathrooms">Количество санузлов</label>
                    <input type="number" id="bathrooms" name="bathrooms"
                           placeholder="2" min="0" max="20"
                           value="${param.bathrooms}">
                </div>
            </div>

            <!-- Секция загрузки фотографий -->
            <div class="photo-upload-section">
                <h3>📷 Фотографии объекта</h3>
                <div class="photo-counter" id="photoCounter">Фотографий: 0</div>
                <div id="photoUploads">
                    <!-- Поля для загрузки фото будут добавляться динамически -->
                </div>
                <button type="button" class="add-photo-btn" onclick="addPhotoField()">+ Добавить фото</button>
                <div id="photoPreview" class="photo-preview"></div>
            </div>

            <div class="form-group">
                <button type="submit" class="submit-btn">✅ Создать объект с фото</button>
            </div>
        </form>
    </div>
</div>

<script>
let photoCount = 0;

// Автоматическое закрытие окна после успешного создания
document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('form');
    const successMessage = document.querySelector('.success');

    if (successMessage) {
        setTimeout(() => {
            if (window.opener) {
                window.opener.postMessage({ type: 'entityCreated', entity: 'object' }, '*');
                window.close();
            }
        }, 2000);
    }

    form.addEventListener('submit', function(e) {
        // Валидация цены
        const price = document.getElementById('price').value;
        if (price && parseFloat(price) < 1000) {
            e.preventDefault();
            alert('Цена должна быть не менее 1000');
            return;
        }

        // Валидация площади
        const area = document.getElementById('area').value;
        if (area && parseFloat(area) < 1) {
            e.preventDefault();
            alert('Площадь должна быть не менее 1 м²');
            return;
        }

        // Проверяем порядок отображения
        const orderInputs = document.querySelectorAll('input[name^="photoOrder"]');
        const orders = new Set();
        let hasDuplicates = false;

        orderInputs.forEach(input => {
            const order = parseInt(input.value);
            if (orders.has(order)) {
                hasDuplicates = true;
                input.style.borderColor = '#dc3545';
            } else {
                orders.add(order);
                input.style.borderColor = '';
            }
        });

        if (hasDuplicates) {
            e.preventDefault();
            alert('Порядок отображения должен быть уникальным для каждого фото');
            return;
        }

        // Проверяем, что загружены фото (опционально)
        const photoInputs = document.querySelectorAll('input[type="file"]');
        let hasPhotos = false;
        photoInputs.forEach(input => {
            if (input.files.length > 0) {
                hasPhotos = true;
            }
        });

        if (!hasPhotos) {
            if (!confirm('Вы не добавили фотографии. Продолжить без фото?')) {
                e.preventDefault();
                return;
            }
        }
    });

    // Добавляем первое поле для фото при загрузке
    addPhotoField();
});

function addPhotoField() {
    const photoUploads = document.getElementById('photoUploads');
    const photoIndex = photoCount++;

    const photoGroup = document.createElement('div');
    photoGroup.className = 'photo-input-group';
    photoGroup.innerHTML = `
        <div class="photo-input-row">
            <input type="file" name="photos" accept="image/*" class="photo-file-input" onchange="previewPhoto(this, ${photoIndex})">
            <input type="number" name="photoOrder${photoIndex}" class="photo-order-input" placeholder="Порядок" value="${photoIndex + 1}" min="1" max="1000" required>
        </div>
        <input type="text" name="photoCaption${photoIndex}" placeholder="Подпись к фото (необязательно)" class="photo-caption" maxlength="255">
        <div class="photo-controls">
            <button type="button" class="move-up-btn" onclick="movePhotoUp(this)">↑</button>
            <button type="button" class="move-down-btn" onclick="movePhotoDown(this)">↓</button>
            <button type="button" class="remove-btn" onclick="removePhotoField(this)">Удалить</button>
        </div>
    `;

    photoUploads.appendChild(photoGroup);
    updatePhotoCounter();
}

function removePhotoField(button) {
    const photoGroup = button.closest('.photo-input-group');
    const fileInput = photoGroup.querySelector('input[type="file"]');
    const photoIndex = Array.from(document.querySelectorAll('input[type="file"]')).indexOf(fileInput);

    // Удаляем превью если есть
    const preview = document.querySelector(`.photo-preview-item[data-index="${photoIndex}"]`);
    if (preview) {
        preview.remove();
    }

    photoGroup.remove();
    reindexPhotoFields();
    updatePhotoCounter();
}

function reindexPhotoFields() {
    const fileInputs = document.querySelectorAll('input[type="file"]');
    const orderInputs = document.querySelectorAll('input[name^="photoOrder"]');
    const captionInputs = document.querySelectorAll('input[name^="photoCaption"]');

    fileInputs.forEach((input, index) => {
        input.setAttribute('onchange', `previewPhoto(this, ${index})`);
    });

    orderInputs.forEach((input, index) => {
        input.name = `photoOrder${index}`;
        // Обновляем значение порядка если оно не задано
        if (!input.value || input.value === '') {
            input.value = index + 1;
        }
    });

    captionInputs.forEach((input, index) => {
        input.name = `photoCaption${index}`;
    });

    // Обновляем data-index в превью
    const previewItems = document.querySelectorAll('.photo-preview-item');
    previewItems.forEach((item, index) => {
        item.setAttribute('data-index', index);
        // Обновляем информацию в превью
        const orderInput = document.querySelector(`input[name="photoOrder${index}"]`);
        const captionInput = document.querySelector(`input[name="photoCaption${index}"]`);
        if (orderInput && captionInput) {
            const infoDiv = item.querySelector('.photo-preview-info');
            if (infoDiv) {
                infoDiv.innerHTML = `Порядок: ${orderInput.value}<br>${captionInput.value || 'Без подписи'}`;
            }
        }
    });
}

function previewPhoto(input, index) {
    const file = input.files[0];
    if (file) {
        const reader = new FileReader();
        reader.onload = function(e) {
            const previewContainer = document.getElementById('photoPreview');

            // Удаляем существующее превью для этого индекса
            const existingPreview = document.querySelector(`.photo-preview-item[data-index="${index}"]`);
            if (existingPreview) {
                existingPreview.remove();
            }

            const orderInput = document.querySelector(`input[name="photoOrder${index}"]`);
            const captionInput = document.querySelector(`input[name="photoCaption${index}"]`);

            const previewItem = document.createElement('div');
            previewItem.className = 'photo-preview-item';
            previewItem.setAttribute('data-index', index);
            previewItem.innerHTML = `
                <img src="${e.target.result}" alt="Preview">
                <div class="photo-preview-info">
                    Порядок: ${orderInput ? orderInput.value : index + 1}<br>
                    ${captionInput ? (captionInput.value || 'Без подписи') : 'Без подписи'}
                </div>
                <button type="button" class="remove-photo" onclick="removePhotoPreview(${index})">×</button>
            `;

            previewContainer.appendChild(previewItem);
        };
        reader.readAsDataURL(file);
    }
    updatePhotoCounter();
}

function removePhotoPreview(index) {
    // Удаляем превью
    const preview = document.querySelector(`.photo-preview-item[data-index="${index}"]`);
    if (preview) {
        preview.remove();
    }

    // Очищаем поле ввода файла
    const fileInputs = document.querySelectorAll('input[type="file"]');
    if (fileInputs[index]) {
        fileInputs[index].value = '';
    }

    // Очищаем поле подписи
    const captionInput = document.querySelector(`input[name="photoCaption${index}"]`);
    if (captionInput) {
        captionInput.value = '';
    }

    updatePhotoCounter();
}

function movePhotoUp(button) {
    const currentGroup = button.closest('.photo-input-group');
    const prevGroup = currentGroup.previousElementSibling;

    if (prevGroup && prevGroup.classList.contains('photo-input-group')) {
        currentGroup.parentNode.insertBefore(currentGroup, prevGroup);
        reindexPhotoFields();
    }
}

function movePhotoDown(button) {
    const currentGroup = button.closest('.photo-input-group');
    const nextGroup = currentGroup.nextElementSibling;

    if (nextGroup && nextGroup.classList.contains('photo-input-group')) {
        currentGroup.parentNode.insertBefore(nextGroup, currentGroup);
        reindexPhotoFields();
    }
}

function updatePhotoCounter() {
    const fileInputs = document.querySelectorAll('input[type="file"]');
    const photoCount = Array.from(fileInputs).filter(input => input.files.length > 0).length;
    document.getElementById('photoCounter').textContent = `Фотографий: ${photoCount}`;
}
</script>
</body>
</html>