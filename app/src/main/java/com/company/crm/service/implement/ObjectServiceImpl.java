package com.company.crm.service.implement;

import com.company.crm.dao.interfaces.ObjectDao;
import com.company.crm.model.Object;
import com.company.crm.service.interfaces.ObjectService;
import com.company.crm.util.ValidationException;
import com.company.crm.util.ValidationUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ObjectServiceImpl implements ObjectService {
    private final ObjectDao dao;

    public ObjectServiceImpl(ObjectDao dao) {
        this.dao = dao;
    }

    @Override
    public List<Object> getAll() {
        return dao.findAll();
    }

    @Override
    public Optional<Object> getById(int id) {
        if (id <= 0) {
            throw new ValidationException("Property ID must be positive");
        }
        return dao.findById(id);
    }

    @Override
    public Object create(Object object) {
        validateObject(object);
        checkAddressUniqueness(object); // Проверка уникальности названия
        return dao.save(object);
    }

    @Override
    public void update(Object object) {
        if (object.getId() == null || object.getId() <= 0) {
            throw new ValidationException("Valid property ID is required for update");
        }
        validateObject(object);
        checkAddressUniquenessForUpdate(object); // Проверка уникальности названия при обновлении
        dao.update(object);
    }

    @Override
    public void delete(int id) {
        if (id <= 0) {
            throw new ValidationException("Property ID must be positive");
        }
        dao.delete(id);
    }

    private void validateObject(Object object) {
        // Обязательные поля
        ValidationUtils.validateRequired(object.getTitle(), "Title");
        ValidationUtils.validateRequired(object.getObjectType(), "Object type");
        ValidationUtils.validateRequired(object.getDealType(), "Deal type");
        ValidationUtils.validateRequired(object.getStatus(), "Status");
        ValidationUtils.validateRequired(object.getPrice(), "Price");

        // Валидация длины строк
        ValidationUtils.validateMaxLength(object.getTitle(), 255, "Title");
        ValidationUtils.validateMaxLength(object.getObjectType(), 100, "Object type");
        ValidationUtils.validateMaxLength(object.getDealType(), 50, "Deal type");
        ValidationUtils.validateMaxLength(object.getStatus(), 50, "Status");
        ValidationUtils.validateMaxLength(object.getAddress(), 500, "Address");

        // Валидация числовых полей
        ValidationUtils.validatePositive(object.getPrice(), "Price");
        ValidationUtils.validateMinValue(object.getPrice(), new BigDecimal("1000"), "Price");

        if (object.getArea() != null) {
            ValidationUtils.validatePositive(object.getArea(), "Area");
            ValidationUtils.validateMinValue(object.getArea(), new BigDecimal("1"), "Area");
        }

        if (object.getRooms() != null) {
            ValidationUtils.validateRange(object.getRooms(), 0, 50, "Rooms");
        }

        if (object.getBathrooms() != null) {
            ValidationUtils.validateRange(object.getBathrooms(), 0, 20, "Bathrooms");
        }

        // Валидация типов
        validateObjectType(object.getObjectType());
        validateDealType(object.getDealType());
        validateStatus(object.getStatus());
    }

    private void validateObjectType(String objectType) {
        if (objectType == null || objectType.trim().isEmpty()) {
            throw new ValidationException("Object type is required");
        }

        String[] validTypes = {"Apartment", "House", "Commercial", "Land", "Villa"};

        boolean isValid = false;
        for (String validType : validTypes) {
            if (validType.equalsIgnoreCase(objectType.trim())) {
                isValid = true;
                break;
            }
        }

        if (!isValid) {
            throw new ValidationException("Invalid object type. Allowed values: Apartment, House, Commercial, Land, Villa");
        }
    }

    private void validateDealType(String dealType) {
        if (dealType == null || dealType.trim().isEmpty()) {
            throw new ValidationException("Deal type is required");
        }

        String[] validDealTypes = {"Sale", "Rent"};

        boolean isValid = false;
        for (String validDealType : validDealTypes) {
            if (validDealType.equalsIgnoreCase(dealType.trim())) {
                isValid = true;
                break;
            }
        }

        if (!isValid) {
            throw new ValidationException("Invalid deal type. Allowed values: Sale, Rent");
        }
    }

    private void validateStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new ValidationException("Status is required");
        }

        String[] validStatuses = {"Available", "Sold", "Rented", "Reserved", "Draft"};

        boolean isValid = false;
        for (String validStatus : validStatuses) {
            if (validStatus.equalsIgnoreCase(status.trim())) {
                isValid = true;
                break;
            }
        }

        if (!isValid) {
            throw new ValidationException("Invalid status. Allowed values: Available, Sold, Rented, Reserved, Draft");
        }
    }

    // ПРОВЕРКА: Уникальность только для адреса (создание)
    private void checkAddressUniqueness(Object object) {
        if (object.getAddress() == null || object.getAddress().trim().isEmpty()) {
            return;
        }

        dao.findAll().stream()
                .filter(existingObject ->
                        object.getAddress().equalsIgnoreCase(existingObject.getAddress())
                )
                .findFirst()
                .ifPresent(existingObject -> {
                    throw new ValidationException("Property with address '" + object.getAddress() + "' already exists (ID: " + existingObject.getId() + ")");
                });
    }

    // ПРОВЕРКА: Уникальность только для адреса (обновление)
    private void checkAddressUniquenessForUpdate(Object object) {
        if (object.getAddress() == null || object.getAddress().trim().isEmpty()) {
            return;
        }

        dao.findAll().stream()
                .filter(existingObject ->
                        !existingObject.getId().equals(object.getId()) &&
                                object.getAddress().equalsIgnoreCase(existingObject.getAddress())
                )
                .findFirst()
                .ifPresent(existingObject -> {
                    throw new ValidationException("Property with address '" + object.getAddress() + "' already exists (ID: " + existingObject.getId() + ")");
                });
    }

    // Дополнительные методы для бизнес-логики
    public List<Object> getByObjectType(String objectType) {
        validateObjectType(objectType);
        return dao.findAll().stream()
                .filter(obj -> objectType.equalsIgnoreCase(obj.getObjectType()))
                .toList();
    }

    public List<Object> getByDealType(String dealType) {
        validateDealType(dealType);
        return dao.findAll().stream()
                .filter(obj -> dealType.equalsIgnoreCase(obj.getDealType()))
                .toList();
    }

    public List<Object> getByStatus(String status) {
        validateStatus(status);
        return dao.findAll().stream()
                .filter(obj -> status.equalsIgnoreCase(obj.getStatus()))
                .toList();
    }

    public List<Object> getAvailableProperties() {
        return dao.findAll().stream()
                .filter(obj -> "Available".equalsIgnoreCase(obj.getStatus()))
                .toList();
    }

    public List<Object> searchByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        if (minPrice == null || maxPrice == null) {
            throw new ValidationException("Both minPrice and maxPrice are required");
        }
        if (minPrice.compareTo(maxPrice) > 0) {
            throw new ValidationException("Min price cannot be greater than max price");
        }

        return dao.findAll().stream()
                .filter(obj -> obj.getPrice() != null)
                .filter(obj -> obj.getPrice().compareTo(minPrice) >= 0 && obj.getPrice().compareTo(maxPrice) <= 0)
                .toList();
    }

    public List<Object> searchByAreaRange(BigDecimal minArea, BigDecimal maxArea) {
        if (minArea == null || maxArea == null) {
            throw new ValidationException("Both minArea and maxArea are required");
        }
        if (minArea.compareTo(maxArea) > 0) {
            throw new ValidationException("Min area cannot be greater than max area");
        }

        return dao.findAll().stream()
                .filter(obj -> obj.getArea() != null)
                .filter(obj -> obj.getArea().compareTo(minArea) >= 0 && obj.getArea().compareTo(maxArea) <= 0)
                .toList();
    }
}