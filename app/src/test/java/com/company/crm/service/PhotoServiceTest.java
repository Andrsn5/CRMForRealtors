package com.company.crm.service;

import com.company.crm.config.AppConfig;
import com.company.crm.model.Object;
import com.company.crm.model.Photo;
import com.company.crm.service.interfaces.ObjectService;
import com.company.crm.service.interfaces.PhotoService;
import com.company.crm.util.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PhotoServiceTest {
    private PhotoService photoService;
    private ObjectService objectService;
    private AppConfig appConfig;

    @BeforeEach
    void setUp() {
        appConfig = new AppConfig();
        photoService = appConfig.getPhotoService();
        objectService = appConfig.getObjectService();
    }

    @Test
    void testCreatePhoto_WithObjectId_ShouldSuccess() {
        Object testObject = createTestObject("Test Object for Photo");
        Photo photo = createTestPhoto(testObject.getId(), 1);

        Photo created = photoService.create(photo);

        assertNotNull(created);
        assertEquals("https://example.com/photo" + testObject.getId() + "_1.jpg", created.getPhotoUrl());
        assertEquals(1, created.getDisplayOrder());
        assertEquals(testObject.getId(), created.getObjectId());
    }

    @Test
    void testCreatePhoto_WithNullObjectId_ShouldThrowValidationException() {
        Photo photo = createTestPhoto(null, 1);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> photoService.create(photo));

        assertEquals("Object is required", exception.getMessage());
    }

    @Test
    void testCreatePhoto_WithNegativeDisplayOrder_ShouldThrowValidationException() {
        Object testObject = createTestObject("Test Object Negative Display");
        Photo photo = createTestPhoto(testObject.getId(), -1);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> photoService.create(photo));

        assertTrue(exception.getMessage().contains("Display order must be between 1 and 1000"));
    }

    @Test
    void testCreatePhoto_WithZeroDisplayOrder_ShouldThrowValidationException() {
        Object testObject = createTestObject("Test Object Zero Display");
        Photo photo = createTestPhoto(testObject.getId(), 0);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> photoService.create(photo));

        assertTrue(exception.getMessage().contains("Display order must be between 1 and 1000"));
    }

    @Test
    void testUpdatePhoto_ChangeCaptionAndOrder_ShouldUpdate() {
        Object testObject = createTestObject("Test Object for Update");
        Photo photo = photoService.create(createTestPhoto(testObject.getId(), 1));
        photo.setCaption("Updated Caption");
        photo.setDisplayOrder(5);

        photoService.update(photo);
        Photo updated = photoService.getById(photo.getId()).orElse(null);

        assertNotNull(updated);
        assertEquals("Updated Caption", updated.getCaption());
        assertEquals(5, updated.getDisplayOrder());
    }



    @Test
    void testMultiplePhotos_DifferentObjectIds_ShouldWork() {
        Object object1 = createTestObject("Test Object 1");
        Object object2 = createTestObject("Test Object 2");
        Object object3 = createTestObject("Test Object 3");

        int initialCount = photoService.getAll().size();

        photoService.create(createTestPhoto(object1.getId(), 1));
        photoService.create(createTestPhoto(object2.getId(), 1));
        photoService.create(createTestPhoto(object3.getId(), 1));

        List<Photo> allPhotos = photoService.getAll();

        assertEquals(initialCount + 3, allPhotos.size());
    }

    @Test
    void testPhotoWithInvalidUrl_ShouldThrowValidationException() {
        Object testObject = createTestObject("Test Object Invalid URL");
        Photo photo = createTestPhoto(testObject.getId(), 1);
        photo.setPhotoUrl("invalid-url");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> photoService.create(photo));

        assertTrue(exception.getMessage().contains("Invalid URL format"));
    }

    @Test
    void testPhotoWithValidUrl_ShouldSuccess() {
        Object testObject = createTestObject("Test Object Valid URL");
        Photo photo = createTestPhoto(testObject.getId(), 1);
        photo.setPhotoUrl("https://example.com/valid-photo.jpg");

        assertDoesNotThrow(() -> photoService.create(photo));
    }

    @Test
    void testPhotoWithHttpUrl_ShouldSuccess() {
        Object testObject = createTestObject("Test Object HTTP URL");
        Photo photo = createTestPhoto(testObject.getId(), 1);
        photo.setPhotoUrl("http://example.com/photo.jpg");

        assertDoesNotThrow(() -> photoService.create(photo));
    }

    @Test
    void testPhotoWithNullUrl_ShouldThrowValidationException() {
        Object testObject = createTestObject("Test Object Null URL");
        Photo photo = createTestPhoto(testObject.getId(), 1);
        photo.setPhotoUrl(null);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> photoService.create(photo));

        assertEquals("Photo URL is required", exception.getMessage());
    }

    @Test
    void testPhotoWithEmptyUrl_ShouldThrowValidationException() {
        Object testObject = createTestObject("Test Object Empty URL");
        Photo photo = createTestPhoto(testObject.getId(), 1);
        photo.setPhotoUrl("");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> photoService.create(photo));

        assertEquals("Photo URL is required", exception.getMessage());
    }

    @Test
    void testPhotoWithLongCaption_ShouldThrowValidationException() {
        Object testObject = createTestObject("Test Object Long Caption");
        Photo photo = createTestPhoto(testObject.getId(), 1);
        photo.setCaption("A".repeat(256));

        ValidationException exception = assertThrows(ValidationException.class,
                () -> photoService.create(photo));

        assertTrue(exception.getMessage().contains("Caption exceeds maximum length of 255 characters"));
    }

    @Test
    void testPhotoWithNullCaption_ShouldSuccess() {
        Object testObject = createTestObject("Test Object Null Caption");
        Photo photo = createTestPhoto(testObject.getId(), 1);
        photo.setCaption(null);

        assertDoesNotThrow(() -> photoService.create(photo));
    }

    @Test
    void testPhotoWithNonExistentObject_ShouldThrowValidationException() {
        Photo photo = createTestPhoto(99999, 1);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> photoService.create(photo));

        assertTrue(exception.getMessage().contains("Object not found"));
    }

    @Test
    void testDeletePhoto_ShouldRemoveFromSystem() {
        Object testObject = createTestObject("Test Object for Delete");
        Photo photo = photoService.create(createTestPhoto(testObject.getId(), 1));
        int photoId = photo.getId();

        photoService.delete(photoId);
        Photo deleted = photoService.getById(photoId).orElse(null);

        assertNull(deleted);
    }

    @Test
    void testCreatePhoto_WithMaxDisplayOrder_ShouldSuccess() {
        Object testObject = createTestObject("Test Object Max Display");
        Photo photo = createTestPhoto(testObject.getId(), 1000);

        assertDoesNotThrow(() -> photoService.create(photo));
    }

    @Test
    void testCreatePhoto_WithExcessiveDisplayOrder_ShouldThrowValidationException() {
        Object testObject = createTestObject("Test Object Excessive Display");
        Photo photo = createTestPhoto(testObject.getId(), 1001);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> photoService.create(photo));

        assertTrue(exception.getMessage().contains("Display order must be between 1 and 1000"));
    }

    @Test
    void testCreatePhoto_WithDuplicateDisplayOrder_SameObject_ShouldThrowValidationException() {
        Object testObject = createTestObject("Test Object Duplicate Display");
        Photo photo1 = createTestPhoto(testObject.getId(), 1);
        photoService.create(photo1);

        Photo photo2 = createTestPhoto(testObject.getId(), 1); // Тот же objectId и displayOrder

        ValidationException exception = assertThrows(ValidationException.class,
                () -> photoService.create(photo2));

        assertTrue(exception.getMessage().contains("Display order 1 is already used"));
    }

    @Test
    void testCreatePhoto_WithSameDisplayOrder_DifferentObjects_ShouldSuccess() {
        Object testObject1 = createTestObject("Test Object 1 Same Display");
        Object testObject2 = createTestObject("Test Object 2 Same Display");

        Photo photo1 = createTestPhoto(testObject1.getId(), 1);
        Photo photo2 = createTestPhoto(testObject2.getId(), 1); // Тот же displayOrder, но разные objectId

        assertDoesNotThrow(() -> photoService.create(photo1));
        assertDoesNotThrow(() -> photoService.create(photo2));
    }

    @Test
    void testUpdatePhoto_ChangeObjectId_ShouldSuccess() {
        Object originalObject = createTestObject("Original Object");
        Object targetObject = createTestObject("Target Object");

        Photo photo = photoService.create(createTestPhoto(originalObject.getId(), 1));

        // Меняем objectId на другой объект
        photo.setObjectId(targetObject.getId());

        assertDoesNotThrow(() -> photoService.update(photo));

        Photo updated = photoService.getById(photo.getId()).orElse(null);
        assertNotNull(updated);
        assertEquals(targetObject.getId(), updated.getObjectId());
    }

    @Test
    void testUpdatePhoto_ChangeDisplayOrder_ToDuplicate_ShouldThrowValidationException() {
        Object testObject = createTestObject("Test Object Duplicate Update");

        Photo photo1 = photoService.create(createTestPhoto(testObject.getId(), 1));
        Photo photo2 = photoService.create(createTestPhoto(testObject.getId(), 2));

        // Пытаемся изменить displayOrder фото2 на тот, который уже используется фото1
        photo2.setDisplayOrder(1);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> photoService.update(photo2));

        assertTrue(exception.getMessage().contains("Display order 1 is already used"));
    }


    private Photo createTestPhoto(Integer objectId, Integer displayOrder) {
        Photo photo = new Photo();
        photo.setPhotoUrl("https://example.com/photo" + objectId + "_" + displayOrder + ".jpg");
        photo.setDisplayOrder(displayOrder);
        photo.setCaption("Property Photo for Object " + objectId);
        photo.setObjectId(objectId);
        return photo;
    }

    private Object createTestObject(String title) {
        Object obj = new Object();
        obj.setTitle(title + " " + System.currentTimeMillis());
        obj.setObjectType("Apartment");
        obj.setDealType("Sale");
        obj.setStatus("Available");
        obj.setPrice(new BigDecimal("150000.00"));
        return objectService.create(obj);
    }
}