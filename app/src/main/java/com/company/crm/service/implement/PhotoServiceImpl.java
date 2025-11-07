package com.company.crm.service.implement;

import com.company.crm.dao.interfaces.PhotoDao;
import com.company.crm.dao.interfaces.ObjectDao;
import com.company.crm.model.Photo;
import com.company.crm.service.interfaces.PhotoService;
import com.company.crm.util.ValidationException;
import com.company.crm.util.ValidationUtils;

import java.util.List;
import java.util.Optional;

public class PhotoServiceImpl implements PhotoService {
    private final PhotoDao dao;
    private final ObjectDao objectDao;

    public PhotoServiceImpl(PhotoDao dao, ObjectDao objectDao) {
        this.dao = dao;
        this.objectDao = objectDao;
    }

    @Override
    public List<Photo> getAll() {
        return dao.findAll();
    }

    @Override
    public Optional<Photo> getById(int id) {
        if (id <= 0) {
            throw new ValidationException("Photo ID must be positive");
        }
        return dao.findById(id);
    }

    @Override
    public Photo create(Photo photo) {
        validatePhoto(photo);
        validateObjectExists(photo.getObjectId());
        // УБРАНА проверка уникальности Object ID - один объект может иметь много фото
        checkDisplayOrderUniqueness(photo); // Проверка уникальности порядка отображения для объекта
        return dao.save(photo);
    }

    @Override
    public void update(Photo photo) {
        if (photo.getId() == null || photo.getId() <= 0) {
            throw new ValidationException("Valid photo ID is required for update");
        }
        validatePhoto(photo);
        validateObjectExists(photo.getObjectId());
        // УБРАНА проверка уникальности Object ID при обновлении
        checkDisplayOrderUniquenessForUpdate(photo); // Проверка уникальности порядка отображения при обновлении
        dao.update(photo);
    }

    @Override
    public void delete(int id) {
        if (id <= 0) {
            throw new ValidationException("Photo ID must be positive");
        }
        dao.delete(id);
    }

    private void validatePhoto(Photo photo) {
        ValidationUtils.validateRequired(photo.getPhotoUrl(), "Photo URL");
        ValidationUtils.validateRequired(photo.getObjectId(), "Object");
        ValidationUtils.validateRequired(photo.getDisplayOrder(), "Display order");

        ValidationUtils.validateUrl(photo.getPhotoUrl());
        ValidationUtils.validateRange(photo.getDisplayOrder(), 1, 1000, "Display order");
        ValidationUtils.validateMaxLength(photo.getCaption(), 255, "Caption");

        // Валидация Object ID
        if (photo.getObjectId() != null && photo.getObjectId() <= 0) {
            throw new ValidationException("Object ID must be positive");
        }
    }

    private void validateObjectExists(Integer objectId) {
        if (objectId != null) {
            objectDao.findById(objectId)
                    .orElseThrow(() -> new ValidationException("Object not found with ID: " + objectId));
        }
    }

    // УБРАН метод checkObjectUniqueness - один объект может иметь много фото

    // УБРАН метод checkObjectUniquenessForUpdate - один объект может иметь много фото

    // Проверка уникальности порядка отображения для объекта
    private void checkDisplayOrderUniqueness(Photo photo) {
        if (photo.getObjectId() == null || photo.getDisplayOrder() == null) {
            return;
        }

        dao.findAll().stream()
                .filter(existingPhoto ->
                        photo.getObjectId().equals(existingPhoto.getObjectId()) &&
                                photo.getDisplayOrder().equals(existingPhoto.getDisplayOrder())
                )
                .findFirst()
                .ifPresent(existingPhoto -> {
                    throw new ValidationException("Display order " + photo.getDisplayOrder() +
                            " is already used for object " + photo.getObjectId() +
                            " (Photo ID: " + existingPhoto.getId() + ")");
                });
    }

    // Проверка уникальности порядка отображения при обновлении
    private void checkDisplayOrderUniquenessForUpdate(Photo photo) {
        if (photo.getObjectId() == null || photo.getDisplayOrder() == null) {
            return;
        }

        dao.findAll().stream()
                .filter(existingPhoto ->
                        !existingPhoto.getId().equals(photo.getId()) &&
                                photo.getObjectId().equals(existingPhoto.getObjectId()) &&
                                photo.getDisplayOrder().equals(existingPhoto.getDisplayOrder())
                )
                .findFirst()
                .ifPresent(existingPhoto -> {
                    throw new ValidationException("Display order " + photo.getDisplayOrder() +
                            " is already used for object " + photo.getObjectId() +
                            " (Photo ID: " + existingPhoto.getId() + ")");
                });
    }
}